package cn.tasays.www.july.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.tasays.www.july.R;
import cn.tasays.www.july.api.BaseAPi;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    public String apiUrl = new BaseAPi().getApiUrl();
    public String vueUrl = new BaseAPi().getVueUrl();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 创建请求队列, 默认并发3个请求, 传入数字改变并发数量: NoHttp.newRequestQueue(5);
        //mRequestQueue = NoHttp.newRequestQueue();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //在fragment中监听onkeyDown事件
    public abstract boolean onKeyDown(int keyCode,KeyEvent event);
}
