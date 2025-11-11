package com.juxiao.xchat.manager.common.user;

import com.juxiao.xchat.dao.user.dto.UserExtendDTO;
import com.juxiao.xchat.dao.user.dto.UserSettingDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.common.user.vo.UserSimpleVO;

import java.util.List;

/**
 * 用户信息接口类
 *
 * @class: UsersManager.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
public interface UsersManager {
    /**
     * 更新用户关注数量
     *
     * @param uid
     */
    void updateFollowNum(Long uid);

    /**
     * 更新被喜欢人的粉丝数量
     *
     * @param uid
     */
    void updateFansNum(Long uid);

    /**
     * 获取用户信息
     *
     * @param uid
     * @return
     * @author: chenjunsheng
     * @date 2018/6/7
     */
    UsersDTO getUser(Long uid);

    /**
     * 根据电话号码获取用户信息
     *
     * @param phone
     * @return
     */
    UsersDTO getUserByPhone(String phone);

    /**
     * 根据电话号码获取多个用户信息
     *
     * @param phone
     * @return
     */
    List<UsersDTO> getUserByUserPhone(String phone);

    /**
     * 根据官方号获取用户信息
     *
     * @param erbanNo
     * @return
     */
    UsersDTO getUserByErbanNo(Long erbanNo);

    /**
     * 获取用户个性化设置
     *
     * @param uid
     * @return
     */
    UserSettingDTO getUserSetting(Long uid);

    /***
     * 增加用户的活跃度
     * @param uid
     * @param incNum
     */
    void increaseUserLiveness(Long uid, Integer incNum);

    /**
     * 获取用户的拓展信息
     *
     * @param uid
     * @return
     */
    UserExtendDTO getUserExtend(Long uid);

    /**
     * 获取审核版的速配用户
     *
     * @return
     */
    List<UserSimpleVO> getAuditSpeedMatchUsers();

    /**
     * 获取魅力速配用户
     *
     * @return
     */
    List<UserSimpleVO> getCharmSpeedMatchUsers();

    /**
     * 获取魅力速配用户
     *
     * @return
     */
    List<UserSimpleVO> getCharmSpeedMatchUsersV2(Integer gender, Integer pageNum);

    /**
     * 根据最近登录时间获取总数
     *
     * @return
     */
    Integer getCountByLoginTime(Integer gender);

    /**
     * 获取最新魅力变化的用户id列表
     *
     * @param size
     * @return
     */
    List<Long> getCharmLastChangeUids(Integer size, Long uid);
}
