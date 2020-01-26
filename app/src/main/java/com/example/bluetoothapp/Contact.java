package com.example.bluetoothapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Contact extends AppCompatActivity {


    private int month,day,year;
    private Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
      calendar = Calendar.getInstance();
       month = calendar.get(Calendar.MONTH);
       day = calendar.get(Calendar.DAY_OF_MONTH);
       year = calendar.get(Calendar.YEAR);

        Intent intent = getIntent();
        if(intent != null){
            String deviceName = intent.getStringExtra("device_name");
            setTitle(deviceName);
        }
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
        Toast.makeText(this,""+year+"-"+month+"-"+day,Toast.LENGTH_LONG).show();
    }
}
