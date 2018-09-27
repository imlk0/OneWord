package top.imlk.oneword.util;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import top.imlk.oneword.BuildConfig;
import top.imlk.oneword.application.client.service.OneWordAutoRefreshService;
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


    //    public static final String CMD_INTENT_START_MAIN_ACTICITY_WHEN_CLICKED = BuildConfig.APPLICATION_ID+".CMD_INTENT_START_MAIN_ACTICITY_WHEN_CLICKED";
    public static final String THE_CLICKED_WORDBEAN = BuildConfig.APPLICATION_ID + ".THE_CLICKED_WORDBEAN";


    public static final String APPLICATION_PORT_VERSION_CODE = BuildConfig.APPLICATION_ID + ".APPLICATION_PORT_VERSION_CODE";
    public static final String SEND_BY_SUER = BuildConfig.APPLICATION_ID + ".SEND_BY_SUER";


    private BroadcastSender() {

    }

    public static void sendUseNewOneWordBroadcast(Context context, WordBean wordBean) {
        if (wordBean != null) {

            Intent intent = new Intent();
            intent.setAction(CMD_BROADCAST_SET_NEW_WORDBEAN);

            intent.putExtra(THE_NEW_WORDBEAN, wordBean);

            sendBroadCastAspect(context, intent);
        }
    }


    public static void sendReloadOneWordBySelfBroadcast(Context context) {
        Intent intent = new Intent();
        intent.setAction(CMD_BROADCAST_RELOAD_WORDBEAN);

        sendBroadCastAspect(context, intent);
    }


    public static void sendUseNewWordViewConfigBroadcast(Context context, WordViewConfig config) {
        if (config != null) {

            Intent intent = new Intent();
            intent.setAction(CMD_BROADCAST_SET_NEW_WORDVIEWCONFIG);

            intent.putExtra(THE_NEW_WORDVIEWCONFIG, config);

            sendBroadCastAspect(context, intent);
        }
    }

    public static void sendReloadWordViewConfigBySelfBroadcast(Context context) {
        Intent intent = new Intent();
        intent.setAction(CMD_BROADCAST_RELOAD_WORDVIEWCONFIG);

        sendBroadCastAspect(context, intent);
    }


    public static void startAutoRefresh(Context context) {
        Intent intent = new Intent(context, OneWordAutoRefreshService.class);
        intent.setAction(BroadcastSender.CMD_SERVICES_START_AUTO_REFRESH);
        intent.putExtra(THE_REFRESH_MODE, SharedPreferencesUtil.getRefreshMode(context));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public static void sendBroadCastAspect(Context context, Intent intent) {
        intent.putExtra(APPLICATION_PORT_VERSION_CODE, BuildConfig.VERSION_CODE);
        intent.putExtra(SEND_BY_SUER, AppStatus.isMianProcess());
        context.sendBroadcast(intent);
    }

    public static void stopAutoRefresh(Context context) {

        Intent intent = new Intent(context, OneWordAutoRefreshService.class);
        intent.setAction(BroadcastSender.CMD_SERVICES_STOP_AUTO_REFRESH);

        context.startService(intent);
    }

//    public static void pauseAutoRefresh(Context context) {
//
//        Intent intent = new Intent(context, OneWordAutoRefreshService.class);
//        intent.setAction(BroadcastSender.CMD_SERVICES_PAUSE_AUTO_REFRESH);
//
//        context.startService(intent);
//    }


    public static void startShowNitificationOneword(Context context) {
        Intent intent = new Intent(context, OneWordAutoRefreshService.class);
        intent.setAction(BroadcastSender.CMD_SERVICES_START_SHOW_NOTIFICATION_ONEWORD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public static void stopShowNitificationOneword(Context context) {

        Intent intent = new Intent(context, OneWordAutoRefreshService.class);
        intent.setAction(BroadcastSender.CMD_SERVICES_STOP_SHOW_NOTIFICATION_ONEWORD);


        context.startService(intent);

    }


    public static void startMainActivityWhenClicked(Context context, WordBean wordBean) {

        Intent intent = context.getPackageManager().getLaunchIntentForPackage(BuildConfig.APPLICATION_ID);
        if (intent != null) {

            intent.putExtra(BroadcastSender.THE_CLICKED_WORDBEAN, wordBean);

            context.startActivity(intent);
        }
    }

}
