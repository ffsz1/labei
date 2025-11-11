package com.erban.main.mybatismapper;

import com.erban.main.model.BillRecord;
import com.erban.main.vo.BillRecordExpandVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BillRecordMapperExpand {


    Long getGiftSumByGift();

    Long getGiftTypeSumByRoom();

    //获取用户数（不重复）
    List<Long> getUserNum();

    public List<BillRecord> getRecordAllList(long uid, int pageNo, int pageSize, Byte type);

    public Integer getRecordCount(Long uid, Byte type);

    public Integer getRecordCountByDate(Long uid, Date timesnights, Date timesnights1, Byte type);

    public List<BillRecord> getRecordByDateList(Long uid, Date timesnights, Date timesnights1, Integer pageNo, Integer pageSize, Byte type);

    Integer getRecordCountByOrder(Long uid, Byte orderIncome, Byte orderPay);

    Integer getRecordCountByOrderDate(Long uid, Date startTime, Date timesnights, Byte orderPay, Byte orderIncome);

    List<BillRecord> getRecordByDateListByOrder(Long uid, Date startTime, Date timesnights, Integer pageNo, Integer pageSize, Byte orderIncome, Byte orderPay);

    List<BillRecord> getRecordAllListByOrder(Long uid, Integer pageNo, Integer pageSize, Byte orderIncome, Byte orderPay);

//    List<BillRecordExpandVo> getRecordByStatusAndDate(long uid, List<Integer> status, Date date, int skip, int size);
    List<BillRecordExpandVo> getRecordByStatusAndDate(Map<String, Object> param);

//    List<BillRecordExpandVo> getRecordByStatusAndDate2(long uid, List<Integer> status, Date date, int skip, int size);
    List<BillRecordExpandVo> getRecordByStatusAndDate2(Map<String, Object> param);

    List<BillRecord> sumDiamondWithDraw(List<Long> uids);
}
