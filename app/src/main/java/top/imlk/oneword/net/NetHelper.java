package top.imlk.oneword.net;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import top.imlk.oneword.net.Hitokoto.HitokotoApi;

/**
 * Created by imlk on 2018/5/19.
 */
public class NetHelper {

    private static NetHelper mNetHelper;


    private Retrofit mRetrofit;

    private NetHelper() {
    }

    public static NetHelper getInstance() {
        if (mNetHelper == null) {
            mNetHelper = new NetHelper();
        }

        return mNetHelper;
    }

    public void initRetrofit() {
        this.mRetrofit = new Retrofit.Builder()
                .baseUrl(HitokotoApi.BASEURL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofitInstance() {
        if (this.mRetrofit == null) {
            initRetrofit();
        }

        return this.mRetrofit;
    }

}
