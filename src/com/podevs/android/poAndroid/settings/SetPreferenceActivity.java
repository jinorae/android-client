package com.podevs.android.poAndroid.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.podevs.android.poAndroid.NetworkService;

public class SetPreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new Settings()).commit();
    }

    @Override
    public void onBackPressed() {
        try {NetworkService.loadSettings();}
        catch (Exception e) {
            Log.e("Preference Activity", "FAILURE TO CALL NETWORK");
        }
        super.onBackPressed();
    }

}
