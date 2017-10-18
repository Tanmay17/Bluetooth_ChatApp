package com.example.android.bluetoothchatapp;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    Animation a;
    ImageView iv;
    BluetoothAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        a= AnimationUtils.loadAnimation(MainActivity.this,R.anim.zoom);
        iv=(ImageView)findViewById(R.id.imageView);
        adapter=BluetoothAdapter.getDefaultAdapter();
        iv.setAnimation(a);
        if(!adapter.isEnabled()){
            adapter.enable();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                }catch(Exception e)
                {}
                Intent intn=new Intent(MainActivity.this,Front.class);
                startActivity(intn);
            }
        }).start();
    }
}


