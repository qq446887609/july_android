package cn.tasays.www.july.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import cn.tasays.www.july.R;
import cn.tasays.www.july.fragment.BookFragment;
import cn.tasays.www.july.fragment.HappyFragment;
import cn.tasays.www.july.fragment.IndexFragment;
import cn.tasays.www.july.fragment.UserFragment;
import cn.tasays.www.july.model.User;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    //fragement切换
    protected LinearLayout indexll;
    protected LinearLayout happyll;
    protected LinearLayout bookll;
    protected LinearLayout userll;
    protected IndexFragment indexfm = new IndexFragment();//首页fm
    protected HappyFragment happyfm =  new HappyFragment(); //新闻资讯
    protected BookFragment  bookfm  =  new BookFragment();//创建bookfragment
    protected UserFragment  userfm  = new UserFragment(); //创建会员中心 fragment

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
                .add(R.id.container_content,bookfm)
                .add(R.id.container_content,userfm)
                .hide(bookfm) //隐藏其他fm
                .hide(happyfm)
                .hide(userfm)
                //事物添加
                .commit();
    }


    //初始化视图 注册事件
    public  void  initView()
    {
        indexll = (LinearLayout) this.findViewById(R.id.menu_index);
        happyll = (LinearLayout) this.findViewById(R.id.menu_happy);
        bookll  = (LinearLayout) this.findViewById(R.id.menu_book);
        userll  = (LinearLayout) this.findViewById(R.id.menu_me);
        indexll.setOnClickListener(this);
        happyll.setOnClickListener(this);
        bookll.setOnClickListener(this);
        userll.setOnClickListener(this);
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
                        .hide(bookfm)
                        .hide(userfm)
                        .commit();
                break;
            case R.id.menu_happy:  //happy
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(happyfm)
                        .hide(indexfm)
                        .hide(bookfm)
                        .hide(userfm)
                        .commit();
                break;
            case R.id.menu_book: //图书
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(bookfm)
                        .hide(indexfm)
                        .hide(happyfm)
                        .hide(userfm)
                        .commit();
                break;
            case R.id.menu_me://我的
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(userfm)
                        .hide(indexfm)
                        .hide(happyfm)
                        .hide(bookfm)
                        .commit();
                break;
        }
    }
}
