package top.imlk.oneword.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


import top.imlk.oneword.net.Hitokoto.HitokotoBean;
import top.imlk.oneword.application.client.service.OneWordAutoRefreshService;
import top.imlk.oneword.StaticValue;

import static top.imlk.oneword.StaticValue.SHARED_PER_KEY_APP_VERSION_CODE;
import static top.imlk.oneword.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_CREATOR;
import static top.imlk.oneword.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_CREAT_AT;
import static top.imlk.oneword.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_FROM;
import static top.imlk.oneword.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_ID;
import static top.imlk.oneword.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_LIKE;
import static top.imlk.oneword.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_MSG;
import static top.imlk.oneword.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_TYPE;
import static top.imlk.oneword.StaticValue.SHARED_PER_KEY_FIRSTTIME_INSTALL;
import static top.imlk.oneword.StaticValue.SHARED_PER_KEY_IS_REFRESH_OPENED;
import static top.imlk.oneword.StaticValue.SHARED_PER_KEY_ONEWORD_TYPE_ANIME;
import static top.imlk.oneword.StaticValue.SHARED_PER_KEY_ONEWORD_TYPE_COMIC;
import static top.imlk.oneword.StaticValue.SHARED_PER_KEY_ONEWORD_TYPE_GAME;
import static top.imlk.oneword.StaticValue.SHARED_PER_KEY_ONEWORD_TYPE_INTERNET;
import static top.imlk.oneword.StaticValue.SHARED_PER_KEY_ONEWORD_TYPE_MYSELF;
import static top.imlk.oneword.StaticValue.SHARED_PER_KEY_ONEWORD_TYPE_NOVEL;
import static top.imlk.oneword.StaticValue.SHARED_PER_KEY_ONEWORD_TYPE_OTHER;
import static top.imlk.oneword.StaticValue.SHARED_PER_KEY_REFRESH_MODE;

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


    public static boolean[] readOneWordTypes(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(StaticValue.SHARED_PER_ONEWORD_TYPE_SELECT_STATE, Context.MODE_PRIVATE);
        boolean a = sharedPreferences.getBoolean(SHARED_PER_KEY_ONEWORD_TYPE_ANIME, true);
        boolean b = sharedPreferences.getBoolean(SHARED_PER_KEY_ONEWORD_TYPE_COMIC, true);
        boolean c = sharedPreferences.getBoolean(SHARED_PER_KEY_ONEWORD_TYPE_GAME, true);
        boolean d = sharedPreferences.getBoolean(SHARED_PER_KEY_ONEWORD_TYPE_NOVEL, true);
        boolean e = sharedPreferences.getBoolean(SHARED_PER_KEY_ONEWORD_TYPE_MYSELF, true);
        boolean f = sharedPreferences.getBoolean(SHARED_PER_KEY_ONEWORD_TYPE_INTERNET, true);
        boolean g = sharedPreferences.getBoolean(SHARED_PER_KEY_ONEWORD_TYPE_OTHER, true);

        return new boolean[]{a, b, c, d, e, f, g,};

    }

    public static void saveOneWordTypes(Context context, boolean selectState[]) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(StaticValue.SHARED_PER_ONEWORD_TYPE_SELECT_STATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SHARED_PER_KEY_ONEWORD_TYPE_ANIME, selectState[0]);
        editor.putBoolean(SHARED_PER_KEY_ONEWORD_TYPE_COMIC, selectState[1]);
        editor.putBoolean(SHARED_PER_KEY_ONEWORD_TYPE_GAME, selectState[2]);
        editor.putBoolean(SHARED_PER_KEY_ONEWORD_TYPE_NOVEL, selectState[3]);
        editor.putBoolean(SHARED_PER_KEY_ONEWORD_TYPE_MYSELF, selectState[4]);
        editor.putBoolean(SHARED_PER_KEY_ONEWORD_TYPE_INTERNET, selectState[5]);
        editor.putBoolean(SHARED_PER_KEY_ONEWORD_TYPE_OTHER, selectState[6]);

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
        return OneWordAutoRefreshService.Mode.valueOf(sharedPreferences.getString(SHARED_PER_KEY_REFRESH_MODE, OneWordAutoRefreshService.Mode.TWICE_LOCK.toString()));

    }

    public static void setRefreshMode(Context context, OneWordAutoRefreshService.Mode mode) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(StaticValue.SHARED_PER_AUTO_REFRESH_STATE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SHARED_PER_KEY_REFRESH_MODE, mode.toString());

        editor.commit();

    }

    public static boolean isFirstTimeUse(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(StaticValue.SHARED_PER_USER_INF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        boolean firstTime = sharedPreferences.getBoolean(SHARED_PER_KEY_FIRSTTIME_INSTALL, true);
        if (firstTime) {
            editor.putBoolean(SHARED_PER_KEY_FIRSTTIME_INSTALL, false);
        }


        int versionCode = ApplicationInfoUtil.getAppVersionCode(context);
        if (versionCode > sharedPreferences.getInt(SHARED_PER_KEY_APP_VERSION_CODE, 0)) {
            editor.putInt(SHARED_PER_KEY_APP_VERSION_CODE, versionCode);
            editor.commit();
        }


        return firstTime;

    }

}
