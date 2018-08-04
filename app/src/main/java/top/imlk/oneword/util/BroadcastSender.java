package top.imlk.oneword.util;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import top.imlk.oneword.bean.WordBean;


/**
 * Created by imlk on 2018/5/26.
 */
public class BroadcastSender {

    public static final String CMD_BROADCAST_SET_NEW_LOCK_SCREEN_INFO = "top.imlk.oneword.set_new_lock_screen_info";
//    public static final String THE_NEW_LOCK_SCREEN_INFO_MSG = "top.imlk.oneword.the_new_lock_screen_info_msg";
//    public static final String THE_NEW_LOCK_SCREEN_INFO_FROM = "top.imlk.oneword.the_new_lock_screen_info_from";


    public static final String THE_NEW_LOCK_SCREEN_INFO_JSON = "top.imlk.oneword.the_new_lock_screen_info_json";


    public static final String CMD_SERVICES_START_AUTO_REFRESH_SERVICE = "top.imlk.oneword.start_auto_refresh_service";
    public static final String CMD_SERVICES_STOP_SERVICE = "top.imlk.oneword.stop_service";


    public static final String CMD_BROADCAST_UPDATE_LOCK_SCREEN_INFO = "top.imlk.oneword.update_lock_screen_info";


    private BroadcastSender() {

    }

    public static void sendSetNewLockScreenInfoBroadcast(Context context, WordBean wordBean) {
        if (wordBean != null) {

            Intent intent = new Intent();
            intent.setAction(CMD_BROADCAST_SET_NEW_LOCK_SCREEN_INFO);

            intent.putExtra(THE_NEW_LOCK_SCREEN_INFO_JSON, new Gson().toJson(wordBean, WordBean.class));

            context.sendBroadcast(intent);
            Toast.makeText(context, "设置锁屏一言中...", Toast.LENGTH_SHORT).show();
        }
    }


    public static void sendReloadLockScreenInfoBySelfBroadcast(Context context) {
        Intent intent = new Intent();
        intent.setAction(CMD_BROADCAST_UPDATE_LOCK_SCREEN_INFO);
        context.sendBroadcast(intent);
    }

}
