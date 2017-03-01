package com.bt.bluespp;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothSPP.AutoConnectionListener;
import app.akexorcist.bluetotohspp.library.BluetoothSPP.BluetoothStateListener;
import app.akexorcist.bluetotohspp.library.BluetoothSPP.BluetoothConnectionListener;
import app.akexorcist.bluetotohspp.library.BluetoothSPP.OnDataReceivedListener;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends Activity {
    BluetoothSPP bt;
//    EditText edit=(EditText)findViewById(R.id.editText);
    //public String str;
    OnDataReceivedListener Listener;
    int i=0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText edit=(EditText)findViewById(R.id.editText);
        final TextView text=(TextView)findViewById(R.id.textView2);
        final String str;

        bt = new BluetoothSPP(this);

        if(!bt.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setBluetoothStateListener(new BluetoothStateListener() {
            public void onServiceStateChanged(int state) {
                if(state == BluetoothState.STATE_CONNECTED)
                    Log.i("Check", "State : Connected");
                else if(state == BluetoothState.STATE_CONNECTING)
                    Log.i("Check", "State : Connecting");
                else if(state == BluetoothState.STATE_LISTEN)
                    Log.i("Check", "State : Listen");
                else if(state == BluetoothState.STATE_NONE)
                    Log.i("Check", "State : None");
            }
        });

        bt.setOnDataReceivedListener(new OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                Log.i("Check", "Message : " + message);

                edit.setText("random value is"+message);
                text.setText("random value is"+message);
                Log.i("bbbbbbbbbbbbbb", "aaaaaaaaaaaaaaaaaaaaaaaaaa");
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                Log.i("Check", "Device Connected!!");
                i=1;
                run();
            }

            public void onDeviceDisconnected() {
                Log.i("Check", "Device Disconnected!!");
            }

            public void onDeviceConnectionFailed() {
                Log.i("Check", "Unable to Connected!!");
            }
        });

        bt.setAutoConnectionListener(new AutoConnectionListener() {
            public void onNewConnection(String name, String address) {
                Log.i("Check", "New Connection - " + name + " - " + address);
            }

            public void onAutoConnectionStarted() {
                Log.i("Check", "Auto menu_connection started");
            }
        });

        Button button2=(Button)findViewById(R.id.button2);
        button2.setOnClickListener(new OnClickListener(){
          public void onClick(View v){


              //OnDataReceivedListener Listener;
              bt.setOnDataReceivedListener(Listener);
              run();



          }
        });

        Button btnConnect = (Button)findViewById(R.id.btnconnect);
        btnConnect.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                if(bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(MainActivity.this, DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }

        });
        bt.setOnDataReceivedListener(Listener);
        run();


    }


    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }

    public void onStart() {
        super.onStart();
        if(!bt.isBluetoothEnabled()) {
            bt.enable();
        } else {
            if(!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                bt.setupService();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void setup() {
        Log.i("crappy crappy dooo","crappy");
        //bt.setOnDataReceivedListener(Listener);


    }


    public void run() {
        byte[] buffer = new byte[256];
        int bytes;
        Log.i("aaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaaaaaa");
        if (i == 1) {


            // Keep looping to listen for received messages
           // while (true) {
                Log.i("bbbbbbbbbbbbbbb", "aaaaaaaaaaaaaaaaaaaaaaaaaa");
            bt.send("Message", true);

                bt.setOnDataReceivedListener(Listener);

           // }
        }
    }

    };