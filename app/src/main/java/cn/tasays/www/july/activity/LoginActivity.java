package cn.tasays.www.july.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;

import cn.tasays.www.july.R;
import cn.tasays.www.july.api.BaseAPi;
import cn.tasays.www.july.model.Result;
import cn.tasays.www.july.model.User;

public class LoginActivity extends BaseActivity {

    private EditText phone_input;
    private EditText password_input;
    private Button login_btn;
    private String phone;
    private String password;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //auto login
        auto_login();
        //listen_login
        listen_login();
        //jump sign()
        jump_sign_listen();
        //other login
        other_login();

        //添加activity到栈中
        add(LoginActivity.this);
    }

    private void jump_sign_listen() {
        TextView jump_txt_view = (TextView) findViewById(R.id.jump_sign);

        jump_txt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignStepOneActivity.class));
            }
        });
    }

    //第三方登录
    private void other_login()
    {
        ImageView qq_view = (ImageView) findViewById(R.id.qq_login);
        ImageView wb_view = (ImageView) findViewById(R.id.wb_login);

        final Toast toast = (Toast) Toast.makeText(LoginActivity.this,"暂未开放",Toast.LENGTH_SHORT);

        qq_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast.show();
            }
        });

        wb_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast.show();
            }
        });
    }

    //监听登录
    private void listen_login()
    {
        login_btn = (Button)findViewById(R.id.login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phone_input = (EditText)findViewById(R.id.phone);
                phone = phone_input.getText().toString();

                password_input = (EditText)findViewById(R.id.password);
                password = password_input.getText().toString();

                login();
            }
        });
    }

    //自动登录
    private void auto_login()
    {
        //sharedPreferences 中获得 保存账号信息
        SharedPreferences user_data = getSharedPreferences("user_data",MODE_PRIVATE);

        String access_token = user_data.getString("access_token","");

        if(!access_token.isEmpty())
        {
            //发起登录
            postLogin(false);
        }
    }

    //登录
    private void login()
    {
        //校验参数
        String message = checkLoginData();

        if(!message.isEmpty())
        {
            showToast(message);
            return;
        }

        //发起登录
        postLogin(true);
    }

    //验证数据
    private String checkLoginData()
    {
        if(phone.isEmpty())
        {
            return "手机号不能为空";
        }

        if(password.isEmpty())
        {
            return "密码不能为空";
        }

        return  "";
    }

    //开始登录
    private void postLogin(final boolean showProcess)
    {
        //请求链接

        String postUrl =  apiUrl+"/api/authorizations";

        //创建请求
        Request<String> req = NoHttp.createStringRequest(postUrl, RequestMethod.POST);

        //利用队列去添加消息请求
        //使用request对象添加上传的对象添加键与值,post方式添加上传的数据

        req.add("username",phone);
        req.add("password",password);

        //request 继承baseActivity request方法 封装了nohttp 单例模式
        request(1, req, new OnResponseListener<String>() {

            @Override
            public void onStart(int what) {

                if(showProcess){
                    //显示进度条
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("登录中....");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            }

            @Override
            public void onSucceed(int what, com.yanzhenjie.nohttp.rest.Response<String> response) {

                if(showProcess){
                    //关闭dialog
                    progressDialog.dismiss();
                }

                String result = response.get();

                if(response.responseCode()==201){//登录成功

                    Gson gson = new Gson();
                    User user = gson.fromJson(result,User.class);

                    showToast("登录成功");

                    //登录成功保存数据用户数据信息
                    //sharedPreferences 中获得 保存账号信息
                    SharedPreferences.Editor editor = getSharedPreferences("user_data",MODE_PRIVATE).edit();
                    editor.putString("access_token",user.getAccess_token());
                    editor.apply();

                    //跳转首页 mainactivity webview vue页面
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
                //展示错误信息
                else if(response.responseCode()==401){

                    Gson gson = new Gson();
                    Result resultModel = gson.fromJson(result,Result.class);
                    showToast(resultModel.getMessage());
                }
            }

            @Override
            public void onFailed(int what, com.yanzhenjie.nohttp.rest.Response<String> response) {

                //        这里还有很多错误类型，可以看demo：
                //        https://github.com/yanzhenjie/NoHttp
                if(showProcess){
                    //关闭dialog
                    progressDialog.dismiss();
                }
                showToast("登录失败");
            }

            @Override
            public void onFinish(int what) {
                // 这里可以dismiss()上面show()的wait dialog。
            }
        });


    }

    /* 原android 默认方法执行登录

    private void postLogin()
    {
       //网络请求 必须 构建子线程
        new Thread(loginApi).start();

    }

    *//**
     * 发起登录
     *//*
    Runnable loginApi = new Runnable() {
        @Override
        public void run() {

            Form post_form = new Form()
                    .add("username",phone)
                    .add("password",password);

            try {

                Request request = Bridge
                        .post("http://www.tasays.cn/api/authorizations")
                        .body(post_form)
                        .request();

                Response response = request.response();

                //返回回调信息
                Message message = new Message();

                if (response.isSuccess())//如果调用api接口成功
                {
                    //http status 200-300
                    JSONObject jsonObject = response.asJsonObject();

                    String code = jsonObject.get("code").toString();

                    if(code.equals("0"))
                    {
                        message.what = 0;//成功
                        message.obj = jsonObject.get("data");
                    }
                    else
                    {
                        message.what = 1;//访问成功 登录失败

                        try {
                            message.obj = jsonObject.get("message");
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            message.obj = "登录失败";
                        }
                    }
                }
                else
                {
                    message.what = 2;//访问失败
                }

                login_handler.sendMessage(message);//把message对象发送给handler
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };

    *//**
     * 处理登录回调
     *//*
    private Handler login_handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {

            //处理result结果
            if(msg.what==0) //登录成功
            {
                JSONObject obj = (JSONObject) msg.obj;

                //登录成功保存数据用户数据信息
                //sharedPreferences 中获得 保存账号信息
                SharedPreferences.Editor editor = getSharedPreferences("user_data",MODE_PRIVATE).edit();
                try {
                    editor.putString("phone",obj.get("phone").toString());
                    editor.putString("password",password);
                    editor.putString("api_token",(obj.get("api_token").toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor.apply();

                //跳转首页 mainactivity webview vue页面
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
            else if(msg.what==1)//其他错误
            {
                //关闭dialog
                progressDialog.dismiss();
                showToast(msg.obj.toString());
            }
            else //未知错误
            {
                //关闭dialog
                progressDialog.dismiss();
                showToast("登录失败");
            }
        }
    };*/

}
