package cn.tasays.www.july.jsevent;

/**
 * 此方法为 vue页面js 执行android 方法函数
 */

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import cn.tasays.www.july.activity.ChaChaDetailActivity;
import cn.tasays.www.july.activity.DetailActivity;

public class VueJsEvent {

    private WebView webView;
    private Activity activity;

    //构造方法
    public VueJsEvent(Activity activity, WebView webView){
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

    /**
     * 在index fragment 中点击文章详情 跳转文章详情
     */
    @JavascriptInterface
    public void jumpArticleDetail(String url){
        Intent intent = new Intent(this.activity, DetailActivity.class);
        intent.putExtra("detail_url",url);
        this.activity.startActivity(intent);
    }

    /**
     * 测试 show Toast
     */
    @JavascriptInterface
    public void showToast(){
        Toast.makeText(this.activity,"66666",Toast.LENGTH_SHORT).show();
    }
}
