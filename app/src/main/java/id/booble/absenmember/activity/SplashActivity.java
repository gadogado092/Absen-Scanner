package id.booble.absenmember.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import id.booble.absenmember.R;
import id.booble.absenmember.util.MyPreference;

public class SplashActivity extends AppCompatActivity {

    MyPreference myPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        myPreference = new MyPreference(SplashActivity.this);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                myPreference.checkLogin();
                finish();
            }
        },2000L);
    }
}
