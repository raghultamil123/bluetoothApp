package com.example.bluetoothapp.Service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.bluetoothapp.ChatActivity;
import com.example.bluetoothapp.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

/*
 *@author rag
 *@project BluetoothApp
 */public class ChatService {

    ConnectThread connectThread;
    AcceptThread acceptThread;
    ConnectedThread connectedThread;
    String deviceInsecure = "insecure";
    BluetoothAdapter bluetoothAdapter;
    private final Handler handler;
    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;
    private static final UUID MY_UUID_INSECURE = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static int state;

    public ChatService (Context context, Handler handler){
        bluetoothAdapter = MainActivity.bluetoothAdapter;
        this.handler = handler;
        this.state = STATE_NONE;
    }
    private synchronized void setState(int state){
        this.state = state;
        handler.obtainMessage(ChatActivity.MESSAGE_STATE_CHANGE,state,-1).sendToTarget();
    }
    public static int getState(){
        return state;
    }

    class ConnectThread extends Thread{
        private final BluetoothSocket socket;
        private final BluetoothDevice device;
        BluetoothSocket fallbackSocket;
        private String socketType;
        public ConnectThread(BluetoothDevice device){
            this.device = device;
            BluetoothSocket temp =null;
            try {
                temp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID_INSECURE);
                System.out.println("mass "+device.createInsecureRfcommSocketToServiceRecord(MY_UUID_INSECURE));
            }catch (IOException exception){
                exception.printStackTrace();
            }
            socket = temp;
            System.out.println("raghul");
        }
        public void run(){
            try {
                MainActivity.bluetoothAdapter.cancelDiscovery();
                socket.connect();
                // Toast.makeText(ChatActivity.this,"connected with "+device.getName(),Toast.LENGTH_LONG).show();
            }catch (IOException exception){


                exception.printStackTrace();
                try {
                    socket.close();
                    System.out.println("inside the socket close");

                }catch(IOException ex){

                    System.out.println("inside the exception");

                }
                ChatService.this.start();
                return;
            }
            //
            synchronized (ChatService.this) {
                connectThread = null;
            }
            connected(socket,device);


        }
        public void close(){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    class AcceptThread extends Thread{
        private final BluetoothServerSocket serverSocket;
        public AcceptThread(){
            BluetoothServerSocket temp = null;
            try {
                temp = MainActivity.bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(
                        deviceInsecure, MY_UUID_INSECURE);
            }catch (IOException exception){


            }
            serverSocket = temp;
        }
        public void run(){
            System.out.println("ted");
            BluetoothSocket socket = null;
            while(state != STATE_CONNECTED){
                try {
                    socket = serverSocket.accept();
                    System.out.println("loop");
                }catch (IOException e){

                    System.out.println("exception" +e);
                    break;


                }
                if(socket!=null){
                    synchronized (ChatService.this) {
                        switch (state) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                connected(socket, socket.getRemoteDevice());
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                try {
                                    socket.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;

                        }
                    }


                    System.out.println("running start");
                    // Toast.makeText(ChatActivity.this,socket.getRemoteDevice().getName(),Toast.LENGTH_LONG).show();
                }

            }

        }
        public void close(){
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class ConnectedThread extends Thread{
        private  final BluetoothSocket socket;
        private  final InputStream inputStream;
        private final OutputStream outputStream;
        public ConnectedThread(BluetoothSocket socket){
            this.socket = socket;
            InputStream temp = null;
            OutputStream temp2 = null;
            try{
                temp = socket.getInputStream();
                temp2 = socket.getOutputStream();
            }catch (Exception e){
                System.out.println("Exception in getting the i/o stream");
            }
            inputStream = temp;
            outputStream = temp2;

        }
        public void run(){
            byte[] buf = new byte[1024];
            int bytes;
            while(true){
                try {
                    bytes= inputStream.read(buf);
                    handler.obtainMessage(ChatActivity.MESSAGE_READ, bytes, -1,
                            buf).sendToTarget();
                    System.out.println("message received "+new String(buf));
                }catch(Exception e){
                    e.printStackTrace();
                    ChatService.this.start();
                    ChatService.this.start();

                    System.out.println("Excception caught while getting the input Stream");
                    break;

                }
            }

        }
        public void write(byte[] buff){
            try {
                outputStream.write(buff);
                handler.obtainMessage(ChatActivity.MESSAGE_WRITE, -1, -1,
                        buff).sendToTarget();
            }catch (Exception e){
                System.out.println("Exception in writing the value");
            }
        }
        public void cancel(){
            try {
                socket.close();
            }catch(Exception e){
                System.out.println("Exception in closing the socket");
            }
        }
    }
    public synchronized void start(){
         if(connectThread !=null){
            connectThread.close();
            connectThread = null;
        }
         System.out.println("raghul start");
        if(connectedThread !=null){
            connectedThread.cancel();
            connectedThread = null;
        }
        setState(STATE_LISTEN);
        if(acceptThread==null) {
            System.out.println("inside run of rafhul");
            acceptThread = new AcceptThread();
            acceptThread.start();
        }

    }
    public  synchronized void connect(BluetoothDevice device){

        if(state == STATE_CONNECTING) {
            if(connectThread != null){
                connectThread.close();
                connectThread = null;
            }
        }

            if (connectedThread != null) {
                connectedThread.cancel();
                connectedThread = null;
            }


        connectThread = new ConnectThread(device);
        connectThread.start();
        setState(STATE_CONNECTING);
    }
    public  synchronized void connected(BluetoothSocket socket,BluetoothDevice device){
        if(connectThread!=null){
            connectThread.close();
            connectThread = null;
        }
        if(connectedThread!=null){
            connectThread.close();
            connectThread = null;

        }
        if(acceptThread!=null){
            acceptThread.close();
            acceptThread = null;
        }
        connectedThread = new ConnectedThread(socket);
        connectedThread.start();

        Message msg = handler.obtainMessage(ChatActivity.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(ChatActivity.DEVICE_NAME,device.getName());
        msg.setData(bundle);
        handler.sendMessage(msg);
        setState(STATE_CONNECTED);

    }
     public void write(byte[] buff){
        ConnectedThread th=null;
        th = connectedThread;
        if(th!=null){
          th.write(buff);
        }
    }
    public synchronized void stop(){
        if(connectThread!=null){
            connectThread.close();
            connectThread = null;
        }
        if(connectedThread!=null){
            connectThread.close();
            connectThread = null;

        }
        if(acceptThread!=null){
            acceptThread.close();
            acceptThread = null;
        }
        setState(STATE_NONE);
    }

}
