package com.erban.admin.main.service;

import com.beust.jcommander.internal.Lists;
import com.erban.main.model.BillRecord;
import com.erban.main.model.Users;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.BillRecordVo;
import com.xchat.common.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class WithDrawCashService {

    @Autowired
    private UsersService usersService;

    @Autowired
    private BillRecordService billRecordService;

    public void updateWithDrawStatus(String billId) {
        BillRecord billRecord = billRecordService.selectBillRecordByBillId(billId);
        if (billRecord == null || billRecord.getBillStatus() == null || !billRecord.getBillStatus().equals(Constant.WithDraw.ing) || !billRecord.getObjType().equals(Constant.BillType.getCash)) {
            return;
        }
        billRecord.setBillStatus(Constant.WithDraw.finish);
        billRecordService.updateBillRecordByWithDraw(billRecord);
    }

    public List<BillRecordVo> getBillRecordList() {
        List<BillRecord> billRecordList = billRecordService.selectBillRecordList();
        List<BillRecordVo> billRecordVos = converToBillRecordVos(billRecordList);
        return billRecordVos;
    }

    private List<BillRecordVo> converToBillRecordVos(List<BillRecord> billRecordList) {
        List<BillRecordVo> billRecordVos = Lists.newArrayList();
        if (CollectionUtils.isEmpty(billRecordList)) {
            return billRecordVos;
        }
        for (BillRecord billRecord : billRecordList) {
            BillRecordVo billRecordVo = convertBillRecordToVo(billRecord);
            billRecordVos.add(billRecordVo);
        }
        return billRecordVos;
    }

    private BillRecordVo convertBillRecordToVo(BillRecord billRecord) {
        BillRecordVo billRecordVo = new BillRecordVo();
        billRecordVo.setBillId(billRecord.getBillId());
        billRecordVo.setMoney(billRecord.getMoney());
        billRecordVo.setDiamondNum(billRecord.getDiamondNum());
        billRecordVo.setCreateTime(billRecord.getCreateTime());
        Users users = usersService.getUsersByUid(billRecord.getUid());
        billRecordVo.setErbanNo(users.getErbanNo());
        billRecordVo.setNick(users.getNick());
        return billRecordVo;
    }
}
