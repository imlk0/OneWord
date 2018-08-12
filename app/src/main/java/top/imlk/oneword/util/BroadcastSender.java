package top.imlk.oneword.util;

import android.content.Context;
import android.content.Intent;

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


    public static final String CMD_SERVICES_START_AUTO_REFRESH_SERVICE = "top.imlk.oneword.start_auto_refresh_service";
    public static final String CMD_SERVICES_STOP_SERVICE = "top.imlk.oneword.stop_service";


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


}
