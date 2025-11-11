package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.domain.UserAlbumReportRecordDO;
import com.juxiao.xchat.dao.user.domain.UserAvatarReportRecordDO;
import com.juxiao.xchat.dao.user.domain.UserReportRecordDO;
import org.apache.ibatis.annotations.Insert;

/**
 * 用户举报信息
 *
 * @class: UserReportRecordDao.java
 * @author: chenjunsheng
 * @date 2018/7/23
 */
public interface UserReportRecordDao {
    /**
     * 保存用户举报信息
     *
     * @param recordDo
     */
    @TargetDataSource
    @Insert("INSERT INTO user_report_record (`uid`, `report_uid`, `device_id`, `phone_no`, `ip`, `create_date`, " +
            "`report_type`, `type`, `remark`) VALUES (#{informantsId}, #{reportUid}, #{deviceId}, #{phoneNo}, #{ip}, " +
            "#{createDate}, #{reportType}, #{type}, #{remark})")
    void save(UserReportRecordDO recordDo);

    /**
     * 头像举报
     *
     * @param recordDo recordDo
     */
    @TargetDataSource
    @Insert("INSERT INTO user_avatar_report_record (`uid`, `report_uid`, `device_id`, `phone_no`, `ip`, " +
            "`create_date`, `report_type`, `type`,`url`) VALUES (#{uid}, #{reportUid}, #{deviceId}, #{phoneNo}, " +
            "#{ip}, #{createDate}, #{reportType}, #{type}, #{url})")
    void saveAvatar(UserAvatarReportRecordDO recordDo);

    /**
     * 相册举报
     *
     * @param recordDo recordDo
     */
    @TargetDataSource
    @Insert("INSERT INTO user_album_report_record (`uid`, `report_uid`, `device_id`, `phone_no`, `ip`, `create_date`," +
            " `report_type`, `type`,`url`) VALUES (#{uid}, #{reportUid}, #{deviceId}, #{phoneNo}, #{ip}, " +
            "#{createDate}, #{reportType}, #{type}, #{url})")
    void saveAlbum(UserAlbumReportRecordDO recordDo);
}
