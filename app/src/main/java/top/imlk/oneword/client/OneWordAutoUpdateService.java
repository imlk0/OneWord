package top.imlk.oneword.client;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import top.imlk.oneword.common.StaticValue;
import top.imlk.oneword.util.SharedPreferencesUtil;

/**
 * Created by imlk on 2018/5/26.
 */
public class OneWordAutoUpdateService extends Service implements Observer {

    private static final String LOG_TAG = "OneWordAutoUpdateServ";
    public static final String SERVICE_NAME = OneWordAutoUpdateService.class.getName();

    public enum Mode {

        FIVE_MINUTE,
        TEN_MINUTE,
        TWENTY_MINUTE,
        THIRTY_MINUTE,
        ONE_HOUR,
        TWO_HOUR,

        EVERY_LOCK,
        TWICE_LOCK,
        FIFTH_LOCK,
        TEN_TIMES_LOCK,

    }

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

        switch (intent.getAction()) {
            //TODO
            case StaticValue.CMD_SERVICES_START_AUTO_UPDATE:
                Log.i(LOG_TAG, "Hello~");


                if (SharedPreferencesUtil.isUpdatingOpened(this)) {

                    Log.e(LOG_TAG, intent.getAction());
                    switch (SharedPreferencesUtil.getUpdatingMode(this)) {
                        case EVERY_LOCK:
                            break;
                        case TWICE_LOCK:
                            break;
                        case FIFTH_LOCK:
                            break;
                        case TEN_TIMES_LOCK:
                            break;
                        case ONE_HOUR:
                            break;
                        case TWO_HOUR:
                            break;
                        case FIVE_MINUTE:
                            break;
                        case TEN_MINUTE:
                            break;
                        case THIRTY_MINUTE:
                            break;
                        case TWENTY_MINUTE:
                            break;
                        default:
                            break;
                    }

                } else {
//                    this.stopSelf();
                }

                break;
            case StaticValue.CMD_SERVICES_STOP_SERVICE:
                this.stopSelf();
                Log.i(LOG_TAG, "Bye~");
                break;
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
