package top.imlk.oneword.application.client.service;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import top.imlk.oneword.R;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.dao.OneWordSQLiteOpenHelper;
import top.imlk.oneword.net.OneWordApi;
import top.imlk.oneword.systemui.uifixer.BaseUIFixer;
import top.imlk.oneword.util.BroadcastSender;
import top.imlk.oneword.util.BugUtil;
import top.imlk.oneword.util.OneWordFileStation;
import top.imlk.oneword.util.SharedPreferencesUtil;


/**
 * Created by imlk on 2018/5/26.
 */
public class OneWordAutoRefreshService extends Service implements Observer<WordBean> {

    private static final String LOG_TAG = "OneWordAutoRefreshServ";


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

    private Mode currentMode;

    private long spaceTime = 0;
    private int lockTime = 0;
    private int lockTimeCount = 0;

    private UserPresentBroadCastReceiver mUserPresentBroadCastReceiver;
    private Timer mTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {//非绑定模式
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(LOG_TAG, "onCreate");


        if (!SharedPreferencesUtil.isRefreshOpened(this)) {
            Log.e(LOG_TAG, "自动更新开关未启动，搞毛线啊");
            Log.i(LOG_TAG, "Bye~");
            this.stopSelf();
        }

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, "Hello~");

        Log.e(LOG_TAG, intent == null ? "intent is null" : intent.getAction() + "");


        toForeground();

        if (intent == null || TextUtils.isEmpty(intent.getAction()) || BroadcastSender.CMD_SERVICES_START_AUTO_REFRESH_SERVICE.equals(intent.getAction())) {

            Log.e("isRefreshOpened()", SharedPreferencesUtil.isRefreshOpened(this) + "");

            if (SharedPreferencesUtil.isRefreshOpened(this)) {

                currentMode = SharedPreferencesUtil.getRefreshMode(this);

                parseMode();


            } else {
                Log.e(LOG_TAG, "自动更新开关未启动，搞毛线啊");
                Log.i(LOG_TAG, "Bye~");
                this.stopSelf();
            }
        } else if (BroadcastSender.CMD_SERVICES_STOP_SERVICE.equals(intent.getAction())) {


            Log.i(LOG_TAG, "Bye~");
            this.stopSelf();
        } else {
            Log.i(LOG_TAG, "NO match command!!!");

        }


        return START_REDELIVER_INTENT;

    }


    private void parseMode() {
        if (currentMode == null) {
            Log.e(LOG_TAG, "wtf! currentMode is null, error when parse");
        } else {
            Log.e(LOG_TAG, "currentMode -> " + Mode.values()[currentMode.ordinal()]);

            boolean updateByLockTimes = true;
            switch (currentMode) {
                case EVERY_LOCK:
                    lockTime = 1;
                    break;
                case TWICE_LOCK:
                    lockTime = 2;
                    break;
                case FIFTH_LOCK:
                    lockTime = 5;
                    break;
                case TEN_TIMES_LOCK:
                    lockTime = 10;
                    break;


                case ONE_HOUR:
                    spaceTime = 1 * 60 * 60 * 1000;
                    updateByLockTimes = false;
                    break;
                case TWO_HOUR:
                    spaceTime = 2 * 60 * 60 * 1000;
                    updateByLockTimes = false;
                    break;
                case FIVE_MINUTE:
                    spaceTime = 5 * 60 * 1000;
                    updateByLockTimes = false;
                    break;
                case TEN_MINUTE:
                    spaceTime = 10 * 60 * 1000;
                    updateByLockTimes = false;
                    break;
                case THIRTY_MINUTE:
                    spaceTime = 30 * 60 * 1000;
                    updateByLockTimes = false;
                    break;
                case TWENTY_MINUTE:
                    spaceTime = 20 * 60 * 1000;
                    updateByLockTimes = false;
                    break;
                default:
                    lockTime = 1;
                    break;
            }


            scheduleUpdateMode(updateByLockTimes);

        }
    }


    private void scheduleUpdateMode(boolean updateByLockTimes) {

        checkIfEnough();

        if (updateByLockTimes) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
            mUserPresentBroadCastReceiver = new UserPresentBroadCastReceiver();
            lockTimeCount = 0;
            this.registerReceiver(mUserPresentBroadCastReceiver, intentFilter);
        } else {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {

                    doOnClockEvent();

                }
            }, spaceTime, spaceTime);
        }
    }

    class UserPresentBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            lockTimeCount++;

            if ((lockTimeCount %= lockTime) == 0) {

                doOnClockEvent();

            }

        }
    }

    private void checkIfEnough() {
        if (OneWordSQLiteOpenHelper.getInstance().countToShow() < 10) {
            getSomeOneWord(20);
        }
    }


    private void getSomeOneWord(int count) {

        for (int i = 0; i < count; ++i) {
            OneWordApi.requestOneWord(this);
        }

    }

    private void doOnClockEvent() {
        Log.i(LOG_TAG, "锁屏一言自动更新服务 执行");

        checkIfEnough();


        WordBean bean = OneWordSQLiteOpenHelper.getInstance().queryOneWordFromToShowByASC();

        if (bean != null) {
            OneWordSQLiteOpenHelper.getInstance().removeFromToShow(bean.id);
        }


        if (bean == null) {
            bean = OneWordSQLiteOpenHelper.getInstance().queryOneWordFromHistoryByRandom();
        }
        if (bean == null) {
            bean = OneWordSQLiteOpenHelper.getInstance().queryOneWordFromFavorByRandom();
        }


        if (bean == null) {
            Toast.makeText(this, "网络不行呀，\n没有新的一言可以更新", Toast.LENGTH_LONG).show();
            return;
        }

        checkIfEnough();

//        if (bean.like || OneWordSQLiteOpenHelper.getInstance().query_one_item_exist(TABLE_LIKE, bean)) {
//            bean.like = true;
//        }

        if (bean != null) {

            OneWordSQLiteOpenHelper.getInstance().insertToHistory(bean);

//            SharedPreferencesUtil.saveCurOneWordId(this, bean.id);

            OneWordFileStation.saveOneWordJSON(bean);
            BroadcastSender.sendUseNewOneWordInfoBroadcast(this, bean);
        }

        Log.i(LOG_TAG, "锁屏一言自动更新服务 执行完毕");
    }


    @Override
    public void onDestroy() {

        Log.e(LOG_TAG, "onDestroy");


        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mUserPresentBroadCastReceiver != null) {
            this.unregisterReceiver(mUserPresentBroadCastReceiver);
            mUserPresentBroadCastReceiver = null;
        }

        OneWordSQLiteOpenHelper.closeDataBase();

        super.onDestroy();

    }


    //implement reference Observer

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(WordBean wordBean) {

        Log.i(LOG_TAG, "成功获取到一言");

        OneWordSQLiteOpenHelper.getInstance().insertToToShow(wordBean);

    }


    @Override
    public void onError(Throwable e) {
        Log.e(LOG_TAG, "error when get new oneword");
//        BugUtil.printAndSaveCrashThrow2File(e);
    }

    @Override
    public void onComplete() {

    }


    public void toForeground() {
        Notification notification = new Notification.Builder(this)
                .setContentText("锁屏一言自动刷新服务")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .build();

        startForeground(1, notification);
    }

}
