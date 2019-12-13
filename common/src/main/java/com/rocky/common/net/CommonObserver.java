package com.rocky.common.net;


import com.rocky.common.base.event.LoadingEvent;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author
 * @date 2019/12/6.
 * description：
 */
public abstract class CommonObserver<T> implements Observer<T> {
    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {
        EventBus.getDefault().post(new LoadingEvent(true));

    }

    @Override
    public void onNext(T t) {
        EventBus.getDefault().post(new LoadingEvent(false));
        if (t == null) {
            onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
        }
    }

    @Override
    public void onError(Throwable e) {
        EventBus.getDefault().post(new LoadingEvent(false));

    }

}
