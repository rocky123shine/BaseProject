package com.rocky.common.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rocky.common.base.event.LoadingEvent;
import com.rocky.common.base.widget.LoadDialog;
import com.rocky.common.util.status_bar.MStatusBarCompat;
import com.rocky.common.util.status_bar.StatusBarCompat;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author
 * @date 2019/12/6.
 * description：
 */
public abstract class BaseActivity extends AppCompatActivity implements LifecycleProvider<ActivityEvent> {
    //线程转换
    final ObservableTransformer schedulersTransformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {
            return upstream.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        }
    };
    //生命周期得被观察者
    private final BehaviorSubject<ActivityEvent> lifecyclerSubject = BehaviorSubject.create();
    protected AppCompatActivity activity; //本身对象


    //绑定生命周期事件
    @Override
    @NonNull//androudx 是融合了v4和v7 解决了包引用混乱的问题
    @CheckResult//androidx
    public <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecyclerSubject, event);
    }

    //绑定生命周期
    @Override
    @NonNull
    @CheckResult
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecyclerSubject);
    }

    @Override
    @NonNull
    @CheckResult
    public Observable<ActivityEvent> lifecycle() {
        return lifecyclerSubject.hide();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        lifecyclerSubject.onNext(ActivityEvent.CREATE);
        EventBus.getDefault().register(this);
        if (!_onCreate(savedInstanceState)) {
            finish();
            return;
        }
        setContentView(setContentLayout());
        try {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); // (WidgetSearchActivity是当前的Activity)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } catch (Exception e) {

        } finally {
            setStatusBar(false);
            init();
        }
    }

    //可以不实现
    protected void init() {
    }

    protected abstract int setContentLayout();


    //暴露出去 onCreate
    protected boolean _onCreate(@Nullable Bundle savedInstanceState) {
        return true;
    }

    public void setStatusBar(boolean isMain) {
        if (!isMain) {

            StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FFFFFFFF"), true);
        } else {
            MStatusBarCompat.setStatusBarColor(this, Color.WHITE);

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        lifecyclerSubject.onNext(ActivityEvent.START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecyclerSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lifecyclerSubject.onNext(ActivityEvent.PAUSE);
    }


    @Override
    protected void onStop() {
        lifecyclerSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        lifecyclerSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @SuppressWarnings("unchecked")
    protected <T> ObservableTransformer<T, T> applySchedulers() {

        return schedulersTransformer;
    }

    /**
     * 跳转界面
     */
    protected void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 带参数跳转界面
     */
    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        this.startActivity(intent);
    }

    protected void startActivity(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        this.startActivityForResult(intent, requestCode);
    }

    protected boolean cancel = false;

    //处理点击焦点外部 收起键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!cancel) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
                View v = getCurrentFocus();
                if (isShouldHideInput(v, ev)) {
                    hideSoftInput(v.getWindowToken());

                }
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    //处理加载进度
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dealwithLoadingDialog(LoadingEvent event) {
        if (event.isLoading()) {
            LoadDialog.show(this);
        } else {
            LoadDialog.dismiss(activity);

        }

    }

}
