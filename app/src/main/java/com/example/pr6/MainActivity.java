package com.example.pr6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.example.pr6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final int NOTIFY_ID = 322;
    private static String CHANNEL_ID = "My channel";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        requestOverlayPermission();
        binding.defaultActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("main","click");
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.search)
                        .setContentTitle("Поздравляем!")
                        .setContentText("Вы нажали на кнопку")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                    return;
                }
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"donut", NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableLights(true);
                channel.setShowBadge(true);
                notificationManagerCompat.createNotificationChannel(channel);
                builder.setChannelId(CHANNEL_ID);
                notificationManagerCompat.notify(NOTIFY_ID, builder.build());
                startService(new Intent(MainActivity.this,MyService.class));
            }
        });
        setContentView(binding.getRoot());
    }
    private void requestOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            myIntent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(myIntent, 1);
        }
    }
}