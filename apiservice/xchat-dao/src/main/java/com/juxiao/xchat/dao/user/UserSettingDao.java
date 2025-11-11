package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.domain.UserSettingDO;
import com.juxiao.xchat.dao.user.dto.SoundMatchConfDTO;
import com.juxiao.xchat.dao.user.dto.UserSettingDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


public interface UserSettingDao {

    /**
     * @param settingDo
     */
    @TargetDataSource
    @Insert("<script>" +
            "INSERT INTO `user_setting`( " +
            "   `uid`, " +
            "   `create_time` " +
            "   <if test=\"likedSend != null\">, `liked_send`</if>" +
            "   <if test=\"chatPermission != null\">, `chat_permission` </if>" +
            ") VALUES( " +
            "   #{uid}, " +
            "   NOW() " +
            "   <if test=\"likedSend != null\">, #{likedSend}</if>" +
            "   <if test=\"chatPermission != null\">, #{chatPermission} </if>" +
            ") ON DUPLICATE KEY UPDATE " +
            "   <if test=\"likedSend != null\">`liked_send` = #{likedSend} </if>" +
            "   <if test=\"likedSend != null and chatPermission != null\">, </if>" +
            "   <if test=\"chatPermission != null\">`chat_permission` = #{chatPermission} </if>" +
            "</script>")
    void save(UserSettingDO settingDo);

    /**
     * 查询用户配置
     *
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT `uid`, `liked_send`, `chat_permission` FROM `user_setting` WHERE `uid` = #{uid}")
    UserSettingDTO getUserSetting(@Param("uid") Long uid);


    @TargetDataSource(name = "ds2")
    @Select("SELECT * FROM user_setting WHERE uid = #{uid}")
    SoundMatchConfDTO getSoundConfig(Long uid);

    @TargetDataSource
    @Insert("REPLACE INTO user_setting(uid, filter_gender, voice_hide) VALUES (#{uid}, #{filterGender}, #{voiceHide})")
    int saveSoundConfig(SoundMatchConfDTO confDTO);

}
