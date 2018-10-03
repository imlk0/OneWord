package top.imlk.oneword.app.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.dao.OneWordDBHelper;

/**
 * Created by imlk on 2018/9/26.
 */
public class OneWordDBResolverHelper {

    public static WordBean queryOneWordInAllOneWordById(Context context, int id) {

        if (id <= 0) {
            return null;
        }

        Cursor cursor = context.getContentResolver().query(OneWordDBProvider.URI_ALL_ONEWORD, null, OneWordDBHelper.KEY_ID + " =?", new String[]{String.valueOf(id)}, null);

        try {
            if (cursor.moveToNext()) {
                return new WordBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            }
            return null;
        } finally {
            cursor.close();
        }
    }

    public static int countToShow(Context context) {
        Cursor cursor = context.getContentResolver().query(OneWordDBProvider.URI_TOSHOW, null, null, null, null);
        try {
            return cursor.getCount();
        } finally {
            cursor.close();
        }
    }

    public static WordBean queryOneWordFromToShowByASC(Context context) {
        Cursor cursor = context.getContentResolver().query(OneWordDBProvider.URI_TOSHOW, null, null, null, "ASC");
        try {
            if (cursor.moveToNext()) {
                return queryOneWordInAllOneWordById(context, cursor.getInt(0));
            }
            return null;
        } finally {
            cursor.close();
        }
    }


    public static WordBean queryOneWordFromHistoryByRandom(Context context) {
        Cursor cursor = context.getContentResolver().query(OneWordDBProvider.URI_HISTORY, null, null, null, "random()");
        try {
            if (cursor.moveToNext()) {
                return queryOneWordInAllOneWordById(context, cursor.getInt(0));
            }
            return null;
        } finally {
            cursor.close();
        }
    }

    public static WordBean queryOneWordFromFavorByRandom(Context context) {
        Cursor cursor = context.getContentResolver().query(OneWordDBProvider.URI_FAVOR, null, null, null, "random()");
        try {
            if (cursor.moveToNext()) {
                return queryOneWordInAllOneWordById(context, cursor.getInt(0));
            }
            return null;
        } finally {
            cursor.close();
        }
    }

    public static void removeFromToShow(Context context, int id) {
        if (id <= 0) {
            return;
        }
        context.getContentResolver().delete(OneWordDBProvider.URI_TOSHOW, OneWordDBHelper.KEY_ONEWORD_ID + " =?", new String[]{String.valueOf(id)});
    }


    public static void insertToHistory(Context context, WordBean wordBean) {
        insertToSubTable(context, OneWordDBProvider.URI_HISTORY, wordBean);
    }

    public static void insertToToShow(Context context, WordBean wordBean) {
        insertToSubTable(context, OneWordDBProvider.URI_TOSHOW, wordBean);
    }

    public static void insertToSubTable(Context context, Uri subTable, WordBean wordBean) {
        if (wordBean == null) {
            return;
        }
        synchronized (OneWordDBResolverHelper.class) {

            int id = wordBean.id;

            if (id <= 0) {
                id = queryIdOfOneWordInAllOneWord(context, wordBean);
                if (id <= 0) {
                    // 未收录
                    id = insertOneWordWithoutCheck(context, wordBean);
                    if (id <= 0) {
                        return;
                    }
                }
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put(OneWordDBHelper.KEY_ONEWORD_ID, id);
            contentValues.put(OneWordDBHelper.KEY_ADDED_AT, System.currentTimeMillis());

            context.getContentResolver().insert(subTable, contentValues);
        }
    }

    private static int insertOneWordWithoutCheck(Context context, WordBean wordBean) {
        if (wordBean == null) {
            return -1;
        }

        ContentValues contentValues = new ContentValues();


        // id 是自增列，不需要填值
        contentValues.put(OneWordDBHelper.KEY_CONTENT, OneWordDBHelper.nullToVoid(wordBean.content));
        contentValues.put(OneWordDBHelper.KEY_REFERENCE, OneWordDBHelper.nullToVoid(wordBean.reference));
        contentValues.put(OneWordDBHelper.KEY_TARGET_URL, wordBean.target_url);
        contentValues.put(OneWordDBHelper.KEY_TARGET_TEXT, wordBean.target_text);

        int id = (int) ContentUris.parseId(context.getContentResolver().insert(OneWordDBProvider.URI_ALL_ONEWORD, contentValues));

        wordBean.id = id;
        return id;
    }


    public static int queryIdOfOneWordInAllOneWord(Context context, WordBean wordBean) {
        synchronized (OneWordDBResolverHelper.class) {

            Cursor cursor = context.getContentResolver().query(OneWordDBProvider.URI_ALL_ONEWORD, new String[]{OneWordDBHelper.KEY_ID}, OneWordDBHelper.KEY_CONTENT + "=? AND " + OneWordDBHelper.KEY_REFERENCE + "=?", new String[]{
                    OneWordDBHelper.nullToVoid(wordBean.content), OneWordDBHelper.nullToVoid(wordBean.reference)}, null);
            ;
            try {
                int result = -1;
                if (cursor.moveToNext()) {
                    result = cursor.getInt(0);
                }
                wordBean.id = result;
                return result;

            } finally {
                cursor.close();
            }
        }
    }
}
