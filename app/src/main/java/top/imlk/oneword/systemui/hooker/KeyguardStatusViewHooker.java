package top.imlk.oneword.systemui.hooker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.bean.WordViewConfig;
import top.imlk.oneword.systemui.uifixer.BaseUIFixer;
import top.imlk.oneword.util.BroadcastSender;
import top.imlk.oneword.util.BugUtil;
import top.imlk.oneword.util.OneWordFileStation;


/**
 * Created by imlk on 2018/5/24.
 */
public class KeyguardStatusViewHooker extends BaseHooker {

    public static Class class_com_android_keyguard_KeyguardStatusView;
//    public static Class class_com_android_keyguard_KeyguardUpdateMonitor;

    public static Field field_com_android_keyguard_KeyguardStatusView_mOwnerInfo;
//    public static Field field_com_android_keyguard_KeyguardStatusView_mLockPatternUtils;

    //    public static Method method_com_android_keyguard_KeyguardStatusView_updateOwnerInfo;
//    public static Method method_com_android_keyguard_KeyguardStatusView_getOwnerInfo;
//    public static Method method_com_android_keyguard_KeyguardUpdateMonitor_getCurrentUser;

//    public static Object keyguardStatusView;
    //    public static WeakReference<LockPatternUtils> ref_mLockPatternUtils;
//    public static OwnerInfoTextViewProxy ownerInfoTextViewProxy;


    @Override
    public boolean initClass(ClassLoader classLoader) {

        try {
            class_com_android_keyguard_KeyguardStatusView = XposedHelpers.findClass("com.android.keyguard.KeyguardStatusView", classLoader);
//            class_com_android_keyguard_KeyguardUpdateMonitor = XposedHelpers.findClass("com.android.keyguard.KeyguardUpdateMonitor", classLoader);

            field_com_android_keyguard_KeyguardStatusView_mOwnerInfo = XposedHelpers.findField(class_com_android_keyguard_KeyguardStatusView, "mOwnerInfo");
//            field_com_android_keyguard_KeyguardStatusView_mLockPatternUtils = XposedHelpers.findField(class_com_android_keyguard_KeyguardStatusView, "mLockPatternUtils");

//            method_com_android_keyguard_KeyguardStatusView_updateOwnerInfo = XposedHelpers.findMethodExact(class_com_android_keyguard_KeyguardStatusView, "updateOwnerInfo");
//            method_com_android_keyguard_KeyguardStatusView_getOwnerInfo = XposedHelpers.findMethodExact(class_com_android_keyguard_KeyguardStatusView, "getOwnerInfo");
//        method_com_android_keyguard_KeyguardUpdateMonitor_getCurrentUser = XposedHelpers.findMethodExact(class_com_android_keyguard_KeyguardUpdateMonitor, "getCurrentUser");

//            throw new Throwable();

        } catch (Throwable e) {
            XposedBridge.log("------KeyguardStatusViewHooker.initClass()发生异常------");
            XposedBridge.log(e);
            XposedBridge.log("-------------------------------------------------------");
            return false;
        }
        XposedBridge.log("------KeyguardStatusViewHooker.initClass()顺利------");
        return true;
    }

    @Override
    public void subcribeHook(ClassLoader classLoader) {
        doHookMethod(XposedHelpers.findMethodBestMatch(class_com_android_keyguard_KeyguardStatusView, "onFinishInflate"));

    }

    public BaseUIFixer performUIFixAfterHookedMethod(XC_MethodHook.MethodHookParam param) throws IllegalAccessException {

        TextView mOwnerInfo = (TextView) field_com_android_keyguard_KeyguardStatusView_mOwnerInfo.get(param.thisObject);

        BaseUIFixer uiFixer = new BaseUIFixer(mOwnerInfo);
        uiFixer.fixUI(mOwnerInfo);

        field_com_android_keyguard_KeyguardStatusView_mOwnerInfo.set(param.thisObject, uiFixer.getOwnerInfoTextViewProxy());

        return uiFixer;
    }


}
