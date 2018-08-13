package top.imlk.oneword.application.client.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import top.imlk.oneword.R;
import top.imlk.oneword.application.client.activity.MainActivity;
import top.imlk.oneword.bean.ApiBean;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.net.WordRequestObserver;
import top.imlk.oneword.dao.OneWordSQLiteOpenHelper;
import top.imlk.oneword.net.OneWordApi;
import top.imlk.oneword.util.BroadcastSender;
import top.imlk.oneword.util.OneWordFileStation;
import top.imlk.oneword.util.SharedPreferencesUtil;


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
        TEN_TIMES_LOCK,

    }

    private Mode currentMode;

    private long spaceTime = 0;

    private int lockTimes = 0;
    private int lockTimesCount = 0;

    private boolean updateByLockTimes;

    private boolean canUpdate = true;

    private UserPresentBroadCastReceiver mUserPresentBroadCastReceiver;
    private Timer mTimer;


    private WordBean currentWord;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {//非绑定模式
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");


        if (!SharedPreferencesUtil.isAutoRefreshOpened(this)) {
            Log.i(TAG, "自动更新开关未启动，搞毛线啊");
            Log.i(TAG, "Bye~");
            this.stopSelf();
        }

        currentWord = OneWordFileStation.readOneWordJSON();
        if (currentWord == null) {
            currentWord = WordBean.generateDefaultBean();
        }
        toForeground();

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Hello~");

        Log.i(TAG, intent == null ? "intent is null" : intent.getAction() + "");


        if (intent == null || TextUtils.isEmpty(intent.getAction()) || BroadcastSender.CMD_SERVICES_START_AUTO_REFRESH_SERVICE.equals(intent.getAction())) {

            Log.i("isAutoRefreshOpened()", SharedPreferencesUtil.isAutoRefreshOpened(this) + "");

            if (SharedPreferencesUtil.isAutoRefreshOpened(this)) {

                currentMode = SharedPreferencesUtil.getRefreshMode(this);

                parseMode();


            } else {
                Log.i(TAG, "自动更新开关未启动，搞毛线啊");
                Log.i(TAG, "Bye~");
                this.stopSelf();
            }
        } else if (BroadcastSender.CMD_SERVICES_STOP_SERVICE.equals(intent.getAction())) {


            Log.i(TAG, "Bye~");
            this.stopSelf();
        } else {
            Log.i(TAG, "NO match command!!!");

        }


        return START_REDELIVER_INTENT;

    }


    private void parseMode() {
        if (currentMode == null) {
            Log.i(TAG, "wtf! currentMode is null, error when parse");
        } else {
            Log.i(TAG, "currentMode -> " + Mode.values()[currentMode.ordinal()]);

            updateByLockTimes = true;

            switch (currentMode) {
                case EVERY_LOCK:
                    lockTimes = 1;
                    break;
                case TWICE_LOCK:
                    lockTimes = 2;
                    break;
                case FIFTH_LOCK:
                    lockTimes = 5;
                    break;
                case TEN_TIMES_LOCK:
                    lockTimes = 10;
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
                    lockTimes = 1;
                    break;
            }


            scheduleUpdateMode();

        }
    }


    private void scheduleUpdateMode() {

        checkIfEnough();

        clearSchedule();

        IntentFilter intentFilter = new IntentFilter();
        mUserPresentBroadCastReceiver = new UserPresentBroadCastReceiver();

        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        if (updateByLockTimes) {
            lockTimesCount = 0;

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

        this.registerReceiver(mUserPresentBroadCastReceiver, intentFilter);

    }

    class UserPresentBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case Intent.ACTION_SCREEN_OFF:


                    if (updateByLockTimes) {// 若根据锁屏次数来刷新

                        lockTimesCount++;

                        if ((lockTimesCount %= lockTimes) == 0) {

                            canUpdate = true;
                            doOnClockEvent();
                        }
                    }

                    toForeground();
                    break;
                case Intent.ACTION_SCREEN_ON:
                    if (!updateByLockTimes) {//根据时间间隔刷新
                        canUpdate = true;
                    }
                    break;
            }

        }
    }

    private void checkIfEnough() {
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
                OneWordFileStation.saveOneWordJSON(bean);
                BroadcastSender.sendUseNewOneWordBroadcast(this, bean);

                currentWord = bean;
                canUpdate = false;
                toForeground();
                Log.i(TAG, String.valueOf(currentWord));
            }

        }

        Log.i(TAG, "锁屏一言自动更新服务 执行完毕");
    }


    @Override
    public void onDestroy() {

        Log.i(TAG, "onDestroy");

        clearSchedule();

        OneWordSQLiteOpenHelper.closeDataBase();

        super.onDestroy();

        System.exit(0);

    }

    private void clearSchedule() {

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mUserPresentBroadCastReceiver != null) {
            this.unregisterReceiver(mUserPresentBroadCastReceiver);
            mUserPresentBroadCastReceiver = null;
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
        Log.i(TAG, "error when get new oneword");
//        BugUtil.printAndSaveCrashThrow2File(e);
    }


    private static final String CHANNEL_ID = "top.imlk.oneword.notification_channel";

    private NotificationCompat.Builder builder;
    private RemoteViews remoteViews;

    public synchronized void toForeground() {

        if (builder == null) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("锁屏一言·自动刷新服务")
                    .setSmallIcon(R.mipmap.ic_oneword_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_oneword_icon))
                    .setAutoCancel(false)
                    .setContentIntent(PendingIntent.getActivity(this, 1, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
//                .setDeleteIntent(PendingIntent.getBroadcast(this, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT))
                    .setWhen(System.currentTimeMillis());

            RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification_oneword);


            if (!SharedPreferencesUtil.isShowNotificationTitleOpened(this)) {
                remoteViews.setViewVisibility(R.id.ll_notification_title, View.GONE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "锁屏一言自动刷新服务", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(false);
                notificationChannel.setShowBadge(false);
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.createNotificationChannel(notificationChannel);
//                builder.setCustomContentView(remoteViews);
            }

            builder.setChannelId(CHANNEL_ID);
//            builder.setContent(remoteViews);
            builder.setPriority(NotificationCompat.PRIORITY_MAX);


            this.builder = builder;
            this.remoteViews = remoteViews;

        }

        remoteViews.setTextViewText(R.id.tv_content, currentWord.content);

        if (TextUtils.isEmpty(currentWord.reference)) {
            remoteViews.setTextViewText(R.id.tv_reference, "");
            remoteViews.setViewVisibility(R.id.tv_reference, View.GONE);
        } else {
            remoteViews.setTextViewText(R.id.tv_reference, "——" + currentWord.reference);
            remoteViews.setViewVisibility(R.id.tv_reference, View.VISIBLE);
        }


        builder.setCustomContentView(remoteViews);
        builder.setCustomBigContentView(remoteViews);

        startForeground(1, builder.build());
    }

}
