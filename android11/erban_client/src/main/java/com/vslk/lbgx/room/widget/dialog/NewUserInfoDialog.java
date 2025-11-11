package com.vslk.lbgx.room.widget.dialog;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.gift.GiftInfo;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.praise.IPraiseClient;
import com.tongdaxing.xchat_core.praise.IPraiseCore;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.queue.bean.MicMemberInfo;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.vslk.lbgx.room.avroom.other.ButtonItemFactory;
import com.vslk.lbgx.room.gift.GiftDialog;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.ui.me.user.activity.UserInfoActivity;
import com.vslk.lbgx.ui.me.wallet.activity.WalletActivity;
import com.vslk.lbgx.ui.widget.LevelView;
import com.vslk.lbgx.utils.ImageLoadUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 用户个人信息对话框
 *
 * @author Edward
 * @date 2017/12/13
 */
public class NewUserInfoDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = "NewUserInfoDialog";
    private Context context;
    private long uid;
    private UserInfo userInfo;
    //    private TextView report;
    private LinearLayout topLly;
    private LinearLayout otherUserLly;
    private RoundedImageView avatar;
    private TextView nick;
    private TextView erbanId;
    private TextView tvCopy;
    private TextView usePageBtn;
    private TextView fansNumber;
    private long fansCount = 0;
    private TextView attentionBtn;
    private long myUid;
    private LevelView mLevelView;
    private ImageView publicLevel;
    private ImageView ivSex;
    private TextView tvSendGift;
    private TextView rlUserHome;
    private TextView tvReportBtn;
    //    private FrameLayout llMyHome;
    private LinearLayout mOtherUser;
    private TextView attentionNumber;
    private TextView tvFriends;
    private ImageView iv;
    private ImageView ivHeadWear;

    public NewUserInfoDialog(Context context, long uid) {
        super(context, R.style.dialog);
        this.context = context;
        this.uid = uid;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoreManager.addClient(this);
        setContentView(R.layout.new_dialog_user_info);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        mClipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        initView();
//        Bitmap bitmap = DisplayUtils.getCurrentScreenShot((Activity) context);
//        ImageLoadUtils.loadImageWithBlurTransformation(context, bitmap, iv);
        Window window = getWindow();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(uid, true);
        myUid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        if (uid == myUid) {
//            llMyHome.setVisibility(View.VISIBLE);
            mOtherUser.setVisibility(View.GONE);
//            report.setVisibility(View.GONE);
        } else {
//            report.setVisibility(View.VISIBLE);
//            llMyHome.setVisibility(View.GONE);
            mOtherUser.setVisibility(View.VISIBLE);
            CoreManager.getCore(IPraiseCore.class).isPraised(myUid, uid);
        }
        initData();
        initListener();
        updateView();
    }

    private void initView() {
//        report = findViewById(R.id.report_text);
        otherUserLly = findViewById(R.id.otherUser);
        topLly = findViewById(R.id.user_dialog_top_lly);
        tvCopy = findViewById(R.id.tv_copy);
        iv = findViewById(R.id.iv);
        ivHeadWear = findViewById(R.id.iv_headwear);
        tvSendGift = findViewById(R.id.tv_send_gift);
        tvReportBtn = findViewById(R.id.report_btn);
        avatar = findViewById(R.id.avatar);
        attentionNumber = findViewById(R.id.attention_number);
        nick = findViewById(R.id.nick);
        erbanId = findViewById(R.id.tv_erban_id);
        mOtherUser = findViewById(R.id.otherUser);
//        llMyHome = findViewById(R.id.ll_my_home);
        tvFriends = findViewById(R.id.tv_friends);
        usePageBtn = findViewById(R.id.user_info_page_btn);
        attentionBtn = findViewById(R.id.follow_text);
        fansNumber = findViewById(R.id.fans_number);
        mLevelView = findViewById(R.id.level_info_user_dialog);
        publicLevel = findViewById(R.id.iv_public_tag);
        ivSex = findViewById(R.id.iv_sex);
        rlUserHome = findViewById(R.id.rl_user_info);
    }

    private void initData() {
        if (AvRoomDataManager.get().isOwner(uid)) {
            otherUserLly.setVisibility(View.GONE);
            topLly.setBackgroundResource(R.mipmap.user_data_top_bg);
        } else {
            otherUserLly.setVisibility(View.VISIBLE);
            topLly.setBackgroundResource(R.mipmap.user_data_bg);
        }
    }

    private void initListener() {
        iv.setOnClickListener(this);
        tvCopy.setOnClickListener(this);
        attentionBtn.setOnClickListener(this);
        tvSendGift.setOnClickListener(this);
        tvReportBtn.setOnClickListener(this);
//        llMyHome.setOnClickListener(this);
        tvFriends.setOnClickListener(this);
        usePageBtn.setOnClickListener(this);
        rlUserHome.setOnClickListener(this);
        avatar.setOnClickListener(this);
//        report.setOnClickListener(this);
    }

    /**
     * 保存用户id
     */
    private void save2Clipboard() {
        if (userInfo == null) {
            SingleToastUtil.showToast("复制用户ID失败");
            return;
        }
        ClipData clipData = ClipData.newPlainText("用户ID复制成功!", String.valueOf(userInfo.getErbanNo()));
        mClipboardManager.setPrimaryClip(clipData);
        SingleToastUtil.showToast("用户ID复制成功!");
    }

    private void updateView() {
        if (userInfo != null) {
            if (!StringUtil.isEmpty(userInfo.getHeadwearUrl())) {
                ImageLoadUtils.loadImage(context, userInfo.getHeadwearUrl(), ivHeadWear);
            }

            ImageLoadUtils.loadAvatar(getContext(), userInfo.getAvatar(), avatar, true);
            nick.setText(userInfo.getNick());
            erbanId.setText(String.format(Locale.getDefault(),
                    getContext().getString(R.string.me_user_id), userInfo.getErbanNo()));
            Drawable drawable;
            if (userInfo.getGender() == 1) {
                drawable = context.getResources().getDrawable(R.drawable.icon_man);
            } else {
                drawable = context.getResources().getDrawable(R.drawable.icon_woman);
            }
            ivSex.setImageDrawable(drawable);
            //设置粉丝数量
            fansCount = userInfo.getFansNum();
            fansNumber.setText(String.valueOf(fansCount));
            attentionNumber.setText(String.valueOf(userInfo.getFollowNum()));
            tvFriends.setText(String.valueOf(userInfo.getLiveness()));
            mLevelView.setExperLevel(userInfo.getExperLevel());
            mLevelView.setCharmLevel(userInfo.getCharmLevel());
            publicLevel.setVisibility(userInfo.getDefUser() == 2 ? View.VISIBLE : View.GONE);
        }
    }

    private boolean isAttention = false;

    //------------------------------IPraiseClient--------------------------------
    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onIsLiked(Boolean islike, long uid) {
        isAttention = islike;
        if (islike) {
            attentionBtn.setText("已关注");
        } else {
            attentionBtn.setText("关注");
        }
    }

    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onPraise(long likedUid) {
        isAttention = true;
        attentionBtn.setText("已关注");
        fansCount++;
        fansNumber.setText(String.valueOf(fansCount));
        SingleToastUtil.showToast("关注成功");
    }

    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onCanceledPraise(long uid, boolean showNotice) {
        isAttention = false;
        attentionBtn.setText("关注");
        if (fansCount > 0) {
            fansCount--;
        }
        fansNumber.setText(String.valueOf(fansCount));
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onRequestUserInfo(UserInfo info) {
        if (info.getUid() == uid) {
            this.userInfo = info;
            updateView();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        CoreManager.removeClient(this);
    }

    private ClipboardManager mClipboardManager;
    private List<String> list = new ArrayList<String>();
    private BaseQuickAdapter<String, BaseViewHolder> baseQuickAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_text, list) {
        @Override
        protected void convert(BaseViewHolder helper, String item) {
            int position = helper.getLayoutPosition();
            TextView textView = helper.getView(R.id.tv);
            textView.setText(item);
            if (position == list.size() - 1) {
                textView.setBackgroundResource(R.drawable.shape_33ffffff_bottom_30dp);
            } else {
                textView.setBackgroundResource(R.drawable.shape_33ffffff);
            }
        }
    };
    private PopupWindow popupWindow;

    /**
     * 生成用户功能按钮
     */
    private void createUserMoreOperate(long uid, String avatar) {
//        CommonWebViewActivity.start(getContext(), WebUrl.REPORT + "?uid=" + uid + "&type=1");

        final int type = 1;//针对用户
        List<ButtonItem> buttons = new ArrayList<>();
        ButtonItem button5 = new ButtonItem("举报头像", () -> ButtonItemFactory.reportAvatar(context, type, 5, uid, avatar));
        ButtonItem button6 = new ButtonItem("举报昵称", () -> ButtonItemFactory.reportCommit(context, type, 6, uid));
        ButtonItem button1 = new ButtonItem("政治敏感", () -> ButtonItemFactory.reportCommit(context, type, 1, uid));
        ButtonItem button2 = new ButtonItem("色情低俗", () -> ButtonItemFactory.reportCommit(context, type, 2, uid));
        ButtonItem button3 = new ButtonItem("广告骚扰", () -> ButtonItemFactory.reportCommit(context, type, 3, uid));
        ButtonItem button4 = new ButtonItem("人身攻击", () -> ButtonItemFactory.reportCommit(context, type, 4, uid));
        buttons.add(button5);
        buttons.add(button6);
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);
        buttons.add(button4);
        DialogManager dialogManager = new DialogManager(context);
        dialogManager.showCommonPopupDialog(buttons, "取消");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv:
                dismiss();
                break;
            case R.id.report_btn:
                createUserMoreOperate(userInfo.getUid(), userInfo.getAvatar());
//                CommonWebViewActivity.start(getContext(), WebUrl.REPORT + "?uid=" + userInfo.getUid() + "&type=1");
                break;
            case R.id.tv_send_gift:
                if (userInfo != null) {
                    GiftDialog giftDialog = new GiftDialog(context, userInfo.getUid(), userInfo.getNick(), userInfo.getAvatar());
                    giftDialog.setGiftDialogBtnClickListener(new GiftDialog.OnGiftDialogBtnClickListener() {
                        @Override
                        public void onRechargeBtnClick() {
                            WalletActivity.start(context);
                        }

                        @Override
                        public void onSendGiftBtnClick(GiftInfo giftInfo, long uid, int number, int currentP) {
                            RoomInfo currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
                            if (currentRoomInfo == null) {
                                return;
                            }
                            CoreManager.getCore(IGiftCore.class).sendRoomGift(giftInfo.getGiftId(), uid, currentRoomInfo.getUid(), number, giftInfo.getGoldPrice(), currentP);
                        }

                        @Override
                        public void onSendGiftBtnClick(GiftInfo giftInfo, List<MicMemberInfo> micMemberInfos, int number, int currentP) {
                            RoomInfo currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
                            if (currentRoomInfo == null) {
                                return;
                            }
                            List<Long> targetUids = new ArrayList<>();
                            for (int i = 0; i < micMemberInfos.size(); i++) {
                                targetUids.add(micMemberInfos.get(i).getUid());
                            }
                            CoreManager.getCore(IGiftCore.class).sendRoomMultiGift(giftInfo.getGiftId(), targetUids, currentRoomInfo.getUid(), number, giftInfo.getGoldPrice(), currentP);
                        }
                    });
                    giftDialog.show();
                    dismiss();
                }
                break;
            case R.id.tv_copy:
                save2Clipboard();
                break;
//            case R.id.report_text:
//                if (flag) {
//                    if (popupWindow != null) {
//                        popupWindow.dismiss();
//                        recoverButton();
//                        flag = false;
//                    }
//                } else {
//                    report.setText("取消");
//                    report.setBackgroundResource(R.drawable.shape_33ffffff_top_30dp);
//                    list = new ArrayList<>();
//                    list.add("举报");
//                    list.add("拉黑");
//                    baseQuickAdapter.setNewData(list);
//                    popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    popupWindow.setAnimationStyle(R.style.popmenu_animation);
//                    popupWindow.setOutsideTouchable(true);
//                    popupWindow.showAsDropDown(report, 0, 0);
//                    flag = true;
//                }
//                break;
            case R.id.follow_text:
                if (userInfo != null) {
                    if (isAttention) {
                        CoreManager.getCore(IPraiseCore.class).cancelPraise(userInfo.getUid(), true);
                    } else {
                        CoreManager.getCore(IPraiseCore.class).praise(userInfo.getUid());
                    }
                }
                break;
            case R.id.rl_user_info:
//            case R.id.ll_my_home:
                UserInfoActivity.start(context, uid);
                dismiss();
                break;
            case R.id.avatar:
            case R.id.user_info_page_btn:
                if (userInfo != null) {
                    if (CoreManager.getCore(IAuthCore.class).getCurrentUid() == userInfo.getUid()) {
                        return;
                    }
                    NimUserInfo nimUserInfo = NimUserInfoCache.getInstance().getUserInfo(String.valueOf(this.userInfo.getUid()));
                    if (nimUserInfo != null) {
                        NimUIKit.startP2PSession(context, String.valueOf(uid));
                    } else {
                        NimUserInfoCache.getInstance().getUserInfoFromRemote(String.valueOf(userInfo.getUid()), new RequestCallbackWrapper<NimUserInfo>() {

                            @Override
                            public void onResult(int code, NimUserInfo result, Throwable exception) {
                                if (code == 200) {
                                    NimUIKit.startP2PSession(context, String.valueOf(uid));
                                } else {
                                    SingleToastUtil.showToast("网络异常，请重试");
                                }
                            }
                        });
                    }
                } else {
                    SingleToastUtil.showToast("网络异常，请重试");
                }
                dismiss();
                break;
            default:
        }
    }


    //处理Unable to add window -- token android.os.BinderProxy@f9e7485 is not valid; is your activity running?
    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //not attached to window manager
    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void showUserDialog(Context context, long uid) {
        if (uid == 0) return;
        NewUserInfoDialog dialog = new NewUserInfoDialog(context, uid);
        dialog.show();
    }
}
