package com.example.tarea2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MessagesActivity extends AppCompatActivity {

    TextView tvMensajes;
    ImageView ivMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        tvMensajes = findViewById(R.id.tvMensajes);
        ivMessage = findViewById(R.id.ivMessage);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.cancel(getIntent().getIntExtra("notification_id",0));
        String sMensajes = getIntent().getStringExtra("Mensajes");
        tvMensajes.setText(sMensajes);
        if(getIntent().hasExtra("Image")) {
            //ImageView iv = new ImageView(this);
            Bitmap bmp = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("Image"), 0, getIntent().getByteArrayExtra("Image").length);
            ivMessage.setImageBitmap(bmp);
        }
    }
}
