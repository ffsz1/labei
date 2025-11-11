package com.xchat.oauth2.service.domain.user;

import com.xchat.oauth2.service.domain.shared.Repository;
import org.apache.ibatis.annotations.Param;

/**
 * @author liuguofu
 */

public interface UserRepository extends Repository {

    User findByUid(Long uid);

    void saveUser(User user);

    void updateUser(User user);

    User findByUsername(String username);

    void updatePasswordByUid(@Param("uid")Long uid,@Param("password")String password);

    void updatePasswordByUsername(@Param("username")String username,@Param("password")String password);

    User findByQQOpenId(String openId);

    User findByWeiXinOpenId(String openId);

    User findByWeiBoOpenId(String openId);

    void updateQQBindInfoByUid(@Param("uid")long uid,@Param("openId")String openId,@Param("qqNick")String qqNick);

    void updateWeiboBindInfoByUid(@Param("uid")long uid,@Param("openId")String openId,@Param("weiboNick")String weiboNick);

    void updateWeixinBindInfoByUid(@Param("uid")long uid,@Param("openId")String openId,@Param("weixinNick")String weixinNick);

    void updatePhoneBindInfoByUid(@Param("uid")long uid,@Param("phone")String phone,@Param("password")String password);

    void updateUserRoomPlayList(@Param("uid")Long uid,@Param("roomId")Long roomId,@Param("playListId")Long playListId);

}
