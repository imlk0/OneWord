package top.imlk.oneword.application.client.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import top.imlk.oneword.R;
import top.imlk.oneword.application.client.activity.MainActivity;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.util.SharedPreferencesUtil;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by imlk on 2018/8/19.
 */
public class NotificationHelper {


    private NotificationCompat.Builder builder;
    private RemoteViews remoteViews;

    private static final String CHANNEL_ID = "top.imlk.oneword.notification_channel";

    public synchronized Notification getShowingCurOnewordNotification(Context context, WordBean currentWord) {

        if (builder == null) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle("锁屏一言·自动刷新服务")
                    .setSmallIcon(R.mipmap.ic_oneword_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_oneword_icon))
                    .setAutoCancel(false)
                    .setContentIntent(PendingIntent.getActivity(context, 1, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
//                .setDeleteIntent(PendingIntent.getBroadcast(this, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT))
                    .setWhen(System.currentTimeMillis());

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_oneword);


            if (!SharedPreferencesUtil.isShowNotificationTitleOpened(context)) {
                remoteViews.setViewVisibility(R.id.ll_notification_title, View.GONE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "锁屏一言自动刷新服务", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(false);
                notificationChannel.setShowBadge(false);
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

                NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
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

        return builder.build();
    }


}
