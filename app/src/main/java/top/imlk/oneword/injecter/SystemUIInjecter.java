package top.imlk.oneword.injecter;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import top.imlk.oneword.holder.SystemUIHolder;

/**
 * Created by imlk on 2018/5/24.
 */
public class SystemUIInjecter implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName == "com.android.systemui") {
            SystemUIHolder.init(lpparam.classLoader);
            SystemUIHolder.doHook_onFinishInflate();
        }

    }
}
