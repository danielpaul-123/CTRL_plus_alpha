package com.example.ctrl;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class shortcuts extends AppCompatActivity {
    Button shut_down,restart,sleep,lock;
    SharedPreferences preferences;
    String ipAddress;
    int port;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcuts);
        Toolbar toolbar = findViewById(R.id.tbar1);
        toolbar.setTitle(R.string.activity_shortcuts);
        shut_down = findViewById(R.id.shut_down);
        restart = findViewById(R.id.restart);
        sleep = findViewById(R.id.sleep);
        lock = findViewById(R.id.lock);

        preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        ipAddress = preferences.getString("ipAddress", "");
        port =  preferences.getInt("port", 0);

        shut_down.setOnClickListener(v -> {
            SignalSender.SendShortcuts(ipAddress, port, 404);

        });
        restart.setOnClickListener(v -> SignalSender.SendShortcuts(ipAddress,port,201));
        sleep.setOnClickListener(v -> SignalSender.SendShortcuts(ipAddress,port,240));
        lock.setOnClickListener(v -> SignalSender.SendShortcuts(ipAddress,port,360));
    }
}