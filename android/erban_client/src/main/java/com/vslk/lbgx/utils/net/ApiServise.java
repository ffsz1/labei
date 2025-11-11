package com.vslk.lbgx.utils.net;

import com.tongdaxing.xchat_core.auth.AccountInfo;
import com.tongdaxing.xchat_core.auth.TicketInfo;
import com.tongdaxing.xchat_core.find.FindInfo;
import com.tongdaxing.xchat_core.home.HomeInfo;
import com.tongdaxing.xchat_core.redpacket.bean.RedDrawListInfo;
import com.tongdaxing.xchat_core.redpacket.bean.RedPacketInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by huangmeng1 on 2018/1/3.
 */

public interface ApiServise {

    @GET("home/v2/hotindex")
    Observable<ServiceResult<HomeInfo>> getHomeInfo(@Query("pageNum") int pageNum, @Query("pageSize") int pagesize, @Query("uid") String uid);

    @GET("room/get")
    Observable<ServiceResult<RoomInfo>> getRoomInfo(@Query("uid") String uid);

    @POST("room/close")
    Observable<ServiceResult<String>> closeRoom(@Query("uid") String uid, @Query("ticket") String ticket);

    @GET("statpacket/get")
    Observable<ServiceResult<RedPacketInfo>> getRedPacketInfo(@Query("uid") String uid);

    @GET("redpacket/drawlist")
    Observable<ServiceResult<List<RedDrawListInfo>>> getRedDrawList();

    @POST("room/open")
    Observable<ServiceResult<RoomInfo>> openRoom(@Query("uid") String uid, @Query("type") String type,
                                                 @Query("title") String title, @Query("roomDesc") String roomDesc,
                                                 @Query("backPic") String backPic, @Query("rewardId") String rewardId);

    @POST("oauth/token")
    Observable<ServiceResult<AccountInfo>> login(@Query("phone") String phone, @Query("version") String version, @Query("client_id") String client_id
            , @Query("username") String username, @Query("password") String password
            , @Query("grant_type") String grant_type, @Query("client_secret") String client_secret);

    @POST("oauth/ticket")
    Observable<ServiceResult<TicketInfo>> getTicket(@Query("issue_type") String issue_type, @Query("access_token") String access_token);

    @GET("advertise/getList")
    Observable<ServiceResult<List<FindInfo>>> getFindInfo();

    @FormUrlEncoded
    @POST
    Observable<ResponseBody> post(@Url String url, @FieldMap HashMap<String,String> params);

    @FormUrlEncoded
    @POST
    Observable<ResponseBody> postAddHeader(@Url String url,@HeaderMap HashMap<String, String> headers, @FieldMap HashMap<String,String> params);

    @GET
    Observable<ResponseBody> get(@Url String url,@QueryMap HashMap<String,String> params);

    @GET
    Observable<ResponseBody> getAddHeader(@Url String url, @HeaderMap HashMap<String, String> headers, @QueryMap HashMap<String,String> params);
}
