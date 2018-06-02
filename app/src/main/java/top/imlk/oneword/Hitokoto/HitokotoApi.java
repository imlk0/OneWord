package top.imlk.oneword.Hitokoto;


import io.reactivex.Observable;
import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import top.imlk.oneword.net.NetHelper;

/**
 * Created by imlk on 2018/5/19.
 */
public class HitokotoApi {

    public static final String BASEURL = "https://v1.hitokoto.cn/";


    static class Parameter {
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
        public static final String[] c_Array = {"a", "b", "c", "d", "e", "f", "g"};
    }

    public static void getAnime(final Observer callback) {
        Observable<HitokotoBean> observable = NetHelper.getInstance().getRetrofitInstance().create(HitokotoRequest.class).getAnRequest(Parameter.c_Array[0]);

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);

    }





    public static void main(String[] args) {


    }

}
