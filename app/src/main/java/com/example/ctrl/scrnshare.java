package com.example.ctrl;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class scrnshare extends AppCompatActivity {
    String ipAddress;
    int port;
    private static final String TAG = "ScreenCaptureClient";
    private ImageView imageView;
    private Bitmap bitmap;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            imageView.setImageBitmap(bitmap);
            return true;
        }
    });
    private Socket socket;
    private List<Byte> buffer = new ArrayList<>();
    private int expectedLength = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrnshare);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toolbar toolbar = findViewById(R.id.tlbr);
        toolbar.setTitle(R.string.activity_scnshare);
        imageView = findViewById(R.id.imageView);
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        ipAddress = preferences.getString("ipAddress", "");
        port = preferences.getInt("port", 0);

        new Thread(new ClientThread()).start();
    }

    class ClientThread implements Runnable {

        @Override
        public void run() {
            try {
                socket = new Socket(ipAddress, 9000);
                while (true) {
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    byte[] bytes = (byte[]) objectInputStream.readObject();
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    //Rotate bitmap
                    //Matrix matrix = new Matrix();
                    //matrix.postRotate(90);
                    //bitmap = Bitmap.createBitmap(bitmap, 0 , 0,bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    Message message = handler.obtainMessage();
                    handler.sendMessage(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                Log.e(TAG, "Error :" + e.getMessage());
            }
        }
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
        try {
            if(socket != null) {
                socket.close();
            }
        }catch (IOException e){
            Log.e(TAG, "Error closing socket: " + e.getMessage());
        }
    }
}