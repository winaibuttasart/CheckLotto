package nize.example.com.checklotto;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasySplashScreen config = new EasySplashScreen(Screen.this)
                .withFullScreen()
                .withTargetActivity(WelcomeActivity.class)
                .withSplashTimeOut(1500)
                .withBackgroundColor(Color.parseColor("#FFFF99"))
                .withLogo(R.drawable.logoonload);
        View view = config.create();
        setContentView(view);
    }
}