package com.example.bluetoothapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import com.example.bluetoothapp.Adapter.ContactsAdapter;
import com.example.bluetoothapp.Data.Contact.ContactsContract;
import com.example.bluetoothapp.Data.Contact.ContactsDataHelper;
import com.example.bluetoothapp.DataModel.ContactDetail;

import java.util.ArrayList;
import java.util.List;

public class ContactDetails extends AppCompatActivity {
    ContactsDataHelper contactsDataHelper ;
    List<ContactDetail> details;
    ContactsAdapter adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);


        contactsDataHelper = new ContactsDataHelper(this);
        details = new ArrayList<>();
        getDetails();
        adapter= new ContactsAdapter(this,details);
        ListView view = findViewById(R.id.device_list);

        view.setAdapter(adapter);

    }
    public void getDetails(){

        SQLiteDatabase database = contactsDataHelper.getReadableDatabase();
        String columns[]=new String[]{ContactsContract.BluetoothDetailsEntry.DEV_NAME, ContactsContract.BluetoothDetailsEntry.DEV_MAC_ADDR};
        //database.query(ContactsContract.BluetoothDetailsEntry.TABLE_NAME,columns,null,null,null,null,null);
        String query = "select * from bluetooth_details bd inner join user_details ud on ud._id = bd.user_details_id ";
        Cursor cursor = database.rawQuery(query,null);
        System.out.println("raghul"+cursor.getCount());
        while(cursor.moveToNext()){
            ContactDetail detail = new ContactDetail();
            detail.setDeviceName(cursor.getString(2));
            detail.setDeviceAddress(cursor.getString(1));
            detail.setFirstName(cursor.getString(5));
            detail.setLastName(cursor.getString(6));
            detail.setDateOfBirth(cursor.getString(8));
            details.add(detail);

        }
        System.out.println("testing the size od contact"+ details.get(0).getDeviceAddress());
    }
}
