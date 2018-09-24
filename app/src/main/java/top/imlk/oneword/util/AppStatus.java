package top.imlk.oneword.util;

import android.app.ActivityManager;
import android.content.Context;

import top.imlk.oneword.BuildConfig;
import top.imlk.oneword.application.client.application.OneWordApplication;

import static top.imlk.oneword.application.client.application.OneWordApplication.getOneWordApplication;


/**
 * Created by imlk on 2018/8/11.
 */
public class AppStatus {
    public static boolean hasBeenHooked() {
        return false;
    }

    public static int getModuleVersionCode() {//增加更新后提示
        return 0;
    }


    public synchronized static boolean isMianProcess() {
        if (processName == null) {
            processName = getCurrentProcessName();
        }

        return BuildConfig.APPLICATION_ID.equals(processName);
    }

    private static String processName;

    private static String getCurrentProcessName() {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) OneWordApplication.getOneWordApplication().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
                break;
            }
        }
        return processName;
    }


}
