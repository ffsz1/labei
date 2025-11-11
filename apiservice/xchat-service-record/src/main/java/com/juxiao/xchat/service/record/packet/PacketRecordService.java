package com.juxiao.xchat.service.record.packet;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.dto.PacketRecordDTO;

import java.util.List;
import java.util.Map;

/**
 * 查询红包记录
 *
 * @class: PacketRecordService.java
 * @author: chenjunsheng
 * @date 2018/6/4
 */
public interface PacketRecordService {

    /**
     * 查询用户红包记录
     *
     * @param uid
     * @param date
     * @param page
     * @param pageSize
     * @return
     * @throws WebServiceException
     * @author: chenjunsheng
     * @date 2018/6/4
     */
    Map<String, List<Map<Long, List<PacketRecordDTO>>>> listPacketRecord(long uid, long date, int page, int pageSize) throws WebServiceException;

    /**
     * 查询用户提现红包的记录
     *
     * @param uid
     * @param date
     * @param page
     * @param pageSize
     * @return
     * @throws WebServiceException
     * @author: chenjunsheng
     * @date 2018/6/5
     */
    Map<String, List<Map<Long, List<PacketRecordDTO>>>> listPacketDeposiRecord(long uid, long date, int page, int pageSize) throws WebServiceException;
}
