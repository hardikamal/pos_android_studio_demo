package com.szfp.demoui.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.szfp.demoui.R;


public class WelcomeActivity extends BaseActivity implements OnClickListener{
	private Button audio,serial_port,normal_blu,other_blu,print;
	private Intent intent;
	private static final int LOCATION_CODE = 101;
	private LocationManager lm;//【Location management】

	private static final String[] BLE_PERMISSIONS = new String[]{
			android.Manifest.permission.ACCESS_COARSE_LOCATION,
			android.Manifest.permission.ACCESS_FINE_LOCATION,
	};

	private static final String[] ANDROID_12_BLE_PERMISSIONS = new String[]{
			android.Manifest.permission.BLUETOOTH_SCAN,
			android.Manifest.permission.BLUETOOTH_CONNECT,
			android.Manifest.permission.ACCESS_FINE_LOCATION,
	};

	public static void requestBlePermissions(Activity activity, int requestCode) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
			ActivityCompat.requestPermissions(activity, ANDROID_12_BLE_PERMISSIONS, requestCode);
		else
			ActivityCompat.requestPermissions(activity, BLE_PERMISSIONS, requestCode);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		setTitle(getString(R.string.title_welcome));
		audio=(Button) findViewById(R.id.audio);
		serial_port=(Button) findViewById(R.id.serial_port);
		normal_blu=(Button) findViewById(R.id.normal_bluetooth);
		other_blu=(Button) findViewById(R.id.other_bluetooth);
		print = (Button) findViewById(R.id.print);
		audio.setOnClickListener(this);
		serial_port.setOnClickListener(this);
		normal_blu.setOnClickListener(this);
		other_blu.setOnClickListener(this);
		print.setOnClickListener(this);
		requestBlePermissions(this,100);
	}

	@Override
	public void onToolbarLinstener() {

	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_welcome;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.audio://Audio
				intent = new Intent(this,OtherActivity.class);
				intent.putExtra("connect_type", 1);
				startActivity(intent);
				break;
			case R.id.serial_port://Serial Port
				intent = new Intent(this, OtherActivity.class);

				intent.putExtra("connect_type", 2);
				startActivity(intent);
				break;
			case R.id.normal_bluetooth://Normal Bluetooth
				intent = new Intent(this, MainActivity.class);

				intent.putExtra("connect_type", 3);
				startActivity(intent);
				break;
			case R.id.other_bluetooth://Other Bluetooth，such as：BLE，，，
				intent = new Intent(this,MainActivity.class);
				intent.putExtra("connect_type", 4);
				startActivity(intent);
				break;
			case R.id.print:
				Log.d("pos","print");
				intent = new Intent(this, PrintSettingActivity.class);
				startActivity(intent);
				break;
		}
	}

	public void bluetoothRelaPer() {

		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter != null && !adapter.isEnabled()) {//if bluetooth is disabled, add one fix
			Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivity(enabler);
		}
		lm = (LocationManager) WelcomeActivity.this.getSystemService(WelcomeActivity.this.LOCATION_SERVICE);
		boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (ok) {//Location service is on
			if (ContextCompat.checkSelfPermission(WelcomeActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
					!= PackageManager.PERMISSION_GRANTED) {
				Log.e("POS_SDK", "Permission Denied");
				// Permission denied
				// Request authorization
				ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_CODE);
//                        Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
			} else {
				// have permission
				Toast.makeText(WelcomeActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
			}
		} else {
			Log.e("BRG", "System detects that the GPS location service is not turned on");
			Toast.makeText(WelcomeActivity.this, "System detects that the GPS location service is not turned on", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(intent, 1315);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		//bluetoothRelaPer();
		switch (requestCode) {
			case LOCATION_CODE: {
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// Permission is agreed by the user
					Toast.makeText(WelcomeActivity.this, getString(R.string.msg_allowed_location_permission), Toast.LENGTH_LONG).show();
				} else {
					// Permission is denied by the user
					Toast.makeText(WelcomeActivity.this, getString(R.string.msg_not_allowed_loaction_permission), Toast.LENGTH_LONG).show();
				}
			}
			break;
		}
	}

}
