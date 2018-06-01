package top.imlk.oneword.injecter;

import android.os.Debug;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import top.imlk.oneword.holder.SystemUIHolder;

/**
 * Created by imlk on 2018/5/24.
 */
public class SystemUIInjecter implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
//        XposedBridge.log("SystemUIInjecter"+lpparam.packageName);
        if ("com.android.systemui".equals(lpparam.packageName)) {
//            Debug.waitForDebugger();
            SystemUIHolder.init(lpparam.classLoader);
            SystemUIHolder.doHook_onFinishInflate();
        }

    }
}
