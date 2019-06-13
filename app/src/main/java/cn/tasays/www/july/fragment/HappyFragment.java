package cn.tasays.www.july.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import cn.tasays.www.july.R;
import cn.tasays.www.july.api.BaseAPi;
import cn.tasays.www.july.jsevent.VueJsEvent;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class HappyFragment extends Fragment {

    public String vueUrl = new BaseAPi().getVueUrl();

    //初始化时设置展示url
    @SuppressLint("ValidFragment")
    public HappyFragment(String url) {
        if(!url.equals("base")){
            this.url =  this.baseHttp+url;
        }else{
            this.url = this.baseHttp+"/art_list";
        }
    }

    private String baseHttp = vueUrl;
    public String url = "";
    private View view;
    private FragmentTransaction fragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.happy,container,false);

        init_view(view);

        //添加fragment到栈中
        FragmentManager fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack("index");
        fragmentTransaction.commit();

        return  view;
    }

    //初始化view
    public void  init_view(View view)
    {
        WebView webView = (WebView) view.findViewById(R.id.web_happy_woman);
        webView.getSettings().setJavaScriptEnabled(true);//开启javascript
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(url);

        //渲染js事件到vue页面
        webView.addJavascriptInterface(new VueJsEvent(getActivity(),webView),"$App");
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
