package top.imlk.oneword.net;

import android.text.TextUtils;

import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import java.util.Iterator;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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

    public static void requestOneWord(final WordRequestObserver callback) {

        ApiBean apiBean = OneWordSQLiteOpenHelper.getInstance().queryAEnabledApiRandom();

        requestOneWordByAPI(callback, apiBean);

    }

    public static void requestOneWordByAPI(final WordRequestObserver callback, final ApiBean apiBean) {

        requestOneWordInternal(apiBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<WordBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callback.onStart(apiBean);
            }

            @Override
            public void onNext(WordBean wordBean) {
                callback.onAcquire(apiBean, wordBean);
            }

            @Override
            public void onError(Throwable e) {
                callback.onError(apiBean, e);
            }

            @Override
            public void onComplete() {

            }
        });
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

                        if (!TextUtils.isEmpty(apiBean.req_args_json)) {
                            try {

                                JSONObject jsonObject = new JSONObject(apiBean.req_args_json);

                                for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                                    String key = it.next();

                                    httpUrlBuilder.addQueryParameter(key, String.valueOf(jsonObject.get(key)));

                                }

                            } catch (Throwable e) {
                                throw new RuntimeException("参数解析出错", e);
                            }

                        }

                        req = new Request.Builder()
                                .url(httpUrlBuilder.build()).get().build();

                    } else if ("POST".equalsIgnoreCase(apiBean.req_method)) {

                        FormBody.Builder formBodyBuilder = new FormBody.Builder();

                        if (!TextUtils.isEmpty(apiBean.req_args_json)) {

                            try {
                                JSONObject jsonObject = new JSONObject(apiBean.req_args_json);

                                for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                                    String key = it.next();

                                    formBodyBuilder.add(key, String.valueOf(jsonObject.get(key)));
                                }

                            } catch (Throwable e) {
                                throw new RuntimeException("参数解析出错", e);
                            }
                        }
                        req = new Request.Builder().url(apiBean.url).post(formBodyBuilder.build()).build();

                    } else {
                        throw new UnsupportedOperationException(String.format("不支持的请求方式：%s", apiBean.req_method));

                    }

                    getClientInstance().newCall(req).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                            emitter.onError(new RuntimeException("网络连接失败", e));

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            String responseBody = null;

                            try {

                                if (response.code() == 200) {

                                    responseBody = response.body().string();

                                    WordBean wordBean = new WordBean();

                                    if (!TextUtils.isEmpty(apiBean.resp_form)) {// 结果格式不为空

                                        try {// 尝试用json格式解析
                                            JSONTokener jsonForm = new JSONTokener(apiBean.resp_form);

                                            JSONTokener jsonReal = new JSONTokener(responseBody);

                                            OneWordInJsonExtracter.direction(jsonForm.nextValue(), jsonReal.nextValue(), wordBean);
                                        } catch (Throwable e0) {// 解析出错，改用分隔符解析

                                        }
                                    }


                                    if (TextUtils.isEmpty(wordBean.content) && !TextUtils.isEmpty(apiBean.resp_form)) {
                                        String[] substr = responseBody.split(apiBean.resp_form);

                                        StringBuilder builder = new StringBuilder();
                                        for (int i = 0; i < substr.length - 1; i++) {
                                            builder.append(substr[i]);
                                        }
                                        wordBean.content = builder.toString();
                                        wordBean.reference = substr[substr.length - 1];
                                    }

                                    if (TextUtils.isEmpty(wordBean.content)) {
                                        wordBean = new WordBean(responseBody, null);
                                    }

                                    emitter.onNext(wordBean);

                                    emitter.onComplete();

                                } else {

                                    emitter.onError(new RuntimeException("不正确的HTTP状态码：" + response.code()));

                                }
                            } catch (Throwable throwable) {

                                RuntimeException re = new RuntimeException("responseBody=\n" + responseBody + "\napiBean=\n" + apiBean, throwable);

                                BugUtil.printAndSaveCrashThrow2File(re);
                                emitter.onError(re);
                            }

                        }
                    });

                } catch (Throwable throwable) {
                    CrashReport.postCatchedException(throwable);
                    emitter.onError(new RuntimeException("未知异常", throwable));
                }


            }
        });


    }
}
