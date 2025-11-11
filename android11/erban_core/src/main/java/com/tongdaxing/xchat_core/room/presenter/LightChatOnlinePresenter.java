package com.tongdaxing.xchat_core.room.presenter;

import android.annotation.SuppressLint;
import android.util.SparseArray;

import com.orhanobut.logger.Logger;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.bean.OnlineChatMember;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.model.AuctionModel;
import com.tongdaxing.xchat_core.room.model.HomePartyUserListModel;
import com.tongdaxing.xchat_core.room.view.ILightChatOnlineView;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>  </p>
 *
 * @author jiahui
 * @date 2017/12/25
 */
public class LightChatOnlinePresenter extends AbstractMvpPresenter<ILightChatOnlineView> {
    private final HomePartyUserListModel mHomePartyUserListMode;

    public LightChatOnlinePresenter() {
        mHomePartyUserListMode = new HomePartyUserListModel();
    }

    /**
     * 分页获取房间成员：第一页包含队列成员，固定成员，游客50人，之后每一页获取游客50人
     *
     * @param page 页数
     * @param time 固定成员列表用updateTime,
     *             游客列表用进入enterTime，
     *             填0会使用当前服务器最新时间开始查询，即第一页，单位毫秒
     */
    public void requestChatMemberByPage(final int page, long time, List<OnlineChatMember> oldList) {
        mHomePartyUserListMode.getPageMembers(page, time, oldList)
                .subscribe(onlineChatMembers -> {
                    Logger.i("第%1d页成员人数:%2d", page, onlineChatMembers.size());
                    if (getMvpView() != null) {
                        getMvpView().onRequestChatMemberByPageSuccess(onlineChatMembers, page);
                    }
                }, throwable -> {
                    Logger.i("第%d页成员人数失败:%s", page, throwable.getMessage());
                    if (getMvpView() != null) {
                        getMvpView().onRequestChatMemberByPageFail(throwable.getMessage(), page);
                    }
                })
        ;
    }


    public void onItemClick(OnlineChatMember onlineChatMember) {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null || onlineChatMember == null || onlineChatMember.chatRoomMember == null) {
            return;
        }
        int position = AvRoomDataManager.get().findFreePositionNoOwner();
        SparseArray<ButtonItem> buttonItemList = null;
        if (getMvpView() != null && position != Integer.MIN_VALUE) {
            buttonItemList = getMvpView().getButtonItemList(onlineChatMember.chatRoomMember, position);
        }
        if (buttonItemList == null || buttonItemList.size() == 0) {
            return;
        }

        List<ButtonItem> buttonItems = new ArrayList<>();
//        //是否是自己
        if (AvRoomDataManager.get().isOwner(onlineChatMember.chatRoomMember.getAccount())) {
            if (roomInfo.getType() == RoomInfo.ROOMTYPE_AUCTION &&
                    !AuctionModel.get().isInAuctionNow() && !AvRoomDataManager.get().isGuess()) {
                // 查看资料
                buttonItems.add(buttonItemList.get(ButtonItem.SEND_SHOW_USER_INCO_ITEM));
                // 发起竞拍
                buttonItems.add(buttonItemList.get(ButtonItem.START_AUCTION));
                if (getMvpView() != null) {
                    getMvpView().showItemClickDialog(buttonItems);
                }
                return;
            } else {
                if (getMvpView() != null) {
                    getMvpView().showUserInfoDialog(onlineChatMember.chatRoomMember.getAccount());
                }
                return;
            }
        }
        if (AvRoomDataManager.get().isRoomOwner()) {
            //房主点击
            roomOwnerClick(onlineChatMember, buttonItemList, buttonItems);
        } else if (AvRoomDataManager.get().isRoomAdmin()) {
            //管理员点击
            roomAdminClick(onlineChatMember, buttonItemList, buttonItems);
        } else {
            //游客点击
            //弹出个人资料
            if (getMvpView() != null) {
                getMvpView().showUserInfoDialog(onlineChatMember.chatRoomMember.getAccount());
            }
        }

    }

    private void roomAdminClick(OnlineChatMember onlineChatMember, SparseArray<ButtonItem> buttonItemList,
                                List<ButtonItem> buttonItems) {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        int type = roomInfo.getType();
        if (onlineChatMember.isRoomOwer) {
            if (type == RoomInfo.ROOMTYPE_AUCTION && !AuctionModel.get().isInAuctionNow()) {
                buttonItems.add(buttonItemList.get(ButtonItem.SEND_SHOW_USER_INCO_ITEM));
                // 发起竞拍
                buttonItems.add(buttonItemList.get(ButtonItem.START_AUCTION));
                if (getMvpView() != null) {
                    getMvpView().showItemClickDialog(buttonItems);
                }
            } else {
                if (getMvpView() != null) {
                    getMvpView().showUserInfoDialog(onlineChatMember.chatRoomMember.getAccount());
                }
            }
        } else if (onlineChatMember.isAdmin) {
            if (onlineChatMember.isOnMic) {
                if (type == RoomInfo.ROOMTYPE_AUCTION) {
                    // 在麦上,在竞拍中,显示资料框
                    if (AuctionModel.get().isInAuctionNow()) {
                        if (getMvpView() != null) {
                            getMvpView().showUserInfoDialog(onlineChatMember.chatRoomMember.getAccount());
                        }
                    } else {
                        // 在麦上,不在竞拍中,查看资料,发起竞拍
                        buttonItems.add(buttonItemList.get(ButtonItem.SEND_SHOW_USER_INCO_ITEM));
                        buttonItems.add(buttonItemList.get(ButtonItem.START_AUCTION));
                        if (getMvpView() != null) {
                            getMvpView().showItemClickDialog(buttonItems);
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().showUserInfoDialog(onlineChatMember.chatRoomMember.getAccount());
                    }
                }
            } else {
                if (type == RoomInfo.ROOMTYPE_AUCTION) {
                    // 不在麦上,在竞拍中,查看资料
                    if (AuctionModel.get().isInAuctionNow()) {
                        buttonItems.add(buttonItemList.get(ButtonItem.SEND_SHOW_USER_INCO_ITEM));
                        buttonItems.add(buttonItemList.get(ButtonItem.SEND_INVITE_MIC_ITEM));
                        if (getMvpView() != null) {
                            getMvpView().showItemClickDialog(buttonItems);
                        }
                    } else {
                        // 不在麦上,不在竞拍中,查看资料,发起竞拍,抱Ta上麦
                        buttonItems.add(buttonItemList.get(ButtonItem.SEND_SHOW_USER_INCO_ITEM));
                        buttonItems.add(buttonItemList.get(ButtonItem.START_AUCTION));
                        buttonItems.add(buttonItemList.get(ButtonItem.SEND_INVITE_MIC_ITEM));
                        if (getMvpView() != null) {
                            getMvpView().showItemClickDialog(buttonItems);
                        }
                    }
                } else {
                    //抱它上麦，查看资料
                    buttonItems.add(buttonItemList.get(ButtonItem.SEND_SHOW_USER_INCO_ITEM));
                    buttonItems.add(buttonItemList.get(ButtonItem.SEND_INVITE_MIC_ITEM));
                    if (getMvpView() != null) {
                        getMvpView().showItemClickDialog(buttonItems);
                    }
                }
            }
        } else {
            // 游客
            // 查看资料，踢出房间，加入黑名单
            buttonItems.add(buttonItemList.get(ButtonItem.SEND_SHOW_USER_INCO_ITEM));
            if (roomInfo.getType() == RoomInfo.ROOMTYPE_AUCTION) {
                if (!AuctionModel.get().isInAuctionNow()) {
                    buttonItems.add(buttonItemList.get(ButtonItem.START_AUCTION));
                }
            }
            if (!onlineChatMember.isOnMic) {
                //抱它上麦
                buttonItems.add(buttonItemList.get(ButtonItem.SEND_INVITE_MIC_ITEM));
            }
            buttonItems.add(buttonItemList.get(ButtonItem.SEND_KICKOUT_ROOM_ITEM));
            buttonItems.add(buttonItemList.get(ButtonItem.SEND_MARK_BLACK_ITEM));
            if (getMvpView() != null) {
                getMvpView().showItemClickDialog(buttonItems);
            }
        }
    }

    private void roomOwnerClick(OnlineChatMember onlineChatMember, SparseArray<ButtonItem> buttonItemList,
                                List<ButtonItem> buttonItems) {
        // 房间类型
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        int type = roomInfo.getType();
        if (onlineChatMember.isAdmin) {
            //管理员

            //1. 查看资料
            //2. 踢出房间
            //3. 取消管理员
            //4. 加入黑名单
            buttonItems.add(buttonItemList.get(ButtonItem.SEND_SHOW_USER_INCO_ITEM));
            if (type == RoomInfo.ROOMTYPE_AUCTION && !AuctionModel.get().isInAuctionNow()) {
                // 发起竞拍
                buttonItems.add(buttonItemList.get(ButtonItem.START_AUCTION));
            }
            if (!onlineChatMember.isOnMic) {
                //不在麦上
                //1. 抱他上麦
                buttonItems.add(buttonItemList.get(ButtonItem.SEND_INVITE_MIC_ITEM));
            }
            buttonItems.add(buttonItemList.get(ButtonItem.SEND_KICKOUT_ROOM_ITEM));
            buttonItems.add(buttonItemList.get(ButtonItem.SEND_NOMARK_MANAGER_ITEM));
            buttonItems.add(buttonItemList.get(ButtonItem.SEND_MARK_BLACK_ITEM));
        } else {
            //非管理员

            //1. 查看资料
            //2. 踢出房间
            //3. 设置管理员
            //4. 加入黑名单
            buttonItems.add(buttonItemList.get(ButtonItem.SEND_SHOW_USER_INCO_ITEM));
            if (type == RoomInfo.ROOMTYPE_AUCTION && !AuctionModel.get().isInAuctionNow()) {
                // 发起竞拍
                buttonItems.add(buttonItemList.get(ButtonItem.START_AUCTION));
            }
            if (!onlineChatMember.isOnMic) {
                //不在麦上
                //1. 抱他上麦
                buttonItems.add(buttonItemList.get(ButtonItem.SEND_INVITE_MIC_ITEM));
            }
            buttonItems.add(buttonItemList.get(ButtonItem.SEND_KICKOUT_ROOM_ITEM));
            buttonItems.add(buttonItemList.get(ButtonItem.SEND_MARK_MANAGER_ITEM));
            buttonItems.add(buttonItemList.get(ButtonItem.SEND_MARK_BLACK_ITEM));
        }
        if (getMvpView() != null)
            getMvpView().showItemClickDialog(buttonItems);
    }

    /**
     * 成员进来刷新在线列表
     *
     * @param account
     * @param onlineChatMembers
     */
    public void onMemberInRefreshData(String account, List<OnlineChatMember> onlineChatMembers, final int page) {
//        mHomePartyUserListMode.onMemberInRefreshData(account, page, onlineChatMembers)
//                .subscribe(new Consumer<List<OnlineChatMember>>() {
//                    @Override
//                    public void accept(List<OnlineChatMember> onlineChatMembers) throws Exception {
//                        if (getMvpView() != null)
//                            getMvpView().onRequestChatMemberByPageSuccess(onlineChatMembers, page);
//                    }
//                });

    }

    @SuppressLint("CheckResult")
    public void onMemberDownUpMic(String account, boolean isUpMic, List<OnlineChatMember> dataList,
                                  final int page) {
        mHomePartyUserListMode.onMemberDownUpMic(account, isUpMic, dataList)
                .subscribe(onlineChatMembers -> {
                    if (getMvpView() != null)
                        getMvpView().onRequestChatMemberByPageSuccess(onlineChatMembers, page);
                });
    }


    @SuppressLint("CheckResult")
    public void onUpdateMemberManager(String account, List<OnlineChatMember> dataList,
                                      boolean isRemoveManager, final int page) {
        mHomePartyUserListMode.onUpdateMemberManager(account, isRemoveManager, dataList)
                .subscribe(onlineChatMembers -> {
                    if (getMvpView() != null)
                        getMvpView().onRequestChatMemberByPageSuccess(onlineChatMembers, page);
                });
    }

}
