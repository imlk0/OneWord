package top.imlk.oneword.application.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import top.imlk.oneword.BuildConfig;
import top.imlk.oneword.R;
import top.imlk.oneword.application.client.activity.AdjustStyleActivity;
import top.imlk.oneword.application.client.activity.AllApiActivity;
import top.imlk.oneword.application.client.activity.MainActivity;
import top.imlk.oneword.util.SharedPreferencesUtil;
import top.imlk.oneword.util.ShowDialogUtil;

/**
 * Created by imlk on 2018/5/30.
 */
public class SettingPage extends android.support.v4.widget.NestedScrollView implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private MainActivity mainActivity;

    public SwitchCompat swOpenAutoRefresh;
    public LinearLayout llOpenAutoRefresh;
    public LinearLayout llSetRefreshMode;
    public LinearLayout llSetOneWordShowForm;
    public LinearLayout llEditOneWordApi;


    public LinearLayout llShowDonate;
    public LinearLayout llShowAboutApp;
    public LinearLayout llCoolapkMarket;
    public LinearLayout llShowThxOpenSource;
    public LinearLayout llVersion;


    public SettingPage(Context context) {
        super(context);
    }

    public SettingPage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SettingPage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void init(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        swOpenAutoRefresh = findViewById(R.id.sw_open_auto_refresh);
        swOpenAutoRefresh.setOnCheckedChangeListener(this);
        swOpenAutoRefresh.setChecked(SharedPreferencesUtil.isRefreshOpened(mainActivity));
        llOpenAutoRefresh = findViewById(R.id.ll_open_auto_refresh);
        llOpenAutoRefresh.setOnClickListener(this);

        llSetRefreshMode = findViewById(R.id.ll_set_refresh_mode);
        llSetRefreshMode.setOnClickListener(this);

        llSetOneWordShowForm = findViewById(R.id.ll_set_oneword_show_form);
        llSetOneWordShowForm.setOnClickListener(this);


        llEditOneWordApi = findViewById(R.id.ll_edit_apis);
        llEditOneWordApi.setOnClickListener(this);


        llShowAboutApp = findViewById(R.id.ll_show_about_app);
        llShowAboutApp.setOnClickListener(this);

        llShowDonate = findViewById(R.id.ll_show_donate);
        llShowDonate.setOnClickListener(this);

        llCoolapkMarket = findViewById(R.id.ll_coolapk_market);
        llCoolapkMarket.setOnClickListener(this);


        llShowThxOpenSource = findViewById(R.id.ll_show_thx_open_source);
        llShowThxOpenSource.setOnClickListener(this);

        llVersion = findViewById(R.id.ll_version);
//        llVersion.setOnClickListener(this);

        ((TextView) llVersion.findViewById(R.id.tv_version_code)).setText("VersionName:" + BuildConfig.VERSION_NAME + "\nVersionCode:" + BuildConfig.VERSION_CODE);

    }


    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.ll_open_auto_refresh:
                swOpenAutoRefresh.setChecked(!swOpenAutoRefresh.isChecked());
                break;

            case R.id.ll_set_refresh_mode:

                ShowDialogUtil.showSelectRefreshModeDialog(mainActivity);
                break;


            case R.id.ll_set_oneword_show_form:

                intent = new Intent(mainActivity, AdjustStyleActivity.class);
                intent.putExtra(AdjustStyleActivity.ADJUST_SAMPLE_ONEWORD, mainActivity.getCurWordBeanCopy());
                mainActivity.startActivity(intent);

                break;

            case R.id.ll_edit_apis:
                //TODO API编辑
                mainActivity.startActivity(new Intent(mainActivity, AllApiActivity.class));
                break;
            //TODO 手动编写句子

            case R.id.ll_show_about_app:
                ShowDialogUtil.showAboutAppDialog(mainActivity);
                break;

            case R.id.ll_show_donate:
                ShowDialogUtil.showDonateDialog(mainActivity);
                break;
            case R.id.ll_coolapk_market:
                mainActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.coolapk.com/apk/top.imlk.oneword")));
                break;
            case R.id.ll_show_thx_open_source:
                ShowDialogUtil.showOpenSourceProjectDialog(mainActivity);
                break;


        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sw_open_auto_refresh:
                SharedPreferencesUtil.setAutoRefreshOpened(mainActivity, isChecked);
                break;
        }
    }

}
