package com.pixplicity.weardemo.shared;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class NotificationUtils {

    private static final int NOTIFICATION_ID = 1000;

    public static NotificationCompat.Builder createNotification(Context context, PendingIntent pendingIntentOpen, String text, int iconResId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle("Hello world!")
                .setContentText(text)
                .setContentIntent(pendingIntentOpen)
                .setSmallIcon(iconResId);

        return builder;
    }

    public static void showNotification(Context context, NotificationCompat.Builder notificationBuilder) {
        Notification notification = notificationBuilder.build();
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification);
    }

}
