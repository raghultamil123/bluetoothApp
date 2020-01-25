package com.example.bluetoothapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bluetoothapp.Adapter.BluetoothDetailsAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    List<String> availableDevices = new ArrayList<>();
    List<BluetoothDevice> devices;
    public static BluetoothAdapter bluetoothAdapter;
    Set<BluetoothDevice> bluetoothDevices;
    ListView listView;
    ListView availableView;
    ArrayAdapter<String> availAdapter;
    BluetoothManager manager;
    private static final int REQUEST_ENABLE_BT = 3;
    BluetoothDetailsAdapter adapter;



    @Override
    protected void onDestroy() {
        unregisterReceiver(discoveryFinishReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = (BluetoothManager)this.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        getBlutoothConnection(manager);
        ChatActivity chatActivity = new ChatActivity();




    }

    @Override
    protected void onStart() {

        if(!bluetoothAdapter.isEnabled()){
            Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(bluetoothIntent,REQUEST_ENABLE_BT);
        }
        super.onStart();
    }

    BroadcastReceiver discoveryFinishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("main", "onReceive: ");
            if(BluetoothDevice.ACTION_FOUND.equals(action)){

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);



            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED .equals(action)){

                if(availableDevices.size()==0){

                }
                bluetoothDevices = bluetoothAdapter.getBondedDevices();
                devices = new ArrayList<>(bluetoothDevices);
                pairedDevicesGeneration(devices);
                //    System.out.println("mame "+availableDevices.get(0));


            }
            else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                Toast.makeText(MainActivity.this,"started",Toast.LENGTH_LONG).show();
            }
        }
    };

public void getBlutoothConnection(BluetoothManager manager){
    manager = (BluetoothManager)this.getSystemService(Context.BLUETOOTH_SERVICE);
    //check the bluetooth availablity on device
    bluetoothAdapter = manager.getAdapter();
    if(bluetoothAdapter==null){
        Toast.makeText(this,"Bluetooth is not available",Toast.LENGTH_LONG).show();
        finish();
    }

    if(bluetoothAdapter.isDiscovering()){
        bluetoothAdapter.cancelDiscovery();
    }
    bluetoothAdapter.enable();
    IntentFilter filter = new IntentFilter();
    filter.addAction(BluetoothDevice.ACTION_FOUND);
    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
    registerReceiver(discoveryFinishReceiver,filter);
    bluetoothAdapter.startDiscovery();





}

public void pairedDevicesGeneration(List<BluetoothDevice> paired){

    adapter = new BluetoothDetailsAdapter(MainActivity.this,paired);
    System.out.println("insixe the ");

    adapter.notifyDataSetChanged();
    listView = findViewById(R.id.discovered_device);
    listView.setAdapter(adapter);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(MainActivity.this,devices.get(position).getAddress(),Toast.LENGTH_LONG).show();
            Context context = MainActivity.this;
            Class targetClass = ChatActivity.class;
            Intent targetIntent = new Intent(context,targetClass);
            Bundle bundle = new Bundle();
            bundle.putParcelable("deviceDetail",devices.get(position));
            targetIntent.putExtra("bluetooth",bundle);
            startActivity(targetIntent);
        }
    });
}

}
