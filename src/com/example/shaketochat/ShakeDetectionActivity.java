package com.example.shaketochat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

public class ShakeDetectionActivity extends Activity {

	
	protected static final int PICK_CONTACT = 0;
	private SensorManager mSensorManager;
	private ShakeEventListener mSensorListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mSensorListener = new ShakeEventListener();
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensorManager.registerListener((SensorEventListener) mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
		mSensorListener
				.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
					public void onShake() {

						Toast.makeText(ShakeDetectionActivity.this, "Shake!",
								Toast.LENGTH_SHORT).show();
						// Get instance of Vibrator from current Context
						Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

						// Vibrate for 300 milliseconds
						v.vibrate(300);
						Intent waIntent = new Intent(Intent.ACTION_SEND);
					    waIntent.setType("text/plain");
					            String text = "Hi";
					    waIntent.setPackage("com.whatsapp");
					    if (waIntent != null) {
					        waIntent.putExtra(Intent.EXTRA_TEXT, text);//
					        startActivity(Intent.createChooser(waIntent, "Send Message"));
					    } else {
					        //Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
					               // .show();
					    }
						
					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener((SensorEventListener) mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	protected void onStop() {
		mSensorManager.unregisterListener((SensorListener) mSensorListener);
		super.onStop();
	}

}
