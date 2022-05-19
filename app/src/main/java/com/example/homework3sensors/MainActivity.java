package com.example.homework3sensors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager lightSensorManager, motionSensorManager;
    private Sensor lightSensor, motionSensor;
    private ImageView light, motion;

    float lightThreshold = 2000, motionThreshold = 0;
    float xAcceleration, yAcceleration, zAcceleration;
    double currentAcceleration, previousAcceleration, accChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        light = findViewById(R.id.imageView);
        lightSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = lightSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        motion = findViewById(R.id.gifView);
        motionSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        motionSensor = motionSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            if (sensorEvent.values[0] > lightThreshold) {
                light.setImageResource(R.drawable.on);
            } else {
                light.setImageResource(R.drawable.off);
            }
        }

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            xAcceleration = sensorEvent.values[0];
            yAcceleration = sensorEvent.values[1];
            zAcceleration = sensorEvent.values[2];

            currentAcceleration = (float) Math.sqrt((xAcceleration * xAcceleration +
                    yAcceleration * yAcceleration + zAcceleration * zAcceleration));
            accChange = Math.abs(currentAcceleration -previousAcceleration);
            previousAcceleration = currentAcceleration;

            if (accChange > motionThreshold){
                motion.setImageResource(R.drawable.walking);
            }else{
                motion.setImageResource(R.drawable.standing);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        lightSensorManager.unregisterListener(this);
        motionSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lightSensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        motionSensorManager.registerListener(this, motionSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


}