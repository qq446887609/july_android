package cn.tasays.www.july.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import cn.tasays.www.july.R;
import cn.tasays.www.july.jsevent.VueJsEvent;

import static android.content.Context.MODE_PRIVATE;
import static cn.tasays.www.july.activity.BaseActivity.killAll;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends BaseFragment{

    private View view;
    private long mExitTime;

    FragmentTransaction fragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.index,container,false);

        initView(view);

        //添加fragment到栈中
        FragmentManager fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack("index");
        fragmentTransaction.commit();

        return  view;
    }

    //加载首页
    public void initView(View view)
    {
        final WebView webView = (WebView) view.findViewById(R.id.index_webveiw);
        webView.getSettings().setJavaScriptEnabled(true);//开启javascript
        webView.getSettings().setDomStorageEnabled(true);

        webView.loadUrl("http://116.196.125.67:8080/#/");
        //渲染js事件到vue页面
        webView.addJavascriptInterface(new VueJsEvent(getActivity(),webView),"$App");


        //需要等页面加载完在 WebView 的 onPageFinished 方法中写调用逻辑，否则不会执行。
        webView.setWebViewClient(new WebViewClient(){

            //安卓调用js方法。注意需要在 onPageFinished 回调里调用
            @Override
            public void onPageFinished(final WebView view, String url) {
                super.onPageFinished(view, url);
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        //获得登录成功后的用户token
                        SharedPreferences preferences = getActivity().getSharedPreferences("user_data",MODE_PRIVATE);
                        String token = preferences.getString("access_token","");
                        if(!token.isEmpty()){
                            view.loadUrl("javascript:getUser('"+token+"')");
                        }
                    }
                });
            }
        });

    }

    @Override
    public  boolean onKeyDown(int keyCode,KeyEvent event) {
        boolean ret = false;
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_UP:
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                break;
            case KeyEvent.KEYCODE_BACK:
                //判断用户是否点击了“返回键”
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //与上次点击返回键时刻作差
                    if ((System.currentTimeMillis() - mExitTime) > 2000) {
                        //大于2000ms则认为是误操作，使用Toast进行提示
                        Toast.makeText(getActivity(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        //并记录下本次点击“返回键”的时刻，以便下次进行判断
                        mExitTime = System.currentTimeMillis();
                    } else {
                        //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                        //在退出应用的时候遍历关闭所有的activity
                        killAll();
                        System.exit(0);
                    }
                    ret = true;
                }
                break;

        }
        return ret;
    }
}
