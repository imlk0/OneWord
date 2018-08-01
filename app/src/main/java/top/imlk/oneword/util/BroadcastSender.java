package top.imlk.oneword.util;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;

import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.net.Hitokoto.HitokotoBean;

import static top.imlk.oneword.StaticValue.CMD_BROADCAST_SET_NEW_LOCK_SCREEN_INFO;
import static top.imlk.oneword.StaticValue.THE_NEW_LOCK_SCREEN_INFO_JSON;
import static top.imlk.oneword.StaticValue.CMD_BROADCAST_UPDATE_LOCK_SCREEN_INFO;

/**
 * Created by imlk on 2018/5/26.
 */
public class BroadcastSender {

    private BroadcastSender() {

    }

    public static void sendSetNewLockScreenInfoBroadcast(Context context, HitokotoBean hitokotoBean) {
        Intent intent = new Intent();
        intent.setAction(CMD_BROADCAST_SET_NEW_LOCK_SCREEN_INFO);

        WordBean wordBean = new WordBean(hitokotoBean.hitokoto, hitokotoBean.from);
        intent.putExtra(THE_NEW_LOCK_SCREEN_INFO_JSON, new Gson().toJson(wordBean, WordBean.class));

        context.sendBroadcast(intent);
    }


    public static void sendReloadLockScreenInfoBySelfBroadcast(Context context) {
        Intent intent = new Intent();
        intent.setAction(CMD_BROADCAST_UPDATE_LOCK_SCREEN_INFO);
        context.sendBroadcast(intent);
    }

}
