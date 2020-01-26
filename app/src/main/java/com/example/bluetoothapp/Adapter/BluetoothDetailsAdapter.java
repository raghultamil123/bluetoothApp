package com.example.bluetoothapp.Adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.bluetoothapp.Contact;
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
        final BluetoothDevice currentDevice = deviceList.get(position);
        TextView nameView = contentView.findViewById(R.id.deviceName);
        nameView.setText(currentDevice.getName());
        TextView macView = contentView.findViewById(R.id.deviceAddress);
        macView.setText(currentDevice.getAddress());
        ImageView addButton = contentView.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getContext(),"click",Toast.LENGTH_LONG).show();
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setMessage(getContext().getString(R.string.save_message));
                dialog.setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Context context = getContext();
                        Class targetClass = Contact.class;
                        Intent intent = new Intent(context,targetClass);
                        intent.putExtra("device_name",currentDevice.getName());
                        Toast.makeText(getContext(),"ok is selected",Toast.LENGTH_LONG).show();
                        getContext().startActivity(intent);
                    }
                });
                dialog.create().show();
            }

        });
        return contentView;


    }
}
