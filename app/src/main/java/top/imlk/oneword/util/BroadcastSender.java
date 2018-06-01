package top.imlk.oneword.util;

import android.content.Context;
import android.content.Intent;

import top.imlk.oneword.Hitokoto.HitokotoBean;

import static top.imlk.oneword.common.StaticValue.CMD_BROADCAST_SET_NEW_LOCK_SCREEN_INFO;
import static top.imlk.oneword.common.StaticValue.THE_NEW_LOCK_SCREEN_INFO_FROM;
import static top.imlk.oneword.common.StaticValue.THE_NEW_LOCK_SCREEN_INFO_MSG;
import static top.imlk.oneword.common.StaticValue.CMD_BROADCAST_UPDATE_LOCK_SCREEN_INFO;

/**
 * Created by imlk on 2018/5/26.
 */
public class BroadcastSender {

    private BroadcastSender() {

    }

    public static void sendSetNewLockScreenInfoBroadcast(Context context, HitokotoBean hitokotoBean) {
        Intent intent = new Intent();
        intent.setAction(CMD_BROADCAST_SET_NEW_LOCK_SCREEN_INFO);
        intent.putExtra(THE_NEW_LOCK_SCREEN_INFO_MSG, hitokotoBean.hitokoto);
        intent.putExtra(THE_NEW_LOCK_SCREEN_INFO_FROM, hitokotoBean.from);
        context.sendBroadcast(intent);
    }


    public static void sendUpdateLockScreenInfoBySelfBroadcast(Context context) {
        Intent intent = new Intent();
        intent.setAction(CMD_BROADCAST_UPDATE_LOCK_SCREEN_INFO);
        context.sendBroadcast(intent);
    }

}
