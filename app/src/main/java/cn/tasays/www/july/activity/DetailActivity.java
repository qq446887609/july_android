package cn.tasays.www.july.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.tasays.www.july.R;
import cn.tasays.www.july.api.BaseAPi;

public class DetailActivity extends BaseActivity {

    public WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        //获得上一页面传来的url
        Intent intent = getIntent();
        String detail_url = intent.getStringExtra("detail_url");

        WebView webView = (WebView) findViewById(R.id.article_detial);
        webView.getSettings().setJavaScriptEnabled(true);//开启javascript
        webView.getSettings().setDomStorageEnabled(true);
        detail_url = vueUrl+""+detail_url;

        webView.loadUrl(detail_url);

        showToast(detail_url);

        //添加activity到栈中
        add(DetailActivity.this);
    }

}
