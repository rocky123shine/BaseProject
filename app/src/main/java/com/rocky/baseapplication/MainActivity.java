package com.rocky.baseapplication;

import android.view.View;

import com.rocky.baseapplication.test.net.NetActivity;
import com.rocky.baseapplication.test.recyclerview.RvActivity;
import com.rocky.baseapplication.test.screen.TestScreenActivity;
import com.rocky.common.base.BaseActivity;

public class MainActivity extends BaseActivity {


    @Override
    protected int setContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        super.init();

    }


    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.screen:
                startActivity(TestScreenActivity.class);
                break;
            case R.id.net:
                startActivity(NetActivity.class);
                break;
            case R.id.rv:
                startActivity(RvActivity.class);
                break;
        }
    }
}
