package top.imlk.oneword.systemui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import top.imlk.oneword.application.client.service.OneWordAutoRefreshService;
import top.imlk.oneword.util.BroadcastSender;
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

                if (SharedPreferencesUtil.isAutoRefreshOpened(context)) {
                    Intent service = new Intent(context, OneWordAutoRefreshService.class);
                    intent.setAction(BroadcastSender.CMD_SERVICES_START_AUTO_REFRESH_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(service);
                    } else {
                        context.startService(service);
                    }
                } else {
                    Log.i("BootCompletedReceiver", "isAutoRefreshOpened : false");
                }
                break;
        }

    }

}
