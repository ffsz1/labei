package com.vslk.lbgx.ui.common.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_framework.http_image.image.ImageConfig;
import com.tongdaxing.xchat_framework.http_image.image.ImageManager;
import com.tongdaxing.xchat_framework.http_image.image.RecycleImageView;


/**
 * Created by xujiexing on 14-4-9.
 */
public class NoDataFragment extends AbsStatusFragment {
    private static final String TIP_PARAM = "TIP_PARAM";
    private static final String DRAWABLE_PARAM = "DRAWABLE_PARAM";
    private CharSequence mTip;
    private int mDrawable;

    public static NoDataFragment newInstance() {
        return new NoDataFragment();
    }

    public static NoDataFragment newInstance(int drawable, CharSequence tips) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(TIP_PARAM, tips);
        bundle.putInt(DRAWABLE_PARAM, drawable);
        NoDataFragment fragment = new NoDataFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static NoDataFragment newInstance(int drawable, CharSequence tips, boolean isClickReload) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(TIP_PARAM, tips);
        bundle.putInt(DRAWABLE_PARAM, drawable);
        bundle.putBoolean("isClickReload", isClickReload);
        NoDataFragment fragment = new NoDataFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_no_data, container, false);
        boolean isClickReload = true;
        if (savedInstanceState != null) {
            mTip = savedInstanceState.getCharSequence(TIP_PARAM);
            mDrawable = savedInstanceState.getInt(DRAWABLE_PARAM, R.drawable.content_empty);
            isClickReload = savedInstanceState.getBoolean("isClickReload", true);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                mTip = bundle.getCharSequence(TIP_PARAM);
                mDrawable = bundle.getInt(DRAWABLE_PARAM, R.drawable.content_empty);
                isClickReload = bundle.getBoolean("isClickReload", true);
            } else {
                mTip = getString(
                        R.string.no_list_data);
                mDrawable = R.drawable.content_empty;
                isClickReload = true;
            }
        }
        if (isClickReload)
            view.setOnClickListener(this.mSelfListener);
        if (mTip == null || mTip.length() <= 0) {
            mTip = getString(R.string.no_list_data);
        }

        if (mDrawable <= 0) {
            mDrawable = R.drawable.content_empty;
        }
        RecycleImageView imageView = (RecycleImageView) view.findViewById(R.id.no_data_icon);
        ImageManager.instance().loadImageResource(mDrawable, imageView, ImageConfig.fullImageConfig());
        imageView.setImageDrawable(getResources().getDrawable(mDrawable));
        TextView textView = (TextView) view.findViewById(R.id.no_data_text);
        textView.setText(mTip);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(TIP_PARAM, mTip);
        outState.putInt(DRAWABLE_PARAM, mDrawable);
    }

    private View.OnClickListener mSelfListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

//            if(!NetworkUtils.isNetworkStrictlyAvailable(getActivity())){
//                checkNetToast();
//                return;
//            }


            if (mLoadListener != null)
                mLoadListener.onClick(v);
        }
    };
}
