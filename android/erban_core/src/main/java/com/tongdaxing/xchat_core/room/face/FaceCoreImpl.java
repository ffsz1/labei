package com.tongdaxing.xchat_core.room.face;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.tongdaxing.erban.libcommon.net.rxnet.RequestManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.DemoCache;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.bean.RoomQueueInfo;
import com.tongdaxing.xchat_core.im.custom.bean.CustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.FaceAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.RoomMatchAttachment;
import com.tongdaxing.xchat_core.initial.InitModel;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.ReUsedSocketManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.http.ProgressInfo;
import com.tongdaxing.xchat_framework.http_image.http.ProgressListener;
import com.tongdaxing.xchat_framework.http_image.http.RequestError;
import com.tongdaxing.xchat_framework.http_image.http.ResponseErrorListener;
import com.tongdaxing.xchat_framework.http_image.http.ResponseListener;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.im.IMProCallBack;
import com.tongdaxing.xchat_framework.im.IMReportBean;
import com.tongdaxing.xchat_framework.im.IMReportRoute;
import com.tongdaxing.xchat_framework.util.util.DESUtils;
import com.tongdaxing.xchat_framework.util.util.NetworkUtils;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.tongdaxing.xchat_framework.util.util.codec.MD5Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import io.reactivex.functions.BiConsumer;

import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_FACE;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MATCH;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_FACE_SEND;

/**
 * @author xiaoyu
 * @date 2017/12/8
 */

public class FaceCoreImpl extends AbstractBaseCore implements IFaceCore {

    private static final String TAG = "FaceCoreImpl";
    private boolean isShowingFace;

    /**
     * 服务器中json
     */
    private FaceListInfo onlineFacesList;
    /**
     * 本地的保存的json
     */
    private FaceListInfo offlineFaceList;

    /**
     * 表情包根目录
     */
    private File facesRootDir;
    private File facesZipPath;

    public FaceCoreImpl() {
        CoreManager.addClient(this);
        //: 2017/11/29
        String offlineEncryptString = DemoCache.readFaceList();

        try {
            offlineFaceList = new Gson().fromJson(DESUtils.DESAndBase64Decrypt(offlineEncryptString), FaceListInfo.class);

        } catch (Exception e) {
            e.printStackTrace();

        }

        // data/data/{包名}/files/faces/
        // data/data/{包名}/files/faces/{version}
        // data/data/{包名}/files/faces/{version}/{表情拼音}
        // data/data/{包名}/files/faces/{version}/{表情拼音}/表情图片
        facesRootDir = new File(getContext().getApplicationContext().getFilesDir() + "/faces");
        facesZipPath = new File(facesRootDir.getAbsolutePath() + "/face.zip");
        if (!facesRootDir.exists()) {
            facesRootDir.mkdirs();
        }
        // 写进离线的faceInfoList中的图片根目录
        setPicRootDirectoryIntoFaceInfo(offlineFaceList);
    }

    @Override
    public boolean isShowingFace() {
        return isShowingFace;
    }

    private boolean hasFacesZipPacket(String zipMd5) {
        boolean has = false;
        File zip = new File(facesRootDir.getAbsolutePath() + "/face.zip");
        // 检测是否有对应version的zip表情
        try {
            long time = System.currentTimeMillis();
            String md5 = MD5Utils.getFileMD5String(zip);
            if (zipMd5.equalsIgnoreCase(md5)) {
                has = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return has;
    }

    @Override
    public void getOnlineFaceJsonOrZip() {
        // 如果获取不了在线表情json,表明app初始化失败
        if (onlineFacesList == null) {
            InitModel.get().init();
//            .subscribe(new BiConsumer<ServiceResult<InitInfo>, Throwable>() {
//                @Override
//                public void accept(ServiceResult<InitInfo> initResult, Throwable throwable) throws Exception {
//                    if (initResult != null && initResult.getData() != null && initResult.getData().getFaceJson() != null) {
//                        onReceiveOnlineFaceJson(initResult.getData().getFaceJson().getJson());
//                    }
//                }
//            });
        } else if (!hasFacesZipPacket(onlineFacesList.getZipMd5())) {
            // 有在线json,但是没有对应的表情包
            getOnlineFaceZipFile();
        }
    }

    @Override
    public void onReceiveOnlineFaceJson(String encrypt) {
        if (StringUtil.isEmpty(encrypt)) {
            return;
        }
        // 保证facesRootDir存在
        if (!facesRootDir.exists()) {
            facesRootDir.mkdirs();
        }
        FaceListInfo faceListInfo = null;
        try {
            faceListInfo = new Gson().fromJson(DESUtils.DESAndBase64Decrypt(encrypt), FaceListInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onlineFacesList = faceListInfo;
        DemoCache.saveFaceList(encrypt);
        // 没有服务器对应版本的表情zip包
        // 检测当前表情版本是否落后
        boolean versionOutDated = (offlineFaceList != null &&
                offlineFaceList.getVersion() < onlineFacesList.getVersion());
        if (versionOutDated ||
                !hasFacesZipPacket(onlineFacesList.getZipMd5())) {
            // 获取对应版本的表情zip包
            getOnlineFaceZipFile();
        } else {
            // 如果有对应的zip包,判断需不需要解压对应的zip包
            File faceRoot = new File(facesRootDir.getAbsolutePath()
                    + "/" + onlineFacesList.getVersion());
            if (!faceRoot.exists() ||
                    faceRoot.list().length <= 0) {
                unzipFaceZipFile(onlineFacesList);
            } else {
                // 如果有对应的zip包,并且已经解压了,则设置图片的根目录到FaceInfo中
                setPicRootDirectoryIntoFaceInfo(onlineFacesList);
            }
        }
    }

    /**
     * 寻找对应的表情包图片的根目录,如果没有则设置一个默认的
     *
     * @param faceListInfo--
     */
    private void setPicRootDirectoryIntoFaceInfo(FaceListInfo faceListInfo) {
        if (faceListInfo == null ||
                facesRootDir == null) {
            return;
        }
        File picRootDirectory = null;
        File tmp = new File(facesRootDir.getAbsolutePath() + "/" + faceListInfo.getVersion());
        if (!tmp.exists()) {
            return;
        } else {
            File[] files = tmp.listFiles();
            if (files.length == 1 && files[0].list().length > 0) {
                // 压缩包里面有根目录的情况
                picRootDirectory = files[0];
            } else if (files.length > 1) {
                // 压缩包里面没有根目录的情况
                picRootDirectory = tmp;
            }
        }
        // 如果没有找到,设置一个默认的
        if (picRootDirectory == null) {
            picRootDirectory = new File(facesRootDir.getAbsolutePath() + "/" + faceListInfo.getVersion() + "/face");
        }

        // 设置表情包的根目录到所有的FaceInfo中
        for (int i = 0; i < faceListInfo.getFaces().size(); i++) {
            FaceInfo faceInfo = faceListInfo.getFaces().get(i);
            faceInfo.setPicturesRootDirectory(picRootDirectory.getAbsolutePath());
        }

    }


    private boolean isRequestingZip;

    // 表情压缩未修改为最新
    @Override
    public void getOnlineFaceZipFile() {
        if (isRequestingZip) return;
        isRequestingZip = true;
        ResponseListener<String> listener = new ResponseListener<String>() {
            @Override
            public void onResponse(String response) {
                unzipFaceZipFile(onlineFacesList);
                isRequestingZip = false;
            }
        };

        ProgressListener progressListener = new ProgressListener() {
            @Override
            public void onProgress(ProgressInfo info) {

            }
        };

        ResponseErrorListener errorListener = new ResponseErrorListener() {
            @Override
            public void onErrorResponse(RequestError error) {
                isRequestingZip = false;
            }
        };

        if (facesZipPath.exists()) {
            // 删除旧的zip包
            facesZipPath.delete();
        } else {
            // 创建其父目录
            if (!facesZipPath.getParentFile().exists()) {
                facesZipPath.getParentFile().mkdirs();
            }
        }

        String url = onlineFacesList.getZipUrl();
        RequestManager.instance()
                .submitDownloadRequest(url,
                        CommonParamUtil.getDefaultHeaders(getContext()),
                        facesZipPath.getAbsolutePath(), listener, errorListener,
                        progressListener, false);
    }

    @Override
    public void sendRoomMatchAbandon(int second) {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        final RoomMatchAttachment matchAttachment = new RoomMatchAttachment(IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MATCH, second);
        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
        if (userInfo != null) {
            matchAttachment.setUid(userInfo.getUid());
            matchAttachment.setNick(userInfo.getNick());
            matchAttachment.setExperLevel(userInfo.getExperLevel());
            ChatRoomMessage message = new ChatRoomMessage();
            message.setRoom_id(String.valueOf(roomInfo.getRoomId()));
            message.setAttachment(matchAttachment);
            ReUsedSocketManager.get().sendCustomMessage(String.valueOf(roomInfo.getRoomId()), message, new IMProCallBack() {
                @Override
                public void onSuccessPro(IMReportBean imReportBean) {
                }

                @Override
                public void onError(int errorCode, String errorMsg) {

                }
            });

        }
    }

    /**
     * 解压缩对应版本的zip包,并且告知FaceInfo的根目录
     *
     * @param faceListInfo--
     */
    private void unzipFaceZipFile(FaceListInfo faceListInfo) {
        try {
            // 如果zip包不存在,或者说zip包的md5值不一样则返回
            if (!facesZipPath.exists() || faceListInfo == null ||
                    !faceListInfo.getZipMd5().equalsIgnoreCase(MD5Utils.getFileMD5String(facesZipPath))) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long startTime = System.currentTimeMillis();
        try {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(facesZipPath));
            BufferedInputStream bis = new BufferedInputStream(zis);
            //输出路径（文件夹目录）
            String parent = facesRootDir.getAbsolutePath() + "/" + faceListInfo.getVersion();
            File file;
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    // 如果有对应的路径名字的文件,则删除,重建一个目录
                    file = new File(parent, entry.getName());
                    if (file.exists() && file.isFile()) {
                        boolean b = file.delete();
                        if (b) {
                            file.mkdirs();
                        }
                    }
                    continue;
                }
                file = new File(parent, entry.getName());
                if (!file.exists()) {
                    (new File(file.getParent())).mkdirs();
                } else if (file.exists() && file.isFile() && file.length() > 0) {
                    continue;
                }
                FileOutputStream out = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(out);
                int b;
                while ((b = bis.read()) != -1) {
                    bos.write(b);
                }
                bos.close();
                out.close();
            }
            bis.close();
            zis.close();
        } catch (Exception e) {
        }
        long endTime = System.currentTimeMillis();
        // 更新对应的faceInfoList中的图片根目录
        setPicRootDirectoryIntoFaceInfo(faceListInfo);
        // 如果有dialog.可以显示出对应的数据
        notifyClients(IFaceCoreClient.class, IFaceCoreClient.METHOD_ON_UNZIP_SUCCESS);
    }

    private static class TimerHandler extends Handler {

        static final int SHOW_PUBLIC_SCREEN = 1;
        static final int SHOW_NONE = 0;

        private WeakReference<FaceCoreImpl> core;

        TimerHandler(FaceCoreImpl faceCore) {
            core = new WeakReference<>(faceCore);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FaceCoreImpl faceCore = core.get();
            if (faceCore == null) {
                return;
            }
            if (msg.what == SHOW_PUBLIC_SCREEN) {
                faceCore.isShowingFace = false;
                ChatRoomMessage chatRoomMessage = (ChatRoomMessage) msg.obj;
                IMNetEaseManager.get().addMessagesImmediately(chatRoomMessage);
            }
        }
    }

    @Override
    public void sendAllFace(FaceInfo faceInfo) {
        if (!usable()) return;
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) return;
        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(roomInfo.getUid());
        UserInfo myUserInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
        if (userInfo == null) return;
        // 聊天室所有麦上的人,包括自己
        SparseArray<RoomQueueInfo> mMicQueueMemberMap = AvRoomDataManager.get().mMicQueueMemberMap;
        List<FaceReceiveInfo> faceReceiveInfos = new ArrayList<>();
        for (int i = 0; i < mMicQueueMemberMap.size(); i++) {
            IMChatRoomMember mChatRoomMember = mMicQueueMemberMap.get(mMicQueueMemberMap.keyAt(i)).mChatRoomMember;
            if (mChatRoomMember == null ||
                    TextUtils.isEmpty(mChatRoomMember.getNick()) ||
                    TextUtils.isEmpty(mChatRoomMember.getAccount())) continue;
            FaceReceiveInfo faceReceiveInfo = new FaceReceiveInfo();
            faceReceiveInfo.setFaceId(faceInfo.getId());
            faceReceiveInfo.setNick(mChatRoomMember.getNick());
            faceReceiveInfo.setUid(Long.valueOf(mChatRoomMember.getAccount()));
            faceReceiveInfo.setResultIndexes(generateRandomNumber(1, faceInfo));
            faceReceiveInfos.add(faceReceiveInfo);
        }

        FaceAttachment faceAttachment = new FaceAttachment(CUSTOM_MSG_HEADER_TYPE_FACE, CUSTOM_MSG_SUB_TYPE_FACE_SEND);
        faceAttachment.setUid(userInfo.getUid());

        faceAttachment.setFaceReceiveInfos(faceReceiveInfos);
        faceAttachment.setCharmLevel(myUserInfo.getCharmLevel());
        faceAttachment.setExperLevel(myUserInfo.getExperLevel());

//        ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomCustomMessage(
//                // 聊天室id
//                roomInfo.getRoomId() + "",
//                // 自定义消息
//                faceAttachment
//        );

        final ChatRoomMessage message = new ChatRoomMessage();
        message.setRoom_id(roomInfo.getRoomId() + "");
        message.setAttachment(faceAttachment);
        ReUsedSocketManager.get().sendCustomMessage(roomInfo.getRoomId() + "", message, new IMProCallBack() {
            @Override
            public void onSuccessPro(IMReportBean imReportBean) {
                if (imReportBean.getReportData().errno == 0) {
                    onSendRoomMessageSuccess(message);
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {

            }
        });
    }

    @Override
    public FaceInfo getPlayTogetherFace() {
        //: 2017/12/7 以后改成把对应的表情配置放到json的最后面,或者用字段区分出来
        if (!checkFaceCanUseOrNot(onlineFacesList)) {
            // 没有最新的online json,或者没有最新的zip包
            getOnlineFaceJsonOrZip();
        }
        // 找到对应的骰子表情信息
        return findFaceInfoById(17);
    }

    private boolean usable() {
        // 如果没有网络,return
        return NetworkUtils.isNetworkAvailable(getContext());
    }

    @Override
    public void sendFace(FaceInfo faceInfo) {
        if (!usable()) return;
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) return;
        // 普通表情
        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
        List<FaceReceiveInfo> faceReceiveInfos = new ArrayList<>();
        FaceReceiveInfo faceReceiveInfo = new FaceReceiveInfo();
        faceReceiveInfo.setNick(userInfo.getNick());
        faceReceiveInfo.setFaceId(faceInfo.getId());
        faceReceiveInfo.setUid(userInfo.getUid());

        // 运气表情
        if (faceInfo.getResultCount() > 0) {
            faceReceiveInfo.setResultIndexes(generateRandomNumber(faceInfo.getResultCount(), faceInfo));
        }
        faceReceiveInfos.add(faceReceiveInfo);

        // 发送云信信息给所有人
        FaceAttachment faceAttachment =
                new FaceAttachment(CUSTOM_MSG_HEADER_TYPE_FACE, CUSTOM_MSG_SUB_TYPE_FACE_SEND);
        faceAttachment.setUid(userInfo.getUid());
        faceAttachment.setCharmLevel(userInfo.getCharmLevel());
        faceAttachment.setExperLevel(userInfo.getExperLevel());
        faceAttachment.setFaceReceiveInfos(faceReceiveInfos);
        final ChatRoomMessage message = new ChatRoomMessage();
        message.setRoom_id(roomInfo.getRoomId() + "");
        message.setAttachment(faceAttachment);

//        IMNetEaseManager.get().sendChatRoomMessage(message, false)
//                .subscribe(new BiConsumer<ChatRoomMessage, Throwable>() {
//                    @Override
//                    public void accept(ChatRoomMessage chatRoomMessage,
//                                       Throwable throwable) throws Exception {
//                        if (chatRoomMessage != null) {
//                            onSendRoomMessageSuccess(chatRoomMessage);
//                        }
//                    }
//                });
        ReUsedSocketManager.get().sendCustomMessage(roomInfo.getRoomId() + "", message, new IMProCallBack() {
            @Override
            public void onSuccessPro(IMReportBean imReportBean) {
                if (imReportBean.getReportData().errno == 0) {
                    onSendRoomMessageSuccess(message);
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                SingleToastUtil.showToast(errorMsg);
            }
        });
    }

    /**
     * 速配（龙珠）消息
     *
     * @param isShow
     * @param numArr
     * @param second
     */
    @Override
    public void sendRoomMatchFace(boolean isShow, int[] numArr, int second) {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        final RoomMatchAttachment matchAttachment = new RoomMatchAttachment(CUSTOM_MSG_HEADER_TYPE_MATCH, second);
        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
        if (userInfo != null) {
            matchAttachment.setUid(userInfo.getUid());
            matchAttachment.setNick(userInfo.getNick());
            matchAttachment.setShowd(isShow);
            matchAttachment.setNumArr(numArr);
            matchAttachment.setExperLevel(userInfo.getExperLevel());

            final ChatRoomMessage message = new ChatRoomMessage();
            message.setRoom_id(roomInfo.getRoomId() + "");
            message.setAttachment(matchAttachment);
            ReUsedSocketManager.get().sendCustomMessage(roomInfo.getRoomId() + "", message, new IMProCallBack() {

                @Override
                public void onError(int errorCode, String errorMsg) {

                }

                @Override
                public void onSuccessPro(IMReportBean imReportBean) {
                    if (imReportBean.getReportData().errno == 0) {
                        notifyClients(IFaceCoreClient.class, IFaceCoreClient.METHOD_ON_RECEIVE_MATCH_FACE, matchAttachment);
                    }
                }
            });
        }
    }

    @Override
    public void onReceiveRoomMatchFace(ChatRoomMessage msg) {
        if (msg == null)
            return;
        if (msg.getAttachment() instanceof IMCustomAttachment) {
            IMCustomAttachment face = (IMCustomAttachment) msg.getAttachment();
            if (face.getFirst() == CUSTOM_MSG_HEADER_TYPE_MATCH) {
                notifyClients(IFaceCoreClient.class, IFaceCoreClient.METHOD_ON_RECEIVE_MATCH_FACE, (RoomMatchAttachment) face);
            }
        }
    }

    /**
     * 生成随机数
     *
     * @param resultCount--
     * @param faceInfo--
     * @return --
     */
    private List<Integer> generateRandomNumber(int resultCount, FaceInfo faceInfo) {
        // 结果的数量,1/2/3/4/5
        // 生成随机数结果下标
        int resultStartPos = faceInfo.getResultIndexStart();
        int resultEndPos = faceInfo.getResultIndexEnd();
        List<Integer> resultIndexes = new ArrayList<>();
        int random;
        Random r = new Random();
        while (resultIndexes.size() < resultCount) {
            random = resultStartPos + r.nextInt(resultEndPos - resultStartPos + 1);
            // 不可以重复,需要去重
            if (faceInfo.getRepeat() == FaceInfo.RESULT_CAN_NOT_REPEAT) {
                if (!resultIndexes.contains(random)) {
                    resultIndexes.add(random);
                }
            } else {
                // 可以重复,直接加进去
                resultIndexes.add(random);
            }
        }
        return resultIndexes;
    }

    /**
     * 收到其他手机发送过来的表情信息
     *
     * @param chatRoomMessageList--
     */
    @Override
    public void onReceiveChatRoomMessages(List<ChatRoomMessage> chatRoomMessageList) {
        if (chatRoomMessageList == null || chatRoomMessageList.size() <= 0) {
            return;
        }
        for (ChatRoomMessage msg : chatRoomMessageList) {
            if (IMReportRoute.sendMessageReport.equalsIgnoreCase(msg.getRoute())) {
                IMCustomAttachment attachment = (IMCustomAttachment) msg.getAttachment();
                // 显示表情到对应的地方
                parseAttachment(attachment);
                if (attachment.getFirst() == CUSTOM_MSG_HEADER_TYPE_FACE) {
                    FaceAttachment faceAttachment = (FaceAttachment) attachment;
                    List<FaceReceiveInfo> faceReceiveInfos = faceAttachment.getFaceReceiveInfos();
                    if (ListUtils.isListEmpty(faceReceiveInfos)) continue;
                    FaceReceiveInfo faceReceiveInfo = faceReceiveInfos.get(0);
                    FaceInfo faceInfo = findFaceInfoById(faceReceiveInfo.getFaceId());
                    if (faceInfo != null) {
                        TimerHandler timerHandler = new TimerHandler(this);
                        Message message = Message.obtain();
                        message.what = (faceReceiveInfo.getResultIndexes() != null &&
                                faceReceiveInfo.getResultIndexes().size() > 0) ?
                                TimerHandler.SHOW_PUBLIC_SCREEN :
                                TimerHandler.SHOW_NONE;
                        message.obj = msg;
                        timerHandler.sendMessageDelayed(message, 3000);
                    }
                }
            }
        }

    }

    /**
     * 自己发送云信自定义消息(表情信息)成功后的回调
     *
     * @param message--
     */
    private void onSendRoomMessageSuccess(ChatRoomMessage message) {
        if (IMReportRoute.sendMessageReport.equalsIgnoreCase(message.getRoute())) {
            IMCustomAttachment attachment = (IMCustomAttachment) message.getAttachment();
            // 显示表情在对应的位置
            parseAttachment(attachment);
            if (attachment.getFirst() == CUSTOM_MSG_HEADER_TYPE_FACE) {
                FaceAttachment faceAttachment = (FaceAttachment) attachment;
                List<FaceReceiveInfo> faceReceiveInfos = faceAttachment.getFaceReceiveInfos();
                FaceReceiveInfo faceReceiveInfo = faceReceiveInfos.get(0);
                FaceInfo faceInfo = findFaceInfoById(faceReceiveInfo.getFaceId());
                // 3s时间后,显示表情的公屏信息
                if (faceInfo != null) {
                    TimerHandler timerHandler = new TimerHandler(this);
                    Message msg = Message.obtain();
                    msg.what = (faceReceiveInfo.getResultIndexes() != null &&
                            faceReceiveInfo.getResultIndexes().size() > 0) ?
                            TimerHandler.SHOW_PUBLIC_SCREEN :
                            TimerHandler.SHOW_NONE;
                    msg.obj = message;
                    timerHandler.sendMessageDelayed(msg, 3000);
                }
            }
        }
    }


    private void parseAttachment(IMCustomAttachment attachment) {
        if (attachment.getFirst() == CUSTOM_MSG_HEADER_TYPE_FACE) {
            FaceAttachment faceAttachment = (FaceAttachment) attachment;
            List<FaceReceiveInfo> faceReceiveInfos = faceAttachment.getFaceReceiveInfos();
            if (faceReceiveInfos != null && faceReceiveInfos.size() > 0) {
                // 显示动画
                notifyClients(IFaceCoreClient.class, IFaceCoreClient.METHOD_ON_RECEIVE_FACE, faceReceiveInfos);
                for (int i = 0; i < faceReceiveInfos.size(); i++) {
                    FaceReceiveInfo faceReceiveInfo1 = faceReceiveInfos.get(i);
                    FaceInfo faceInfo1 = findFaceInfoById(faceReceiveInfo1.getFaceId());
                    if (faceInfo1 != null) {
                        long myUid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
                        if (myUid == faceReceiveInfo1.getUid() &&
                                faceReceiveInfo1.getResultIndexes() != null &&
                                faceReceiveInfo1.getResultIndexes().size() > 0) {
                            isShowingFace = true;
                        }
                    }
                }
            }
        }
    }

    private List<FaceInfo> filterNobleFaces(List<FaceInfo> originals) {
        //获取房间信息
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        List<Integer> hideFace = null;
        if (roomInfo != null) {
            hideFace = roomInfo.getHideFace();
        }
        //判断数据
        List<FaceInfo> noNobleFaces = new ArrayList<>(originals.size());
        for (FaceInfo faceInfo : originals) {
            if (!ListUtils.isListEmpty(hideFace) && hideFace.contains(faceInfo.getId())) {
                continue;
            }
            if (faceInfo.isNobleFace()) {
                continue;
            }
            noNobleFaces.add(faceInfo);
        }
        return noNobleFaces;
    }

    @Override
    public List<FaceInfo> getFaceInfos() {
        // 有最新的online json,也有最新的zip包,返回
        if (checkFaceCanUseOrNot(onlineFacesList)) {
            // 如果有对应的zip包,判断需不需要解压对应的zip包
            File faceRoot = new File(facesRootDir.getAbsolutePath()
                    + "/" + onlineFacesList.getVersion());

            if (!faceRoot.exists() ||
                    faceRoot.list().length <= 0) {
                unzipFaceZipFile(onlineFacesList);

            } else {

                // 如果有对应的zip包,并且已经解压了,则设置图片的根目录到FaceInfo中
                setPicRootDirectoryIntoFaceInfo(onlineFacesList);
            }

            return filterNobleFaces(onlineFacesList.getFaces());
        }
        // 没有重新请求最新的json,还有对应的zip包
        getOnlineFaceJsonOrZip();

        // 判断现在能不能显示离线的json和对应的表情
        if (checkFaceCanUseOrNot(offlineFaceList)) {
            // 有离线的json,并且有对应的zip包,才返回显示出列表

            return filterNobleFaces(offlineFaceList.getFaces());
        }

        return null;
    }

    private boolean checkFaceCanUseOrNot(FaceListInfo faceListInfo) {
        // 有没有对应的json
        boolean hasJson = checkFaceListInfoValid(faceListInfo);
        // 有没有json对应的zip包
        boolean hasZip = (hasJson &&
                hasFacesZipPacket(faceListInfo.getZipMd5()));
        return (hasJson && hasZip);
    }

    private boolean checkFaceListInfoValid(FaceListInfo faceListInfo) {
        // 检查对应的faceListInfo是不是合法
        return (faceListInfo != null &&
                faceListInfo.getFaces() != null &&
                faceListInfo.getFaces().size() > 0);
    }

    @Override
    public FaceInfo findFaceInfoById(int faceId) {
        FaceListInfo found = null;
        if (checkFaceListInfoValid(onlineFacesList)) {
            found = onlineFacesList;
        } else if (checkFaceListInfoValid(offlineFaceList)) {
            found = offlineFaceList;
        }
        if (found == null) {
            return null;
        }
        FaceInfo faceInfo = null;
        List<FaceInfo> faces = found.getFaces();
        int size = faces.size();
        for (int i = 0; i < size; i++) {
            if (faces.get(i).getId() == faceId) {
                faceInfo = faces.get(i);
                break;
            }
        }
        return faceInfo;
    }

    private String json = "{\n" +
            "    \"version\": 1,\n" +
            "    \"zipMd5\": \"a9f71f0337a8219ef4528f0828f283b1\",\n" +
            "    \"zipUrl\": \"https://img.erbanyy.com/face.zip\",\n" +
            "    \"faces\": [\n" +
            "              {\n" +
            "                  \"name\": \"流汗\",\n" +
            "                  \"pinyin\": \"liuhan\",\n" +
            "                  \"animDuration\": 1900,\n" +
            "                  \"animEndPos\": 8,\n" +
            "                  \"animStartPos\": 1,\n" +
            "                  \"iconPos\": 0,\n" +
            "                  \"id\": 2,\n" +
            "                  \"animRepeatCount\": 1,\n" +
            "                  \"resultDuration\": 0,\n" +
            "                  \"resultEndPos\": 0,\n" +
            "                  \"resultStartPos\": 0,\n" +
            "                  \"imageCount\": 9,\n" +
            "                  \"displayType\": 0\n" +
            "              },\n" +
            "              {\n" +
            "                  \"name\": \"大笑\",\n" +
            "                  \"pinyin\": \"daxiao\",\n" +
            "                  \"animDuration\": 1200,\n" +
            "                  \"animEndPos\": 6,\n" +
            "                  \"animStartPos\": 1,\n" +
            "                  \"iconPos\": 0,\n" +
            "                  \"id\": 1,\n" +
            "                  \"animRepeatCount\": 3,\n" +
            "                  \"resultDuration\": 0,\n" +
            "                  \"resultEndPos\": 0,\n" +
            "                  \"resultStartPos\": 0,\n" +
            "                  \"imageCount\": 7,\n" +
            "                  \"displayType\": 0\n" +
            "              },\n" +
            "              {\n" +
            "                  \"name\": \"抱抱\",\n" +
            "                  \"pinyin\": \"baobao\",\n" +
            "                  \"animDuration\": 2000,\n" +
            "                  \"animEndPos\": 5,\n" +
            "                  \"animStartPos\": 1,\n" +
            "                  \"iconPos\": 0,\n" +
            "                  \"id\": 14,\n" +
            "                  \"animRepeatCount\": 3,\n" +
            "                  \"resultDuration\": 0,\n" +
            "                  \"resultEndPos\": 0,\n" +
            "                  \"resultStartPos\": 0,\n" +
            "                  \"imageCount\": 6,\n" +
            "                  \"displayType\": 0\n" +
            "              },\n" +
            "              {\n" +
            "                  \"name\": \"拜拜\",\n" +
            "                  \"pinyin\": \"baibai\",\n" +
            "                  \"animDuration\": 1200,\n" +
            "                  \"animEndPos\": 6,\n" +
            "                  \"animStartPos\": 1,\n" +
            "                  \"iconPos\": 0,\n" +
            "                  \"id\": 9,\n" +
            "                  \"animRepeatCount\": 3,\n" +
            "                  \"resultDuration\": 0,\n" +
            "                  \"resultEndPos\": 0,\n" +
            "                  \"resultStartPos\": 0,\n" +
            "                  \"imageCount\": 7,\n" +
            "                  \"displayType\": 0\n" +
            "              },\n" +
            "              {\n" +
            "                  \"name\": \"鄙视\",\n" +
            "                  \"pinyin\": \"bishi\",\n" +
            "                  \"animDuration\": 3000,\n" +
            "                  \"animEndPos\": 2,\n" +
            "                  \"animStartPos\": 1,\n" +
            "                  \"iconPos\": 0,\n" +
            "                  \"id\": 10,\n" +
            "                  \"animRepeatCount\": 1,\n" +
            "                  \"resultDuration\": 0,\n" +
            "                  \"resultEndPos\": 0,\n" +
            "                  \"resultStartPos\": 0,\n" +
            "                  \"imageCount\": 3,\n" +
            "                  \"displayType\": 0\n" +
            "              },\n" +
            "              {\n" +
            "                  \"name\": \"抽签\",\n" +
            "                  \"pinyin\": \"chouqian\",\n" +
            "                  \"animDuration\": 600,\n" +
            "                  \"animEndPos\": 6,\n" +
            "                  \"animStartPos\": 1,\n" +
            "                  \"iconPos\": 0,\n" +
            "                  \"id\": 20,\n" +
            "                  \"resultCount\": 1,\n" +
            "                  \"canResultRepeat\": 0,\n" +
            "                  \"animRepeatCount\": 3,\n" +
            "                  \"resultDuration\": 5000,\n" +
            "                  \"resultEndPos\": 16,\n" +
            "                  \"resultStartPos\": 7,\n" +
            "                  \"imageCount\": 17,\n" +
            "                  \"displayType\": 0\n" +
            "              },\n" +
            "              {\n" +
            "                  \"name\": \"恶魔坏笑\",\n" +
            "                  \"pinyin\": \"emohuaixiao\",\n" +
            "                  \"animDuration\": 800,\n" +
            "                  \"animEndPos\": 4,\n" +
            "                  \"animStartPos\": 1,\n" +
            "                  \"iconPos\": 0,\n" +
            "                  \"id\": 11,\n" +
            "                  \"animRepeatCount\": 5,\n" +
            "                  \"resultDuration\": 0,\n" +
            "                  \"resultEndPos\": 0,\n" +
            "                  \"resultStartPos\": 0,\n" +
            "                  \"imageCount\": 5,\n" +
            "                  \"displayType\": 0\n" +
            "              },\n" +
            "              {\n" +
            "                  \"name\": \"飞吻\",\n" +
            "                  \"pinyin\": \"feiwen\",\n" +
            "                  \"animDuration\": 1600,\n" +
            "                  \"animEndPos\": 8,\n" +
            "                  \"animStartPos\": 1,\n" +
            "                  \"iconPos\": 0,\n" +
            "                  \"id\": 5,\n" +
            "                  \"animRepeatCount\": 1,\n" +
            "                  \"resultDuration\": 0,\n" +
            "                  \"resultEndPos\": 0,\n" +
            "                  \"resultStartPos\": 0,\n" +
            "                  \"imageCount\": 9,\n" +
            "                  \"displayType\": 0\n" +
            "              },\n" +
            "              {\n" +
            "                  \"name\": \"沮丧\",\n" +
            "                  \"pinyin\": \"jusang\",\n" +
            "                  \"animDuration\": 1600,\n" +
            "                  \"animEndPos\": 8,\n" +
            "                  \"animStartPos\": 1,\n" +
            "                  \"iconPos\": 0,\n" +
            "                  \"id\": 6,\n" +
            "                  \"animRepeatCount\": 2,\n" +
            "                  \"resultDuration\": 0,\n" +
            "                  \"resultEndPos\": 0,\n" +
            "                  \"resultStartPos\": 0,\n" +
            "                  \"imageCount\": 9,\n" +
            "                  \"displayType\": 0\n" +
            "              },\n" +
            "              {\n" +
            "                  \"name\": \"开心吐舌头\",\n" +
            "                  \"pinyin\": \"kaxintushetou\",\n" +
            "                  \"animDuration\": 1600,\n" +
            "                  \"animEndPos\": 8,\n" +
            "                  \"animStartPos\": 1,\n" +
            "                  \"iconPos\": 0,\n" +
            "                  \"id\": 8,\n" +
            "                  \"animRepeatCount\": 3,\n" +
            "                  \"resultDuration\": 0,\n" +
            "                  \"resultEndPos\": 0,\n" +
            "                  \"resultStartPos\": 0,\n" +
            "                  \"imageCount\": 9,\n" +
            "                  \"displayType\": 0\n" +
            "              },\n" +
            "              {\n" +
            "                  \"name\": \"流泪\",\n" +
            "                  \"pinyin\": \"liulei\",\n" +
            "                  \"animDuration\": 400,\n" +
            "                  \"animEndPos\": 4,\n" +
            "                  \"animStartPos\": 1,\n" +
            "                  \"iconPos\": 0,\n" +
            "                  \"id\": 4,\n" +
            "                  \"animRepeatCount\": 5,\n" +
            "                  \"resultDuration\": 0,\n" +
            "                  \"resultEndPos\": 0,\n" +
            "                  \"resultStartPos\": 0,\n" +
            "                  \"imageCount\": 5,\n" +
            "                  \"displayType\": 0\n" +
            "              },\n" +
            "              {\n" +
            "                  \"name\": \"扑克牌\",\n" +
            "                  \"pinyin\": \"pukepai\",\n" +
            "                  \"animDuration\": 1800,\n" +
            "                  \"animEndPos\": 9,\n" +
            "                  \"animStartPos\": 1,\n" +
            "                  \"iconPos\": 0,\n" +
            "                  \"id\": 23,\n" +
            "                  \"resultCount\": 3,\n" +
            "                  \"canResultRepeat\": 0,\n" +
            "                  \"animRepeatCount\": 1,\n" +
            "                  \"resultDuration\": 5000,\n" +
            "                  \"resultEndPos\": 61,\n" +
            "                  \"resultStartPos\": 10,\n" +
            "                  \"imageCount\": 23,\n" +
            "                  \"displayType\": 2\n" +
            "              },\n" +
            "              {\n" +
            "                  \"name\": \"色眯眯\",\n" +
            "                  \"pinyin\": \"semimi\",\n" +
            "                  \"animDuration\": 900,\n" +
            "                  \"animEndPos\": 3,\n" +
            "                  \"animStartPos\": 1,\n" +
            "                  \"iconPos\": 0,\n" +
            "                  \"id\": 3,\n" +
            "                  \"animRepeatCount\": 5,\n" +
            "                  \"resultDuration\": 0,\n" +
            "                  \"resultEndPos\": 0,\n" +
            "                  \"resultStartPos\": 0,\n" +
            "                  \"imageCount\": 4,\n" +
            "                  \"displayType\": 0\n" +
            "              },\n" +
            "              {\n" +
            "                  \"name\": \"骰子\",\n" +
            "                  \"pinyin\": \"shaizi\",\n" +
            "                  \"animDuration\": 400,\n" +
            "                  \"animEndPos\": 5,\n" +
            "                  \"animStartPos\": 1,\n" +
            "                  \"iconPos\": 0,\n" +
            "                  \"id\": 17,\n" +
            "                  \"resultCount\": 3,\n" +
            "                  \"canResultRepeat\": 0,\n" +
            "                  \"animRepeatCount\": 4,\n" +
            "                  \"resultDuration\": 5000,\n" +
            "                  \"resultEndPos\": 11,\n" +
            "                  \"resultStartPos\": 6,\n" +
            "                  \"imageCount\": 12,\n" +
            "                  \"displayType\": 1\n" +
            "              },\n" +
            "              {\n" +
            "                  \"name\": \"生气\",\n" +
            "                  \"pinyin\": \"shengqi\",\n" +
            "                  \"animDuration\": 1200,\n" +
            "                  \"animEndPos\": 6,\n" +
            "                  \"animStartPos\": 1,\n" +
            "                  \"iconPos\": 0,\n" +
            "                  \"id\": 13,\n" +
            "                  \"animRepeatCount\": 4,\n" +
            "                  \"resultDuration\": 0,\n" +
            "                  \"resultEndPos\": 0,\n" +
            "                  \"resultStartPos\": 0,\n" +
            "                  \"imageCount\": 7,\n" +
            "                  \"displayType\": 0\n" +
            "              },\n" +
            "              {\n" +
            "                  \"name\": \"石头剪刀布\",\n" +
            "                  \"pinyin\": \"shitoujiandaobu\",\n" +
            "                  \"animDuration\": 600,\n" +
            "                  \"animEndPos\": 3,\n" +
            "                  \"animStartPos\": 1,\n" +
            "                  \"iconPos\": 0,\n" +
            "                  \"id\": 18,\n" +
            "                  \"animRepeatCount\": 3,\n" +
            "                  \"resultDuration\": 5000,\n" +
            "                  \"resultCount\": 1,\n" +
            "                  \"resultEndPos\": 3,\n" +
            "                  \"resultStartPos\": 1,\n" +
            "                  \"imageCount\": 4,\n" +
            "                  \"displayType\": 0\n" +
            "              },\n" +
            "              {\n" +
            "                  \"name\": \"天使微笑\",\n" +
            "                  \"pinyin\": \"tianshiweixiao\",\n" +
            "                  \"animDuration\": 900,\n" +
            "                  \"animEndPos\": 6,\n" +
            "                  \"animStartPos\": 1,\n" +
            "                  \"iconPos\": 0,\n" +
            "                  \"id\": 12,\n" +
            "                  \"animRepeatCount\": 4,\n" +
            "                  \"resultDuration\": 0,\n" +
            "                  \"resultEndPos\": 0,\n" +
            "                  \"resultStartPos\": 0,\n" +
            "                  \"imageCount\": 7,\n" +
            "                  \"displayType\": 0\n" +
            "              },\n" +
            "              {\n" +
            "                  \"name\": \"听音乐\",\n" +
            "                  \"pinyin\": \"tingyinyue\",\n" +
            "                  \"animDuration\": 1200,\n" +
            "                  \"animEndPos\": 6,\n" +
            "                  \"animStartPos\": 1,\n" +
            "                  \"iconPos\": 0,\n" +
            "                  \"id\": 15,\n" +
            "                  \"animRepeatCount\": 4,\n" +
            "                  \"resultDuration\": 0,\n" +
            "                  \"resultEndPos\": 0,\n" +
            "                  \"resultStartPos\": 0,\n" +
            "                  \"imageCount\": 7,\n" +
            "                  \"displayType\": 0\n" +
            "              },\n" +
            "              {\n" +
            "                  \"name\": \"挖鼻孔\",\n" +
            "                  \"pinyin\": \"wabikong\",\n" +
            "                  \"animDuration\": 600,\n" +
            "                  \"animEndPos\": 3,\n" +
            "                  \"animStartPos\": 1,\n" +
            "                  \"iconPos\": 0,\n" +
            "                  \"id\": 21,\n" +
            "                  \"animRepeatCount\": 4,\n" +
            "                  \"resultDuration\": 0,\n" +
            "                  \"resultEndPos\": 0,\n" +
            "                  \"resultStartPos\": 0,\n" +
            "                  \"imageCount\": 4,\n" +
            "                  \"displayType\": 0\n" +
            "              },\n" +
            "              {\n" +
            "                  \"name\": \"无言\",\n" +
            "                  \"pinyin\": \"wuyan\",\n" +
            "                  \"animDuration\": 1400,\n" +
            "                  \"animEndPos\": 7,\n" +
            "                  \"animStartPos\": 1,\n" +
            "                  \"iconPos\": 0,\n" +
            "                  \"id\": 7,\n" +
            "                  \"animRepeatCount\": 2,\n" +
            "                  \"resultDuration\": 0,\n" +
            "                  \"resultEndPos\": 0,\n" +
            "                  \"resultStartPos\": 0,\n" +
            "                  \"imageCount\": 8,\n" +
            "                  \"displayType\": 0\n" +
            "              }\n" +
            "              ]\n" +
            "}\n";
}
