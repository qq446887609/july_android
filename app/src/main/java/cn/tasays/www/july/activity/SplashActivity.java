package cn.tasays.www.july.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.tasays.www.july.R;

public class SplashActivity extends BaseActivity {

    protected Handler myHandelr = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        View decorView = getWindow().getDecorView();//获取屏幕decorView
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);//设置全屏

        //检测网络

        //先判断是否需要登录。不需要初始化信息。需要跳转登录页面。

        //先要初始化一些信息

        myHandelr.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        }, 2000);

        //添加activity到栈中
        add(SplashActivity.this);
    }
}
