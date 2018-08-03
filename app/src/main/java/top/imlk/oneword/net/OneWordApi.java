package top.imlk.oneword.net;

import android.content.Context;

import io.reactivex.Observer;
import top.imlk.oneword.bean.ApiBean;
import top.imlk.oneword.dao.OneWordSQLiteOpenHelper;

/**
 * Created by imlk on 2018/8/3.
 */
public class OneWordApi {
    public static void requestOneWord(Context context, final Observer callback) {
        ApiBean apiBean = OneWordSQLiteOpenHelper.getInstance(context)
    }
}
