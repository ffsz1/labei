package com.vslk.lbgx.ui.find.fragment;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.vslk.lbgx.base.fragment.BaseMvpFragment;
import com.vslk.lbgx.presenter.find.MicroMatchingPresenter;
import com.vslk.lbgx.presenter.find.MicroMatchingView;
import com.vslk.lbgx.ui.find.activity.MicroMatchingActivity;
import com.vslk.lbgx.ui.find.activity.SquareActivity;
import com.vslk.lbgx.ui.find.adapter.UserTagsAdapter;
import com.vslk.lbgx.ui.me.activities.ShowActivitiesActivity;
import com.vslk.lbgx.ui.me.user.activity.UserInfoActivity;
import com.vslk.lbgx.ui.widget.cloud.TagCloudView;
import com.vslk.lbgx.ui.widget.marqueeview.MarqueeView;
import com.vslk.lbgx.utils.FastClickUtil;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.hncxco.library_ui.widget.AppToolBar;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.find.SpeedUserInfo;
import com.tongdaxing.xchat_core.user.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 创建者      Created by dell
 * 创建时间    2018/12/24
 * 描述        速配界面
 * <p>
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
@CreatePresenter(MicroMatchingPresenter.class)
public class SpeedDatingFragment extends BaseMvpFragment<MicroMatchingView, MicroMatchingPresenter> implements View.OnClickListener,
        MicroMatchingView, MarqueeView.OnItemSelectedListener, UserTagsAdapter.OnTagItemSelectedListener {

    @BindView(R.id.tagCloudView)
    TagCloudView mTagCloudView;
    @BindView(R.id.ic_speed_friend)
    FrameLayout flSpeedFriend;
    @BindView(R.id.ic_speed_square)
    FrameLayout flSpeedSquare;
    @BindView(R.id.error_content)
    TextView errorContent;
    @BindView(R.id.marqueeView)
    MarqueeView mMarqueeView;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.userNick)
    TextView nick;
    @BindView(R.id.refresh)
    FrameLayout refreshContent;
    @BindView(R.id.ic_refresh)
    ImageView icRefresh;
    @BindView(R.id.toolbar)
    AppToolBar mToolBar;

    private boolean stopScroll;
    private List<SpeedUserInfo> mSpeedUserInfos = new ArrayList<>();

    public static final String TAG = SpeedDatingFragment.class.getSimpleName();

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_speed_dating;
    }

    @Override
    public void onSetListener() {
        flSpeedFriend.setOnClickListener(this);
        flSpeedSquare.setOnClickListener(this);
        refreshContent.setOnClickListener(this);
        mMarqueeView.setOnItemSelectedListener(this);
        mToolBar.setOnRightImgBtnClickListener(() -> ShowActivitiesActivity.start(getActivity()));
    }

    @Override
    protected void onLazyLoadData() {
        onRefresh();
    }

    @Override
    public void onCharmUserListView(List<UserInfo> charmUsers) {
        //设置可点击
        refreshContent.setEnabled(true);
        //停止动画效果
        stopAnim();
        if (!ListUtils.isListEmpty(charmUsers)) {
            if (mTagCloudView.getVisibility() != View.VISIBLE) {
                mTagCloudView.setVisibility(View.VISIBLE);
                errorContent.setVisibility(View.GONE);
            }
            UserTagsAdapter adapter = new UserTagsAdapter(charmUsers);
            adapter.setOnTagItemSelectedListener(this);
            mTagCloudView.setAdapter(adapter);
        } else {
            if (mTagCloudView.getVisibility() != View.GONE) {
                errorContent.setVisibility(View.VISIBLE);
                mTagCloudView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCharmUserListFailView(String msg) {
        //设置可点击
        refreshContent.setEnabled(true);
        //停止动画效果
        stopAnim();
        if (mTagCloudView.getVisibility() != View.GONE) {
            errorContent.setVisibility(View.VISIBLE);
            mTagCloudView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLobbyChatInfoSuccessView(List<SpeedUserInfo> speedUserInfos) {
        if (!ListUtils.isListEmpty(speedUserInfos)) {
            mSpeedUserInfos.clear();
            mSpeedUserInfos.addAll(speedUserInfos);
            List<String> content = new ArrayList<>();
            for (SpeedUserInfo userInfo : speedUserInfos) {
                content.add(userInfo.getContent());
            }
            mMarqueeView.startWithList(content);
        }
    }

    @Override
    public void onItemSelected(int position) {
        if (!ListUtils.isListEmpty(mSpeedUserInfos)) {
            SpeedUserInfo userInfo = mSpeedUserInfos.get(position);
            ImageLoadUtils.loadCircleImage(avatar.getContext(), userInfo.getAvatar(), avatar, R.drawable.ic_default_avatar);
            nick.setText(userInfo.getNick());
        }
    }

    @Override
    public void onTagItemSelected(UserInfo userInfo) {
        if (userInfo != null) {
            if (FastClickUtil.isFastClick()) {
                UserInfoActivity.start(getActivity(), userInfo.getUid());
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic_speed_friend:
                MicroMatchingActivity.start(getActivity());
                break;
            case R.id.ic_speed_square:
                SquareActivity.start(getActivity());
                break;
            case R.id.refresh:
                onRefresh();
                break;
            default:
                break;
        }
    }

    private void onRefresh() {
        getMvpPresenter().soundMatchCharmUser();
        getMvpPresenter().getLobbyChatInfo();
        //禁止当前点击
        refreshContent.setEnabled(false);
        //开启动画效果
        startAnim();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!stopScroll) {
            stopScroll = !stopScroll;
            mTagCloudView.setStopScroll(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (stopScroll) {
            stopScroll = !stopScroll;
            mTagCloudView.setStopScroll(false);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mTagCloudView.setStopScroll(hidden);
    }

    private void startAnim() {
        Animation rotateAnimation = new RotateAnimation(0, -359, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(1500);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        icRefresh.startAnimation(rotateAnimation);
    }

    private void stopAnim() {
        icRefresh.clearAnimation();
    }
}
