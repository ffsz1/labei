package com.tongdaxing.xchat_core.manager;

import android.graphics.Point;
import android.text.TextUtils;
import android.util.SparseArray;

import com.netease.nimlib.sdk.chatroom.constant.MemberType;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.bean.RoomCharmInfo;
import com.tongdaxing.xchat_core.bean.RoomMemberComeInfo;
import com.tongdaxing.xchat_core.bean.RoomQueueInfo;
import com.tongdaxing.xchat_core.im.avroom.IAVRoomCore;
import com.tongdaxing.xchat_core.im.avroom.IAVRoomCoreClient;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.LogUtils;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <p> 全局变量，存储房间相关信息(周期与房间一样) </p>
 *
 * @author jiahui
 * @date 2017/12/11
 */
public final class AvRoomDataManager {
    public static final String TAG = "room_log ---> IM";
    private static volatile AvRoomDataManager mInstance;
    private static final Object SYNC_OBJECT = new Object();

    public RoomInfo mServiceRoominfo;
    /**
     * 当前房间信息
     */
    public volatile RoomInfo mCurrentRoomInfo;
    private long charmTimestamps = 0;
    /**
     * 房间创建者
     */
    public IMChatRoomMember mRoomCreateMember;
    /**
     * 自己的实体
     */
    public IMChatRoomMember mOwnerMember;
    /**
     * 房间管理员集合
     */
    public List<IMChatRoomMember> mRoomManagerList;
    /**
     * 房间普通成员
     */
    //: 2018/11/14 直接废弃
    public List<IMChatRoomMember> mRoomNormalMemberList;
    /**
     * 房间受限成员：禁言用户和黑名单用户都属于受限用户
     */
    //: 2018/11/14 直接废弃
    public List<IMChatRoomMember> mRoomLimitMemberList;
    /**
     * 固定成员：包括创建者,管理员,普通用户,受限用户
     */
    //: 2018/11/14 直接废弃
    @Deprecated
    public List<IMChatRoomMember> mRoomFixedMemberList;
    /**
     * 房间所有人员
     */
    //: 2018/11/14 处理后，废弃
    public List<IMChatRoomMember> mRoomAllMemberList;
    /**
     * 麦序位置信息：对应的位置，坑位信息（用户成员，坑位状态）
     */
    public SparseArray<RoomQueueInfo> mMicQueueMemberMap;
    /**
     * 记录每一个麦位的中心点位置
     */
    public SparseArray<Point> mMicPointMap;
    /**
     * * 戴帽子规则
     * 1、麦上最高魅力值的人戴帽子
     * 2、如果两个人魅力值一样，则不戴帽子。
     * 3、离开房间魅力值清空
     * 4、进入房间送了礼物才会显示魅力值（帽子）
     */
    private Map<String, RoomCharmInfo> mMicCharmInfo;
    /**
     * 需要处理的 房间人员进入 消息
     */
    private ConcurrentLinkedQueue<RoomMemberComeInfo> mMemberComeMsgQueue;

    public SparseArray<Json> mMicInListMap;

    /**
     * 是否需要打开麦克风,用户自己的行为，不受房主管理员的管理
     */
    public boolean mIsNeedOpenMic = true;
    private boolean hasCharm = false;//魅力值开关
    //是否开始播放全服
    private boolean isStartPlayFull = false;

    private long timestamp = 0;//房间人员进出时间戳


    private AvRoomDataManager() {
        mRoomManagerList = new ArrayList<>();
        mRoomFixedMemberList = new ArrayList<>();
        mRoomAllMemberList = new ArrayList<>();
        mMicQueueMemberMap = new SparseArray<>();
        mRoomNormalMemberList = new ArrayList<>();
        mRoomLimitMemberList = new ArrayList<>();
        mMicInListMap = new SparseArray<>();
        mMemberComeMsgQueue = new ConcurrentLinkedQueue<>();
        mMicCharmInfo = new HashMap<>();
    }

    public void release() {
        IMNetEaseManager.get().setImRoomConnection(false);
        IMNetEaseManager.get().setBeforeDisConnectionMuteStatus(0);
        RtcEngineManager.get().leaveChannel();
        clear();
    }

    public static AvRoomDataManager get() {
        if (mInstance == null) {
            synchronized (SYNC_OBJECT) {
                if (mInstance == null) {
                    mInstance = new AvRoomDataManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取麦上队列信息
     *
     * @param micPosition 麦的位置
     * @return 对应队列信息
     */
    public RoomQueueInfo getRoomQueueMemberInfoByMicPosition(int micPosition) {
        if (micPosition >= mMicQueueMemberMap.size()) {
            return null;
        }
        return mMicQueueMemberMap.get(micPosition);
    }

    /**
     * 获取自己在麦上队列信息
     *
     * @return 对应队列信息
     */
    public RoomQueueInfo getRoomQueueMemberInfoMyself() {
        return getRoomQueueMemberInfoByAccount(CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
    }

    /**
     * 获取麦上队列信息
     *
     * @param account 用户的id
     * @return 对应队列信息
     */
    public RoomQueueInfo getRoomQueueMemberInfoByAccount(String account) {
        if (TextUtils.isEmpty(account)) return null;
        RoomQueueInfo roomQueueInfo = null;
        int size = mMicQueueMemberMap.size();
        for (int i = 0; i < size; i++) {
            RoomQueueInfo roomQueueInfo1 = mMicQueueMemberMap.valueAt(i);
            if (roomQueueInfo1.mChatRoomMember != null &&
                    Objects.equals(roomQueueInfo1.mChatRoomMember.getAccount(), account)) {
                roomQueueInfo = roomQueueInfo1;
                break;
            }
        }
        return roomQueueInfo;
    }

    /**
     * 清除房间成员信息
     */
    public void clearMembers() {
        if (mOwnerMember != null) {
            mOwnerMember = null;
        }
        if (mRoomCreateMember != null) {
            mRoomCreateMember = null;
        }
        mRoomAllMemberList.clear();
        mRoomFixedMemberList.clear();
        mRoomManagerList.clear();
        mRoomLimitMemberList.clear();
        mRoomNormalMemberList.clear();
        mMemberComeMsgQueue.clear();
    }

    /**
     * 添加和更新魅力值列表
     *
     * @param updateInfo
     */
    public void addMicRoomCharmInfo(Map<String, RoomCharmInfo> updateInfo) {
        if (mMicCharmInfo == null) {
            mMicCharmInfo = new HashMap<>();
        }
        if (updateInfo != null && updateInfo.size() > 0) {
            for (Map.Entry<String, RoomCharmInfo> m : updateInfo.entrySet()) {
                mMicCharmInfo.put(m.getKey(), m.getValue());
            }
        }
    }

    /**
     * 添加和更新魅力值列表
     */
    public void addMicRoomCharmInfo(String userId, RoomCharmInfo roomCharmInfo) {
        if (mMicCharmInfo == null) {
            mMicCharmInfo = new HashMap<>();
        }
        if (!TextUtils.isEmpty(userId) && roomCharmInfo != null) {
            mMicCharmInfo.put(userId, roomCharmInfo);
        }
    }

    /**
     * 添加和更新魅力值列表
     *
     * @param account
     */
    public void removeMicRoomCharmInfo(String account) {
        if (mMicCharmInfo == null) {
            return;
        }
        if (mMicCharmInfo.size() > 0) {
            mMicCharmInfo.put(account, new RoomCharmInfo(0, false));
        }
    }

//    /**
//     * 假设麦上有A最高魅力值有帽子，如果A下麦，则B应用回调此方法，判断麦上是否有人符合戴帽子规则
//     */
//    public void refreshHat() {
//        if (mMicCharmInfo != null && mMicCharmInfo.isEmpty()) {
//            int maxCharm = 0;
//            String maxCharmUserId = null;
//            boolean isRepeat = false;//是否有两个魅力值相等
//            for (Map.Entry<String, RoomCharmInfo> charmInfoEntry : mMicCharmInfo.entrySet()) {
//                RoomCharmInfo charmInfo = charmInfoEntry.getValue();
//                if (charmInfo.getValue() > maxCharm) {
//                    maxCharm = charmInfo.getValue();
//                    maxCharmUserId = charmInfoEntry.getKey();
//                } else if (charmInfo.getValue() == maxCharm && maxCharm != 0) {
//                    isRepeat = true;//如果有多个人魅力值相等则不显示帽子
//                }
//            }
//
//            if (maxCharm > 0 && !TextUtils.isEmpty(maxCharmUserId)) {//更新帽子
//                addMicRoomCharmInfo(maxCharmUserId, new RoomCharmInfo(maxCharm, !isRepeat));
//            }
//        }
//    }

    public void clear() {
        clearMembers();

        if (mCurrentRoomInfo != null) {
            mCurrentRoomInfo = null;
        }
        // 进入房间无声音
        mIsNeedOpenMic = true;
        isMinimize = false;
        hasCharm = false;
        timestamp = 0;
        charmTimestamps = 0;
        mMicInListMap.clear();
        mMicCharmInfo.clear();
        mMicQueueMemberMap.clear();
        IMNetEaseManager.get().clear();
    }

    public IMChatRoomMember getChatRoomMember(String uid) {
        if (!ListUtils.isListEmpty(mRoomAllMemberList)) {
            for (IMChatRoomMember chatRoomMember : mRoomAllMemberList) {
                if (Objects.equals(chatRoomMember.getAccount(), uid)) {
                    return chatRoomMember;
                }
            }
        }
        return null;
    }

    public boolean hashatRoomMember(String uid) {
        boolean has = false;
        if (!ListUtils.isListEmpty(mRoomAllMemberList)) {
            for (IMChatRoomMember chatRoomMember : mRoomAllMemberList) {
                if (Objects.equals(chatRoomMember.getAccount(), uid)) {
                    has = true;
                    break;
                }
            }
        }
        return has;
    }

    public void addAdminMember(IMChatRoomMember chatRoomMember) {
        if (chatRoomMember == null || containsAdminMember(chatRoomMember.getAccount())) return;
        mRoomManagerList.add(chatRoomMember);
    }

    public boolean containsAdminMember(String uid) {
        for (IMChatRoomMember chatRoomMember : mRoomManagerList) {
            if (Objects.equals(chatRoomMember.getAccount(), String.valueOf(uid))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断该用户是第一次进来还是切换房间
     *
     * @param roomUid 要进入房间的用户id
     * @return
     */
    public boolean isFirstEnterRoomOrChangeOtherRoom(long roomUid) {
        return mCurrentRoomInfo == null || mCurrentRoomInfo.getUid() != roomUid;
    }

    /***
     * 是否是房间创建者
     * @param currentUid
     * @return
     */
    public boolean isRoomOwner(String currentUid) {
        return mCurrentRoomInfo != null && Objects.equals(String.valueOf(mCurrentRoomInfo.getUid()), currentUid);
    }

    /***
     * 是否是房间创建者
     * @param currentUid
     * @return
     */
    public boolean isRoomOwner(long currentUid) {
        return mCurrentRoomInfo != null && mCurrentRoomInfo.getUid() == currentUid;
    }

    /**
     * 是否是自己
     */
    public boolean isOwner(String currentUid) {
        return Objects.equals(currentUid, String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
    }

    /**
     * 是否是自己
     */
    public boolean isOwner(long currentUid) {
        return currentUid == CoreManager.getCore(IAuthCore.class).getCurrentUid();
    }

    public boolean isRoomOwner() {
        return mCurrentRoomInfo != null && mCurrentRoomInfo.getUid() == CoreManager.getCore(IAuthCore.class).getCurrentUid();
    }

    public void removeManagerMember(String account) {
        if (ListUtils.isListEmpty(mRoomManagerList) || TextUtils.isEmpty(account)) return;
        ListIterator<IMChatRoomMember> iterator = mRoomManagerList.listIterator();
        for (; iterator.hasNext(); ) {
            IMChatRoomMember chatRoomMember = iterator.next();
            if (Objects.equals(chatRoomMember.getAccount(), account)) {
                iterator.remove();
                break;
            }
        }
        if (AvRoomDataManager.get().isOwner(account) && mOwnerMember != null) {
            //自己是管理员被移除，恢复身份
            mOwnerMember.setMemberType(MemberType.NORMAL);
        }

    }

    public boolean isGuess() {
        return !isRoomAdmin() && !isRoomOwner();
    }

    public boolean isGuess(String account) {
        return !isRoomAdmin(account) && !isRoomOwner(account);
    }

    /**
     * 是否是房间管理员
     *
     * @return -
     */
    public boolean isRoomAdmin() {
        return isRoomAdmin(String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
    }

    public boolean isRoomAdmin(String account) {
        if (ListUtils.isListEmpty(mRoomManagerList)) return false;
        for (IMChatRoomMember chatRoomMember : mRoomManagerList) {
            if (Objects.equals(chatRoomMember.getAccount(), account)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 房主是否在线
     */
    public boolean isRoomOwnnerOnline() {
        return mRoomCreateMember != null;
    }

    /**
     * 判断指定用户id是否在麦上
     *
     * @param myUid 用户id
     * @return -
     */
    public boolean isOnMic(long myUid) {
        return isOnMic(String.valueOf(myUid));
    }

    /**
     * 判断指定用户id是否在麦上
     *
     * @param myUid 用户id
     * @return -
     */
    public boolean isOnMic(String myUid) {
        int size = mMicQueueMemberMap.size();
        for (int i = 0; i < size; i++) {
            RoomQueueInfo roomQueueInfo = mMicQueueMemberMap.valueAt(i);
            if (roomQueueInfo != null && roomQueueInfo.mChatRoomMember != null
                    && Objects.equals(roomQueueInfo.mChatRoomMember.getAccount(), myUid)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断自己是否在麦上
     *
     * @return -
     */
    public boolean isOwnerOnMic() {
        return isOnMic(CoreManager.getCore(IAuthCore.class).getCurrentUid());
    }

    /**
     * 根据用户id去获取当前用户在麦上的位置
     *
     * @param currentUid -
     * @return -
     */
    public int getMicPosition(long currentUid) {
        int size = mMicQueueMemberMap.size();
        for (int i = 0; i < size; i++) {
            RoomQueueInfo roomQueueInfo = mMicQueueMemberMap.valueAt(i);
            if (roomQueueInfo != null && roomQueueInfo.mChatRoomMember != null
                    && Objects.equals(roomQueueInfo.mChatRoomMember.getAccount(), String.valueOf(currentUid))) {
                return mMicQueueMemberMap.keyAt(i);
            }
        }
        //判断是否房主
        UserInfo roomOwner = CoreManager.getCore(IAVRoomCore.class).getRoomOwner();
        if (roomOwner != null) {
            if (roomOwner.getUid() == currentUid)
                return -1;
        }

        return Integer.MIN_VALUE;
    }

    /**
     * 即构是已streamID表示用户唯一信息
     *
     * @param streamID 流的格式  s-初始化时传入的用户uid-时间戳
     */
    public int getMicPositionByStreamID(String streamID) {
        if (StringUtils.isEmpty(streamID)) {
            return Integer.MIN_VALUE;
        }
        for (int index = 0; index < mMicQueueMemberMap.size(); index++) {
            RoomQueueInfo roomQueueInfo = mMicQueueMemberMap.valueAt(index);
            if (roomQueueInfo != null && roomQueueInfo.mChatRoomMember != null
                    && streamID.contains(roomQueueInfo.mChatRoomMember.getAccount())) {
                return mMicQueueMemberMap.keyAt(index);
            }
        }
        //判断是否房主
        UserInfo roomOwner = CoreManager.getCore(IAVRoomCore.class).getRoomOwner();
        if (roomOwner != null) {
            if (streamID.contains(roomOwner.getUid() + "")) {
                return -1;
            }
        }
        return Integer.MIN_VALUE;
    }

    /**
     * 根据用户id去获取当前用户在麦上的位置
     *
     * @param currentUid -
     * @return -
     */
    public int getMicPosition(String currentUid) {
        return getMicPosition(Long.valueOf(currentUid));
    }

    /**
     * 获取坑上没人的位置
     */
    public int findFreePosition() {
        int size;
        if (mMicQueueMemberMap != null && (size = mMicQueueMemberMap.size()) > 0) {
            for (int i = 0; i < size; i++) {
                int key = mMicQueueMemberMap.keyAt(i);
                RoomQueueInfo roomQueueInfo = mMicQueueMemberMap.valueAt(i);
                if (roomQueueInfo.mChatRoomMember == null) {
                    if (key == -1)
                        continue;
                    return key;
                }
            }
        }
        return Integer.MIN_VALUE;
    }

    /**
     * 获取坑上没人的位置,排除房主的位置，也就是-1
     */
    public int findFreePositionNoOwner() {
        int size;
        if (mMicQueueMemberMap != null && (size = mMicQueueMemberMap.size()) > 0) {
            for (int i = 0; i < size; i++) {
                int key = mMicQueueMemberMap.keyAt(i);
                if (key == -1) continue;
                RoomQueueInfo roomQueueInfo = mMicQueueMemberMap.valueAt(i);
                if (roomQueueInfo.mChatRoomMember == null && !roomQueueInfo.mRoomMicInfo.isMicLock()) {
                    return key;
                }
            }
        }
        return Integer.MIN_VALUE;
    }

    public void addRoomQueueInfo(String micPosition, RoomQueueInfo roomQueueInfo) {
        if (roomQueueInfo == null || roomQueueInfo.mChatRoomMember == null) return;
        Integer position = Integer.valueOf(micPosition);
        int size = AvRoomDataManager.get().mMicQueueMemberMap.size();
        //循环是移除重复的对象
        for (int i = 0; i < size; i++) {

            RoomQueueInfo temp = AvRoomDataManager.get().mMicQueueMemberMap.valueAt(i);
            if (temp.mChatRoomMember != null
                    && Objects.equals(temp.mChatRoomMember.getAccount(), roomQueueInfo.mChatRoomMember.getAccount())) {
                mMicQueueMemberMap.put(AvRoomDataManager.get().mMicQueueMemberMap.keyAt(i),
                        new RoomQueueInfo(temp.mRoomMicInfo, null));
            }
        }
        mMicQueueMemberMap.put(position, roomQueueInfo);
    }

    //防止最小化时候这时候的进场消息是不能进入队列的
    private boolean isMinimize = false;//是否最小化 true 最小化

    public void addMemberComeInfo(RoomMemberComeInfo memberComeInfo) {
        if (!isMinimize) {
            mMemberComeMsgQueue.offer(memberComeInfo);
        }
    }

    public RoomMemberComeInfo getAndRemoveFirstMemberComeInfo() {
        return mMemberComeMsgQueue.poll();
    }

    public int getMemberComeSize() {
        if (mMemberComeMsgQueue == null) {
            return 0;
        }
        return mMemberComeMsgQueue.size();
    }

    public void addMicInListInfo(String key, Json json) {
        if (json == null) return;
        Integer keyInt = Integer.valueOf(key);

        mMicInListMap.put(keyInt, json);
        CoreManager.notifyClients(IAVRoomCoreClient.class, IAVRoomCoreClient.onMicInListChange);
    }


    public void removeMicListInfo(String key) {
        if (TextUtils.isEmpty(key)) return;
        Integer keyInt = Integer.valueOf(key);

        removeMicListInfo(keyInt);
    }

    public void removeMicListInfo(int key) {

        mMicInListMap.remove(key);
        CoreManager.notifyClients(IAVRoomCoreClient.class, IAVRoomCoreClient.onMicInListChange);
    }


    public void resetMicMembers() {
        int size = mMicQueueMemberMap.size();
        for (int i = 0; i < size; i++) {
            RoomQueueInfo roomQueueInfo = mMicQueueMemberMap.valueAt(i);
            if (roomQueueInfo.mChatRoomMember != null) {
                LogUtils.d("remove  mChatRoomMember", 4 + "");
                roomQueueInfo.mChatRoomMember = null;
            }

        }
    }

    public boolean checkInMicInlist() {
        UserInfo cacheLoginUserInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
        if (cacheLoginUserInfo == null)
            return false;
        long uid = cacheLoginUserInfo.getUid();

        if (mMicInListMap.get((int) uid) != null)
            return true;

        return false;

    }

    TreeSet<Json> treeSet = new TreeSet<Json>((o1, o2) -> {
        String time1 = o1.str("time");
        String time2 = o2.str("time");
        long l1 = Long.parseLong(time1);
        long l2 = Long.parseLong(time2);
        if (l1 > l2) {
            return 1;
        } else {
            return -1;
        }


    });

    public Json getMicInListTopInfo() {

        if (mMicInListMap.size() < 1)
            return null;

        treeSet.clear();
        for (int i = 0; i < mMicInListMap.size(); i++) {
            treeSet.add(mMicInListMap.valueAt(i));

            LogUtils.d("micInListLog", "key:" + mMicInListMap.keyAt(i) + "   value:" + mMicInListMap.valueAt(i));
        }

        if (treeSet.size() > 0)
            return treeSet.first();
        return null;

    }


    public final static int MIC_FULL = -2;

    /**
     * 返回-2的话代表满了
     */
    public int checkHasEmpteyMic() {
        if (mMicQueueMemberMap == null) {
            return MIC_FULL;
        }

        for (int i = 0; i < mMicQueueMemberMap.size(); i++) {
            RoomQueueInfo roomQueueInfo = mMicQueueMemberMap.valueAt(i);
            int i1 = mMicQueueMemberMap.keyAt(i);
            if (roomQueueInfo.mChatRoomMember == null && i1 != -1 && !roomQueueInfo.mRoomMicInfo.isMicLock()) {

                return i1;
            }
        }
        return MIC_FULL;
    }

    public String getRoomRule() {
        if (mCurrentRoomInfo != null) {
            return mCurrentRoomInfo.getPlayInfo();
        }
        return "";
    }

    public Map<String, RoomCharmInfo> getmMicCharmInfo() {
        return mMicCharmInfo;
    }

    public void setmMicCharmInfo(Map<String, RoomCharmInfo> mMicCharmInfo) {
        this.mMicCharmInfo = mMicCharmInfo;
    }

    public void setHasCharm(boolean hasCharm) {
        this.hasCharm = hasCharm;
    }

    public boolean isHasCharm() {
        return hasCharm;
    }

    public long getCharmTimestamps() {
        return charmTimestamps;
    }

    public void setCharmTimestamps(long charmTimestamps) {
        this.charmTimestamps = charmTimestamps;
    }

    public void setMinimize(boolean minimize) {
        isMinimize = minimize;
    }

    public boolean isMinimize() {
        return isMinimize;
    }

    public void setStartPlayFull(boolean startPlayFull) {
        isStartPlayFull = startPlayFull;
    }

    public boolean isStartPlayFull() {
        return isStartPlayFull;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
