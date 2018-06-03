package top.imlk.oneword.holder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.internal.widget.LockPatternUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static top.imlk.oneword.common.StaticValue.CMD_BROADCAST_SET_NEW_LOCK_SCREEN_INFO;
import static top.imlk.oneword.common.StaticValue.SPILITER;
import static top.imlk.oneword.common.StaticValue.THE_NEW_LOCK_SCREEN_INFO_FROM;
import static top.imlk.oneword.common.StaticValue.THE_NEW_LOCK_SCREEN_INFO_MSG;
import static top.imlk.oneword.common.StaticValue.CMD_BROADCAST_UPDATE_LOCK_SCREEN_INFO;

/**
 * Created by imlk on 2018/5/24.
 */
public class SystemUIHolder {

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

                ref_mOwnerInfo = new WeakReference(field_com_android_keyguard_KeyguardStatusView_mOwnerInfo.get(param.thisObject));

                ViewGroup.MarginLayoutParams marginLayoutParams = ((ViewGroup.MarginLayoutParams) ref_mOwnerInfo.get().getLayoutParams());
                marginLayoutParams.leftMargin *= 2;
                marginLayoutParams.rightMargin *= 2;

                ref_mOwnerInfo.get().setLayoutParams(marginLayoutParams);


                ref_mLockPatternUtils = new WeakReference(field_com_android_keyguard_KeyguardStatusView_mLockPatternUtils.get(param.thisObject));
                ref_keyguardStatusView = new WeakReference(param.thisObject);
                ref_OwnerInfoTextViewProxy = new WeakReference<>(new OwnerInfoTextViewProxy(ref_mOwnerInfo.get().getContext(), ref_mOwnerInfo.get()));

                field_com_android_keyguard_KeyguardStatusView_mOwnerInfo.set(param.thisObject, ref_OwnerInfoTextViewProxy.get());

                //添加到现有视图中
                ViewGroup viewGroup = ((ViewGroup) ref_mOwnerInfo.get().getParent());
                int i;
                for (i = 0; i < viewGroup.getChildCount(); ++i) {
                    if (viewGroup.getChildAt(i) == ref_mOwnerInfo.get()) {
                        break;
                    }
                }

                ((ViewGroup) ref_mOwnerInfo.get().getParent()).addView(ref_OwnerInfoTextViewProxy.get().getCustomTextView(), i + 1);

                viewGroup = null;

                //TODO test multiline
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ref_mOwnerInfo.get().setElegantTextHeight(true);
                }
                ref_mOwnerInfo.get().setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                ref_mOwnerInfo.get().setSingleLine(false);

                method_com_android_keyguard_KeyguardStatusView_updateOwnerInfo.invoke(ref_keyguardStatusView.get());

                registerBroadcastReceiver();
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

            switch (intent.getAction()) {
                case CMD_BROADCAST_SET_NEW_LOCK_SCREEN_INFO:
                    if (ref_mLockPatternUtils.get().isDeviceOwnerInfoEnabled()) {
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


        }
    }


}
