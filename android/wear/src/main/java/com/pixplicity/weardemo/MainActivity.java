package com.pixplicity.weardemo;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pixplicity.weardemo.shared.NotificationUtils;

public class MainActivity extends Activity implements WatchViewStub.OnLayoutInflatedListener {

    private static final int REQUEST_CODE_SIMPLE = 100;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);

        stub.setOnLayoutInflatedListener(this);
    }

    @Override
    public void onLayoutInflated(WatchViewStub watchViewStub) {
        mTextView = (TextView) watchViewStub.findViewById(R.id.text);

        // Only to illustrate the context
        final Context context = this;

        final int iconResId = R.drawable.ic_launcher;

        // Various intents used in notifications
        Intent intentOpen = new Intent(context, MainActivity.class);
        final PendingIntent pendingIntentOpen = PendingIntent.getActivity(
                context,
                REQUEST_CODE_SIMPLE,
                intentOpen,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // NOTE this does not work from the handheld; the notification needs to be created from a Wear app
        Button btDisplayIntent = (Button) findViewById(R.id.bt_display_intent);
        btDisplayIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationCompat.Builder builder = NotificationUtils.createNotification(
                        context,
                        pendingIntentOpen,
                        "This notification uses a DisplayIntent",
                        iconResId);

                NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
//                        .setBackground(background)
                        .setCustomSizePreset(Notification.WearableExtender.SIZE_FULL_SCREEN)
                        .setDisplayIntent(PendingIntent.getActivity(context, 0, new Intent(context, FullscreenWearActivity.class), 0));
                builder.extend(wearableExtender);

                NotificationUtils.showNotification(context, builder);
            }
        });
    }
}
