package nize.example.com.checklotto;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout layoutDot;
    private TextView[] dotstv;
    private int[] layouts;
    private Button btnNext;
    private MyPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setStatusBarTransparent();
        setContentView(R.layout.activity_welcome);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        layoutDot = findViewById(R.id.dotLayout);
        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int currentPage = viewPager.getCurrentItem();
                System.out.println(currentPage);
                Log.i("Current : ", currentPage + "");
                if (currentPage == layouts.length - 5) {
                    startCheckLottery();
                } else if (currentPage == layouts.length - 4) {
                    startHistory();
                } else if (currentPage == layouts.length - 3) {
                    startTop();
                } else if (currentPage == layouts.length - 2) {
                    startNumberHistory();
                }
            }
        });

        //แสดง 4 หน้าต่างเป็น Arrays
        layouts = new int[]{R.layout.slider_1, R.layout.slider_2, R.layout.slider_3, R.layout.slider_4, R.layout.slider_5};
        pagerAdapter = new MyPagerAdapter(layouts, getApplicationContext());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //ค้นหาตำแหน่งเมื่อคลิกจนสุดให้หายไป
            @Override
            public void onPageSelected(int position) {
                if (position == layouts.length - 1) {
                    btnNext.setVisibility(View.GONE);
                } else {
                    btnNext.setVisibility(View.VISIBLE);

                }
                setDotStatus(position); //ตัวเลื่อนจุดด้านล่างเมื่อกด next
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setDotStatus(0);
    }

    private void setFirstTimeStartStatus(boolean stt) {
        SharedPreferences ref = getApplicationContext().getSharedPreferences("IntroSliderApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();
        editor.putBoolean("FirstTimeStartFlag", stt);
        editor.commit();
    }

    private void setDotStatus(int page) {
        layoutDot.removeAllViews();
        dotstv = new TextView[layouts.length];
        for (int i = 0; i < dotstv.length; i++) {
            dotstv[i] = new TextView(this);
            dotstv[i].setText(Html.fromHtml("&#8226"));
            dotstv[i].setTextSize(30);
            dotstv[i].setTextColor(Color.parseColor("#a9b4bb"));
            layoutDot.addView(dotstv[i]);

        }
        if (dotstv.length > 0) {
            dotstv[page].setTextColor(Color.parseColor("#ffffff"));
        }
    }

    private void startCheckLottery() {
        setFirstTimeStartStatus(true);
        startActivity(new Intent(WelcomeActivity.this, CheckLottery.class));
    }

    private void startHistory() {
        setFirstTimeStartStatus(true);
        startActivity(new Intent(WelcomeActivity.this, Top.class));
    }

    private void startTop() {
        setFirstTimeStartStatus(true);
        startActivity(new Intent(WelcomeActivity.this, History.class));
    }

    private void startNumberHistory() {
        setFirstTimeStartStatus(true);
        startActivity(new Intent(WelcomeActivity.this, NumberHistory.class));
    }


    private void setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
        builder.setTitle("ออกจากแอป?")
                .setMessage("คุณแน่ใจว่าจะออกจากแอป?")
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        WelcomeActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("ยกเลิก", null).setCancelable(false);

        AlertDialog alert = builder.create();
        alert.show();
    }
}
