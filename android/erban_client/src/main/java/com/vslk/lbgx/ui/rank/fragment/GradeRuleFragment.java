package com.vslk.lbgx.ui.rank.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hncxco.library_ui.widget.DrawableTextView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.vslk.lbgx.base.fragment.BaseMvpFragment;
import com.vslk.lbgx.presenter.rankinglist.IRankingListView;
import com.vslk.lbgx.presenter.rankinglist.RankingListPresenter;
import com.vslk.lbgx.ui.rank.activity.GradeRuleActivity;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.bean.UserLevelInfo;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.vslk.lbgx.ui.widget.LevelView;
import com.vslk.lbgx.utils.ImageLoadUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 创建者      Created by dell
 * 创建时间    2018/12/3
 * 描述        等级排行
 * <p>
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
@CreatePresenter(RankingListPresenter.class)
public class GradeRuleFragment extends BaseMvpFragment<IRankingListView, RankingListPresenter> implements
        View.OnClickListener, IRankingListView {

    private boolean isCharm;
    @BindView(R.id.startGrade)
    DrawableTextView startGrade;
    @BindView(R.id.endGrade)
    DrawableTextView endGrade;
    @BindView(R.id.explain)
    TextView explain;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.level_view)
    LevelView levelView;
    @BindView(R.id.notify_msg)
    TextView notifyMsg;
    @BindView(R.id.grade_bg_llt)
    LinearLayout gradeBgLlt;
    @BindView(R.id.avatar)
    RoundedImageView avatar;
    @BindView(R.id.gave_gift_tv)
    TextView gaveGiftTv;
    @BindView(R.id.gold_tv)
    TextView goldTv;

    Unbinder unbinder;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_grade_rule;
    }

    @Override
    protected void onInitArguments(Bundle bundle) {
        if (bundle != null) {
            isCharm = bundle.getBoolean("isCharm", false);
        }
    }

    @Override
    public void onFindViews() {
        startGrade.setTextColor(isCharm ? getResources().getColor(R.color.color_ffff9eba) : getResources().getColor(R.color.color_ff6631cc));
        endGrade.changeSoildColor(isCharm ? getResources().getColor(R.color.color_ffff9eba) : getResources().getColor(R.color.color_ff5e2dca));

        gradeBgLlt.setBackgroundResource(isCharm ? R.drawable.ic_user_charm_grade_bg : R.drawable.ic_user_gold_grade_bg);
        gaveGiftTv.setText(isCharm ? R.string.charm_info : R.string.gold_info);
        goldTv.setText(isCharm ? R.string.charm_tips : R.string.gold_tips);
        explain.setText(Html.fromHtml("<u>等级说明</u>。"));


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onSetListener() {
        explain.setOnClickListener(this);
        seekBar.setOnTouchListener((view, motionEvent) -> true);
    }

    @Override
    public void initiate() {
        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
        if (userInfo != null) {
            if(StringUtil.isEmpty(String.valueOf(userInfo.getCharmLevel()))){
                if (isCharm) {
                    levelView.setCharmLevel(userInfo.getCharmLevel());
                } else {
                    levelView.setExperLevel(userInfo.getExperLevel());
                }
            }
        }
        getMvpPresenter().getUserLevel(isCharm ? UriProvider.levelCharmGet() : UriProvider.levelExeperienceGet());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.explain:
                GradeRuleActivity.start(getActivity(), isCharm);
                break;
            default:
                break;
        }
    }

    @Override
    public void getUserLevelSuccess(UserLevelInfo info) {
        if (info == null) {
            return;
        }
        if (!StringUtil.isEmpty(info.getAvatar())) {
            ImageLoadUtils.loadSmallRoundBackground(getContext(), info.getAvatar(), avatar, R.drawable.ic_default_avatar);
        }
        if (!StringUtil.isEmpty(String.valueOf(info.getLevelPercent()))) {
            seekBar.setProgress((int) (info.getLevelPercent() * 100));
        }
        if (!StringUtil.isEmpty(String.valueOf(info.getLeftGoldNum()))) {
            notifyMsg.setText("距离升级还差" + info.getLeftGoldNum() + "经验值，" /*+ (isCharm ? "。" : "，")*/);
        }

        if (!StringUtil.isEmpty(String.valueOf(info.getLevel()))) {
            startGrade.setText("Lv." + info.getLevel());
            if (info.getLevel() >= 50) {
                endGrade.setText("Lv." + info.getLevel());
            } else {
                endGrade.setText("Lv." + (info.getLevel() + 1));
            }
        }
    }

    @Override
    public void getUserLevelFail(String msg) {
        toast(msg);
    }

    public static Fragment newInstance(boolean isCharm) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isCharm", isCharm);
        GradeRuleFragment fragment = new GradeRuleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
