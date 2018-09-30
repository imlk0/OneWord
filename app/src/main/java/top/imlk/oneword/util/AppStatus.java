package top.imlk.oneword.util;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import top.imlk.oneword.BuildConfig;
import top.imlk.oneword.app.application.OneWordApplication;


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
        ActivityManager manager = (ActivityManager) getRunningApplication().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
                break;
            }
        }
        return processName;
    }


    private static Application runningApplication;

    public static Application getRunningApplication() {
        return runningApplication;
    }

    public static void setRunningApplication(Application application) {
        runningApplication = application;
    }


}
