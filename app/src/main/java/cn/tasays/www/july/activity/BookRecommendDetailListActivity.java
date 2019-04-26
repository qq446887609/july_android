package cn.tasays.www.july.activity;

/**
 * 图书推荐列表
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BookRecommendDetailListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加activity到栈中
        add(BookRecommendDetailListActivity.this);
    }
}
