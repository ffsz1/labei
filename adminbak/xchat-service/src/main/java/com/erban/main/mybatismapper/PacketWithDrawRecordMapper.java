package com.erban.main.mybatismapper;

import com.erban.main.model.PacketWithDrawRecord;
import com.erban.main.model.PacketWithDrawRecordExample;
import java.util.List;

import com.erban.main.param.admin.WithDrawParam;
import com.erban.main.vo.admin.PacketWithdrawRecordVo;
import org.apache.ibatis.annotations.Param;

public interface PacketWithDrawRecordMapper {
    int countByExample(PacketWithDrawRecordExample example);

    int deleteByExample(PacketWithDrawRecordExample example);

    int deleteByPrimaryKey(String recordId);

    int insert(PacketWithDrawRecord record);

    int insertSelective(PacketWithDrawRecord record);

    List<PacketWithDrawRecord> selectByExample(PacketWithDrawRecordExample example);

    PacketWithDrawRecord selectByPrimaryKey(String recordId);

    int updateByExampleSelective(@Param("record") PacketWithDrawRecord record, @Param("example") PacketWithDrawRecordExample example);

    int updateByExample(@Param("record") PacketWithDrawRecord record, @Param("example") PacketWithDrawRecordExample example);

    int updateByPrimaryKeySelective(PacketWithDrawRecord record);

    int updateByPrimaryKey(PacketWithDrawRecord record);


    List<PacketWithdrawRecordVo> selectByQuery(WithDrawParam withDrawParam);

    List<PacketWithdrawRecordVo> selectByIds(List<String> ids);


    PacketWithdrawRecordVo selectById(@Param("recordId") String recordId);

    PacketWithDrawRecord selectByMerchantOrderNo(@Param("merchantOrderNo")String merchantOrderNo);

    PacketWithdrawRecordVo selectByCount(WithDrawParam withDrawParam);
}
