package com.example.bluetoothapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bluetoothapp.DataModel.ChatMessage;
import com.example.bluetoothapp.R;

import java.util.ArrayList;
import java.util.List;

/*
 *@author rag
 *@project BluetoothApp
 */public class ChatAdapter extends ArrayAdapter<ChatMessage> {

     List<ChatMessage> messages = new ArrayList<>();
     Context context;

     public ChatAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context = context;
    }

    @Override
    public void add(@Nullable ChatMessage object) {
        super.add(object);
        messages.add(object);
    }
    public ChatMessage getItem(int index){
         return this.messages.get(index);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
         ChatMessage message = getItem(position);
        View view = convertView;
        if(!message.isLeft()){
            view = LayoutInflater.from(context).inflate(R.layout.message_receive,parent,false);
        }
        else{
            view = LayoutInflater.from(context).inflate(R.layout.message_send,parent,false);
        }
        TextView textView = view.findViewById(R.id.message_text);
        textView.setText(message.getMessage());
        return view;
    }
}
