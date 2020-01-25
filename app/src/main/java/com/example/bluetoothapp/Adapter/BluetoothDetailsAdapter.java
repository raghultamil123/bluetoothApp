package com.example.bluetoothapp.Adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bluetoothapp.R;

import java.util.List;

/*
 *@author rag
 *@project BluetoothApp
 */public class BluetoothDetailsAdapter extends ArrayAdapter {
     List<BluetoothDevice> deviceList;
    public BluetoothDetailsAdapter(@NonNull Context context, @NonNull List<BluetoothDevice> objects) {
        super(context, 0, objects);
        deviceList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View contentView = convertView;
        if(contentView == null){
            contentView = LayoutInflater.from(getContext()).inflate(R.layout.device_details,parent,false);

        }
        BluetoothDevice currentDevice = deviceList.get(position);

        TextView nameView = contentView.findViewById(R.id.deviceName);
        nameView.setText(currentDevice.getName());
        TextView macView = contentView.findViewById(R.id.deviceAddress);
        macView.setText(currentDevice.getAddress());
        return contentView;


    }
}
