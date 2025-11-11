package com.vslk.lbgx.room.egg.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vslk.lbgx.base.fragment.BaseDialogFragment;
import com.vslk.lbgx.room.egg.adapter.PoundEggRankListAdapter;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.vslk.lbgx.room.widget.dialog.UserInfoDialog;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by MadisonRong on 13/01/2018.
 */

public class PoundEggRankListDialog extends BaseDialogFragment implements BaseQuickAdapter.OnItemClickListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    public static final String TYPE_ONLINE_USER = "ONLINE_USER";
    public static final String TYPE_CONTRIBUTION = "ROOM_CONTRIBUTION";
    public static final String KEY_TITLE = "KEY_TITLE";
    public static final String KEY_TYPE = "KEY_TYPE";

    @BindView(R.id.user_rank)
    RadioGroup userRank;
    @BindView(R.id.rv_pay_income_list)
    RecyclerView rvPayIncomeList;
    Unbinder unbinder;
    @BindView(R.id.iv_pound_egg_list_closs)
    ImageView ivPoundEggListCloss;

    private int type = 1;
    private long roomId;
    private PoundEggRankListAdapter rankListAdapter;
    private View noDataView;

    public static PoundEggRankListDialog newContributionListInstance(Context context) {
        return newInstance(context.getString(R.string.pound_egg_record), TYPE_CONTRIBUTION);
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, null);
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(this, tag).addToBackStack(null);
        return transaction.commitAllowingStateLoss();

    }

    public static PoundEggRankListDialog newInstance(String title, String type) {
        PoundEggRankListDialog listDataDialog = new PoundEggRankListDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_TYPE, type);
        listDataDialog.setArguments(bundle);
        return listDataDialog;
    }

    public PoundEggRankListDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        // setup window and width
        View view = inflater.inflate(R.layout.dialog_pound_egg_list_data, window.findViewById(android.R.id.content),
                false);
        unbinder = ButterKnife.bind(this, view);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        setCancelable(true);
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo != null) {
            roomId = roomInfo.getUid();
        }
        noDataView = view.findViewById(R.id.tv_no_data);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initRv();
        return view;
    }

    private void initRv() {
        rvPayIncomeList.setLayoutManager(new LinearLayoutManager(getContext()));
        rankListAdapter = new PoundEggRankListAdapter(getContext());
        rankListAdapter.setOnItemClickListener(this);
        rvPayIncomeList.setAdapter(rankListAdapter);
        getData();
    }

    private void initView() {
        userRank.setOnCheckedChangeListener(this);
        ivPoundEggListCloss.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.rb_tab_day:
                typeChange(1);
                break;
            case R.id.rb_tab_week:
                typeChange(2);
                break;
            case R.id.rb_tab_all:
                typeChange(3);
                break;
            default:
                break;
        }
    }

    private void typeChange(int i) {
        if (i == type) {
            return;
        }
        type = i;
        if (selectOptionAction != null) {
            selectOptionAction.optionClick();
        }
        getData();
    }

    private void getData() {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("roomId", roomId + "");
        param.put("type", type + "");
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        OkHttpManager.getInstance().getRequest(UriProvider.getPoundEggRank(), param, new OkHttpManager.MyCallBack<ServiceResult<List<UserInfo>>>() {
            @Override
            public void onError(Exception e) {
                if (selectOptionAction != null) {
                    selectOptionAction.onDataResponse();
                }
            }

            @Override
            public void onResponse(ServiceResult<List<UserInfo>> response) {
                if (selectOptionAction != null) {
                    selectOptionAction.onDataResponse();
                }
                if (response.isSuccess()) {
                    if (response.getData() != null) {
                        noDataView.setVisibility(response.getData().size() > 0 ? View.GONE : View.VISIBLE);
                        rankListAdapter.setNewData(response.getData());
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        List<UserInfo> list = rankListAdapter.getData();
        if (ListUtils.isListEmpty(list)) {
            return;
        }
        UserInfo userInfo = list.get(i);
        UserInfoDialog.showUserDialog(getContext(), userInfo.getUid());
    }

    private SelectOptionAction selectOptionAction;

    public void setSelectOptionAction(SelectOptionAction selectOptionAction) {
        this.selectOptionAction = selectOptionAction;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pound_egg_list_closs:
                try {
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public interface SelectOptionAction {
        void optionClick();

        void onDataResponse();
    }

}
