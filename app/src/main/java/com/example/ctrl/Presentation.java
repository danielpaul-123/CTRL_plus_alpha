package com.example.ctrl;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

public class Presentation extends AppCompatActivity {
    private long mTimeStartInMillis;

    private TextView mTextViewCountDown;
    private ImageButton mButtonReset;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;

    Button start;
    Button left;
    Button right;
    SharedPreferences preferences;
    String ipAddress;
    int port;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        start = findViewById(R.id.start);
        left = findViewById(R.id.left_arrow);
        right = findViewById(R.id.right_arrow);
        preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        ipAddress = preferences.getString("ipAddress", "");
        port =  preferences.getInt("port", 0);

        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mButtonReset = findViewById(R.id.button_reset);

        mTextViewCountDown.setOnClickListener(v -> {
            // Inflate the dialog layout
            View dialogView = getLayoutInflater().inflate(R.layout.set_time, null);

            // Find the EditText view in the dialog layout
            EditText timeEditText = dialogView.findViewById(R.id.edit_text_time);
            Button button_set = dialogView.findViewById(R.id.button_set);
            Button button_cancel = dialogView.findViewById(R.id.button_cancel);
            // Create the dialog and set its content view
            AlertDialog.Builder builder = new AlertDialog.Builder(Presentation.this);
            builder.setView(dialogView);

            AlertDialog dialog = builder.create();
            dialog.show();
            // Add "OK" button to the dialog
            button_set.setOnClickListener(v12 -> {
                // Get the time entered by the user
                String timeString = timeEditText.getText().toString().trim();
                if(timeString == null){
                    timeEditText.setError("Cannot be Empty");
                }else {
                    int timeInSeconds = Integer.parseInt(timeString) * 60;

                    // Set the timer duration
                    mTimeStartInMillis = timeInSeconds * 1000L;
                    resetTimer();

                    dialog.cancel();
                }
            });

            // Add "Cancel" button to the dialog
            button_cancel.setOnClickListener(v1 -> dialog.cancel());
        });

        Toolbar toolbar = findViewById(R.id.toolbar3);
        toolbar.setTitle(R.string.activity_presentation);

        mButtonReset.setOnClickListener(v -> resetTimer());
        updateCountDownText();

        start.setOnClickListener(v -> SignalSender.sendKeycode(ipAddress, port,116));

        left.setOnClickListener(v -> SignalSender.sendKeycode(ipAddress, port,37));

        right.setOnClickListener(v -> SignalSender.sendKeycode(ipAddress, port,39));
    }
    @SuppressLint("SetTextI18n")
    private void startTimer(){
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonReset.setVisibility(View.VISIBLE);
            }
        }.start();
        mTimerRunning = true;
        start.setText("exit");
        mButtonReset.setVisibility(View.INVISIBLE);
    }
    @SuppressLint("SetTextI18n")
    private void pauseTimer(){
        mCountDownTimer.cancel();
        mTimerRunning = false;
        start.setText("start");
        mButtonReset.setVisibility(View.VISIBLE);
    }
    private void resetTimer(){

        mTimeLeftInMillis = mTimeStartInMillis;
        Log.d("Presentation", "Time entered by user: " + mTimeLeftInMillis);

        updateCountDownText();
        mButtonReset.setVisibility(View.INVISIBLE);
    }
    private void updateCountDownText(){
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }
}
