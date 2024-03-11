package com.example.campuseventstudents;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyNotification extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        shownotification(getApplicationContext(),message.getNotification().getTitle(),message.getNotification().getBody(),i);
    }

    private void shownotification(Context context, String title, String message, Intent intent)
    {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        int notificationId=1;
        String channelId = "Channel1";
        String channelName = "My Channe";

        int important = NotificationManager.IMPORTANCE_HIGH;

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName,important
            );
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(message);

        PendingIntent intent1 = PendingIntent.getActivity(context,1,intent,PendingIntent.FLAG_MUTABLE);
        mBuilder.setContentIntent(intent1);
        notificationManager.notify(notificationId,mBuilder.build());
    }
}
