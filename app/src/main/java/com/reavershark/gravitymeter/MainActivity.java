package com.reavershark.gravitymeter;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

    //Vars
    public float[] gravity = {0, 0, 0};
    public float pointerImgViewCenterX = 0;
    public float pointerImgViewCenterY = 0;

    public TextView gravxTxtView, GravyTxtView, gravyTxtView, gravzTxtView;
    public ImageView pointerImgView, centerImgView;
    public Handler mHandler;
    public int mInterval = 10; // in ms

    //Called on activity start, some methods like onSensorChanged might be called earlier
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gravxTxtView = (TextView) findViewById(R.id.gravxTxtView);
        gravyTxtView = (TextView) findViewById(R.id.gravyTxtView);
        gravzTxtView = (TextView) findViewById(R.id.gravzTxtView);
        pointerImgView = (ImageView) findViewById(R.id.pointerImageView);
        centerImgView = (ImageView) findViewById(R.id.centerImageView);
        mHandler = new Handler();

        startRepeater();

        pointerImgView.setImageResource(R.mipmap.circle_filled);
        pointerImgView.setImageAlpha(100);
        centerImgView.setImageResource(R.mipmap.circle);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);

        if (!getPreferences(MODE_PRIVATE).contains("rawData")){
            getPreferences(MODE_PRIVATE).edit().putBoolean("rawData", true);
            getPreferences(MODE_PRIVATE).edit().apply();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    //Called every listened sensor update
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && event.accuracy == SensorManager.SENSOR_STATUS_ACCURACY_HIGH) {
            gravity[0] = event.values[0];
            gravity[1] = -event.values[1];
            gravity[2] = -event.values[2];
        }
    }

    //Called every mInterval
    Runnable mRepeater = new Runnable() {
        @Override
        public void run() {
            try {

            } finally {
                gravxTxtView.setText(String.format("Gravity x: %s", Float.toString(gravity[0])));
                gravyTxtView.setText(String.format("Gravity y: %s", Float.toString(gravity[1])));
                gravzTxtView.setText(String.format("Gravity z: %s", Float.toString(gravity[2])));

                pointerImgViewCenterX = centerImgView.getX();
                pointerImgViewCenterY = centerImgView.getY();

                pointerImgView.setX(pointerImgViewCenterX + gravity[0] * 50);
                pointerImgView.setY(pointerImgViewCenterY + gravity[1] * 50);

                if (getPreferences(MODE_PRIVATE).getBoolean("rawData", true)){
                    gravxTxtView.setVisibility(View.VISIBLE);
                    gravyTxtView.setVisibility(View.VISIBLE);
                    gravzTxtView.setVisibility(View.VISIBLE);
                } else {
                    gravxTxtView.setVisibility(View.INVISIBLE);
                    gravyTxtView.setVisibility(View.INVISIBLE);
                    gravzTxtView.setVisibility(View.INVISIBLE);
                }

                mHandler.postDelayed(mRepeater, mInterval);
            }
        }
    };

    private void startRepeater() {
        mRepeater.run();
    }
    /*
    private void stopRepeater(){
        mHandler.removeCallbacks(mRepeater);
    }
    */


    @Override //Create menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override //Called when an menu item is selected
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_about:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //start about activity on menu press
    public void aboutClicked(MenuItem item) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    //Start settings activity on menu press
    public void settingsClicked(MenuItem item) {
        Intent intent = new Intent(this, PreferencesActivity.class);
        startActivity(intent);
    }
}
