package com.vslk.lbgx.ui.me;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.WebUrl;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.praise.IPraiseClient;
import com.tongdaxing.xchat_core.room.IRoomCore;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.vslk.lbgx.base.fragment.BaseMvpFragment;
import com.vslk.lbgx.ui.find.activity.InviteAwardActivity;
import com.vslk.lbgx.ui.me.activities.ShowActivitiesActivity;
import com.vslk.lbgx.ui.me.setting.activity.FeedbackActivity;
import com.vslk.lbgx.ui.me.setting.activity.SettingActivity;
import com.vslk.lbgx.ui.me.shopping.activity.ShopActivity;
import com.vslk.lbgx.ui.me.task.view.IMeView;
import com.vslk.lbgx.ui.me.wallet.activity.BinderPhoneActivity;
import com.vslk.lbgx.ui.me.wallet.activity.SetPasswordActivity;
import com.vslk.lbgx.ui.message.activity.AttentionListActivity;
import com.vslk.lbgx.ui.message.activity.FansListActivity;
import com.vslk.lbgx.ui.rank.activity.UserGradeRuleActivity;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;
import com.vslk.lbgx.ui.widget.LevelView;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.vslk.lbgx.utils.UIHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author MadisonRong
 * @date 04/01/2018
 */
@CreatePresenter(MePresenter.class)
public class MeFragment extends BaseMvpFragment<IMeView, MePresenter> implements View.OnClickListener, IMeView {

    public static final String TAG = "MeFragment";
    @BindView(R.id.iv_user_head)
    RoundedImageView ivUserHead;
    @BindView(R.id.iv_headwear)
    ImageView ivHeadWear;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.iv_gender)
    ImageView ivGender;
    @BindView(R.id.level_info)
    LevelView levelInfo;
    @BindView(R.id.tv_user_id)
    TextView tvUserId;
    @BindView(R.id.clipboard)
    TextView clipboard;
    @BindView(R.id.tv_user_attentions)
    TextView tvUserAttentions;
    @BindView(R.id.tv_user_fans)
    TextView tvUserFans;
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.me_item_wallet)
    LinearLayout meItemWallet;
    @BindView(R.id.rl_me_my_lv)
    LinearLayout rlMeMyLv;
    @BindView(R.id.rl_market)
    LinearLayout meCar;
    @BindView(R.id.me_item_activities)
    LinearLayout meItemActivities;
    @BindView(R.id.me_item_recommend)
    LinearLayout meItemRecommend;
    @BindView(R.id.rl_teenager_model)
    LinearLayout rlTeenagerModel;
    @BindView(R.id.me_item_rela_auth)
    LinearLayout meItemRelaAuth;
    @BindView(R.id.me_item_settings)
    LinearLayout meItemSettings;
    @BindView(R.id.iv_remark_back)
    ImageView ivRemarkBack;
    @BindView(R.id.iv_edit)
    TextView ivEdit;
    @BindView(R.id.rv_feedback)
    RelativeLayout rvFeedback;
    Unbinder unbinder;
    @BindView(R.id.ll_attentions)
    LinearLayout llAttentions;
    @BindView(R.id.ll_fans)
    LinearLayout llFans;


    private UserInfo mUserInfo;
    private ClipboardManager mClipboardManager;

    /**
     * 判断是绑定手机还是设置手机密码
     */
    private boolean isBindPhone;

    @Override
    public void onFindViews() {
        mClipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
    }


    @Override
    public void onSetListener() {
        llFans.setOnClickListener(this);
        llAttentions.setOnClickListener(this);
        clipboard.setOnClickListener(this);
        ivUserHead.setOnClickListener(this);
        meItemRelaAuth.setOnClickListener(this);
        tvUserAttentions.setOnClickListener(this);
        tvUserFans.setOnClickListener(this);
        rlMeMyLv.setOnClickListener(this);
        meCar.setOnClickListener(this);
        meItemSettings.setOnClickListener(this);
        meItemActivities.setOnClickListener(this);
        meItemRecommend.setOnClickListener(this);
        rlTeenagerModel.setOnClickListener(this);
        rvFeedback.setOnClickListener(this);
        meItemWallet.setOnClickListener(this);
        ivEdit.setOnClickListener(v -> {
            if (mUserInfo != null) {
                UIHelper.showUserInfoModifyAct(getContext(), mUserInfo.getUid());
            }
        });

    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    public void onResume() {
        super.onResume();
        getMvpPresenter().initUserData();
    }

    //关注更新用户资料
    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onPraise(long uid) {
        CoreManager.getCore(IUserCore.class).requestUserInfo(CoreManager.getCore(IAuthCore.class).getCurrentUid());
    }

    //取消关注更新用户资料
    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onCanceledPraise(long likedUid, boolean showNotice) {
        CoreManager.getCore(IUserCore.class).requestUserInfo(CoreManager.getCore(IAuthCore.class).getCurrentUid());
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onCurrentUserInfoUpdate(UserInfo userInfo) {
        updateUserInfoUI(userInfo);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clipboard:
                if (mUserInfo == null) {
                    Toast.makeText(getContext(), "复制用户ID失败", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(getContext(), "用户ID复制成功!", Toast.LENGTH_LONG).show();
                save2Clipboard();
                break;
            case R.id.me_item_create_my_room:
                getDialogManager().showProgressDialog(getActivity(), "请稍后...");
                CoreManager.getCore(IRoomCore.class).requestRoomInfo(CoreManager.getCore(IAuthCore.class).getCurrentUid(), 0);
                break;
            default:
                break;
            case R.id.me_item_rela_auth:
                CommonWebViewActivity.start(getContext(), WebUrl.VERIFIED_REAL_NAME);
                break;
//            case R.id.iv_user_info_more:
            case R.id.iv_user_head:
                if (mUserInfo != null) {
                    UIHelper.showUserInfoAct(getContext(), mUserInfo.getUid());
                }
                break;
//            case R.id.tv_task:
//                TaskCenterActivity.start(getContext());
//                break;
//            case R.id.me_item_feedback:
//                getContext().startActivity(new Intent(getContext(), FeedbackActivity.class));
//                break;
//            case R.id.me_item_about:
//                UIHelper.openContactUs(getContext());
////                getContext().startActivity(new Intent(getContext(), AboutActivity.class));
//                break;
            case R.id.ll_attentions:
            case R.id.tv_user_attentions:
//            case R.id.tv_user_attention_text:
                getContext().startActivity(new Intent(getContext(), AttentionListActivity.class));
                break;
            case R.id.ll_fans:
            case R.id.tv_user_fans:
//            case R.id.tv_user_fans_text:
                getContext().startActivity(new Intent(getContext(), FansListActivity.class));
                break;
            case R.id.me_item_wallet:
                UIHelper.showWalletAct(getContext());
                break;
            case R.id.rl_me_my_lv:
                UserGradeRuleActivity.start(getContext());
                break;
            case R.id.rl_market:
                ShopActivity.start(getContext(), true, 0);
                break;
            case R.id.me_item_settings:
                SettingActivity.start(getContext());
                break;
            case R.id.me_item_activities:
                ShowActivitiesActivity.start(getContext());
                break;
            case R.id.me_item_recommend:
                InviteAwardActivity.start(getContext());
                break;
            case R.id.rl_teenager_model://青少年模式
                CommonWebViewActivity.start(getContext(), WebUrl.TEENAGER_MODEL_URL);
                break;
            case R.id.rv_feedback:
                getContext().startActivity(new Intent(getContext(), FeedbackActivity.class));
                break;
//
//            case R.id.rv_gold_coin:
//                UIHelper.showWalletAct(getContext());
//                break;
//
//            case R.id.rl_silver_coin:
//                TaskCenterActivity.start(getContext());
//                break;
        }
    }

    /**
     * 保存用户id
     */
    private void save2Clipboard() {
        ClipData clipData = ClipData.newPlainText("用户ID复制成功!", String.valueOf(mUserInfo.getErbanNo()));
        mClipboardManager.setPrimaryClip(clipData);

    }

    /**
     * 查询是否已经绑定手机
     *
     * @param isBindPhone true 绑定手机 false
     */
    private void queryBindPhone(boolean isBindPhone) {
        this.isBindPhone = isBindPhone;
        getDialogManager().showProgressDialog(getActivity(), "正在查询请稍后...");
        getMvpPresenter().isPhone();
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onRequestUserInfo(UserInfo info, UserMvpModel userMvpModel) {
        if (info.getUid() == CoreManager.getCore(IAuthCore.class).getCurrentUid()) {
            updateUserInfoUI(info);
        }
    }

    @Override
    public void updateUserInfoUI(UserInfo userInfo) {
        if (userInfo != null) {
            this.mUserInfo = userInfo;
            tvUserName.setText(userInfo.getNick());
//            liveness.setText(St.getLring.valueOf(userInfoiveness()));
            tvUserId.setText(getString(R.string.me_user_id, userInfo.getErbanNo()));
            ImageLoadUtils.loadCircleImage(mContext, userInfo.getAvatar(), ivUserHead,
                    R.drawable.ic_default_avatar);

            if (!StringUtil.isEmpty(userInfo.getHeadwearUrl())) {
                ImageLoadUtils.loadImage(mContext, userInfo.getHeadwearUrl(), ivHeadWear);
            }

            tvUserAttentions.setText(String.valueOf(userInfo.getFollowNum()));
            tvUserFans.setText(String.valueOf(userInfo.getFansNum()));
            int experLevel = userInfo.getExperLevel();
            levelInfo.setExperLevel(experLevel);
            int charmLevel = userInfo.getCharmLevel();
            levelInfo.setCharmLevel(charmLevel);
//            //开心数值
//            tvGoldValue.setText(getString(R.string.charge_gold, userInfo.getGoldNum()));
//            //点点币数值***********!!
//            tvSilverValue.setText(String.valueOf(userInfo.getMcoinNum()));
            //等级和魅力值
            if (charmLevel > 0 || experLevel > 0) {
                levelInfo.setVisibility(View.VISIBLE);
            } else {
                levelInfo.setVisibility(View.GONE);
            }

            if (userInfo.getGender() == 1) {
                ivGender.setBackground(getResources().getDrawable(R.drawable.icon_man));
            } else {
                ivGender.setBackground(getResources().getDrawable(R.drawable.icon_woman));
            }
            tvSign.setText((userInfo.getUserDesc() == null || userInfo.getUserDesc().isEmpty()) ? getString(R.string.user_info_desc_empty) : userInfo.getUserDesc());
        } else {
            tvUserName.setText("");
            tvUserId.setText(getString(R.string.me_user_id_null));
            tvUserAttentions.setText("0");
            tvUserFans.setText("0");
            levelInfo.setVisibility(View.GONE);
            levelInfo.setExperLevel(0);
            levelInfo.setCharmLevel(0);
        }
    }

    @Override
    public void onIsBindPhone() {
        if (isBindPhone) {
            getDialogManager().dismissDialog();
            Intent intent = new Intent(getActivity(), BinderPhoneActivity.class);
            intent.putExtra(BinderPhoneActivity.hasBand, BinderPhoneActivity.modifyBand);
            startActivity(intent);
        } else {
            getMvpPresenter().checkPwd();
        }
    }

    @Override
    public void onIsBindPhoneFail(String msg) {
        getDialogManager().dismissDialog();
        Intent intent = new Intent(getActivity(), BinderPhoneActivity.class);
        if (!isBindPhone) {
            intent.putExtra(BinderPhoneActivity.isSetPassword, true);
        }
        startActivity(intent);
    }

    @Override
    public void onIsSetPwd(boolean isSetPassWord) {
        getDialogManager().dismissDialog();
        if (isSetPassWord) {
            SetPasswordActivity.start(getActivity(), true);
        } else {
            BinderPhoneActivity.start(getActivity());
        }
    }

    @Override
    public void onIsSetPwdFail(String msg) {
        getDialogManager().dismissDialog();
        toast(msg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
