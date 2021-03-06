package com.example.bluetoothapp.Data.Contact;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.bluetoothapp.Data.Contact.ContactsContract;
import com.example.bluetoothapp.Data.Contact.ContactsContract.UserDetailsEntry;
import com.example.bluetoothapp.Data.Contact.ContactsContract.BluetoothDetailsEntry;

/*
 *@author rag
 *@project BluetoothApp
 */public class ContactsDataProvider extends ContentProvider {
     ContactsDataHelper contactsDataHelper ;
     public static final int CONTACT_CODE = 1;
     public static final UriMatcher matcher =  new UriMatcher(UriMatcher.NO_MATCH);
     static {
matcher.addURI(ContactsContract.CONTENT_AUTHORITY,ContactsContract.CONTACT_PATH,CONTACT_CODE);
     }
    @Override
    public boolean onCreate() {
        contactsDataHelper = new ContactsDataHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int code = matcher.match(uri);
        switch (code){
            case CONTACT_CODE : return ContactsContract.CONTENT_LIST_TYPE;
            default:throw new IllegalStateException("Exception is called in data provider");
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
       int code = matcher.match(uri);
        System.out.println("mass"+code);
       switch (code){

           case CONTACT_CODE :
             return  insertBluetoothDetails(uri,values);


           default: System.out.println(code);
               throw new IllegalStateException("message is called from the data");
       }
    }

    private Uri insertBluetoothDetails(Uri uri, ContentValues values) {
         System.out.println("rsg");
         Long user_details_id = null ;
         values.put(BluetoothDetailsEntry.USER_ID,user_details_id);

        SQLiteDatabase sqLiteDatabase = contactsDataHelper.getWritableDatabase();
        ContentValues bluetoothValues = new ContentValues();
        ContentValues userDetailsValue = new ContentValues();
        userDetailsValue.put(UserDetailsEntry.FIRST_NAME,values.getAsString(UserDetailsEntry.FIRST_NAME));
        userDetailsValue.put(UserDetailsEntry.LAST_NAME,values.getAsString(UserDetailsEntry.LAST_NAME));
        userDetailsValue.put(UserDetailsEntry.DATE_OF_BIRTH,values.getAsString(UserDetailsEntry.DATE_OF_BIRTH));
        if (values.getAsString(UserDetailsEntry.FIRST_NAME)!=null){

          user_details_id=  insertUserDetails(uri,userDetailsValue);
          bluetoothValues.put(BluetoothDetailsEntry.USER_ID,user_details_id);

        }
        bluetoothValues.put(BluetoothDetailsEntry.DEV_NAME,values.getAsString(BluetoothDetailsEntry.DEV_NAME));
        bluetoothValues.put(BluetoothDetailsEntry.DEV_MAC_ADDR,values.getAsString(BluetoothDetailsEntry.DEV_MAC_ADDR));
       long id =  sqLiteDatabase.insert(BluetoothDetailsEntry.TABLE_NAME,null,bluetoothValues);
        return ContentUris.withAppendedId(uri,id);

    }
    private Long insertUserDetails(Uri uri,ContentValues values){
         SQLiteDatabase sqLiteDatabase = contactsDataHelper.getWritableDatabase();
         long id = sqLiteDatabase.insert(UserDetailsEntry.TABLE_NAME,null,
                 values);
         return id;
    }
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }



}
