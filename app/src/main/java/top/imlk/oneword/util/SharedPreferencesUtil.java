package top.imlk.oneword.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


import top.imlk.oneword.Hitokoto.HitokotoBean;
import top.imlk.oneword.client.OneWordAutoRefreshService;
import top.imlk.oneword.common.StaticValue;

import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_CREATOR;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_CREAT_AT;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_FROM;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_ID;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_LIKE;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_MSG;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_TYPE;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_IS_REFRESH_OPENED;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_REFRESH_MODE;

/**
 * Created by imlk on 2018/5/26.
 */
public class SharedPreferencesUtil {
    private static final String LOG_TAG = "SharedPreferencesUtil";


    public static HitokotoBean readSavedOneWord(Context context) {


//        try {

        SharedPreferences sharedPreferences = context.getSharedPreferences(StaticValue.SHARED_PER_CURRENT_STATE, Context.MODE_PRIVATE);

        int id = sharedPreferences.getInt(SHARED_PER_KEY_CURRENT_ONEWORD_ID, -1);
        String type = sharedPreferences.getString(SHARED_PER_KEY_CURRENT_ONEWORD_TYPE, null);
        String msg = sharedPreferences.getString(SHARED_PER_KEY_CURRENT_ONEWORD_MSG, null);
        String from = sharedPreferences.getString(SHARED_PER_KEY_CURRENT_ONEWORD_FROM, null);
        String creator = sharedPreferences.getString(SHARED_PER_KEY_CURRENT_ONEWORD_CREATOR, null);
        String create_at = sharedPreferences.getString(SHARED_PER_KEY_CURRENT_ONEWORD_CREAT_AT, null);
        boolean like = sharedPreferences.getBoolean(SHARED_PER_KEY_CURRENT_ONEWORD_LIKE, false);


//            CurrentStatePreference currentStatePreference = new CurrentStatePreference(context);
//
//            int id = currentStatePreference.getInt(SHARED_PER_KEY_CURRENT_ONEWORD_ID, -1);
//            String type = currentStatePreference.getString(SHARED_PER_KEY_CURRENT_ONEWORD_TYPE, null);
//            String msg = currentStatePreference.getString(SHARED_PER_KEY_CURRENT_ONEWORD_MSG, null);
//            String from = currentStatePreference.getString(SHARED_PER_KEY_CURRENT_ONEWORD_FROM, null);
//            String creator = currentStatePreference.getString(SHARED_PER_KEY_CURRENT_ONEWORD_CREATOR, null);
//            String create_at = currentStatePreference.getString(SHARED_PER_KEY_CURRENT_ONEWORD_CREAT_AT, null);
//            boolean like = currentStatePreference.getBoolean(SHARED_PER_KEY_CURRENT_ONEWORD_LIKE, false);


        if (id != -1 && (!TextUtils.isEmpty(type)) && (!TextUtils.isEmpty(msg)) && (!TextUtils.isEmpty(from))) {
            return new HitokotoBean(id, msg, type, from, creator, create_at, like);
        }

//        } catch (PackageManager.NameNotFoundException e) {
//            Log.e(LOG_TAG, "error when readSavedOneWord", e);
//        }
        return null;
    }

    public static void saveCurOneWord(Context context, HitokotoBean hitokotoBean) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(StaticValue.SHARED_PER_CURRENT_STATE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(SHARED_PER_KEY_CURRENT_ONEWORD_ID, hitokotoBean.id);
        editor.putString(SHARED_PER_KEY_CURRENT_ONEWORD_TYPE, hitokotoBean.type);
        editor.putString(SHARED_PER_KEY_CURRENT_ONEWORD_MSG, hitokotoBean.hitokoto);
        editor.putString(SHARED_PER_KEY_CURRENT_ONEWORD_FROM, hitokotoBean.from);
        editor.putString(SHARED_PER_KEY_CURRENT_ONEWORD_CREATOR, hitokotoBean.creator);
        editor.putString(SHARED_PER_KEY_CURRENT_ONEWORD_CREAT_AT, hitokotoBean.created_at);
        editor.putBoolean(SHARED_PER_KEY_CURRENT_ONEWORD_LIKE, hitokotoBean.like);

        editor.commit();

    }


    public static boolean isRefreshOpened(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(StaticValue.SHARED_PER_AUTO_REFRESH_STATE, Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean(SHARED_PER_KEY_IS_REFRESH_OPENED, false);

    }

    public static void setAutoRefreshOpened(Context context, boolean opened) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(StaticValue.SHARED_PER_AUTO_REFRESH_STATE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SHARED_PER_KEY_IS_REFRESH_OPENED, opened);

        editor.commit();

    }


    public static OneWordAutoRefreshService.Mode getRefreshMode(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(StaticValue.SHARED_PER_AUTO_REFRESH_STATE, Context.MODE_PRIVATE);
        return OneWordAutoRefreshService.Mode.valueOf(sharedPreferences.getString(SHARED_PER_KEY_REFRESH_MODE, OneWordAutoRefreshService.Mode.EVERY_LOCK.toString()));

    }

    public static void setRefreshMode(Context context, OneWordAutoRefreshService.Mode mode) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(StaticValue.SHARED_PER_AUTO_REFRESH_STATE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SHARED_PER_KEY_REFRESH_MODE, mode.toString());

        editor.commit();

    }

}
