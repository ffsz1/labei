package com.vslk.lbgx.room.avroom.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.room.widget.dialog.AuctionFinishDialog;
import com.vslk.lbgx.room.widget.dialog.AuctionPlusDialog;
import com.vslk.lbgx.room.widget.dialog.NewUserInfoDialog;
import com.vslk.lbgx.ui.common.widget.CircleImageView;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_core.room.auction.bean.AuctionInfo;
import com.tongdaxing.xchat_core.room.auction.bean.AuctionUser;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.model.AuctionModel;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 竞拍界面
 * 暂时先在view层使用model获取数据
 *
 * @author chenran
 * @date 2017/7/26
 */
public class AuctionView extends RelativeLayout implements View.OnClickListener {
    private AuctionInfo auctionInfo;

    private CircleImageView auctionOwnerAvatar;
    private TextView auctionOwnerNick;

    private ImageView auctionFirstAvatar;
    private ImageView auctionSecondAvatar;
    private ImageView auctionThirdAvatar;

    private TextView auctionFirstNick;
    private TextView auctionSecondNick;
    private TextView auctionThirdNick;

    private TextView auctionFirstCorn;
    private TextView auctionSecondCorn;
    private TextView auctionThirdCorn;

    private UserInfo auctUserInfo;
    private UserInfo firstAuctUserInfo;
    private UserInfo secondAuctUserInfo;
    private UserInfo thirdAuctUserInfo;

    private ImageView auctionTipImage;
    private ImageView auctionBtnImage;

    private ImageView auctionListBtn;
    private Disposable subscribe;

    public AuctionView(Context context) {
        this(context, null);
    }

    public AuctionView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public AuctionView(Context context, AttributeSet attr, int i) {
        super(context, attr, i);
        init();
    }

    private void init() {
        CoreManager.addClient(this);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_auction_view, this, true);
        findView();
        updateAuction();
        subscribe = IMNetEaseManager.get().getChatRoomEventObservable().subscribe(new Consumer<RoomEvent>() {
            @Override
            public void accept(RoomEvent roomEvent) throws Exception {
                onReceiveRoomEvent(roomEvent);
            }
        });
    }

    private void onReceiveRoomEvent(RoomEvent roomEvent) {
        if (roomEvent == null || roomEvent.getEvent() == RoomEvent.NONE) return;
        switch (roomEvent.getEvent()) {
            case RoomEvent.AUCTION_UPDATE:
                onAuctionUpdate(roomEvent.getAuctionInfo());
                break;
            case RoomEvent.AUCTION_FINISH:
                onAuctionFinish(roomEvent.getAuctionInfo());
                break;
            case RoomEvent.AUCTION_START:
                onAuctionStart(roomEvent.getAuctionInfo());
                break;
            case RoomEvent.AUCTION_UPDATE_FAIL:
                onAuctionUpFail(roomEvent.getCode());
                break;
            case RoomEvent.ROOM_MANAGER_ADD:
            case RoomEvent.ROOM_MANAGER_REMOVE:
                updateAuction();
                break;
            default:
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (subscribe != null) {
            subscribe.dispose();
            subscribe = null;
            auctionInfo = null;
        }
    }

    public void onAuctionFinish(AuctionInfo auctionInfo) {
        this.auctionInfo = null;
        updateAuction();
        if (auctionInfo != null && auctionInfo.getRivals() != null && auctionInfo.getRivals().size() > 0) {
            AuctionFinishDialog dialog = new AuctionFinishDialog(getContext(), auctionInfo.getCurMaxUid(), auctionInfo.getAuctUid());
            dialog.show();
        }
    }

    public void onAuctionStart(AuctionInfo auctionInfo) {
        this.auctionInfo = auctionInfo;
        updateAuction();
    }

    public void onAuctionUpdate(AuctionInfo auctionInfo) {
        this.auctionInfo = auctionInfo;
        updateAuction();
    }

    public void onAuctionUpFail(int code) {
        if (code == 2101) {
            SingleToastUtil.showToast(BasicConfig.INSTANCE.getAppContext(), "出价失败，您出的价格少于当前最高价");
        } else if (code == 2103) {
            //
        } else {
            SingleToastUtil.showToast(BasicConfig.INSTANCE.getAppContext(), "加价失败");
        }
    }

    private void findView() {
        auctionOwnerAvatar = findViewById(R.id.auction_owner_avatar);
        auctionOwnerNick = findViewById(R.id.auction_owner_nick);
        auctionFirstAvatar = findViewById(R.id.auction_first_avatar);
        auctionSecondAvatar = findViewById(R.id.auction_second_avatar);
        auctionThirdAvatar = findViewById(R.id.auction_third_avatar);
        auctionFirstNick = findViewById(R.id.auction_first_nick);
        auctionSecondNick = findViewById(R.id.auction_second_nick);
        auctionThirdNick = findViewById(R.id.auction_third_nick);
        auctionFirstCorn = findViewById(R.id.auction_first_corn);
        auctionSecondCorn = findViewById(R.id.auction_second_corn);
        auctionThirdCorn = findViewById(R.id.auction_third_corn);

        auctionBtnImage = findViewById(R.id.auction_btn_image);
        auctionTipImage = findViewById(R.id.auction_tip_image);
       /* //加价按钮 。300行也改了，改成整个布局可点
        auctionBtnImage.setOnClickListener(this);*/
        auctionBtnImage.setOnClickListener(this);
        auctionTipImage.setOnClickListener(this);
        auctionOwnerAvatar.setOnClickListener(this);
        auctionFirstAvatar.setOnClickListener(this);
        auctionSecondAvatar.setOnClickListener(this);
        auctionThirdAvatar.setOnClickListener(this);
    }

    private void updateAuction() {
        updateActionState();
        updateAuctionUser();
        updateFirstAuctionUser();
        updateSecondAuctionUser();
        updateThirdAuctionUser();
    }

    private void updateActionState() {
        long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null || auctionInfo == null || !AuctionModel.get().isInAuctionNow()) {
            auctionBtnImage.setVisibility(GONE);
            auctionTipImage.setVisibility(GONE);
            return;
        }
        auctionBtnImage.setVisibility(VISIBLE);
        auctionTipImage.setVisibility(VISIBLE);
        if (roomInfo.getUid() == uid || AvRoomDataManager.get().isRoomAdmin(String.valueOf(uid))) {
            auctionBtnImage.setImageResource(R.drawable.icon_hammer_finish_auction);
            auctionTipImage.setImageResource(R.drawable.icon_hammer_tip_finish);
        } else if (auctionInfo.getAuctUid() == uid) {
            auctionBtnImage.setImageResource(R.drawable.icon_hammer);
            auctionTipImage.setImageResource(R.drawable.icon_hammer_tip);
        } else {
            auctionBtnImage.setImageResource(R.drawable.icon_hammer);
            auctionTipImage.setImageResource(R.drawable.icon_hammer_tip);
        }

    }

    private void updateAuctionUser() {
        if (auctionInfo != null) {
            auctUserInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(auctionInfo.getAuctUid());
            if (auctUserInfo != null) {
                auctionOwnerNick.setVisibility(VISIBLE);
                auctionOwnerNick.setText(auctUserInfo.getNick());
                Drawable drawable;
                if (auctUserInfo.getGender() == 1) {
                    Drawable drawMan = getResources().getDrawable(R.drawable.icon_man);
                    drawable = drawMan;
                } else {
                    Drawable drawFemale = getResources().getDrawable(R.drawable.icon_woman);
                    drawable = drawFemale;
                }
                auctionOwnerNick.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                ImageLoadUtils.loadAvatar(getContext(), auctUserInfo.getAvatar(), auctionOwnerAvatar);
            } else {
                auctionOwnerNick.setVisibility(INVISIBLE);
                auctionOwnerAvatar.setImageResource(R.drawable.default_auction_header);
            }
        } else {
            auctionOwnerNick.setVisibility(INVISIBLE);
            auctionOwnerAvatar.setImageResource(R.drawable.default_auction_header);
        }
    }

    private void updateFirstAuctionUser() {
        if (auctionInfo != null && auctionInfo.getRivals() != null && auctionInfo.getRivals().size() > 0) {
            AuctionUser auctionUser = auctionInfo.getRivals().get(0);
            firstAuctUserInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(auctionUser.getUid());
            if (firstAuctUserInfo != null) {
                auctionFirstNick.setVisibility(VISIBLE);
                auctionFirstCorn.setVisibility(VISIBLE);

                auctionFirstNick.setText(firstAuctUserInfo.getNick());
                auctionFirstCorn.setText(auctionUser.getAuctMoney() + "");
                ImageLoadUtils.loadAvatar(getContext(), firstAuctUserInfo.getAvatar(), auctionFirstAvatar, true);
            } else {
                auctionFirstNick.setVisibility(INVISIBLE);
                auctionFirstCorn.setVisibility(INVISIBLE);
                auctionFirstAvatar.setImageResource(R.drawable.icon_default_auction_avatar);
            }
        } else {
            auctionFirstNick.setVisibility(INVISIBLE);
            auctionFirstCorn.setVisibility(INVISIBLE);
            auctionFirstAvatar.setImageResource(R.drawable.icon_default_auction_avatar);
        }
    }

    private void updateSecondAuctionUser() {
        if (auctionInfo != null && auctionInfo.getRivals() != null && auctionInfo.getRivals().size() > 1) {
            AuctionUser auctionUser = auctionInfo.getRivals().get(1);
            secondAuctUserInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(auctionUser.getUid());
            if (secondAuctUserInfo != null) {
                auctionSecondNick.setVisibility(VISIBLE);
                auctionSecondCorn.setVisibility(VISIBLE);

                auctionSecondNick.setText(secondAuctUserInfo.getNick());
                auctionSecondCorn.setText(auctionUser.getAuctMoney() + "");
                ImageLoadUtils.loadAvatar(getContext(), secondAuctUserInfo.getAvatar(), auctionSecondAvatar, true);
            } else {
                auctionSecondNick.setVisibility(INVISIBLE);
                auctionSecondCorn.setVisibility(INVISIBLE);
                auctionSecondAvatar.setImageResource(R.drawable.icon_default_auction_avatar);
            }
        } else {
            auctionSecondNick.setVisibility(INVISIBLE);
            auctionSecondCorn.setVisibility(INVISIBLE);
            auctionSecondAvatar.setImageResource(R.drawable.icon_default_auction_avatar);
        }
    }

    private void updateThirdAuctionUser() {
        if (auctionInfo != null && auctionInfo.getRivals() != null && auctionInfo.getRivals().size() > 2) {
            AuctionUser auctionUser = auctionInfo.getRivals().get(2);
            thirdAuctUserInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(auctionUser.getUid());
            if (thirdAuctUserInfo != null) {
                auctionThirdNick.setVisibility(VISIBLE);
                auctionThirdCorn.setVisibility(VISIBLE);

                auctionThirdNick.setText(thirdAuctUserInfo.getNick());
                auctionThirdCorn.setText(auctionUser.getAuctMoney() + "");
                ImageLoadUtils.loadAvatar(getContext(), thirdAuctUserInfo.getAvatar(), auctionThirdAvatar, true);
            } else {
                auctionThirdNick.setVisibility(INVISIBLE);
                auctionThirdCorn.setVisibility(INVISIBLE);
                auctionThirdAvatar.setImageResource(R.drawable.icon_default_auction_avatar);
            }
        } else {
            auctionThirdNick.setVisibility(INVISIBLE);
            auctionThirdCorn.setVisibility(INVISIBLE);
            auctionThirdAvatar.setImageResource(R.drawable.icon_default_auction_avatar);
        }
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onRequestUserInfo(UserInfo info) {
        updateAuction();
    }

    public void release() {
        CoreManager.removeClient(this);
        if (subscribe != null) {
            subscribe.dispose();
            subscribe = null;
            auctionInfo = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.auction_btn_image:
            case R.id.auction_tip_image:
                long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
                boolean isAdmin = AvRoomDataManager.get().isRoomAdmin(String.valueOf(uid));
                final RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
                if (roomInfo == null || auctionInfo == null) return;
                if (uid == auctionInfo.getAuctUid() &&
                        AvRoomDataManager.get().isGuess(String.valueOf(auctionInfo.getAuctUid()))) {
                    SingleToastUtil.showToast(null, "您正在被竞拍，无法加价");
                } else if (roomInfo.getUid() == uid || isAdmin) {
                    ((BaseMvpActivity) getContext()).getDialogManager().showOkCancelDialog(
                            "是否结束当前竞拍?", true,
                            new DialogManager.AbsOkDialogListener() {
                                @Override
                                public void onOk() {
                                    AuctionModel.get().finishAuction(roomInfo.getUid(),
                                            auctionInfo.getAuctId()).subscribe();
                                }
                            });
                } else {
                    showAuctionPlusDialog();
                }

                break;
            case R.id.auction_owner_avatar:
                if (auctionInfo != null && auctionInfo.getAuctUid() != 0)
                    showUserInfoDialog(auctionInfo.getAuctUid());
                break;
            case R.id.auction_first_avatar:
                if (firstAuctUserInfo != null)
                    showUserInfoDialog(firstAuctUserInfo.getUid());
                break;
            case R.id.auction_second_avatar:
                if (secondAuctUserInfo != null)
                    showUserInfoDialog(secondAuctUserInfo.getUid());
                break;
            case R.id.auction_third_avatar:
                if (thirdAuctUserInfo != null)
                    showUserInfoDialog(thirdAuctUserInfo.getUid());
                break;
            default:
        }
    }

    private void showUserInfoDialog(long uid) {
        NewUserInfoDialog.showUserDialog(getContext(),uid);
    }

    private void showAuctionPlusDialog() {
        AuctionPlusDialog plusDialog = new AuctionPlusDialog(getContext(), auctionInfo);
        plusDialog.setAuctionPlusDialogItemClickListener(new AuctionPlusDialog.AuctionPlusDialogItemClickListener() {
            @Override
            public void onClickDoPlus(int price) {
                if (auctionInfo != null) {
                    RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
                    AuctionModel.get().onAuctionUp(roomInfo.getUid(),
                            CoreManager.getCore(IAuthCore.class).getCurrentUid(),
                            auctionInfo.getAuctId(), 1, price).subscribe();
                }
            }
        });
        plusDialog.show();
    }
}
