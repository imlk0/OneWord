package top.imlk.oneword.client;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import de.robv.android.xposed.XposedBridge;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by imlk on 2018/5/26.
 */
public class OneWordAutoUpdateService extends Service implements Observer {

    private static final String LOG_TAG = "OneWordAutoUpdateServ";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {//非绑定模式
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(LOG_TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        switch (intent.getAction()){
            //TODO
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Object o) {

    }

    @Override
    public void onError(Throwable e) {
        Log.e(LOG_TAG, "error when get new oneword", e);
    }

    @Override
    public void onComplete() {

    }
}
