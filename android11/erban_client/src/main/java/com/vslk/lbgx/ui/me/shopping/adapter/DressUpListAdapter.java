package com.vslk.lbgx.ui.me.shopping.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.user.bean.DressUpBean;

import java.util.List;


/**
 * 装扮商城列表共用adapter
 * Created by zwk on 2018/10/16.
 */
public class DressUpListAdapter extends BaseQuickAdapter<DressUpBean, BaseViewHolder> {
    private int type;
    private boolean isMySelf;
    private int selectIndex = -1;//位置选中标记
    private OnDressUpClickListener onDressUpClickListener;

    public DressUpListAdapter(int type, boolean isMySelf) {
        super(R.layout.item_rv_dress_up);
        this.type = type;
        this.isMySelf = isMySelf;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.getView(R.id.rootView).setOnClickListener(v -> {
            selectIndex = position;
            notifyDataSetChanged();
        });
        //避免刷新数据后数据变化出现数据变少的情况(到期)
        if (selectIndex >= getItemCount()){
            selectIndex = 0;
        }
        holder.setBackgroundRes(R.id.rootView, selectIndex == position ? R.drawable.ic_dress_select_bg : R.drawable.icon_shop_item_normal_bg);
        if (selectIndex == position){
            //View.post()，还是有可能会造成内存泄漏的不建议这种写法，但是为了将item状态和页面底部功能按钮状态绑定（建议产品不要这样设计）
            //这里有个很诡异的问题如果直接通知底部控制按钮显示隐藏第一次会出现无法显示的问题只能
            holder.getView(R.id.rootView).post(() -> {
                if (onDressUpClickListener != null) {
                    onDressUpClickListener.onDressUpItemClickListener(getItem(position));
                }
            });
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, DressUpBean item) {
        helper.setGone(R.id.play, type == 1 && item.getIsPurse() != 2);
        helper.setText(R.id.tv_car_name, type == 0 ? item.getHeadwearName() : item.getCarName());
        helper.setText(R.id.tv_car_price, item.getGoldPrice() + "");
        helper.setGone(R.id.tv_car_price,item.isAllowPurse());
        helper.setText(R.id.tv_car_time, isMySelf ? (item.getDaysRemaining() + "天") : (item.getEffectiveTime() + "天"));
        ImageLoadUtils.loadImage(mContext, item.getPicUrl(), helper.getView(R.id.iv_car_style));
        helper.getView(R.id.play).setOnClickListener(v -> {
            if (onDressUpClickListener != null) {
                onDressUpClickListener.onCarTryClickListener(item.getVggUrl());
            }
        });

    }

    /**
     * 获取当前选中的装扮数据
     * @return
     */
    public DressUpBean getCurrentSelectData() {
        if (selectIndex >= getItemCount()) {
            return null;
        }
        return getItem(selectIndex);
    }

    /**
     * 修改列表数据状态，用于使用和或者取消使用更新
     * @param dressUpId
     */
    public void resetUseState(int dressUpId) {
        List<DressUpBean> datas = getData();
        if (!ListUtils.isListEmpty(datas)) {
            for (int i = 0; i < datas.size(); i++) {
                if (dressUpId == getDressUpId(datas.get(i))) {
                    datas.get(i).setIsPurse(2);
                } else {
                    datas.get(i).setIsPurse(1);
                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     *
     * 获取对应的装扮id
     * @param bean
     * @return
     */
    public int getDressUpId(DressUpBean bean) {
        if (bean == null) {
            return -1;
        }
        return type == 0 ? bean.getHeadwearId() : bean.getCarId();
    }


    public interface OnDressUpClickListener {
        void onDressUpItemClickListener(DressUpBean item);

        void onCarTryClickListener(String vggUrl);
    }

    public void setOnDressUpClickListener(OnDressUpClickListener onDressUpClickListener) {
        this.onDressUpClickListener = onDressUpClickListener;
    }

}
