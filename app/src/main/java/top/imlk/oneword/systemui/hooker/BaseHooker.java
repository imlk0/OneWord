package top.imlk.oneword.systemui.hooker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;

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
 * Created by imlk on 2018/8/24.
 */
public abstract class BaseHooker {

    //    public static TextView mOwnerInfo;
    public ArrayList<BaseUIFixer> uiFixers = new ArrayList<>();
//    public static Context context;

    public final void doHook(ClassLoader classLoader) {
        try {

            if (initClass(classLoader)) {

                subcribeHook(classLoader);

            }

        } catch (Throwable e) {
//            XposedBridge.log(e);
            String logPath = BugUtil.printAndSaveCrashThrow2File(e);
//            Toast.makeText(((View) param.thisObject).getContext(), String.format("Hook时发生异常，可能是系统兼容性问题，日志产生在:\n%s", logPath), Toast.LENGTH_LONG).show();
        }
    }


    // return true if success
    public abstract boolean initClass(ClassLoader classLoader);

    public abstract void subcribeHook(ClassLoader classLoader);

    public abstract BaseUIFixer performUIFixAfterHookedMethod(XC_MethodHook.MethodHookParam param) throws IllegalAccessException;

    protected final void doHookMethod(Method method) {
        XposedBridge.hookMethod(method, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                XposedBridge.log("doHook_onFinishInflate()");

                Context context = ((View) param.thisObject).getContext().getApplicationContext();

                BaseUIFixer baseUIFixer = null;

                try {
                    baseUIFixer = performUIFixAfterHookedMethod(param);

                    if (baseUIFixer == null) {
                        XposedBridge.log(this.getClass().getName() + ".performUIFixAfterHookedMethod()结果为null！！wtf！！！");
                        XposedBridge.log(new Throwable());
                    } else {
                        baseUIFixer.getAndSetOneWord();
                        baseUIFixer.getAndApplyWordViewConfig();
                        XposedBridge.log("BaseUIFixer 注入成功");
                    }

                } catch (Throwable e) {
//                    XposedBridge.log(e);
                    String logPath = BugUtil.printAndSaveCrashThrow2File(e);
                    Toast.makeText(context, String.format("Hook后的操作发生异常，日志产生在:\n%s", logPath), Toast.LENGTH_LONG).show();
                }

//                baseUIFixer.registerBroadcastReceiver(context);

            }
        });
    }

}
