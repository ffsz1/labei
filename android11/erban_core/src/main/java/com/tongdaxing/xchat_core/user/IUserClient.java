package com.tongdaxing.xchat_core.user;


import com.tongdaxing.xchat_core.room.bean.TaskBean;
import com.tongdaxing.xchat_core.user.bean.GiftWallInfo;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by chenran on 2017/3/15.
 */

public interface IUserClient extends ICoreClient {
    String METHOD_REQUEST_OPEN_TEENAGER_MODEL_DIALOG = "isShowTeenagerModelDialog";

    String METHOD_REQUEST_USER_INFO = "onRequestUserInfo";
    String METHOD_REQUEST_USER_INFO_MAP = "onRequestUserInfoMap";
    String METHOD_REQUEST_USER_INFO_ERROR = "onRequestUserInfoError";

    String METHOD_REQUEST_USER_INFO_MAP_HOME = "onRequestUserInfoMapHome";
    String METHOD_REQUEST_USER_INFO_ERROR_HOME = "onRequestUserInfoErrorHome";

    String METHOD_REQUEST_USER_INFO_MAP_ATTENTION = "onRequestUserInfoMapAttention";
    String METHOD_REQUEST_USER_INFO_ERROR_ATTENTION = "onRequestUserInfoMapErrorAttention";

    String METHOD_REQUEST_USER_INFO_MAP_ERROR = "onRequestUserInfoMapError";
    String METHOD_REQUEST_USER_INFO_UPDAET = "onRequestUserInfoUpdate";
    String METHOD_REQUEST_USER_INFO_UPDAET_ERROR = "onRequestUserInfoUpdateError";
    String METHOD_ON_SEARCH_USERINFO = "onSearchUserInfo";
    String METHOD_ON_SEARCH_USERINFO_FAITH = "onSearchUserInfoFail";
    String METHOD_ON_CURRENT_USERINFO_UPDATE = "onCurrentUserInfoUpdate";
    String METHOD_ON_CURRENT_USERINFO_UPDATE_FAIL = "onCurrentUserInfoUpdateFail";

    String METHOD_ON_CURRENT_USER_INFO_COMPLETE = "onCurrentUserInfoComplete";
    String METHOD_ON_CURRENT_USER_INFO_COMPLETE_FAITH = "onCurrentUserInfoCompleteFaith";

    String METHOD_ON_REQUEST_ADD_PHOTO = "onRequestAddPhoto";
    String METHOD_ON_REQUEST_ADD_PHOTO_FAITH = "onRequestAddPhotoFaith";

    String METHOD_ON_REQUEST_DELETE_PHOTO = "onRequestDeletePhoto";
    String METHOD_ON_REQUEST_DELETE_PHOTO_FAITH = "onRequestDeletePhotoFaith";

    String METHOD_ON_REQUEST_GIFT_WALL = "onRequestGiftWall";
    String METHOD_ON_REQUEST_GIFT_WALL_FAIL = "onRequestGiftWallFail";

    String METHOD_ON_NEED_COMPLETE_INFO = "onNeedCompleteInfo";

    String METHOD_ON_TASK_LIST = "onTaskList";
    String METHOD_ON_TASK_LIST_FAIL = "onTaskListFAIL";

    String METHOD_ON_SAVE_SHARE_CODE = "onSaveShareCode";
    String METHOD_ON_SAVE_SHARE_CODE_FAIL = "onSaveShareCodeFail";

    void isShowTeenagerModelDialog();

    void onNeedCompleteInfo();

    void onCurrentUserInfoUpdate(UserInfo userInfo);

    void onCurrentUserInfoUpdateFail(String msg);

    void onCurrentUserInfoComplete(UserInfo userInfo);

    void onCurrentUserInfoCompleteFaith(String msg);

    void onRequestUserInfo(UserInfo info);

    void onRequestUserInfoError(String error);

    void onRequestUserInfoMap(LinkedHashMap<Long, UserInfo> map);

    void onRequestUserInfoMapError();

    void onRequestUserInfoMapHome(LinkedHashMap<Long, UserInfo> map);

    void onRequestUserInfoMapErrorHome();

    void onRequestUserInfoMapAttention(LinkedHashMap<Long, UserInfo> map);

    void onRequestUserInfoMapErrorAttention();


    void onRequestUserInfoUpdate(UserInfo userInfo);

    void onRequestUserInfoUpdateError(String error);

    void onSearchUserInfo(UserInfo userInfo);

    void onSearchUserInfoFail(String error);

    void onRequestAddPhoto();

    void onRequestAddPhotoFaith(String msg);

    void onRequestGiftWall(List<GiftWallInfo> giftWallInfoList);

    void onRequestGiftWallFail(String msg);

    void onRequestDeletePhoto();

    void onRequestDeletePhotoFaith(String msg);

    //每日任务
    void onTaskList(TaskBean task);

    void onTaskListFAIL(String error);

    void onSaveShareCode(UserInfo userInfo);

    void onSaveShareCodeFail(String msg);
}
