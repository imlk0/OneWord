package top.imlk.oneword.application.client.service;

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

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

import top.imlk.oneword.application.client.helper.NotificationHelper;
import top.imlk.oneword.bean.ApiBean;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.bean.WordViewConfig;
import top.imlk.oneword.net.WordRequestObserver;
import top.imlk.oneword.dao.OneWordSQLiteOpenHelper;
import top.imlk.oneword.net.OneWordApi;
import top.imlk.oneword.util.BroadcastSender;
import top.imlk.oneword.util.OneWordFileStation;


/**
 * Created by imlk on 2018/5/26.
 */
public class OneWordAutoRefreshService extends Service implements WordRequestObserver {

    private static final String TAG = "OneWordAutoRefreshServ";


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
        TEN_TIMES_LOCK;

        public static Mode defaultMode() {
            return TWICE_LOCK;
        }
    }

    private Mode currentMode;
    private long spaceTime = 0;

    private int lockTimesLimit = 0;
    private int lockTimesCount = 0;

    private boolean refreshByLockTimes;
    private boolean canUpdate = true;// 合理控制更新周期

    private RefreshSignReceiver mRefreshSignReceiver;
    private Timer mTimer;

    private boolean isAutoRefreshOn = false;
    private boolean isShowNotificationOnewordOn = false;

    private ReSetNotificationWordSignReceiver reSetNotificationWordSignReceiver;
    private WordBean currentWord;
    private WordViewConfig currentConfig;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {//非绑定模式
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");

//        if (!SharedPreferencesUtil.isAutoRefreshOpened(this)) {
//            Log.i(TAG, "自动更新开关未启动，搞毛线啊");
//            Log.i(TAG, "Bye~");
//            this.stopSelf();
//        }

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Hello~");

        Log.i(TAG, intent == null ? "intent is null" : "intent.getAction():" + intent.getAction());

        if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
            switch (intent.getAction()) {
                case BroadcastSender.CMD_SERVICES_START_AUTO_REFRESH:
                    isAutoRefreshOn = true;
                    updateNotification();

                    Serializable serializable = intent.getSerializableExtra(BroadcastSender.THE_REFRESH_MODE);

                    if (serializable != null && serializable instanceof Mode) {
                        currentMode = ((Mode) serializable);
                    } else {
                        currentMode = Mode.defaultMode();
                    }

                    canUpdate = true;
                    lockTimesCount = 0;

                    parseMode();
                    scheduleAutoRefresh();


                    Log.i(TAG, "start auto refresh");
                    break;
                case BroadcastSender.CMD_SERVICES_STOP_AUTO_REFRESH:
                    isAutoRefreshOn = false;
                    updateNotification();
                    stopAutoRefreshTask();

                    Log.i(TAG, "stop auto refresh");
                    break;
                case BroadcastSender.CMD_SERVICES_START_SHOW_NOTIFICATION_ONEWORD:
                    isShowNotificationOnewordOn = true;

                    WordBean wordBean = intent.getParcelableExtra(BroadcastSender.THE_INIT_WORDBEAN);
                    if (wordBean != null) {
                        currentWord = wordBean;
                    } else if (currentWord == null) {
                        currentWord = OneWordFileStation.readOneWordJSON();
                        if (currentWord == null) {
                            currentWord = WordBean.generateDefaultBean();
                        }
                    }

                    if (currentConfig == null) {
                        currentConfig = OneWordFileStation.readWordViewConfigJSON();
                        if (currentConfig == null) {
                            currentConfig = WordViewConfig.generateDefaultBean();
                        }
                    }
                    updateNotification();
                    scheduleReSetNotificationWord();

                    Log.i(TAG, "start show notification oneword");
                    break;
                case BroadcastSender.CMD_SERVICES_STOP_SHOW_NOTIFICATION_ONEWORD:
                    isShowNotificationOnewordOn = false;
                    updateNotification();
                    clearScheduleReSetNotificationWord();

                    Log.i(TAG, "stop show notification oneword");
                    break;


                default:
                    Log.i(TAG, "NO match command!!!");
                    break;
            }
        }

        return START_REDELIVER_INTENT;
    }


    private void parseMode() {
        if (currentMode == null) {
            Log.i(TAG, "wtf! currentMode is null, error when parse");
        } else {
            Log.i(TAG, "currentMode -> " + Mode.values()[currentMode.ordinal()]);

            refreshByLockTimes = true;

            switch (currentMode) {
                case EVERY_LOCK:
                    lockTimesLimit = 1;
                    break;
                case TWICE_LOCK:
                    lockTimesLimit = 2;
                    break;
                case FIFTH_LOCK:
                    lockTimesLimit = 5;
                    break;
                case TEN_TIMES_LOCK:
                    lockTimesLimit = 10;
                    break;


                case ONE_HOUR:
                    spaceTime = 1 * 60 * 60 * 1000;
                    refreshByLockTimes = false;
                    break;
                case TWO_HOUR:
                    spaceTime = 2 * 60 * 60 * 1000;
                    refreshByLockTimes = false;
                    break;
                case FIVE_MINUTE:
                    spaceTime = 5 * 60 * 1000;
                    refreshByLockTimes = false;
                    break;
                case TEN_MINUTE:
                    spaceTime = 10 * 60 * 1000;
                    refreshByLockTimes = false;
                    break;
                case THIRTY_MINUTE:
                    spaceTime = 30 * 60 * 1000;
                    refreshByLockTimes = false;
                    break;
                case TWENTY_MINUTE:
                    spaceTime = 20 * 60 * 1000;
                    refreshByLockTimes = false;
                    break;
                default:
                    lockTimesLimit = 1;
                    break;
            }
        }
    }


    private void scheduleAutoRefresh() {

        checkIfEnoughAsyn();

        clearScheduleAutoRefresh();

        IntentFilter intentFilter = new IntentFilter();
        mRefreshSignReceiver = new RefreshSignReceiver();

        if (refreshByLockTimes) {
            intentFilter.addAction(Intent.ACTION_SCREEN_OFF);

        } else {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {

                    doOnClockEvent();

                }
            }, spaceTime, spaceTime);
            intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        }

        this.registerReceiver(mRefreshSignReceiver, intentFilter);

    }

    class RefreshSignReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case Intent.ACTION_SCREEN_OFF:

                    if (refreshByLockTimes) {// 若根据锁屏次数来刷新

                        lockTimesCount++;

                        if ((lockTimesCount %= lockTimesLimit) == 0) {

                            canUpdate = true;
                            doOnClockEvent();
                        }
                    }

                    updateNotification();
                    break;
                case Intent.ACTION_SCREEN_ON:
                    if (!refreshByLockTimes) {//根据时间间隔刷新
                        canUpdate = true;
                    }
                    break;
            }

        }
    }


    private void stopAutoRefreshTask() {

        clearScheduleAutoRefresh();

        OneWordSQLiteOpenHelper.closeDataBase();
    }

    private void checkIfEnoughAsyn() {
        if (OneWordSQLiteOpenHelper.getInstance().countToShow() < 5) {
            getSomeOneWord(20);
        }
    }


    private void getSomeOneWord(int count) {

        for (int i = 0; i < count; ++i) {
            OneWordApi.requestOneWord(this);
        }

    }

    private void doOnClockEvent() {
        Log.i(TAG, "锁屏一言自动更新服务 执行");

        Log.i(TAG, "can update:" + canUpdate);

        if (canUpdate) {

            checkIfEnoughAsyn();


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

            checkIfEnoughAsyn();

//        if (bean.like || OneWordSQLiteOpenHelper.getInstance().query_one_item_exist(TABLE_LIKE, bean)) {
//            bean.like = true;
//        }

            if (bean != null) {

                OneWordSQLiteOpenHelper.getInstance().insertToHistory(bean);
                OneWordFileStation.saveOneWordJSON(bean);
                BroadcastSender.sendUseNewOneWordBroadcast(this, bean);

                currentWord = bean;
                canUpdate = false;
                Log.i(TAG, String.valueOf(currentWord));
            }
        }

        Log.i(TAG, "锁屏一言自动更新服务 执行完毕");
    }


    @Override
    public void onDestroy() {

        Log.i(TAG, "onDestroy");

        stopAutoRefreshTask();

        super.onDestroy();

//        System.exit(0);

    }

    private void clearScheduleAutoRefresh() {

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mRefreshSignReceiver != null) {
            this.unregisterReceiver(mRefreshSignReceiver);
            mRefreshSignReceiver = null;
        }
    }


    //implement reference WordRequestObserver

    @Override
    public void onStart(ApiBean apiBean) {

    }

    @Override
    public void onAcquire(ApiBean apiBean, WordBean wordBean) {

        Log.i(TAG, "成功获取到一言");

        OneWordSQLiteOpenHelper.getInstance().insertToToShow(wordBean);

    }


    @Override
    public void onError(ApiBean apiBean, Throwable e) {
        Log.e(TAG, "error when get new oneword", e);
//        BugUtil.printAndSaveCrashThrow2File(e);
    }


    class ReSetNotificationWordSignReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case Intent.ACTION_SCREEN_OFF:
                case Intent.ACTION_SCREEN_ON:

                    updateNotification();
                    break;
                case BroadcastSender.CMD_BROADCAST_SET_NEW_WORDBEAN:
                    WordBean wordBean = intent.getParcelableExtra(BroadcastSender.THE_NEW_WORDBEAN);
                    if (wordBean != null) {
                        currentWord = wordBean;
                        updateNotification();
                    }
                    break;
                case BroadcastSender.CMD_BROADCAST_SET_NEW_WORDVIEWCONFIG:
                    WordViewConfig wordViewConfig = intent.getParcelableExtra(BroadcastSender.THE_NEW_WORDVIEWCONFIG);
                    if (wordViewConfig != null) {
                        currentConfig = wordViewConfig;
                        updateNotification();
                    }
                    break;
            }

        }
    }

    private void scheduleReSetNotificationWord() {
        if (this.reSetNotificationWordSignReceiver == null) {
            IntentFilter intentFilter = new IntentFilter();
//            intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
            intentFilter.addAction(Intent.ACTION_SCREEN_ON);
            intentFilter.addAction(BroadcastSender.CMD_BROADCAST_SET_NEW_WORDBEAN);
            intentFilter.addAction(BroadcastSender.CMD_BROADCAST_SET_NEW_WORDVIEWCONFIG);
            this.reSetNotificationWordSignReceiver = new ReSetNotificationWordSignReceiver();
            registerReceiver(this.reSetNotificationWordSignReceiver, intentFilter);
        }
    }

    private void clearScheduleReSetNotificationWord() {
        if (this.reSetNotificationWordSignReceiver != null) {
            unregisterReceiver(this.reSetNotificationWordSignReceiver);
            this.reSetNotificationWordSignReceiver = null;
        }
    }

    private NotificationHelper notificationHelper = new NotificationHelper();

    private synchronized void updateNotification() {
        if (isShowNotificationOnewordOn) {
            startForeground(1, notificationHelper.getShowingCurOnewordNotification(this, currentWord, currentConfig));
        } else {
            if (isAutoRefreshOn) {
                startForeground(1, notificationHelper.getShowingAutoRefreshServiceRunningNotification(this));
            } else {
                stopForeground(true);
            }
        }
    }

}
