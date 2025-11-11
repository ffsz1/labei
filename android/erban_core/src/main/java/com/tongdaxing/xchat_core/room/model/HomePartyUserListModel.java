package com.tongdaxing.xchat_core.room.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.netease.nimlib.sdk.chatroom.constant.MemberType;
import com.orhanobut.logger.Logger;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.bean.RoomQueueInfo;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.room.bean.OnlineChatMember;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * <p> 轰趴房用户列表网络处理 </p>
 *
 * @author jiahui
 * @date 2017/12/8
 */
public class HomePartyUserListModel extends RoomBaseModel {

    /**
     * 分页获取房间成员：第一页包含队列成员，固定成员，游客50人，之后每一页获取游客50人
     *
     * @param page 页数
     * @param time 固定成员列表用updateTime,
     *             游客列表用进入enterTime，
     *             填0会使用当前服务器最新时间开始查询，即第一页，单位毫秒
     */
    public Single<List<OnlineChatMember>> getPageMembers(final int page, long time,
                                                         final List<OnlineChatMember> oldList) {
        if (page == Constants.PAGE_START) {
            Single<List<IMChatRoomMember>> onlineUserObservable = queryOnlineList(500);
            Single<List<IMChatRoomMember>> firstGuestObservable = queryGuestList(ROOM_MEMBER_SIZE, 0);

            return Single.zip(onlineUserObservable, firstGuestObservable,
                    new BiFunction<List<IMChatRoomMember>, List<IMChatRoomMember>, List<OnlineChatMember>>() {
                        @Override
                        public List<OnlineChatMember> apply(List<IMChatRoomMember> onlineChatRoomMemberList,
                                                            List<IMChatRoomMember> guestChatRoomMemberList) throws Exception {
                            return getChatRoomMemberList(onlineChatRoomMemberList, guestChatRoomMemberList);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            return queryGuestList(ROOM_MEMBER_SIZE, time)
                    .map(new Function<List<IMChatRoomMember>, List<OnlineChatMember>>() {
                        @Override
                        public List<OnlineChatMember> apply(List<IMChatRoomMember> chatRoomMemberList) throws Exception {
                            if (!ListUtils.isListEmpty(chatRoomMemberList)) {
                                Logger.i("第%1d页游客在线人数:%2d", page, chatRoomMemberList.size());
                                List<IMChatRoomMember> list = null;
                                if (!ListUtils.isListEmpty(oldList)) {
                                    list = new ArrayList<>(oldList.size());
                                    for (OnlineChatMember temp : oldList) {
                                        if (temp.chatRoomMember == null) continue;
                                        list.add(temp.chatRoomMember);
                                    }
                                }
                                return getChatRoomMemberList(list, chatRoomMemberList);
                            }
                            return null;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }




    @NonNull
    private List<OnlineChatMember> getChatRoomMemberList(List<IMChatRoomMember> oldList,
                                                         List<IMChatRoomMember> newList) {
        long startTime = System.currentTimeMillis();
       /* try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        //处理耗时的循环
        List<OnlineChatMember> allMemberList = OnlineChatMember.coverToOnlineChatMember(oldList, newList);

        List<OnlineChatMember> part1MemberList = new ArrayList<>();
        List<OnlineChatMember> limitMemberList = new ArrayList<>();
        List<OnlineChatMember> managerMemberList = new ArrayList<>();
        List<OnlineChatMember> normalMemberList = new ArrayList<>();
        List<OnlineChatMember> onMicMemberList = new ArrayList<>();
        List<OnlineChatMember> guestMemberList = new ArrayList<>();

        int size = AvRoomDataManager.get().mMicQueueMemberMap.size();
        boolean isRoomOwnerOnline = false;
        IMChatRoomMember chatRoomMember;
        for (OnlineChatMember temp : allMemberList) {
            chatRoomMember = temp.chatRoomMember;
            if (chatRoomMember == null) continue;
            String account = chatRoomMember.getAccount();
            MemberType memberType = chatRoomMember.getMemberType();
            //自己
            if (AvRoomDataManager.get().isOwner(account)) {
                AvRoomDataManager.get().mOwnerMember = chatRoomMember;
            }
            //在麦上集合处理
            boolean isOnMic = false;
            for (int i = 0; i < size; i++) {
                RoomQueueInfo roomQueueInfo = AvRoomDataManager.get().mMicQueueMemberMap.valueAt(i);
                if (roomQueueInfo.mChatRoomMember != null
                        && Objects.equals(roomQueueInfo.mChatRoomMember.getAccount(), account)) {
                    temp.isOnMic = true;
                    if (memberType == MemberType.CREATOR) {
                        isRoomOwnerOnline = true;
                        //房主在麦上
                        temp.isRoomOwer = true;
                        onMicMemberList.add(0, temp);
                    } else if (memberType == MemberType.ADMIN) {
                        //管理员在麦上
                        temp.isAdmin = true;
                        onMicMemberList.add(temp);
                    } else {
                        onMicMemberList.add(temp);
                    }
                    isOnMic = true;
                }
            }
            if (isOnMic) continue;

            //处理不再麦上的
            if (memberType == MemberType.ADMIN) {
                temp.isAdmin = true;
                managerMemberList.add(temp);
            } else if (memberType == MemberType.CREATOR) {
                isRoomOwnerOnline = chatRoomMember.isIs_online();
                AvRoomDataManager.get().mRoomCreateMember = chatRoomMember;
            } else if (chatRoomMember.isIs_black_list() || chatRoomMember.isIs_mute()) {
                limitMemberList.add(temp);
            } else if (memberType == MemberType.NORMAL) {
                normalMemberList.add(temp);
            } else if (memberType == MemberType.GUEST) {
                guestMemberList.add(temp);
            }
        }

        //房主
        if (isRoomOwnerOnline) {
            if (AvRoomDataManager.get().mRoomCreateMember != null) {
                if (!ListUtils.isListEmpty(onMicMemberList)
                        && !AvRoomDataManager.get().isRoomOwner(onMicMemberList.get(0).chatRoomMember.getAccount())) {
                    part1MemberList.add(0, new OnlineChatMember(AvRoomDataManager.get().mRoomCreateMember,
                            false, false, true));
                } else if (ListUtils.isListEmpty(onMicMemberList)) {
                    //处理麦上没有人的情况
                    part1MemberList.add(0, new OnlineChatMember(AvRoomDataManager.get().mRoomCreateMember,
                            false, false, true));
                }
            }
        }
        //上麦用户
        if (!ListUtils.isListEmpty(onMicMemberList)) {
            part1MemberList.addAll(onMicMemberList);
        }
        //管理员
        if (!ListUtils.isListEmpty(managerMemberList)) {
            part1MemberList.addAll(managerMemberList);
        }

        //固定在线普通成员
        if (!ListUtils.isListEmpty(normalMemberList)) {
            part1MemberList.addAll(normalMemberList);
        }

        //添加游客
        if (!ListUtils.isListEmpty(guestMemberList)) {
            part1MemberList.addAll(guestMemberList);
        }
        Logger.i("循环处理在线顺序列表耗时：" + (System.currentTimeMillis() - startTime));
        return part1MemberList;
    }

    /**
     * 成员进来刷新在线列表
     *
     * @param account           进来成员的账号
     * @param onlineChatMembers 成员列表
     */
    public Single<List<OnlineChatMember>> onMemberInRefreshData(String account, int page,
                                                                final List<OnlineChatMember> onlineChatMembers) {
        if (TextUtils.isEmpty(account)) return Single.error(new Throwable("account 不能为空"));
        List<String> accounts = new ArrayList<>(1);
        accounts.add(account);
        return IMNetEaseManager.get().fetchRoomMembersByIds(accounts)
                .observeOn(Schedulers.io())
                .map(new Function<List<IMChatRoomMember>, List<OnlineChatMember>>() {
                    @Override
                    public List<OnlineChatMember> apply(List<IMChatRoomMember> chatRoomMemberList) throws Exception {
                        if (ListUtils.isListEmpty(chatRoomMemberList)) return onlineChatMembers;
                        return /*getChatRoomMemberList(chatRoomMemberList, OnlineChatMember.converOnlineToNormal(onlineChatMembers))*/onlineChatMembers;
                    }
                })
                .delay(page == Constants.PAGE_START ? 2 : 0, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<OnlineChatMember>> onMemberDownUpMic(final String account,
                                                            final boolean isUpMic,
                                                            final List<OnlineChatMember> dataList) {
        if (TextUtils.isEmpty(account)) return Single.error(new Throwable("account 不能为空"));
        return Single.create(
                new SingleOnSubscribe<List<OnlineChatMember>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<OnlineChatMember>> e) throws Exception {
                        if (ListUtils.isListEmpty(dataList)) e.onSuccess(dataList);
                        int size = dataList.size();
                        for (int i = 0; i < size; i++) {
                            OnlineChatMember onlineChatMember = dataList.get(i);
                            if (onlineChatMember.chatRoomMember != null
                                    && Objects.equals(onlineChatMember.chatRoomMember.getAccount(), account)) {
                                onlineChatMember.isOnMic = isUpMic;
                            }
                        }
                        e.onSuccess(dataList);
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<OnlineChatMember>> onUpdateMemberManager(final String account,
                                                                final boolean isRemoveManager,
                                                                final List<OnlineChatMember> dataList) {
        return Single.create(
                new SingleOnSubscribe<List<OnlineChatMember>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<OnlineChatMember>> e) throws Exception {
                        if (ListUtils.isListEmpty(dataList)) e.onSuccess(dataList);
                        int size = dataList.size();
                        for (int i = 0; i < size; i++) {
                            OnlineChatMember onlineChatMember = dataList.get(i);
                            if (onlineChatMember.chatRoomMember != null
                                    && Objects.equals(onlineChatMember.chatRoomMember.getAccount(), account)) {
                                onlineChatMember.isAdmin = !isRemoveManager;
                            }
                        }
                        e.onSuccess(dataList);
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
