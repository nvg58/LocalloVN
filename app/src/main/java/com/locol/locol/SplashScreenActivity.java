package com.locol.locol;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ProgressBar;


public class SplashScreenActivity extends ActionBarActivity {


    private static final int TIME = 4 * 1000; // 4 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.pre_loader);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);

                Intent intent = new Intent(SplashScreenActivity.this, WalkthroughActivity.class);
                startActivity(intent);

                SplashScreenActivity.this.finish();

                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        }, TIME);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//            }
//        }, TIME);

    }


    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }
}
