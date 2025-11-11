package com.juxiao.xchat.dao.event;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.event.dto.UsersCharmDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chris
 * @date 2019-07-10
 */
public interface UsersCharmDAO {

    /**
     * 获取魅力活动排行榜
     * @param list list
     * @return List
     */
    @TargetDataSource(name = "ds2")
    List<UsersCharmDTO> selectUsersCharmByPage(@Param("list") List<Long> list);

    /**
     * 获取魅力值用户
     * @return List
     */
    @TargetDataSource(name = "ds2")
    List<Long> selectByUid();
}
