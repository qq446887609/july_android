package cn.tasays.www.july.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import cn.tasays.www.july.R;
import cn.tasays.www.july.activity.BaseActivity;
import cn.tasays.www.july.activity.LoginActivity;
import cn.tasays.www.july.activity.MainActivity;
import cn.tasays.www.july.model.Result;
import cn.tasays.www.july.model.User;
import okhttp3.internal.http.RequestLine;

import static android.content.Context.MODE_PRIVATE;

/**
 * user信息
 */

public class UserFragment extends BaseFragment {

    private View view;
    private FragmentTransaction fragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //添加布局文件到容器
        view = inflater.inflate(R.layout.user,container,false);

        init_view(view);

        //添加fragment到栈中
        FragmentManager fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack("index");
        fragmentTransaction.commit();

        return  view;
    }

    /**
     * 初始化用户数据 api请求获得用户信息接口
     */
    public void init_view(View view)
    {
        //获得用户token
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_data",MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token","");

        //请求地址
        String getUrl = apiUrl+"/api/user";

        Request<String> req = NoHttp.createStringRequest(getUrl,RequestMethod.GET);

        req.setHeader("Authorization","Bearer "+token);//添加用户token

        ((MainActivity)getActivity()).request(1, req, new OnResponseListener<String>() {

            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                String result = response.get();

                if(response.responseCode()==200){//获得成功

                    Gson gson = new Gson();
                    User user = gson.fromJson(result,User.class);

                    TextView nameView = getActivity().findViewById(R.id.userName);
                    nameView.setText(user.getName());
                }
                //展示错误信息
                else if(response.responseCode()==401){

                    Gson gson = new Gson();
                    Result resultModel = gson.fromJson(result,Result.class);
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                String result = response.get();
            }

            @Override
            public void onFinish(int what) {

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event) {
        return false;
    }
}
