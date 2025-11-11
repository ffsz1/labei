package com.vslk.lbgx.room.egg.dialog;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.vslk.lbgx.base.fragment.BaseDialogFragment;
import com.vslk.lbgx.room.egg.adapter.PoundPrizePoolAdapter;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.gift.GiftInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Function:
 * Author: Edward on 2019/5/8
 */
public class PoundPrizePoolDialog extends BaseDialogFragment {

    public static final String KEY_TITLE = "KEY_TITLE";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_pay_income_list)
    RecyclerView rvPayIncomeList;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    Unbinder unbinder;

    private PoundPrizePoolAdapter rankListAdapter;

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, null);
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(this, tag).addToBackStack(null);
        return transaction.commitAllowingStateLoss();

    }

    public static PoundPrizePoolDialog newInstance(String title) {
        PoundPrizePoolDialog listDataDialog = new PoundPrizePoolDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        listDataDialog.setArguments(bundle);
        return listDataDialog;
    }

    public PoundPrizePoolDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        View view = inflater.inflate(R.layout.dialog_pound_egg_prize_pool, window.findViewById(android.R.id.content),
                false);
        unbinder = ButterKnife.bind(this, view);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        setCancelable(true);
        initView();
        getData();
        return view;
    }

    private void initView() {
        if (getArguments() != null) {
            String title = getArguments().getString(KEY_TITLE);
//            tvTitle.setText(title);
        }
        ivClose.setOnClickListener(v -> dismiss());
        rvPayIncomeList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvPayIncomeList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = ConvertUtils.dp2px(10);
                outRect.right = ConvertUtils.dp2px(5);
                outRect.left = ConvertUtils.dp2px(5);
            }
        });
        rankListAdapter = new PoundPrizePoolAdapter();
        rvPayIncomeList.setAdapter(rankListAdapter);
    }

    private void getData() {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().getRequest(UriProvider.getPoundEggPrizePool(), params, new OkHttpManager.MyCallBack<ServiceResult<List<GiftInfo>>>() {
            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onResponse(ServiceResult<List<GiftInfo>> response) {
                if (response.isSuccess()) {
                    rankListAdapter.setNewData(response.getData());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
