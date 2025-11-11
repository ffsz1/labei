package com.vslk.lbgx.ui.common.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tongdaxing.erban.R;


/**
 * Created by xujiexing on 14-4-9.
 */
public class LoadingFragment extends AbsStatusFragment {
    private static final String TIP_PARAM = "TIP_PARAM";
    private static final String DRAWABLE_PARAM = "DRAWABLE_PARAM";
    private static final String BACKGROUND_COLOR_PARAM = "BACKGROUND_COLOR_PARAM";
    private int mTip;
    private int mDrawable;
    private int mBackgroundColor;

    public static LoadingFragment newInstance() {
        return new LoadingFragment();
    }

    public static LoadingFragment newInstance(int drawable, int tips) {
        Bundle bundle = new Bundle();
        bundle.putInt(TIP_PARAM, tips);
        bundle.putInt(DRAWABLE_PARAM, drawable);
        LoadingFragment fragment = new LoadingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static LoadingFragment newInstance(int drawable, int tips, int backgroundColor) {
        Bundle bundle = new Bundle();
        bundle.putInt(TIP_PARAM, tips);
        bundle.putInt(DRAWABLE_PARAM, drawable);
        bundle.putInt(BACKGROUND_COLOR_PARAM, backgroundColor);
        LoadingFragment fragment = new LoadingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_loading, container, false);
        if (savedInstanceState != null) {
            mTip = savedInstanceState.getInt(TIP_PARAM, R.string.loading);
            mDrawable = savedInstanceState.getInt(DRAWABLE_PARAM, R.drawable.loading_progress);
            mBackgroundColor = savedInstanceState.getInt(BACKGROUND_COLOR_PARAM, getResources().getColor(R.color.common_color_2));
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                mTip = bundle.getInt(TIP_PARAM, R.string.loading);
                mDrawable = bundle.getInt(DRAWABLE_PARAM, R.drawable.loading_progress);
                mBackgroundColor = bundle.getInt(BACKGROUND_COLOR_PARAM, getResources().getColor(R.color.common_color_2));
            } else {
                mTip = R.string.loading;
            }
        }

        if (mTip > 0) {
            TextView textView = (TextView) view.findViewById(R.id.loading_text);
            textView.setText(getString(mTip));
            textView.setVisibility(View.VISIBLE);
        }
        if (mDrawable > 0) {
            ImageView imageView = (ImageView) view.findViewById(R.id.loadingIv);
            imageView.setImageResource(mDrawable);
            imageView.setVisibility(View.VISIBLE);
        }
        if (mBackgroundColor > 0) {
            view.setBackgroundColor(mBackgroundColor);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TIP_PARAM, mTip);
        outState.putInt(DRAWABLE_PARAM, mDrawable);
        outState.putInt(BACKGROUND_COLOR_PARAM, mBackgroundColor);
    }
}
