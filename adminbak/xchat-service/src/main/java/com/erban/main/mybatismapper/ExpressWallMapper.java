package com.erban.main.mybatismapper;

import com.erban.main.model.ExpressWall;
import com.erban.main.vo.ExpressWallVo;

import java.util.List;

/**
*  @author author
*/
public interface ExpressWallMapper {

    int insert(ExpressWall object);

    /**
     * 更加ID查询记录
     * @param id ID
     * @return
     */
    ExpressWallVo getById(Long id);

    /**
     * 分页查询
     * @param beginNo 开始行号
     * @param pageSize 每页大小
     * @return
     */
    List<ExpressWallVo> findByPage(Integer beginNo, Integer pageSize);

    /**
     * 获取当天的置顶记录, 根据时间,和礼物金额排序
     * @param date 时间, 天
     * @return
     */
    List<ExpressWallVo> getByTop(String date);

}
