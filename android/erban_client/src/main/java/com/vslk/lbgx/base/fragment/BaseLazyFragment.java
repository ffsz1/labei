package com.vslk.lbgx.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * <p> 具备懒加载的fragment </p>
 *
 * @author Administrator
 * @date 2017/11/23
 */
public abstract class BaseLazyFragment extends BaseFragment {

    private boolean mIsInitView;
    private boolean mIsLoaded;
    /** 当前fragment是否还活着 */
    private boolean mIsDestroyView = true;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIsInitView = true;
        if (getUserVisibleHint() && !mIsLoaded && mIsDestroyView) {
            mIsDestroyView = false;
            onLazyLoadData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mIsInitView && !mIsLoaded && mIsDestroyView) {
                mIsDestroyView = false;
                mIsLoaded = true;
                onLazyLoadData();
            }
        } else {
            mIsLoaded = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsDestroyView = true;
    }

    /** 数据懒加载 */
    protected abstract void onLazyLoadData();
}
