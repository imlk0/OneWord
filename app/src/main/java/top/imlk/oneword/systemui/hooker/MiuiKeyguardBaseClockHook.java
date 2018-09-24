package top.imlk.oneword.systemui.hooker;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import top.imlk.oneword.systemui.injecter.AppInjecter;
import top.imlk.oneword.systemui.uifixer.BaseUIFixer;

/**
 * Created by imlk on 2018/8/24.
 * <p>
 * 旧版MIUI，源码为基于Android4的版本，锁屏在包名为com.android.keyguard的应用中
 */
public class MiuiKeyguardBaseClockHook extends BaseHooker {

    public static Class class_com_android_keyguard_MiuiKeyguardBaseClock;

    @Override
    public boolean initClass(ClassLoader classLoader) {
        try {
            class_com_android_keyguard_MiuiKeyguardBaseClock = XposedHelpers.findClass("com.android.keyguard.MiuiKeyguardBaseClock", classLoader);
        } catch (Throwable e) {
            XposedBridge.log("------MiuiKeyguardBaseClockHook.initClass()发生异常[" + AppInjecter.HostPackageName + "]------");
            XposedBridge.log(e);
            XposedBridge.log("--------------------------------------------------------");

            return false;
        }
        XposedBridge.log("------MiuiKeyguardBaseClockHook.initClass()顺利[" + AppInjecter.HostPackageName + "]------");
        return true;
    }

    @Override
    public void subcribeHook(ClassLoader classLoader) {

        //直接hook所有继承了MiuiKeyguardBaseClock的类
        XposedHelpers.findAndHookConstructor(class_com_android_keyguard_MiuiKeyguardBaseClock, Context.class, AttributeSet.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                doHookMethod(XposedHelpers.findMethodBestMatch(param.thisObject.getClass(), "onFinishInflate"));

            }
        });
    }

    @Override
    public BaseUIFixer performUIFixAfterHookedMethod(XC_MethodHook.MethodHookParam param) throws IllegalAccessException {

        Class keyguardBaseClockClass = param.thisObject.getClass();
        Field mOwnerInfoField = XposedHelpers.findField(keyguardBaseClockClass, "mOwnerInfo");

        TextView mOwnerInfo = (TextView) mOwnerInfoField.get(param.thisObject);

        if (mOwnerInfo.getClass().getName().equals(BaseUIFixer.OwnerInfoTextViewProxy.class.getName())) {
            XposedBridge.log("检测到重复注入，操作已被主动中止！！！");
            XposedBridge.log("Hooker:" + this);
            XposedBridge.log("HookerClass.getClassLoader():" + this.getClass().getClassLoader() + "@" + Integer.toHexString(this.getClass().getClassLoader().hashCode()));
//            XposedBridge.log(new Throwable());
            return null;
        }

        BaseUIFixer uiFixer = new BaseUIFixer(mOwnerInfo);
        uiFixer.fixUI(mOwnerInfo);

        mOwnerInfoField.set(param.thisObject, uiFixer.getOwnerInfoTextViewProxy());

        return uiFixer;
    }
}
