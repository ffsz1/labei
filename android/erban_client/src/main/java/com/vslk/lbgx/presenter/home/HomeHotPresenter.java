package com.vslk.lbgx.presenter.home;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.home.BannerInfo;
import com.tongdaxing.xchat_core.home.HomeInfo;
import com.tongdaxing.xchat_core.home.HomeRoom;
import com.tongdaxing.xchat_core.home.PeiPeiBean;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachParser;
import com.tongdaxing.xchat_core.im.custom.bean.PublicChatRoomAttachment;
import com.tongdaxing.xchat_framework.data.BaseConstants;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.im.IMProCallBack;
import com.tongdaxing.xchat_framework.im.IMReportBean;
import com.tongdaxing.xchat_framework.im.IMReportRoute;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.StringUtils;
import com.vslk.lbgx.model.find.FindSquareModel;
import com.vslk.lbgx.model.home.HomeModel;

import java.util.ArrayList;
import java.util.List;

public class HomeHotPresenter extends AbstractMvpPresenter<IHomeHotView> {
    private FindSquareModel findSquareModel;
    private HomeModel homeModel;


    public HomeHotPresenter() {
        findSquareModel = new FindSquareModel();
        homeModel = new HomeModel();
    }

    //陪陪
    public void getBestCompanies(int mPage, int gender) {
        homeModel.getBestCompanies(mPage, gender, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().getHomeHotRoomFail(e);
                }
            }

            @Override
            public void onResponse(Json response) {
                if (response == null || getMvpView() == null) {
                    return;
                }
                int code = JSON.parseObject(response.toString()).getIntValue("code");
                if (code == 200) {
                    getMvpView().getPeiPeiList(analysisPeiPei(response.toString(), true));
                } else {
                    String message = JSON.parseObject(response.toString()).getString("message");
                    onError(new Exception(message));
                    getMvpView().getPeiPeiList(new ArrayList<>());
                }
            }
        });
    }

    //萌新
    public void getNewUsers(int mPage) {
        homeModel.getNewUsers(mPage, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().getHomeHotRoomFail(e);
                }
            }

            @Override
            public void onResponse(Json response) {
                if (response == null || getMvpView() == null) {
                    return;
                }
                int code = JSON.parseObject(response.toString()).getIntValue("code");
                if (code == 200) {
                    getMvpView().getPeiPeiList(analysisPeiPei(response.toString(), false));
                } else {
                    String message = JSON.parseObject(response.toString()).getString("message");
                    onError(new Exception(message));
                    getMvpView().getPeiPeiList(new ArrayList<>());
                }
            }
        });
    }

    private static ArrayList<HomeRoom> analysisPeiPei(String str, boolean isPeiPei) {
        JSONArray arr = JSON.parseObject(str).getJSONArray("data");
        ArrayList<HomeRoom> list = new ArrayList<>();
        if (arr != null && arr.size() > 0) {
            for (int i = 0; i < arr.size(); i++) {
                HomeRoom homeRoom = new HomeRoom();
                homeRoom.setType(3);
                homeRoom.setItemType(4);
                JSONObject obj = arr.getJSONObject(i);
                PeiPeiBean item = new PeiPeiBean();
                item.setUid(obj.getIntValue("uid"));
                item.setNick(obj.getString("nick"));
                item.setAvatar(obj.getString("avatar"));
                item.setSignature(obj.getIntValue("signTime"));
                item.setGender(obj.getIntValue("gender"));
                item.setUserDescription(obj.getString("userDescription"));
                if (isPeiPei) {
                    item.setGlamour(obj.getIntValue("glamour"));
                    item.setVoiceDuration(obj.getIntValue("voiceDuration"));
                    item.setUserVoice(obj.getString("userVoice"));
                    item.setRoomState(obj.getInteger("roomState"));
                    homeRoom.setItemType(3);
                } else {
                    item.setFirstCharge(obj.getBoolean("isFirstCharge"));
                    item.setExperLevel(obj.getInteger("experLevel"));
                }
                homeRoom.setPeiPeiBean(item);
                list.add(homeRoom);
            }
        }
        return list;
    }

    /**
     * 获取公聊大厅房间id
     */
    public void getSquareRoomId() {
        findSquareModel.checkSquareRoomVersion(new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200) {
                    Json data = response.json("data");
                    if (data != null) {
                        if (getMvpView() != null) {
                            enterRoom(String.valueOf(BaseConstants.getRoomId(data.boo("audit"))));
                        }
                    }
                }
            }
        });
    }

    /**
     * 进入公聊房间
     */
    public void enterRoom(String roomId) {
        findSquareModel.enterPublicRoom(roomId, new IMProCallBack() {
            @Override
            public void onSuccessPro(IMReportBean imReportBean) {
                if (imReportBean != null && getMvpView() != null && imReportBean.getReportData().errno == 0) {
                    Json str = imReportBean.getReportData().data;
                    List<Json> his_list = str.jlist("his_list");
                    List<PublicChatRoomAttachment> list = new ArrayList<>();
                    for (int i = his_list.size() - 1; i >= 0; i--) {
                        ChatRoomMessage message = new ChatRoomMessage();
                        message.setRoute(IMReportRoute.sendPublicMsgNotice);
                        String custom = his_list.get(i).toString();
                        if (StringUtils.isEmpty(custom))
                            continue;
                        PublicChatRoomAttachment publicChatRoomAttachment = (PublicChatRoomAttachment) IMCustomAttachParser.parse(custom);
                        list.add(publicChatRoomAttachment);
                    }
                    getMvpView().getSquareData(list);
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
            }
        });
    }

    public void getTopBanner() {
        homeModel.getHomeRoomBanner(new OkHttpManager.MyCallBack<ServiceResult<List<BannerInfo>>>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().getHomeBannerFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<List<BannerInfo>> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().getHomeBannerSuccess(response.getData());
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().getHomeBannerFail(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().getHomeBannerFail("数据异常");
                    }
                }
            }
        });
    }

    public void getHomeHotRoomList(int mPage) {
        homeModel.getHomeHotRoomList(mPage, new OkHttpManager.MyCallBack<ServiceResult<HomeInfo>>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().getHomeHotRoomFail(e);
                }
            }

            @Override
            public void onResponse(ServiceResult<HomeInfo> response) {
                if (response == null || getMvpView() == null) {
                    return;
                }
                if (response.isSuccess()) {

                    getMvpView().getHomeHotRoomSuccess(response.getData());
                } else {
                    onError(new Exception(response.getMessage()));
                }
            }
        });
    }
}
