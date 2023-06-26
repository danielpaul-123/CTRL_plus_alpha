package com.example.ctrl;

import java.nio.ByteBuffer;

public class SignalSender {
    private static String ipAddress;
    private static int port;
    public static void sendKeycode(String ip,int prt,int code){
        ipAddress = ip;
        Messagesender task = new Messagesender(ipAddress,prt);
        byte[] dta = createkeycode(code);
        task.execute(dta);
    }
    public static void sendMouseData(String ip,int prt,int x, int y, boolean leftButton, boolean rightButton) {
        ipAddress = ip;
        port = prt;
        Messagesender task = new Messagesender(ipAddress,port);
        byte[] data = createMouseData(x, y, leftButton, rightButton);
        task.execute(data);
    }
    public static void SendShortcuts(String ip,int port,int sig){
        Messagesender task = new Messagesender(ip,port);
        byte[] data = SendSignal(sig);
        task.execute(data);
    }

    private static byte[] createMouseData(int x, int y, boolean leftButton, boolean rightButton) {
        ByteBuffer buffer = ByteBuffer.allocate(14);
        buffer.put((byte) 1); // add the identifier bit
        buffer.putInt(x);
        buffer.putInt(y);
        buffer.put(leftButton ? (byte) 1 : (byte) 0);
        buffer.put(rightButton ? (byte) 1 : (byte) 0);
        return buffer.array();
    }
    private static byte[] createkeycode(int code){
        ByteBuffer buff = ByteBuffer.allocate(14);
        buff.put((byte) 2);
        buff.putInt(code);
        return buff.array();
    }
    private static byte[] SendSignal(int sig){
        ByteBuffer bff = ByteBuffer.allocate(14);
        bff.put((byte) 3);
        bff.putInt(sig);
        return bff.array();
    }
}
