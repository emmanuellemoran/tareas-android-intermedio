package com.example.tarea2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final String CHANEL_ID = "canal_ejemplo";
    Button btnBigText,btnBigImage;

    String sMensajes = "";
    Integer iMessage = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBigText = findViewById(R.id.btnBigText);
        btnBigImage = findViewById(R.id.btnBigImage);

    }
    @RequiresApi(Build.VERSION_CODES.O)//Solo ejecutar a partir de Android 8
    private void crearNotificationChannel(String channelId, String nombre) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null){
            notificationManager.createNotificationChannel(
                    new NotificationChannel(channelId,nombre,NotificationManager.IMPORTANCE_HIGH));
        }
    }

    public void btnBigText_onClick(View view) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            crearNotificationChannel(CHANEL_ID,"Canal de ejemplo");
        }
        mostrarNotificacion(0, construirNotificacionDeMensajes(0));
    }


    public void btnBigImage_onClick(View view) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            crearNotificationChannel(CHANEL_ID,"Canal de ejemplo");
        }
        mostrarNotificacion(0, construirNotificacionDeImagen(0));
    }

    private void mostrarNotificacion(int id, Notification notification)
    {
        //Mostrando Notificacion
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(id,notification);
    }

    private Notification construirNotificacionDeMensajes(int id)
    {
        sMensajes = sMensajes + "Nuevo Mensaje " + iMessage +"\r\n";

        Intent MainIntent = new Intent(this,MainActivity.class);

        Intent MessagesIntent = new Intent(this,MessagesActivity.class);
        MessagesIntent.putExtra("notification_id",id);
        MessagesIntent.putExtra("Mensajes", sMensajes);

        Intent dismissIntent = new Intent(this,DismissReceiver.class);
        dismissIntent.putExtra("notification_id",id);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntent(MainIntent);
        taskStackBuilder.addNextIntent(MessagesIntent);

        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(this,0,dismissIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent MessagesPendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder= new NotificationCompat.Builder(this,CHANEL_ID)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_message)
                .setColor(Color.parseColor(getResources().getString(R.color.colorPrimary)))//"#1ebea5"
                .setContentTitle(iMessage + " Nuevo(s) Mensaje(s)")
                .setContentText(sMensajes.split("\r\n")[0])
                .setStyle(new NotificationCompat.BigTextStyle().bigText(sMensajes))
                .setContentIntent(MessagesPendingIntent)
                .addAction(R.drawable.ic_show,"Ver",MessagesPendingIntent)
                .addAction(R.drawable.ic_dismiss,"Descartar",dismissPendingIntent);
        iMessage++;
        return  builder.build();
    }

    private Notification construirNotificacionDeImagen(int id)
    {
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.paradise);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 50, baos);

        Bitmap bmp = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length);

        Intent MainIntent = new Intent(this,MainActivity.class);

        Intent MessagesIntent = new Intent(this,MessagesActivity.class);
        MessagesIntent.putExtra("notification_id",id);
        //MessagesIntent.putExtra("Mensajes", sMensajes);
        MessagesIntent.putExtra("Image", baos.toByteArray());

        Intent dismissIntent = new Intent(this,DismissReceiver.class);
        dismissIntent.putExtra("notification_id",id);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntent(MainIntent);
        taskStackBuilder.addNextIntent(MessagesIntent);

        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(this,0,dismissIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent MessagesPendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder= new NotificationCompat.Builder(this,CHANEL_ID)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_message)
                .setColor(Color.parseColor(getResources().getString(R.color.colorPrimary)))//"#1ebea5"
                .setContentTitle("Nuevo Mensaje")
                //.setContentText(sMensajes.split("\r\n")[0])
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(sMensajes))
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bmp))
                .setLargeIcon(bmp)
                .setContentIntent(MessagesPendingIntent)
                .addAction(R.drawable.ic_show,"Ver",MessagesPendingIntent)
                .addAction(R.drawable.ic_dismiss,"Descartar",dismissPendingIntent);
        iMessage++;
        return  builder.build();
    }
}
