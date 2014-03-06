package com.example.shaketochat;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeEventListener implements SensorEventListener {

	private static final int MIN_FORCE = 10;
	private static final int MIN_DIRECTION_CHANGE = 3;

	private static final int MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE = 200;

	private static final int MAX_TOTAL_DURATION_OF_SHAKE = 400;

	private long mFirstDirectionChangeTime = 0;

	private long mLastDirectionChangeTime;

	private int mDirectionChangeCount = 0;

	private float lastX = 0;

	private float lastY = 0;

	private float lastZ = 0;

	private OnShakeListener mShakeListener;

	public interface OnShakeListener {

		void onShake();
	}

	public void setOnShakeListener(OnShakeListener listener) {
		mShakeListener = listener;
	}

	@Override
	public void onSensorChanged(SensorEvent se) {
		// get sensor data
		float x = se.values[SensorManager.DATA_X];
		float y = se.values[SensorManager.DATA_Y];
		float z = se.values[SensorManager.DATA_Z];
		// calculate movement
		float totalMovement = Math.abs(x + y + z - lastX - lastY - lastZ);
		if (totalMovement > MIN_FORCE) {
			// get time
			long now = System.currentTimeMillis();
			// store first movement time
			if (mFirstDirectionChangeTime == 0) {
				mFirstDirectionChangeTime = now;
				mLastDirectionChangeTime = now;
			}
			// check if the last movement was not long ago
			long lastChangeWasAgo = now - mLastDirectionChangeTime;
			if (lastChangeWasAgo < MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE) {
				// store movement data
				mLastDirectionChangeTime = now;
				mDirectionChangeCount++;
				// store last sensor data
				lastX = x;
				lastY = y;
				lastZ = z;
				// check how many movements are so far
				if (mDirectionChangeCount >= MIN_DIRECTION_CHANGE) {
					// check total duration
					long totalDuration = now - mFirstDirectionChangeTime;
					if (totalDuration < MAX_TOTAL_DURATION_OF_SHAKE) {
						mShakeListener.onShake();
						resetShakeParameters();
					}
				}
			} else {
				resetShakeParameters();
			}
		}
	}

	private void resetShakeParameters() {
		mFirstDirectionChangeTime = 0;
		mDirectionChangeCount = 0;
		mLastDirectionChangeTime = 0;
		lastX = 0;
		lastY = 0;
		lastZ = 0;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
}
