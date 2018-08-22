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

import com.zqc.opencc.android.lib.ChineseConverter;
import com.zqc.opencc.android.lib.ConversionType;

import top.imlk.oneword.R;
import top.imlk.oneword.application.client.activity.MainActivity;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.bean.WordViewConfig;
import top.imlk.oneword.util.BroadcastSender;
import top.imlk.oneword.util.SharedPreferencesUtil;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by imlk on 2018/8/19.
 */
public class NotificationHelper {

    private NotificationCompat.Builder showingAutoRefreshServiceBuilder;

    public synchronized Notification getShowingAutoRefreshServiceRunningNotification(Context context) {
        if (showingAutoRefreshServiceBuilder == null) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle("锁屏一言·自动刷新服务")
                    .setContentText("通知用于后台服务保活，可长按选择隐藏，请勿黑阈我")
                    .setSmallIcon(R.mipmap.ic_oneword_icon)
                    .setAutoCancel(false);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "锁屏一言·一言", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(false);
                notificationChannel.setShowBadge(false);
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

                NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                manager.createNotificationChannel(notificationChannel);
//                builder.setCustomContentView(remoteViews);
            }


            builder.setChannelId(CHANNEL_ID);
            builder.setPriority(NotificationCompat.PRIORITY_MAX);

            this.showingAutoRefreshServiceBuilder = builder;
        }


        showingAutoRefreshServiceBuilder.setWhen(System.currentTimeMillis());

        return showingAutoRefreshServiceBuilder.build();

    }


    private NotificationCompat.Builder showingCurOnewordBuilder;
    private RemoteViews showingCurOnewordRemoteViews;

    private static final String CHANNEL_ID = "top.imlk.oneword.notification_channel";

    public synchronized Notification getShowingCurOnewordNotification(Context context, WordBean currentWord, WordViewConfig wordViewConfig) {
        if (currentWord == null) {
            currentWord = WordBean.generateDefaultBean();
        }
        if (showingCurOnewordBuilder == null) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle("锁屏一言·一言")
                    .setSmallIcon(R.mipmap.ic_oneword_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_oneword_icon))
                    .setAutoCancel(false);
//                .setDeleteIntent(PendingIntent.getBroadcast(this, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT))

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_oneword);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "锁屏一言·一言", NotificationManager.IMPORTANCE_HIGH);
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


            this.showingCurOnewordBuilder = builder;
            this.showingCurOnewordRemoteViews = remoteViews;

        }

        // content
        if (wordViewConfig.toTraditional) {
            showingCurOnewordRemoteViews.setTextViewText(R.id.tv_content, ChineseConverter.convert(currentWord.content, ConversionType.S2T, context));
        } else {
            showingCurOnewordRemoteViews.setTextViewText(R.id.tv_content, currentWord.content);
        }

        // reference
        if (TextUtils.isEmpty(currentWord.reference)) {
            showingCurOnewordRemoteViews.setTextViewText(R.id.tv_reference, "");
            showingCurOnewordRemoteViews.setViewVisibility(R.id.tv_reference, View.GONE);
        } else {
            if (wordViewConfig.toTraditional) {
                showingCurOnewordRemoteViews.setTextViewText(R.id.tv_reference, ChineseConverter.convert("——" + currentWord.reference, ConversionType.S2T, context));
            } else {
                showingCurOnewordRemoteViews.setTextViewText(R.id.tv_reference, "——" + currentWord.reference);
            }
            showingCurOnewordRemoteViews.setViewVisibility(R.id.tv_reference, View.VISIBLE);
        }

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(BroadcastSender.THE_CLICKED_WORDBEAN, currentWord);
        showingCurOnewordBuilder.setContentIntent(PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        showingCurOnewordBuilder.setCustomContentView(showingCurOnewordRemoteViews);
        showingCurOnewordBuilder.setCustomBigContentView(showingCurOnewordRemoteViews);

        showingCurOnewordBuilder.setWhen(System.currentTimeMillis());

        return showingCurOnewordBuilder.build();
    }


    public static void notify(Context context, int id, Notification notification) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }

}
