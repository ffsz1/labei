package com.vslk.lbgx.ui.common.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;

/**
 * Created by xujiexing on 14-7-21.
 */
public abstract class AbsStatusFragment extends Fragment implements IStatusFragment {
    protected View.OnClickListener mLoadListener;

    @Override
    public void setListener(View.OnClickListener listener) {
        this.mLoadListener = listener;
    }


    public void checkNetToast(){

        Toast.makeText(BasicConfig.INSTANCE.getAppContext(), R.string.str_network_not_capable, Toast.LENGTH_SHORT).show();
    }
}
