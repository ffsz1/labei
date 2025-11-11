package com.erban.main.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.beust.jcommander.internal.Maps;
import com.erban.main.vo.MicUserVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xchat.common.constant.Constant;
import com.xchat.common.netease.neteaseacc.NetEaseBaseClient;
import com.xchat.common.netease.neteaseacc.result.*;
import com.xchat.common.netease.util.NetEaseConstant;
import com.xchat.common.utils.BlankUtil;
import com.xchat.common.utils.GsonUtil;
import org.apache.geronimo.mail.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ErBanNetEaseService {
    private static final Logger logger = LoggerFactory.getLogger(ErBanNetEaseService.class);
    private Gson gson = new Gson();
    private Gson gsonDefine = GsonUtil.getGson();


    public UserInfoRet getUserInfo(String accid) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.UserUrl.get;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        JSONArray array = new JSONArray();
        array.add(accid);
        param.put("accids", array);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        logger.info("getUserInfo accid:{}, result:{}", accid, result);
        RubbishRet rubbishRet = gson.fromJson(result, RubbishRet.class);
        if (rubbishRet.getCode() != 200) {
            return null;
        }
        return null;
    }

    /**
     * 更新网易云用户信息，目前只支持更新昵称和头像
     *
     * @param accid
     * @param name
     * @param icon
     * @return
     * @throws Exception
     */
    public BaseNetEaseRet updateUserInfo(String accid, String name, String icon) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.UserUrl.updateUinfo;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("accid", accid);
        param.put("name", name);
        param.put("icon", icon);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        BaseNetEaseRet baseNetEaseRet = gson.fromJson(result, BaseNetEaseRet.class);
        return baseNetEaseRet;
    }

    /**
     * 只更新网易云性别，用于在用户第一次补全资料，性别一经补全，不可更改
     *
     * @param accid
     * @param gender
     * @return
     * @throws Exception
     */
    public BaseNetEaseRet updateUserGenderOnly(String accid, int gender) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.UserUrl.updateUinfo;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("accid", accid);
        param.put("gender", gender);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        BaseNetEaseRet baseNetEaseRet = gson.fromJson(result, BaseNetEaseRet.class);
        return baseNetEaseRet;
    }

    /**
     * /**
     * 更新网易云用户的所有个人信息，注意此方法只适用于后台管理系统
     *
     * @param accid
     * @param gender
     * @param icon
     * @param name
     * @return
     * @throws Exception
     * @Author yuanyi
     */
    public BaseNetEaseRet updateUserInfoForUserAdmin(String accid, int gender, String icon, String name) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.UserUrl.updateUinfo;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("accid", accid);
        param.put("gender", gender);
        param.put("icon", icon);
        param.put("name", name);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        BaseNetEaseRet baseNetEaseRet = gson.fromJson(result, BaseNetEaseRet.class);
        return baseNetEaseRet;
    }


    public BaseNetEaseRet updateUserInfoOfExpand(String accid, String expand) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.UserUrl.updateUinfo;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("accid", accid);
        param.put("ex", expand);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        BaseNetEaseRet baseNetEaseRet = gson.fromJson(result, BaseNetEaseRet.class);
        return baseNetEaseRet;
    }

    /**
     * 加好友
     *
     * @param accid
     * @param faccid
     * @param type   1直接加好友，2请求加好友，3同意加好友，4拒绝加好友
     * @param msg
     * @return
     * @throws Exception
     */
    public BaseNetEaseRet addFriends(String accid, String faccid, String type, String msg) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.FriendsUrl.friendAdd;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("accid", accid);
        param.put("faccid", faccid);
        param.put("type", type);
        param.put("msg", msg);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        BaseNetEaseRet baseNetEaseRet = gson.fromJson(result, BaseNetEaseRet.class);
        return baseNetEaseRet;
    }

    public BaseNetEaseRet deleteFriends(String accid, String faccid) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.FriendsUrl.friendDelete;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("accid", accid);
        param.put("faccid", faccid);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        BaseNetEaseRet baseNetEaseRet = gson.fromJson(result, BaseNetEaseRet.class);
        return baseNetEaseRet;
    }

    /**
     * 查询聊天室信息
     *
     * @param roomId
     * @return
     * @throws Exception
     */
    public RoomRet getRoomMessage(long roomId) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.RoomUrl.get;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        param.put("needOnlineUserCount", true);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost2();
        return gson.fromJson(result, RoomRet.class);
    }

    /**
     * 创建聊天室
     *
     * @param accid 房主账号
     * @param name  名称
     * @param ext   扩展字段
     * @return
     * @throws Exception
     */
    public RoomRet openRoom(String accid, String name, String ext) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.RoomUrl.create;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("creator", accid);
        param.put("name", name);
        param.put("ext", ext);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        logger.info("openRoom result: {}", result);
        return gsonDefine.fromJson(result, RoomRet.class);
    }

    /**
     * 更新聊天室的信息
     *
     * @param roomId
     * @param name
     * @param ext
     * @return
     * @throws Exception
     */
    public RoomRet updateRoomInfo(Long roomId, String name, String ext, String notifyExt) throws Exception {
        logger.info("updateRoomInfo roomId:{}, name:{}, ext:{}, notifyExt:{}", roomId, name, ext, notifyExt);
        String url = NetEaseConstant.basicUrl + NetEaseConstant.RoomUrl.update;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        param.put("name", name);
        param.put("ext", ext);
        if (!BlankUtil.isBlank(notifyExt)) {
            param.put("notifyExt", notifyExt);
        }
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost2();
        return gsonDefine.fromJson(result, RoomRet.class);
    }

    /**
     * 修改聊天室开/关闭状态
     *
     * @param roomId
     * @param operator 操作者账号，必须是创建者才可以操作
     * @param valid    false:关闭聊天室；true:打开聊天室
     * @return
     * @throws Exception
     */
    public CloseRoomRet toggleCloseStat(Long roomId, String operator, boolean valid) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.RoomUrl.toggleCloseStat;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        param.put("operator", operator);
        param.put("valid", String.valueOf(valid));
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        return gson.fromJson(result, CloseRoomRet.class);
    }

    /**
     * 往聊天室内发消息
     *
     * @param roomId
     * @param msgId     消息标识，可用UUID随机串
     * @param fromAccid
     * @param msgType   消息类型
     * @param attach    消息内容
     * @return
     * @throws Exception
     */
    public RubbishRet sendChatRoomMsg(Long roomId, String msgId, String fromAccid, int msgType, String attach)
            throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.RoomUrl.sendMsg;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        param.put("msgId", msgId);
        param.put("fromAccid", fromAccid);
        param.put("msgType", msgType);
        param.put("attach", attach);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        RubbishRet rubbishRet = gson.fromJson(result, RubbishRet.class);
        if (rubbishRet.getCode() != 200) {
            logger.error("sendChatRoomMsg roomId=" + roomId + "&msgId=" + msgId + "&fromAccid=" + fromAccid + "&msgType="
                    + msgType + "&attach=" + attach + "====result=" + result);
        }
        return rubbishRet;
    }

    public RubbishRet setChatRoomMemberRole(Long roomId, String operator, String target, int opt, String optvalue)
            throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.RoomUrl.setMemberRole;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        param.put("operator", operator);
        param.put("target", target);
        param.put("opt", opt);
        param.put("optvalue", optvalue);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        Gson gson = new Gson();
        RubbishRet rubbishRet = gson.fromJson(result, RubbishRet.class);
        logger.info("setChatRoomMemberRole roomId=" + roomId + "&operator=" + operator + "&target=" + target + "&opt="
                + opt + "&optvalue=" + optvalue + "====result=" + result);
        if (optvalue.equals("false")) {
            Map<String, Object> param2 = Maps.newHashMap();
            param2.put("roomid", roomId);
            param2.put("operator", operator);
            param2.put("target", target);
            param2.put("opt", Constant.RoleOpt.common);
            param2.put("optvalue", optvalue);
            String result2 = netEaseBaseClient.buildHttpPostParam(param2).executePost();
            Gson gson2 = new Gson();
            RubbishRet rubbishRet2 = gson2.fromJson(result2, RubbishRet.class);
            logger.info("setChatRoomMemberRole roomId=" + roomId + "&operator=" + operator + "&target=" + target + "&opt="
                    + Constant.RoleOpt.common + "&optvalue=" + optvalue + "====result=" + result);
        }


        return rubbishRet;
    }

    public String getMembersByPage(long roomId, int type, long endtime, long limit) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.RoomUrl.membersByPage;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        param.put("type", type);
        param.put("endtime", endtime);
        param.put("limit", limit);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        return result;
    }

    public RubbishRet sendSysAttachMsg(String from, int msgtype, String to, String attach, String pushcontent,
                                       String payload, String sound, int save, String option) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.MsgUrl.sendAttachMsg;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("from", from);
        param.put("msgtype", msgtype);
        param.put("to", to);
        param.put("attach", attach);
        param.put("pushcontent", pushcontent);
        param.put("payload", payload);
        param.put("sound", sound);
        param.put("save", save);
        param.put("option", option);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        RubbishRet rubbishRet = gson.fromJson(result, RubbishRet.class);
        logger.info("sendSysAttachMsg from=" + from + "&to=" + to + "&pushcontent=" + pushcontent + "&payload="
                + payload + "&save=" + save + "====result=" + result);
        return rubbishRet;
    }

    /* 上传图片 */
    public String uploadImgBatch(File file) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.MsgUrl.uploadImg;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        File[] files = file.listFiles();
        String result = "";
        String str = "";
        // 遍历文件夹
        for (File file2 : files) {
            @SuppressWarnings("resource")
            FileInputStream fis = new FileInputStream(file2);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bys = new byte[1024];
            int len = 0;
            while ((len = fis.read(bys)) != -1) {
                baos.write(bys, 0, len);
            }
            String encode = new String(Base64.encode(baos.toByteArray()));
            param.put("content", encode);
            result = netEaseBaseClient.buildHttpPostParam(param).executePost();
            str = str + result + file2.getName() + "\n";
        }
        return str;
    }

    public FileUploadRet uploadFile(File file) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.MsgUrl.uploadImg;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        String result = "";
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bys = new byte[1024];
        int len = 0;
        while ((len = fis.read(bys)) != -1) {
            baos.write(bys, 0, len);
        }
        String encode = new String(Base64.encode(baos.toByteArray()));
        param.put("content", encode);
        result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        logger.info("result=" + result);
        FileUploadRet fileUploadRet = gson.fromJson(result, FileUploadRet.class);
        return fileUploadRet;
    }

    public FileUploadRet uploadMultipartFile(MultipartFile file) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.MsgUrl.uploadImg;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        String result = "";
        InputStream fis = file.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bys = new byte[1024];
        int len = 0;
        while ((len = fis.read(bys)) != -1) {
            baos.write(bys, 0, len);
        }
        String encode = new String(Base64.encode(baos.toByteArray()));
        param.put("content", encode);
        result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        logger.info("result=" + result);
        FileUploadRet fileUploadRet = gson.fromJson(result, FileUploadRet.class);
        return fileUploadRet;
    }

    public FileUploadRet uploadFileByInputStream(InputStream inputStream) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.MsgUrl.uploadImg;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bys = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(bys)) != -1) {
            baos.write(bys, 0, len);
        }
        String encode = new String(Base64.encode(baos.toByteArray()));
        param.put("content", encode);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        FileUploadRet fileUploadRet = gson.fromJson(result, FileUploadRet.class);
        return fileUploadRet;
    }

    public RubbishRet sendBatchSysAttachMsg(String fromAccid, String toAccids, String attach, String pushcontent,
                                            String payload, String sound, int save, String option) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.MsgUrl.sendBatchAttachMsg;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("fromAccid", fromAccid);
        param.put("toAccids", toAccids);
        param.put("attach", attach);
        param.put("pushcontent", pushcontent);
        param.put("payload", payload);
        param.put("sound", sound);
        param.put("save", save);
        param.put("option", option);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        RubbishRet rubbishRet = gson.fromJson(result, RubbishRet.class);
        logger.info("sendSysAttachMsg fromAccid=" + fromAccid + "&toAccids=" + toAccids + "&pushcontent=" + pushcontent
                + "&payload=" + payload + "&save=" + save + "====result=" + result);
        return rubbishRet;
    }

    //批量发送消息接口
    public RubbishRet sendBatchMsg(String fromAccid, String toAccids, int type, String body, String pushcontent,
                                   String payload, String option) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.MsgUrl.sendBatchMsg;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("fromAccid", fromAccid);
        param.put("toAccids", toAccids);
        param.put("type", type);
        param.put("body", body);
        param.put("pushcontent", pushcontent);
        param.put("payload", payload);
        param.put("option", option);
        logger.info("调用云信（网易）发送消息接口：{}，接口入参：{}", url, JSON.toJSONString(param));
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        logger.info("调用云信（网易）发送消息接口：{}，接口出参：{}", url, JSON.toJSONString(result));
        RubbishRet rubbishRet = gson.fromJson(result, RubbishRet.class);
        logger.info("sendBatchMsg fromAccid=" + fromAccid + "&toAccids=" + toAccids + "&pushcontent=" + pushcontent
                + "&payload=" + payload + "====result=" + result);
        return rubbishRet;
    }


    //点对点发送消息
    public RubbishRet sendMsg(String from, int ope, String to, int type, String body, String pushcontent,
                              String payload, String option) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.MsgUrl.sendMsg;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        //https://api.netease.im/nimserver/msg/sendMsg.action
        Map<String, Object> param = Maps.newHashMap();
        param.put("from", from);
        param.put("ope", ope);
        param.put("to", to);
        param.put("type", type);
        param.put("body", body);
        param.put("pushcontent", pushcontent);
        param.put("payload", payload);
        param.put("option", option);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        RubbishRet rubbishRet = gson.fromJson(result, RubbishRet.class);
        logger.info("sendMsg from=" + from + "&to=" + to + "&ope" + ope + "&type" + type + "&pushcontent=" + pushcontent
                + "&payload=" + payload + "====result=" + result);
        return rubbishRet;
    }


    public RubbishRet broadcastMsg(String from, String body) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.MsgUrl.broadcastMsg;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        //https://api.netease.im/nimserver/msg/broadcastMsg.action
        Map<String, Object> param = Maps.newHashMap();
        param.put("from", from);
        param.put("body", body);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        RubbishRet rubbishRet = gson.fromJson(result, RubbishRet.class);
        return rubbishRet;
    }


    /* 获取房间内在线成员信息 */
    public RoomUserListRet getRoomMemberListInfo(long roomId, long uid) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.RoomUrl.membersList;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        List<Object> accids = new ArrayList<>();
        accids.add(uid);
        String json = gson.toJson(accids);
        param.put("roomid", roomId);
        param.put("accids", json);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        RubbishRet rubbishRet = gson.fromJson(result, RubbishRet.class);
        RoomUserListRet roomListRet = null;
        if (rubbishRet.getCode() == 200) {
            roomListRet = gson.fromJson(result, RoomUserListRet.class);
        }

        logger.info("getRoomMemberListInfo roomid=" + roomId + " accids=" + json);
        return roomListRet;
    }

    public String addRobotToRoom(long roomId, String accids) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.RoomUrl.addRobot;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        String json = gson.toJson(accids);
        param.put("roomid", roomId);
        param.put("accids", accids);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        logger.info("addRobotToRoom roomid=" + roomId + " accids=" + json + "&result=" + result);
        return "";
    }

    public void deleteRobotToRoom(Long roomId, String id) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.RoomUrl.removeRobot;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        String json = gson.toJson(id);
        param.put("roomid", roomId);
        param.put("accids", id);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        logger.info("deleteRobotToRoom roomid=" + roomId + " accids=" + json + "&result=" + result);
    }

    /**
     * 列出队列中的所有元素
     *
     * @param roomId
     * @throws Exception
     */
    public Map<String, MicUserVo> queueList(Long roomId) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.RoomUrl.queueList;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost2();
        RubbishRet rubbishRet = gson.fromJson(result, RubbishRet.class);
        if (rubbishRet.getCode() != 200) {
            logger.error("queueList error, roomId:" + roomId + ", result:" + result);
            return Maps.newHashMap();
        }
        QueueRet queueRet = gson.fromJson(result, new TypeToken<QueueRet>() {
        }.getType());
        List<Map<String, String>> list = queueRet.getDesc().get("list");
        Map<String, MicUserVo> queueMap = Maps.newHashMap();
        for (Map<String, String> map : list) {
            String key = map.keySet().iterator().next();
            queueMap.put(key, gson.fromJson(map.get(key), MicUserVo.class));
        }
        return queueMap;
    }

    /**
     * 往聊天室有序队列中新加或更新元素
     *
     * @param roomId
     * @param key
     * @param value
     * @param operator 提交者
     * @param transie  提交者不在线时，提交的元素是否删除
     * @return
     * @throws Exception
     */
    public int queueOffer(long roomId, String key, String value, String operator, String transie)
            throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.RoomUrl.queueOffer;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        param.put("key", key);
        param.put("value", value);
        if (!BlankUtil.isBlank(operator)) {
            param.put("operator", operator);
        }
        if (!BlankUtil.isBlank(transie)) {
            param.put("transient", transie);
        }
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        return gson.fromJson(result, RubbishRet.class).getCode();
    }

    /**
     * 取出队列的元素
     *
     * @param roomId
     * @param key
     * @return
     * @throws Exception
     */
    public int queuePoll(long roomId, String key) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.RoomUrl.queuePoll;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        param.put("key", key);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost2();
        return gson.fromJson(result, RubbishRet.class).getCode();
    }

    /**
     * 清空队列中的所有元素
     *
     * @param roomId
     * @throws Exception
     */
    public int queueDrop(Long roomId) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.RoomUrl.queueDrop;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        logger.info("queueList roomid=" + roomId + "&result=" + result);
        return gson.fromJson(result, RubbishRet.class).getCode();
    }

    public void block(String accid, String needkick) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.AccUrl.block;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("accid", accid);
        param.put("needkick", needkick);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        logger.info("block accid=" + accid + "&result=" + result);
    }

    public void unblock(String accid) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.AccUrl.unblock;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("accid", accid);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        logger.info("unblock accid=" + accid + "&result=" + result);
    }

    /**
     * 获取聊天室地址
     *
     * @param accId      房主账号
     * @param roomId     房间
     * @param clientType 1:weblink（客户端为web端时使用）; 2:commonlink（客户端为非web端时使用）;3:wechatlink(微信小程序使用), 默认1
     * @return
     * @throws Exception
     */
    public AddrNetEaseRet inRoom(long roomId, String accId, int clientType) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.RoomUrl.requestAddr;
        NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        param.put("accid", accId);
        param.put("clienttype", clientType);
        String result = netEaseBaseClient.buildHttpPostParam(param).executePost();
        return gson.fromJson(result, AddrNetEaseRet.class);
    }

    public InitQueueRet initQueue(Long roomId, long sizeLimit) throws Exception {
        String url = NetEaseConstant.basicUrl + NetEaseConstant.RoomUrl.initQueue;
        NetEaseBaseClient client = new NetEaseBaseClient(url);
        Map<String, Object> param = Maps.newHashMap();
        param.put("roomid", roomId);
        param.put("sizeLimit", sizeLimit);
        String result = client.buildHttpPostParam(param).executePost();
        return gson.fromJson(result, InitQueueRet.class);
    }

    public static void main(String args[]) throws Exception {
        ErBanNetEaseService erBanNetEaseService = new ErBanNetEaseService();

//        RoomRet roomRet = erBanNetEaseService.getRoomMessage(18811543L);
//        RoomRet roomRet = erBanNetEaseService.getRoomMessage(18705331L);
//        UserInfoRet userInfoRet = erBanNetEaseService.getUserInfo("90650");
//        erBanNetEaseService.updateUserInfoOfExpand("90650", "{}");
        RoomRet roomRet = erBanNetEaseService.getRoomMessage(21377442L);
        UserInfoRet userInfoRet2 = erBanNetEaseService.getUserInfo("90650");
        System.out.println(roomRet.getChatroom());

//        Gson gson = new Gson();
//        erBanNetEaseService.openRoom("906769", "test", null);
////        erBanNetEaseService.queueDrop(18515497L);
////        Map<String, MicUserVo> map = erBanNetEaseService.queueList(18515497L);
//        RoomRet roomRet = erBanNetEaseService.getRoomMessage(18515497L);
//        RoomUserListRet elist = erBanNetEaseService.getRoomMemberListInfo(18515497L, 90970L);
//        System.out.println(roomRet.getChatroom());
//
//        String result = erBanNetEaseService.getMembersByPage(18515497L, 1, System.currentTimeMillis(), 100);
//        RubbishRet rubbishRet = gson.fromJson(result, RubbishRet.class);
//        RoomUserListRet roomUserListRet = gson.fromJson(result, RoomUserListRet.class);
//        List<Map<String, Object>> list = roomUserListRet.getDesc().get("data");

        System.out.println("=========================end");
//        Set<String> keySet = map.keySet();
//        for (String key : keySet) {
//            MicUserVo micUserVo = map.get(key);
//            System.out.println(micUserVo.toString());
//        }
//        roomRet.getChatroom();
//        List<QueueItem> list = erBanNetEaseService.queueList(18515497L);
//        System.out.println(list);
//        erBanNetEaseService.queuePoll(18515497L, "4");
//        erBanNetEaseService.queueOffer(19260886L, "1", "{uid:90972}", null, null);
//        list = erBanNetEaseService.queueList(19260886L);
//        System.out.println(list);
    }

}
