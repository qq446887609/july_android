package cn.tasays.www.july.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.tasays.www.july.R;
import cn.tasays.www.july.model.Result;
import cn.tasays.www.july.model.User;
import cn.tasays.www.july.model.article;
import cn.tasays.www.july.module.CountDownButton;

public class SignStepThreeActivity extends BaseActivity {

    private String key;//用户注册需要的key
    private String password;//用户密码
    private String validate_code;//短信验证码
    private String name;//用户昵称
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);

        Intent intent = getIntent();
        key = intent.getStringExtra("key");//获得signStepTwo 用户输入图片验证码后api返回的key

        //sign listener
        TextView sign_view = findViewById(R.id.sign_btn);
        sign_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign();
            }
        });

        //jump login
        jump_login();
    }

    private void jump_login()
    {
        TextView jump_txt_view = (TextView) findViewById(R.id.jump_login);

        jump_txt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignStepThreeActivity.this,LoginActivity.class));
            }
        });
    }

    private void sign()
    {
       if (checkSignData()) {
           //发起注册
           postSign();
       }
    }


    /**
     * 验证注册数据
     */
    private Boolean checkSignData()
    {
        EditText name_input = (EditText)findViewById(R.id.sign_nickname);
        name = name_input.getText().toString();

        EditText code_input = (EditText)findViewById(R.id.sign_code);
        validate_code = code_input.getText().toString();

        EditText password_input = (EditText)findViewById(R.id.sign_password);
        password = password_input.getText().toString();

        if(validate_code.equals("")) {
            showToast("验证码不能为空");
            return false;
        }

        if(name.equals("")){
            showToast("昵称不能为空");
            return  false;
        }

        if(password.equals("")) {
            showToast("密码不能为空");
            return false;
        }

       return  true;
    }

    private void postSign()
    {
        String postUrl = "http://www.tasays.cn/api/users";//接口连接
        Request<String> req = NoHttp.createStringRequest(postUrl, RequestMethod.POST);//创建post
        req.add("verification_code",validate_code);//短信验证码
        req.add("verification_key",key);//用户注册需要的key
        req.add("password",password);
        req.add("name",name);

        request(4, req, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
                progressDialog = new ProgressDialog(SignStepThreeActivity.this);
                progressDialog.setMessage("注册中...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                progressDialog.dismiss();
                String result = response.get();
                Gson gson = new Gson();

                if (response.responseCode()==201) {
                    showToast("注册成功");
                    User user = gson.fromJson(result,User.class);
                    //todo
                    startActivity(new Intent(SignStepThreeActivity.this,MainActivity.class));
                } else {
                    Result resultModel = gson.fromJson(result,Result.class);
                    showToast(resultModel.getMessage());
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                progressDialog.dismiss();
                showToast("注册失败");
            }

            @Override
            public void onFinish(int what) {

            }
        });

    }


    /**
     * 发送验证码api
    public void send_validate_code(String phone)
    {
        //new Thread(smsApi).start();
    }


  Runnable smsApi = new Runnable() {
        @Override
        public void run() {
            Form form_body = new Form();

            form_body.add("phone",phone);
            //form_body.add("api_token","");apitoken  防止其他人盗用

            try{

                Request request = Bridge
                        .post("http://www.tasays.cn/july/public/api/sms")
                        .body(form_body)
                        .request();

                Response response = request.response();//返回结果

                Message message = new Message();

                if (response.isSuccess())//status 200
                {
                    JSONObject jsonObject = response.asJsonObject();

                    String code = jsonObject.get("code").toString();

                    if(code.equals("0"))
                    {
                        message.what = 0;//成功
                    }
                    else
                    {
                        message.what = 1;
                        message.obj = jsonObject.get("message");
                    }
                }
                else
                {
                    message.what = 1;
                    message.obj = "发送短信失败";
                }

                smsHandler.sendMessage(message);


            }catch (Exception e)
            {
                System.out.println(e);
            }
        }
    };

    //发送短信回调
    private Handler smsHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            //判断返回结果

            if(msg.what == 0)
            {
                //发送验证码请求成功后调用view倒计时
                countDownButton.start();
            }
            else
            {
                showToast(msg.obj.toString());
            }
        }
    };

    //发起登录
    private void postSign()
    {
        //new Thread(signApi).start();
    }

    Runnable signApi = new Runnable() {

        @Override
        public void run() {

            Form form = new Form();
            form.add("phone",phone);
            form.add("password",password);
            form.add("verifyCode",validate_code);

            try {

                Request request = Bridge
                        .post("http://www.tasays.cn/july/public/api/register")
                        .body(form)
                        .request();

                Response response = request.response();

                Message message = new Message();

                if(response.isSuccess())//请求成功
                {
                    JSONObject jsonObject = response.asJsonObject();

                    String code = jsonObject.get("code").toString();

                    if(code.equals("0"))
                    {
                        message.what = 0;//成功
                        message.obj = jsonObject.get("data");//用户数据
                    }
                    else
                    {
                        message.what = 1;//注册失败
                        message.obj = jsonObject.get("message");
                    }
                }
                else
                {
                    //注册失败
                    message.what = 1;
                    message.obj = "注册失败";
                }

                signHandle.sendMessage(message);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

    *//**
     * sign handle
     *//*
    private Handler signHandle = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0)//注册成功
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
                startActivity(new Intent(SignActivity.this,MainActivity.class));
            }
            else //注册失败
            {
                //关闭dialog
                progressDialog.dismiss();

                showToast(msg.obj.toString());
            }
        }
    };

    //发送验证码的生命周期
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!countDownButton.isFinish()) {
            countDownButton.cancel();
        }
    }*/

    //子线程中调用网络请求
//    private void test_okhttp()
//    {
//        System.out.println("666666666666666666666666666666666");
//
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    // 创建okhttp实例
//                    OkHttpClient client = new OkHttpClient();
//
//                    // 创建request对象
//
//                    //Request request = new Request.Builder().build();//.build() 方法前可以添加许多方法 参与构造 如.url().build()
//
//
//                    //get方式请求百度-----------------
//
//                    Request request = new Request.Builder()
//                            .url("http://www.baidu.com")
//                            .build();
//
//                    //创建一个call对象 调用 execute() 方法发送请求并获得响应
//                    Response response = client.newCall(request).execute();
//
//
//                    //post 方式请求百度----------------
//
//                    //先构建表单
//                    RequestBody request_body = new FormBody.Builder()
//                            .add("user","admin")
//                            .add("pass","admin")
//                            .build();
//
//                    //调用post方法 传入body
//                    Request request2 = new Request.Builder()
//                            .url("http://www.baidu.com")
//                            .post(request_body)
//                            .build();
//
//                    //创建一个call对象 调用 execute() 方法发送请求并获得响应
//                    Response response2 = client.newCall(request2).execute();
//
//                    String data = response2.body().string();
//
//                    //System.out.println(data);
//
//
//
//                    //获得article 接口中数据并且解析
//                    Request request_art = new Request.Builder()
//                            .url("http://cx.lo.cn/july/public/api/articles/0/20")
//                            .build();
//
//                    Response response_art = client.newCall(request_art).execute();
//
//                    String response_data = response_art.body().string();
//
//                    //解析json
//                    parseJson(response_data);
//
//                } catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    //解析json数据
//    private void parseJson(String data)
//    {
//        System.out.println(data);
//
//        //实例话gson类
//        Gson gson = new Gson();
//
//        //article art = gson.fromJson("{'title':'你好'}",article.class);
//
//        //System.out.println(art.getTitle());
//
//
//        //json 数组 需要借助 typeToken 将期望解析城的数据传入fromJson()方法
//        List<article> article_list = gson.fromJson("[{'title':'cx'},{'title':'hehe'}]",new TypeToken<List<article>>(){}.getType());
//
//        //循环输出数据
//        for(article item:article_list)
//        {
//            Log.d("title","title is"+item.getTitle());
//        }
//        System.out.println("77777777777777");
//    }

}
