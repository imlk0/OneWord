package top.imlk.oneword.net;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import java.util.Iterator;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import top.imlk.oneword.bean.ApiBean;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.dao.OneWordSQLiteOpenHelper;
import top.imlk.oneword.util.BugUtil;

/**
 * Created by imlk on 2018/8/3.
 */
public class OneWordApi {

    private static OkHttpClient client;

    private static OkHttpClient getClientInstance() {
        if (client == null) {
            synchronized (OkHttpClient.class) {
                if (client == null) {
                    client = new OkHttpClient();
                }
            }
        }

        return client;
    }

    public static void requestOneWord(final Observer<WordBean> callback) {

        ApiBean apiBean = OneWordSQLiteOpenHelper.getInstance().queryAEnabledApiRandom();

        requestOneWordByAPI(callback, apiBean);

    }

    public static void requestOneWordByAPI(final Observer<WordBean> callback, ApiBean apiBean) {

        requestOneWordInternal(apiBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(callback);
    }

    private static Observable<WordBean> requestOneWordInternal(final ApiBean apiBean) {

        return Observable.create(new ObservableOnSubscribe<WordBean>() {
            @Override
            public void subscribe(final ObservableEmitter<WordBean> emitter) throws Exception {


                try {

                    if (apiBean == null) {
                        throw new RuntimeException("没有有效的api配置或未启用任何api");
                    }

                    Request req;

                    if ("GET".equalsIgnoreCase(apiBean.req_method)) {
                        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(apiBean.url).newBuilder();

                        JSONObject jsonObject = new JSONObject(apiBean.req_args_json);

                        for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                            String key = it.next();

                            httpUrlBuilder.addQueryParameter(key, String.valueOf(jsonObject.get(key)));

                        }

                        req = new Request.Builder()
                                .url(httpUrlBuilder.build()).get().build();

                    } else if ("POST".equalsIgnoreCase(apiBean.req_method)) {

                        FormBody.Builder formBodyBuilder = new FormBody.Builder();

                        JSONObject jsonObject = new JSONObject(apiBean.req_args_json);

                        for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                            String key = it.next();

                            formBodyBuilder.add(key, String.valueOf(jsonObject.get(key)));
                        }

                        req = new Request.Builder().url(apiBean.url).post(formBodyBuilder.build()).build();

                    } else {
                        throw new UnsupportedOperationException(String.format("不支持的请求方式：%s", apiBean.req_method));

                    }

                    getClientInstance().newCall(req).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                            emitter.onError(e);

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            String responseBody = null;

                            try {

                                responseBody = response.body().string();


                                WordBean wordBean = new WordBean();

                                JSONTokener jsonForm = new JSONTokener(apiBean.resp_form_json);

                                JSONTokener jsonReal = new JSONTokener(responseBody);

                                OneWordInJsonExtracter.direction(jsonForm.nextValue(), jsonReal.nextValue(), wordBean);

                                emitter.onNext(wordBean);

                                emitter.onComplete();
                            } catch (Throwable throwable) {

                                throwable.addSuppressed(new Throwable("responseBody=\n" + responseBody + "\napiBean=\n" + apiBean));

                                BugUtil.printAndSaveCrashThrow2File(throwable);
                                emitter.onError(throwable);
                            }

                        }
                    });

                } catch (Throwable throwable) {
                    emitter.onError(throwable);
                }


            }
        });


    }
}
