package top.imlk.oneword.systemui.hooker;

import android.content.Context;
import android.content.Intent;

import com.android.internal.widget.LockPatternUtils;

import java.lang.reflect.InvocationTargetException;

import de.robv.android.xposed.XposedHelpers;

import static top.imlk.oneword.StaticValue.SPILITER;
import static top.imlk.oneword.StaticValue.THE_NEW_LOCK_SCREEN_INFO_FROM;
import static top.imlk.oneword.StaticValue.THE_NEW_LOCK_SCREEN_INFO_MSG;
import static top.imlk.oneword.systemui.hooker.KeyguardStatusViewHooker.SystemUICmdBroadcastReceiver.butifyString;
import static top.imlk.oneword.systemui.hooker.KeyguardStatusViewHooker.class_com_android_keyguard_KeyguardUpdateMonitor;
import static top.imlk.oneword.systemui.hooker.KeyguardStatusViewHooker.ref_mLockPatternUtils;

/**
 * Created by imlk on 2018/7/31.
 */
public class KeyguardStatusViewHelper {

    public static boolean isfullMultiUserSys = true;


    static {
        try {
            LockPatternUtils.class.getDeclaredMethod("isDeviceOwnerInfoEnabled");
        } catch (NoSuchMethodException e) {
            isfullMultiUserSys = false;
        }
    }


    public static void setOwnerInfo(Context context, Intent intent) throws InvocationTargetException, IllegalAccessException {

        if (isfullMultiUserSys) {
            setOwnerInfoInFullMUSys(context, intent);

        } else {
            setOwnerInfoInNOTFullMUSys(context, intent);
        }

    }


    public static void setOwnerInfoInFullMUSys(Context context, Intent intent) throws InvocationTargetException, IllegalAccessException {

        if (ref_mLockPatternUtils.get().isDeviceOwnerInfoEnabled()) {
            // TODO 直接写入json
            ref_mLockPatternUtils.get().setDeviceOwnerInfo(butifyString(intent.getStringExtra(THE_NEW_LOCK_SCREEN_INFO_MSG)) + SPILITER + intent.getStringExtra(THE_NEW_LOCK_SCREEN_INFO_FROM));
        } else {

            int currentUser = (int) XposedHelpers.findMethodExact(class_com_android_keyguard_KeyguardUpdateMonitor, "getCurrentUser").invoke(null);

            if (!ref_mLockPatternUtils.get().isOwnerInfoEnabled(currentUser)) {
                ref_mLockPatternUtils.get().setOwnerInfoEnabled(true, currentUser);
            }
            ref_mLockPatternUtils.get().setOwnerInfo(butifyString(intent.getStringExtra(THE_NEW_LOCK_SCREEN_INFO_MSG)) + SPILITER + intent.getStringExtra(THE_NEW_LOCK_SCREEN_INFO_FROM), currentUser);
        }

    }


    public static void setOwnerInfoInNOTFullMUSys(Context context, Intent intent) throws InvocationTargetException, IllegalAccessException {
        ;
        if ((boolean) XposedHelpers.findMethodBestMatch(LockPatternUtils.class, "isOwnerInfoEnabled").invoke(ref_mLockPatternUtils.get())) {
            XposedHelpers.findMethodBestMatch(LockPatternUtils.class, "setOwnerInfoEnabled", boolean.class).invoke(ref_mLockPatternUtils.get(), true);
        }

        int currentUser = (int) XposedHelpers.findMethodBestMatch(LockPatternUtils.class, "getCurrentUser").invoke(ref_mLockPatternUtils.get());

        ref_mLockPatternUtils.get().setOwnerInfo(
                butifyString(intent.getStringExtra(THE_NEW_LOCK_SCREEN_INFO_MSG)) + SPILITER + intent.getStringExtra(THE_NEW_LOCK_SCREEN_INFO_FROM),
                currentUser);

    }


    public static String getOwnerInfo() throws InvocationTargetException, IllegalAccessException {

        if (isfullMultiUserSys) {
            return getOwnerInfoInFullMUSys();

        } else {
            return getOwnerInfoInNOTFullMUSys();
        }

    }


    public static String getOwnerInfoInFullMUSys() throws InvocationTargetException, IllegalAccessException {

        if (ref_mLockPatternUtils.get().isDeviceOwnerInfoEnabled()) {

            return ref_mLockPatternUtils.get().getDeviceOwnerInfo();
        } else {

            int currentUser = (int) XposedHelpers.findMethodExact(class_com_android_keyguard_KeyguardUpdateMonitor, "getCurrentUser").invoke(null);

            if (ref_mLockPatternUtils.get().isOwnerInfoEnabled(currentUser)) {
                return ref_mLockPatternUtils.get().getOwnerInfo(currentUser);
            }
        }

        return null;
    }


    public static String getOwnerInfoInNOTFullMUSys() throws InvocationTargetException, IllegalAccessException {
        ;
        if ((boolean) XposedHelpers.findMethodBestMatch(LockPatternUtils.class, "isOwnerInfoEnabled").invoke(ref_mLockPatternUtils.get())) {

            int currentUser = (int) XposedHelpers.findMethodBestMatch(LockPatternUtils.class, "getCurrentUser").invoke(ref_mLockPatternUtils.get());

            return ref_mLockPatternUtils.get().getOwnerInfo(currentUser);
        }

        return null;
    }


}
