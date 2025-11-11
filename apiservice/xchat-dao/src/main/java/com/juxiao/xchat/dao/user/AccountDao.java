package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.domain.AccountDO;
import com.juxiao.xchat.dao.user.dto.AccountDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @class: AccountDao.java
 * @author: chenjunsheng
 * @date 2018/6/11
 */
public interface AccountDao {

    /**
     * 更新用户账号信息
     *
     * @param accountDo
     */
    @TargetDataSource
    int update(AccountDO accountDo);

    /**
     * 根据用户绑定手机号码统计用户
     *
     * @param phone
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT COUNT(*) FROM `account` WHERE `phone`=#{phone}")
    int countAccountPhone(@Param("phone") String phone);

    /**
     * 根据用户ID获取账号
     *
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    AccountDTO getAccount(@Param("uid") Long uid);

    /**
     * @param netEaseToken
     * @return
     */
    @TargetDataSource(name = "ds2")
    AccountDTO getByNetEaseToken(@Param("netEaseToken") String netEaseToken);

    /**
     * 设置密码
     *
     * @param uid
     * @param password
     * @return
     */
    @TargetDataSource
    int updateByPassword(@Param("uid") Long uid, @Param("password") String password);

    /**
     * 设置二级密码
     *
     * @param uid
     * @param passwordSecond
     * @return
     */
    @TargetDataSource
    int updateBySecondPassword(@Param("uid") Long uid, @Param("passwordSecond") String passwordSecond);

    @TargetDataSource(name = "ds2")
    @Select("SELECT uid FROM account WHERE erban_no = #{erbanNo}")
    List<AccountDTO> selectByErbanNo(@Param("erbanNo") Long erbanNo);

    @TargetDataSource
    void insert(AccountDTO accountDo);


    @TargetDataSource(name = "ds2")
    @Select("SELECT COUNT(1) FROM `account` WHERE `weixin_openid`=#{openId}")
    int countWx(@Param("openId") String openId);

    @TargetDataSource(name = "ds2")
    @Select("SELECT COUNT(1) FROM `account` WHERE `qq_openid`=#{openId}")
    int countQQ(@Param("openId") String openId);


    @TargetDataSource(name = "ds2")
    @Select("SELECT uid,weixin_openid as weixinOpenid,weixin_unionid as weixinUnionid FROM `account` WHERE `weixin_unionid`=#{unionId} limit 1")
    AccountDTO getByWeixinUnionId(@Param("unionId") String unionId);

    @TargetDataSource
    void updateBySelective(AccountDO accountDo);

    /**
     * 获取最新变动的魅力数据
     *
     * @param offset 偏移量
     * @param rows   行数
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("select u.uid, u.nick, u.gender, u.avatar, ifnull(u.user_voice, '') as userVoice, ifnull(u.voice_dura, 0) as voiceDura, ifnull(u.user_desc, '') userDescription, ifnull(u.signture, '') signture from account a inner join users u on u.uid = a.uid where u.gender = #{gender} and def_user = 1 and a.is_shuijun is null order by a.last_login_time desc limit #{offset}, #{rows}")
    List<UsersDTO> queryCharmUser(@Param("gender") Integer gender, @Param("offset") Integer offset, @Param("rows") Integer rows);

    /**
     * 获取最近登录的总数
     *
     * @param gender 性别
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("select count(a.uid) from account a inner join users u on u.uid = a.uid where a.last_login_time > DATE_SUB(now(),INTERVAL 7 DAY) and u.gender = #{gender} and def_user = 1;")
    Integer queryTotalCount(@Param("gender") Integer gender);

    /**
     * 获取第三方绑定的昵称
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    Map<String, String> getBindNickMap(Long uid);
}