package cn.tasays.www.july.activity;

/**
 * 查查网详情页面
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import cn.tasays.www.july.R;
import cn.tasays.www.july.api.BaseAPi;

public class ChaChaDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chacha_detail);

        //获得vue传来的url
        Intent intent = getIntent();
        String detail_url = intent.getStringExtra("detail_url");

        WebView webView = (WebView) findViewById(R.id.chacha_detial);
        webView.getSettings().setJavaScriptEnabled(true);//开启javascript
        webView.getSettings().setDomStorageEnabled(true);
        detail_url = new BaseAPi().getBaseApi()+"/#"+detail_url;
        webView.loadUrl(detail_url);

        //添加activity到栈中
        add(ChaChaDetailActivity.this);
    }
}
