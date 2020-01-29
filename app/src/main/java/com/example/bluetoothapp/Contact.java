package com.example.bluetoothapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import com.example.bluetoothapp.Data.Contact.ContactsContract;
import com.example.bluetoothapp.Data.Contact.ContactsContract.BluetoothDetailsEntry;
import com.example.bluetoothapp.Data.Contact.ContactsContract.UserDetailsEntry;

public class Contact extends AppCompatActivity {

    private TextView mDeviceText;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText dateOfBirth;

    private int month,day,year;
    private Calendar calendar;
    BluetoothDevice bluetoothDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
      calendar = Calendar.getInstance();
       month = calendar.get(Calendar.MONTH);
       day = calendar.get(Calendar.DAY_OF_MONTH);
       year = calendar.get(Calendar.YEAR);
       setTitle("Contact");
        initialization();
        Intent intent = getIntent();
        if(intent != null){
           Bundle bundle = intent.getBundleExtra("device_det");
            bluetoothDevice = bundle.getParcelable("device");
         String deviceName=  bluetoothDevice.getName();
         mDeviceText.setText(deviceName);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contact_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.submit){
            if(validationResult()) {
                Toast.makeText(this, "Submit button is presses", Toast.LENGTH_LONG).show();
                saveContact();
            }
            else
                Toast.makeText(this,"need to fill all the values",Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean validationResult(){
        if(mFirstName.length() > 0 && mLastName.length() > 0 && dateOfBirth.length() > 0){
            return true;
        }
        return false;
    }
    public void saveContact(){
        String firstName=mFirstName.getText().toString();
        String lastName=mLastName.getText().toString();
        String date = dateOfBirth.getText().toString();
        String deviceName = mDeviceText.getText().toString();
        ContentValues values = new ContentValues();
        values.put(BluetoothDetailsEntry.DEV_MAC_ADDR,bluetoothDevice.getAddress());
        values.put(BluetoothDetailsEntry.DEV_NAME,bluetoothDevice.getName());
        values.put(UserDetailsEntry.FIRST_NAME,firstName);
        values.put(UserDetailsEntry.LAST_NAME,lastName);
        values.put(UserDetailsEntry.DATE_OF_BIRTH,date);
        Uri uri  = getContentResolver().insert(ContactsContract.CONTENT_URI,values);
        Toast.makeText(this,uri.getLastPathSegment(),Toast.LENGTH_LONG).show();




    }

    public void initialization(){
        mDeviceText = findViewById(R.id.device_name);
        mFirstName = findViewById(R.id.first_name);
        mLastName = findViewById(R.id.last_name);
        dateOfBirth = findViewById(R.id.date_of_birth);
    }
    public void setDate(View view){
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id==999){
            return new DatePickerDialog(this,myDateListener,year,month,day);
        }
        return super.onCreateDialog(id);
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            showDate(year,month+1,dayOfMonth);
        }
    };
    public void showDate(int year,int month,int date){
        Toast.makeText(this,""+year+"-"+month+"-"+date,Toast.LENGTH_LONG).show();
        String selectedDate = year+"-"+month+"-"+date;
        dateOfBirth.setText(selectedDate);

    }
}
