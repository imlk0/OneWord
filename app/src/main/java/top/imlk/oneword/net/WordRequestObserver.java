package top.imlk.oneword.net;

import io.reactivex.annotations.NonNull;
import top.imlk.oneword.bean.ApiBean;
import top.imlk.oneword.bean.WordBean;

/**
 * Created by imlk on 2018/8/12.
 */
public interface WordRequestObserver {

    void onStart(@NonNull ApiBean apiBean);

    void onAcquire(@NonNull ApiBean apiBean, @NonNull WordBean wordBean);

    void onError(@NonNull ApiBean apiBean, @NonNull Throwable e);

}
