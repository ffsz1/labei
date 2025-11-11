package com.juxiao.xchat.dao.sysconf;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.sysconf.dto.AppActivityWinDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 查询活动
 *
 * @class: AppActivityDao.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
public interface AppActivityDao {

    /**
     * 查询线上正在生效的活动
     *
     * @param actStatus
     * @param alertWinLoc
     * @return
     * @author: chenjunsheng
     * @date 2018/6/7
     */
    @TargetDataSource(name = "ds2")
    List<AppActivityWinDTO> listWinActivity(@Param("actStatus") Integer actStatus, @Param("alertWinLoc") Integer alertWinLoc);

    /**
     * 查询线上正在生效的活动
     *
     * @param actStatus
     * @return
     * @author: chenjunsheng
     * @date 2018/6/7
     */
    @TargetDataSource(name = "ds2")
    List<AppActivityWinDTO> listAllActivity(@Param("actStatus") Integer actStatus);
}
