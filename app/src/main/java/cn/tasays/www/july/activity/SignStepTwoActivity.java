package cn.tasays.www.july.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.Iterator;

import cn.tasays.www.july.R;
import cn.tasays.www.july.model.Captcha;
import cn.tasays.www.july.model.Result;
import cn.tasays.www.july.model.VerificationCodes;

/**
 * 获得填写获得的图片验证码 发送手机短信验证码
 */

public class SignStepTwoActivity extends BaseActivity{

    private String captchaKey = "";//用以获得短信验证码的key
    private String phone = ""; //用户输入手机号 只用以再次获得图片验证码
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_step2);

        //获得上一页获得验证码图片码及key
        Intent intent = getIntent();

        captchaKey = intent.getStringExtra("captcha_key");

        phone = intent.getStringExtra("phone");

        String base64 = intent.getStringExtra("base64_image");//获得signStepOne 获得用户发送的图片验证码

        initCaptchaImage(base64);

        initListener();//点击事件
    }

    //显示base64验证码图片
    public void initCaptchaImage(String base64)
    {
        ImageView imageView = (ImageView) findViewById(R.id.image_code);

        base64 = base64.replace("data:image/jpeg;base64,","");

        //显示图片验证码
        Bitmap decodedByte = base64ToBitmap(base64);//base64图片转bitmap

        imageView.setImageBitmap(decodedByte);
    }

    //监听点击事件
    public void initListener()
    {
        TextView change_btn = (TextView) findViewById(R.id.captcha_change);

        Button verify_commit_btn = findViewById(R.id.verifi_commit_btn);

        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeCaptchaImage();//换一张图片
            }
        });

        verify_commit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postVerify();//获得短信验证码并跳转
            }
        });
    }

    //换一张图片
    public void changeCaptchaImage()
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
                progressDialog = new ProgressDialog(SignStepTwoActivity.this);
                progressDialog.setMessage("提交中...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            public void onSucceed(int what, Response<String> response) {

                progressDialog.dismiss();
                String result = response.get();

                if (response.responseCode()==201) {
                    Gson gson = new Gson();
                    Captcha captcha = gson.fromJson(result,Captcha.class);//转换api数据为对象
                    initCaptchaImage(captcha.getCaptcha_image_content());//设置图片
                    captchaKey = captcha.getCaptcha_key();//设置key

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

    //获得短信验证码并跳转
    public void postVerify()
    {
        EditText editText = findViewById(R.id.captcha_code);
        String captchaCode = editText.getText().toString();//用户输入验证码
        //请求链接
        String postUrl = "http://www.tasays.cn/api/verificationCodes";
        //创建请求
        Request<String> req = NoHttp.createStringRequest(postUrl,RequestMethod.POST);
        req.add("captcha_code",captchaCode);
        req.add("captcha_key",captchaKey);

        request(3,req,new OnResponseListener<String>(){

            @Override
            public void onStart(int what) {
                progressDialog = new ProgressDialog(SignStepTwoActivity.this);
                progressDialog.setMessage("提交中...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            public void onSucceed(int what, Response<String> response) {

                progressDialog.dismiss();//关闭遮罩层
                String result = response.get();//获得返回结果
                Gson gson = new Gson();

                if (response.responseCode()==201) {//成功
                    //获得返回数据 key
                    VerificationCodes verificationCodes = gson.fromJson(result,VerificationCodes.class);
                    //跳转用户注册页面
                    Intent intent = new Intent(SignStepTwoActivity.this,SignStepThreeActivity.class);
                    intent.putExtra("key",verificationCodes.getKey());//获得用以注册账号手机短信key
                    startActivity(intent);

                } else {//失败
                    Result resultModel = gson.fromJson(result,Result.class);//转换api数据为对象
                    showToast(resultModel.getMessage());
                    //失败后再次更换图片
                    changeCaptchaImage();
                }

            }

            @Override
            public void onFailed(int what, Response<String> response) {
                progressDialog.dismiss();
                showToast("请求失败");
                //失败后再次更换图片
                changeCaptchaImage();
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }
}
