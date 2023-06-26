package com.example.ctrl;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Messagesender extends AsyncTask<byte[],Void,Void> {
    private Socket socket;
    private String host;
    private  int port;

    public Messagesender(String host, int port) {
        this.host = host;
        this.port = port;
        //this.socket = null;
    }

    @Override
    protected Void doInBackground(byte[]... bytes) {
        //Socket socket = null;
        try{
            if(socket == null) {
                socket = new Socket(host, port);
            }
            OutputStream outputStream = socket.getOutputStream();

            outputStream.write(bytes[0]);
            outputStream.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public void closeConnection() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}