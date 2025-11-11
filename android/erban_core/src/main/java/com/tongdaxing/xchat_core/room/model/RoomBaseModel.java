package com.tongdaxing.xchat_core.room.model;

import android.text.TextUtils;
import android.util.SparseArray;

import com.netease.nimlib.sdk.NIMChatRoomSDK;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.chatroom.constant.MemberQueryType;
import com.tongdaxing.erban.libcommon.listener.CallBack;
import com.tongdaxing.erban.libcommon.net.exception.ErrorThrowable;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.bean.RoomMicInfo;
import com.tongdaxing.xchat_core.bean.RoomQueueInfo;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.BaseMvpModel;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.ReUsedSocketManager;
import com.tongdaxing.xchat_core.room.bean.OnlineChatMember;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.im.IMProCallBack;
import com.tongdaxing.xchat_framework.im.IMReportBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * <p>房间相关操作model </p>
 *
 * @author jiahui
 * @date 2017/12/19
 */
public class RoomBaseModel extends BaseMvpModel {
    /**
     * 一页房间人数
     */
    protected static final int ROOM_MEMBER_SIZE = 50;

    /**
     * 获取最新固定在线人数(操作在子线程，需要在主线程操作的自己转)
     *
     * @param limit 请求数量
     */
    public Single<List<IMChatRoomMember>> queryOnlineList(int limit) {
        return fetchRoomMembers(MemberQueryType.ONLINE_NORMAL, 0, limit);
    }

    /**
     * 获取在线游客列表 (操作在子线程，需要在主线程操作的自己转)
     *
     * @param limit 获取数量大小
     * @param time  从当前时间开始查找
     */
    public Single<List<IMChatRoomMember>> queryGuestList(int limit, long time) {
        return fetchRoomMembers(MemberQueryType.GUEST, time, limit);
    }

    private Single<List<IMChatRoomMember>> fetchRoomMembers(final MemberQueryType memberType, final long time, final int limit) {
        final RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) {
            return Single.error(new ErrorThrowable(ErrorThrowable.ROOM_INFO_NULL_ERROR));
        }
        return Single.create(new SingleOnSubscribe<List<com.netease.nimlib.sdk.chatroom.model.ChatRoomMember>>() {
            @Override
            public void subscribe(SingleEmitter<List<com.netease.nimlib.sdk.chatroom.model.ChatRoomMember>> e) throws Exception {
                executeNIMClient(NIMClient.syncRequest(NIMChatRoomSDK.getChatRoomService()
                        .fetchRoomMembers(String.valueOf(roomInfo.getRoomId()), memberType, time, limit)), e);
            }
        }).map(chatRoomMembers -> {
            List<IMChatRoomMember> members = new ArrayList<>();
            if (chatRoomMembers != null && chatRoomMembers.size() > 0) {
                for (int i = 0; i < chatRoomMembers.size(); i++) {
                    IMChatRoomMember chatRoomMember = new IMChatRoomMember(chatRoomMembers.get(i));
                    members.add(chatRoomMember);
                }
            }
            return members;
        }).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io());
    }

    /**
     * 设置管理员
     *
     * @param roomId
     * @param account
     * @param mark    true:设置管理员 ,false：移除管理员
     */
    public void markManagerList(long roomId, String account, boolean mark, OkHttpManager.MyCallBack callBack) {
        IMNetEaseManager.get().markManager(account, mark, callBack);
    }

    /**
     * @param account 被操作的用户uid
     * @param is_add  1添加，0移除
     */
    public void markBlackList(String account, boolean is_add, OkHttpManager.MyCallBack myCallBack) {
        IMNetEaseManager.get().markBlackList(account, is_add, myCallBack);
    }

    /**
     * 邀请上麦
     *
     * @param micUid   上麦用户uid
     * @param position
     * @return Single<ChatRoomMessage>
     */
    public void inviteMicroPhone(String nickName, long micUid, int position) {
        IMNetEaseManager.get().inviteMicroPhoneBySdk(nickName, micUid, position);
    }

    /**
     * 上麦
     *
     * @param micPosition
     * @param uId           要上麦的用户id
     * @param roomId
     * @param isInviteUpMic 是否是主动的
     * @param callBack
     */
    public void upMicroPhone(final int micPosition, final String uId, final String roomId,
                             boolean isInviteUpMic, final CallBack<String> callBack) {
        RoomQueueInfo roomQueueInfo = AvRoomDataManager.get().mMicQueueMemberMap.get(micPosition);
        if (roomQueueInfo == null) {
            return;
        }
        IMChatRoomMember chatRoomMember = roomQueueInfo.mChatRoomMember;
        final RoomMicInfo roomMicInfo = roomQueueInfo.mRoomMicInfo;

        //坑上没人且没锁
        if (roomMicInfo != null && ((!roomMicInfo.isMicLock() || AvRoomDataManager.get().isRoomOwner(uId)
                || AvRoomDataManager.get().isRoomAdmin(uId))
                || isInviteUpMic)
                && chatRoomMember == null) {
            final UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
            if (userInfo != null) {
                updateQueueEx(micPosition, roomId, callBack, userInfo, -1);
            }
        }
    }

    /**
     * 下麦
     */
    public void downMicroPhone(int micPosition, final CallBack<String> callBack) {
        IMNetEaseManager.get().downMicroPhoneBySdk(micPosition, callBack);
    }

    public void updateQueueEx(int micPosition, String roomId, final CallBack<String> callBack, UserInfo userInfo, int type) {
        updataQueueExBySdk(micPosition, roomId, callBack, userInfo.getUid(), true);
    }

    public void updataQueueExBySdk(int micPosition, String roomId, final CallBack<String> callBack, long uid, boolean isTransient) {
        //防止房主掉麦
        if (micPosition == -1) {
            String currentUid = String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid());
            if (!AvRoomDataManager.get().isRoomOwner(currentUid)) {
                return;
            }
        }
        ReUsedSocketManager.get().updateQueue(roomId, micPosition, uid, new IMProCallBack() {
            @Override
            public void onSuccessPro(IMReportBean imReportBean) {
                if (imReportBean == null || imReportBean.getReportData() == null) {
                    if (callBack != null) {
                        callBack.onFail(-1, "数据异常！");
                    }
                    return;
                }
                if (imReportBean.getReportData().errno == 0) {
                    if (callBack != null) {
                        callBack.onSuccess("上麦成功");
                    }
                } else {
                    if (callBack != null) {
                        callBack.onFail(imReportBean.getReportData().errno, "上麦失败:" + imReportBean.getReportData().errmsg);
                    }
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                if (callBack != null) {
                    callBack.onFail(errorCode, errorMsg);
                }
            }
        });
    }

    public void getMembers(int index, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> imDefaultParamsMap = IMNetEaseManager.get().getImDefaultParamsMap();
        if (imDefaultParamsMap == null) {
            return;
        }

        imDefaultParamsMap.put("limit", "100");
        imDefaultParamsMap.put("start", String.valueOf(index));
        OkHttpManager.getInstance().doPostRequest(UriProvider.fetchRoomMembers(), imDefaultParamsMap, myCallBack);
    }

    public List<OnlineChatMember> memberToOnlineMember(List<IMChatRoomMember> imChatRoomMembers, boolean filterOnMic, List<OnlineChatMember> oldList) {
        Set<String> micUIds = new HashSet<>();
        //排除麦上的用户
        SparseArray<RoomQueueInfo> mMicQueueMemberMap = AvRoomDataManager.get().mMicQueueMemberMap;
        for (int i = 0; i < mMicQueueMemberMap.size(); i++) {
            RoomQueueInfo roomQueueInfo = mMicQueueMemberMap.valueAt(i);
            if (roomQueueInfo.mChatRoomMember != null) {
                String account = roomQueueInfo.mChatRoomMember.getAccount();
                micUIds.add(account);
            }
        }
        Set<String> newListUids = new HashSet<>();
        List<OnlineChatMember> onlineChatMembers = new ArrayList<>();

        for (IMChatRoomMember member : imChatRoomMembers) {
            String account = member.getAccount();
            boolean onMic = micUIds.contains(account);
            //过滤麦上用户
            newListUids.add(account);
            if (onMic && filterOnMic) {
                continue;
            }
            onlineChatMembers.add(new OnlineChatMember(member, onMic, member.isIs_manager(), AvRoomDataManager.get().isRoomOwner(account)));
        }

        if (!ListUtils.isListEmpty(oldList)) {
            //排重
            List<OnlineChatMember> repeatData = new ArrayList<>();
            for (OnlineChatMember onlineChatMember : oldList) {
                IMChatRoomMember chatRoomMember = onlineChatMember.chatRoomMember;
                if (chatRoomMember == null) {
                    continue;
                }
                String account = chatRoomMember.getAccount();
                if (newListUids.contains(account)) {
                    repeatData.add(onlineChatMember);
                }
            }
            oldList.removeAll(repeatData);
            onlineChatMembers.addAll(0, oldList);
        }

        onlineChatMembers = multipleSort(onlineChatMembers);
        return onlineChatMembers;
    }

    /**
     * 房主-管理-游客排序,用户身份有可能会变化，需要重新排序
     * 排序规则
     * 1、房主优先
     * 2、上麦优先
     * 3、管理员优先
     * 4、活人优先
     * 5、机器人
     */
    private List<OnlineChatMember> multipleSort(List<OnlineChatMember> onlineChatMembers) {//FIXME 暂时先用此方法处理，后期再优化
        List<OnlineChatMember> temp = new ArrayList<>();
        if (ListUtils.isListEmpty(onlineChatMembers)) {
            return temp;
        }
        for (int i = onlineChatMembers.size() - 1; i >= 0; i--) {//把房主放第一位
            if (onlineChatMembers.get(i).isRoomOwer) {
                temp.add(onlineChatMembers.remove(i));
                break;
            }
        }

        for (int i = onlineChatMembers.size() - 1; i >= 0; i--) {//上麦优先
            if (onlineChatMembers.get(i).isOnMic) {
                temp.add(onlineChatMembers.remove(i));
            }
        }

        for (int i = onlineChatMembers.size() - 1; i >= 0; i--) {//管理员优先
            if (onlineChatMembers.get(i).isAdmin) {
                temp.add(onlineChatMembers.remove(i));
            }
        }

        for (int i = onlineChatMembers.size() - 1; i >= 0; i--) {//活人优先
            IMChatRoomMember imChatRoomMember = onlineChatMembers.get(i).chatRoomMember;
            if (imChatRoomMember != null && !TextUtils.isEmpty(imChatRoomMember.getDef_user()) && imChatRoomMember.getDef_user().equals("1")) {
                temp.add(onlineChatMembers.remove(i));
            }
        }
        temp.addAll(onlineChatMembers);
        return temp;
    }

    /* *
     * 查询房间管理员列表
     *
     * @param myCallBack
     */
    public void fetchRoomManagers(OkHttpManager.MyCallBack myCallBack) {
        OkHttpManager.getInstance().doPostRequest(UriProvider.fetchRoomManagers(), IMNetEaseManager.get().getImDefaultParamsMap(), myCallBack);
    }

    /**
     * 查询房间黑名单列表
     */
    public void fetchRoomBlackList(int start, int limit, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> imDefaultParamsMap = IMNetEaseManager.get().getImDefaultParamsMap();
        if (imDefaultParamsMap == null) {
            return;
        }
        imDefaultParamsMap.put("limit", limit + "");
        imDefaultParamsMap.put("start", start + "");
        OkHttpManager.getInstance().doPostRequest(UriProvider.fetchRoomBlackList(), imDefaultParamsMap, myCallBack);
    }
}
