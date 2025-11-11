package com.erban.main.mybatismapper;

import com.erban.main.model.ChargeAppleRecord;
import com.erban.main.model.ChargeAppleRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ChargeAppleRecordMapper {
    int countByExample(ChargeAppleRecordExample example);

    int deleteByExample(ChargeAppleRecordExample example);

    int deleteByPrimaryKey(Integer appleId);

    int insert(ChargeAppleRecord record);

    int insertSelective(ChargeAppleRecord record);

    List<ChargeAppleRecord> selectByExample(ChargeAppleRecordExample example);

    ChargeAppleRecord selectByPrimaryKey(Integer appleId);

    int updateByExampleSelective(@Param("record") ChargeAppleRecord record, @Param("example") ChargeAppleRecordExample example);

    int updateByExample(@Param("record") ChargeAppleRecord record, @Param("example") ChargeAppleRecordExample example);

    int updateByPrimaryKeySelective(ChargeAppleRecord record);

    int updateByPrimaryKey(ChargeAppleRecord record);
}
