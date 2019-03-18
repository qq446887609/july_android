package cn.tasays.www.july.activity;

/**
 * 注册第一步, 获得图片验证码
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import cn.tasays.www.july.R;
import cn.tasays.www.july.model.Captcha;
import cn.tasays.www.july.model.Result;

public class SignStepOneActivity extends BaseActivity {

    private EditText phone_text;
    private String phone = ""; //用户输入的手机号
    private Button commit_btn;
    private TextView jump_login;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_step1);
        initViewBtn();
        initListener();//点击事件
    }

    //初始化view 按钮
    public void initViewBtn()
    {
        phone_text = (EditText) findViewById(R.id.captcha_phone);//用户手机号edit_text
        commit_btn = (Button) findViewById(R.id.captcha_commit_btn);//提交按钮
        jump_login = (TextView) findViewById(R.id.captcha_jump_login);//跳转登录页
    }

    /**
     * 点击事件
     */
    public void  initListener()
    {
        //点击提交事件
        commit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkData()) //验证数据通过执行获得API
                {
                    postCaptcha();//获得验证码
                }

            }
        });

        //点击跳转
        jump_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignStepOneActivity.this,LoginActivity.class));
            }
        });

    }

    /**
     * 验证数据
     */
    public boolean checkData()
    {
        phone = phone_text.getText().toString();

        if (phone.equals(""))
        {
            showToast("请填写手机号");

            return  false;
        }

        return  true;
    }

    /**
     * 获得图片验证码
     */
    public void postCaptcha()
    {
        //请求链接
        String postUrl = "http://www.tasays.cn/api/captchas";
        //创建请求
        Request<String> req = NoHttp.createStringRequest(postUrl, RequestMethod.POST);
        //请求参数
        req.add("phone",phone);

        request(2, req, new OnResponseListener<String>() {

            @Override
            public void onStart(int what) {
                progressDialog = new ProgressDialog(SignStepOneActivity.this);
                progressDialog.setMessage("提交中...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            public void onSucceed(int what, Response<String> response) {

                progressDialog.dismiss();
                String result = response.get();

                if (response.responseCode()==201)
                {
                    Gson gson = new Gson();
                    Captcha captcha = gson.fromJson(result,Captcha.class);//转换api数据为对象
                    //跳转到输入验证码activity
                    Intent intent = new Intent(SignStepOneActivity.this,SignStepTwoActivity.class);
                    intent.putExtra("base64_image",captcha.getCaptcha_image_content());//图片base64位
                    intent.putExtra("captcha_key",captcha.getCaptcha_key());//
                    intent.putExtra("phone",phone);//传递手机号过去 用以实现换一张图片
                    startActivity(intent);

                } else {
                    Gson gson = new Gson();
                    Result resultModel = gson.fromJson(result,Result.class);//转换api数据为对象
                    showToast(resultModel.getMessage());
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                progressDialog.dismiss();
                showToast("获得失败");
            }

            @Override
            public void onFinish(int what) {

            }
        });

    }

}
