package com.tongdaxing.xchat_core.room.model;

import com.netease.nim.uikit.common.util.string.StringUtil;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.BaseMvpModel;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

import java.util.Map;

/**
 * <p> 房间设置 </p>
 *
 * @author jiahui
 * @date 2017/12/15
 */
public class RoomSettingModel extends BaseMvpModel {

    public RoomSettingModel() {

    }

    public void requestTagAll(String ticket, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("ticket", ticket);
        OkHttpManager.getInstance().doPostRequest(UriProvider.getRoomTagList(), param, myCallBack);
    }

    /**
     * 更新房间设置信息
     *
     * @param title
     * @param desc
     * @param pwd
     * @param label 标签名字
     * @param tagId 标签id
     */
    public void updateRoomInfo(String title, String desc, String pwd, String label, int tagId, long uid, String ticket, String backPic, int giftEffect, int ridtEffect, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        if (StringUtils.isNotEmpty(title)) {
            param.put("title", title);
        }
        if (desc != null) {
            param.put("roomDesc", desc);
        }
        if (pwd != null) {
            param.put("roomPwd", pwd);
        }
        if (StringUtils.isNotEmpty(label)) {
            param.put("roomTag", label);
        }
        if (StringUtils.isNotEmpty(backPic)) {
            param.put("backPic", backPic);
        }
        param.put("tagId", String.valueOf(tagId));
        param.put("uid", String.valueOf(uid));
        param.put("ticket", ticket);
        param.put("giftEffectSwitch", giftEffect + "");
        param.put("giftCardSwitch", ridtEffect + "");
        OkHttpManager.getInstance().doPostRequest(UriProvider.updateRoomInfo(), param, myCallBack);
    }

    /**
     * 更新房间设置信息
     *
     * @param title
     * @param desc
     * @param pwd
     * @param label 标签名字
     * @param tagId 标签id
     */
    public void updateByAdmin(long roomUid, String title, String desc, String pwd, String label, int tagId, long uid, String ticket, String backPic, int giftEffect, int rideEffect, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("roomUid", roomUid + "");
        if (StringUtils.isNotEmpty(title)) {
            param.put("title", title);
        }
        if (desc != null) {
            param.put("roomDesc", desc);
        }
        if (pwd != null) {
            param.put("roomPwd", pwd);
        }
        if (StringUtils.isNotEmpty(label)) {
            param.put("roomTag", label);
        }
        if (StringUtils.isNotEmpty(backPic)) {
            param.put("backPic", backPic);
        }
        param.put("tagId", tagId + "");
        param.put("uid", uid + "");
        param.put("ticket", ticket);
        param.put("giftEffectSwitch", giftEffect + "");
        param.put("giftCardSwitch", rideEffect + "");
        OkHttpManager.getInstance().doPostRequest(UriProvider.updateByAdimin(), param, myCallBack);
    }

    /**
     * 保存房间主题
     */
    public void saveRoomPlayTip(String playInfo, RoomInfo roomInfo, String url, OkHttpManager.MyCallBack callBack) {
        Map<String, String> params = getModifyRoomParams(roomInfo);
        params.put("playInfo", playInfo);

        OkHttpManager.getInstance().doPostRequest(url, params, callBack);
    }

    /**
     * 修改房间话题
     */
    public void saveRoomTopic(String roomDesc, String roomNotice, RoomInfo roomInfo, String url, OkHttpManager.MyCallBack callBack) {
        Map<String, String> params = getModifyRoomParams(roomInfo);

        params.put("roomDesc", roomDesc);
        params.put("roomNotice", roomNotice);

        OkHttpManager.getInstance().doPostRequest(url, params, callBack);
    }

    private Map<String, String> getModifyRoomParams(RoomInfo roomInfo) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("tagId", String.valueOf(roomInfo.tagId));
        param.put("roomUid", String.valueOf(roomInfo.getUid()));
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        return param;
    }

}
