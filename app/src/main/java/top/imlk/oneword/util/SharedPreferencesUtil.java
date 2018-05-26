package top.imlk.oneword.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import top.imlk.oneword.Hitokoto.HitokotoBean;

import static top.imlk.oneword.common.StaticValue.SHARED_PER_CURRENT_STATE;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_CREATOR;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_CREAT_AT;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_FROM;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_ID;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_LIKE;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_MSG;
import static top.imlk.oneword.common.StaticValue.SHARED_PER_KEY_CURRENT_ONEWORD_TYPE;

/**
 * Created by imlk on 2018/5/26.
 */
public class SharedPreferencesUtil {

    public static HitokotoBean readSavedOneWord(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PER_CURRENT_STATE, Context.MODE_PRIVATE);

        int id = sharedPreferences.getInt(SHARED_PER_KEY_CURRENT_ONEWORD_ID, -1);
        String type = sharedPreferences.getString(SHARED_PER_KEY_CURRENT_ONEWORD_TYPE, null);
        String msg = sharedPreferences.getString(SHARED_PER_KEY_CURRENT_ONEWORD_MSG, null);
        String from = sharedPreferences.getString(SHARED_PER_KEY_CURRENT_ONEWORD_FROM, null);
        String creator = sharedPreferences.getString(SHARED_PER_KEY_CURRENT_ONEWORD_CREATOR, null);
        String create_at = sharedPreferences.getString(SHARED_PER_KEY_CURRENT_ONEWORD_CREAT_AT, null);
        boolean like = sharedPreferences.getBoolean(SHARED_PER_KEY_CURRENT_ONEWORD_LIKE, false);


        if (id != -1 && (!TextUtils.isEmpty(type)) && (!TextUtils.isEmpty(msg)) && (!TextUtils.isEmpty(from))) {
            return new HitokotoBean(id, msg, type, from, creator, create_at, like);
        }
        return null;
    }

    public static void saveCurOneWord(Context context, HitokotoBean hitokotoBean) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PER_CURRENT_STATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(SHARED_PER_KEY_CURRENT_ONEWORD_ID, hitokotoBean.id);
        editor.putString(SHARED_PER_KEY_CURRENT_ONEWORD_TYPE, hitokotoBean.type);
        editor.putString(SHARED_PER_KEY_CURRENT_ONEWORD_MSG, hitokotoBean.hitokoto);
        editor.putString(SHARED_PER_KEY_CURRENT_ONEWORD_FROM, hitokotoBean.from);
        editor.putString(SHARED_PER_KEY_CURRENT_ONEWORD_CREATOR, hitokotoBean.creator);
        editor.putString(SHARED_PER_KEY_CURRENT_ONEWORD_CREAT_AT, hitokotoBean.created_at);
        editor.putBoolean(SHARED_PER_KEY_CURRENT_ONEWORD_LIKE, hitokotoBean.like);
        editor.apply();

    }
}
