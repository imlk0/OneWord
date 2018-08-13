package top.imlk.oneword.application.client.application;

import android.app.Application;
import android.view.View;

import com.tencent.bugly.crashreport.CrashReport;

import top.imlk.oneword.BuildConfig;

/**
 * Created by imlk on 2018/8/12.
 */
public class OneWordApplication extends Application {

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
