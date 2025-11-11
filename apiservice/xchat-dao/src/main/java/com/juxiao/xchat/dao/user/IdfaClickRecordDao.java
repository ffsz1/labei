package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.domain.IdfaClickRecordDO;
import org.apache.ibatis.annotations.Insert;


public interface IdfaClickRecordDao {

    @TargetDataSource
    @Insert("INSERT INTO `idfa_click_record`\n" +
            "            (`appid`,\n" +
            "             `idfa`,\n" +
            "             `idfamd5`,\n" +
            "             `clicktime`,\n" +
            "             `create_time`)\n" +
            "VALUES (\n" +
            "        #{appid},\n" +
            "        #{idfa},\n" +
            "       #{idfamd5},\n" +
            "        #{clicktime},\n" +
            "        NOW());")
    void insertIdfaClickRecord(
            IdfaClickRecordDO record
    );

}
