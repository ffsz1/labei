package com.xchat.oauth2.service.infrastructure.myaccountmybatis;

import com.xchat.oauth2.service.model.AccountLoginRecord;
import com.xchat.oauth2.service.model.AccountLoginRecordExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface AccountLoginRecordMapper {
    int countByExample(AccountLoginRecordExample example);

    int deleteByExample(AccountLoginRecordExample example);

    int deleteByPrimaryKey(Long recordId);

    int insert(AccountLoginRecord record);

    int insertSelective(AccountLoginRecord record);

    List<AccountLoginRecord> selectByExample(AccountLoginRecordExample example);

    AccountLoginRecord selectByPrimaryKey(Long recordId);

    int updateByExampleSelective(@Param("record") AccountLoginRecord record, @Param("example") AccountLoginRecordExample example);

    int updateByExample(@Param("record") AccountLoginRecord record, @Param("example") AccountLoginRecordExample example);

    int updateByPrimaryKeySelective(AccountLoginRecord record);

    int updateByPrimaryKey(AccountLoginRecord record);

    List<AccountLoginRecord> getLatestLogin(List<Long> uids);

    @Select("SELECT DISTINCT uid from account_login_record WHERE create_time > #{date} ")
    List<Long> listUid(String date);

    /**
     * 分页查询活跃用户
     * @param date
     * @param startNo
     * @param limit
     * @return
     */
    @Select("SELECT DISTINCT uid from account_login_record WHERE create_time > #{date} limit #{startNo}, #{limit}")
    List<Long> pageUid(@Param("date") String date, @Param("startNo") Integer startNo, @Param("limit") Integer limit);

    @Select("SELECT count(uid) from account_login_record WHERE uid = #{uid} ")
    int countUserLogin(@Param("uid") Long uid);


}
