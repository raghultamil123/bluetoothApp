package com.example.bluetoothapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bluetoothapp.DataModel.ContactDetail;
import com.example.bluetoothapp.R;

import java.util.ArrayList;
import java.util.List;

/*
 *@author rag
 *@project BluetoothApp
 */public class ContactsAdapter extends ArrayAdapter<ContactDetail> {
     List<ContactDetail> details = new ArrayList<>();
    public ContactsAdapter(@NonNull Context context,@NonNull List<ContactDetail> objects) {
        super(context, 0, objects);
        this.details = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.contacts,parent,false);
        }
        ContactDetail currentContactDetail = details.get(position);
        TextView letterView = view.findViewById(R.id.contact_icon);
        letterView.setText(currentContactDetail.getFirstName().substring(0,1).toUpperCase());
        TextView nameView = view.findViewById(R.id.contact_name);
        TextView deviceView = view.findViewById(R.id.contact_device);
        nameView.setText(currentContactDetail.getFirstName());
        deviceView.setText(currentContactDetail.getDeviceName());

        return view;
    }
}
