package cn.tasays.www.july.fragment;


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
import android.widget.TextView;
import android.widget.Toast;

import cn.tasays.www.july.R;

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

    public void initView(View view)
    {
        WebView webView = (WebView) view.findViewById(R.id.index_webveiw);
        webView.getSettings().setJavaScriptEnabled(true);//开启javascript
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl("http://116.196.125.67:8080/#/");
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
