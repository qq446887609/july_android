package cn.tasays.www.july.fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yanzhenjie.nohttp.NoHttp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.tasays.www.july.R;
import cn.tasays.www.july.activity.BaseActivity;
import cn.tasays.www.july.activity.MainActivity;
import cn.tasays.www.july.api.BaseAPi;
import cn.tasays.www.july.jsevent.VueJsEvent;
import cn.tasays.www.july.model.User;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;


import static android.content.Context.MODE_PRIVATE;
import static cn.tasays.www.july.activity.BaseActivity.killAll;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends BaseFragment{

    private View view;
    private long mExitTime;
    private ProgressDialog progressDialog;


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

        //首页检测更新
        checkUpdate();

        //初始化ProgressDialog进度条
        progressDialog = progress();

        permissiongen();

        return  view;
    }

    //申请权限
    private void permissiongen() {
        //处理需要动态申请的权限
        PermissionGen.with(getActivity())
                .addRequestCode(200)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .request();
    }

    //申请权限结果的返回
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    //权限申请成功
    @PermissionSuccess(requestCode = 200)
    public void doSomething() {
        //在这个方法中做一些权限申请成功的事情
        Toast.makeText(getActivity().getApplication(), "成功", Toast.LENGTH_SHORT).show();

    }

    //申请失败
    @PermissionFail(requestCode = 200)
    public void doFailSomething() {
        Toast.makeText(getActivity().getApplication(), "失败", Toast.LENGTH_SHORT).show();
    }


    /*
     * 方法名：progress()
     * 功    能：初始化ProgressDialog进度框
     * 参    数：无
     * 返回值：ProgressDialog
     */
    private ProgressDialog progress() {
        //自定义标题
        TextView title = new TextView(getActivity());
        title.setText("正在更新");//设置文本
        title.setPadding(0, 40, 0, 0); //边距,左上右下
        title.setGravity(Gravity.CENTER); //位置
        title.setTextColor(Color.parseColor("#5d9eff"));//字体的颜色
        title.setTextSize(23); //字体的大小

        ProgressDialog progressDialog = new ProgressDialog(getActivity());//创建一个ProgressDialog的实例
        progressDialog.setProgressDrawable(getResources().getDrawable(R.drawable.dialog_color));//设置背景色,设置进度条颜色
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//样式_水平
        progressDialog.setMax(100);//最大值
        progressDialog.setCancelable(false);//设置可否用back键关闭对话框
        progressDialog.setCustomTitle(title);//设置自定义标题
        progressDialog.setProgress(0);//设定进度
//        progressDialog.setMessage("内容");//内容消息
//      progressDialog.setTitle("正在更新");//标题
//      progressDialog.setProgressNumberFormat(" ");//只显示左下角的百分比,右下角的数字去掉
        return progressDialog;
    }

    /*
     * 方法名： download()
     * 功    能：下载apk，保存到本地，安装apk
     * 参    数：无
     * 返回值：无
     */
    private void download(String apkUrl) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apkUrl)
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG-失败", e.toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.cancel();
                        Toast.makeText(getActivity().getApplication(), "网络请求失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws FileNotFoundException {
                Log.e("TAG-下载成功", response.code() + "---" + response.body().toString());

                //设置apk存储路径和名称
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/百度助手.apk");

                //保存文件到本地
                localStorage(response, file);
            }
        });
    }

    /*
     * 方法名：localStorage(final Response response, final File file)
     * 功    能：保存文件到本地
     * 参    数：Response response, File file
     * 返回值：无
     */
    private void localStorage(final Response response, final File file) throws FileNotFoundException {
        //拿到字节流
        InputStream is = response.body().byteStream();
        int len = 0;
        final FileOutputStream fos = new FileOutputStream(file);
        byte[] buf = new byte[2048];
//        int currentSize=0;
        try {
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
//                currentSize+=len;
//                Log.e("TAG保存到文件进度：", currentSize + "/" + response.body().contentLength());
                Log.e("TAG每次写入到文件大小", "onResponse: " + len);
                Log.e("TAG保存到文件进度：", file.length() + "/" + response.body().contentLength());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.setProgress((int) (file.length() * 100 / response.body().contentLength()));//当前文件长度除以文件总长度乘以100，返回int值。
                        //如果当前文件长度等于文件总长度与progressDialog框存在
                        if (file.length() == response.body().contentLength() && progressDialog.isShowing()) {
                            //进度重置，关闭进度框
                            progressDialog.setProgress(0);
                            progressDialog.cancel();
                            //弹出对话框，提示是否安装
                            new AlertView("下载完成", "是否立即安装?", null, null, new String[]{"取消", "安装"}, getActivity(), AlertView.Style.Alert, new OnItemClickListener() {
                                @Override
                                public void onItemClick(Object o, int position) {
                                    switch (position) {
                                        case 1:
                                            //安装apk
                                            installingAPK(file);
                                            break;
                                    }
                                }
                            }).show();
                        }
                    }
                });
            }
            fos.flush();
            fos.close();
            is.close();
        } catch (IOException e) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.setProgress(0);
                    progressDialog.cancel();
                    Toast.makeText(getActivity(), "下载失败！", Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
        }
    }

    /*
     * 方法名：installingAPK(File file)
     * 功    能：安装apk,适配安卓6.0,7.0,8.0
     * 参    数：File file
     * 返回值：无
     */
    private void installingAPK(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //安卓7.0以上需要在在Manifest.xml里的application里，设置provider路径
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getActivity(), "cn.tasays.www.july", new File(file.getPath()));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }



    //加载首页
    public void initView(View view)
    {
        final WebView webView = (WebView) view.findViewById(R.id.index_webveiw);
        webView.getSettings().setJavaScriptEnabled(true);//开启javascript
        webView.getSettings().setDomStorageEnabled(true);

        webView.loadUrl(vueUrl+"/");
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

    //重写返回按钮
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

    //检测更新
    public void checkUpdate()
    {
        //获得app的版本号
        int versionCode = (int) getVersionCode(getActivity().getApplicationContext());

        //访问api查询是否存在版本更新
        String checkUpdateApi = apiUrl+"/api/checkVersion";
        com.yanzhenjie.nohttp.rest.Request<String> req = NoHttp.createStringRequest(checkUpdateApi);
        req.add("versionCode",versionCode);

        ((MainActivity)getActivity()).request(0,req,new OnResponseListener<String>(){
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, com.yanzhenjie.nohttp.rest.Response<String> response) {
                String result = response.get();

                if(response.responseCode()==200){//获得成功

                    JsonObject jsonObject = (JsonObject) new JsonParser().parse(result);
                    String apkUrl = jsonObject.get("version").getAsJsonObject().get("url").getAsString();//新版本apk下载地址
                    int versionCode = jsonObject.get("version").getAsJsonObject().get("versionCode").getAsInt();//新版apk版本
                    showUpdateDialog(apkUrl);
                }
            }

            @Override
            public void onFailed(int what, com.yanzhenjie.nohttp.rest.Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    //展示弹出更新提示
    public void showUpdateDialog(final String apkUrl)
    {
        //测试直接弹出
        new AlertView("通知", "发现新版是否更新?", null, null, new String[]{"以后再说", "立即更新"}, getActivity(), AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                switch (position) {
                    case 1:
                        progressDialog.show();//将ProgessDialog显示出来
                        download(apkUrl);//开始下载
                        break;
                }
            }
        }).show();
    }

    //获得当前程序包版本号
    public static int getVersionCode(Context ctx)
    {
        // 获取packagemanager的实例
        int version = 0;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            //getPackageName()是你当前程序的包名
            PackageInfo packInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
            version = packInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }
}
