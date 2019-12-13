package com.rocky.common.base;

import android.app.Application;
import android.content.Context;

import com.rocky.common.net.NetWork;

/**
 * @author
 * @date 2019/12/12.
 * description：
 */
public class BaseApplication extends Application {
    protected static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        new NetWork.Builder().baseUrl("https://www.jingjing.shop/appserver/").build();

    }

    public static Context getContext() {
        return context;
    }
}
