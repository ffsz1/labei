package com.tongdaxing.erban.libcommon.net.statistic;

import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * <p>  统计接口日志 </p>
 *
 * @author jiahui
 * @date 2018/1/4
 */
public interface StatisticService {

    /**
     * 发送日志前往服务器
     *
     * @param params 日志参数
     * @return
     */
    @FormUrlEncoded
    @POST("basicusers/record")
    Single<ServiceResult<Object>> sendStatsticToService(@FieldMap Map<String, String> params);
}
