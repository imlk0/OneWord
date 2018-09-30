package top.imlk.oneword.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Parcel;

import top.imlk.oneword.BuildConfig;
import top.imlk.oneword.app.application.OneWordApplication;
import top.imlk.oneword.app.service.OneWordAutoRefreshService;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.bean.WordViewConfig;


/**
 * Created by imlk on 2018/5/26.
 */
public class BroadcastSender {

    public static final String CMD_BROADCAST_SET_NEW_WORDBEAN = BuildConfig.APPLICATION_ID + ".CMD_BROADCAST_SET_NEW_WORDBEAN";
    public static final String THE_NEW_WORDBEAN = BuildConfig.APPLICATION_ID + ".THE_NEW_WORDBEAN";
    //    public static final String THE_NEW_LOCK_SCREEN_INFO_FROM = BuildConfig.APPLICATION_ID+".THE_NEW_LOCK_SCREEN_INFO_FROM";
//    public static final String THE_NEW_LOCK_SCREEN_INFO_MSG = BuildConfig.APPLICATION_ID+".THE_NEW_LOCK_SCREEN_INFO_MSG";

    public static final String CMD_BROADCAST_RELOAD_WORDBEAN = BuildConfig.APPLICATION_ID + ".CMD_BROADCAST_RELOAD_WORDBEAN";


    public static final String CMD_BROADCAST_SET_NEW_WORDVIEWCONFIG = BuildConfig.APPLICATION_ID + ".CMD_BROADCAST_SET_NEW_WORDVIEWCONFIG";
    public static final String THE_NEW_WORDVIEWCONFIG = BuildConfig.APPLICATION_ID + ".THE_NEW_WORDVIEWCONFIG";
    //    public static final String THE_NEW_LOCK_SCREEN_INFO_FROM = BuildConfig.APPLICATION_ID+".THE_NEW_LOCK_SCREEN_INFO_FROM";
//    public static final String THE_NEW_LOCK_SCREEN_INFO_MSG = BuildConfig.APPLICATION_ID+".THE_NEW_LOCK_SCREEN_INFO_MSG";

    public static final String CMD_BROADCAST_RELOAD_WORDVIEWCONFIG = BuildConfig.APPLICATION_ID + ".CMD_BROADCAST_RELOAD_WORDVIEWCONFIG";


    public static final String CMD_SERVICES_START_AUTO_REFRESH = BuildConfig.APPLICATION_ID + ".CMD_SERVICES_START_AUTO_REFRESH";
    public static final String CMD_SERVICES_PAUSE_AUTO_REFRESH = BuildConfig.APPLICATION_ID + ".CMD_SERVICES_PAUSE_AUTO_REFRESH";
    public static final String CMD_SERVICES_STOP_AUTO_REFRESH = BuildConfig.APPLICATION_ID + ".CMD_SERVICES_STOP_AUTO_REFRESH";
    public static final String THE_REFRESH_MODE = BuildConfig.APPLICATION_ID + ".THE_REFRESH_MODE";


    public static final String CMD_SERVICES_START_SHOW_NOTIFICATION_ONEWORD = BuildConfig.APPLICATION_ID + ".CMD_SERVICES_START_SHOW_NOTIFICATION_ONEWORD";
    public static final String CMD_SERVICES_STOP_SHOW_NOTIFICATION_ONEWORD = BuildConfig.APPLICATION_ID + ".CMD_SERVICES_STOP_SHOW_NOTIFICATION_ONEWORD";
    public static final String THE_INIT_WORDBEAN = BuildConfig.APPLICATION_ID + ".THE_INIT_WORDBEAN";


    public static final String CMD_SERVICES_SWITCH_TO_NEXT_ONEWORD = BuildConfig.APPLICATION_ID + ".CMD_SERVICES_SWITCH_TO_NEXT_ONEWORD";


    //    public static final String CMD_INTENT_START_MAIN_ACTICITY_WHEN_CLICKED = BuildConfig.APPLICATION_ID+".CMD_INTENT_START_MAIN_ACTICITY_WHEN_CLICKED";
    public static final String THE_CLICKED_WORDBEAN = BuildConfig.APPLICATION_ID + ".THE_CLICKED_WORDBEAN";


    public static final String ONEWORD_APP_VERSION_CODE = BuildConfig.APPLICATION_ID + ".ONEWORD_APP_VERSION_CODE";
    public static final String IS_IN_MAIN_PROCESS = BuildConfig.APPLICATION_ID + ".IS_IN_MAIN_PROCESS";
    public static final String IS_IN_MODULE = BuildConfig.APPLICATION_ID + ".IS_IN_MODULE";


    private BroadcastSender() {

    }

    public static void sendUseNewOneWordBroadcast(Context context, WordBean wordBean) {
        if (wordBean != null) {

            Intent intent = new MyIntent();
            intent.setAction(CMD_BROADCAST_SET_NEW_WORDBEAN);

            intent.putExtra(THE_NEW_WORDBEAN, wordBean);

            context.sendBroadcast(intent);
        }
    }


    public static void sendReloadOneWordBySelfBroadcast(Context context) {
        Intent intent = new MyIntent();
        intent.setAction(CMD_BROADCAST_RELOAD_WORDBEAN);

        context.sendBroadcast(intent);
    }


    public static void sendUseNewWordViewConfigBroadcast(Context context, WordViewConfig config) {
        if (config != null) {

            Intent intent = new MyIntent();
            intent.setAction(CMD_BROADCAST_SET_NEW_WORDVIEWCONFIG);

            intent.putExtra(THE_NEW_WORDVIEWCONFIG, config);

            context.sendBroadcast(intent);
        }
    }

    public static void sendReloadWordViewConfigBySelfBroadcast(Context context) {
        Intent intent = new MyIntent();
        intent.setAction(CMD_BROADCAST_RELOAD_WORDVIEWCONFIG);

        context.sendBroadcast(intent);
    }


    public static void startAutoRefresh(Context context) {
        Intent intent = new MyIntent(context, OneWordAutoRefreshService.class);
        intent.setAction(CMD_SERVICES_START_AUTO_REFRESH);
        intent.putExtra(THE_REFRESH_MODE, SharedPreferencesUtil.getRefreshMode(context));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public static void stopAutoRefresh(Context context) {

        Intent intent = new MyIntent(context, OneWordAutoRefreshService.class);
        intent.setAction(CMD_SERVICES_STOP_AUTO_REFRESH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }


//    public static void pauseAutoRefresh(Context context) {

    //    }
//        context.startService(intent);
//
//        intent.setAction(CMD_SERVICES_PAUSE_AUTO_REFRESH);
//        Intent intent = new MyIntent(context, OneWordAutoRefreshService.class);
//

    public static void startShowNitificationOneword(Context context) {
        Intent intent = new MyIntent(context, OneWordAutoRefreshService.class);
        intent.setAction(CMD_SERVICES_START_SHOW_NOTIFICATION_ONEWORD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public static void stopShowNitificationOneword(Context context) {

        Intent intent = new MyIntent(context, OneWordAutoRefreshService.class);
        intent.setAction(CMD_SERVICES_STOP_SHOW_NOTIFICATION_ONEWORD);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }


    public static void startMainActivityWhenClicked(Context context, WordBean wordBean) {

        Intent intent = context.getPackageManager().getLaunchIntentForPackage(BuildConfig.APPLICATION_ID);
        if (intent != null) {

            intent.putExtra(THE_CLICKED_WORDBEAN, wordBean);

            context.startActivity(intent);
        }

    }

    public static void switchToNextOnewordManually(Context context) {

        Intent intent = new MyIntent();
        intent.setComponent(new ComponentName(BuildConfig.APPLICATION_ID, OneWordAutoRefreshService.class.getName()));
        intent.setAction(CMD_SERVICES_SWITCH_TO_NEXT_ONEWORD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }

    }


    private static class MyIntent extends Intent {

        private void injectExtra() {
            putExtra(ONEWORD_APP_VERSION_CODE, BuildConfig.VERSION_CODE);
            putExtra(IS_IN_MODULE, AppStatus.getRunningApplication() instanceof OneWordApplication);
            putExtra(IS_IN_MAIN_PROCESS, AppStatus.isMianProcess());
        }

        public MyIntent() {
            super();
            injectExtra();
        }

        public MyIntent(Intent o) {
            super(o);
            injectExtra();
        }

        public MyIntent(String action) {
            super(action);
            injectExtra();
        }

        public MyIntent(String action, Uri uri) {
            super(action, uri);
            injectExtra();
        }

        public MyIntent(Context packageContext, Class<?> cls) {
            super(packageContext, cls);
            injectExtra();
        }

        public MyIntent(String action, Uri uri, Context packageContext, Class<?> cls) {
            super(action, uri, packageContext, cls);
            injectExtra();
        }

    }

}
