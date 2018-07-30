package top.imlk.oneword.systemui.holder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.TextView;

import com.android.internal.widget.LockPatternUtils;


import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import top.imlk.oneword.systemui.uifixer.AOSPUIFixer;
import top.imlk.oneword.systemui.uifixer.UIFixer;
import top.imlk.oneword.systemui.view.OwnerInfoTextViewProxy;
import top.imlk.oneword.util.BugUtil;

import static top.imlk.oneword.StaticValue.CMD_BROADCAST_SET_NEW_LOCK_SCREEN_INFO;
import static top.imlk.oneword.StaticValue.SPILITER;
import static top.imlk.oneword.StaticValue.THE_NEW_LOCK_SCREEN_INFO_FROM;
import static top.imlk.oneword.StaticValue.THE_NEW_LOCK_SCREEN_INFO_MSG;
import static top.imlk.oneword.StaticValue.CMD_BROADCAST_UPDATE_LOCK_SCREEN_INFO;

/**
 * Created by imlk on 2018/5/24.
 */
public class LUpperMemberHolder {

    public static Class class_com_android_keyguard_KeyguardStatusView;
    public static Class class_com_android_keyguard_KeyguardUpdateMonitor;

    public static Field field_com_android_keyguard_KeyguardStatusView_mOwnerInfo;
    public static Field field_com_android_keyguard_KeyguardStatusView_mLockPatternUtils;

    public static Method method_com_android_keyguard_KeyguardStatusView_updateOwnerInfo;
    public static Method method_com_android_keyguard_KeyguardStatusView_getOwnerInfo;
    public static Method method_com_android_keyguard_KeyguardUpdateMonitor_getCurrentUser;

    public static WeakReference<TextView> ref_mOwnerInfo;
    public static WeakReference<Object> ref_keyguardStatusView;
    public static WeakReference<LockPatternUtils> ref_mLockPatternUtils;
    public static WeakReference<OwnerInfoTextViewProxy> ref_OwnerInfoTextViewProxy;


    public static void init(ClassLoader classLoader) throws NoSuchFieldException {

        class_com_android_keyguard_KeyguardStatusView = XposedHelpers.findClass("com.android.keyguard.KeyguardStatusView", classLoader);
        class_com_android_keyguard_KeyguardUpdateMonitor = XposedHelpers.findClass("com.android.keyguard.KeyguardUpdateMonitor", classLoader);

        field_com_android_keyguard_KeyguardStatusView_mOwnerInfo = XposedHelpers.findField(class_com_android_keyguard_KeyguardStatusView, "mOwnerInfo");
        field_com_android_keyguard_KeyguardStatusView_mLockPatternUtils = XposedHelpers.findField(class_com_android_keyguard_KeyguardStatusView, "mLockPatternUtils");

        method_com_android_keyguard_KeyguardStatusView_updateOwnerInfo = XposedHelpers.findMethodExact(class_com_android_keyguard_KeyguardStatusView, "updateOwnerInfo");
        method_com_android_keyguard_KeyguardStatusView_getOwnerInfo = XposedHelpers.findMethodExact(class_com_android_keyguard_KeyguardStatusView, "getOwnerInfo");
        method_com_android_keyguard_KeyguardUpdateMonitor_getCurrentUser = XposedHelpers.findMethodExact(class_com_android_keyguard_KeyguardUpdateMonitor, "getCurrentUser");

    }

    public static void doHook_onFinishInflate() {
        XposedHelpers.findAndHookMethod(class_com_android_keyguard_KeyguardStatusView, "onFinishInflate", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

//                CrashReport.initCrashReport(((View) param.thisObject).getContext().getApplicationContext(), "03e888691d", false);
//
//                CrashReport.testJavaCrash();

                try {

                    XposedBridge.log("doHook_onFinishInflate()");

                    ref_mOwnerInfo = new WeakReference(field_com_android_keyguard_KeyguardStatusView_mOwnerInfo.get(param.thisObject));
                    ref_mLockPatternUtils = new WeakReference(field_com_android_keyguard_KeyguardStatusView_mLockPatternUtils.get(param.thisObject));
                    ref_keyguardStatusView = new WeakReference(param.thisObject);

                    OwnerInfoTextViewProxy proxyView = new OwnerInfoTextViewProxy(ref_mOwnerInfo.get().getContext());

                    UIFixer uiFixer = new AOSPUIFixer(ref_mOwnerInfo.get());
                    uiFixer.fixUI(ref_mOwnerInfo.get());

                    proxyView.setUiFixer(uiFixer);

                    ref_OwnerInfoTextViewProxy = new WeakReference<>(proxyView);


                    field_com_android_keyguard_KeyguardStatusView_mOwnerInfo.set(param.thisObject, ref_OwnerInfoTextViewProxy.get());


                    method_com_android_keyguard_KeyguardStatusView_updateOwnerInfo.invoke(ref_keyguardStatusView.get());

                    registerBroadcastReceiver();
                } catch (Exception e) {
                    BugUtil.saveCrashInfo2File(e);
                }

            }
        });
    }


    public static void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CMD_BROADCAST_SET_NEW_LOCK_SCREEN_INFO);
        intentFilter.addAction(CMD_BROADCAST_UPDATE_LOCK_SCREEN_INFO);
        ref_mOwnerInfo.get().getContext().registerReceiver(new SystemUICmdBroadcastReceiver(), intentFilter);
        XposedBridge.log("create SystemUICmdBroadcastReceiver");

    }


    static class SystemUICmdBroadcastReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {//此处context具体类型与注册时的context有关

            XposedBridge.log("SystemUICmdBroadcastReceiver -> onReceive" + intent.getAction());

            try {
                switch (intent.getAction()) {
                    case CMD_BROADCAST_SET_NEW_LOCK_SCREEN_INFO:
                        if (ref_mLockPatternUtils.get().isDeviceOwnerInfoEnabled()) {
                            // TODO 直接写入json
                            ref_mLockPatternUtils.get().setDeviceOwnerInfo(butifyString(intent.getStringExtra(THE_NEW_LOCK_SCREEN_INFO_MSG)) + SPILITER + intent.getStringExtra(THE_NEW_LOCK_SCREEN_INFO_FROM));
                        } else {
                            try {

                                if (!ref_mLockPatternUtils.get().isOwnerInfoEnabled((Integer) method_com_android_keyguard_KeyguardUpdateMonitor_getCurrentUser.invoke(null))) {
                                    ref_mLockPatternUtils.get().setOwnerInfoEnabled(true, (Integer) method_com_android_keyguard_KeyguardUpdateMonitor_getCurrentUser.invoke(null));
                                }
                                ref_mLockPatternUtils.get().setOwnerInfo(butifyString(intent.getStringExtra(THE_NEW_LOCK_SCREEN_INFO_MSG)) + SPILITER + intent.getStringExtra(THE_NEW_LOCK_SCREEN_INFO_FROM), (Integer) method_com_android_keyguard_KeyguardUpdateMonitor_getCurrentUser.invoke(null));

                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }

                        }

                    case CMD_BROADCAST_UPDATE_LOCK_SCREEN_INFO:
                        try {
                            method_com_android_keyguard_KeyguardStatusView_updateOwnerInfo.invoke(ref_keyguardStatusView.get());

                        } catch (IllegalAccessException e) {
                            e.printStackTrace();

                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }

                        break;
                }
            } catch (Exception e) {

                BugUtil.saveCrashInfo2File(e);
            }
        }


        public static String butifyString(String s0) {
            if (s0 == null) {
                return "";
            }


            String s1 = s0
//                    .replace(" ", "\n")
                    .replace("；", "；\n")
                    .replace(";", ";\n")
                    .replace("?", "?\n")
                    .replace("？", "？\n")
                    .replace("!", "!\n")
                    .replace("！", "！\n");

            int count = 0;
            for (int i = 0; i < s1.length(); ++i) {
                if (s1.charAt(i) == '，') {
                    count++;
                }
            }
            if (count <= 6) {
                s1 = s1.replace("，", "，\n");
            }
            s1 = s1.replace("。", "。\n");

            return s1.trim();
        }


        public static void reportException(Context context, Exception e) {
            e.printStackTrace();
            // TODO 错误上报

        }
    }


}
