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

import top.imlk.oneword.R;
import top.imlk.oneword.util.ApplicationInfoUtil;
import top.imlk.oneword.util.SharedPreferencesUtil;
import top.imlk.oneword.util.ShowDialogUtil;

/**
 * Created by imlk on 2018/5/30.
 */
public class SettingPage extends LinearLayout implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Context context;

    public SwitchCompat swOpenAutoRefresh;
    public LinearLayout llOpenAutoRefresh;
    public LinearLayout llSetRefreshMode;
    public LinearLayout llSetOneWordType;
    public LinearLayout llShowAboutApp;
    public LinearLayout llShowDonate;
    public LinearLayout llCoolapkMarket;
    public LinearLayout llShowThxHitokoto;
    public LinearLayout llShowThxOpenSource;
    public LinearLayout llVersion;


    public SettingPage(Context context) {
        super(context);
        updateContext(context);
    }

    public SettingPage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        updateContext(context);
    }

    public SettingPage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        updateContext(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SettingPage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        updateContext(context);
    }


    public void updateContext(Context context) {
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        swOpenAutoRefresh = findViewById(R.id.sw_open_auto_refresh);
        swOpenAutoRefresh.setOnCheckedChangeListener(this);
        swOpenAutoRefresh.setChecked(SharedPreferencesUtil.isRefreshOpened(context));
        llOpenAutoRefresh = findViewById(R.id.ll_open_auto_refresh);
        llOpenAutoRefresh.setOnClickListener(this);

        llSetRefreshMode = findViewById(R.id.ll_set_refresh_mode);
        llSetRefreshMode.setOnClickListener(this);

        llSetOneWordType = findViewById(R.id.ll_set_oneword_type);
        llSetOneWordType.setOnClickListener(this);

        llShowAboutApp = findViewById(R.id.ll_show_about_app);
        llShowAboutApp.setOnClickListener(this);

        llShowDonate = findViewById(R.id.ll_show_donate);
        llShowDonate.setOnClickListener(this);

        llCoolapkMarket = findViewById(R.id.ll_coolapk_market);
        llCoolapkMarket.setOnClickListener(this);

        llShowThxHitokoto = findViewById(R.id.ll_show_thx_hitokoto);
        llShowThxHitokoto.setOnClickListener(this);

        llShowThxOpenSource = findViewById(R.id.ll_show_thx_open_source);
        llShowThxOpenSource.setOnClickListener(this);

        llVersion = findViewById(R.id.ll_version);
//        llVersion.setOnClickListener(this);

        ((TextView) llVersion.findViewById(R.id.tv_version_code)).setText(ApplicationInfoUtil.getAppVersionName(context) + "[" + ApplicationInfoUtil.getAppVersionCode(context) + "]");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_open_auto_refresh:
                swOpenAutoRefresh.setChecked(!swOpenAutoRefresh.isChecked());
                break;

            case R.id.ll_set_refresh_mode:

                ShowDialogUtil.showSelectRefreshModeDialog(context);
                break;


            case R.id.ll_set_oneword_type:

                //TODO API编辑
                ShowDialogUtil.showSelectOneWordTypeDialog(context);
                break;

            case R.id.ll_show_about_app:
                ShowDialogUtil.showAboutAppDialog(context);
                break;

            case R.id.ll_show_donate:
                ShowDialogUtil.showDonateDialog(context);
                break;
            case R.id.ll_coolapk_market:
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.coolapk.com/apk/top.imlk.oneword")));
                break;
            case R.id.ll_show_thx_hitokoto:
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://hitokoto.cn/about")));
                break;
            case R.id.ll_show_thx_open_source:
                ShowDialogUtil.showOpenSourceProjectDialog(context);
                break;


        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sw_open_auto_refresh:
                SharedPreferencesUtil.setAutoRefreshOpened(context, isChecked);
                break;
        }
    }

}
