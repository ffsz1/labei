package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.user.domain.UserLikeSoundDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description:
 * @Author: alwyn
 * @Date: 2018/11/26 18:29
 */
public interface UserLikeSoundDAO {

    @Insert("REPLACE INTO user_like_sound(uid, like_uid, create_date) VALUES (#{uid}, #{likeUid}, #{createDate})")
    int save(UserLikeSoundDO soundDO);

    @Select("SELECT like_uid FROM user_like_sound WHERE uid = #{uid}")
    List<Long> listLikeByUid(Long uid);
}
