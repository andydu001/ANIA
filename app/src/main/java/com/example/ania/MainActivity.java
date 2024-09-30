package com.example.ania;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.companion.*;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.BatteryState;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.ext.SdkExtensions;
import android.os.health.HealthStats;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.bluetooth.*;
import android.telecom.TelecomManager;
import android.adservices.measurement.MeasurementManager;
import android.os.health.SystemHealthManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.os.VibratorManager;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private Button btn, btn2;
    private TextView textView;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private static BluetoothDevice bluetoothDevice;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.button);
        btn2 = findViewById(R.id.button2);
        textView = findViewById(R.id.textView3);
        textView1 = findViewById(R.id.text3);
        textView2 = findViewById(R.id.text4);
        textView3 = findViewById(R.id.text5);

        btn.setBackgroundColor(Color.GREEN);
        btn2.setBackgroundColor(Color.MAGENTA);
        //BTE
        TelecomManager telecomManager = getSystemService(TelecomManager.class);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            String[] perm = {Manifest.permission.READ_PHONE_STATE};
            ActivityCompat.requestPermissions(this, perm, 0);

        }
        telecomManager.showInCallScreen(true);
        textView.append(" Device:");
        textView1.append("Address:");
        textView2.append("ON:");
        textView3.append("Audio Connect:");
        btn.setOnClickListener(view -> {
            //    Log.println(Log.WARN, "Tag", "Check my device " );

            //Get Default adapter
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            //  bluetoothAdapter.

            //  BluetoothDevice bluetoothDevice = getSystemService(BluetoothDevice.class);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                String[] perm = {Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_SCAN};
                ActivityCompat.requestPermissions(this, perm, 0);


                return;
            }
            //bluetoothAdapter.setName("CFS HSGA");
            Log.println(Log.WARN, "Tag", "Check my device " + bluetoothAdapter.getName());
            // Poulate info
           /* textView.append("\t" + bluetoothAdapter.getName() + "\n");
            textView1.append("\t\t" + bluetoothAdapter.getAddress() + "\n");
            textView2.append("\t"   + "\n");*/


            //Get scanner instance
            BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
            Log.println(Log.WARN, "Tag", "Check my device " + bluetoothLeScanner);
            // Scan for devices

            bluetoothAdapter.startDiscovery();

            Set<BluetoothDevice>  bluetoothDeviceSet = bluetoothAdapter.getBondedDevices();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    try {
                        BluetoothServerSocket bluetoothServerSocket = bluetoothAdapter.listenUsingInsecureL2capChannel();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                Log.println(Log.WARN, "Tag", "Check my device " + bluetoothAdapter.isLe2MPhySupported());
            }

            String str = String.valueOf(bluetoothDeviceSet.stream().findFirst());

            if(bluetoothAdapter.isEnabled()) {

                BluetoothDevice bluetoothDevice1 = bluetoothAdapter.getRemoteDevice(str.substring(9, 26));

                textView.append("\t" + bluetoothAdapter.getName() + "\n");
                textView1.append("\t\t" + bluetoothAdapter.getAddress() + "\n");
                textView2.append("\t"   +bluetoothAdapter.isEnabled() +"\n");
                textView3.append("\t" + bluetoothDevice1.getName());
            }
            else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {


                    VibratorManager vibratorManager = getSystemService(VibratorManager.class);
                    Vibrator vibrator = vibratorManager.getDefaultVibrator();
                    vibrator.vibrate(VibrationEffect.createOneShot(1000, 1));

                    BatteryManager batteryManager = getSystemService(BatteryManager.class);
                    BatteryState batteryState = getSystemService(BatteryState.class);
                    //textView3.setText("");
                    Dialog dialog = new Dialog(this);

                    Button button = new Button(this);
                    button.setBackgroundColor(Color.WHITE);
                    ViewPropertyAnimator propertyAnimator = button.animate();

                    propertyAnimator.rotation(6.6f);
                    propertyAnimator.start();

                    button.append("Bluetooth is off. Please turn it on.");
                    dialog.setContentView(button);

                    dialog.show();


                }
                //textView3.append("\t"+"Turn on Bluetooth");
            }


            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textView.setText("");
                    textView1.setText("");
                    textView2.setText("");
                    textView3.setText("");
                    textView.append("Device:");
                    textView1.append("Address:");
                    textView2.append("ON:");
                    textView3.append("Audio Connect:");

                    //textView1.setText("\t\t" + bluetoothAdapter.getAddress() + "\n");
                    //textView2.setText("\t" + bluetoothAdapter.isEnabled() + "\n");
                    // textView3.setText("\t" + bluetoothDevice1.getName());
                    SystemHealthManager systemHealthManager = getSystemService(SystemHealthManager.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(SdkExtensions.AD_SERVICES) >= 4) {
                        MeasurementManager measurementManager = getSystemService(MeasurementManager.class) ;

                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        BluetoothDeviceFilter bluetoothDeviceFilter = getSystemService(BluetoothDeviceFilter.class);


                    }
                    HealthStats healthStats = systemHealthManager.takeMyUidSnapshot();
                    // textView3.setText(healthStats.getTimersKeyCount());
                }
            });


            bluetoothLeScanner.startScan(new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {

                    Log.println(Log.WARN, "Tag", "Check my device sssss" + result.getDevice());


                    super.onScanResult(callbackType, result);
                }
            });



            bluetoothLeScanner.flushPendingScanResults(new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    Log.println(Log.WARN, "Tag", "Check my device sssss" + result.getDevice());

                    super.onScanResult(callbackType, result);
                }
            });

        });



    }
}

class Conf extends BluetoothGattService {


    public Conf(UUID uuid, int serviceType) {
        super(uuid, serviceType);
    }

    static class Base implements BluetoothProfile {

        private BluetoothDevice bluetoothDevice;
        private Context context;
        private BluetoothGattCallback gattCallback;
        private List<BluetoothDevice> bluetoothDevices;


        Base(Context context, BluetoothGattCallback gattCallback) {
            this.context = context;

            this.gattCallback = gattCallback;


        }

        @Override
        public List<BluetoothDevice> getConnectedDevices() {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }


            //  BluetoothGatt gatt = bluetoothDevice.connectGatt(context, true, gattCallback);
            return bluetoothDevices;
        }

        @Override
        public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] ints) {
            return Collections.emptyList();
        }

        @Override
        public int getConnectionState(BluetoothDevice bluetoothDevice) {
            return BluetoothProfile.STATE_DISCONNECTED;
        }
    }
}