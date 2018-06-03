package top.imlk.oneword.Hitokoto;


import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import top.imlk.oneword.net.NetHelper;
import top.imlk.oneword.util.SharedPreferencesUtil;

/**
 * Created by imlk on 2018/5/19.
 */
public class HitokotoApi {

    public static final String BASEURL = "https://v1.hitokoto.cn/";


    public static class Parameter {
        /**
         * Cat，即类型。提交不同的参数代表不同的类别，具体：
         * a 	Anime - 动画
         * b 	Comic – 漫画
         * c 	Game – 游戏
         * d 	Novel – 小说
         * e 	Myself – 原创
         * f 	Internet – 来自网络
         * g 	Other – 其他
         * 其他不存在参数 	任意类型随机取得
         */
        public static final String[] c_Array_ALL = {"a", "b", "c", "d", "e", "f", "g"};

        public static String[] c_Array_custom = null;

    }


    public static void requestOneWord(final Observer callback) {

        Observable<HitokotoBean> observable = NetHelper.getInstance()
                .getRetrofitInstance()
                .create(HitokotoRequest.class)
                .getAnRequest(Parameter.c_Array_custom == null || Parameter.c_Array_custom.length == 0 ?
                        Parameter.c_Array_ALL[((int) (Math.random() * Parameter.c_Array_ALL.length))] :
                        Parameter.c_Array_custom[((int) (Math.random() * Parameter.c_Array_custom.length))]);

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);

    }


    public static void refreshCustomArray(boolean[] selectState) {

        int count = 0;
        for (int i = 0; i < selectState.length; ++i) {
            if (selectState[i]) {
                count++;
            }
        }
        Parameter.c_Array_custom = new String[count];

        count = 0;
        for (int i = 0; i < selectState.length; ++i) {
            if (selectState[i]) {
                Parameter.c_Array_custom[count] = Parameter.c_Array_ALL[i];
                count++;
            }
        }
    }


    public static void main(String[] args) {


    }

}
