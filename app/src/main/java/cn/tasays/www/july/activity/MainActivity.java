package cn.tasays.www.july.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.tasays.www.july.R;
import cn.tasays.www.july.fragment.BookFragment;
import cn.tasays.www.july.fragment.HappyFragment;
import cn.tasays.www.july.fragment.IndexFragment;
import cn.tasays.www.july.fragment.UserFragment;

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
    private long mExitTime;
    private String myFragment = "";//保存当前选中的是哪个fragment用以判断

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
        myFragment = "index";

        ImageView imageView = findViewById(R.id.menu_index_img);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.index_current));
        TextView textView = findViewById(R.id.menu_index_text);
        setCurrentStyle(textView);

        //添加activity到栈中
        add(MainActivity.this);
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
        ImageView imageView;
        TextView textView;
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

                myFragment = "index";

                clearStyle();
                imageView = findViewById(R.id.menu_index_img);
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.index_current));
                textView = findViewById(R.id.menu_index_text);
                setCurrentStyle(textView);
                break;

            case R.id.menu_happy:  //happy
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(happyfm)
                        .hide(indexfm)
                        .hide(bookfm)
                        .hide(userfm)
                        .commit();

                myFragment = "happy";

                clearStyle();
                imageView = findViewById(R.id.menu_happy_img);
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.woman_current));
                textView = findViewById(R.id.menu_happy_text);
                setCurrentStyle(textView);
                break;

            case R.id.menu_book: //图书
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(bookfm)
                        .hide(indexfm)
                        .hide(happyfm)
                        .hide(userfm)
                        .commit();

                myFragment = "book";

                clearStyle();
                imageView = findViewById(R.id.menu_book_img);
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.book_current));
                textView = findViewById(R.id.menu_new_text);
                setCurrentStyle(textView);
                break;

            case R.id.menu_me://我的
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(userfm)
                        .hide(indexfm)
                        .hide(happyfm)
                        .hide(bookfm)
                        .commit();

                myFragment = "me";

                clearStyle();
                imageView = findViewById(R.id.menu_me_img);
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.me_current));
                textView = findViewById(R.id.menu_me_text);
                setCurrentStyle(textView);
                break;
        }
    }

    //监听按钮返回事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean ret = false;
        ret = activityParseOnkey(keyCode,event);
       /* if (!ret) {
            ret = indexfm.onKeyDown(keyCode,event);  //这里的mCurFragment是我们前的Fragment
        }*/
        return ret;
    }

    private boolean activityParseOnkey(int keyCode,KeyEvent event) {
        boolean ret = false;
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                break;
            //回退操作
            case KeyEvent.KEYCODE_BACK:
                ret = indexfm.onKeyDown(keyCode,event);
                break;
        }
        return ret;
    }

    //设置选中按钮current样式
    public void setCurrentStyle(TextView textView)
    {
        textView.setTextColor(getResources().getColor(R.color.base_blue));
    }

    //清除其他按钮样式
    public void clearStyle()
    {
        //重新设置字体颜色
        List<TextView> text_list = new ArrayList<TextView>();
        text_list.add((TextView) findViewById(R.id.menu_me_text));
        text_list.add((TextView) findViewById(R.id.menu_index_text));
        text_list.add((TextView) findViewById(R.id.menu_new_text));
        text_list.add((TextView) findViewById(R.id.menu_happy_text));

        for (TextView item:text_list) {
            item.setTextColor(getResources().getColor(R.color.gray));
        }

        //添加需要修改的图片到数组
        List<BaseImg> img_list = new ArrayList<BaseImg>();
        img_list.add(createBaseImgObj((ImageView) findViewById(R.id.menu_happy_img),getResources().getDrawable(R.drawable.woman)));
        img_list.add(createBaseImgObj((ImageView) findViewById(R.id.menu_index_img),getResources().getDrawable(R.drawable.index)));
        img_list.add(createBaseImgObj((ImageView) findViewById(R.id.menu_book_img),getResources().getDrawable(R.drawable.book)));
        img_list.add(createBaseImgObj((ImageView) findViewById(R.id.menu_me_img),getResources().getDrawable(R.drawable.me)));

        //重新设置图片样式
        for(BaseImg item:img_list) {
            item.getImageView().setImageDrawable(item.getBaseImg());
        }
    }

    //创建baseimg 对象
    public BaseImg createBaseImgObj(ImageView imageView,Drawable drawable)
    {
        BaseImg obj = new BaseImg();
        obj.setImageView(imageView);
        obj.setBaseImg(drawable);
        return  obj;
    }

}

//默认图片样式
class BaseImg{
    public Drawable getBaseImg() {
        return baseImg;
    }

    public void setBaseImg(Drawable base_img) {
        this.baseImg = base_img;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    private Drawable baseImg;
    private ImageView imageView;
}
