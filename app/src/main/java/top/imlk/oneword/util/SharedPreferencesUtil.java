package top.imlk.oneword.util;

import android.content.Context;
import android.content.SharedPreferences;


import top.imlk.oneword.BuildConfig;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.dao.OneWordSQLiteOpenHelper;
import top.imlk.oneword.application.client.service.OneWordAutoRefreshService;


/**
 * Created by imlk on 2018/5/26.
 */
public class SharedPreferencesUtil {
    private static final String LOG_TAG = "SharedPreferencesUtil";


    public static final String SHARED_PER_CURRENT_STATE = "SHARED_PER_CURRENT_STATE";
    public static final String SHARED_PER_KEY_CURRENT_ONEWORD_ID = "current_id";
//    public static final String SHARED_PER_KEY_CURRENT_ONEWORD_TYPE = "current_type";
//    public static final String SHARED_PER_KEY_CURRENT_ONEWORD_MSG = "current_msg";
//    public static final String SHARED_PER_KEY_CURRENT_ONEWORD_FROM = "current_from";
//    public static final String SHARED_PER_KEY_CURRENT_ONEWORD_CREATOR = "current_creator";
//    public static final String SHARED_PER_KEY_CURRENT_ONEWORD_CREAT_AT = "current_created_at";
//    public static final String SHARED_PER_KEY_CURRENT_ONEWORD_LIKE = "current_like";


    public static final String SHARED_PER_AUTO_REFRESH_STATE = "SHARED_PER_AUTO_REFRESH_STATE";
    public static final String SHARED_PER_KEY_IS_REFRESH_OPENED = "is_refresh_opened";
    public static final String SHARED_PER_KEY_REFRESH_MODE = "frequency_to_refresh";


//    public static final String SHARED_PER_ONEWORD_TYPE_SELECT_STATE = "SHARED_PER_ONEWORD_TYPE_SELECT_STATE";


    public static final String SHARED_PER_USER_INF = "SHARED_PER_USER_INF";
    public static final String SHARED_PER_KEY_FIRSTTIME_INSTALL = "is_first_time_install";
    public static final String SHARED_PER_KEY_APP_VERSION_CODE = "versionCode";


    public static final String SHARED_PER_USER_CONFIG = "SHARED_PER_USER_CONFIG";
    public static final String SHARED_PER_KEY_SELECTED_THEME_NAME = "selected_theme_name";


//    public static final String SHARED_PER_KEY_ONEWORD_TYPE_ANIME = "Anime";
//    public static final String SHARED_PER_KEY_ONEWORD_TYPE_COMIC = "Comic";
//    public static final String SHARED_PER_KEY_ONEWORD_TYPE_GAME = "Game";
//    public static final String SHARED_PER_KEY_ONEWORD_TYPE_NOVEL = "Novel";
//    public static final String SHARED_PER_KEY_ONEWORD_TYPE_MYSELF = "Myself";
//    public static final String SHARED_PER_KEY_ONEWORD_TYPE_INTERNET = "Internet";
//    public static final String SHARED_PER_KEY_ONEWORD_TYPE_OTHER = "Other";


    public static WordBean readSavedOneWord(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PER_CURRENT_STATE, Context.MODE_PRIVATE);

        int id = sharedPreferences.getInt(SHARED_PER_KEY_CURRENT_ONEWORD_ID, -1);

        if (id > 0) {
            return OneWordSQLiteOpenHelper.getInstance().queryOneWordInAllOneWordById(id);
        }

        return null;
    }


    public static void saveCurOneWordId(Context context, int id) {

        if (id > 0) {

            SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PER_CURRENT_STATE, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putInt(SHARED_PER_KEY_CURRENT_ONEWORD_ID, id);

            editor.commit();
        }
    }


//    public static boolean[] readOneWordTypes(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PER_ONEWORD_TYPE_SELECT_STATE, Context.MODE_PRIVATE);
//        boolean a = sharedPreferences.getBoolean(SHARED_PER_KEY_ONEWORD_TYPE_ANIME, true);
//        boolean b = sharedPreferences.getBoolean(SHARED_PER_KEY_ONEWORD_TYPE_COMIC, true);
//        boolean c = sharedPreferences.getBoolean(SHARED_PER_KEY_ONEWORD_TYPE_GAME, true);
//        boolean d = sharedPreferences.getBoolean(SHARED_PER_KEY_ONEWORD_TYPE_NOVEL, true);
//        boolean e = sharedPreferences.getBoolean(SHARED_PER_KEY_ONEWORD_TYPE_MYSELF, true);
//        boolean f = sharedPreferences.getBoolean(SHARED_PER_KEY_ONEWORD_TYPE_INTERNET, true);
//        boolean g = sharedPreferences.getBoolean(SHARED_PER_KEY_ONEWORD_TYPE_OTHER, true);
//
//        return new boolean[]{a, b, c, d, e, f, g,};
//
//    }

//    public static void saveOneWordTypes(Context context, boolean selectState[]) {
//
//        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PER_ONEWORD_TYPE_SELECT_STATE, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        editor.putBoolean(SHARED_PER_KEY_ONEWORD_TYPE_ANIME, selectState[0]);
//        editor.putBoolean(SHARED_PER_KEY_ONEWORD_TYPE_COMIC, selectState[1]);
//        editor.putBoolean(SHARED_PER_KEY_ONEWORD_TYPE_GAME, selectState[2]);
//        editor.putBoolean(SHARED_PER_KEY_ONEWORD_TYPE_NOVEL, selectState[3]);
//        editor.putBoolean(SHARED_PER_KEY_ONEWORD_TYPE_MYSELF, selectState[4]);
//        editor.putBoolean(SHARED_PER_KEY_ONEWORD_TYPE_INTERNET, selectState[5]);
//        editor.putBoolean(SHARED_PER_KEY_ONEWORD_TYPE_OTHER, selectState[6]);
//
//        editor.commit();
//    }

    public static boolean isRefreshOpened(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PER_AUTO_REFRESH_STATE, Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean(SHARED_PER_KEY_IS_REFRESH_OPENED, false);

    }

    public static void setAutoRefreshOpened(Context context, boolean opened) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PER_AUTO_REFRESH_STATE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SHARED_PER_KEY_IS_REFRESH_OPENED, opened);

        editor.commit();

    }


    public static OneWordAutoRefreshService.Mode getRefreshMode(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PER_AUTO_REFRESH_STATE, Context.MODE_PRIVATE);
        return OneWordAutoRefreshService.Mode.valueOf(sharedPreferences.getString(SHARED_PER_KEY_REFRESH_MODE, OneWordAutoRefreshService.Mode.TWICE_LOCK.toString()));

    }

    public static void setRefreshMode(Context context, OneWordAutoRefreshService.Mode mode) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PER_AUTO_REFRESH_STATE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SHARED_PER_KEY_REFRESH_MODE, mode.toString());

        editor.commit();

    }

//    public static void setSelectedThemeName(Context context, String name) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PER_KEY_SELECTED_THEME_NAME, Context.MODE_PRIVATE);
//
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        editor.putString(SHARED_PER_KEY_SELECTED_THEME_NAME, name);
//        editor.commit();
//    }

//    public static String getSelectedThemeName(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PER_KEY_SELECTED_THEME_NAME, Context.MODE_PRIVATE);
//        return sharedPreferences.getString(SHARED_PER_KEY_SELECTED_THEME_NAME, AppStyleHelper.DEFAULT_THEME_NAME);
//    }


    public static void onMainActivityCreate(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PER_USER_INF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        boolean firstTimeInstall = sharedPreferences.getBoolean(SHARED_PER_KEY_FIRSTTIME_INSTALL, true);
        if (firstTimeInstall) {
            editor.putBoolean(SHARED_PER_KEY_FIRSTTIME_INSTALL, false);
            editor.commit();

            onAppFirstTimeInstall(context);
        }


        int curVerCode = BuildConfig.VERSION_CODE;
        if (curVerCode > sharedPreferences.getInt(SHARED_PER_KEY_APP_VERSION_CODE, 0)) {// 更新版本后
            int oldVerCode = sharedPreferences.getInt(SHARED_PER_KEY_APP_VERSION_CODE, 0);

            editor.putInt(SHARED_PER_KEY_APP_VERSION_CODE, curVerCode);
            editor.commit();


            if (oldVerCode > 0 && !firstTimeInstall) {
                onAppUpgrade(context, oldVerCode, curVerCode);
            }


        }
    }

    public static void onAppFirstTimeInstall(Context context) {

        ShowDialogUtil.showAboutAppDialog(context);

    }

    public static void onAppUpgrade(Context context, int oldVerCode, int curVerCode) {
        switch (oldVerCode) {
            case 1:

                SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PER_CURRENT_STATE, Context.MODE_PRIVATE);

                WordBean wordBean = new WordBean();
                wordBean.content = sharedPreferences.getString("current_msg", null);
                wordBean.reference = sharedPreferences.getString("current_from", null);

                if (wordBean.content != null) {
                    wordBean.id = OneWordSQLiteOpenHelper.getInstance().queryIdOfOneWordInAllOneWord(wordBean);
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.remove("current_id");
                editor.remove("current_type");
                editor.remove("current_msg");
                editor.remove("current_from");
                editor.remove("current_creator");
                editor.remove("current_created_at");
                editor.remove("current_like");

                editor.remove("Anime");
                editor.remove("Comic");
                editor.remove("Game");
                editor.remove("Novel");
                editor.remove("Myself");
                editor.remove("Internet");
                editor.remove("Other");

                if (wordBean.id > 0) {
                    editor.putInt(SHARED_PER_KEY_CURRENT_ONEWORD_ID, wordBean.id);
                }

                editor.commit();

                break;
        }
    }

}
