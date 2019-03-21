package cn.tasays.www.july.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import cn.tasays.www.july.R;
import cn.tasays.www.july.activity.DetailActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HappyFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.happy,container,false);

        init_view(view);

        return  view;
    }

    //初始化view
    public void  init_view(View view)
    {
        WebView webView = (WebView) view.findViewById(R.id.web_happy_woman);
        webView.getSettings().setJavaScriptEnabled(true);//开启javascript
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl("http://116.196.125.67:8080/#/art_list");
    }

    /*//重写javascript接口方法 跳转detail
    *//**
     * @param id     文章or数据id
     * @param model  model 具体操作哪张表
     *//*
    @JavascriptInterface
    public void startDetail(String id,String model)
    {
        Intent intent = new Intent();
        intent.putExtra("id",id);
        intent.putExtra("model",model);
        intent.setClass(getActivity(),DetailActivity.class);
        this.startActivity(intent);
    }*/
}
