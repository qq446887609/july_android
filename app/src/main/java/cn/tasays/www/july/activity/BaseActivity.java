package cn.tasays.www.july.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;

import java.util.Stack;

import cn.tasays.www.july.R;

public class BaseActivity extends AppCompatActivity {

    /**
     * 请求队列
     */
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //设置修改状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏的颜色，和你的app主题或者标题栏颜色设置一致就ok了
            window.setStatusBarColor(getResources().getColor(R.color.white));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//黑色
        }

        super.onCreate(savedInstanceState);

        // 创建请求队列, 默认并发3个请求, 传入数字改变并发数量: NoHttp.newRequestQueue(5);
        mRequestQueue = NoHttp.newRequestQueue();
    }


    public void showToast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRequestQueue.cancelAll(); // 退出页面时时取消所有请求。
        mRequestQueue.stop(); // 退出时销毁队列，回收资源。
    }

    /**
     * 发起一个请求(单例模式)
     * 这里的RequestQueue是请求队列，默认3个并发请求，也就是有3个子线程在跑，所以正式项目中不要每发起一个请求就new一个队列，
     * 建议在BaseActivity#onCreate(Bundle)中new一个队列即可，
     * 这个activity的所有请求包括依赖此activity的fragment都是用这个队列即可，在BaseActivity#onDestory()时stop这个队列即可，
     * 当然特殊情况下可以再new一个Queue出来
     *
     * @param what     what.
     * @param request  请求对象。
     * @param listener 结果监听。
     * @param <T>      要请求到的数据类型。
     */
    public <T> void request(int what, Request<T> request, OnResponseListener<T> listener) {
        mRequestQueue.add(what, request, listener);
    }

    /**
     * 图片base64为转换
     */
    public Bitmap base64ToBitmap(String base64Str)
    {
        Bitmap decodedByte = null;

        try{
            byte[] decodedString = Base64.decode(base64Str, Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception $e) {
            $e.getMessage();
        }

        return  decodedByte;
    }


    //activity合集
    private static Stack<Activity> activities = new Stack<>();

    public static void add(Activity activity) {
        activities.add(activity);
    }

    //在某个activity中调用此方法，可以删除自己

    public static void remove(Activity activity) {
        activities.remove(activity);
    }

    //清空存放activity的集合
    public static void clear() {
        activities.clear();
    }

    //在退出应用的时候遍历关闭所有的activity
    public static void killAll() {
        for (Activity activity : activities) {
            activity.finish();
        }
    }

}
