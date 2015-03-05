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

import weardemo.pixplicity.com.com.R;


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

        Button btSimple = (Button) findViewById(R.id.bt_simple);
        btSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOpen = new Intent(context, MainActivity.class);
                PendingIntent pendingIntentOpen = PendingIntent.getActivity(
                        context,
                        REQUEST_CODE_SIMPLE,
                        intentOpen,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setContentTitle("Hello world!")
                        .setContentText("This is a simple notification")
                        .setContentIntent(pendingIntentOpen)
                        .setSmallIcon(R.drawable.ic_launcher);

                Intent intentAction = new Intent(context, MainActivity.class);
                PendingIntent pendingIntentAction = PendingIntent.getActivity(
                        context,
                        REQUEST_CODE_SIMPLE,
                        intentAction,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                Notification notification = builder.extend(
                        new NotificationCompat.WearableExtender()
                                .addAction(new NotificationCompat.Action.Builder(
                                        R.drawable.ic_launcher,
                                        "Action",
                                        pendingIntentAction)
                                        .addRemoteInput(new RemoteInput.Builder(EXTRA_REPLY)
                                                .setLabel("Reply action")
                                                .build())
                                        .build()))
                        .build();
                NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification);
            }
        });

        Button btVoiceReply = (Button) findViewById(R.id.bt_voice_reply);
        btVoiceReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOpen = new Intent(context, MainActivity.class);
                PendingIntent pendingIntentOpen = PendingIntent.getActivity(
                        context,
                        REQUEST_CODE_SIMPLE,
                        intentOpen,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setContentTitle("Hello world!")
                        .setContentText("This is a simple notification")
                        .setContentIntent(pendingIntentOpen)
                        .setSmallIcon(R.drawable.ic_launcher);

                Intent intentAction = new Intent(context, MainActivity.class);
                PendingIntent pendingIntentAction = PendingIntent.getActivity(
                        context,
                        REQUEST_CODE_SIMPLE,
                        intentAction,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                Notification notification = builder.extend(
                        new NotificationCompat.WearableExtender()
                                .addAction(new NotificationCompat.Action.Builder(
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
                                        .build()))
                        .build();
                NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification);
            }
        });

        Button btPages = (Button) findViewById(R.id.bt_pages);
        btPages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOpen = new Intent(context, MainActivity.class);
                PendingIntent pendingIntentOpen = PendingIntent.getActivity(
                        context,
                        REQUEST_CODE_SIMPLE,
                        intentOpen,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setContentTitle("Hello world!")
                        .setContentText("This is a simple notification")
                        .setContentIntent(pendingIntentOpen)
                        .setSmallIcon(R.drawable.ic_launcher);

                Intent intentAction = new Intent(context, MainActivity.class);
                PendingIntent pendingIntentAction = PendingIntent.getActivity(
                        context,
                        REQUEST_CODE_SIMPLE,
                        intentAction,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                Notification chatHistory = new NotificationCompat.Builder(context)
                        .setStyle(
                                new NotificationCompat.BigTextStyle()
                                        .bigText(getChatHistory()))
                        .build();

                Bitmap bigIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
                Notification picture = new NotificationCompat.Builder(context)
                        .setStyle(
                                new NotificationCompat.BigPictureStyle()
                                        .bigPicture(bigIcon))
                        .build();

                Notification notification = builder.extend(
                        new NotificationCompat.WearableExtender()
                                .addPage(chatHistory)
                                .addPage(picture))
                        .build();
                NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification);
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        sendData();
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
