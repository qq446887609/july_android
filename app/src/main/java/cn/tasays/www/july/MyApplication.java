package cn.tasays.www.july;
import android.app.Application;

import com.yanzhenjie.nohttp.NoHttp;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化http请求框架
        NoHttp.initialize(this); // NoHttp默认初始化。

        //初始化激光sdk
        JPushInterface.setDebugMode(true);//正式版的时候设置false，关闭调试
        JPushInterface.init(this);
        //建议添加tag标签，发送消息的之后就可以指定tag标签来发送了
        Set<String> set = new HashSet<>();
        set.add("cx");//名字任意，可多添加几个
        JPushInterface.setTags(this, set, null);//设置标签
    }
}