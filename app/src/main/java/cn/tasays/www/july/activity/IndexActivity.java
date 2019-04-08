package cn.tasays.www.july.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.Toast;

import cn.tasays.www.july.R;
import cn.tasays.www.july.fragment.IndexFragment;


public class IndexActivity extends BaseActivity {

    private long mExitTime;

    private IndexFragment indexFm = new IndexFragment();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.index);
    }
}
