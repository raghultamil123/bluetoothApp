package com.example.bluetoothapp.Data.Contact;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/*
 *@author rag
 *@project BluetoothApp
 * bluetoth contact details
 */public class ContactsContract {
     public static final String CONTENT_AUTHORITY = "com.example.bluetoothapp";
     public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
     public static final String CONTACT_PATH = "contacts";
     public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
             CONTENT_AUTHORITY  +"/" +CONTACT_PATH;
     public static final Uri CONTENT_URI = Uri.parse(BASE_CONTENT_URI+"/"+CONTACT_PATH);
     public static final class BluetoothDetailsEntry implements BaseColumns {
         public static final String TABLE_NAME = "bluetooth_details";
         public static final String ID = BaseColumns._ID;
         public static final String DEV_MAC_ADDR = "device_mac_address";
         public static final String DEV_NAME="device_name";
         public static final String USER_ID="user_details_id";
     }
     public static final class UserDetailsEntry implements BaseColumns{
         public static final String TABLE_NAME="user_details";
         public static final String ID = BaseColumns._ID;
         public static final String FIRST_NAME="first_name";
         public static final String IMAGE="image";
         public static final String LAST_NAME="last_name";
         public static final String DATE_OF_BIRTH = "date_of_birth";
     }
}
