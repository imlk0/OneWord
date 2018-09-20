package top.imlk.oneword.systemui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import top.imlk.oneword.util.BroadcastSender;
import top.imlk.oneword.util.OneWordFileStation;
import top.imlk.oneword.util.SharedPreferencesUtil;


/**
 * Created by imlk on 2018/6/3.
 */
public class BootCompletedReceiver extends BroadcastReceiver {

//    public static final String ACTION_SYSTEMUI_INJECT_COMPLETED = "top.imlk.oneword.broadcast.SYSTEMUI_INJECT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction()) {
            case Intent.ACTION_BOOT_COMPLETED:

                Log.i("BootCompletedReceiver", "ACTION_BOOT_COMPLETED");

                Log.i("BootCompletedReceiver", "isAutoRefreshOpened:" + SharedPreferencesUtil.isAutoRefreshOpened(context));
                Log.i("BootCompletedReceiver", "isShowNotificationOneWordOpened:" + SharedPreferencesUtil.isShowNotificationOneWordOpened(context));

//                BroadcastSender.sendReloadOneWordBySelfBroadcast(context);// TODO 可能存在的权限问题
//                BroadcastSender.sendReloadWordViewConfigBySelfBroadcast(context);

                BroadcastSender.sendUseNewOneWordBroadcast(context, OneWordFileStation.readOneWordToJSON());
                BroadcastSender.sendUseNewWordViewConfigBroadcast(context, OneWordFileStation.readWordViewConfigToJSON());

                if (SharedPreferencesUtil.isAutoRefreshOpened(context)) {
                    BroadcastSender.startAutoRefresh(context);
                }

                if (SharedPreferencesUtil.isShowNotificationOneWordOpened(context)) {
                    BroadcastSender.startShowNitificationOneword(context);
                }


                break;
        }

    }

}
