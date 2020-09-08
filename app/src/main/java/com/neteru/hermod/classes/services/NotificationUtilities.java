package com.neteru.hermod.classes.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;

import com.neteru.hermod.R;
import com.neteru.hermod.activities.MainActivity;

import java.util.Random;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

class NotificationUtilities {

    static void showNotification(Context globalContext, NotificationManager notificationManager, String title, String description, String url){

        if (notificationManager == null || !getDefaultSharedPreferences(globalContext).getBoolean("notification", true)) return;

        int notificationId = new Random().nextInt();
        String channelId = "AGRIDIGITALE-CHANNEL-ID";
        String channelName = "Agridigitale News Channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            notificationManager
                    .createNotificationChannel(
                            new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));

        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(globalContext, channelId)
                .setLargeIcon(BitmapFactory.decodeResource(globalContext.getResources(), R.drawable.direct_news_launcher))
                .setColor(ContextCompat.getColor(globalContext, R.color.colorPrimaryDark))
                .setSmallIcon(R.drawable.ic_direct_news_launcher)
                .setContentText(description)
                .setContentTitle(title)
                .setAutoCancel(true);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(globalContext);

        stackBuilder.addNextIntent(new Intent(globalContext, MainActivity.class).putExtra("url", url));

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(notificationId, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());

    }
}
