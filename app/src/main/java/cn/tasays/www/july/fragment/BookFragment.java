package cn.tasays.www.july.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import cn.tasays.www.july.R;

public class BookFragment extends BaseFragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.book,container,false);

        init_view(view);

        return view;
    }

    //初始化view
    public void init_view(View view){
        WebView webView = (WebView) view.findViewById(R.id.web_book);
        webView.getSettings().setJavaScriptEnabled(true);//开启javascript
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl("http://116.196.125.67:8080/#/bookshelf");
    }
}
