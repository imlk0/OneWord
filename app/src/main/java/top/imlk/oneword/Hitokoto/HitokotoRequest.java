package top.imlk.oneword.Hitokoto;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by imlk on 2018/5/19.
 */
public interface HitokotoRequest {

    @GET("/")
    Observable<HitokotoBean> getAnRequest(@Query("c") String type);

}
