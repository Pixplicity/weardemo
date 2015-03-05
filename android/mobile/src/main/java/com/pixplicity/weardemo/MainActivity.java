package com.pixplicity.weardemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;
import com.pixplicity.weardemo.shared.Constants;


public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, DataApi.DataListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_CODE_SIMPLE = 100;

    private static final int NOTIFICATION_ID = 1000;

    private static final String EXTRA_REPLY = "reply";

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Only to illustrate the context
        final Context context = this;

        // Various intents used in notifications
        Intent intentOpen = new Intent(context, MainActivity.class);
        final PendingIntent pendingIntentOpen = PendingIntent.getActivity(
                context,
                REQUEST_CODE_SIMPLE,
                intentOpen,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentAction = new Intent(context, MainActivity.class);
        final PendingIntent pendingIntentAction = PendingIntent.getActivity(
                context,
                REQUEST_CODE_SIMPLE,
                intentAction,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Various shared resources
        final Bitmap background = BitmapFactory.decodeResource(
                context.getResources(),
                R.drawable.im_backdrop);

        Button btSimple = (Button) findViewById(R.id.bt_simple);
        btSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationCompat.Builder builder = createNotification(
                        context,
                        pendingIntentOpen,
                        "This is a simple notification");

                NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                        R.drawable.ic_action_reply,
                        "Reply",
                        pendingIntentAction)
                        .addRemoteInput(new RemoteInput.Builder(EXTRA_REPLY)
                                .setLabel("Send a reply")
                                .setChoices(new CharSequence[]{
                                        "Hello Amsterdam!",
                                        "Hello Berlin!",
                                        "Hello Copenhagen!",
                                        "Hello Dublin!",
                                        "Hello Edinburgh!",
                                        "Hello Fortaleza!"})
                                .build())
                        .build();
                builder.addAction(action);

                showNotification(context, builder);
            }
        });

        Button btBackground = (Button) findViewById(R.id.bt_background);
        btBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationCompat.Builder builder = createNotification(
                        context,
                        pendingIntentOpen,
                        "This notification has a background");

                NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                        .setBackground(background);
                builder.extend(wearableExtender);

                showNotification(context, builder);
            }
        });

        Button btWearOnlyAction = (Button) findViewById(R.id.bt_wear_only_action);
        btWearOnlyAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationCompat.Builder builder = createNotification(
                        context,
                        pendingIntentOpen,
                        "This notification has a wear-only action");

                NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                        .setBackground(background)
                        .addAction(new NotificationCompat.Action.Builder(
                                R.drawable.ic_action_mute,
                                "Mute",
                                pendingIntentAction)
                                .build());

                builder.extend(wearableExtender);

                showNotification(context, builder);
            }
        });

        Button btVoiceReply = (Button) findViewById(R.id.bt_voice_reply);
        btVoiceReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationCompat.Builder builder = createNotification(
                        context,
                        pendingIntentOpen,
                        "This notification has a voice reply");

                NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                        .setBackground(background);
                builder.extend(wearableExtender);

                builder.addAction(new NotificationCompat.Action.Builder(
                        R.drawable.ic_action_reply,
                        "Reply",
                        pendingIntentAction)
                        .addRemoteInput(new RemoteInput.Builder(EXTRA_REPLY)
                                .setLabel("Send a reply")
                                .setChoices(new CharSequence[]{
                                        "Hello Amsterdam!",
                                        "Hello Berlin!",
                                        "Hello Copenhagen!",
                                        "Hello Dublin!",
                                        "Hello Edinburgh!",
                                        "Hello Fortaleza!"})
                                .build())
                        .build());

                showNotification(context, builder);
            }
        });

        Button btPages = (Button) findViewById(R.id.bt_pages);
        btPages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationCompat.Builder builder = createNotification(
                        context,
                        pendingIntentOpen,
                        "This notification has a page of information");

                Notification chatHistory = new NotificationCompat.Builder(context)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(getChatHistory()))
                        .build();

                Bitmap bigIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);

                Notification picture = new NotificationCompat.Builder(context)
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(bigIcon))
                        .build();

                NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                        .setBackground(background)
                        .addPage(chatHistory)
                        .addPage(picture);
                builder.extend(wearableExtender);

                showNotification(context, builder);
            }
        });

        Button btDisplayIntent = (Button) findViewById(R.id.bt_display_intent);
        btDisplayIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationCompat.Builder builder = createNotification(
                        context,
                        pendingIntentOpen,
                        "This notification uses a DisplayIntent");

                NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                        .setBackground(background)
                        .setCustomSizePreset(Notification.WearableExtender.SIZE_FULL_SCREEN)
                        .setDisplayIntent(PendingIntent.getActivity(context, 0, new Intent(context, FullscreenWearActivity.class), 0));
                builder.extend(wearableExtender);

                showNotification(context, builder);
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        sendData();
    }

    private NotificationCompat.Builder createNotification(Context context, PendingIntent pendingIntentOpen, String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle("Hello world!")
                .setContentText(text)
                .setContentIntent(pendingIntentOpen)
                .setSmallIcon(R.drawable.ic_launcher);

        return builder;
    }

    public void showNotification(Context context, NotificationCompat.Builder notificationBuilder) {
        Notification notification = notificationBuilder.build();
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification);
    }

    private CharSequence getChatHistory() {
        return "Paul\nWanna grab a pizza later?\n\nThifane\nSure\n\nPaul\nAny preference?\nHow about pepperoni?";
    }

    private void sendData() {
        PutDataMapRequest dataMapRequest = PutDataMapRequest.create(Constants.DATA_ITEM_NAME);
        dataMapRequest.getDataMap().putString(Constants.FIELD_CHAT_HISTORY, getChatHistory().toString());
        Wearable.DataApi.putDataItem(mGoogleApiClient, dataMapRequest.asPutDataRequest());
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // TODO
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        // TODO
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // TODO
    }

}
