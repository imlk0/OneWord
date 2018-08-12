package top.imlk.oneword.systemui.injecter;


import java.io.File;

import dalvik.system.PathClassLoader;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import top.imlk.oneword.BuildConfig;
import top.imlk.oneword.systemui.hooker.KeyguardStatusViewHooker;
import top.imlk.oneword.util.AppStatus;
import top.imlk.oneword.util.ShowDialogUtil;

/**
 * Created by imlk on 2018/5/24.
 */
public class AppInjecter implements IXposedHookLoadPackage {


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
//        XposedBridge.log("AppInjecter"+lpparam.packageName);

        if (BuildConfig.DEBUG) {


            String path = "/data/app/top.imlk.oneword-%d/base.apk";

            File file = null;
            for (int i = 1; i <= 2; i++) {
                file = new File(String.format(path, i));
                if (file.exists()) {
                    break;
                }
            }

//        XposedBridge.log("file exist: " + file == null ? null : file.getAbsolutePath());


            if (file != null && file.exists())

            {

                PathClassLoader pathClassLoader = new PathClassLoader(file.getAbsolutePath(), ClassLoader.getSystemClassLoader());

                Class aimClass = pathClassLoader.loadClass(AppInjecter.class.getName());
                aimClass.getDeclaredMethod("handleLoadPackage_Out", XC_LoadPackage.LoadPackageParam.class).invoke(aimClass.newInstance(), lpparam);

//            XposedBridge.log("Module loaded");
            }
        } else {

            handleLoadPackage_Out(lpparam);
        }

    }


    public void handleLoadPackage_Out(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {


        if (false) {

        } else if ("com.android.systemui".equals(lpparam.packageName)) {
//            Debug.waitForDebugger();
            KeyguardStatusViewHooker.init(lpparam.classLoader);
            KeyguardStatusViewHooker.doHook_onFinishInflate();
        } else if ("top.imlk.oneword".equals(lpparam.packageName)) {
            XposedHelpers.findAndHookMethod(AppStatus.class.getName(), lpparam.classLoader, "hasBeenHooked", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    param.setResult(true);
                }
            });
        }

    }


}
