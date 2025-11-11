package com.vslk.lbgx.ui.me.user.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nimlib.sdk.media.player.OnPlayListener;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.databinding.ActivityUserInfoNewBinding;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.audio.AudioPlayManager;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.im.friend.IIMFriendCore;
import com.tongdaxing.xchat_core.im.friend.IIMFriendCoreClient;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_core.praise.IPraiseClient;
import com.tongdaxing.xchat_core.praise.IPraiseCore;
import com.tongdaxing.xchat_core.room.IRoomCoreClient;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.publicchatroom.PublicChatRoomController;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.DressUpBean;
import com.tongdaxing.xchat_core.user.bean.GiftWallInfo;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_core.user.bean.UserPhoto;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.StringUtils;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.event.ToHim;
import com.vslk.lbgx.presenter.shopping.DressUpFragmentPresenter;
import com.vslk.lbgx.room.AVRoomActivity;
import com.vslk.lbgx.room.audio.activity.AudioRecordActivity;
import com.vslk.lbgx.room.avroom.other.ButtonItemFactory;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.ui.home.adpater.UserAlbumAdapter;
import com.vslk.lbgx.ui.me.shopping.adapter.GiftWallAdapter;
import com.vslk.lbgx.ui.me.shopping.view.IDressUpFragmentView;
import com.vslk.lbgx.ui.me.user.adapter.NewUserPhotoAdapter;
import com.vslk.lbgx.ui.me.user.adapter.PhotosBannerAdapter;
import com.vslk.lbgx.ui.me.user.adapter.UserPhotoAdapter;
import com.vslk.lbgx.ui.widget.ExpandableTextView;
import com.vslk.lbgx.ui.widget.MyRadioButton;
import com.vslk.lbgx.ui.widget.ObservableScrollView;
import com.vslk.lbgx.utils.HttpUtil;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.vslk.lbgx.utils.SessionHelper;
import com.vslk.lbgx.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;

/**
 * 用户中心
 *
 * @author zhouxiangfeng
 * @date 2017/5/24
 */
@CreatePresenter(DressUpFragmentPresenter.class)
public class UserInfoActivity extends BaseMvpActivity<IDressUpFragmentView, DressUpFragmentPresenter> implements
        View.OnClickListener, UserPhotoAdapter.ImageClickListener, NewUserPhotoAdapter.ImageClickListener,
        IDressUpFragmentView, ExpandableTextView.OnExpandableListener,
        ObservableScrollView.ScrollViewListener, RadioGroup.OnCheckedChangeListener {

    private ActivityUserInfoNewBinding infoBinding;

    private UserInfo userInfo;
    private long userId;
    private RoomInfo mRoomInfo;
    private GiftWallAdapter giftWallAdapter;
    //    private NewUserPhotoAdapter photoAdapter;
    private UserAlbumAdapter albumAdapter;
    private ArrayList<UserPhoto> photos = new ArrayList<>();

    private int playState = PlayState.NORMAL;
    private AudioPlayManager audioManager;
    private ClipboardManager mClipboardManager;


    private interface PlayState {
        int NORMAL = 0;
        int PLAYING = 1;
    }

    public static void start(Context context, long userId) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        infoBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_info_new);
        infoBinding.setClick(this);
        onFindViews();
        init();
        onSetListener();
        userId = getIntent().getLongExtra("userId", 0);
        userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(userId, true);
        infoBinding.clipboard.setOnClickListener(v -> {
            if (userInfo == null) {
                Toast.makeText(v.getContext(), "复制用户ID失败", Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(v.getContext(), "用户ID复制成功!", Toast.LENGTH_LONG).show();
            save2Clipboard();
            showByPlayState(false);
            save2Clipboard();
        });
        //修改新的注册监听方式防止内存泄露
        IMNetEaseManager.get().subscribeChatRoomEventObservable(roomEvent -> {
            if (roomEvent == null) {
                return;
            }
            int event = roomEvent.getEvent();
            switch (event) {
                case RoomEvent.KICK_OUT_ROOM:
                    if (roomEvent.getReason_no() == 3) {
//                        infoBinding.userRoom.setVisibility(View.GONE);
                    }
                    break;
                default:
            }
        }, this);
    }

    private void init() {
        audioManager = new AudioPlayManager(this, null, onPlayListener);
    }

    private void onFindViews() {
        //设置显示相册
        infoBinding.photoRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(infoBinding.photoRecyclerView);//初始化数据

//        photoAdapter = new NewUserPhotoAdapter(0,
//                (DisplayUtils.getScreenWidth(this) - ConvertUtils.dp2px(42)) / 4);
        albumAdapter = new UserAlbumAdapter(new ArrayList<>());
        infoBinding.photoRecyclerView.setAdapter(albumAdapter);
        albumAdapter.setOnItemClickListener(((adapter, view, position) -> {
            UserPhoto userPhoto = (UserPhoto) adapter.getData().get(position);
            click(position, userPhoto);
        }));

        //设置显示礼物数
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        infoBinding.giftRecyclerView.setNestedScrollingEnabled(false);
        infoBinding.giftRecyclerView.setLayoutManager(manager);

        giftWallAdapter = new GiftWallAdapter(this);
        infoBinding.giftRecyclerView.setAdapter(giftWallAdapter);
        mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        infoBinding.expandTextView.setTextSize(14);
    }

    private void onSetListener() {
        infoBinding.ivBack.setOnClickListener(view -> finish());
        infoBinding.expandTextView.setOnExpandableListener(this);
        infoBinding.scrollView.setScrollViewListener(this);
        infoBinding.rgGiftCarTab.setOnCheckedChangeListener(this);
    }

    private void initDatas(UserInfo userInfo) {
        if (null != userInfo) {
            ImageLoadUtils.loadImageWithBlurTransformation(this, userInfo.getAvatar(), infoBinding.userBigAvatar);
            //设置主页头部背景 -- 这里显示会有问题（暂时没有找到原因）需要固定高度布局中使用wrap_content会出现无法显示的问题
            ImageLoadUtils.loadCircleImage(this, userInfo.getAvatar(), infoBinding.avatar, R.drawable.ic_default_avatar);

            if (!StringUtil.isEmpty(userInfo.getHeadwearUrl())) {
                ImageLoadUtils.loadImage(this, userInfo.getHeadwearUrl(), infoBinding.headwear);
            }
            infoBinding.setUserInfo(userInfo);
            if (StringUtils.isNotEmpty(userInfo.getUserDesc())) {
                infoBinding.expandTextView.setText(userInfo.getUserDesc());
            } else {
                infoBinding.expandTextView.setText(getString(R.string.user_info_desc_empty));
            }
            infoBinding.levelViewUserInfo.setExperLevel(userInfo.getExperLevel());
            infoBinding.levelViewUserInfo.setCharmLevel(userInfo.getCharmLevel());
//            infoBinding.tvErbanId.setText("ID:" + userInfo.getErbanNo());
//            Log.e(TAG, "initDatas: " + userInfo.getErbanNo());
            requestRoomInfo(userInfo);

            photos.clear();
            RealmList<UserPhoto> photo = userInfo.getPrivatePhoto();
            if (!ListUtils.isListEmpty(photo)) {
                if (CoreManager.getCore(IAuthCore.class).getCurrentUid() == userId) {
                    if (photo.size() >= 9) {
                        photos.addAll(photo.subList(0, 9));
                    } else {
                        photos.addAll(photo);
                    }
                } else {
                    if (photo.size() >= 10) {
                        photos.addAll(photo.subList(0, 10));
                    } else {
                        photos.addAll(photo);
                    }
                }
            }
            //显示用户相册banner
            infoBinding.setShowBanner(photos.size() != 0);
            //判断是否是当前用户
            if (CoreManager.getCore(IAuthCore.class).getCurrentUid() == userId) {
                infoBinding.setIsUserSelf(true);
//                photoAdapter.setSelf(false);
                infoBinding.tvZoneTop.setText("我的空间");
            } else {
                if (photos.size() == 0) {
                    if (infoBinding.tvTip.getVisibility() == View.GONE) {
                        infoBinding.tvTip.setVisibility(View.VISIBLE);
                    }
                    if (infoBinding.photoRecyclerView.getVisibility() == View.VISIBLE) {
                        infoBinding.photoRecyclerView.setVisibility(View.GONE);
                    }
                } else {
                    if (infoBinding.tvTip.getVisibility() == View.VISIBLE) {
                        infoBinding.tvTip.setVisibility(View.GONE);
                    }
                    if (infoBinding.photoRecyclerView.getVisibility() == View.GONE) {
                        infoBinding.photoRecyclerView.setVisibility(View.VISIBLE);
                    }
                }
                //设置数据
                infoBinding.setIsUserSelf(false);
//                photoAdapter.setSelf(false);
                infoBinding.tvZoneTop.setText("Ta的空间");
            }
            albumAdapter.setNewData(photos);
//            photoAdapter.setPhotoUrls(photos);
            if (!ListUtils.isListEmpty(photos)) {
                PhotosBannerAdapter bannerAdapter = new PhotosBannerAdapter(photos, this);
                infoBinding.banner.setAdapter(bannerAdapter);
                infoBinding.banner.setAnimationDurtion(500);
                infoBinding.banner.setPlayDelay(3000);
            }
//            photoAdapter.setImageClickListener(this);
            if (CoreManager.getCore(IAuthCore.class).getCurrentUid() != userInfo.getUid()) {
                CoreManager.getCore(IPraiseCore.class).isPraised(CoreManager.getCore(IAuthCore.class).getCurrentUid(),
                        userInfo.getUid());
            }
//            if (TimeUtils.getAge(userInfo.getBirth()) > 0) {
//                infoBinding.ivGender.setText(String.valueOf(TimeUtils.getAge(userInfo.getBirth())));
//                infoBinding.ivGender.setSelected(userInfo.getGender() == 1);
//            }
            int gender = userInfo.getGender();
//            Log.e(TAG, "initDatas: gender " + gender);
            if (gender == 1) {
//                infoBinding.ivGender.setBackground(getResources().getDrawable(R.drawable.icon_man));
//                infoBinding.tvUserName.setCompoundDrawables();
                setCompoundDrawables(getResources().getDrawable(R.drawable.icon_man),
                        infoBinding.tvUserName);
            } else {
//                infoBinding.ivGender.setBackground(getResources().getDrawable(R.drawable.icon_woman));
                setCompoundDrawables(getResources().getDrawable(R.drawable.icon_woman),
                        infoBinding.tvUserName);
            }
//            String star = StarUtils.getConstellation(new Date(userInfo.getBirth()));
//            if (StringUtils.isNotEmpty(star)) {
//                infoBinding.tvUserStar.setVisibility(View.VISIBLE);
//                infoBinding.tvUserStar.setText(star);
//            }
            infoBinding.userWhere.setText("找Ta");
            infoBinding.userRoomTv.setText("房间");
            //获取礼物
            CoreManager.getCore(IUserCore.class).requestUserGiftWall(userId, 2);
        }
    }

    /**
     * 保存用户id
     */
    private void save2Clipboard() {
        ClipData clipData = ClipData.newPlainText("用户ID复制成功!", String.valueOf(userInfo.getErbanNo()));
        mClipboardManager.setPrimaryClip(clipData);
    }

    /**
     * @param @param drw
     * @return void
     * @desc 设置左边图标
     */
    /**
     * @paramleft 图片在左边
     * @paramtop 图片在上边
     * @paramright 图片在右边
     * @parambottom 图片在下边
     */
    public void setCompoundDrawables(Drawable drawable, TextView view) {

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, drawable, null);
    }

    /**
     * 获取房间信息
     */
    private void requestRoomInfo(UserInfo userInfo) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("uid", String.valueOf(userInfo.getUid()));
        param.put("visitorUid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        OkHttpManager.getInstance().getRequest(UriProvider.getRoomInfo(), param, new OkHttpManager.MyCallBack<ServiceResult<RoomInfo>>() {
            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onResponse(ServiceResult<RoomInfo> response) {
                if (response != null && response.isSuccess()) {
                    RoomInfo roomInfo = response.getData();
                    if (roomInfo == null) {
                        //该用户还未开房间
//                        infoBinding.userRoom.setVisibility(View.GONE);
                        return;
                    }
                    if (mRoomInfo != null && mRoomInfo.getRoomId() == roomInfo.getRoomId()) {
                        mRoomInfo = roomInfo;
                    } else if (mRoomInfo == null) {
                        //打开新的activity的时候
                        mRoomInfo = roomInfo;
                    }
//                    infoBinding.userRoom.setVisibility(roomInfo.isValid() ? View.VISIBLE : View.GONE);
                }
            }
        });
    }

    @Override
    public void click(int position, UserPhoto userPhoto) {
        ArrayList<UserPhoto> userPhotos1 = new ArrayList<>();
        userPhotos1.addAll(photos);
        Json photoDataJson = getPhotoDataJson(photos);
        if (photoDataJson == null) {
            return;
        }
        Intent intent = new Intent(this, ShowPhotoActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("photoJsonData", photoDataJson.toString());
        startActivity(intent);
    }

    private Json getPhotoDataJson(ArrayList<UserPhoto> photos) {
        if (photos == null) {
            return null;
        }
        Json json = new Json();
        for (int i = 0; i < photos.size(); i++) {
            UserPhoto userPhoto = photos.get(i);
            Json j = new Json();
            j.set("pid", userPhoto.getPid());
            j.set("photoUrl", userPhoto.getPhotoUrl());
            json.set(i + "", j.toString());
        }
        return json;
    }

    @Override
    public void addClick() {
        UIHelper.showModifyPhotosAct(this, userId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_play_audio:
                if (userInfo != null) {
                    if (!StringUtils.isEmpty(userInfo.getUserVoice())) {
                        showByPlayState(true);
                    } else {
                        if (userInfo.getUid() == CoreManager.getCore(IAuthCore.class).getCurrentUid()) {
                            AudioRecordActivity.start(this);
                        } else {
                            toast("TA还没有录制声音，快去提醒TA吧~");
                        }
                    }
                }
                break;
            case R.id.user_room:
                showByPlayState(false);
                if (mRoomInfo != null) {
                    AVRoomActivity.start(this, mRoomInfo.getUid(), mRoomInfo.getType());
                }
                break;
            //私聊
            case R.id.send_msg_layout:
                showByPlayState(false);
                if (userInfo == null) {
                    return;
                }
                boolean isFriend = CoreManager.getCore(IIMFriendCore.class).isMyFriend(userInfo.getUid() + "");
                HttpUtil.checkUserIsDisturb(this, isFriend, userId);
                break;
            case R.id.ll_attention:
                //防止空指针
                if (userInfo == null) {
                    return;
                }
                if (null != infoBinding && null != infoBinding.getIsLike() && infoBinding.getIsLike()) {
                    boolean isMyFriend = CoreManager.getCore(IIMFriendCore.class).isMyFriend(userInfo.getUid() + "");
                    String tip;
                    if (isMyFriend) {
                        tip = "取消关注将不再是好友关系，确定取消关注？";
                    } else {
                        tip = "确定取消关注？";
                    }
                    getDialogManager().showOkCancelDialog(tip, true, new DialogManager.OkCancelDialogListener() {
                        @Override
                        public void onCancel() {
                            getDialogManager().dismissDialog();
                        }

                        @Override
                        public void onOk() {
                            getDialogManager().dismissDialog();
                            getDialogManager().showProgressDialog(UserInfoActivity.this, "请稍后...");
                            CoreManager.getCore(IPraiseCore.class).cancelPraise(userInfo.getUid(), true);
                        }
                    });
                } else {
                    getDialogManager().showProgressDialog(UserInfoActivity.this, "请稍后...");
                    CoreManager.getCore(IPraiseCore.class).praise(userInfo.getUid());
                }
                break;
            case R.id.user_where:
                showByPlayState(false);
                ToHim.postToHim(userId, this.getClass().getName());
                break;
            case R.id.user_room_tv:
                showByPlayState(false);
                if (mRoomInfo != null && mRoomInfo.getUid() > 0) {
                    AVRoomActivity.start(this, mRoomInfo.getUid());
                } else {
                    toast("对方不在房间内");
                }
                break;
            case R.id.tv_defriend:
                showUserInfoMoreMenu();
                break;
            case R.id.iv_edit:
                showByPlayState(false);
                break;
            case R.id.iv_back:
                showByPlayState(false);
                finish();
                break;
//            case R.id.expand:
//                infoBinding.expandTextView.switchs();
//                break;
            default:
                break;
        }
    }

    // 0 头饰，1座驾
    private void getCarData() {
        getMvpPresenter().getMyDressUpData(1, userId);
    }

    private void getHeadDressData() {
        getMvpPresenter().getMyDressUpData(0, userId);
    }

    private void getGiftdata() {
        //获取礼物
        CoreManager.getCore(IUserCore.class).requestUserGiftWall(userId, 2);

    }

    //获取我的头饰成功
    @Override
    public void getHeadWearListSuccess(List<DressUpBean> result) {
        if (ListUtils.isListEmpty(result)) {
            showEmpty(0);
        } else {
            showNoEmpty();
        }
        giftWallAdapter.setGiftWallInfoList(null, result, 0);
    }

    //获取我的头饰失败
    @Override
    public void getHeadWearListSuccessFail(String msg) {
        showEmpty(0);
    }

    //获取我的座驾成功
    @Override
    public void getCarListSuccess(List<DressUpBean> result) {

        if (ListUtils.isListEmpty(result)) {
            showEmpty(1);
        } else {
            showNoEmpty();
        }
        giftWallAdapter.setGiftWallInfoList(null, result, 1);

    }

    //获取我的座驾失败
    @Override
    public void getCarListSuccessFail(String msg) {
        showEmpty(1);

    }

    @Override
    public void onExpandabled(boolean isExpand) {
//        infoBinding.ivExpand.setImageResource(isExpand ? R.drawable.ic_expand_less : R.drawable.ic_expand_more);
    }

    /**
     * 展示更多用户菜单
     */
    private void showUserInfoMoreMenu() {
        if (userId == SessionHelper.getSendFocusMsgUid()) {
            Map<String, String> params = CommonParamUtil.getDefaultParam();
            params.put("queryUid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
            params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
            params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
            OkHttpManager.getInstance().doPostRequest(UriProvider.getFocusMsgSwitch(), params, new OkHttpManager.MyCallBack<Json>() {
                @Override
                public void onError(Exception e) {
                    createUserMoreOperate(null);
                }

                @Override
                public void onResponse(Json response) {
                    if (response != null && response.num("code") == 200) {
                        Json json = response.json("data");
                        if (json != null) {
                            int likedSend = json.num("likedSend");
                            if (likedSend == 1 || likedSend == 2) {
                                createUserMoreOperate(new ButtonItem(likedSend == 1 ? "关闭消息提醒" : "开启消息提醒",
                                        () -> HttpUtil.saveFocusMsgState(
                                                CoreManager.getCore(IAuthCore.class).getCurrentUid(),
                                                likedSend == 1 ? 2 : 1)));
                            }
                        }
                    } else {
                        createUserMoreOperate(null);
                    }
                }
            });
        } else {
            createUserMoreOperate(null);
        }
    }

    /**
     * 生成用户功能按钮
     */
    private void createUserMoreOperate(ButtonItem focusItem) {
        if (userInfo != null) {
            List<ButtonItem> buttonItems = new ArrayList<>();
            ButtonItem msgBlackListItem = ButtonItemFactory.createMsgBlackListItem(!CoreManager.getCore(IIMFriendCore.class)
                    .isUserInBlackList(userId + "") ? "拉黑" : "取消拉黑", this::showDefriendDialog);
            buttonItems.add(ButtonItemFactory.createReportItem(UserInfoActivity.this, "举报", 1,
                    userInfo.getUid(), userInfo.getAvatar(), getMvpPresenter().getPhotosId(userInfo)));
            buttonItems.add(msgBlackListItem);
            if (focusItem != null) {
                buttonItems.add(focusItem);
            }
            getDialogManager().showCommonPopupDialog(buttonItems, getString(R.string.cancel));
        } else {
            toast("数据异常，请稍后再试!");
        }
    }

    private void showDefriendDialog() {
        if (CoreManager.getCore(IIMFriendCore.class).isUserInBlackList(userId + "")) {
            getDialogManager().showOkCancelDialog("是否取消拉黑", true, new DialogManager.AbsOkDialogListener() {
                @Override
                public void onOk() {
                    CoreManager.getCore(IIMFriendCore.class).removeFromBlackList("" + userId);
                }
            });
        } else {
            getDialogManager().showOkCancelDialog("加入黑名单后，将不再收到对方信息", true, new DialogManager.AbsOkDialogListener() {
                @Override
                public void onOk() {
                    CoreManager.getCore(IIMFriendCore.class).addToBlackList("" + userId);
                }
            });
        }
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onRequestUserInfo(UserInfo info) {
        if (info.getUid() == userId) {
            userInfo = info;
            initDatas(userInfo);
        }
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onCurrentUserInfoUpdate(UserInfo info) {
        if (info.getUid() == userId) {
            userInfo = info;
            initDatas(userInfo);
        }
    }

    @CoreEvent(coreClientClass = IIMFriendCoreClient.class)
    public void removeBlackListSuccess() {
        toast("已取消拉黑");
    }

    @CoreEvent(coreClientClass = IIMFriendCoreClient.class)
    public void addBlackListSuccess() {
        toast("已拉黑");
    }


    @CoreEvent(coreClientClass = IRoomCoreClient.class)
    public void onGetUserRoom(RoomInfo roomInfo) {
        long formalRoomId =
                BasicConfig.INSTANCE.isDebuggable() ? PublicChatRoomController.devRoomId : PublicChatRoomController.formalRoomId;
        if (roomInfo != null && roomInfo.getRoomId() == formalRoomId) {
            toast("对方不在房间内");
            return;
        }
        getDialogManager().dismissDialog();
        RoomInfo current = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo != null && roomInfo.getUid() > 0) {
            AVRoomActivity.start(this, roomInfo.getUid());
        } else {
            toast("对方不在房间内");
        }
    }

    @CoreEvent(coreClientClass = IRoomCoreClient.class)
    public void onGetUserRoomFail(String msg) {
        getDialogManager().dismissDialog();
        toast(msg);
    }

    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onIsLiked(boolean islike, long uid) {
        infoBinding.setIsLike(islike);
    }

    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onIsLikedFail(String error) {
        getDialogManager().dismissDialog();
        toast(error);
    }

    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onPraise(long likedUid) {
        getDialogManager().dismissDialog();
        toast("关注成功，相互关注可成为好友哦！");
        infoBinding.setIsLike(true);
    }

    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onCanceledPraise(long likedUid, boolean showNotice) {
        if (showNotice) {
            toast("取消关注成功");
        }
        getDialogManager().dismissDialog();
        infoBinding.setIsLike(false);
    }

    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onCanceledPraiseFaith(String error) {
        toast(error);
    }

    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onPraiseFaith(String error) {
        getDialogManager().dismissDialog();
        toast(error);
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onRequestGiftWall(List<GiftWallInfo> giftWallInfoList) {
        if (getDialogManager().isDialogShowing()) {
            getDialogManager().dismissDialog();
        }
        if (ListUtils.isListEmpty(giftWallInfoList)) {
            showEmpty(2);
            infoBinding.giftNumber.setVisibility(View.INVISIBLE);
        } else {
            showNoEmpty();
            int totalCount = 0;
            for (int i = 0; i < giftWallInfoList.size(); i++) {
                GiftWallInfo giftWallInfo = giftWallInfoList.get(i);
                totalCount += giftWallInfo.getReciveCount();
            }
            infoBinding.giftNumber.setVisibility(View.VISIBLE);
            infoBinding.giftNumber.setText(String.valueOf("(" + totalCount + ")"));
        }
        giftWallAdapter.setGiftWallInfoList(giftWallInfoList, null, 2);
    }

    private void showNoEmpty() {
        if (infoBinding.llUserGiftEmpty.getVisibility() == View.VISIBLE) {
            infoBinding.llUserGiftEmpty.setVisibility(View.GONE);
        }
        if (infoBinding.giftRecyclerView.getVisibility() == View.GONE) {
            infoBinding.giftRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onRequestGiftWallFail(String msg) {
        if (getDialogManager().isDialogShowing()) {
            getDialogManager().dismissDialog();
        }
        showEmpty(2);
    }

    private OnPlayListener onPlayListener = new OnPlayListener() {

        // 音频转码解码完成，会马上开始播放了
        @Override
        public void onPrepared() {

        }

        // 播放结束
        @Override
        public void onCompletion() {
            System.out.println("onCompletion");
            showByPlayState(false);
        }

        // 播放被中断了
        @Override
        public void onInterrupt() {
            System.out.println("onInterrupt");
            showByPlayState(false);
        }

        // 播放过程中出错。参数为出错原因描述
        @Override
        public void onError(String error) {
            System.out.println("onError");
            showByPlayState(false);
        }

        // 播放进度报告，每隔 500ms 会回调一次，告诉当前进度。 参数为当前进度，单位为毫秒，可用于更新 UI
        @Override
        public void onPlaying(long curPosition) {

        }
    };

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {

            case R.id.rb_tab_my_gift:
                changeBtnStatus(checkedId, 0);
                getGiftdata();
                break;

            case R.id.rb_tab_my_head_dress:
                changeBtnStatus(checkedId, 1);
                getHeadDressData();

                break;
            case R.id.rb_tab_my_car:
                changeBtnStatus(checkedId, 2);
                getCarData();
                break;
        }

    }


    //关联修改字体
    private void changeBtnStatus(int checkedId, int index) {

        int childCount = infoBinding.rgGiftCarTab.getChildCount();

        if (index == 0) {
            infoBinding.giftNumber.setVisibility(View.VISIBLE);
        } else {
            infoBinding.giftNumber.setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < childCount; i++) {

            //不改变礼物数量的字体
            if (i != 1) {
                MyRadioButton mMyRadioButton = (MyRadioButton) infoBinding.rgGiftCarTab.getChildAt(i);
                if (mMyRadioButton.getId() == checkedId) {
                    mMyRadioButton.setTextSize(16f);
                    mMyRadioButton.getPaint().setFakeBoldText(true);
                    mMyRadioButton.setTextColor(getResources().getColor(R.color.black));
                } else {
                    mMyRadioButton.getPaint().setFakeBoldText(false);
                    mMyRadioButton.setTextSize(14f);
                    mMyRadioButton.setTextColor(getResources().getColor(R.color.gray7));
                }
            }
        }
    }

    //alpha以及颜色渐变实现
    @Override
    public void onScrollChanged(NestedScrollView view, int x, int y, int oldx, int oldy) {

        //底部私聊的透明度渐变
        int scrollY = view.getScrollY();

        if (!(CoreManager.getCore(IAuthCore.class).getCurrentUid() == userId)) {

            //滑动的时候设置底部Button透明度

            //1.获取ScrollView的Y方向的滑动距离
            //2.获取屏幕高度
            int heightPixels = getResources().getDisplayMetrics().heightPixels;

            //3.设置滚动范围，当scrollY < 屏幕高度的1/3时，进行设置
            float v = heightPixels / 3f;

            //4.设置滚动的百分比

            float v1 = scrollY / v;//(0-1)
            //5.透明度应为1-0
            float alpha = 1 - v1;

            //设置是否可以点击
            if (alpha > 0.5f) {
                infoBinding.llBtnBottom.setVisibility(View.VISIBLE);
            } else {
                infoBinding.llBtnBottom.setVisibility(View.GONE);
            }

            infoBinding.llBtnBottom.setAlpha(alpha);

        }
        int height = infoBinding.rlTopCorner.getMeasuredHeight();

//        float scale = (float) scrollY / height;
//
//        float alpha_0_1 = 1 - (scale);

//        //顶部Button的背景色渐变
//        if (scrollY <= 0) {
//            //滑动之前，标题栏布局背景颜色为完全透明，标题文字完全透明
//            infoBinding.rlTopCorner.setBackgroundColor(Color.argb(0, 0, 0, 0));
//            infoBinding.tvZoneTop.setTextColor(Color.argb(0, 0, 0, 0));
//            infoBinding.tvDefriend.setTextColor(Color.argb(255, 255, 255, 255));
//
////            infoBinding.ivEdit.setVisibility(View.VISIBLE);
////            infoBinding.ivBackBlack.setVisibility(View.VISIBLE);
//
//            infoBinding.ivBackBlack.setAlpha(0f);
//            infoBinding.ivEditBlack.setAlpha(0f);
//            //让mScrollView滑动的距离在0~height之间时颜色发生渐变
//        } else if (scrollY <= height) {
//            //获取渐变率
//
//            //获取渐变数值
//            float alpha = (255 * scale);
//            //布局文本颜色逐渐发生变化
//            infoBinding.rlTopCorner.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
//            infoBinding.tvZoneTop.setTextColor(Color.argb((int) alpha, 0, 0, 0));
//            infoBinding.tvDefriend.setTextColor(Color.argb((int) alpha, 0, 0, 0));
//
////            infoBinding.ivEdit.setVisibility(View.INVISIBLE);
////            infoBinding.ivBackBlack.setVisibility(View.INVISIBLE);
//
//            infoBinding.ivBackBlack.setAlpha(scale);
//            infoBinding.ivEditBlack.setAlpha(scale);
//
//
//        } else {
//
//            //当滑动距离超过height，布局文本颜色完全不透明
//            infoBinding.rlTopCorner.setBackgroundColor(Color.argb(255, 255, 255, 255));
//
//        }


    }

    private void showByPlayState(boolean isPlay) {
        if (isPlay) {
            if (playState == PlayState.NORMAL) {
                playState = PlayState.PLAYING;
                //设置播放状态
                ImageLoadUtils.loadGifImage(this, R.drawable.micro_match_record_play_anim, infoBinding.playAnim);
//                infoBinding.play.setImageResource(R.drawable.ic_user_info_pause);
                audioManager.setDataSource(userInfo.getUserVoice());
                audioManager.play();
            } else {
                getDialogManager().showOkCancelDialog("当前正在播放或加载中，确定要停止播放吗?", false, new DialogManager.OkCancelDialogListener() {

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onOk() {
                        playState = PlayState.NORMAL;
                        //设置播放状态
                        infoBinding.playAnim.setImageResource(R.drawable.icon_micro_match_record_default);
//                        infoBinding.play.setImageResource(R.drawable.ic_user_info_play);
                        if (audioManager != null && audioManager.isPlaying()) {
                            audioManager.stopPlay();
                        }
                    }
                });
            }
        } else {
            playState = PlayState.NORMAL;
            //停止播放动画
            infoBinding.playAnim.setImageResource(R.drawable.icon_micro_match_record_default);
//            infoBinding.play.setImageResource(R.drawable.ic_user_info_play);
            if (audioManager != null && audioManager.isPlaying()) {
                audioManager.stopPlay();
            }
        }
    }

    private void showEmpty(int type) {

        if (infoBinding.llUserGiftEmpty.getVisibility() == View.GONE) {
            infoBinding.llUserGiftEmpty.setVisibility(View.VISIBLE);
        }
        if (infoBinding.giftRecyclerView.getVisibility() == View.VISIBLE) {
            infoBinding.giftRecyclerView.setVisibility(View.GONE);
        }
//        if (isError) {
//            infoBinding.giftEmpty.setText(StringUtils.isEmpty(msg) ? "网络异常" : msg);
//
//        } else
        if (type == 0) {
            infoBinding.giftEmpty.setText("暂没有头饰");
        } else if (type == 1) {
            infoBinding.giftEmpty.setText("暂没有座驾");
        } else if (type == 2) {
            infoBinding.giftEmpty.setText("暂没有收到礼物");

        }
    }
}
