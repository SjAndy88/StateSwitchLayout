package com.jsheng.stateswitchlayout.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.jsheng.stateswitchlayout.StateSwitchLayout;

public class MainActivity extends AppCompatActivity {

    StateSwitchLayout mStateSwitchLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStateSwitchLayout = (StateSwitchLayout) findViewById(R.id.state_switch_layout);
        mStateSwitchLayout.setErrorClickListener(R.id.error1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingToSuccess();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadingToEmpty();
    }

    private void loadingToEmpty() {
        mStateSwitchLayout.showLoadingView();
        mStateSwitchLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mStateSwitchLayout.showEmptyView();
                mStateSwitchLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingToError();
                    }
                }, 300);
            }
        }, 3000);
    }

    private void loadingToError() {
        mStateSwitchLayout.showLoadingView();
        mStateSwitchLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mStateSwitchLayout.showErrorView();
//                mStateSwitchLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        loadingToSuccess();
//                    }
//                }, 3000);
            }
        }, 3000);
    }

    private void loadingToSuccess() {
        mStateSwitchLayout.showLoadingView();
        mStateSwitchLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mStateSwitchLayout.switchToSucceed();
            }
        }, 1);
        Log.d("J.Sheng", System.currentTimeMillis() + "");
    }
}
