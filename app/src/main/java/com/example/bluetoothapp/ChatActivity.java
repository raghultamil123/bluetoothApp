package com.example.bluetoothapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bluetoothapp.Adapter.ChatAdapter;
import com.example.bluetoothapp.DataModel.ChatMessage;
import com.example.bluetoothapp.Service.ChatService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {
   String deviceName;
   String deviceAddress;
   public static final int MESSAGE_STATE_CHANGE = 1;
   public static final int MESSAGE_DEVICE_NAME = 4;
   public static final int MESSAGE_WRITE = 3;
   public static final int MESSAGE_READ = 2;
   public static final int REQUEST_ENABLE_BT  = 300;
   public static final String DEVICE_NAME = "deviceName";
   BluetoothAdapter bluetoothAdapter;
   ChatService chatService;
    ChatAdapter adapter;
   String status="waiting for connection";
    EditText messageText;
    Button sendButton;
    public String writeMessage;
    public String readMessage;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
           switch(msg.what){
               case MESSAGE_STATE_CHANGE:
                   switch (msg.arg1) {
                       case (ChatService.STATE_CONNECTED):
                           setStatus(getString(R.string.device_connected));
                           break;
                       case (ChatService.STATE_CONNECTING):
                           setStatus(getString(R.string.device_connecting));
                           break;
                       case (ChatService.STATE_LISTEN):
                       case (ChatService.STATE_NONE):
                             setStatus(getString(R.string.device_not_connected));
                             break;
                   }
                   break;
               case MESSAGE_WRITE:
                   byte[] writeBuff = (byte[])msg.obj;
                   writeMessage = new String(writeBuff);
                   System.out.println("write message "+writeMessage);
                   adapter.add(new ChatMessage(false,writeMessage));
                   messageText.setText("");

                   break;
               case MESSAGE_READ:
                   byte[] readBuff = (byte[])msg.obj;
                   readMessage = new String(readBuff,0,msg.arg1);
                   System.out.println("read message "+readMessage);
                   Toast.makeText(ChatActivity.this,"read "+readMessage,Toast.LENGTH_LONG).show();
                   adapter.add(new ChatMessage(true,readMessage));
                   break;
               case MESSAGE_DEVICE_NAME:
                   Toast.makeText(ChatActivity.this,"Connected to "+msg.getData().getString(DEVICE_NAME),Toast.LENGTH_LONG).show();
                   break;

           }
           return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        System.out.println("inside create");
        Intent availableIntent = getIntent();
        bluetoothAdapter = MainActivity.bluetoothAdapter;
        setupChat();
        Bundle device = availableIntent.getBundleExtra("bluetooth");
        BluetoothDevice bluetoothDevice = device.getParcelable("deviceDetail");
        chatService.start();
        connectDevice(bluetoothDevice);
        getSupportActionBar().setTitle(bluetoothDevice.getName());
        messageText = findViewById(R.id.message);
        sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String message =  messageText.getText().toString();
               System.out.println("message sent "+message);
               sendMessage(message);
            }
        });
        ListView listView = findViewById(R.id.chat_area);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        adapter = new ChatAdapter(this,R.layout.message_send);
        listView.setAdapter(adapter);

    }

    private void setStatus(String value){
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle(value);
    }
    private void connectDevice(BluetoothDevice device) {

        chatService.connect(device);
    }
    private void sendMessage(String message) {
        if (chatService.getState() != ChatService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.device_not_connected, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (message.length() > 0) {
            byte[] send = message.getBytes();
            chatService.write(send);
        }

    }

    private void setupChat(){
        chatService = new ChatService(this,handler);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        else {
            if(chatService == null){
                setupChat();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (chatService != null) {
            if (chatService.getState() == ChatService.STATE_NONE) {
                chatService.start();
            }
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatService != null)
            chatService.stop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(chatService !=null){
            chatService.stop();
        }
    }
}
