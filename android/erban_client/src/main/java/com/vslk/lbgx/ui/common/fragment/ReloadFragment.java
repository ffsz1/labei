package com.vslk.lbgx.ui.common.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_framework.util.util.NetworkUtils;
import com.tongdaxing.xchat_framework.util.util.log.MLog;


/**
 * Created by xujiexing on 14-4-9.
 */
public class ReloadFragment extends AbsStatusFragment {
    private static final String TIP_PARAM = "TIP_PARAM";
    private static final String DRAWABLE_PARAM = "DRAWABLE_PARAM";
    private int mTip;
    private int mDrawable;

    public static ReloadFragment newInstance() {
        return new ReloadFragment();
    }

    public static ReloadFragment newInstance(int drawable, int tips) {
        Bundle bundle = new Bundle();
        bundle.putInt(TIP_PARAM, tips);
        bundle.putInt(DRAWABLE_PARAM, drawable);
        ReloadFragment fragment = new ReloadFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_reload, container, false);

        if (savedInstanceState != null) {
            mTip = savedInstanceState.getInt(TIP_PARAM, R.string.click_screen_reload);
            mDrawable = savedInstanceState.getInt(DRAWABLE_PARAM, R.drawable.icon_error);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                mTip = bundle.getInt(TIP_PARAM, R.string.click_screen_reload);
                mDrawable = bundle.getInt(DRAWABLE_PARAM, R.drawable.icon_error);
            } else {
                mTip = R.string.click_screen_reload;
                mDrawable = R.drawable.icon_error;
            }
        }
        if(mTip <= 0){
            mTip = R.string.click_screen_reload;
        }

        if(mDrawable <= 0){
            mDrawable = R.drawable.icon_error;
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.reload_icon);
        imageView.setImageDrawable(getResources().getDrawable(mDrawable));
        TextView textView = (TextView) view.findViewById(R.id.error_text);
        textView.setText(getString(mTip));
        view.setOnClickListener(mSelfListener);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TIP_PARAM, mTip);
        outState.putInt(DRAWABLE_PARAM, mDrawable);
    }

    private View.OnClickListener mSelfListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!checkActivityValid())
                return;
            if(!NetworkUtils.isNetworkStrictlyAvailable(getActivity())){
                checkNetToast();
                return;
            }

            if (mLoadListener != null){
                mLoadListener.onClick(v);
            }
        }
    };

    @TargetApi(17)
    protected boolean checkActivityValid() {
        if (getActivity()  == null) {
            MLog.warn(this, "Fragment " + this + " not attached to Activity");
            return false;
        }

        if(getActivity().isFinishing()){
            MLog.warn(this, "activity is finishing");
            return false;
        }

        if(Build.VERSION.SDK_INT >= 17 && getActivity().isDestroyed()){
            MLog.warn(this, "activity is isDestroyed");
            return false;
        }
        return true;
    }
}
