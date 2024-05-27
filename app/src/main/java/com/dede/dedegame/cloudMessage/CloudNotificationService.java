package com.dede.dedegame.cloudMessage;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.dede.dedegame.R;
import com.dede.dedegame.presentation.common.LogUtil;
import com.dede.dedegame.presentation.splash.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class CloudNotificationService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        LogUtil.getInstance().e("==============================" + "Refreshed token: " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        sendNotification(message);
    }

    private void sendNotification(RemoteMessage data) {
        String channelId = "dede_app_notification";
        String title = "";
        String bodyMessage = "";
        if (data.getNotification() != null && data.getNotification().getTitle() != null) {
            title = data.getNotification().getTitle();
        }
        if (data.getNotification() != null && data.getNotification().getBody() != null) {
            bodyMessage = data.getNotification().getBody();
        }

        int requestCode = (int) System.currentTimeMillis();
        Intent intent = new Intent(this, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        int idNotify = (int) System.currentTimeMillis();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(bodyMessage)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setLargeIcon(getBitmapFromUrl(data.getNotification().getImageUrl().toString()))
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(getBitmapFromUrl(data.getNotification().getImageUrl().toString()))
                        .bigLargeIcon(null))
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel for dede app",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(idNotify, notificationBuilder.build());
    }

    private Bitmap getBitmapFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            Log.d("awesome", "Error in getting notification image: " + e.getLocalizedMessage());
            return null;
        }
    }
}
