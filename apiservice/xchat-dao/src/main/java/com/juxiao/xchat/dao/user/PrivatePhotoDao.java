package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.domain.PrivatePhotoDO;
import com.juxiao.xchat.dao.user.dto.PrivatePhotoDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @class: PrivatePhotoDao.java
 * @author: chenjunsheng
 * @date 2018/6/15
 */
public interface PrivatePhotoDao {

    /**
     * @param photoDo
     */
    @TargetDataSource
    void save(PrivatePhotoDO photoDo);

    /**
     * 删除图片
     *
     * @param pid
     */
    @TargetDataSource
    @Delete("DELETE FROM private_photo WHERE pid = #{pid,jdbcType=BIGINT}")
    void delete(@Param("pid") Long pid);

    /**
     * 删除图片
     *
     * @param uid
     */
    @TargetDataSource
    @Select("SELECT COUNT(*) FROM `private_photo` WHERE uid = #{uid}")
    int countUserPhoto(@Param("uid") Long uid);

    /**
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT pid AS pid,photo_url AS photoUrl,seq_no AS seqNo,create_time AS createTime,photo_status AS photoStatus FROM private_photo WHERE uid = #{uid} and photo_status = #{photoStatus} ORDER BY seq_no")
    List<PrivatePhotoDTO> listUserPrivatePhoto(@Param("uid") Long uid, @Param("photoStatus") Integer photoStatus);

}