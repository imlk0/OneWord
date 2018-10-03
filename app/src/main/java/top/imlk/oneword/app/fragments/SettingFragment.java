package top.imlk.oneword.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.imlk.oneword.BuildConfig;
import top.imlk.oneword.R;
import top.imlk.oneword.app.activity.AdjustStyleActivity;
import top.imlk.oneword.app.activity.AllApiActivity;
import top.imlk.oneword.app.activity.CustomWordActivity;
import top.imlk.oneword.app.viewmodels.MainActivityViewModel;
import top.imlk.oneword.util.AppStatus;
import top.imlk.oneword.util.BroadcastSender;
import top.imlk.oneword.util.SharedPreferencesUtil;
import top.imlk.oneword.util.ShowDialogUtil;


public class SettingFragment extends BottomFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @BindView(R.id.sw_open_auto_refresh)
    SwitchCompat swOpenAutoRefresh;
    @BindView(R.id.ll_open_auto_refresh)
    LinearLayout llOpenAutoRefresh;
    @BindView(R.id.sw_open_show_notification_oneword)
    SwitchCompat swOpenShowNotificationOneword;
    @BindView(R.id.ll_open_show_notification_oneword)
    LinearLayout llOpenShowNotificationOneword;
    @BindView(R.id.ll_set_refresh_mode)
    LinearLayout llSetRefreshMode;
    @BindView(R.id.ll_set_custom_oneword)
    LinearLayout llSetCustomOneword;
    @BindView(R.id.ll_set_oneword_show_form)
    LinearLayout llSetOnewordShowForm;
    @BindView(R.id.ll_edit_apis)
    LinearLayout llEditApis;
    @BindView(R.id.ll_show_about_app)
    LinearLayout llShowAboutApp;
    @BindView(R.id.ll_show_donate)
    LinearLayout llShowDonate;
    @BindView(R.id.ll_market)
    LinearLayout llMarket;
    @BindView(R.id.ll_qq_group)
    LinearLayout llQqGroup;
    @BindView(R.id.ll_show_thx_open_source)
    LinearLayout llShowThxOpenSource;
    @BindView(R.id.tv_version_code)
    TextView tvVersionCode;
    @BindView(R.id.ll_version)
    LinearLayout llVersion;


    private MainActivityViewModel mainActivityViewModel;

    public SettingFragment() {
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);

//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        ButterKnife.bind(this, rootView);

        swOpenAutoRefresh.setChecked(SharedPreferencesUtil.isAutoRefreshOpened(AppStatus.getRunningApplication()));
        swOpenShowNotificationOneword.setChecked(SharedPreferencesUtil.isShowNotificationOneWordOpened(AppStatus.getRunningApplication()));

        swOpenAutoRefresh.setOnCheckedChangeListener(this::onSwitchItemCheckedChanged);
        swOpenShowNotificationOneword.setOnCheckedChangeListener(this::onSwitchItemCheckedChanged);


        tvVersionCode.setText("VersionName:" + BuildConfig.VERSION_NAME + "\nVersionCode:" + BuildConfig.VERSION_CODE);

        SharedPreferencesUtil.registerChangeListener(getActivity(), SharedPreferencesUtil.SHARED_PER_AUTO_REFRESH_STATE, this);

        return rootView;
    }

    @Override
    public void onDestroyView() {

        SharedPreferencesUtil.unregisterChangeListener(getActivity(), SharedPreferencesUtil.SHARED_PER_AUTO_REFRESH_STATE, this);

        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @OnClick({R.id.ll_open_auto_refresh, R.id.ll_open_show_notification_oneword, R.id.ll_set_refresh_mode, R.id.ll_set_custom_oneword, R.id.ll_set_oneword_show_form, R.id.ll_edit_apis, R.id.ll_show_about_app, R.id.ll_show_donate, R.id.ll_market, R.id.ll_qq_group, R.id.ll_show_thx_open_source})
    public void onPreferenceItemClick(View v) {

        Intent intent;

        switch (v.getId()) {
            case R.id.ll_open_auto_refresh:
                swOpenAutoRefresh.setChecked(!swOpenAutoRefresh.isChecked());
                break;

            case R.id.ll_open_show_notification_oneword:
                swOpenShowNotificationOneword.setChecked(!swOpenShowNotificationOneword.isChecked());
                break;

            case R.id.ll_set_refresh_mode:

                ShowDialogUtil.showSelectRefreshModeDialog(getActivity());
                break;

            case R.id.ll_set_custom_oneword:
                getActivity().startActivity(new Intent(getActivity(), CustomWordActivity.class));

                break;

            case R.id.ll_set_oneword_show_form:

                intent = new Intent(getActivity(), AdjustStyleActivity.class);
                intent.putExtra(AdjustStyleActivity.ADJUST_SAMPLE_ONEWORD, mainActivityViewModel.getCurWordBean().getValue().clone());
                getActivity().startActivity(intent);

                break;

            case R.id.ll_edit_apis:
                getActivity().startActivity(new Intent(getActivity(), AllApiActivity.class));
                break;

            case R.id.ll_show_about_app:
                ShowDialogUtil.showAboutAppDialog(getActivity());
                break;

            case R.id.ll_show_donate:
                ShowDialogUtil.showDonateDialog(getActivity());
                break;
            case R.id.ll_market:
                getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)));
                break;
            case R.id.ll_qq_group:
                getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3a%2f%2fqm.qq.com%2fcgi-bin%2fqm%2fqr%3fk%3dAMtSN8HJ1Q7dXezumRs9DK-AVfOiOpvr")));
                break;
            case R.id.ll_show_thx_open_source:
                ShowDialogUtil.showOpenSourceProjectDialog(getActivity());
                break;
        }

    }


    public void onSwitchItemCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sw_open_auto_refresh:
                SharedPreferencesUtil.setAutoRefreshOpened(AppStatus.getRunningApplication(), isChecked);
                if (isChecked) {
                    BroadcastSender.startAutoRefresh(AppStatus.getRunningApplication());
                    Toast.makeText(getActivity(), "请在 黑阈/绿守 中放过我的应用\n通知用于保活，可在通知栏长按通知来隐藏通知", Toast.LENGTH_LONG).show();
                } else {
                    BroadcastSender.stopAutoRefresh(AppStatus.getRunningApplication());
                }
                break;

            case R.id.sw_open_show_notification_oneword:
                SharedPreferencesUtil.setShowNotificationOneWordOpened(AppStatus.getRunningApplication(), isChecked);
                if (isChecked) {
                    Toast.makeText(getActivity(), "若通知无法显示，请注意：\n是否曾在系统设置中屏蔽通知\n排除内存清理软件", Toast.LENGTH_LONG).show();

                    BroadcastSender.startShowNitificationOneword(AppStatus.getRunningApplication());
                } else {
                    BroadcastSender.stopShowNitificationOneword(AppStatus.getRunningApplication());
                }
                break;
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (swOpenAutoRefresh != null) {
            if (SharedPreferencesUtil.SHARED_PER_KEY_IS_REFRESH_OPENED.equals(key)) {
                swOpenAutoRefresh.setChecked(sharedPreferences.getBoolean(key, false));
            }
        }
    }


}
