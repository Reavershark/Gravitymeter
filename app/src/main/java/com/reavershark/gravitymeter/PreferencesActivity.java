package com.reavershark.gravitymeter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

public class PreferencesActivity extends Activity {

    public CheckBox settingsCheckBoxDisplayRawData;
    private SharedPreferences prefs;
    public Handler mHandler;
    public int mInterval = 50; // in ms

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        settingsCheckBoxDisplayRawData = (CheckBox) findViewById(R.id.settingsCheckBoxDisplayRawData);
        prefs = getPreferences(MODE_PRIVATE);

        mHandler = new Handler();

        startRepeater();


        if(prefs.getBoolean("rawData", true)) {
            settingsCheckBoxDisplayRawData.setChecked(true);
        } else {
            settingsCheckBoxDisplayRawData.setChecked(false);
        }

    }

    //Called every mInterval
    Runnable mRepeater = new Runnable() {
        @Override
        public void run() {
            try {
            } finally {


                if (settingsCheckBoxDisplayRawData.isChecked()) {
                    prefs.edit().putBoolean("rawData", true);
                    prefs.edit().apply();
                } else {
                    prefs.edit().putBoolean("rawData", false);
                    prefs.edit().apply();
                }

                Log.w("", "A checkbox was flicked!");

                mHandler.postDelayed(mRepeater, mInterval);
            }
        }
    };

    private void startRepeater() {
        mRepeater.run();
    }

}
