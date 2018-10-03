package top.imlk.oneword.app.application;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;

import top.imlk.oneword.BuildConfig;
import top.imlk.oneword.util.AppStatus;

/**
 * Created by imlk on 2018/8/12.
 */
public class OneWordApplication extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        AppStatus.setRunningApplication(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);

        strategy.setAppChannel(BuildConfig.FLAVOR);

        strategy.setAppVersion(BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE + ")");
//        strategy.setAppReportDelay(5000);

        CrashReport.setIsDevelopmentDevice(this, BuildConfig.DEBUG);

        CrashReport.initCrashReport(this, "03e888691d", BuildConfig.DEBUG, strategy);

    }

}
