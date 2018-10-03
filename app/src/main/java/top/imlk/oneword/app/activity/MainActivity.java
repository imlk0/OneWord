package top.imlk.oneword.app.activity;

import android.app.KeyguardManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import top.imlk.oneword.R;
import top.imlk.oneword.app.fragments.SettingFragment;
import top.imlk.oneword.app.fragments.WordListFragment;
import top.imlk.oneword.app.fragments.WordViewFragment;
import top.imlk.oneword.app.viewmodels.MainActivityViewModel;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.dao.OneWordDBHelper;
import top.imlk.oneword.net.OneWordApi;
import top.imlk.oneword.util.AppStyleHelper;
import top.imlk.oneword.util.BroadcastSender;
import top.imlk.oneword.util.SharedPreferencesUtil;


public class MainActivity extends BaseActivity implements OnRefreshListener {


    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.bnv_main)
    BottomNavigationView bnvMain;
    @BindView(R.id.ll_fg_top)
    LinearLayout llFgTop;
    @BindView(R.id.ll_fg_bottom)
    LinearLayout llFgBottom;
    @BindView(R.id.ll_main_container)
    LinearLayout llMainContainer;


    Fragment wordViewFragment = WordViewFragment.newInstance();
    Fragment historyFragment = WordListFragment.newInstance(WordListFragment.PageType.HISTORY_PAGE);
    Fragment favorFragment = WordListFragment.newInstance(WordListFragment.PageType.FAVOR_PAGE);
    Fragment settingFragment = SettingFragment.newInstance();

    private MainActivityViewModel mainActivityViewModel;

    public enum FragmentName {
        WordViewFragment,
        HistoryFragment,
        FavorFragment,
        SettingFragment;
    }

    @BindColor(R.color.color_bnv_item)
    ColorStateList normalBnvItemColor;
    @BindColor(R.color.color_bnv_item_same)
    ColorStateList sameBnvItemColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fixServiceWhenStartUp();

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);


        SharedPreferencesUtil.onMainActivityCreate(this);


        this.refreshLayout.setOnRefreshListener(this);
        this.refreshLayout.setEnableLoadMore(false);

        refreshLayout.setPrimaryColors(
                AppStyleHelper.getColorByAttributeId(this, R.attr.color_primary_light),
                AppStyleHelper.getColorByAttributeId(this, R.attr.color_primary_dark));


        mainActivityViewModel.getCurFragmentName().observe(this, (name) -> {
            TransitionManager.beginDelayedTransition(llMainContainer,
                    new TransitionSet().addTransition(new Fade(Fade.OUT)).
                            addTransition(new ChangeBounds()).
                            addTransition(new Slide(Gravity.BOTTOM)
                                    .addTarget(llFgBottom))
                            .addTransition(new Fade(Fade.IN)
                                    .excludeTarget(llFgBottom, true))
                            .excludeChildren(bnvMain, true)
                            .excludeChildren(llFgTop, true)
            );

            switch (name) {
                case WordViewFragment:
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    if (getSupportFragmentManager().findFragmentById(R.id.ll_fg_top) == null) {
                        ft.add(R.id.ll_fg_top, wordViewFragment);
                    }
                    Fragment btmFragment = getSupportFragmentManager().findFragmentById(R.id.ll_fg_bottom);
                    if (btmFragment != null) {
                        ft.remove(btmFragment);
                    }
                    ft.commit();

                    bnvMain.setItemIconTintList(sameBnvItemColor);
                    bnvMain.setItemTextColor(sameBnvItemColor);


                    llFgTop.setVisibility(View.VISIBLE);
                    llFgBottom.setVisibility(View.GONE);
                    refreshLayout.setEnableRefresh(true);
                    break;
                case HistoryFragment:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.ll_fg_bottom, historyFragment).commit();

                    bnvMain.setItemIconTintList(normalBnvItemColor);
                    bnvMain.setItemTextColor(normalBnvItemColor);

                    llFgTop.setVisibility(View.GONE);
                    llFgBottom.setVisibility(View.VISIBLE);
                    refreshLayout.setEnableRefresh(false);
                    break;
                case FavorFragment:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.ll_fg_bottom, favorFragment).commit();

                    bnvMain.setItemIconTintList(normalBnvItemColor);
                    bnvMain.setItemTextColor(normalBnvItemColor);

                    llFgTop.setVisibility(View.GONE);
                    llFgBottom.setVisibility(View.VISIBLE);
                    refreshLayout.setEnableRefresh(false);
                    break;
                case SettingFragment:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.ll_fg_bottom, settingFragment).commit();

                    bnvMain.setItemIconTintList(normalBnvItemColor);
                    bnvMain.setItemTextColor(normalBnvItemColor);

                    llFgTop.setVisibility(View.GONE);
                    llFgBottom.setVisibility(View.VISIBLE);
                    refreshLayout.setEnableRefresh(false);
                    break;
            }


        });

        bnvMain.setItemHorizontalTranslationEnabled(false);
        bnvMain.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);

        bnvMain.setOnNavigationItemSelectedListener((menuItem) -> {


            switch (menuItem.getItemId()) {
                case R.id.item_history:
                    if (FragmentName.HistoryFragment == mainActivityViewModel.getCurFragmentName().getValue()) {
                        mainActivityViewModel.setCurFragmentName(FragmentName.WordViewFragment);
                    } else {
                        mainActivityViewModel.setCurFragmentName(FragmentName.HistoryFragment);
                    }
                    return true;
                case R.id.item_favor:
                    if (FragmentName.FavorFragment == mainActivityViewModel.getCurFragmentName().getValue()) {
                        mainActivityViewModel.setCurFragmentName(FragmentName.WordViewFragment);
                    } else {
                        mainActivityViewModel.setCurFragmentName(FragmentName.FavorFragment);
                    }
                    return true;
                case R.id.item_next:
                    performRefreshManually();
                    return true;
                case R.id.item_setting:
                    if (FragmentName.SettingFragment == mainActivityViewModel.getCurFragmentName().getValue()) {
                        mainActivityViewModel.setCurFragmentName(FragmentName.WordViewFragment);
                    } else {
                        mainActivityViewModel.setCurFragmentName(FragmentName.SettingFragment);
                    }
                    return true;
            }

            return false;
        });


        mainActivityViewModel.setCurFragmentName(FragmentName.WordViewFragment);

        initWordBean();

    }

    private void initWordBean() {
        WordBean wordBean;
        wordBean = resolveIntent(getIntent());

        if (wordBean == null) {
            wordBean = OneWordDBHelper.queryOneWordFromHistoryByDESC();
        }

        if (wordBean == null) {
            wordBean = new WordBean("当前一言", "出处");
        }

        mainActivityViewModel.setCurWordBean(wordBean);

    }


    private WordBean resolveIntent(Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return null;
        }
        switch (intent.getAction()) {
            case Intent.ACTION_MAIN:
                WordBean wordBean = intent.getParcelableExtra(BroadcastSender.THE_CLICKED_WORDBEAN);
                if (wordBean != null) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        KeyguardManager keyguardManager = (KeyguardManager) this.getSystemService(KEYGUARD_SERVICE);
                        keyguardManager.requestDismissKeyguard(this, null);
                    } else {
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
                    }

                    return wordBean;
                }
                break;
        }

        return null;
    }

    @Override
    public void onBackPressed() {

        if (mainActivityViewModel.getCurFragmentName().getValue() != null && mainActivityViewModel.getCurFragmentName().getValue() != FragmentName.WordViewFragment) {
            mainActivityViewModel.setCurFragmentName(FragmentName.WordViewFragment);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onDestroy() {
        OneWordDBHelper.closeDataBase();

        super.onDestroy();
    }


    public void fixServiceWhenStartUp() {

        if (SharedPreferencesUtil.isAutoRefreshOpened(this)) {
            BroadcastSender.startAutoRefresh(this);
        }
        if (SharedPreferencesUtil.isShowNotificationOneWordOpened(this)) {
            BroadcastSender.startShowNitificationOneword(this);
        }

//        Intent intent = new Intent(this, OneWordAutoRefreshService.class);
//        this.stopService(intent);

    }


    public void performRefreshManually() {
        mainActivityViewModel.setCurFragmentName(FragmentName.WordViewFragment);
        this.refreshLayout.autoRefresh(0, 200, 1);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

        mainActivityViewModel.getResponseStatus().observe(this, (status) -> {
            if (status) {
                refreshLayout.finishRefresh(300, true);
            } else {
                refreshLayout.finishRefresh(0, false);
            }
        });

        mainActivityViewModel.startAnOneWordRequest();

    }


}
