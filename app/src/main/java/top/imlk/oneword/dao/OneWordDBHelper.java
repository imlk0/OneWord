package top.imlk.oneword.dao;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.ArrayList;

import top.imlk.oneword.BuildConfig;
import top.imlk.oneword.application.client.provider.OneWordDBProvider;
import top.imlk.oneword.bean.ApiBean;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.util.AppStatus;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE;
import static android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE;
import static top.imlk.oneword.application.client.provider.OneWordDBProvider.URI_ALL_ONEWORD;

/**
 * Created by imlk on 2018/5/26.
 */
public class OneWordDBHelper {

    private static final String LOG_TAG = "OneWordDBHelper";

    private static final String DB_NAME = "oneword.db";
//    public static final String TABLE_HISTORY = "history";
//    public static final String TABLE_LIKE = "[like]";
//    public static final String TABLE_READY_TO_SHOW = "ready_to_show";

    public static final String TABLE_ALL_ONEWORD = "all_oneword";
    public static final String TABLE_FAVOR = "favor";
    public static final String TABLE_HISTORY = "history";
    public static final String TABLE_TOSHOW = "toshow";
    public static final String TABLE_API = "api";

    public static final String KEY_ID = "id";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_REFERENCE = "reference";
    public static final String KEY_TARGET_URL = "target_url";
    public static final String KEY_TARGET_TEXT = "target_text";

    public static final String KEY_ONEWORD_ID = "oneword_id";
    public static final String KEY_ADDED_AT = "added_at";

    public static final String KEY_NAME = "name";
    public static final String KEY_URL = "url";
    public static final String KEY_REQ_METHOD = "req_method";
    public static final String KEY_REQ_ARGS_JSON = "req_args_json";
    public static final String KEY_RESP_FORM = "resp_form";
    public static final String KEY_ENABLED = "enabled";


//    public static final String KEY_ID = "id";
//    public static final String KEY_MSG = "msg";
//    public static final String KEY_TYPE = "type";
//    public static final String KEY_FROM = "[from]";
//    public static final String KEY_CREATOR = "creator";
//    public static final String KEY_CREATED_AT = "created_at";
//    public static final String KEY_ADDED_AT = "added_at";
//    public static final String KEY_LIKE = "[like]";


    private static MyOpenHelper myOpenHelper;

    public static MyOpenHelper getOpenHelperInstance() {
        if (!AppStatus.isMianProcess()) {
            throw new UnsupportedOperationException("only the main process could instantiate the MyOpenHelper");
        }
        if (myOpenHelper == null) {
            synchronized (OneWordDBHelper.class) {
                if (myOpenHelper == null) {
                    myOpenHelper = new MyOpenHelper(AppStatus.getRunningApplication());
                }
            }
        }
        return myOpenHelper;
    }

    public synchronized static void closeDataBase() {
        if (!OneWordDBHelper.isDataBaseClosed()) {
            OneWordDBHelper.getOpenHelperInstance().close();
        }
    }

    public synchronized static boolean isDataBaseClosed() {
        if (myOpenHelper == null) {
            return true;
        }
        return false;
    }


    /**
     * 往 {@link #TABLE_ALL_ONEWORD} 里加入数据
     * 不查重
     *
     * @return id
     */
    public static int insertOneWordWithoutCheck(WordBean wordBean) {
        if (wordBean == null) {
            return -1;
        }
        synchronized (OneWordDBHelper.class) {
            ContentValues contentValues = new ContentValues();


            // id 是自增列，不需要填值
            contentValues.put(KEY_CONTENT, nullToVoid(wordBean.content));
            contentValues.put(KEY_REFERENCE, nullToVoid(wordBean.reference));
            contentValues.put(KEY_TARGET_URL, wordBean.target_url);
            contentValues.put(KEY_TARGET_TEXT, wordBean.target_text);

            int id;
            if (AppStatus.isMianProcess()) {
                SQLiteDatabase sqLiteDatabase = getOpenHelperInstance().getWritableDatabase();
                id = (int) sqLiteDatabase.insertWithOnConflict(TABLE_ALL_ONEWORD, null, contentValues, CONFLICT_IGNORE);
            } else {
                id = (int) ContentUris.parseId(AppStatus.getRunningApplication().getContentResolver().insert(URI_ALL_ONEWORD, contentValues));
            }

            wordBean.id = id;

            return id;
        }
    }

    /**
     * 查询一条一言的总表ID
     *
     * @param wordBean
     * @return 一言的总表ID，不存在则返回 -1
     */
    public static int queryIdOfOneWordInAllOneWord(WordBean wordBean) {
        synchronized (OneWordDBHelper.class) {

            Cursor cursor;
            if (AppStatus.isMianProcess()) {

                SQLiteDatabase sqLiteDatabase = getOpenHelperInstance().getReadableDatabase();
                cursor = sqLiteDatabase.rawQuery("SELECT " + KEY_ID + " FROM " + TABLE_ALL_ONEWORD + " WHERE " + KEY_CONTENT + "=? AND " + KEY_REFERENCE + "=?", new String[]{
                        nullToVoid(wordBean.content), nullToVoid(wordBean.reference)
                });

            } else {
                cursor = AppStatus.getRunningApplication().getContentResolver().query(URI_ALL_ONEWORD, new String[]{OneWordDBHelper.KEY_ID}, OneWordDBHelper.KEY_CONTENT + "=? AND " + OneWordDBHelper.KEY_REFERENCE + "=?", new String[]{
                        OneWordDBHelper.nullToVoid(wordBean.content), OneWordDBHelper.nullToVoid(wordBean.reference)}, null);
            }

            int result = -1;

            if (cursor.moveToNext()) {
                result = cursor.getInt(0);
            }
            wordBean.id = result;
            cursor.close();
            return result;
        }
    }

    public static WordBean queryOneWordInAllOneWordById(int id) {

        if (id <= 0) {
            return null;
        }

        synchronized (OneWordDBHelper.class) {

            Cursor cursor;
            if (AppStatus.isMianProcess()) {

                SQLiteDatabase sqLiteDatabase = getOpenHelperInstance().getReadableDatabase();

                cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_ALL_ONEWORD + " WHERE " + KEY_ID + " =?", new String[]{String.valueOf(id)});
            } else {
                cursor = AppStatus.getRunningApplication().getContentResolver().query(URI_ALL_ONEWORD, null, OneWordDBHelper.KEY_ID + " =?", new String[]{String.valueOf(id)}, null);

            }

            WordBean result = null;

            if (cursor.moveToNext()) {
                result = new WordBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            }

            cursor.close();

            return result;
        }
    }

    /**
     * 从总表中删除一个
     * 注意由于外键关联，从总表中删除后，关联的表中的行也会删除
     *
     * @param wordBean
     */
    public static void removeOneWordInAllOneWord(WordBean wordBean) {
        synchronized (OneWordDBHelper.class) {

            int id = queryIdOfOneWordInAllOneWord(wordBean);

            if (id <= 0) {
                return;
            }

            if (AppStatus.isMianProcess()) {

                SQLiteDatabase sqLiteDatabase = getOpenHelperInstance().getWritableDatabase();
                sqLiteDatabase.execSQL("DELETE FROM " + TABLE_ALL_ONEWORD + " WHERE " + KEY_ID + "=?", new Object[]{id});
            } else {
                AppStatus.getRunningApplication().getContentResolver().delete(URI_ALL_ONEWORD, KEY_ID + " =?", new String[]{String.valueOf(id)});
            }

        }
    }


    public static void insertToHistory(WordBean wordBean) {
        insertToSubTable(TABLE_HISTORY, wordBean);
    }

    public static void insertToFavor(WordBean wordBean) {
        insertToSubTable(TABLE_FAVOR, wordBean);
    }

    public static void insertToToShow(WordBean wordBean) {
        insertToSubTable(TABLE_TOSHOW, wordBean);
    }

    /**
     * 插入到子表，冲突处理方式为Replace
     *
     * @param wordBean
     * @param tableName
     */

    private static void insertToSubTable(String tableName, WordBean wordBean) {
        if (wordBean == null) {
            return;
        }
        synchronized (OneWordDBHelper.class) {

            int id = wordBean.id;

            if (id <= 0) {
                id = queryIdOfOneWordInAllOneWord(wordBean);
                if (id <= 0) {
                    // 未收录
                    id = insertOneWordWithoutCheck(wordBean);
                    if (id <= 0) {
                        return;
                    }
                }
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_ONEWORD_ID, id);
            contentValues.put(KEY_ADDED_AT, System.currentTimeMillis());


            if (AppStatus.isMianProcess()) {

                SQLiteDatabase sqLiteDatabase = getOpenHelperInstance().getWritableDatabase();
                sqLiteDatabase.insertWithOnConflict(tableName, null, contentValues, CONFLICT_REPLACE);
            } else {
                AppStatus.getRunningApplication().getContentResolver().insert(OneWordDBProvider.getUriByTableName(tableName), contentValues);
            }
        }
    }

    public static boolean checkIfInHistory(int id) {
        return checkIfInSubTable(TABLE_HISTORY, id);
    }

    public static boolean checkIfInFavor(int id) {
        return checkIfInSubTable(TABLE_FAVOR, id);
    }

    public static boolean checkIfInToShow(int id) {
        return checkIfInSubTable(TABLE_TOSHOW, id);
    }

    private static boolean checkIfInSubTable(String tableName, int id) {

        if (id <= 0) {
            return false;
        }

        synchronized (OneWordDBHelper.class) {
            Cursor cursor;

            if (AppStatus.isMianProcess()) {
                SQLiteDatabase sqLiteDatabase = getOpenHelperInstance().getReadableDatabase();
                cursor = sqLiteDatabase.rawQuery("SELECT " + KEY_ONEWORD_ID + " FROM " + tableName + " WHERE " + KEY_ONEWORD_ID + "=?",
                        new String[]{String.valueOf(id)});
            } else {
                cursor = AppStatus.getRunningApplication().getContentResolver().query(OneWordDBProvider.getUriByTableName(tableName), new String[]{KEY_ONEWORD_ID}, KEY_ONEWORD_ID + "=?", new String[]{String.valueOf(id)}, null);
            }

            int count = cursor.getCount();
            cursor.close();

            return count != 0;
        }
    }


    public static void removeFromHistory(int id) {
        removeFromSubTable(TABLE_HISTORY, id);
    }

    public static void removeFromFavor(int id) {
        removeFromSubTable(TABLE_FAVOR, id);
    }

    public static void removeFromToShow(int id) {
        removeFromSubTable(TABLE_TOSHOW, id);
    }

    private static void removeFromSubTable(String tableName, int id) {
        if (id <= 0) {
            return;
        }
        synchronized (OneWordDBHelper.class) {


            if (AppStatus.isMianProcess()) {
                SQLiteDatabase sqLiteDatabase = getOpenHelperInstance().getWritableDatabase();
                sqLiteDatabase.execSQL("DELETE FROM " + tableName + " WHERE " + KEY_ONEWORD_ID + "=?", new Object[]{id});
            } else {
                AppStatus.getRunningApplication().getContentResolver().delete(OneWordDBProvider.getUriByTableName(tableName), KEY_ONEWORD_ID + "=?", new String[]{String.valueOf(id)});
            }

        }
    }


    public static void clearHistory() {
        clearTable(TABLE_HISTORY);
    }

    public static void clearFavor() {
        clearTable(TABLE_FAVOR);
    }

    // 在对api进行修改后需要清空
    public static void clearToShow() {
        clearTable(TABLE_TOSHOW);
    }

    private static void clearTable(String tableName) {
        synchronized (OneWordDBHelper.class) {
            if (AppStatus.isMianProcess()) {

                SQLiteDatabase sqLiteDatabase = getOpenHelperInstance().getWritableDatabase();
                sqLiteDatabase.execSQL("DELETE FROM " + tableName + " WHERE 1");
            } else {
                AppStatus.getRunningApplication().getContentResolver().delete(OneWordDBProvider.getUriByTableName(tableName), null, null);
            }
        }
    }

    public static int countHistory() {
        return countTable(TABLE_HISTORY);
    }

    public static int countFavor() {
        return countTable(TABLE_FAVOR);
    }

    public static int countToShow() {
        return countTable(TABLE_TOSHOW);
    }

    private static int countTable(String tableName) {
        synchronized (OneWordDBHelper.class) {

            Cursor cursor;

            if (AppStatus.isMianProcess()) {
                SQLiteDatabase sqLiteDatabase = getOpenHelperInstance().getReadableDatabase();
                cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + tableName, null);
            } else {
                cursor = AppStatus.getRunningApplication().getContentResolver().query(OneWordDBProvider.getUriByTableName(tableName), null, null, null, null);
            }
            int t = cursor.getCount();
            cursor.close();
            return t;
        }
    }

//    public static long remove_one_item(String table_name, HitokotoBean hitokotoBean) {
//        if (hitokotoBean == null) {
//            return -1;
//        }
//        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
//
//        long t = sqLiteDatabase.delete(table_name, KEY_ID + " = ? and " + KEY_TYPE + " = ?", new String[]{hitokotoBean.id + "", hitokotoBean.type});
//        return t;
//    }

    public static WordBean queryOneWordFromHistoryByRandom() {
        return queryOneWordFromSubTableByOrder(TABLE_HISTORY, "random()");
    }


    public static WordBean queryOneWordFromFavorByRandom() {
        return queryOneWordFromSubTableByOrder(TABLE_FAVOR, "random()");
    }


    public static WordBean queryOneWordFromHistoryByDESC() {
        return queryOneWordFromSubTableByOrder(TABLE_HISTORY, KEY_ADDED_AT + " DESC");
    }


    public static WordBean queryOneWordFromToShowByASC() {
        return queryOneWordFromSubTableByOrder(TABLE_TOSHOW, KEY_ADDED_AT + " ASC");
    }

    private static WordBean queryOneWordFromSubTableByOrder(String tableName, String order) {
        synchronized (OneWordDBHelper.class) {
            Cursor cursor;

            if (AppStatus.isMianProcess()) {

                SQLiteDatabase sqLiteDatabase = getOpenHelperInstance().getReadableDatabase();
                cursor = sqLiteDatabase.rawQuery("SELECT " + KEY_ONEWORD_ID + " FROM " + tableName + " ORDER BY " + order + " LIMIT 1", null);
            } else {
                cursor = AppStatus.getRunningApplication().getContentResolver().query(OneWordDBProvider.getUriByTableName(tableName), new String[]{KEY_ONEWORD_ID}, null, null, order);
            }

            WordBean result = null;

            if (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                result = queryOneWordInAllOneWordById(id);
            }
            cursor.close();

            return result;
        }
    }


    public static ArrayList<WordBean> querySomeOneWordFromHistory(int start, int num) {
        return querySomeOneWordFromSubTable(TABLE_HISTORY, start, num);
    }

    public static ArrayList<WordBean> querySomeOneWordFromFavor(int start, int num) {
        return querySomeOneWordFromSubTable(TABLE_FAVOR, start, num);
    }

    public static ArrayList<WordBean> querySomeOneWordFromToShow(int start, int num) {
        return querySomeOneWordFromSubTable(TABLE_TOSHOW, start, num);
    }

    private static ArrayList<WordBean> querySomeOneWordFromSubTable(String tableName, int start,
                                                                    int num) {
        synchronized (OneWordDBHelper.class) {
            Cursor cursor;

            if (AppStatus.isMianProcess()) {
                SQLiteDatabase sqLiteDatabase = getOpenHelperInstance().getReadableDatabase();
                cursor = sqLiteDatabase.rawQuery("SELECT " + KEY_ONEWORD_ID + " FROM " + tableName + " ORDER BY " + KEY_ADDED_AT + " DESC LIMIT ? OFFSET ?", new String[]{String.valueOf(num), String.valueOf(start)});
            } else {
                cursor = AppStatus.getRunningApplication().getContentResolver().query(OneWordDBProvider.getUriByTableName(tableName), new String[]{KEY_ONEWORD_ID}, null, null, String.format(KEY_ADDED_AT + " DESC LIMIT %d OFFSET %d", num, start));
            }

            ArrayList<WordBean> wordBeans = new ArrayList<>(num);

            while (cursor.moveToNext()) {
                wordBeans.add(queryOneWordInAllOneWordById(cursor.getInt(0)));
            }

            cursor.close();

            return wordBeans;
        }
    }


    public static ArrayList<ApiBean> queryAllApi() {
        synchronized (OneWordDBHelper.class) {
            Cursor cursor;

            if (AppStatus.isMianProcess()) {

                SQLiteDatabase sqLiteDatabase = getOpenHelperInstance().getReadableDatabase();
                cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_API + " ORDER BY " + KEY_ID, new String[0]);
            } else {
                cursor = AppStatus.getRunningApplication().getContentResolver().query(OneWordDBProvider.URI_API, null, null, null, KEY_ID);
            }

            ArrayList<ApiBean> apiBeans = new ArrayList<>();

            while (cursor.moveToNext()) {
                apiBeans.add(new ApiBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6) == 1));
            }
            cursor.close();

            return apiBeans;
        }
    }


    public static ApiBean queryApiById(int id) {
        if (id <= 0) {
            return null;
        }
        synchronized (OneWordDBHelper.class) {
            Cursor cursor;

            if (AppStatus.isMianProcess()) {

                SQLiteDatabase sqLiteDatabase = getOpenHelperInstance().getReadableDatabase();
                cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_API + " WHERE " + KEY_ID + " =?", new String[]{String.valueOf(id)});
            } else {
                cursor = AppStatus.getRunningApplication().getContentResolver().query(OneWordDBProvider.URI_API, null, KEY_ID + " =?", new String[]{String.valueOf(id)}, null);
            }

            ApiBean apiBean = null;

            if (cursor.moveToNext()) {
                apiBean = new ApiBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6) == 1);
            }
            cursor.close();

            return apiBean;
        }
    }

    public static ApiBean queryAEnabledApiRandom() {
        synchronized (OneWordDBHelper.class) {
            Cursor cursor;

            if (AppStatus.isMianProcess()) {

                SQLiteDatabase sqLiteDatabase = getOpenHelperInstance().getReadableDatabase();
                cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_API + " ORDER BY random()", new String[0]);
            } else {
                cursor = AppStatus.getRunningApplication().getContentResolver().query(OneWordDBProvider.URI_API, null, null, null, "random()");
            }

            ApiBean apiBean = null;

            while (cursor.moveToNext()) {
                if (cursor.getInt(6) == 1) {
                    apiBean = new ApiBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6) == 1);
                    break;
                }
            }

            cursor.close();
            return apiBean;
        }
    }

    /**
     * 插入一条API，冲突处理方式为Replace
     *
     * @return
     */
    public static void inserAApi(ApiBean apiBean) {
        if (apiBean == null) {
            return;
        }

        synchronized (OneWordDBHelper.class) {

            ContentValues contentValues = new ContentValues();
            if (apiBean.id > 0) {
                contentValues.put(KEY_ID, apiBean.id);
            }
            contentValues.put(KEY_NAME, apiBean.name);
            contentValues.put(KEY_URL, apiBean.url);
            if (!TextUtils.isEmpty(apiBean.name)) {
                contentValues.put(KEY_REQ_METHOD, apiBean.req_method);
            }
            contentValues.put(KEY_REQ_ARGS_JSON, apiBean.req_args_json);
            contentValues.put(KEY_RESP_FORM, apiBean.resp_form);
            contentValues.put(KEY_ENABLED, apiBean.enabled);

            if (AppStatus.isMianProcess()) {

                SQLiteDatabase sqLiteDatabase = getOpenHelperInstance().getWritableDatabase();
                sqLiteDatabase.insertWithOnConflict(TABLE_API, null, contentValues, CONFLICT_REPLACE);
            } else {
                AppStatus.getRunningApplication().getContentResolver().insert(OneWordDBProvider.URI_API, contentValues);
            }


            clearToShow();
        }
    }

    public static void clearAllApi() {
        clearTable(TABLE_API);

        clearToShow();
    }

    public static void removeApiById(int id) {
        if (id <= 0) {
            return;
        }
        synchronized (OneWordDBHelper.class) {

            if (AppStatus.isMianProcess()) {
                SQLiteDatabase sqLiteDatabase = getOpenHelperInstance().getWritableDatabase();
                sqLiteDatabase.execSQL("DELETE FROM " + TABLE_API + " WHERE " + KEY_ID + " =?", new Object[]{id});
            } else {
                AppStatus.getRunningApplication().getContentResolver().delete(OneWordDBProvider.URI_API, KEY_ID + " =?", new String[]{String.valueOf(id)});
            }

            clearToShow();
        }
    }


    public static void insertInternalApi() {
        synchronized (OneWordDBHelper.class) {
            MyOpenHelper.staticInsertInternalApi(getOpenHelperInstance().getWritableDatabase());
        }
    }

    public static String nullToVoid(String str) {
        return str == null ? "" : str;
    }


    public static class MyOpenHelper extends SQLiteOpenHelper {

        private MyOpenHelper(Context context) {//使用versionCode作为数据库版本
            super(context, DB_NAME, null, BuildConfig.VERSION_CODE);
        }

        @Override
        public void onConfigure(SQLiteDatabase db) {
            // 开启外键支持
            db.execSQL("PRAGMA foreign_keys = on");

        }

        //第一次创建数据库时被调用
        @Override
        public void onCreate(SQLiteDatabase db) {


            db.execSQL("BEGIN TRANSACTION");

            db.execSQL("CREATE TABLE all_oneword (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE CHECK (id > 0), content TEXT, reference TEXT, target_url TEXT, target_text TEXT)");
            db.execSQL("CREATE TABLE api (id INTEGER PRIMARY KEY UNIQUE NOT NULL CHECK (id > 0), name TEXT NOT NULL, url TEXT NOT NULL, req_method TEXT NOT NULL DEFAULT 'GET', req_args_json TEXT, resp_form TEXT , enabled BOOLEAN NOT NULL DEFAULT 1)");
            db.execSQL("CREATE TABLE favor (oneword_id INTEGER REFERENCES all_oneword (id) ON DELETE CASCADE UNIQUE NOT NULL, added_at INTEGER NOT NULL COLLATE BINARY)");
            db.execSQL("CREATE TABLE history (oneword_id INTEGER REFERENCES all_oneword (id) ON DELETE CASCADE NOT NULL UNIQUE, added_at INTEGER COLLATE BINARY NOT NULL)");
            db.execSQL("CREATE TABLE toshow (oneword_id INTEGER REFERENCES all_oneword (id) ON DELETE CASCADE UNIQUE NOT NULL, added_at INTEGER NOT NULL COLLATE BINARY)");
            db.execSQL("CREATE INDEX index_alloneword_content_reference_sourceurl ON all_oneword (content, reference)");
            db.execSQL("CREATE INDEX index_history_addedat ON history (added_at DESC)");
            db.execSQL("CREATE INDEX index_history_onewordid ON history (oneword_id)");
            db.execSQL("CREATE INDEX index_like_addedat ON favor (added_at DESC)");
            db.execSQL("CREATE INDEX index_like_oneowrdid ON favor (oneword_id)");
            db.execSQL("CREATE INDEX index_toshow_addedat ON toshow (added_at DESC)");
            db.execSQL("CREATE INDEX index_toshow_onewordid ON toshow (oneword_id)");

//        db.execSQL("CREATE TRIGGER clear_toshow_when_api_deleted AFTER DELETE ON api FOR EACH ROW BEGIN DELETE FROM toshow; END");
//        db.execSQL("CREATE TRIGGER clear_toshow_when_api_enable_updated AFTER UPDATE OF enabled ON api FOR EACH ROW BEGIN DELETE FROM toshow; END");

            db.execSQL("COMMIT TRANSACTION");

            staticInsertInternalApi(db);
        }


        //数据库版本发生更新后调用
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            switch (oldVersion) {
                case 1:// 第一版

                    db.execSQL("ALTER TABLE history RENAME TO old_history");
                    db.execSQL("ALTER TABLE [like] RENAME TO old_like");
                    db.execSQL("ALTER TABLE ready_to_show RENAME TO old_ready_to_show");

                    this.onCreate(db);


                    // 处理旧的history表的数据
                    Cursor cursor = db.rawQuery("SELECT * FROM old_history;", new String[0]);

                    ContentValues contentValues_all = new ContentValues();

                    ContentValues contentValues_history = new ContentValues();
                    ContentValues contentValues_favor = new ContentValues();

                    while (cursor.moveToNext()) {
                        contentValues_all.put(KEY_CONTENT, nullToVoid(cursor.getString(1)));
                        contentValues_all.put(KEY_REFERENCE, nullToVoid(cursor.getString(3)));

                        // 避免重复
                        Cursor cursor_0 = db.rawQuery("SELECT " + KEY_ID + " FROM " + TABLE_ALL_ONEWORD + " WHERE " + KEY_CONTENT + "=? AND " + KEY_REFERENCE + "=?", new String[]{
                                nullToVoid(cursor.getString(1)), nullToVoid(cursor.getString(3))
                        });

                        if (cursor_0.getCount() == 0) {
                            long id = db.insertWithOnConflict(TABLE_ALL_ONEWORD, null, contentValues_all, CONFLICT_IGNORE);

                            contentValues_history.put(KEY_ONEWORD_ID, id);
                            contentValues_history.put(KEY_ADDED_AT, cursor.getLong(6));

                            db.insertWithOnConflict(TABLE_HISTORY, null, contentValues_history, CONFLICT_REPLACE);

                            if (cursor.getInt(7) == 1) {// 如果点过喜欢

                                Cursor cursor_1 = db.rawQuery("SELECT * FROM old_like WHERE id=? AND type=?", new String[]{
                                        String.valueOf(cursor.getInt(0)), cursor.getString(2)
                                });

                                if (cursor_1.moveToNext()) {// 在旧的like表中存在

                                    contentValues_favor.put(KEY_ONEWORD_ID, id);
                                    contentValues_favor.put(KEY_ADDED_AT, cursor_1.getLong(6));

                                    db.insertWithOnConflict(TABLE_FAVOR, null, contentValues_favor, CONFLICT_REPLACE);

                                }

                                cursor_1.close();
                            }
                        }
                        cursor_0.close();
                    }

                    cursor.close();


                    db.execSQL("DROP TABLE old_history");
                    db.execSQL("DROP TABLE old_like");
                    db.execSQL("DROP TABLE old_ready_to_show");


                    break;
                case 2:
                case 3:
                case 4:
                    db.execSQL("ALTER TABLE all_oneword ADD COLUMN target_text TEXT");
                    staticInsertInternalApi_v5(db);
            }

        }


        private static void staticInsertInternalApi(SQLiteDatabase db) {
            staticInsertInternalApi_v2(db);
            staticInsertInternalApi_v5(db);

        }

        private static void staticInsertInternalApi_v2(SQLiteDatabase db) {

            db.execSQL("INSERT INTO api(name,url,req_method,req_args_json,resp_form,enabled) VALUES('Hitokoto-动漫', 'https://v1.hitokoto.cn', 'GET', '{\"c\":\"a\"}', '{\n  \"hitokoto\": \"[content]\",\n  \"from\": \"[reference]\"\n}', 1)");
            db.execSQL("INSERT INTO api(name,url,req_method,req_args_json,resp_form,enabled) VALUES('Hitokoto-漫画', 'https://v1.hitokoto.cn', 'GET', '{\"c\":\"b\"}', '{\n  \"hitokoto\": \"[content]\",\n  \"from\": \"[reference]\"\n}', 1)");
            db.execSQL("INSERT INTO api(name,url,req_method,req_args_json,resp_form,enabled) VALUES('Hitokoto-游戏', 'https://v1.hitokoto.cn', 'GET', '{\"c\":\"c\"}', '{\n  \"hitokoto\": \"[content]\",\n  \"from\": \"[reference]\"\n}', 1)");
            db.execSQL("INSERT INTO api(name,url,req_method,req_args_json,resp_form,enabled) VALUES('Hitokoto-小说', 'https://v1.hitokoto.cn', 'GET', '{\"c\":\"d\"}', '{\n  \"hitokoto\": \"[content]\",\n  \"from\": \"[reference]\"\n}', 1)");
            db.execSQL("INSERT INTO api(name,url,req_method,req_args_json,resp_form,enabled) VALUES('Hitokoto-原创', 'https://v1.hitokoto.cn', 'GET', '{\"c\":\"e\"}', '{\n  \"hitokoto\": \"[content]\",\n  \"from\": \"[reference]\"\n}', 1)");
            db.execSQL("INSERT INTO api(name,url,req_method,req_args_json,resp_form,enabled) VALUES('Hitokoto-来自网络', 'https://v1.hitokoto.cn', 'GET', '{\"c\":\"f\"}', '{\n  \"hitokoto\": \"[content]\",\n  \"from\": \"[reference]\"\n}', 1)");
            db.execSQL("INSERT INTO api(name,url,req_method,req_args_json,resp_form,enabled) VALUES('Hitokoto-其他', 'https://v1.hitokoto.cn', 'GET', '{\"c\":\"g\"}', '{\n  \"hitokoto\": \"[content]\",\n  \"from\": \"[reference]\"\n}', 1)");
            db.execSQL("INSERT INTO api(name,url,req_method,req_args_json,resp_form,enabled) VALUES('yiju-一句', 'http://yiju.ml/api/word.php', 'GET', '', '——', 1)");

        }

        private static void staticInsertInternalApi_v5(SQLiteDatabase db) {

            db.execSQL("INSERT INTO api(name,url,req_method,req_args_json,resp_form,enabled) VALUES('网易云音乐歌评-华语', 'http://w4y.imlk.top/thin_api', 'GET', '{\"cat\":\"a\"}', '{\n    \"content\": \"[content]\",\n    \"reference\": \"[reference]\",\n    \"target_url\": \"[target_url]\",\n    \"target_text\": \"[target_text]\"\n}', 1)");
            db.execSQL("INSERT INTO api(name,url,req_method,req_args_json,resp_form,enabled) VALUES('网易云音乐歌评-流行', 'http://w4y.imlk.top/thin_api', 'GET', '{\"cat\":\"b\"}', '{\n    \"content\": \"[content]\",\n    \"reference\": \"[reference]\",\n    \"target_url\": \"[target_url]\",\n    \"target_text\": \"[target_text]\"\n}', 1)");
            db.execSQL("INSERT INTO api(name,url,req_method,req_args_json,resp_form,enabled) VALUES('网易云音乐歌评-摇滚', 'http://w4y.imlk.top/thin_api', 'GET', '{\"cat\":\"c\"}', '{\n    \"content\": \"[content]\",\n    \"reference\": \"[reference]\",\n    \"target_url\": \"[target_url]\",\n    \"target_text\": \"[target_text]\"\n}', 1)");
            db.execSQL("INSERT INTO api(name,url,req_method,req_args_json,resp_form,enabled) VALUES('网易云音乐歌评-民谣', 'http://w4y.imlk.top/thin_api', 'GET', '{\"cat\":\"d\"}', '{\n    \"content\": \"[content]\",\n    \"reference\": \"[reference]\",\n    \"target_url\": \"[target_url]\",\n    \"target_text\": \"[target_text]\"\n}', 1)");
            db.execSQL("INSERT INTO api(name,url,req_method,req_args_json,resp_form,enabled) VALUES('网易云音乐歌评-电子', 'http://w4y.imlk.top/thin_api', 'GET', '{\"cat\":\"e\"}', '{\n    \"content\": \"[content]\",\n    \"reference\": \"[reference]\",\n    \"target_url\": \"[target_url]\",\n    \"target_text\": \"[target_text]\"\n}', 1)");
            db.execSQL("INSERT INTO api(name,url,req_method,req_args_json,resp_form,enabled) VALUES('网易云音乐歌评-轻音乐', 'http://w4y.imlk.top/thin_api', 'GET', '{\"cat\":\"f\"}', '{\n    \"content\": \"[content]\",\n    \"reference\": \"[reference]\",\n    \"target_url\": \"[target_url]\",\n    \"target_text\": \"[target_text]\"\n}', 1)");
            db.execSQL("INSERT INTO api(name,url,req_method,req_args_json,resp_form,enabled) VALUES('网易云音乐歌评-影视原声', 'http://w4y.imlk.top/thin_api', 'GET', '{\"cat\":\"g\"}', '{\n    \"content\": \"[content]\",\n    \"reference\": \"[reference]\",\n    \"target_url\": \"[target_url]\",\n    \"target_text\": \"[target_text]\"\n}', 1)");
            db.execSQL("INSERT INTO api(name,url,req_method,req_args_json,resp_form,enabled) VALUES('网易云音乐歌评-ACG', 'http://w4y.imlk.top/thin_api', 'GET', '{\"cat\":\"h\"}', '{\n    \"content\": \"[content]\",\n    \"reference\": \"[reference]\",\n    \"target_url\": \"[target_url]\",\n    \"target_text\": \"[target_text]\"\n}', 1)");
            db.execSQL("INSERT INTO api(name,url,req_method,req_args_json,resp_form,enabled) VALUES('网易云音乐歌评-夜晚', 'http://w4y.imlk.top/thin_api', 'GET', '{\"cat\":\"i\"}', '{\n    \"content\": \"[content]\",\n    \"reference\": \"[reference]\",\n    \"target_url\": \"[target_url]\",\n    \"target_text\": \"[target_text]\"\n}', 1)");
            db.execSQL("INSERT INTO api(name,url,req_method,req_args_json,resp_form,enabled) VALUES('网易云音乐歌评-学习', 'http://w4y.imlk.top/thin_api', 'GET', '{\"cat\":\"j\"}', '{\n    \"content\": \"[content]\",\n    \"reference\": \"[reference]\",\n    \"target_url\": \"[target_url]\",\n    \"target_text\": \"[target_text]\"\n}', 1)");
            db.execSQL("INSERT INTO api(name,url,req_method,req_args_json,resp_form,enabled) VALUES('网易云音乐歌评-运动', 'http://w4y.imlk.top/thin_api', 'GET', '{\"cat\":\"k\"}', '{\n    \"content\": \"[content]\",\n    \"reference\": \"[reference]\",\n    \"target_url\": \"[target_url]\",\n    \"target_text\": \"[target_text]\"\n}', 1)");
            db.execSQL("INSERT INTO api(name,url,req_method,req_args_json,resp_form,enabled) VALUES('网易云音乐歌评-怀旧', 'http://w4y.imlk.top/thin_api', 'GET', '{\"cat\":\"l\"}', '{\n    \"content\": \"[content]\",\n    \"reference\": \"[reference]\",\n    \"target_url\": \"[target_url]\",\n    \"target_text\": \"[target_text]\"\n}', 1)");
            db.execSQL("INSERT INTO api(name,url,req_method,req_args_json,resp_form,enabled) VALUES('网易云音乐歌评-清新', 'http://w4y.imlk.top/thin_api', 'GET', '{\"cat\":\"m\"}', '{\n    \"content\": \"[content]\",\n    \"reference\": \"[reference]\",\n    \"target_url\": \"[target_url]\",\n    \"target_text\": \"[target_text]\"\n}', 1)");
            db.execSQL("INSERT INTO api(name,url,req_method,req_args_json,resp_form,enabled) VALUES('网易云音乐歌评-治愈', 'http://w4y.imlk.top/thin_api', 'GET', '{\"cat\":\"n\"}', '{\n    \"content\": \"[content]\",\n    \"reference\": \"[reference]\",\n    \"target_url\": \"[target_url]\",\n    \"target_text\": \"[target_text]\"\n}', 1)");

        }

    }

}
