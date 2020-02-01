package com.example.bluetoothapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.bluetoothapp.DataModel.ContactDetail;

public class ContactInfo extends AppCompatActivity {

    private TextView mFirstName;
    private TextView mLastName;
    private TextView mDeviceName;
    private TextView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        mImageView = findViewById(R.id.contact_image);
        mFirstName = findViewById(R.id.contact_first_name_value);
        mLastName =  findViewById(R.id.contact_last_name_value);
        mDeviceName = findViewById(R.id.contact_device_name_value);
        setTitle("Contact Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getBundleExtra("contact");
            ContactDetail detail = (ContactDetail) bundle.getSerializable("contact_details");
            displayContact(detail);
        }
    }
    public void displayContact(ContactDetail detail){
        mDeviceName.setText(detail.getDeviceName());
        mFirstName.setText(detail.getFirstName());
        mLastName.setText(detail.getLastName());
        mImageView.setText(detail.getFirstName().substring(0,1).toUpperCase());

    }
}
