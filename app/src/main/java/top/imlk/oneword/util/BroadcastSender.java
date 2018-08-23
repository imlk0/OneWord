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

    public static final String CMD_BROADCAST_SET_NEW_WORDBEAN = "top.imlk.oneword.set_new_word_bean";
    public static final String THE_NEW_WORDBEAN = "top.imlk.oneword.the_new_word_bean";
    //    public static final String THE_NEW_LOCK_SCREEN_INFO_FROM = "top.imlk.oneword.the_new_lock_screen_info_from";
//    public static final String THE_NEW_LOCK_SCREEN_INFO_MSG = "top.imlk.oneword.the_new_lock_screen_info_msg";

    public static final String CMD_BROADCAST_RELOAD_WORDBEAN = "top.imlk.oneword.reload_word_bean";


    public static final String CMD_BROADCAST_SET_NEW_WORDVIEWCONFIG = "top.imlk.oneword.set_new_word_view_config";
    public static final String THE_NEW_WORDVIEWCONFIG = "top.imlk.oneword.the_new_word_view_config";
    //    public static final String THE_NEW_LOCK_SCREEN_INFO_FROM = "top.imlk.oneword.the_new_lock_screen_info_from";
//    public static final String THE_NEW_LOCK_SCREEN_INFO_MSG = "top.imlk.oneword.the_new_lock_screen_info_msg";

    public static final String CMD_BROADCAST_RELOAD_WORDVIEWCONFIG = "top.imlk.oneword.reload_word_view_config";


    public static final String CMD_SERVICES_START_AUTO_REFRESH = "top.imlk.oneword.start_auto_refresh";
    public static final String CMD_SERVICES_PAUSE_AUTO_REFRESH = "top.imlk.oneword.pause_auto_refresh";
    public static final String CMD_SERVICES_STOP_AUTO_REFRESH = "top.imlk.oneword.stop_auto_refresh";
    public static final String THE_REFRESH_MODE = "top.imlk.oneword.the_refresh_mode";


    public static final String CMD_SERVICES_START_SHOW_NOTIFICATION_ONEWORD = "top.imlk.oneword.start_show_notification_oneword";
    public static final String CMD_SERVICES_STOP_SHOW_NOTIFICATION_ONEWORD = "top.imlk.oneword.stop_show_notification_oneword";
    public static final String THE_INIT_WORDBEAN = "top.imlk.oneword.the_init_word_bean";


    //    public static final String CMD_INTENT_START_MAIN_ACTICITY_WHEN_CLICKED = "top.imlk.oneword.start_main_activity_when_clicked";
    public static final String THE_CLICKED_WORDBEAN = "top.imlk.oneword.the_clicked_word_bean";


    private BroadcastSender() {

    }

    public static void sendUseNewOneWordBroadcast(Context context, WordBean wordBean) {
        if (wordBean != null) {

            Intent intent = new Intent();
            intent.setAction(CMD_BROADCAST_SET_NEW_WORDBEAN);

            intent.putExtra(THE_NEW_WORDBEAN, wordBean);

            context.sendBroadcast(intent);
        }
    }


    public static void sendReloadOneWordBySelfBroadcast(Context context) {
        Intent intent = new Intent();
        intent.setAction(CMD_BROADCAST_RELOAD_WORDBEAN);
        context.sendBroadcast(intent);
    }


    public static void sendUseNewWordViewConfigBroadcast(Context context, WordViewConfig config) {
        if (config != null) {

            Intent intent = new Intent();
            intent.setAction(CMD_BROADCAST_SET_NEW_WORDVIEWCONFIG);

            intent.putExtra(THE_NEW_WORDVIEWCONFIG, config);

            context.sendBroadcast(intent);
        }
    }

    public static void sendReloadWordViewConfigBySelfBroadcast(Context context) {
        Intent intent = new Intent();
        intent.setAction(CMD_BROADCAST_RELOAD_WORDVIEWCONFIG);
        context.sendBroadcast(intent);
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

    public static void stopAutoRefresh(Context context) {

        Intent intent = new Intent(context, OneWordAutoRefreshService.class);
        intent.setAction(BroadcastSender.CMD_SERVICES_STOP_AUTO_REFRESH);

        context.startService(intent);
    }

    public static void pauseAutoRefresh(Context context) {

        Intent intent = new Intent(context, OneWordAutoRefreshService.class);
        intent.setAction(BroadcastSender.CMD_SERVICES_PAUSE_AUTO_REFRESH);

        context.startService(intent);
    }


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
