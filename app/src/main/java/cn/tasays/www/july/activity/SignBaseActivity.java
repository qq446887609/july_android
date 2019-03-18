package cn.tasays.www.july.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import cn.tasays.www.july.R;

/**
 * Created by 适可而止 on 2019/3/19.
 * Desc: 公用跳转方法
 */

public class SignBaseActivity extends BaseActivity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public void jumpLogin()
    {
        TextView jump_btn = findViewById(R.id.jump_login);
        jump_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
