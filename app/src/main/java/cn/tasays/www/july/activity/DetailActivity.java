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

public class DetailActivity extends BaseActivity {

    public WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        init_web_view();
    }

    //获得参数加载不同detail
    public void init_web_view()
    {

        //获得上一个活动传入的参数
        Intent intent = getIntent();

        final String id = intent.getStringExtra("id");
        String model = intent.getStringExtra("model");

        webView = (WebView)findViewById(R.id.web_detail);

        //开启webview javascript支持
        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);

        webView.loadUrl("file:///android_asset/detail.html");

        webView.setWebViewClient(new WebViewClient(){
            @Override //加载文件完成后 执行js函数
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl("javascript:show_detail("+id+")");
            }
        });

    }



}
