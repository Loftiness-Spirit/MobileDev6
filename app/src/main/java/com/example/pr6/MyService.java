package com.example.pr6;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.pr6.databinding.OverlayBinding;

public class MyService extends Service {
    WindowManager mWindowManager;
    View mView;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final boolean[] doBreak = {false};
        Log.i("gg","service");
        mWindowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        OverlayBinding binding = OverlayBinding.inflate(((LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE)));
        binding.dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Кнопка нажата",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                getApplicationContext().startActivity(intent);
                MyService.this.stopSelf(startId);
                doBreak[0] = true;
                stopSelf();
                mWindowManager.removeView(mView);


            }
        });
        mView = binding.getRoot();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER;

        mWindowManager.addView(mView, params);

        if(doBreak[0])return START_NOT_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }
}