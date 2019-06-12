package cn.tasays.www.july.activity;

/**
 * 图书推荐列表
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import cn.tasays.www.july.R;
import cn.tasays.www.july.api.BaseAPi;

public class BookRecommendDetailListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_recommend_detail);

        //获得vue传来的url
        Intent intent = getIntent();
        String list_url = intent.getStringExtra("list_url");

        WebView webView = findViewById(R.id.book_recommend_detail);
        webView.getSettings().setJavaScriptEnabled(true);//开启javascript
        webView.getSettings().setDomStorageEnabled(true);
        list_url = new BaseAPi().getBaseApi()+"/#"+list_url;
        webView.loadUrl(list_url);

        //添加activity到栈中
        add(BookRecommendDetailListActivity.this);
    }
}
