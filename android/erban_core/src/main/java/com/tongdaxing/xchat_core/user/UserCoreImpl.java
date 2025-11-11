package com.tongdaxing.xchat_core.user;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.AccountInfo;
import com.tongdaxing.xchat_core.auth.IAuthClient;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.ReUsedSocketManager;
import com.tongdaxing.xchat_core.room.bean.TaskBean;
import com.tongdaxing.xchat_core.user.bean.GiftWallInfo;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.config.SpEvent;
import com.tongdaxing.xchat_framework.util.util.SpUtils;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Created by chenran on 2017/3/15.
 */

public class UserCoreImpl extends AbstractBaseCore implements IUserCore {
    private IUserDbCore userDbCore;
    private UserInfo currentUserInfo;
    private static final String TAG = "UserCoreImpl";
    private Map<Long, UserInfo> mInfoCache = new ConcurrentHashMap(new HashMap<Long, UserInfo>());
    public static final int TYPE_HOME = 1;
    public static final int TYPE_ATTENTION = 2;

    public UserCoreImpl() {
        super();
        userDbCore = CoreManager.getCore(IUserDbCore.class);
        CoreManager.addClient(this);
    }

    @Override
    public void saveCache(long userId, UserInfo uInfo) {
        if (userId > 0 && uInfo != null) {
            mInfoCache.put(userId, uInfo);
        }
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onLogin(final AccountInfo accountInfo) {
        long uid = accountInfo.getUid();
        SpUtils.put(getContext(), SpEvent.cache_uid, uid + "");
        currentUserInfo = getCacheUserInfoByUid(accountInfo.getUid());
        if (currentUserInfo != null && (!StringUtil.isEmpty(currentUserInfo.getNick()) && !StringUtil.isEmpty(currentUserInfo.getAvatar()))) {
            updateCurrentUserInfo(currentUserInfo.getUid(), true);
            return;
        }

        NimUserInfo nimUserInfo = NimUserInfoCache.getInstance().getUserInfo(accountInfo.getUid() + "");
        if (nimUserInfo == null) {
            NimUserInfoCache.getInstance().getUserInfoFromRemote(accountInfo.getUid() + "", new RequestCallbackWrapper<NimUserInfo>() {
                @Override
                public void onResult(int i, NimUserInfo nimUserInfo, Throwable throwable) {
                    if (nimUserInfo == null || StringUtil.isEmpty(nimUserInfo.getName()) || StringUtil.isEmpty(nimUserInfo.getAvatar())) {
                        notifyClients(IUserClient.class, IUserClient.METHOD_ON_NEED_COMPLETE_INFO);
                    } else {
                        updateCurrentUserInfo(accountInfo.getUid(), true);
                    }
                }
            });
        } else {
            if (StringUtil.isEmpty(nimUserInfo.getName()) || StringUtil.isEmpty(nimUserInfo.getAvatar())) {
                notifyClients(IUserClient.class, IUserClient.METHOD_ON_NEED_COMPLETE_INFO);
            } else {
                updateCurrentUserInfo(accountInfo.getUid(), true);
            }
        }
    }


    private void updateCurrentUserInfo(final long userId) {
        updateCurrentUserInfo(userId, false);
    }

    private void updateCurrentUserInfo(final long userId, boolean needLogin) {
        //: 2018/11/2 im login
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("queryUid", String.valueOf(userId));

        final String uid = CoreManager.getCore(IAuthCore.class).getCurrentUid() + "";
        params.put("uid", uid);
        final String ticket = CoreManager.getCore(IAuthCore.class).getTicket();
        params.put("ticket", ticket);

        OkHttpManager.getInstance().getRequest(UriProvider.getUserInfo(), params, new OkHttpManager.MyCallBack<ServiceResult<UserInfo>>() {
            @Override
            public void onError(Exception e) {
                if (currentUserInfo == null || StringUtil.isEmpty(currentUserInfo.getNick()) || StringUtil.isEmpty(currentUserInfo.getAvatar())) {
                    notifyClients(IUserClient.class, IUserClient.METHOD_ON_CURRENT_USERINFO_UPDATE_FAIL, e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<UserInfo> response) {
                if (response.isSuccess()) {
                    UserInfo userInfo = response.getData();

                    ReUsedSocketManager.get().initIM(uid, ticket);

                    if (StringUtil.isEmpty(userInfo.getNick()) || StringUtil.isEmpty(userInfo.getAvatar())) {
                        notifyClients(IUserClient.class, IUserClient.METHOD_ON_NEED_COMPLETE_INFO);
                        return;
                    }
                    saveCache(userId, response.getData());
                    userDbCore.saveDetailUserInfo(response.getData());
                    currentUserInfo = response.getData();
                    notifyClients(IUserClient.class, IUserClient.METHOD_ON_CURRENT_USERINFO_UPDATE, response.getData());
                } else {
                    if (currentUserInfo == null || StringUtil.isEmpty(currentUserInfo.getNick()) || StringUtil.isEmpty(currentUserInfo.getAvatar())) {
                        notifyClients(IUserClient.class, IUserClient.METHOD_ON_CURRENT_USERINFO_UPDATE_FAIL, response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void searchUserInfo(String skey, int pageSize, int pageNo) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("key", skey);
        params.put("pageSize", String.valueOf(pageSize));
        params.put("pageNo", String.valueOf(pageNo));

        OkHttpManager.getInstance().getRequest(UriProvider.searchUserInfo(), params, new OkHttpManager.MyCallBack<ServiceResult<UserInfo>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IUserClient.class, IUserClient.METHOD_ON_SEARCH_USERINFO_FAITH, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<UserInfo> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IUserClient.class, IUserClient.METHOD_ON_SEARCH_USERINFO, response.getData());
                    } else {
                        notifyClients(IUserClient.class, IUserClient.METHOD_ON_SEARCH_USERINFO_FAITH, response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void requestUserInfo(final long userId) {
        if (userId <= 0) {
            return;
        }

        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("queryUid", String.valueOf(userId));
        //新增参数
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());

        OkHttpManager.getInstance().getRequest(UriProvider.getUserInfo(), params, new OkHttpManager.MyCallBack<ServiceResult<UserInfo>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IUserClient.class, IUserClient.METHOD_REQUEST_USER_INFO_ERROR, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<UserInfo> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        saveCache(userId, response.getData());
                        userDbCore.saveDetailUserInfo(response.getData());
                        notifyClients(IUserClient.class, IUserClient.METHOD_REQUEST_USER_INFO, response.getData());
                    } else {
                        notifyClients(IUserClient.class, IUserClient.METHOD_REQUEST_USER_INFO_ERROR, response.getMessage());
                    }
                } else {
                    notifyClients(IUserClient.class, IUserClient.METHOD_REQUEST_USER_INFO_ERROR, response.getMessage());
                }
            }
        });
    }

    @Nullable
    @Override
    public UserInfo getCacheUserInfoByUid(long userId) {
        return getCacheUserInfoByUid(userId, false);
    }

    @Override
    public UserInfo getCacheUserInfoByUid(long userId, boolean refresh) {
        if (userDbCore == null) {
            userDbCore = CoreManager.getCore(IUserDbCore.class);
        }

        if (userId == 0) {
            return null;
        }

        UserInfo userInfo = mInfoCache.get(userId);
        if (userInfo == null) {
            if (userDbCore != null)
                userInfo = userDbCore.queryDetailUserInfo(userId);

        }

        if (refresh) {
            requestUserInfo(userId);

        }
        return userInfo;
    }

    @Override
    public void requestUserInfoMapByUidList(@NonNull final List<Long> uidListToQuery, LinkedHashMap<Long, UserInfo> rstMap) {

        if (uidListToQuery == null || uidListToQuery.size() < 1) {
            notifyClients(IUserClient.class, IUserClient.METHOD_REQUEST_USER_INFO_MAP, rstMap);
            return;
        }
        final LinkedHashMap<Long, UserInfo> userInfoLinkedHashMap
                = (rstMap == null ? new LinkedHashMap<Long, UserInfo>(uidListToQuery.size()) : rstMap);
        final int count = uidListToQuery.size();
        //每次最多处理50个
        final int dealNum = count > 50 ? 50 : count;
        List<Long> paramList = uidListToQuery.subList(0, dealNum);
        final int leftCountToDeal = count - dealNum;
        //请求
        Map<String, String> params = CommonParamUtil.getDefaultParam();

        String listStr = StringUtils.join(paramList, ",");
        params.put("uids", listStr);

        OkHttpManager.getInstance().getRequest(UriProvider.getUserInfoListUrl(), params, new OkHttpManager.MyCallBack<ServiceResult<List<UserInfo>>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IUserClient.class, IUserClient.METHOD_REQUEST_USER_INFO_MAP_ERROR, new Object() {
                });
            }

            @Override
            public void onResponse(ServiceResult<List<UserInfo>> response) {
                Log.d(TAG, "外面的response===" + (response == null));
                Log.d(TAG, "response===" + response);
                if (response != null && response.isSuccess()) {
                    Log.d(TAG, "里面的response===" + (response == null));
                    List<UserInfo> userInfoList = response.getData();
                    if (userInfoList == null || userInfoList.size() < 1) {
                        return;
                    }
                    for (UserInfo userInfo : userInfoList) {
                        saveCache(userInfo.getUid(), userInfo);
                        userDbCore.saveDetailUserInfo(userInfo);
                        userInfoLinkedHashMap.put(userInfo.getUid(), userInfo);
                    }
                    //最后一次更新完才通知
                    if (leftCountToDeal <= 0) {
                        notifyClients(IUserClient.class, IUserClient.METHOD_REQUEST_USER_INFO_MAP, userInfoLinkedHashMap);
                    } else {
                        //递归调用
                        requestUserInfoMapByUidList(uidListToQuery.subList(dealNum, count), userInfoLinkedHashMap);
                    }

                } else {
                    notifyClients(IUserClient.class, IUserClient.METHOD_REQUEST_USER_INFO_MAP_ERROR, new Object() {
                    });
                }
            }
        });
    }

    @Override
    public void requestUserInfoMapByUidList(@NonNull final List<Long> uidListToQuery, LinkedHashMap<Long, UserInfo> rstMap, final int type) {

        if (uidListToQuery == null || uidListToQuery.size() < 1) {
            if (type == TYPE_ATTENTION) {
                notifyClients(IUserClient.class, IUserClient.METHOD_REQUEST_USER_INFO_MAP_ATTENTION, rstMap);
            } else {
                notifyClients(IUserClient.class, IUserClient.METHOD_REQUEST_USER_INFO_MAP_HOME, rstMap);
            }
            return;
        }
        final LinkedHashMap<Long, UserInfo> userInfoLinkedHashMap
                = (rstMap == null ? new LinkedHashMap<Long, UserInfo>(uidListToQuery.size()) : rstMap);
        final int count = uidListToQuery.size();
        //每次最多处理50个
        final int dealNum = count > 50 ? 50 : count;
        List<Long> paramList = uidListToQuery.subList(0, dealNum);
        final int leftCountToDeal = count - dealNum;
        //请求
        Map<String, String> params = CommonParamUtil.getDefaultParam();

        String listStr = StringUtils.join(paramList, ",");
        params.put("uids", listStr);

        OkHttpManager.getInstance().getRequest(UriProvider.getUserInfoListUrl(), params, new OkHttpManager.MyCallBack<ServiceResult<List<UserInfo>>>() {

            @Override
            public void onError(Exception e) {
                if (type == TYPE_ATTENTION) {
                    notifyClients(IUserClient.class, IUserClient.METHOD_REQUEST_USER_INFO_ERROR_ATTENTION, new Object() {
                    });
                } else {
                    notifyClients(IUserClient.class, IUserClient.METHOD_REQUEST_USER_INFO_ERROR_HOME, new Object() {
                    });
                }
            }

            @Override
            public void onResponse(ServiceResult<List<UserInfo>> response) {
                Log.d(TAG, "外面的response===" + (response == null));
                Log.d(TAG, "response===" + response);
                if (response != null && response.isSuccess()) {
                    Log.d(TAG, "里面的response===" + (response == null));
                    List<UserInfo> userInfoList = response.getData();
                    if (userInfoList == null || userInfoList.size() < 1) {
                        return;
                    }
                    for (UserInfo userInfo : userInfoList) {
                        saveCache(userInfo.getUid(), userInfo);
                        userDbCore.saveDetailUserInfo(userInfo);
                        userInfoLinkedHashMap.put(userInfo.getUid(), userInfo);
                    }
                    if (leftCountToDeal <= 0) {//最后一次更新完才通知
                        if (type == TYPE_ATTENTION) {
                            notifyClients(IUserClient.class, IUserClient.METHOD_REQUEST_USER_INFO_MAP_ATTENTION, userInfoLinkedHashMap);
                        } else {
                            notifyClients(IUserClient.class, IUserClient.METHOD_REQUEST_USER_INFO_MAP_HOME, userInfoLinkedHashMap);
                        }

                    } else {
                        requestUserInfoMapByUidList(uidListToQuery.subList(dealNum, count), userInfoLinkedHashMap, type);//递归调用
                    }

                } else {
                    if (type == TYPE_ATTENTION) {
                        notifyClients(IUserClient.class, IUserClient.METHOD_REQUEST_USER_INFO_ERROR_ATTENTION, new Object() {
                        });
                    } else {
                        notifyClients(IUserClient.class, IUserClient.METHOD_REQUEST_USER_INFO_ERROR_HOME, new Object() {
                        });
                    }

                }
            }
        });
    }

    @Override
    public LinkedHashMap<Long, UserInfo> getCacheThenServerUserInfoMapByUidList(List<Long> uidList) {
        if (uidList == null || uidList.size() < 1) {
            return new LinkedHashMap<>();
        }
        LinkedHashMap<Long, UserInfo> userInfoLinkedHashMap = new LinkedHashMap<>();
        Log.d(TAG, "uidListsize===" + uidList.size());
        ArrayList<Long> uidListToQuery = new ArrayList<>();
        //从缓存取
        for (int i = 0; i < uidList.size(); i++) {
            Long userId = uidList.get(i);
            UserInfo userInfo = mInfoCache.get(userId);
            if (userInfo == null) {
                userInfo = userDbCore.queryDetailUserInfo(userId);
            }
            userInfoLinkedHashMap.put(userId, userInfo);
            if (userInfo == null) {
                uidListToQuery.add(userId);
            }
        }
        if (uidListToQuery.size() > 0) {
            Log.d(TAG, "getCacheThenServerUserInfoMapByUidList: 111111111111");
        }
        requestUserInfoMapByUidList(uidListToQuery, userInfoLinkedHashMap);
        return userInfoLinkedHashMap;
    }

    @NonNull
    @Override
    public LinkedHashMap<Long, UserInfo> getCacheThenServerUserInfoMapByUidList(List<Long> uidList, int type) {
        if (uidList == null || uidList.size() < 1) return new LinkedHashMap<>();
        LinkedHashMap<Long, UserInfo> userInfoLinkedHashMap = new LinkedHashMap<>();
        Log.d(TAG, "uidListsize===" + uidList.size());
        ArrayList<Long> uidListToQuery = new ArrayList<>();
        //从缓存取
        for (int i = 0; i < uidList.size(); i++) {
            Long userId = uidList.get(i);
            UserInfo userInfo = mInfoCache.get(userId);
            if (userInfo == null) {
                userInfo = userDbCore.queryDetailUserInfo(userId);
            }
            userInfoLinkedHashMap.put(userId, userInfo);
            if (userInfo == null) {
                uidListToQuery.add(userId);
            }
        }
        if (uidListToQuery.size() > 0) {
            Log.d(TAG, "getCacheThenServerUserInfoMapByUidList: 111111111111");
        }
        requestUserInfoMapByUidList(uidListToQuery, userInfoLinkedHashMap, type);
        return userInfoLinkedHashMap;
    }

    @Nullable
    @Override
    public UserInfo getCacheLoginUserInfo() {
        UserInfo loginUser = getCacheUserInfoByUid(CoreManager.getCore(IAuthCore.class).getCurrentUid());
        return loginUser;
    }

    @Override
    public void requestCompleteUserInfo(final UserInfo userInfo, String shareChannel, String shareUid, String roomUid) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        final String ticket = CoreManager.getCore(IAuthCore.class).getTicket();
        params.put("ticket", ticket);
        //解决uid可能为空，导致一直转圈的问题
        String cacheUid = "";

        try {
            cacheUid = (String) SpUtils.get(getContext(), SpEvent.cache_uid, "");
        } catch (Exception e) {

        }

        String uid = String.valueOf(userInfo.getUid());
        if (!StringUtils.isEmpty(uid)) {
            params.put("uid", uid);
        } else if (!StringUtils.isEmpty(cacheUid)) {
            uid = cacheUid;
            params.put("uid", cacheUid);
        } else {
            notifyClients(IUserClient.class, IUserClient.METHOD_ON_CURRENT_USER_INFO_COMPLETE_FAITH, "数据异常，请杀掉进程再次打开");
            return;
        }
        if (!StringUtils.isEmpty(userInfo.getBirthStr())) {

            params.put("birth", String.valueOf(userInfo.getBirthStr()));
        }
        if (!StringUtils.isEmpty(userInfo.getNick())) {
            params.put("nick", userInfo.getNick());
        }
        if (!StringUtils.isEmpty(userInfo.getAvatar())) {
            params.put("avatar", userInfo.getAvatar());
        }
        if (userInfo.getGender() > 0) {
            params.put("gender", String.valueOf(userInfo.getGender()));
        }
        if (!StringUtils.isEmpty(shareChannel)) {
            params.put("shareChannel", shareChannel);
        }
        if (!StringUtils.isEmpty(shareUid)) {
            params.put("shareUid", shareUid);
            SpUtils.put(getContext(), SpEvent.linkedMeShareUid, "");
        } else {
            String spShareUid = (String) SpUtils.get(getContext(), SpEvent.linkedMeShareUid, "");
            if (!StringUtils.isEmpty(spShareUid)) {
                params.put("shareUid", spShareUid);
                SpUtils.put(getContext(), SpEvent.linkedMeShareUid, "");
            }

        }
        if (!StringUtils.isEmpty(roomUid)) {
            if (isNumeric(roomUid)) {
                params.put("roomUid", roomUid);
            }
        }

        if (StringUtils.isNotEmpty(userInfo.getShareCode())) {
            params.put("shareCode", userInfo.getShareCode());
        }

        final String finalUid = uid;

        OkHttpManager.getInstance().doPostRequest(UriProvider.updateUserInfo(), params, new OkHttpManager.MyCallBack<ServiceResult<UserInfo>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IUserClient.class, IUserClient.METHOD_ON_CURRENT_USER_INFO_COMPLETE_FAITH, "网络异常");
            }

            @Override
            public void onResponse(ServiceResult<UserInfo> response) {
                if (response != null && response.isSuccess()) {
                    UserInfo data = response.getData();
                    ReUsedSocketManager.get().initIM(finalUid, ticket);
                    //: 2018/11/2 im login
                    saveCache(data.getUid(), data);
                    userDbCore.saveDetailUserInfo(data);
                    currentUserInfo = data;
                    CoreManager.getCore(IUserCore.class).saveCache(userInfo.getUid(), userInfo);
                    notifyClients(IUserClient.class, IUserClient.METHOD_ON_CURRENT_USERINFO_UPDATE, response.getData());
                    notifyClients(IUserClient.class, IUserClient.METHOD_ON_CURRENT_USER_INFO_COMPLETE, response.getData());
                } else {
                    notifyClients(IUserClient.class, IUserClient.METHOD_ON_CURRENT_USER_INFO_COMPLETE_FAITH, response.getMessage());
                }
            }
        });
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    @Override
    public void requestUpdateUserInfo(final UserInfo userInfo) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        String ticket = CoreManager.getCore(IAuthCore.class).getTicket();
        params.put("ticket", ticket);
        if (!StringUtils.isEmpty(String.valueOf(userInfo.getUid()))) {
            params.put("uid", String.valueOf(userInfo.getUid()));
        } else {
            return;
        }
        if (!StringUtils.isEmpty(userInfo.getBirthStr())) {
            params.put("birth", String.valueOf(userInfo.getBirthStr()));
        }
        if (!StringUtils.isEmpty(userInfo.getNick())) {
            params.put("nick", userInfo.getNick());
        }
        if (!StringUtils.isEmpty(userInfo.getSignture())) {
            params.put("signture", userInfo.getSignture());
        }
        if (!StringUtils.isEmpty(userInfo.getUserVoice())) {
            params.put("userVoice", userInfo.getUserVoice());
        }
        if (userInfo.getVoiceDura() > 0) {
            params.put("voiceDura", String.valueOf(userInfo.getVoiceDura()));
        }
        if (!StringUtils.isEmpty(userInfo.getAvatar())) {
            params.put("avatar", userInfo.getAvatar());
        }
        if (!StringUtils.isEmpty(userInfo.getRegion())) {
            params.put("region", userInfo.getRegion());
        }
        if (!StringUtils.isEmpty(userInfo.getUserDesc())) {
            params.put("userDesc", userInfo.getUserDesc());
        }
        if (userInfo.getGender() > 0) {
            params.put("gender", String.valueOf(userInfo.getGender()));
        }

        OkHttpManager.getInstance().doPostRequest(UriProvider.updateUserInfo(), params, new OkHttpManager.MyCallBack<ServiceResult<UserInfo>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IUserClient.class, IUserClient.METHOD_REQUEST_USER_INFO_UPDAET_ERROR, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<UserInfo> response) {
                if (response != null && response.isSuccess()) {
                    UserInfo data = response.getData();
                    saveCache(data.getUid(), data);
                    userDbCore.saveDetailUserInfo(data);
                    currentUserInfo = data;
                    notifyClients(IUserClient.class, IUserClient.METHOD_ON_CURRENT_USERINFO_UPDATE, response.getData());
                    notifyClients(IUserClient.class, IUserClient.METHOD_REQUEST_USER_INFO_UPDAET, response.getData());
                } else {
                    notifyClients(IUserClient.class, IUserClient.METHOD_REQUEST_USER_INFO_UPDAET_ERROR, response.getMessage());
                }
            }
        });
    }


    @Override
    public void saveInviteCode(UserInfo userInfo, String inviteCode) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        if (StringUtils.isNotEmpty(inviteCode)) {
            params.put("shareCode", inviteCode);
        }
        OkHttpManager.getInstance().doPostRequest(UriProvider.updateUserInfo(), params, new OkHttpManager.MyCallBack<ServiceResult<UserInfo>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IUserClient.class, IUserClient.METHOD_ON_SAVE_SHARE_CODE_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<UserInfo> response) {
                if (response != null && response.isSuccess()) {
                    UserInfo data = response.getData();
                    saveCache(data.getUid(), data);
                    userDbCore.saveDetailUserInfo(data);
                    currentUserInfo = data;
                    notifyClients(IUserClient.class, IUserClient.METHOD_ON_SAVE_SHARE_CODE, response.getData());
                } else {
                    notifyClients(IUserClient.class, IUserClient.METHOD_ON_SAVE_SHARE_CODE_FAIL, response.getMessage());
                }
            }
        });
    }

    @Override
    public void requestAddPhoto(String url) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        String ticket = CoreManager.getCore(IAuthCore.class).getTicket();
        params.put("ticket", ticket);
        params.put("photoStr", url);
        final long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        params.put("uid", uid + "");
        OkHttpManager.getInstance().doPostRequest(UriProvider.addPhoto(), params, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IUserClient.class, IUserClient.METHOD_ON_REQUEST_ADD_PHOTO_FAITH, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null && response.isSuccess()) {
                    updateCurrentUserInfo(uid);
                    notifyClients(IUserClient.class, IUserClient.METHOD_ON_REQUEST_ADD_PHOTO);
                } else {
                    notifyClients(IUserClient.class, IUserClient.METHOD_ON_REQUEST_ADD_PHOTO_FAITH, response.getMessage());
                }
            }
        });
    }

    @Override
    public void requestDeletePhoto(long pid) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        String ticket = CoreManager.getCore(IAuthCore.class).getTicket();
        params.put("ticket", ticket);

        final long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        params.put("uid", uid + "");
        params.put("pid", pid + "");

        OkHttpManager.getInstance().doPostRequest(UriProvider.deletePhoto(), params, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IUserClient.class, IUserClient.METHOD_ON_REQUEST_DELETE_PHOTO_FAITH, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null && response.isSuccess()) {
                    updateCurrentUserInfo(uid);
                    notifyClients(IUserClient.class, IUserClient.METHOD_ON_REQUEST_DELETE_PHOTO);
                } else {
                    notifyClients(IUserClient.class, IUserClient.METHOD_ON_REQUEST_DELETE_PHOTO_FAITH, response.getMessage());
                }
            }
        });
    }

    @Override
    public void requestUserGiftWall(long uid, int orderType) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", uid + "");
        params.put("orderType", orderType + "");

        OkHttpManager.getInstance().getRequest(UriProvider.giftWall(), params, new OkHttpManager.MyCallBack<ServiceResult<List<GiftWallInfo>>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IUserClient.class, IUserClient.METHOD_ON_REQUEST_GIFT_WALL_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<List<GiftWallInfo>> response) {
                if (response != null && response.isSuccess()) {
                    notifyClients(IUserClient.class, IUserClient.METHOD_ON_REQUEST_GIFT_WALL, response.getData());
                } else {
                    notifyClients(IUserClient.class, IUserClient.METHOD_ON_REQUEST_GIFT_WALL_FAIL, response.getMessage());
                }
            }
        });
    }

    @Override
    public void requestUserMysteryGiftWall(long uid, long queryUid, int orderType) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid + "");
        params.put("queryUid", queryUid + "");
        params.put("orderType", orderType + "");
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());

        OkHttpManager.getInstance().getRequest(UriProvider.getMysteryGiftWall(), params, new OkHttpManager.MyCallBack<ServiceResult<List<GiftWallInfo>>>() {
            @Override
            public void onError(Exception e) {
                notifyClients(IUserClient.class, IUserClient.METHOD_ON_REQUEST_GIFT_WALL_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<List<GiftWallInfo>> response) {
                if (response != null && response.isSuccess()) {
                    notifyClients(IUserClient.class, IUserClient.METHOD_ON_REQUEST_GIFT_WALL, response.getData());
                } else {
                    notifyClients(IUserClient.class, IUserClient.METHOD_ON_REQUEST_GIFT_WALL_FAIL, response != null ? response.getMessage() : "数据异常");
                }
            }
        });

    }

    @Override
    public void getTaskList() {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");

        OkHttpManager.getInstance().getRequest(UriProvider.getTaskList(), params, new OkHttpManager.MyCallBack<ServiceResult<TaskBean>>() {
            @Override
            public void onError(Exception e) {
                if (e != null)
                    notifyClients(IUserClient.class, IUserClient.METHOD_ON_TASK_LIST_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<TaskBean> response) {
                if (response != null && response.isSuccess() && response.getData() != null) {
                    notifyClients(IUserClient.class, IUserClient.METHOD_ON_TASK_LIST, response.getData());
                } else {
                    notifyClients(IUserClient.class, IUserClient.METHOD_ON_TASK_LIST_FAIL, response != null ? response.getErrorMessage() : "数据异常");
                }
            }
        });
    }

    @Override
    public void requestUserInfo(long uid, OkHttpManager.MyCallBack callBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("queryUid", String.valueOf(uid));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        OkHttpManager.getInstance().getRequest(UriProvider.getUserInfo(), params, new OkHttpManager.MyCallBack<ServiceResult<UserInfo>>() {

            @Override
            public void onError(Exception e) {
                if (callBack == null) {
                    notifyClients(IUserClient.class, IUserClient.METHOD_REQUEST_USER_INFO_ERROR, e.getMessage());
                } else {
                    callBack.onError(e);
                }
            }

            @Override
            public void onResponse(ServiceResult<UserInfo> response) {
                if (response != null && response.isSuccess()) {
                    UserInfo data = response.getData();
                    saveCache(data.getUid(), data);
                    userDbCore.saveDetailUserInfo(data);
                    if (callBack == null) {
                        notifyClients(IUserClient.class, IUserClient.METHOD_REQUEST_USER_INFO, response);
                    } else {
                        callBack.onResponse(data);
                    }
                }
            }
        });
    }
}