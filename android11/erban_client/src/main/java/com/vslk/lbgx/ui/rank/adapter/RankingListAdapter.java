package com.vslk.lbgx.ui.rank.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.ui.common.widget.CircleImageView;
import com.vslk.lbgx.ui.rank.fragment.RankingListFragment;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.vslk.lbgx.utils.NumberFormatUtils;
import com.vslk.lbgx.ui.widget.LevelView;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.bean.RankingInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> 排行榜adapter </p>
 *
 * @author dell
 */
public class RankingListAdapter extends BaseQuickAdapter<RankingInfo, BaseViewHolder> {

    private Context context;
    private View headerView;
    //头部数据个数
    private final static int HEADER_VIEW_DATA_COUNT = 3;
    private int rankingType;

    private View.OnClickListener onHeaderChildViewClickListener;

    public RankingListAdapter(int layoutRes, Context context, int rankingType) {
        super(layoutRes);
        this.context = context;
        this.rankingType = rankingType;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, RankingInfo info) {
        if (info == null) {
            return;
        }
        int position = HEADER_VIEW_DATA_COUNT + (baseViewHolder.getAdapterPosition() - getHeaderLayoutCount() + 1);
        ImageView ivRankingNumber = baseViewHolder.getView(R.id.iv_ranking_number);
        TextView rankingNumber = baseViewHolder.getView(R.id.ranking_number);
        if (position == 2) {
            rankingNumber.setVisibility(View.GONE);
            ivRankingNumber.setVisibility(View.VISIBLE);
            ivRankingNumber.setImageResource(R.drawable.ic_ranking_second);
        } else if (position == 3) {
            rankingNumber.setVisibility(View.GONE);
            ivRankingNumber.setVisibility(View.VISIBLE);
            ivRankingNumber.setImageResource(R.drawable.ic_ranking_third);
        } else if (position == 4) {
            baseViewHolder.getView(R.id.item_bg).setBackgroundResource(R.drawable.shape_fffafafa_corner_15dp);
            rankingNumber.setText(String.valueOf(position));

        } else {
            baseViewHolder.getView(R.id.item_bg).setBackgroundColor(Color.WHITE);
            rankingNumber.setVisibility(View.VISIBLE);
            ivRankingNumber.setVisibility(View.GONE);
            rankingNumber.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
            rankingNumber.setText(String.valueOf(position));
            rankingNumber.setTextColor(mContext.getResources().getColor(R.color.color_ff2b3945));
        }
        baseViewHolder.setImageResource(R.id.number_tag, rankingType == 2 || rankingType == 1 ? R.mipmap.ic_ranking_gold : R.mipmap.ic_ranking_charm);
        baseViewHolder.setImageResource(R.id.iv_gender, info.getGender() == 1 ? R.drawable.icon_man : R.drawable.icon_woman);

        ImageLoadUtils.loadAvatar(context, info.getAvatar(), baseViewHolder.getView(R.id.avatar));
        ((TextView) baseViewHolder.getView(R.id.nickname)).setText(info.getNick());
        baseViewHolder.getView(R.id.level_view).setVisibility(View.VISIBLE);
        if (rankingType == RankingListFragment.RANKING_TYPE_CHARM) {
            ((LevelView) baseViewHolder.getView(R.id.level_view)).setCharmLevel(info.getCharmLevel());
        } else {
            ((LevelView) baseViewHolder.getView(R.id.level_view)).setExperLevel(info.getExperLevel());
        }
        if (info.getDistance() == 0) {
            ((TextView) baseViewHolder.getView(R.id.number)).setText("0");
        } else {
            ((TextView) baseViewHolder.getView(R.id.number)).setText(
                    NumberFormatUtils.formatDoubleDecimalPoint(info.getDistance()));
        }
    }

    @Override
    public int setHeaderView(View headerView) {
        this.headerView = headerView;
        return super.setHeaderView(headerView);
    }

    @Override
    public void setNewData(@Nullable List<RankingInfo> dataList) {
        //这里拆分头部和adapter部分显示
        List<RankingInfo> headerDataList = new ArrayList<>();
        List<RankingInfo> adapterDataList = new ArrayList<>();
        if (dataList != null) {
            //头部显示不完
            if (dataList.size() > HEADER_VIEW_DATA_COUNT) {
                headerDataList.addAll(dataList.subList(0, HEADER_VIEW_DATA_COUNT));
                adapterDataList.addAll(dataList.subList(HEADER_VIEW_DATA_COUNT, dataList.size()));
            } else {
                headerDataList.addAll(dataList);
            }
        }
        setupHeaderView(headerDataList);
        super.setNewData(adapterDataList);
    }

    /**
     * 显示头部
     * （数据空的时候各项显示为空，并不是都隐藏）
     */
    private void setupHeaderView(List<RankingInfo> headerDataList) {
        ImageView outImageView = headerView.findViewById(R.id.iv_first_out);
        outImageView.setBackgroundResource(rankingType == 2 || rankingType == 1 ? R.mipmap.caifu_first : R.mipmap.meili_first);
        ImageView ivBgRanking = headerView.findViewById(R.id.iv_ranking_bg);
        ivBgRanking.setImageResource(rankingType == 2 || rankingType == 1 ? R.mipmap.bg_wealth_ranking : R.mipmap.bg_charm_ranking);
        if (context != null && !ListUtils.isListEmpty(headerDataList)) {
            for (int i = 0; i < headerDataList.size(); i++) {
                if (i == 0) {
                    numberOne(headerDataList.get(0));
                } else if (i == 1) {
                    numberTwo(headerDataList.get(1));
                } else if (i == 2) {
                    numberThree(headerDataList.get(2));
                }
            }
        }
    }

    /**
     * 第一名
     */
    private void numberOne(RankingInfo info) {
        CircleImageView circleImageView = headerView.findViewById(R.id.avatar_first);
        TextView nickNameFirst = headerView.findViewById(R.id.nickname_first);
        LevelView levelView = headerView.findViewById(R.id.level_view_first);
        ImageView ivGender = headerView.findViewById(R.id.iv_gender);
        setHeadData(info, circleImageView, nickNameFirst, levelView, ivGender);
    }

    /**
     * 第二名
     */
    private void numberTwo(RankingInfo info) {
        CircleImageView circleImageView = headerView.findViewById(R.id.avatar_second);
        TextView nickNameFirst = headerView.findViewById(R.id.nickname_second);
        LevelView levelView = headerView.findViewById(R.id.level_view_second);
        ImageView ivGender = headerView.findViewById(R.id.iv_gender_second);
        TextView tvDistanceSecond = headerView.findViewById(R.id.tv_distance_second);
        TextView tvDistanceTextSecond = headerView.findViewById(R.id.tv_distance_text_second);
        if (rankingType == 2 || rankingType == 1) {
            tvDistanceTextSecond.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            tvDistanceTextSecond.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
        tvDistanceSecond.setText(NumberFormatUtils.formatDoubleDecimalPoint(info.getDistance()));
        setHeadData(info, circleImageView, nickNameFirst, levelView, ivGender);
    }

    /**
     * 第三名
     */
    private void numberThree(RankingInfo info) {
        CircleImageView circleImageView = headerView.findViewById(R.id.avatar_third);
        TextView nickNameFirst = headerView.findViewById(R.id.nickname_third);
        LevelView levelView = headerView.findViewById(R.id.level_view_third);
        ImageView ivGender = headerView.findViewById(R.id.iv_gender_third);
        TextView tvDistanceThird = headerView.findViewById(R.id.tv_distance_third);
        TextView tvDistanceTextThird = headerView.findViewById(R.id.tv_distance_text_third);
        if (rankingType == 2 || rankingType == 1) {
            tvDistanceTextThird.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            tvDistanceTextThird.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
        tvDistanceThird.setText(NumberFormatUtils.formatDoubleDecimalPoint(info.getDistance()));
        setHeadData(info, circleImageView, nickNameFirst, levelView, ivGender);
    }

    private void setHeadData(RankingInfo info, CircleImageView circleImageView, TextView nickNameFirst, LevelView levelView, ImageView ivGender) {
        if (info != null) {
            ImageLoadUtils.loadAvatar(context, info.getAvatar(), circleImageView);
            circleImageView.setTag(info.getUid());
            circleImageView.setOnClickListener(onHeaderChildViewClickListener);
            nickNameFirst.setText(info.getNick());
            levelView.setVisibility(View.VISIBLE);
            ivGender.setImageResource(info.getGender() == 1 ? R.drawable.icon_man : R.drawable.icon_woman);
            if (rankingType == RankingListFragment.RANKING_TYPE_CHARM) {
                levelView.setCharmLevel(info.getCharmLevel());
            } else {
                levelView.setExperLevel(info.getExperLevel());
            }
        } else {//数据空的时候各项显示为空
            ImageLoadUtils.loadImageRes(context, R.drawable.ic_default_avatar, circleImageView);
            circleImageView.setOnClickListener(null);
            nickNameFirst.setVisibility(View.INVISIBLE);
        }
    }

    public void setOnHeaderChildViewClickListener(View.OnClickListener onHeaderChildViewClickListener) {
        this.onHeaderChildViewClickListener = onHeaderChildViewClickListener;
    }
}
