package com.example.mobile.projects.sensor;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

import com.example.mobile.MyApp;
import com.example.mobile.R;
import com.example.mobile.utils.helper.LanternHelper;
import com.example.mobile.utils.helper.MotorHelper;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SensorReading extends Activity implements SensorEventListener {

    private static final int REQUEST_CODE = 10;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private Sensor proximitySensor;
    private float lightValue;
    private float proximityValue;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.sensor_reading);

        this.sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        this.lightSensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        this.proximitySensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (this.lightSensor != null) {
            this.sensorManager.registerListener(this, this.lightSensor, SensorManager.SENSOR_DELAY_GAME);
        }

        if (this.proximitySensor != null) {
            this.sensorManager.registerListener(this, this.proximitySensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        final float value = event.values[0];
        final Sensor s = event.sensor;

        if (s.getType() == Sensor.TYPE_LIGHT) {
            this.lightValue = value;
        } else if (s.getType() == Sensor.TYPE_PROXIMITY) {
            this.proximityValue = value;
        }
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int accuracy) {
        //
    }

    public void clickSensorReadings(final View view) {
        final Intent it = new Intent("SENSOR_READINGS");

        it.putExtra("lightValue", this.lightValue);
        it.putExtra("proximityValue", this.proximityValue);

        this.startActivityForResult(it, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent it) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && it != null) {
            final String lightClassification = it.getStringExtra("lightClassification");
            final String proximityClassification = it.getStringExtra("proximityClassification");

            final SwitchMaterial lanternSwitch = this.findViewById(R.id.lantern_switch);
            final SwitchMaterial vibrationSwitch = this.findViewById(R.id.vibration_switch);

            final LanternHelper lanternHelper = new LanternHelper(MyApp.getAppContext());
            final MotorHelper motorHelper = new MotorHelper(MyApp.getAppContext());

            if ("low".equals(lightClassification)) {
                lanternHelper.turnOn();
                lanternSwitch.setChecked(true);
            } else if ("high".equals(lightClassification)) {
                lanternHelper.turnOff();
                lanternSwitch.setChecked(false);
            }

            if ("far".equals(proximityClassification)) {
                motorHelper.initiateVibration();
                vibrationSwitch.setChecked(true);
            } else if ("close".equals(proximityClassification)) {
                motorHelper.stopVibration();
                vibrationSwitch.setChecked(false);
            }
        }
    }
}
