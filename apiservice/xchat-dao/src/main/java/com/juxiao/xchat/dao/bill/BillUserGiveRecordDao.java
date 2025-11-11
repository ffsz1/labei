package com.juxiao.xchat.dao.bill;

import com.juxiao.xchat.dao.bill.dto.BillUserGiveGoldDTO;
import com.juxiao.xchat.dao.bill.dto.BillUserTransferDTO;
import com.juxiao.xchat.dao.bill.query.BillUserQuery;
import com.juxiao.xchat.dao.charge.domain.UserGiveRecordDo;
import com.juxiao.xchat.dao.config.TargetDataSource;

import java.util.List;

public interface BillUserGiveRecordDao {
    /**
     * 保存赠送记录
     *
     * @param recordDo
     * @date 2020/7/22
     */
    @TargetDataSource
    void insert(UserGiveRecordDo recordDo);

    List<BillUserGiveGoldDTO> listUserGiveGold(BillUserQuery query);
}
