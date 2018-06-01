package top.imlk.oneword.preference;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import net.grandcentrix.tray.TrayPreferences;
import net.grandcentrix.tray.core.TrayStorage;

import top.imlk.oneword.common.StaticValue;

/**
 * Created by imlk on 2018/5/31.
 */
public class UpdateStatePreference extends TrayPreferences {
    public UpdateStatePreference(@NonNull Context context, TrayStorage.Type type) throws PackageManager.NameNotFoundException {
        super(context, StaticValue.SHARED_PER_UPDATING_STATE, context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode, type);
    }

    public UpdateStatePreference(@NonNull Context context) throws PackageManager.NameNotFoundException {
        super(context, StaticValue.SHARED_PER_UPDATING_STATE, context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
    }

}
