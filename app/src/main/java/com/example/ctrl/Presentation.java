package com.example.ctrl;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Presentation extends AppCompatActivity {
    private long mTimeStartInMillis;
    private TextView mTextViewCountDown;
    private CountDownTimer countDownTimer;
    Button start;
    ImageButton left;
    ImageButton right;
    SharedPreferences preferences;
    String ipAddress;
    int port;
    int timeString = 0;


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
        ImageButton mButtonReset = findViewById(R.id.button_reset);

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
                timeString = Integer.parseInt(timeEditText.getText().toString().trim());
                if(timeString == 0){
                    timeEditText.setError("Cannot be Empty");
                }else {
                    // Set the timer duration
                    mTimeStartInMillis = timeString * 60000L;
                    dialog.cancel();
                }
            });

            // Add "Cancel" button to the dialog
            button_cancel.setOnClickListener(v1 -> dialog.cancel());
        });

        Toolbar toolbar = findViewById(R.id.toolbar3);
        toolbar.setTitle(R.string.activity_presentation);

        mButtonReset.setOnClickListener(v -> {
            countDownTimer.cancel();
            resetTimer();
        });

        start.setOnClickListener(v -> {
            if (countDownTimer == null)
            {
                if (timeString !=0)
                {
                    startCountdownTimer(mTimeStartInMillis);
                }
                SignalSender.sendKeycode(ipAddress, port, 116);
                start.setText(R.string.stop);
            }
            else
            {
                if (countDownTimer != null)
                {
                    countDownTimer.cancel();
                    countDownTimer = null;
                    resetTimer();
                }
                SignalSender.sendKeycode(ipAddress, port, 27);
                start.setText(R.string.start);
            }
        });

        left.setOnClickListener(v -> SignalSender.sendKeycode(ipAddress, port,37));

        right.setOnClickListener(v -> SignalSender.sendKeycode(ipAddress, port,39));
    }
    private void startCountdownTimer(long milliseconds) {
        // Update the TextView with the remaining time
        // Handle the countdown completion
        countDownTimer = new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update the TextView with the remaining time
                long seconds = (millisUntilFinished / 1000)%60;
                long minutes = millisUntilFinished / 60000;

                String timeLeft = minutes + ":" + seconds;

                mTextViewCountDown.setText(timeLeft);
            }

            @Override
            public void onFinish() {
                // Handle the countdown completion
                mTextViewCountDown.setText(R.string._00_00);
            }
        };
        countDownTimer.start();
    }
    private  void resetTimer()
    {
        mTextViewCountDown.setText(R.string._00_00);
    }

}
