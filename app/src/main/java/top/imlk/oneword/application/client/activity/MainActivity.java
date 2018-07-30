package top.imlk.oneword.application.client.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import top.imlk.oneword.application.client.service.OneWordAutoRefreshService;
import top.imlk.oneword.net.Hitokoto.HitokotoApi;
import top.imlk.oneword.R;
import top.imlk.oneword.net.Hitokoto.HitokotoBean;
import top.imlk.oneword.dao.OneWordSQLiteOpenHelper;
import top.imlk.oneword.util.SharedPreferencesUtil;
import top.imlk.oneword.util.ShowDialogUtil;
import top.imlk.oneword.util.StyleHelper;
import top.imlk.oneword.application.view.MainOneWordView;
import top.imlk.oneword.application.view.OneWordShowPanel;
import top.imlk.oneword.application.view.PastedNestedScrollView;

import static top.imlk.oneword.StaticValue.CMD_SERVICES_START_AUTO_REFRESH_SERVICE;

public class MainActivity extends AppCompatActivity implements Observer<HitokotoBean>, OnRefreshListener {


    public PastedNestedScrollView pastedNestedScrollView;

    public MainOneWordView mainOneWordView;

    public OneWordShowPanel oneWordShowPanel;

    public RefreshLayout refreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        killRunningServices();

        int themes[] = {
                R.style.RedTheme,
                R.style.BlueTheme,
                R.style.BlueGreyTheme,
                R.style.BrownTheme,
                R.style.CyanTheme,
                R.style.DeepOrangeTheme,
                R.style.DeepPurpleTheme,
                R.style.GreenTheme,
                R.style.GreyTheme,
                R.style.IndigoTheme,
                R.style.LightBlueTheme,
                R.style.LimeTheme,
                R.style.OrangeTheme,
                R.style.PinkTheme,
                R.style.PurpleTheme,
                R.style.TealTheme,
                R.style.YellowTheme,
        };

        setTheme(themes[((int) (Math.random() * themes.length))]);


        setContentView(R.layout.activity_main);

        this.pastedNestedScrollView = findViewById(R.id.pasted_scroll_view);

        this.mainOneWordView = findViewById(R.id.ll_main_oneword);
        this.mainOneWordView.updateContext(this);

        this.oneWordShowPanel = findViewById(R.id.one_word_show_panel);
        this.oneWordShowPanel.updateContext(this);
        this.oneWordShowPanel.initView();

        this.refreshLayout = findViewById(R.id.refreshLayout);
        this.refreshLayout.setOnRefreshListener(this);
        this.refreshLayout.setEnableLoadMore(false);

        refreshLayout.setPrimaryColors(
                StyleHelper.getColorByAttributeId(this, R.attr.primary_light),
                StyleHelper.getColorByAttributeId(this, R.attr.colorPrimaryDark));

        //更新c_Array_custom数组
        if (HitokotoApi.Parameter.c_Array_custom == null) {
            HitokotoApi.refreshCustomArray(SharedPreferencesUtil.readOneWordTypes(this));
        }

        if (SharedPreferencesUtil.isFirstTimeUse(this)) {


            ShowDialogUtil.showAboutAppDialog(this);


        }

//        startAutoUpdateService();
    }


    public Rect getAreaView() {
        Rect outRect = new Rect();
        getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
        return outRect;
    }

    public void gotoPage(int index) {

        if (this.mainOneWordView.viewPager.getCurrentItem() == index && this.pastedNestedScrollView.canScroll) {
            this.pastedNestedScrollView.scrollToTop();
        } else {
            this.pastedNestedScrollView.scrollToBottom();
            this.mainOneWordView.gotoPage(index);
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        upDateLP();

    }


    //implement from Observer

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(HitokotoBean hitokotoBean) {


        this.oneWordShowPanel.updateStateByBean(hitokotoBean);

        OneWordSQLiteOpenHelper.getInstance(this).insert_one_item(OneWordSQLiteOpenHelper.TABLE_HISTORY, hitokotoBean);

        refreshLayout.finishRefresh(300, true);

    }

    @Override
    public void onError(Throwable e) {
        Toast.makeText(MainActivity.this, "网络异常，获取失败", Toast.LENGTH_LONG).show();
        refreshLayout.finishRefresh(0, false);

    }

    @Override
    public void onComplete() {

    }

    public void upDateLP() {
        if (this.mainOneWordView != null) {
            mainOneWordView.upDateLP(getAreaView());
        }
    }

    @Override
    protected void onDestroy() {
        if (!OneWordSQLiteOpenHelper.isDataBaseClosed()) {
            OneWordSQLiteOpenHelper.getInstance(this).close();
        }

        if (SharedPreferencesUtil.isRefreshOpened(this)) {
            startAutoUpdateService();
        }
        super.onDestroy();
        System.exit(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!this.pastedNestedScrollView.isOnTop) {
                this.pastedNestedScrollView.scrollToTop();
            } else {
                finish();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    public void killRunningServices() {
//        if(SharedPreferencesUtil.isRefreshOpened(this)){
//
//            Intent intent = new Intent(this, OneWordAutoRefreshService.class);
//
//            intent.setAction(CMD_SERVICES_STOP_SERVICE);
//            startService(intent);
//    }

        Intent intent = new Intent(this, OneWordAutoRefreshService.class);
        this.stopService(intent);

    }


    public void startAutoUpdateService() {
        Intent intent = new Intent(this, OneWordAutoRefreshService.class);

        intent.setAction(CMD_SERVICES_START_AUTO_REFRESH_SERVICE);
        startService(intent);
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

        startAnOneWordRequest();

    }

    public void startAnOneWordRequest() {
        try {
            HitokotoApi.requestOneWord(this);
            this.pastedNestedScrollView.scrollToTop();
        } catch (Exception e) {
            Toast.makeText(this, "发生错误:\n" + e.getMessage(), Toast.LENGTH_LONG).show();
            refreshLayout.finishRefresh(500, false);
        }
    }

}
