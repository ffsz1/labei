package com.vslk.lbgx.ui.me.shopping.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.room.IRoomCoreClient;
import com.tongdaxing.xchat_core.user.bean.DressUpBean;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vslk.lbgx.ui.me.shopping.fragment.ShopFragment.IS_NO_SELECT;

/**
 * @author Administrator
 * @date 2018/3/30
 */

public class CarAdapter extends BaseQuickAdapter<DressUpBean, CarAdapter.ViewHolder> {

    private int selectIndex, selectIndexs = -1;
    /**
     * 判断是否有选择过
     */
    public int selectId, selectIds = IS_NO_SELECT;

    /**
     * 是头饰还是座驾   默认为0为头饰  1为座驾
     */
    private int type;

    /**
     * 是否是自己
     */
    private boolean isMySelf;

    private Context mContext;

    public CarAdapter(int type, boolean isMySelf, Context context) {
        super(R.layout.item_car_select);
        this.isMySelf = isMySelf;
        this.mContext = context;
        this.type = type;
    }

    public void setVggAction(VggAction vggAction) {
        this.vggAction = vggAction;
    }

    private VggAction vggAction;

    public interface VggAction {
        void showVgg(String url);
    }

    private OnItemSelectedListener listener;

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(DressUpBean bean);
    }

    @Override
    public void setNewData(@Nullable List<DressUpBean> data) {
        super.setNewData(data);
        for (int i = 0; i < data.size(); i++) {
            DressUpBean bean = data.get(i);
            if (bean.getIsPurse() == 2) {
                selectIndex = selectIndexs = i;
                break;
            }
        }
        selectId = selectIds = IS_NO_SELECT;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        DressUpBean item = getData().get(position);
        int isSelect = item.getIsSelect();
        int isPurse = item.getIsPurse();
        String picUrl = item.getPicUrl();
        String vggUrl = item.getVggUrl();
        //判断显示播放的按钮
        if (type == 1) {
            holder.play.setVisibility(View.VISIBLE);
            holder.play.setOnClickListener((View v) -> {
                if (vggAction != null) {
                    vggAction.showVgg(vggUrl);
                }
            });
        } else {
            holder.play.setVisibility(View.GONE);
        }
        holder.rootView.setOnClickListener(view -> {
            if (isSelect == 0 || isSelect == 1) {
                selectIds = (type == 0 ? item.getHeadwearId() : item.getCarId());
                if (selectIndexs > -1) {
                    getData().get(selectIndexs).setIsSelect(1);
                }
                selectIndexs = position;
                item.setIsSelect(2);
                if (type == 0) {
                    CoreManager.notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_SHOW_HEAD_WEAR, picUrl);
                }
                notifyDataSetChanged();
            }
            if (listener != null) {
                listener.onItemSelected(item);
            }
        });
        holder.tvCarType.setOnClickListener(v -> {
            if (isPurse == 1) {
                selectIds = selectId = (type == 0 ? item.getHeadwearId() : item.getCarId());
                if (selectIndex > -1) {
                    getData().get(selectIndex).setIsPurse(1);
                }
                if (selectIndexs > -1) {
                    getData().get(selectIndexs).setIsSelect(1);
                }
                selectIndex = position;
                selectIndexs = position;

                item.setIsPurse(2);
                item.setIsSelect(2);
                if (type == 0) {
                    CoreManager.notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_SHOW_HEAD_WEAR, picUrl);
                }
            } else if (isPurse == 2) {
                item.setIsPurse(1);
                selectId = -1;
            }
            notifyDataSetChanged();
        });
    }

    @Override
    protected void convert(ViewHolder helper, DressUpBean item) {
        helper.tvCarName.setText(type == 0 ? item.getHeadwearName() : item.getCarName());
        ImageLoadUtils.loadImage(helper.ivCarStyle.getContext(), item.getPicUrl(), helper.ivCarStyle);
        //0未购买，1购买未选中，2选中
        int isPurse = item.getIsPurse();
        int isSelect = item.getIsSelect();
        //设置天数
        helper.tvCarTime.setText(isPurse == 0 ? item.getEffectiveTime() + "天" : item.getDaysRemaining() + "天");
        if (isPurse == 0) {
            helper.ivCarTag.setImageResource(R.drawable.car_lock);
            helper.tvCarTime.setTextColor(mContext.getResources().getColor(R.color.color_ff9b9a9a));
        } else {
            helper.ivCarTag.setImageResource(R.drawable.car_time);
            helper.tvCarTime.setTextColor(mContext.getResources().getColor(R.color.color_ffff0066));
        }
        if (isMySelf) {
            if (isPurse == 0) {
                helper.tvCarType.setVisibility(View.INVISIBLE);
            } else if (isPurse == 1) {
                helper.tvCarType.setVisibility(View.VISIBLE);
                helper.tvCarType.setImageResource(R.drawable.ic_shop_option_arrow_normal);
            } else if (isPurse == 2) {
                helper.tvCarType.setVisibility(View.VISIBLE);
                helper.tvCarType.setImageResource(R.drawable.ic_shop_option_arrow_select);
            }
        }

        if (isSelect == 0 || isSelect == 1) {
            helper.rootView.setBackgroundResource(R.drawable.icon_shop_item_normal_bg);
        } else if (isSelect == 2) {
            helper.rootView.setBackgroundResource(R.drawable.ic_dress_select_bg);
        }

        if (item.getGoldPrice() == 0) {
            helper.tvCarTime.setVisibility(View.GONE);
            helper.tvCarPrice.setVisibility(View.GONE);
        } else {
            helper.tvCarTime.setVisibility(View.VISIBLE);
            helper.tvCarPrice.setVisibility(View.VISIBLE);
        }

        helper.tvCarPrice.setText(String.valueOf(item.getGoldPrice()));
    }

    static class ViewHolder extends BaseViewHolder {

        @BindView(R.id.ic_car_tag)
        ImageView ivCarTag;
        @BindView(R.id.tv_car_time)
        TextView tvCarTime;
        @BindView(R.id.tv_car_type)
        ImageView tvCarType;
        @BindView(R.id.iv_car_style)
        ImageView ivCarStyle;
        @BindView(R.id.tv_car_price)
        TextView tvCarPrice;
        @BindView(R.id.tv_car_name)
        TextView tvCarName;
        @BindView(R.id.play)
        ImageView play;
        @BindView(R.id.rootView)
        RelativeLayout rootView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
