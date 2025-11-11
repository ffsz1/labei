package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.user.domain.AccountLoginRecordDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AccountLoginRecordDao {

    @Insert("insert into account_login_record (" +
            "       uid, erban_no, phone, " +
            "      login_type, login_ip, weixin_openid, " +
            "      qq_openid, os, osVersion, " +
            "      app, imei, isp_type, " +
            "      net_type, model, device_id, " +
            "      app_version, create_time" +
            ") values (" +
            "      #{uid,jdbcType=BIGINT}, #{erbanNo,jdbcType=BIGINT}, #{phone,jdbcType=VARCHAR}, " +
            "      #{loginType,jdbcType=TINYINT}, #{loginIp,jdbcType=VARCHAR}, #{weixinOpenid,jdbcType=VARCHAR}, " +
            "      #{qqOpenid,jdbcType=VARCHAR}, #{os,jdbcType=VARCHAR}, #{osversion,jdbcType=VARCHAR}, " +
            "      #{app,jdbcType=VARCHAR}, #{imei,jdbcType=VARCHAR}, #{ispType,jdbcType=VARCHAR}, " +
            "      #{netType,jdbcType=VARCHAR}, #{model,jdbcType=VARCHAR}, #{deviceId,jdbcType=VARCHAR}, " +
            "      #{appVersion,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP})")
    @Options(useGeneratedKeys = true, keyProperty = "recordId")
    int insert(AccountLoginRecordDO recordDO);

    @Select("SELECT count(uid) from account_login_record WHERE uid = #{uid} ")
    int countUserLogin(@Param("uid") Long uid);


    /**
     * 分页查询活跃用户
     * @param date
     * @param startNo
     * @param limit
     * @return
     */
    @Select("SELECT DISTINCT uid from account_login_record WHERE create_time > #{date} limit #{startNo}, #{limit}")
    List<Long> pageUid(@Param("date") String date, @Param("startNo") Integer startNo, @Param("limit") Integer limit);
}
