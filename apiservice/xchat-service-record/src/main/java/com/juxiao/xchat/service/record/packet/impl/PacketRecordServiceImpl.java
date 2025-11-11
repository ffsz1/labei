package com.juxiao.xchat.service.record.packet.impl;

import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.base.utils.GroupUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.UserPacketRecordDao;
import com.juxiao.xchat.dao.user.dto.PacketRecordDTO;
import com.juxiao.xchat.dao.user.query.PacketRecordQuery;
import com.juxiao.xchat.service.record.packet.PacketRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @class: PacketRecordServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/6/4
 */
@Service
public class PacketRecordServiceImpl implements PacketRecordService {
    private final Logger logger = LoggerFactory.getLogger(PacketRecordService.class);
    @Autowired
    private UserPacketRecordDao packetRecordDao;

    /**
     * @see com.juxiao.xchat.service.record.packet.PacketRecordService#listPacketRecord(long, long, int, int)
     */
    @Override
    public Map<String, List<Map<Long, List<PacketRecordDTO>>>> listPacketRecord(long uid, long date, int page, int pageSize) throws WebServiceException {
        Date qdate = DateTimeUtils.addDay(new Date(date), 1);
        List<PacketRecordDTO> list = packetRecordDao.listPacketRecord(new PacketRecordQuery(uid, qdate, page, pageSize));
        List<Map<Long, List<PacketRecordDTO>>> grouplist;
        if (list == null || list.size() == 0) {
            grouplist = new ArrayList<>();
        } else {
            try {
                grouplist = GroupUtils.groupByDate(list, "date");
            } catch (NoSuchMethodException e) {
                logger.error("[ 红包账单记录 ] 列表分组时找不到{}方法，异常：", "getDate()", e);
                throw new WebServiceException(WebServiceCode.SERVER_ERROR);
            } catch (Exception e) {
                logger.error("[ 红包账单记录 ] 列表分组时异常：", e);
                throw new WebServiceException(WebServiceCode.SERVER_ERROR);
            }
        }

        Map<String, List<Map<Long, List<PacketRecordDTO>>>> result = new HashMap<>();
        result.put("billList", grouplist);
        return result;
    }

    /**
     * @see com.juxiao.xchat.service.record.packet.PacketRecordService#listPacketDeposiRecord(long, long, int, int)
     */
    @Override
    public Map<String, List<Map<Long, List<PacketRecordDTO>>>> listPacketDeposiRecord(long uid, long date, int page, int pageSize) throws WebServiceException {
        Date qdate = DateTimeUtils.addDay(new Date(date), 1);
        page = (page - 1) * pageSize;
        List<PacketRecordDTO> list = packetRecordDao.listPacketDeposiRecord(new PacketRecordQuery(uid, qdate, page, pageSize));
        List<Map<Long, List<PacketRecordDTO>>> grouplist;
        if (list == null || list.size() == 0) {
            grouplist = new ArrayList<>();
        } else {
            try {
                grouplist = GroupUtils.groupByDate(list, "date");
            } catch (NoSuchMethodException e) {
                logger.error("[ 红包提现记录 ] 列表分组时找不到{}方法，异常：", "getDate()", e);
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            } catch (IllegalAccessException e) {
                logger.error("[ 红包提现记录 ] 列表分组时异常：", e);
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            } catch (InvocationTargetException e) {
                logger.error("[ 红包提现记录 ] 列表分组时异常：", e);
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
        }

        Map<String, List<Map<Long, List<PacketRecordDTO>>>> result = new HashMap<>();
        result.put("billList", grouplist);
        return result;
    }
}
