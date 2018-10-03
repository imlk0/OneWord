package top.imlk.oneword.app.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import top.imlk.oneword.dao.OneWordDBHelper;

public class OneWordDBProvider extends ContentProvider {

    private static final String TAG = "OneWordDBProvider";
    private static final String AUTOHORITY = "top.imlk.oneword.OneWordDBProvider";
    private static final UriMatcher mMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    public static final Uri URI_ALL_ONEWORD = Uri.parse("content://top.imlk.oneword.OneWordDBProvider/" + OneWordDBHelper.TABLE_ALL_ONEWORD);
    public static final Uri URI_FAVOR = Uri.parse("content://top.imlk.oneword.OneWordDBProvider/" + OneWordDBHelper.TABLE_FAVOR);
    public static final Uri URI_HISTORY = Uri.parse("content://top.imlk.oneword.OneWordDBProvider/" + OneWordDBHelper.TABLE_HISTORY);
    public static final Uri URI_TOSHOW = Uri.parse("content://top.imlk.oneword.OneWordDBProvider/" + OneWordDBHelper.TABLE_TOSHOW);
    public static final Uri URI_API = Uri.parse("content://top.imlk.oneword.OneWordDBProvider/" + OneWordDBHelper.TABLE_API);

    public static final Uri getUriByTableName(String tableName) {
        switch (tableName) {
            case OneWordDBHelper.TABLE_ALL_ONEWORD:
                return URI_ALL_ONEWORD;
            case OneWordDBHelper.TABLE_FAVOR:
                return URI_FAVOR;
            case OneWordDBHelper.TABLE_HISTORY:
                return URI_HISTORY;
            case OneWordDBHelper.TABLE_TOSHOW:
                return URI_TOSHOW;
            case OneWordDBHelper.TABLE_API:
                return URI_API;
        }

        throw new IllegalArgumentException("table name: " + tableName + " is illeagal!!!");
    }


    public static final int CODE_ALL_ONEWORD = 1;
    public static final int CODE_FAVOR = 2;
    public static final int CODE_HISTORY = 3;
    public static final int CODE_TOSHOW = 4;
    public static final int CODE_API = 5;


    static {
        mMatcher.addURI(AUTOHORITY, OneWordDBHelper.TABLE_ALL_ONEWORD, CODE_ALL_ONEWORD);
        mMatcher.addURI(AUTOHORITY, OneWordDBHelper.TABLE_FAVOR, CODE_FAVOR);
        mMatcher.addURI(AUTOHORITY, OneWordDBHelper.TABLE_HISTORY, CODE_HISTORY);
        mMatcher.addURI(AUTOHORITY, OneWordDBHelper.TABLE_TOSHOW, CODE_TOSHOW);
        mMatcher.addURI(AUTOHORITY, OneWordDBHelper.TABLE_API, CODE_API);
    }

    public OneWordDBProvider() {
    }


    @Override
    public boolean onCreate() {
        try {
            OneWordDBHelper.getOpenHelperInstance();
            return true;
        } catch (Throwable t) {
            Log.e(TAG, "启动OneWordDBProvider失败", t);
            return false;
        }
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int code = mMatcher.match(uri);
        SQLiteDatabase db = OneWordDBHelper.getOpenHelperInstance().getWritableDatabase();

        switch (code) {
            case CODE_ALL_ONEWORD:
                return db.delete(OneWordDBHelper.TABLE_ALL_ONEWORD, selection, selectionArgs);
            case CODE_FAVOR:
                return db.delete(OneWordDBHelper.TABLE_FAVOR, selection, selectionArgs);
            case CODE_HISTORY:
                return db.delete(OneWordDBHelper.TABLE_HISTORY, selection, selectionArgs);
            case CODE_TOSHOW:
                return db.delete(OneWordDBHelper.TABLE_TOSHOW, selection, selectionArgs);
            case CODE_API:
                return db.delete(OneWordDBHelper.TABLE_API, selection, selectionArgs);
            default:
                return -1;
        }

    }

    @Override
    public String getType(Uri uri) {
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int code = mMatcher.match(uri);
        SQLiteDatabase db = OneWordDBHelper.getOpenHelperInstance().getWritableDatabase();

        switch (code) {
            case CODE_ALL_ONEWORD:
                return ContentUris.withAppendedId(uri, db.insertWithOnConflict(OneWordDBHelper.TABLE_ALL_ONEWORD, null, values, SQLiteDatabase.CONFLICT_IGNORE));
            case CODE_FAVOR:
                return ContentUris.withAppendedId(uri, db.insertWithOnConflict(OneWordDBHelper.TABLE_FAVOR, null, values, SQLiteDatabase.CONFLICT_IGNORE));
            case CODE_HISTORY:
                return ContentUris.withAppendedId(uri, db.insertWithOnConflict(OneWordDBHelper.TABLE_HISTORY, null, values, SQLiteDatabase.CONFLICT_IGNORE));
            case CODE_TOSHOW:
                return ContentUris.withAppendedId(uri, db.insertWithOnConflict(OneWordDBHelper.TABLE_TOSHOW, null, values, SQLiteDatabase.CONFLICT_IGNORE));
            case CODE_API:
                return ContentUris.withAppendedId(uri, db.insertWithOnConflict(OneWordDBHelper.TABLE_API, null, values, SQLiteDatabase.CONFLICT_IGNORE));
            default:
                return null;
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        int code = mMatcher.match(uri);
        SQLiteDatabase db = OneWordDBHelper.getOpenHelperInstance().getReadableDatabase();

        switch (code) {
            case CODE_ALL_ONEWORD:
                return db.query(OneWordDBHelper.TABLE_ALL_ONEWORD, projection, selection, selectionArgs, null, null, sortOrder);
            case CODE_FAVOR:
                return db.query(OneWordDBHelper.TABLE_FAVOR, projection, selection, selectionArgs, null, null, sortOrder);
            case CODE_HISTORY:
                return db.query(OneWordDBHelper.TABLE_HISTORY, projection, selection, selectionArgs, null, null, sortOrder);
            case CODE_TOSHOW:
                return db.query(OneWordDBHelper.TABLE_TOSHOW, projection, selection, selectionArgs, null, null, sortOrder);
            case CODE_API:
                return db.query(OneWordDBHelper.TABLE_API, projection, selection, selectionArgs, null, null, sortOrder);
            default:
                return null;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int code = mMatcher.match(uri);
        SQLiteDatabase db = OneWordDBHelper.getOpenHelperInstance().getWritableDatabase();

        switch (code) {
            case CODE_ALL_ONEWORD:
                return db.update(OneWordDBHelper.TABLE_ALL_ONEWORD, values, selection, selectionArgs);
            case CODE_FAVOR:
                return db.update(OneWordDBHelper.TABLE_FAVOR, values, selection, selectionArgs);
            case CODE_HISTORY:
                return db.update(OneWordDBHelper.TABLE_HISTORY, values, selection, selectionArgs);
            case CODE_TOSHOW:
                return db.update(OneWordDBHelper.TABLE_TOSHOW, values, selection, selectionArgs);
            case CODE_API:
                return db.update(OneWordDBHelper.TABLE_API, values, selection, selectionArgs);
            default:
                return -1;
        }
    }
}
