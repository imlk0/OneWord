package top.imlk.oneword.systemui.injecter;


import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.zqc.opencc.android.lib.ChineseConverter;
import com.zqc.opencc.android.lib.ConversionType;

import java.lang.reflect.InvocationTargetException;

import dalvik.system.PathClassLoader;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import top.imlk.oneword.BuildConfig;
import top.imlk.oneword.systemui.hooker.KeyguardStatusViewHooker;
import top.imlk.oneword.systemui.hooker.MiuiKeyguardBaseClockHook;
import top.imlk.oneword.systemui.hooker.SamsungKeyguardCarrierViewHooker;
import top.imlk.oneword.util.AppStatus;

/**
 * Created by imlk on 2018/5/24.
 */
public class AppInjecter implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    private String MODULE_PATH;

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;

    }


    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
//        XposedBridge.log("AppInjecter"+lpparam.packageName);

//        if (BuildConfig.DEBUG) {
//
//
//            String path = "/data/app/top.imlk.oneword-%d/base.apk";
//
//            File file = null;
//            for (int i = 1; i <= 2; i++) {
//                file = new File(String.format(path, i));
//                if (file.exists()) {
//                    break;
//                }
//            }
//
//            XposedBridge.log("file exist: " + file == null ? null : file.getAbsolutePath());
//
//
//            if (file != null && file.exists()) {
//
//                PathClassLoader pathClassLoader = new PathClassLoader(file.getAbsolutePath(), ClassLoader.getSystemClassLoader());
//
//                Class aimClass = pathClassLoader.loadClass(AppInjecter.class.getName());
//                aimClass.getDeclaredMethod("handleLoadPackage_Out", XC_LoadPackage.LoadPackageParam.class, String.class).invoke(aimClass.newInstance(), lpparam, MODULE_PATH);
//
//                XposedBridge.log("Module loaded");
//            }
//        } else {
//
//            handleLoadPackage_Out(lpparam, MODULE_PATH);
//        }

        if (lpparam.packageName == null) {
            return;
        }

        switch (lpparam.packageName) {


            case "com.android.systemui":
            case "com.android.keyguard":

                XposedHelpers.findAndHookMethod(Application.class, "onCreate", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        loadAtApplicationOnCreate(((Application) param.thisObject), lpparam);
                    }
                });

                XposedBridge.log("注入");
                break;
            case BuildConfig.APPLICATION_ID:

                if (BuildConfig.APPLICATION_ID.equals(lpparam.packageName)) {
                    XposedHelpers.findAndHookMethod(AppStatus.class.getName(), lpparam.classLoader, "hasBeenHooked", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(true);
                        }
                    });

                    XposedHelpers.findAndHookMethod(AppStatus.class.getName(), lpparam.classLoader, "getModuleVersionCode", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(BuildConfig.VERSION_CODE);
                        }
                    });
                }

                break;
        }

    }

    public void loadAtApplicationOnCreate(Application application, XC_LoadPackage.LoadPackageParam lpparam) {

        try {
            MODULE_PATH = application.getPackageManager().getApplicationInfo(BuildConfig.APPLICATION_ID, 0).sourceDir;

            PathClassLoader pathClassLoader = new XposedModuleSoFileClassLoader(MODULE_PATH, MODULE_PATH, ClassLoader.getSystemClassLoader());

            Class aimClass = pathClassLoader.loadClass(AppInjecter.class.getName());
            aimClass.getDeclaredMethod("handleLoadPackage_Out", XC_LoadPackage.LoadPackageParam.class)
                    .invoke(aimClass.newInstance(), lpparam);


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static String HostPackageName;

    // 可免重启部分
    public void handleLoadPackage_Out(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        XposedBridge.log("动态加载入口处，在" + lpparam.packageName);


        if (false) {

        } else {
            HostPackageName = lpparam.packageName;


            switch (lpparam.packageName) {

                case "com.android.systemui":
//            Debug.waitForDebugger();

                    hookChineseConverterConvertParam();

                    new KeyguardStatusViewHooker().doHook(lpparam.classLoader);

                    if (RomHelper.isMIUI()) {
                        new MiuiKeyguardBaseClockHook().doHook(lpparam.classLoader);
                    }

                    new SamsungKeyguardCarrierViewHooker().doHook(lpparam.classLoader);

                    break;
                case "com.android.keyguard":

                    hookChineseConverterConvertParam();

                    if (RomHelper.isMIUI()) {
                        new MiuiKeyguardBaseClockHook().doHook(lpparam.classLoader);
                    }
                    break;
            }
        }
    }


    private static void hookChineseConverterConvertParam() {
        XposedHelpers.findAndHookMethod(ChineseConverter.class, "convert", String.class, ConversionType.class, Context.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.args[2] = ModuleAppContext.getModuleAppContext();
                Log.e("handleLoadPackage_Out", "ChineseConverter#convert Context was replaced");
            }
        });
    }


}
