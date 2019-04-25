package cn.tasays.www.july.activity;

/**
 * 查查网详情页面
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import cn.tasays.www.july.R;

public class ChaChaDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获得上一页面传来的url
        Intent intent = getIntent();
        String detail_url = intent.getStringExtra("detail_url");//获得signStepTwo 用户输入图片验证码后api返回的key

        WebView webView = (WebView) findViewById(R.id.chacha_detial);
        webView.getSettings().setJavaScriptEnabled(true);//开启javascript
        webView.getSettings().setDomStorageEnabled(true);
        detail_url = "http://116.196.125.67:8080/#/"+detail_url;
        webView.loadUrl(detail_url);
    }
}
