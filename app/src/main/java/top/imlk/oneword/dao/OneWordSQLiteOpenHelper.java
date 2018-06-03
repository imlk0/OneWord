package top.imlk.oneword.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import top.imlk.oneword.Hitokoto.HitokotoBean;
import top.imlk.oneword.util.ApplicationInfoUtil;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE;

/**
 * Created by imlk on 2018/5/26.
 */
public class OneWordSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "OneWordSQLiteOpenHelper";

    private static final String DB_NAME = "oneword.db";
    public static final String TABLE_HISTORY = "history";
    public static final String TABLE_LIKE = "[like]";
    public static final String TABLE_READY_TO_SHOW = "ready_to_show";

    public static final String KEY_ID = "id";
    public static final String KEY_MSG = "msg";
    public static final String KEY_TYPE = "type";
    public static final String KEY_FROM = "[from]";
    public static final String KEY_CREATOR = "creator";
    public static final String KEY_CREATED_AT = "created_at";
    public static final String KEY_ADDED_AT = "added_at";
    public static final String KEY_LIKE = "[like]";


    private static OneWordSQLiteOpenHelper oneWordSQLiteOpenHelper;

    public synchronized static boolean isDataBaseClosed() {

        if (oneWordSQLiteOpenHelper == null) {
            return true;
        }
        return false;
    }

    public static OneWordSQLiteOpenHelper getInstance(Context context) {
        if (oneWordSQLiteOpenHelper == null) {
            oneWordSQLiteOpenHelper = new OneWordSQLiteOpenHelper(context);

        }
        return oneWordSQLiteOpenHelper;
    }

    private OneWordSQLiteOpenHelper(Context context) {//使用versionCode作为数据库版本
        super(context, DB_NAME, null, ApplicationInfoUtil.getAppVersionCode(context));
    }


//    public static void main(String[] args) {
//        System.out.println(
//                "create table "
//                        + TABLE_HISTORY + " ("
//                        + KEY_ID + " integer NOT NULL,"
//                        + KEY_MSG + " NTEXT,"
//                        + KEY_TYPE + " CHAR(1),"
//                        + KEY_FROM + " NTEXT,"
//                        + KEY_CREATOR + " NTEXT,"
//                        + KEY_CREATED_AT + " DATE,"
//                        + KEY_ADDED_AT + " DATE,"
//                        + KEY_LIKE + " BOOLEAN,"
//                        + "PRIMARY KEY(" + KEY_ID + "," + KEY_TYPE + "))");
//    }

    //第一次创建数据库时被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "
                + TABLE_HISTORY + " ("
                + KEY_ID + " integer NOT NULL,"
                + KEY_MSG + " NTEXT,"
                + KEY_TYPE + " CHAR(1),"
                + KEY_FROM + " NTEXT,"
                + KEY_CREATOR + " NTEXT,"
                + KEY_CREATED_AT + " DATE,"
                + KEY_ADDED_AT + " DATE,"
                + KEY_LIKE + " BOOLEAN,"
                + "PRIMARY KEY(" + KEY_ID + "," + KEY_TYPE + "))");
        db.execSQL("create table "
                + TABLE_LIKE + " ("
                + KEY_ID + " integer NOT NULL,"
                + KEY_MSG + " NTEXT,"
                + KEY_TYPE + " CHAR(1),"
                + KEY_FROM + " NTEXT,"
                + KEY_CREATOR + " NTEXT,"
                + KEY_CREATED_AT + " DATE,"
                + KEY_ADDED_AT + " DATE,"
                + "PRIMARY KEY(" + KEY_ID + "," + KEY_TYPE + "))");
        db.execSQL("create table "
                + TABLE_READY_TO_SHOW + " ("
                + KEY_ID + " integer NOT NULL,"
                + KEY_MSG + " NTEXT,"
                + KEY_TYPE + " CHAR(1),"
                + KEY_FROM + " NTEXT,"
                + KEY_CREATOR + " NTEXT,"
                + KEY_CREATED_AT + " DATE,"
                + KEY_ADDED_AT + " DATE, "
                + "PRIMARY KEY(" + KEY_ID + "," + KEY_TYPE + "))");

    }

    public long insert_one_item(String tableName, HitokotoBean hitokotoBean) {
        synchronized (this) {

            SQLiteDatabase sqLiteDatabase = getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_ID, hitokotoBean.id);
            contentValues.put(KEY_MSG, hitokotoBean.hitokoto);
            contentValues.put(KEY_TYPE, hitokotoBean.type);
            contentValues.put(KEY_FROM, hitokotoBean.from);
            contentValues.put(KEY_CREATOR, hitokotoBean.creator);
            contentValues.put(KEY_CREATED_AT, hitokotoBean.created_at);
            contentValues.put(KEY_ADDED_AT, System.currentTimeMillis());

            if (OneWordSQLiteOpenHelper.TABLE_HISTORY.equals(tableName)) {
                contentValues.put(KEY_LIKE, hitokotoBean.like);
            }

            return sqLiteDatabase.insertWithOnConflict(tableName, null, contentValues, CONFLICT_REPLACE);

        }
    }

    public long count_a_table(String tableName) {
        synchronized (this) {

            SQLiteDatabase sqLiteDatabase = getReadableDatabase();

            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + tableName, null);

            int t = cursor.getCount();
            cursor.close();
            return t;
        }
    }

    public long remove_one_item(String table_name, HitokotoBean hitokotoBean) {
        if (hitokotoBean == null) {
            return -1;
        }
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        long t = sqLiteDatabase.delete(table_name, KEY_ID + " = ? and " + KEY_TYPE + " = ?", new String[]{hitokotoBean.id + "", hitokotoBean.type});
        return t;
    }


    public HitokotoBean get_a_item_order_by(String tableName, String order) {//ascend
        synchronized (this) {

            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + tableName + " ORDER BY " + order + " LIMIT 1", null);

            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToNext();


            HitokotoBean hitokotoBean = null;
            switch (tableName) {
                case TABLE_HISTORY:
                    hitokotoBean = new HitokotoBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(7) != 0);
                    break;
                case TABLE_LIKE:
                    hitokotoBean = new HitokotoBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), true);
                    break;
                case TABLE_READY_TO_SHOW:
                    hitokotoBean = new HitokotoBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), false);
                    break;
            }
            cursor.close();

            return hitokotoBean;
        }
    }

    public ArrayList<HitokotoBean> get_a_bundle_of_item(String tableName, int num, int start) {//descend
        synchronized (this) {

            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + tableName + " ORDER BY " + KEY_ADDED_AT + " DESC LIMIT ? OFFSET ?", new String[]{num + "", start + ""});
            ArrayList<HitokotoBean> arrayList = new ArrayList<>(num);
            switch (tableName) {
                case TABLE_HISTORY:
                    for (; cursor.moveToNext(); ) {
                        arrayList.add(new HitokotoBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(7) != 0));
                    }
                    break;
                case TABLE_LIKE:
                    for (; cursor.moveToNext(); ) {
                        arrayList.add(new HitokotoBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), true));
                    }
                    break;
                case TABLE_READY_TO_SHOW:
                    for (; cursor.moveToNext(); ) {
                        arrayList.add(new HitokotoBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), false));
                    }
                    break;
            }

            cursor.close();

            return arrayList;
        }
    }


    public boolean query_one_item_exist(String table_name, HitokotoBean hitokotoBean) {
        synchronized (this) {

            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + table_name + " WHERE " + KEY_ID + " =? AND " + KEY_TYPE + " =?" + " ORDER BY " + KEY_ADDED_AT + " LIMIT 1", new String[]{hitokotoBean.id + "", hitokotoBean.type});
            if (cursor.getCount() == 0) {
                cursor.close();
                return false;
            } else {
                cursor.close();
                return true;
            }
        }
    }

    public void refresh_like_state(HitokotoBean hitokotoBean) {
        synchronized (this) {

            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            try {
                sqLiteDatabase.execSQL("UPDATE " + TABLE_HISTORY + " SET " + KEY_LIKE + " =? WHERE " + KEY_ID + " =? AND " + KEY_TYPE + " =?", new String[]{hitokotoBean.like ? "1" : "0", hitokotoBean.id + "", hitokotoBean.type});
            } catch (Exception e) {
                Log.e(LOG_TAG, "refresh_like_state", e);
            }
        }
    }

    public void refresh_addedTime_state(String tableName, HitokotoBean hitokotoBean) {
        synchronized (this) {

            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            try {

                sqLiteDatabase.execSQL("UPDATE " + tableName + " SET " + KEY_ADDED_AT + " =? WHERE " + KEY_ID + " =? AND " + KEY_TYPE + " =?", new String[]{System.currentTimeMillis() + "", hitokotoBean.id + "", hitokotoBean.type});
            } catch (Exception e) {
                Log.e(LOG_TAG, "refresh_like_state", e);
            }
        }
    }

    //数据库版本发生更新后调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
