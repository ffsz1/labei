package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.output.bo.OutputValueParam;
import com.juxiao.xchat.dao.output.vo.OutputVO;
import com.juxiao.xchat.dao.output.vo.OutputValueVO;
import com.juxiao.xchat.dao.user.domain.UsersDO;
import com.juxiao.xchat.dao.user.dto.*;
import com.juxiao.xchat.dao.user.query.UserNewQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

/**
 * @class: UsersDao.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
public interface UsersDao {
    /**
     * 保存用户信息
     *
     * @param usersDo
     */
    @TargetDataSource
    void save(UsersDO usersDo);

    /**
     * 更新用户信息
     *
     * @param usersDo
     */
    @TargetDataSource
    void update(UsersDO usersDo);

    /**
     * 根据UID查询用户信息
     *
     * @param uid
     * @return
     * @author: chenjunsheng
     * @date 2018/6/7
     */
    @TargetDataSource
    UsersDTO getUser(@Param("uid") Long uid);

    /**
     * 根据用户绑定手机号码查询用户ID
     *
     * @param phone
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT uid FROM `users` WHERE `phone` = #{phone}")
    Long getUidByPhone(@Param("phone") String phone);

    /**
     * 根据用户绑定手机号码查询用户信息
     *
     * @param phone
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT * FROM `users` WHERE `phone` = #{phone}")
    List<UsersDO> getUserByPhone(@Param("phone") String phone);

    /**
     * 根据用户绑定手机号码查询用户ID
     *
     * @param erbanNo
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT uid FROM `users` WHERE `erban_no` = #{erbanNo}")
    Long getUidByErbanNo(@Param("erbanNo") Long erbanNo);

    /**
     * 查询最近一周注册得用户
     *
     * @param query
     * @return
     */
    List<UserNewDTO> listNewUsers(UserNewQuery query);

    /**
     * 产值报表
     *
     * @param outputValueParam
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<OutputValueVO> selectByParam(OutputValueParam outputValueParam);

    /**
     * 查询渠道下指定时间的注册用户信息
     *
     * @param registerDate
     * @param shareUidList
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<ChannelUserDTO> listByShareUid(@Param("registerDate") String registerDate,
                                        @Param("shareUidList") Collection<Long> shareUidList);

    /**
     * 统计充值金额
     *
     * @param param
     * @return
     */
    @TargetDataSource(name = "ds2")
    OutputVO countCharge(OutputValueParam param);

    @TargetDataSource(name = "ds2")
    @Select("SELECT " +
            " u.* " +
            "FROM " +
            " users u " +
            "LEFT JOIN user_setting us ON us.uid = u.uid " +
            "WHERE " +
            "   us.voice_hide = 0 " +
            "AND voice_dura > 0")
    List<UserSoundDTO> listUserBySound();

    @TargetDataSource(name = "ds2")
    @Select("select * from audit_user_speed_match")
    List<Long> listAuditSpeedMatchUser();

    @TargetDataSource(name = "ds2")
    List<UsersDTO> listUsersByPage(@Param("start") Long start);

    @TargetDataSource(name = "ds2")
    @Select("<script>"
            + "select u.uid,u.erban_no as erbanNo, u.nick, ifnull(u.user_desc, '') as userDescription, ifnull(u" +
            ".signture, '') as signature, ifnull(u.user_voice, '') as userVoice, ifnull(u.voice_dura, '') as " +
            "voiceDuration,  " +
            "u.gender, u.avatar, a.last_login_time as signTime, ifnull(o.glamour, 0) as glamour " +
            "from account a inner join users u on a.uid = u.uid " +
            "left join ( " +
            "  select recv_uid,  sum(sum_gold) glamour " +
            "  from one_day_room_recv_sum " +
            "  where update_time &gt; DATE_SUB(now(),INTERVAL 7 DAY) " +
            "  group by recv_uid " +
            ") o on o.recv_uid = u.uid " +
            "where def_user = 1 " +
            "<if test=\"glamour != null\">and ifnull(o.glamour, 0) &gt;=#{glamour} </if>" +
            "<if test=\"gender != null and gender !=0\">and u.gender=#{gender} </if>" +
            "order by a.last_login_time desc,o.glamour desc limit #{pageNum}, #{pageSize}"
            + "</script>")
    List<UserHotDTO> queryCharmCompanies(@Param("glamour") Integer glamour, @Param("gender") Integer gender, @Param(
            "pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    @TargetDataSource(name = "ds2")
    @Select("select u.uid,u.erban_no as erbanNo, u.nick, ifnull(u.user_desc, '') as userDescription, ifnull(u" +
            ".signture, '') as signature, u.gender, u.avatar, a.sign_time as signTime,p.is_first_charge as " +
            "isFirstCharge,a.last_login_time as lastLoginTime " +
            "from account a " +
            "inner join users u on a.uid = u.uid " +
            "inner join user_purse p on p.uid=a.uid " +
            "where a.sign_time > DATE_SUB(now(),INTERVAL 7 DAY) and def_user = 1 " +
            "order by a.last_login_time desc,a.sign_time desc limit #{pageNum}, #{pageSize}")
    List<UserHotDTO> queryNewUsers(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

}