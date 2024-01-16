package com.example.ctrl;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MouseUI extends AppCompatActivity {
    TextView textView;
    Button leftButton;
    Button rightButton;
    String ipAddress;
    int port;
    private int lastX, lastY; // To track the last X and Y position of the finger

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse_ui);
        textView = findViewById(R.id.TouchView);
        leftButton = findViewById(R.id.leftclick);
        rightButton = findViewById(R.id.rightclick);
        Toolbar toolbar = findViewById(R.id.toolbar12);
        toolbar.setTitle(R.string.ctrl_mouse_controls);

        // Retrieve the IP address and port from shared preferences
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        ipAddress = preferences.getString("ipAddress", "");
        port =  preferences.getInt("port", 0);

//        textView.setOnClickListener(view -> SignalSender.sendMouseData(ipAddress,port,0,0,true,false));
        // Set an OnTouchListener on the textView to handle finger movements
        textView.setOnTouchListener((view, event) -> {
            int action = event.getActionMasked(); // Get the action of the event
            switch (action) {
                case MotionEvent.ACTION_DOWN: // Finger touched the screen
                    lastX = (int) event.getX(); // Set the last X position
                    lastY = (int) event.getY(); // Set the last Y position
                break;

                case MotionEvent.ACTION_MOVE: // Finger moved on the screen
                    int x = (int) event.getX(); // Get the new X position
                    int y = (int) event.getY(); // Get the new Y position
                    int deltaX = x - lastX; // Get the change in X position
                    int deltaY = y - lastY; // Get the change in Y position
                    lastX = x; // Update the last X position
                    lastY = y; // Update the last Y position
                    SignalSender.sendMouseData(ipAddress,port,deltaX, deltaY, false, false); // Send the mouse data to the computer
                    break;
            }
            return true;
        });

        leftButton.setOnClickListener(v -> SignalSender.sendMouseData(ipAddress,port,0,0,true,false));
        rightButton.setOnClickListener(v -> SignalSender.sendMouseData(ipAddress,port,0,0,false,true));
    }
    /*@Override
    protected void onDestroy() {

        super.onDestroy();
        SignalSender.sendMouseData(ipAddress,port,0,0,true,true);
    }*/
}