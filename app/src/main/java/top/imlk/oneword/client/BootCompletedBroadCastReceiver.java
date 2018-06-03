package top.imlk.oneword.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.util.Log;

import static top.imlk.oneword.common.StaticValue.CMD_SERVICES_START_AUTO_REFRESH_SERVICE;

/**
 * Created by imlk on 2018/6/3.
 */
public class BootCompletedBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction()) {
            case Intent.ACTION_BOOT_COMPLETED:

                Log.e("BootCompletedBroadCast","imlk BootCompletedBroadCastReceiver onReceive");
                Intent service = new Intent(context, OneWordAutoRefreshService.class);
                intent.setAction(CMD_SERVICES_START_AUTO_REFRESH_SERVICE);
                context.startService(service);
                break;
        }

    }

}
