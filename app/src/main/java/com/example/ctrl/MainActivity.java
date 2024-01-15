package com.example.ctrl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {
    ImageButton btn;
    String ip_address;
    int pt;
    EditText ipfield,portfield;
    Button connectbutton;
    FloatingActionButton fab_main,fab_one,fab_two;
    Float translationYaxis = 100f;
    boolean menuOpen = false;
    OvershootInterpolator interpolator = new OvershootInterpolator();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.connect);
        ipfield = findViewById(R.id.ipfld);
        portfield = findViewById(R.id.portfld);
        connectbutton = findViewById(R.id.connectbtn);
        Toolbar toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitle(R.string.activity_main);
        ShowMenu();
        btn.setOnClickListener(v -> scanCode());
        connectbutton.setOnClickListener(v -> {
            try{
                ip_address = ipfield.getText().toString().trim();
                pt = Integer.parseInt(portfield.getText().toString());
            }catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),"Enter IP Address and Port Address",Toast.LENGTH_LONG).show();
                ipfield.setError("Fields cannot be Empty");
                portfield.setError("Fields cannot be Empty");
            }
            if(pt == 0 || ip_address.isEmpty()){
                ipfield.setError("Fields cannot be Empty");
                portfield.setError("Fields cannot be Empty");
            }else{
                SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("ipAddress", ip_address);
                editor.putInt("port", pt);
                editor.apply();

                Intent i = new Intent(MainActivity.this, Menu.class);
                startActivity(i);
            }
        });
    }

    private void ShowMenu() {
        fab_main = findViewById(R.id.fab_main);
        fab_one = findViewById(R.id.fab_one);
        fab_two = findViewById(R.id.fab_two);

        fab_one.setAlpha(0f);
        fab_two.setAlpha(0f);

        fab_one.setTranslationY(translationYaxis);
        fab_two.setTranslationY(translationYaxis);

        fab_main.setOnClickListener(v -> {
            if(menuOpen){
                CloseMenu();
            }else{
                OpenMenu();
            }
        });

        fab_one.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse("https://github.com/Pr0-T0/CTRL_PLUS"));
            startActivity(i);
        });
        fab_two.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Make sure Both Devices are connected to the same Network before scanning", Toast.LENGTH_LONG).show());
    }

    private void OpenMenu() {
        menuOpen = !menuOpen;
        fab_main.setImageResource(R.drawable.ic_baseline_menu_open_24);
        fab_one.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_two.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void CloseMenu() {
        menuOpen = !menuOpen;
        fab_main.setImageResource(R.drawable.ic_baseline_menu_open_24);
        fab_one.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_two.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to turn on flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(),result -> {
        String pattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?):([1-9]|[1-9][0-9]{1,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";

        if (result.getContents()==null) {
            LayoutInflater inflater = getLayoutInflater();

            // Inflate the custom layout for the dialog
            View dialogView = inflater.inflate(R.layout.error_popup, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setView(dialogView);

            // Get references to the views in the custom layout
            TextView titleTextView = dialogView.findViewById(R.id.dialog_title);
            TextView messageTextView = dialogView.findViewById(R.id.dialog_message);
            Button yesButton = dialogView.findViewById(R.id.dialog_yes_button);

            // Set the text for the title and message
            titleTextView.setText(R.string.error);
            messageTextView.setText(R.string.please_scan_a_valid_qr_code_to_establish_conection);
            // Create and show the dialog
            AlertDialog dialog = builder.create();
            dialog.show();
            // Set the listener for the button
            yesButton.setText(R.string.ok);
            yesButton.setOnClickListener(v -> dialog.dismiss());
        }
        else if(result.getContents().matches(pattern) ) {
            String[] parts = result.getContents().split(":");
            String ipAddress = parts[0];
            int port = Integer.parseInt(parts[1]);

            //save ip address and port in shared preferences
            SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("ipAddress", ipAddress);
            editor.putInt("port", port);
            editor.apply();

            //menu Intent
            Intent i = new Intent(MainActivity.this, Menu.class);
            startActivity(i);
        }
        else{

            LayoutInflater inflater = getLayoutInflater();

            // Inflate the custom layout for the dialog
            View dialogView = inflater.inflate(R.layout.error_popup, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setView(dialogView);

            // Get references to the views in the custom layout
            TextView titleTextView = dialogView.findViewById(R.id.dialog_title);
            TextView messageTextView = dialogView.findViewById(R.id.dialog_message);
            Button yesButton = dialogView.findViewById(R.id.dialog_yes_button);

            // Set the text for the title and message
            titleTextView.setText("Error");
            messageTextView.setText("Please scan a valid QR code to establish conection.");
            // Create and show the dialog
            AlertDialog dialog = builder.create();
            dialog.show();
            // Set the listener for the button
            yesButton.setText("OK");
            yesButton.setOnClickListener(v -> dialog.dismiss());
        }

    });
}