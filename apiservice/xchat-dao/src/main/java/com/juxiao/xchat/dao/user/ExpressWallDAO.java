package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.user.domain.ExpressWallDO;
import com.juxiao.xchat.dao.user.dto.ExpressWallDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
*  @author author
*/
public interface ExpressWallDAO {

    /**
     * 添加记录
     * @param object
     * @return
     */
    @Insert("INSERT INTO express_wall ( " +
            "    send_uid, " +
            "    receive_uid, " +
            "    gift_id, " +
            "    create_time, " +
            "    message, " +
            "    total_gold, " +
            "    gift_num " +
            ") VALUES ( " +
            "    #{sendUid}, " +
            "    #{receiveUid}, " +
            "    #{giftId}, " +
            "    #{createTime}, " +
            "    #{message}, " +
            "    #{totalGold}, " +
            "    #{giftNum} " +
            ");")
    int insert(ExpressWallDO object);

    /**
     * 更加ID查询记录
     * @param id ID
     * @return
     */
    @Select("SELECT " +
            "    ew.id as id, " +
            "    su.nick as sendNick, " +
            "    su.uid as sendUid, " +
            "    su.avatar as sendAvatar, " +
            "    ru.nick as receiveNick, " +
            "    ru.uid as receiveUid, " +
            "    ru.avatar as receiveAvatar, " +
            "    g.pic_url as picUrl, " +
            "    g.gift_name as giftName, " +
            "    ew.total_gold as giftGold, " +
            "    ew.create_time as create_time, " +
            "    ew.message as expressMessage, " +
            "    ew.gift_num as giftNum " +
            "FROM " +
            "   express_wall ew " +
            "LEFT JOIN " +
            "   users su ON su.uid = ew.send_uid " +
            "LEFT JOIN " +
            "   users ru ON ru.uid = ew.receive_uid " +
            "LEFT JOIN " +
            "   gift g ON g.gift_id = ew.gift_id " +
            "WHERE " +
            "   id = #{id}")
    ExpressWallDTO getById(Long id);

    /**
     * 分页查询
     * @param beginNo 开始行号
     * @param pageSize 每页大小
     * @return
     */
    List<ExpressWallDTO> findByPage(Integer beginNo, Integer pageSize);

    /**
     * 获取当天的置顶记录, 根据时间,和礼物金额排序
     * @param date 时间, 天
     * @return
     */
    List<ExpressWallDTO> getByTop(String date);

}