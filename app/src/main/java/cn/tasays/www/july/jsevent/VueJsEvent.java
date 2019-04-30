package cn.tasays.www.july.jsevent;

/**
 * 此方法为 vue页面js 执行android 方法函数
 */

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.tasays.www.july.R;
import cn.tasays.www.july.activity.BookRecommendDetailListActivity;
import cn.tasays.www.july.activity.ChaChaDetailActivity;
import cn.tasays.www.july.activity.DetailActivity;
import cn.tasays.www.july.activity.MainActivity;
import cn.tasays.www.july.fragment.HappyFragment;

public class VueJsEvent {

    private WebView webView;
    private FragmentActivity activity;

    //构造方法
    public VueJsEvent(FragmentActivity activity, WebView webView){
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
     * 在index fragment 中点击查询 跳转到 文章列表fragment
     */
    @JavascriptInterface
    public void jumpArticleListFragment(String url){
        Intent intent = new Intent(this.activity, MainActivity.class);
        //当前activity跳转到MainActivity 并且设置默认展示页面
        intent.putExtra("showFragment","articleList");
        intent.putExtra("url",url);
        this.activity.startActivity(intent);

    }

    /**
     * 在index fragment中点击书单跳转到书单列表
     */
    @JavascriptInterface
    public void jumpBookRecommendDetail(String url){
        Intent intent = new Intent(this.activity, BookRecommendDetailListActivity.class);
        intent.putExtra("list_url",url);
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
