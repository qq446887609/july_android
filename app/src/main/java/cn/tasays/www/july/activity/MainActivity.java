package cn.tasays.www.july.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import cn.tasays.www.july.R;
import cn.tasays.www.july.fragment.HappyFragment;
import cn.tasays.www.july.fragment.IndexFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    //fragement切换
    protected LinearLayout indexll;
    protected LinearLayout happyll;
    protected IndexFragment indexfm = new IndexFragment();//首页fm
    protected HappyFragment happyfm =  new HappyFragment(); //新闻资讯

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1.初始化 绑定onclick
        initView();

        //2.初始化fragment 默认展示主页fragment
        //获取管理类型
        this.getSupportFragmentManager()
                //fragment都是以事物的方式添加 删除的
                .beginTransaction()
                .add(R.id.container_content,indexfm)//默认展示主页面
                //.replace(R.id.container_content,indexfm)//replace 删除所有fragment 后添加指定1个
                .add(R.id.container_content,happyfm)
                .hide(happyfm) //隐藏其他fm
                //事物添加
                .commit();
    }


    //初始化视图
    public  void  initView()
    {
        indexll = (LinearLayout) this.findViewById(R.id.menu_index);
        happyll = (LinearLayout) this.findViewById(R.id.menu_happy);
        indexll.setOnClickListener(this);
        happyll.setOnClickListener(this);
    }

    //重写onclick方法
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.menu_index: //首页
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(indexfm)
                        .hide(happyfm)
                        .commit();
                break;
            case R.id.menu_happy:  //happy
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(happyfm)
                        .hide(indexfm)
                        .commit();
                break;
        }
    }
}
