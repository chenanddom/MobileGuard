package com.mobileguard.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;


/**
 * Created by chendom on 2017/4/7 0007.
 */

public abstract class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initData();
        setContentView(getLayout());
        initView();
        setListener();
    }
    public abstract int getLayout();
    public abstract void initData();
    public abstract void initView();
    public abstract void setListener();
}
