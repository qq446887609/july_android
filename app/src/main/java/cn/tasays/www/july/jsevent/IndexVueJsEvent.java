package cn.tasays.www.july.jsevent;

/**
 * 此方法为index vue页面js 执行android 方法函数
 */

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import cn.tasays.www.july.activity.ChaChaDetailActivity;

public class IndexVueJsEvent {

    private WebView webView;
    private Activity activity;

    //构造方法
    public IndexVueJsEvent(Activity activity, WebView webView){
        this.activity = activity;
        this.webView = webView;
    }

    /**
     * 在index fragment 中点击查查网文章跳转详情
     * @param url 要跳转chacha详情的url
     */
    @JavascriptInterface
    public void jumpChaChaDetail(String url){
        Intent intent = new Intent(this.activity, ChaChaDetailActivity.class);
        intent.putExtra("detail_url",url);
        this.activity.startActivity(intent);
    }
}
