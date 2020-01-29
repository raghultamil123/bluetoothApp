package com.example.bluetoothapp.Data.Contact;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.bluetoothapp.Data.Contact.ContactsContract.BluetoothDetailsEntry;
import com.example.bluetoothapp.Data.Contact.ContactsContract.UserDetailsEntry;
import androidx.annotation.Nullable;

/*
 *@author rag
 *@project BluetoothApp
 */public class ContactsDataHelper extends SQLiteOpenHelper {
     public static final Integer VERSION = 1;
     public static final String DATABASE="bluetooth.db";
    public ContactsDataHelper(@Nullable Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       StringBuilder createBluetoothDetails = new StringBuilder();
       StringBuilder createUserDetails = new StringBuilder();
       createBluetoothDetails.append("CREATE TABLE ").append(BluetoothDetailsEntry.TABLE_NAME).append("(")
               .append(BluetoothDetailsEntry.ID).append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,")
               .append(BluetoothDetailsEntry.DEV_MAC_ADDR).append(" TEXT NOT NULL,")
               .append(BluetoothDetailsEntry.DEV_NAME).append(" TEXT NOT NULL,")
               .append(BluetoothDetailsEntry.USER_ID).append(" INTEGER").append(")");
       createUserDetails.append("CREATE TABLE ").append(UserDetailsEntry.TABLE_NAME).append("(")
               .append(UserDetailsEntry.ID).append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,")
               .append(UserDetailsEntry.FIRST_NAME).append(" TEXT NOT NULL,")
               .append(UserDetailsEntry.LAST_NAME).append(" TEXT NOT NULL,")
               .append(UserDetailsEntry.IMAGE).append(" BLOB,")
               .append(UserDetailsEntry.DATE_OF_BIRTH).append(" TEXT").append(")");
       db.execSQL(createBluetoothDetails.toString());
       db.execSQL(createUserDetails.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
