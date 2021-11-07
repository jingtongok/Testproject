package com.mosi.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mosi.uikit.loadingview.StateView;
import com.mosi.update.R;


/**
 * Author gjt66888
 * Description: Fragment的基类
 * Time 2019/4/13  16:42
 */

public abstract class BaseFragment<T extends BasePresenter> extends LazyLoadFragment {

    protected T mPresenter;
    private View rootView;
    protected StateView mStateView;//用于显示加载中、网络异常，空布局、内容布局
    protected Activity mActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = createPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(provideContentViewId(), container, false);

            mStateView = StateView.inject(getStateViewRoot());
            if (mStateView != null) {
                mStateView.setLoadingResource(R.layout.base_loading);
                mStateView.setRetryResource(R.layout.view_retry);
                mStateView.setEmptyResource(R.layout.view_empty);
            }

            initView(rootView);
            initData();
            initListener();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    /**
     * StateView的根布局，默认是整个界面，如果需要变换可以重写此方法
     */
    public View getStateViewRoot() {
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    /**
     * 初始化一些view
     *
     * @param rootView
     */
    public void initView(View rootView) {
    }

    /**
     * 初始化数据
     */
    public void initData() {

    }

    /**
     * 设置listener的操作
     */
    public void initListener() {

    }

    @Override
    protected void onFragmentFirstVisible() {
        //当第一次可见的时候，加载数据
        loadData();
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {

        userVisibleHint(isVisible);
    }

    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract T createPresenter();

    //得到当前界面的布局文件id(由子类实现)
    protected abstract int provideContentViewId();

    //加载数据
    protected abstract void loadData();

    //获取焦点和市区焦点时调用
    protected abstract void userVisibleHint(boolean isVisible);

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        rootView = null;
    }


}
