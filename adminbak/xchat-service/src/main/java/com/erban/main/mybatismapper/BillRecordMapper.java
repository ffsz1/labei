package com.erban.main.mybatismapper;

import com.erban.main.model.BillRecord;
import com.erban.main.model.BillRecordExample;
import com.erban.main.param.admin.WithDrawParam;
import com.erban.main.vo.admin.BillTransferDTO;
import com.erban.main.vo.admin.DiamondWithDrawVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BillRecordMapper {
    int deleteByPrimaryKey(String billId);

    int insert(BillRecord record);

    int insertSelective(BillRecord record);

    List<BillRecord> selectByExample(BillRecordExample example);

    BillRecord selectByPrimaryKey(String billId);

    int updateByPrimaryKeySelective(BillRecord record);

    int updateByPrimaryKey(BillRecord record);


    List<DiamondWithDrawVo> selectByQuery(WithDrawParam withDrawParam);

    List<BillTransferDTO> selectBillTransferByIds(List<String> ids);

    List<BillTransferDTO> statByBillTransferQuery(WithDrawParam withDrawParam);

    List<BillTransferDTO> selectBillTransferByQuery(WithDrawParam withDrawParam);

    BillTransferDTO selectBillTransferByBillId(@Param("billId") String billId);
}
