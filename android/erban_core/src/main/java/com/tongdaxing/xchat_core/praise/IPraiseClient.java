package com.tongdaxing.xchat_core.praise;

import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

import java.util.List;

/**
 * Created by zhouxiangfeng on 2017/5/18.
 */

public interface IPraiseClient extends ICoreClient {

    public static final String METHOD_ON_PRAISE = "onPraise";
    public static final String METHOD_ON_PRAISE_FAITH = "onPraiseFaith";
    public static final String METHOD_ON_CANCELED_PRAISE = "onCanceledPraise";
    public static final String METHOD_ON_CANCELED_PRAISE_FAITH = "onCanceledPraiseFaith";
    public static final String METHOD_ON_DELETE_LIKE = "onDeleteLike";
    public static final String METHOD_ON_DELETE_LIKE_FAITH = "onDeleteLikeFaith";
    public static final String METHOD_ON_GET_ALL_FANS = "onGetAllFans";
    public static final String METHOD_ON_GET_ALL_FANS_FAITH = "onGetAllFansFaith";
    public static final String METHOD_ON_ISLIKED = "onIsLiked";
    public static final String METHOD_ON_ISLIKED_FAITH = "onIsLikedFail";

    void onPraise(long uid);

    void onPraiseFaith(String error);

    void onCanceledPraise(long uid, boolean showNotice);

    void onCanceledPraiseFaith(String error);

    void onDeleteLike();

    void onDeleteLikeFaith(String error);

    void onIsLiked(Boolean islike, long uid);

    void onIsLikedFail(String error);

    void onGetAllFans(List<UserInfo> userInfoList);

    void onGetAllFansFaith(String error);

//    void onGoneBtn(UserInfo userInfo);

}
