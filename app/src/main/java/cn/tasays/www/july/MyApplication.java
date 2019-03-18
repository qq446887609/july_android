package cn.tasays.www.july;
import android.app.Application;

import com.yanzhenjie.nohttp.NoHttp;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化http请求框架
        NoHttp.initialize(this); // NoHttp默认初始化。
    }
}