package com.tm.tarvemart.mainUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tm.tarvemart.Helper.NetworkConnectionHelper;
import com.tm.tarvemart.R;

import java.util.Timer;
import java.util.TimerTask;

public class NoInternetConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (NetworkConnectionHelper.isOnline(NoInternetConnectionActivity.this)) {
                    finish();
                } else {

                }
            }
        }, 2000, 2000);

    }
}
