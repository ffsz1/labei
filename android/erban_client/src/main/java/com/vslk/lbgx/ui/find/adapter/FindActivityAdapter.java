package com.vslk.lbgx.ui.find.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.find.AlertInfo;

/**
 * 创建者      Created by dell
 * 创建时间    2018/12/8
 * 描述
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class FindActivityAdapter extends BaseMultiItemQuickAdapter<AlertInfo, BaseViewHolder> {

    private final int mHeight;

    public FindActivityAdapter() {
        super(null);
        addItemType(0, R.layout.item_find_normal);
        addItemType(1, R.layout.item_find_header);
        addItemType(2, R.layout.item_find_title);
        mHeight = ScreenUtil.dip2px(90);
    }

    @Override
    protected void convert(BaseViewHolder helper, AlertInfo item) {
        if (item == null) {
            return;
        }
        switch (item.getItemType()) {
            case 0:
                setAllActivities(helper, item);
                break;
            case 1:
                setNewActivity(helper, item);
                break;
            case 2:
                setAllTitle(helper, item);
                break;
            default:
                break;
        }
    }

    private void setAllTitle(BaseViewHolder helper, AlertInfo item) {

    }

    private void setNewActivity(BaseViewHolder helper, AlertInfo item) {
        ImageView ivActivity = helper.getView(R.id.iv_activity);
        ViewGroup.LayoutParams params = ivActivity.getLayoutParams();
        params.height = mHeight;
        ivActivity.setLayoutParams(params);

        ImageLoadUtils.loadBannerRoundBackground(ivActivity.getContext(), item.getAlertWinPic(), ivActivity, R.dimen.dp_10);
    }

    private void setAllActivities(BaseViewHolder helper, AlertInfo item) {

        LinearLayout rootView = helper.getView(R.id.rootView);
        ViewGroup.LayoutParams params = rootView.getLayoutParams();
        params.height = mHeight;
        rootView.setLayoutParams(params);

        ImageView ivActivity = helper.getView(R.id.iv_activity);
        ImageLoadUtils.loadBannerRoundBackground(ivActivity.getContext(), item.getAlertWinPic(), ivActivity, R.dimen.dp_10);
    }
}
