package top.imlk.oneword.systemui.hooker;

import android.widget.TextView;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import top.imlk.oneword.systemui.injecter.AppInjecter;
import top.imlk.oneword.systemui.uifixer.BaseUIFixer;


/**
 * Created by imlk on 2018/5/24.
 */
public class SamsungKeyguardCarrierViewHooker extends BaseHooker {

    public static Class class_com_android_keyguard_KeyguardCarrierView;

    public static Field field_com_android_keyguard_KeyguardCarrierView_mOwnerInfo;

    @Override
    public boolean initClass(ClassLoader classLoader) {

        try {
            class_com_android_keyguard_KeyguardCarrierView = XposedHelpers.findClass("com.android.keyguard.KeyguardCarrierView", classLoader);

            field_com_android_keyguard_KeyguardCarrierView_mOwnerInfo = XposedHelpers.findField(class_com_android_keyguard_KeyguardCarrierView, "mOwnerInfo");

        } catch (Throwable e) {
            XposedBridge.log("------SamsungKeyguardCarrierViewHooker.initClass()发生异常[" + AppInjecter.HostPackageName + "]------");
            XposedBridge.log(e);
            XposedBridge.log("-------------------------------------------------------");
            return false;
        }
        XposedBridge.log("------SamsungKeyguardCarrierViewHooker.initClass()顺利[" + AppInjecter.HostPackageName + "]------");
        return true;
    }

    @Override
    public void subcribeHook(ClassLoader classLoader) {
        doHookMethod(XposedHelpers.findMethodBestMatch(class_com_android_keyguard_KeyguardCarrierView, "onFinishInflate"));

    }

    public BaseUIFixer performUIFixAfterHookedMethod(XC_MethodHook.MethodHookParam param) throws IllegalAccessException {

        TextView mOwnerInfo = (TextView) field_com_android_keyguard_KeyguardCarrierView_mOwnerInfo.get(param.thisObject);

        BaseUIFixer uiFixer = new BaseUIFixer(mOwnerInfo);
        uiFixer.fixUI(mOwnerInfo);

        field_com_android_keyguard_KeyguardCarrierView_mOwnerInfo.set(param.thisObject, uiFixer.getOwnerInfoTextViewProxy());

        return uiFixer;
    }


}
