package com.example.ctrl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Menu extends AppCompatActivity {
    String ipAddress;
    int port;
    ImageButton img,img1,img2,img3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView ipfld = findViewById(R.id.ipaddressfld);
        TextView portfld = findViewById(R.id.portfld);
        toolbar.setTitle(R.string.activity_menu);

        // Retrieve the IP address and port from shared preferences
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        ipAddress = preferences.getString("ipAddress", "");
        port = preferences.getInt("port", 0);
        ipfld.setText(ipAddress);
        portfld.setText(port+"");

        img = findViewById(R.id.scrnshare);
        img1 = findViewById(R.id.mouse);
        img2 = findViewById(R.id.presentation);
        img3 = findViewById(R.id.shortcuts);
        img.setOnClickListener(v -> {
            Intent i = new Intent(Menu.this, scrnshare.class);
            startActivity(i);
        });
        img1.setOnClickListener(v -> {
            Intent i = new Intent(Menu.this, MouseUI.class);
            startActivity(i);
        });
        img2.setOnClickListener(v -> {
            Intent i = new Intent(Menu.this, Presentation.class);
            startActivity(i);
        });
        img3.setOnClickListener(v -> {
            Intent i = new Intent(Menu.this, shortcuts.class);
            startActivity(i);
        });
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        // Inflate the custom layout for the dialog
        View dialogView = inflater.inflate(R.layout.pop_up, null);
        builder.setView(dialogView);

        // Get references to the views in the custom layout
        TextView titleTextView = dialogView.findViewById(R.id.dialog_title);
        TextView messageTextView = dialogView.findViewById(R.id.dialog_message);
        Button yesButton = dialogView.findViewById(R.id.dialog_yes_button);
        Button noButton = dialogView.findViewById(R.id.dialog_no_button);

        // Set the text for the title and message
        titleTextView.setText("CTRL+");
        messageTextView.setText("Are you sure you want to leave the menu? This will reset the connection.");

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Set the listeners for the buttons
        yesButton.setOnClickListener(v -> Menu.super.onBackPressed());
        noButton.setOnClickListener(v -> dialog.dismiss());

    }


}