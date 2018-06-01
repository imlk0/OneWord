package top.imlk.oneword.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import top.imlk.oneword.Hitokoto.HitokotoBean;

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

    private static final String KEY_ID = "id";
    private static final String KEY_MSG = "msg";
    private static final String KEY_TYPE = "type";
    private static final String KEY_FROM = "[from]";
    private static final String KEY_CREATOR = "creator";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_ADDED_AT = "added_at";
    private static final String KEY_LIKE = "[like]";


    private static OneWordSQLiteOpenHelper oneWordSQLiteOpenHelper;

    public static boolean isDataBasegClosed() {
        if (oneWordSQLiteOpenHelper == null) {
            return true;
        }
        return false;
    }

    public static OneWordSQLiteOpenHelper getInstance(Context context) {
        if (oneWordSQLiteOpenHelper == null) {
            try {
                oneWordSQLiteOpenHelper = new OneWordSQLiteOpenHelper(context);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return oneWordSQLiteOpenHelper;
    }

    private OneWordSQLiteOpenHelper(Context context) throws PackageManager.NameNotFoundException {//使用versionCode作为数据库版本
        super(context, DB_NAME, null, context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
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
        db.execSQL("create table " + TABLE_READY_TO_SHOW + " ("
                + KEY_ID + " integer NOT NULL,"
                + KEY_MSG + " NTEXT,"
                + KEY_TYPE + " CHAR(1),"
                + KEY_FROM + " NTEXT,"
                + KEY_CREATOR + " NTEXT,"
                + KEY_CREATED_AT + " DATE,"
                + KEY_ADDED_AT + " DATE, "
                + "PRIMARY KEY(" + KEY_ID + "," + KEY_TYPE + "))");

    }

    public long insert_to_history(HitokotoBean hitokotoBean) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, hitokotoBean.id);
        contentValues.put(KEY_MSG, hitokotoBean.hitokoto);
        contentValues.put(KEY_TYPE, hitokotoBean.type);
        contentValues.put(KEY_FROM, hitokotoBean.from);
        contentValues.put(KEY_CREATOR, hitokotoBean.creator);
        contentValues.put(KEY_CREATED_AT, hitokotoBean.created_at);
        contentValues.put(KEY_ADDED_AT, System.currentTimeMillis());
        contentValues.put(KEY_LIKE, hitokotoBean.like);

        long t = sqLiteDatabase.insertWithOnConflict(TABLE_HISTORY, null, contentValues, CONFLICT_REPLACE);

        return t;
    }

    public long insert_to_like(HitokotoBean hitokotoBean) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, hitokotoBean.id);
        contentValues.put(KEY_MSG, hitokotoBean.hitokoto);
        contentValues.put(KEY_TYPE, hitokotoBean.type);
        contentValues.put(KEY_FROM, hitokotoBean.from);
        contentValues.put(KEY_CREATOR, hitokotoBean.creator);
        contentValues.put(KEY_CREATED_AT, hitokotoBean.created_at);
        contentValues.put(KEY_ADDED_AT, System.currentTimeMillis());

        long t = sqLiteDatabase.insertWithOnConflict(TABLE_LIKE, null, contentValues, CONFLICT_REPLACE);
        return t;
    }


    public long insert_to_ready(HitokotoBean hitokotoBean) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, hitokotoBean.id);
        contentValues.put(KEY_MSG, hitokotoBean.hitokoto);
        contentValues.put(KEY_TYPE, hitokotoBean.type);
        contentValues.put(KEY_FROM, hitokotoBean.from);
        contentValues.put(KEY_CREATOR, hitokotoBean.creator);
        contentValues.put(KEY_CREATED_AT, hitokotoBean.created_at);
        contentValues.put(KEY_ADDED_AT, System.currentTimeMillis());

        long t = sqLiteDatabase.insertWithOnConflict(TABLE_READY_TO_SHOW, null, contentValues, CONFLICT_REPLACE);
        return t;
    }

    public long remove_one_item(HitokotoBean hitokotoBean, String table_name) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        long t = sqLiteDatabase.delete(table_name, KEY_ID + " = ? and " + KEY_TYPE + " = ?", new String[]{hitokotoBean.id + "", hitokotoBean.type});
        return t;
    }


    public HitokotoBean get_a_ready_to_show_item() {//ascend
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_READY_TO_SHOW + " ORDER BY " + KEY_ADDED_AT + " ASC LIMIT 1", null);

        if (cursor.isAfterLast()) {
            return null;
        }

        HitokotoBean hitokotoBean = new HitokotoBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), false);// TODO 默认dislike

        cursor.close();

        remove_one_item(hitokotoBean, TABLE_READY_TO_SHOW);

        return hitokotoBean;
    }

    public ArrayList<HitokotoBean> get_a_bundle_of_item(String table_name, int num, int start) {//descend
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + table_name + " ORDER BY " + KEY_ADDED_AT + " DESC LIMIT ? OFFSET ?", new String[]{num + "", start + ""});
        ArrayList<HitokotoBean> arrayList = new ArrayList<>(num);
        switch (table_name) {
            case TABLE_HISTORY:
                for (; cursor.moveToNext(); ) {
                    arrayList.add(new HitokotoBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6) != 0));
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

        return arrayList;
    }


    public boolean query_one_item_exist(HitokotoBean hitokotoBean, String table_name) {
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

    public void update_like(HitokotoBean hitokotoBean) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        try {
            sqLiteDatabase.execSQL("UPDATE " + TABLE_HISTORY + " SET " + KEY_LIKE + " =? WHERE " + KEY_ID + " =? AND " + KEY_TYPE + " =?", new String[]{hitokotoBean.like + "", hitokotoBean.id + "", hitokotoBean.type});
        } catch (Exception e) {
            Log.e(LOG_TAG, "update_like", e);
        }
    }

    //数据库版本发生更新后调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
