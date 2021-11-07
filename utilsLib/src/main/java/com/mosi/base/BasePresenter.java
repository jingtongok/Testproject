package com.mosi.base;


import com.mosi.http.ApiRetrofit;
import com.mosi.http.ApiService;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Author gjt66888
 * Description: Presenter的基类
 * Time 2019/4/13  16:42
 */
public abstract class BasePresenter<V> {

    protected ApiService mApiService = ApiRetrofit.getInstance().getApiService();
    protected V mView;
    //    private CompositeSubscription mCompositeSubscription;
    private CompositeDisposable compositeDisposable;


    public BasePresenter(V view) {
        attachView(view);
    }


    public void attachView(V view) {
        mView = view;
    }

    public void detachView() {
        mView = null;
        onUnsubscribe();
    }

    public V getView() {
        return mView;
    }


    public void addSubscription(Observable<?> observable, DisposableObserver subscriber) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }

        compositeDisposable.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(subscriber));
    }

    public void addSubscribe(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    //RXjava取消注册，以避免内存泄露
    public void onUnsubscribe() {
//        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
//            mCompositeSubscription.unsubscribe();
//        }
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

}