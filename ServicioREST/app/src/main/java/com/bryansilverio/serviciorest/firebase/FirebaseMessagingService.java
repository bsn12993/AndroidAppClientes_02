package com.bryansilverio.serviciorest.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.bryansilverio.serviciorest.MainActivity;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Bryan Silverio on 28/12/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String from=remoteMessage.getFrom();
        Log.d("TAG","Mensaje recibido de "+from);
        if(remoteMessage.getNotification()!=null){
            Log.d("Notificacion","Notificacion: "+remoteMessage.getNotification().getBody());
        }

        if(remoteMessage.getData().size()>0){
            Log.d("-> DATA <--",remoteMessage.getData().toString());
        }
    }

    public void mostrarNotificacion(String titulo, String mensaje){
        Intent intent=new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendientIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.support.v4.app.NotificationCompat.Builder notificacion=new NotificationCompat.Builder(this)
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendientIntent);

        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificacion.build());
    }
}
