package com.vslk.lbgx.ui.common.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_framework.util.util.NetworkUtils;

/**
 * Created by xujiexing on 14-4-9.
 */
public class NetworkErrorFragment extends AbsStatusFragment {
    private boolean isClickReload = true;

    public static NetworkErrorFragment newInstance(boolean isClickReload) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isClickReload", isClickReload);
        NetworkErrorFragment fragment = new NetworkErrorFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_network_error, container, false);
        if (savedInstanceState != null) {
            isClickReload = savedInstanceState.getBoolean("isClickReload", true);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                isClickReload = bundle.getBoolean("isClickReload", true);
            } else {
                isClickReload = true;
            }
        }
        if (isClickReload)
            view.setOnClickListener(mSelfListener);
        return view;
    }

    private View.OnClickListener mSelfListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (!NetworkUtils.isNetworkStrictlyAvailable(getActivity())) {
                checkNetToast();
                return;
            }

            if (mLoadListener != null) {
                mLoadListener.onClick(v);
            }
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
