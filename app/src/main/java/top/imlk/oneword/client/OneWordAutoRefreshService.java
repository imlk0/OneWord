package top.imlk.oneword.client;

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
import top.imlk.oneword.Hitokoto.HitokotoApi;
import top.imlk.oneword.Hitokoto.HitokotoBean;
import top.imlk.oneword.common.StaticValue;
import top.imlk.oneword.dao.OneWordSQLiteOpenHelper;
import top.imlk.oneword.util.BroadcastSender;
import top.imlk.oneword.util.SharedPreferencesUtil;

import static top.imlk.oneword.dao.OneWordSQLiteOpenHelper.TABLE_HISTORY;
import static top.imlk.oneword.dao.OneWordSQLiteOpenHelper.TABLE_LIKE;
import static top.imlk.oneword.dao.OneWordSQLiteOpenHelper.TABLE_READY_TO_SHOW;

/**
 * Created by imlk on 2018/5/26.
 */
public class OneWordAutoRefreshService extends Service implements Observer<HitokotoBean> {

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


        //更新c_Array_custom数组
        if (HitokotoApi.Parameter.c_Array_custom == null) {
            HitokotoApi.refreshCustomArray(SharedPreferencesUtil.readOneWordTypes(this));
        }

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, "Hello~");

        Log.e(LOG_TAG, intent == null ? "intent is null" : intent.getAction() + "");


        if (intent == null || TextUtils.isEmpty(intent.getAction()) || StaticValue.CMD_SERVICES_START_AUTO_REFRESH_SERVICE.equals(intent.getAction())) {

            Log.e("isRefreshOpened()", SharedPreferencesUtil.isRefreshOpened(this) + "");

            if (SharedPreferencesUtil.isRefreshOpened(this)) {

                currentMode = SharedPreferencesUtil.getRefreshMode(this);

                parseMode();


            } else {
                Log.e(LOG_TAG, "自动更新开关未启动，搞毛线啊");
                Log.i(LOG_TAG, "Bye~");
                this.stopSelf();
            }
        } else if (StaticValue.CMD_SERVICES_STOP_SERVICE.equals(intent.getAction())) {


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

                    doClockEvent();

                }
            }, spaceTime, spaceTime);
        }
    }

    class UserPresentBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            lockTimeCount++;

            if ((lockTimeCount %= lockTime) == 0) {

                doClockEvent();

            }

        }
    }

    private void checkIfEnough() {
        if (OneWordSQLiteOpenHelper.getInstance(this).count_a_table(TABLE_READY_TO_SHOW) < 10) {
            getSomeOneWord(20);
        }
    }


    private void getSomeOneWord(int count) {

        for (int i = 0; i < count; ++i) {
            HitokotoApi.requestOneWord(this);
        }

    }

    private void doClockEvent() {
        Log.i(LOG_TAG, "锁屏一言自动更新服务");
        HitokotoBean bean = OneWordSQLiteOpenHelper.getInstance(this).get_a_item_order_by(TABLE_READY_TO_SHOW, OneWordSQLiteOpenHelper.KEY_ADDED_AT + " ASC");
        OneWordSQLiteOpenHelper.getInstance(this).remove_one_item(TABLE_READY_TO_SHOW, bean);


        if (bean == null) {
            bean = OneWordSQLiteOpenHelper.getInstance(this).get_a_item_order_by(TABLE_LIKE, "random()");
        }

        if (bean == null) {
            bean = OneWordSQLiteOpenHelper.getInstance(this).get_a_item_order_by(TABLE_HISTORY, "random()");
        }

        if (bean == null) {
            Toast.makeText(this, "网络不行呀，\n没有新的一言可以更新", Toast.LENGTH_LONG).show();
            return;
        }

        checkIfEnough();

        if (bean.like || OneWordSQLiteOpenHelper.getInstance(this).query_one_item_exist(TABLE_LIKE, bean)) {
            bean.like = true;
        }

        OneWordSQLiteOpenHelper.getInstance(this).insert_one_item(TABLE_HISTORY, bean);

        SharedPreferencesUtil.saveCurOneWord(this, bean);

        BroadcastSender.sendSetNewLockScreenInfoBroadcast(this, bean);
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

        if (!OneWordSQLiteOpenHelper.isDataBaseClosed()) {
            OneWordSQLiteOpenHelper.getInstance(this).close();
        }


        super.onDestroy();

        System.exit(0);
    }


    //implement from Observer

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(HitokotoBean hitokotoBean) {

        Log.i(LOG_TAG, "成功获取到一言");

        OneWordSQLiteOpenHelper.getInstance(this).insert_one_item(TABLE_READY_TO_SHOW, hitokotoBean);

    }


    @Override
    public void onError(Throwable e) {
        Log.e(LOG_TAG, "error when get new oneword", e);
    }

    @Override
    public void onComplete() {

    }

}
