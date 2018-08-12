package top.imlk.oneword.systemui.hooker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.bean.WordViewConfig;
import top.imlk.oneword.systemui.uifixer.BaseUIFixer;
import top.imlk.oneword.systemui.view.OwnerInfoTextViewProxy;
import top.imlk.oneword.util.BroadcastSender;
import top.imlk.oneword.util.BugUtil;
import top.imlk.oneword.util.OneWordFileStation;


/**
 * Created by imlk on 2018/5/24.
 */
public class KeyguardStatusViewHooker {

    public static Class class_com_android_keyguard_KeyguardStatusView;
//    public static Class class_com_android_keyguard_KeyguardUpdateMonitor;

    public static Field field_com_android_keyguard_KeyguardStatusView_mOwnerInfo;
//    public static Field field_com_android_keyguard_KeyguardStatusView_mLockPatternUtils;

    //    public static Method method_com_android_keyguard_KeyguardStatusView_updateOwnerInfo;
//    public static Method method_com_android_keyguard_KeyguardStatusView_getOwnerInfo;
//    public static Method method_com_android_keyguard_KeyguardUpdateMonitor_getCurrentUser;

    public static WeakReference<TextView> ref_mOwnerInfo;
    public static WeakReference<Object> ref_keyguardStatusView;
    //    public static WeakReference<LockPatternUtils> ref_mLockPatternUtils;
    public static WeakReference<OwnerInfoTextViewProxy> ref_OwnerInfoTextViewProxy;
    public static WeakReference<BaseUIFixer> ref_BaseUIFixer;


    public static void init(ClassLoader classLoader) throws NoSuchFieldException {

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
//            XposedBridge.log(e);
            String logPath = BugUtil.printAndSaveCrashThrow2File(e);
//            Toast.makeText(((View) param.thisObject).getContext(), String.format("Hook时发生异常，可能是系统兼容性问题，日志产生在:\n%s", logPath), Toast.LENGTH_LONG).show();

        }
    }


    public static void doHook_onFinishInflate() {
        XposedHelpers.findAndHookMethod(class_com_android_keyguard_KeyguardStatusView, "onFinishInflate", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                XposedBridge.log("doHook_onFinishInflate()");

                Context context = ((View) param.thisObject).getContext();


                try {

                    try {

                        ViewGroup.LayoutParams layoutParams = ((View) param.thisObject).getLayoutParams();

                        XposedBridge.log("View.getLP：" + layoutParams);

                    } catch (Throwable e) {
                        XposedBridge.log(e);
                    }


                    ref_mOwnerInfo = new WeakReference(field_com_android_keyguard_KeyguardStatusView_mOwnerInfo.get(param.thisObject));
//                    ref_mLockPatternUtils = new WeakReference(field_com_android_keyguard_KeyguardStatusView_mLockPatternUtils.get(param.thisObject));
                    ref_keyguardStatusView = new WeakReference(param.thisObject);

                    OwnerInfoTextViewProxy proxyView = new OwnerInfoTextViewProxy(ref_mOwnerInfo.get().getContext());

                    BaseUIFixer uiFixer = new BaseUIFixer(ref_mOwnerInfo.get());
                    uiFixer.fixUI(ref_mOwnerInfo.get());

                    proxyView.setUiFixer(uiFixer);

                    ref_BaseUIFixer = new WeakReference<>(uiFixer);
                    ref_OwnerInfoTextViewProxy = new WeakReference<>(proxyView);

                    field_com_android_keyguard_KeyguardStatusView_mOwnerInfo.set(param.thisObject, ref_OwnerInfoTextViewProxy.get());


                    getAndSetOneWord();
                    getAndApplyWordViewConfig();

                } catch (Throwable e) {
//                    XposedBridge.log(e);
                    String logPath = BugUtil.printAndSaveCrashThrow2File(e);
                    Toast.makeText(context, String.format("Hook后的操作发生异常，日志产生在:\n%s", logPath), Toast.LENGTH_LONG).show();
                }

                registerBroadcastReceiver();

            }
        });
    }

    private static void getAndSetOneWord() {

        WordBean wordBean = OneWordFileStation.readOneWordJSON();

        ref_BaseUIFixer.get().setOneWord(wordBean);

        if (wordBean == null) {
            Toast.makeText(((View) ref_keyguardStatusView.get()).getContext(), "解析保存的一言失败，去设置一言？", Toast.LENGTH_SHORT).show();
        }

    }

    private static void getAndApplyWordViewConfig() {
        WordViewConfig wordViewConfig = OneWordFileStation.readWordViewConfigJSON();

        ref_BaseUIFixer.get().applyWordViewConfig(wordViewConfig);

    }


    public static void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastSender.CMD_BROADCAST_SET_NEW_WORDBEAN);
        intentFilter.addAction(BroadcastSender.CMD_BROADCAST_RELOAD_WORDBEAN);
        intentFilter.addAction(BroadcastSender.CMD_BROADCAST_SET_NEW_WORDVIEWCONFIG);
        intentFilter.addAction(BroadcastSender.CMD_BROADCAST_RELOAD_WORDVIEWCONFIG);
        ref_mOwnerInfo.get().getContext().registerReceiver(new SystemUICmdBroadcastReceiver(), intentFilter);
        XposedBridge.log("create SystemUICmdBroadcastReceiver");

    }


    static class SystemUICmdBroadcastReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {//此处context具体类型与注册时的context有关

            XposedBridge.log("SystemUICmdBroadcastReceiver -> onReceive" + intent.getAction());

            try {
                switch (intent.getAction()) {
                    case BroadcastSender.CMD_BROADCAST_SET_NEW_WORDBEAN:

//                        KeyguardStatusViewHelper.setOwnerInfo(intent.getStringExtra(BroadcastSender.THE_NEW_WORDBEAN));

                        WordBean wordBean = intent.getParcelableExtra(BroadcastSender.THE_NEW_WORDBEAN);


                        XposedBridge.log("wordbean received");
                        XposedBridge.log(wordBean.toString());

//                        XposedBridge.log("resolved");

                        ref_BaseUIFixer.get().setOneWord(wordBean);

//                        Toast.makeText(context, "一言已收到", Toast.LENGTH_SHORT).show();

                        XposedBridge.log("wordbean updated");

                        break;

                    case BroadcastSender.CMD_BROADCAST_RELOAD_WORDBEAN:

                        getAndSetOneWord();

                        XposedBridge.log("wordbean updated byself");

                        break;

                    case BroadcastSender.CMD_BROADCAST_SET_NEW_WORDVIEWCONFIG:


                        WordViewConfig config = intent.getParcelableExtra(BroadcastSender.THE_NEW_WORDVIEWCONFIG);


                        XposedBridge.log("wordviewconfig received");
                        XposedBridge.log(config.toString());

//                        XposedBridge.log("resolved");

                        ref_BaseUIFixer.get().applyWordViewConfig(config);


                        XposedBridge.log("wordviewconfig updated");

                        break;

                    case BroadcastSender.CMD_BROADCAST_RELOAD_WORDVIEWCONFIG:

                        getAndApplyWordViewConfig();

                        break;

                }
            } catch (Throwable e) {
//                XposedBridge.log(e);

                String logPath = BugUtil.printAndSaveCrashThrow2File(e);

                Toast.makeText(context, String.format("不小心搞崩了,灰常抱歉，日志在\n%s", logPath), Toast.LENGTH_LONG).show();

            }
        }


        public static void reportException(Context context, Exception e) {
            e.printStackTrace();
            // TODO 错误上报

        }
    }


}
