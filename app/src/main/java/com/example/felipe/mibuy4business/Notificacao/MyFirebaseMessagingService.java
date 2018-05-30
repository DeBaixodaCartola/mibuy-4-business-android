package com.example.felipe.mibuy4business.Notificacao;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.felipe.mibuy4business.MainActivity;
import com.example.felipe.mibuy4business.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private DatabaseReference databaseReference;
    private FirebaseAuth autenticacao;
    private String usuarioLogado;


    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
       if (remoteMessage.getData().size() > 0) {
        Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        int id = (int) (System.currentTimeMillis() / 1000);

        Map<String, String> data = remoteMessage.getData();

        //you can get your text message here.
        String text= data.get("body");
        String titulo = data.get("title");
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(titulo)
                .setContentText(text)
                .setAutoCancel(true)
                .setStyle( new NotificationCompat.BigTextStyle().bigText( text ))
                .setSound(alarmSound)
                .setLights(Color.BLUE,1,1)
                .setVibrate(new long[]{ 1000, 1000, 1000, 1000,})
                ;

        try{
            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this,defaultSoundUri);
            toque.play();

        }catch (Exception e){

        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



        notificationManager.notify(id, notificationBuilder.build());


    }


    private void sendNotification(String messageBody) {




        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("MyBuy")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setLights(Color.BLUE,1,1)
                .setVibrate(new long[]{ 100, 250, 100, 500, 800})
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        try{
            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this,defaultSoundUri);
            toque.play();

        }catch (Exception e){

        }

        notificationManager.notify(0 /* id da notification */, notificationBuilder.build());
    }


}
