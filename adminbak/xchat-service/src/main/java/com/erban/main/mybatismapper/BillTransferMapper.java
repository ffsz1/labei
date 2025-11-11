package com.erban.main.mybatismapper;

import com.erban.main.model.BillTransfer;
import com.erban.main.model.BillTransferExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BillTransferMapper {
    int countByExample(BillTransferExample example);

    int deleteByExample(BillTransferExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BillTransfer record);

    int insertSelective(BillTransfer record);

    List<BillTransfer> selectByExample(BillTransferExample example);

    BillTransfer selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BillTransfer record, @Param("example") BillTransferExample example);

    int updateByExample(@Param("record") BillTransfer record, @Param("example") BillTransferExample example);

    int updateByPrimaryKeySelective(BillTransfer record);

    int updateByPrimaryKey(BillTransfer record);

    BillTransfer selectByBillId(@Param("billId") String billId);
}
