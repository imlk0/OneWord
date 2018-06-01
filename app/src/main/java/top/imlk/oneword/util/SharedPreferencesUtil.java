package top.imlk.oneword.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import net.grandcentrix.tray.AppPreferences;

import top.imlk.oneword.Hitokoto.HitokotoBean;
import top.imlk.oneword.client.OneWordAutoUpdateService;
import top.imlk.oneword.preference.CurrentStatePreference;
import top.imlk.oneword.preference.UpdateStatePreference;

import static top.imlk.oneword.common.StaticValue.SHARED_PER_CURRENT_STATE;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_CREATOR;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_CREAT_AT;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_FROM;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_ID;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_LIKE;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_MSG;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_TYPE;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_IS_UPDATING_OPENED;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_UPDATING_MODE;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_UPDATING_STATE;

/**
 * Created by imlk on 2018/5/26.
 */
public class SharedPreferencesUtil {
    private static final String LOG_TAG = "SharedPreferencesUtil";


    public static HitokotoBean readSavedOneWord(Context context) {


        try {

            CurrentStatePreference currentStatePreference = new CurrentStatePreference(context);

            int id = currentStatePreference.getInt(SHARED_PER_KEY_CURRENT_ONEWORD_ID, -1);
            String type = currentStatePreference.getString(SHARED_PER_KEY_CURRENT_ONEWORD_TYPE, null);
            String msg = currentStatePreference.getString(SHARED_PER_KEY_CURRENT_ONEWORD_MSG, null);
            String from = currentStatePreference.getString(SHARED_PER_KEY_CURRENT_ONEWORD_FROM, null);
            String creator = currentStatePreference.getString(SHARED_PER_KEY_CURRENT_ONEWORD_CREATOR, null);
            String create_at = currentStatePreference.getString(SHARED_PER_KEY_CURRENT_ONEWORD_CREAT_AT, null);
            boolean like = currentStatePreference.getBoolean(SHARED_PER_KEY_CURRENT_ONEWORD_LIKE, false);


            if (id != -1 && (!TextUtils.isEmpty(type)) && (!TextUtils.isEmpty(msg)) && (!TextUtils.isEmpty(from))) {
                return new HitokotoBean(id, msg, type, from, creator, create_at, like);
            }

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LOG_TAG, "error when readSavedOneWord", e);
        }
        return null;
    }

    public static void saveCurOneWord(Context context, HitokotoBean hitokotoBean) {
        try {
            CurrentStatePreference currentStatePreference = new CurrentStatePreference(context);

            currentStatePreference.put(SHARED_PER_KEY_CURRENT_ONEWORD_ID, hitokotoBean.id);
            currentStatePreference.put(SHARED_PER_KEY_CURRENT_ONEWORD_TYPE, hitokotoBean.type);
            currentStatePreference.put(SHARED_PER_KEY_CURRENT_ONEWORD_MSG, hitokotoBean.hitokoto);
            currentStatePreference.put(SHARED_PER_KEY_CURRENT_ONEWORD_FROM, hitokotoBean.from);
            currentStatePreference.put(SHARED_PER_KEY_CURRENT_ONEWORD_CREATOR, hitokotoBean.creator);
            currentStatePreference.put(SHARED_PER_KEY_CURRENT_ONEWORD_CREAT_AT, hitokotoBean.created_at);
            currentStatePreference.put(SHARED_PER_KEY_CURRENT_ONEWORD_LIKE, hitokotoBean.like);

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LOG_TAG, "error when saveCurOneWord", e);
        }
    }


    public static boolean isUpdatingOpened(Context context) {
        try {
            UpdateStatePreference updateStatePreference = new UpdateStatePreference(context);
            return updateStatePreference.getBoolean(SHARED_PER_KEY_IS_UPDATING_OPENED, false);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LOG_TAG, "error when isUpdatingOpened", e);
        }
        return false;
    }

    public static void setUpdatingOpened(Context context, boolean opened) {

        try {
            UpdateStatePreference updateStatePreference = new UpdateStatePreference(context);
            updateStatePreference.put(SHARED_PER_KEY_IS_UPDATING_OPENED, opened);

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LOG_TAG, "error when setUpdatingOpened", e);
        }
    }


    public static OneWordAutoUpdateService.Mode getUpdatingMode(Context context) {
        try {
            UpdateStatePreference updateStatePreference = new UpdateStatePreference(context);


            return OneWordAutoUpdateService.Mode.valueOf(updateStatePreference.getString(SHARED_PER_KEY_UPDATING_MODE, OneWordAutoUpdateService.Mode.EVERY_LOCK.toString()));


        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LOG_TAG, "error when getUpdatingMode", e);
        }
        return OneWordAutoUpdateService.Mode.EVERY_LOCK;
    }

    public static void setUpdatingMode(Context context, OneWordAutoUpdateService.Mode mode) {
        try {
            UpdateStatePreference updateStatePreference = new UpdateStatePreference(context);

            updateStatePreference.put(SHARED_PER_KEY_UPDATING_MODE, mode.toString());

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LOG_TAG, "error when setUpdatingMode", e);
        }
    }

}
