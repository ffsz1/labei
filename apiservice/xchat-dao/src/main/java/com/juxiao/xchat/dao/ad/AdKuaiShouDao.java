package com.juxiao.xchat.dao.ad;

import com.juxiao.xchat.dao.ad.dto.AdKuaiShouRecordDTO;
import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.*;

/**
 * @author chris
 * @date 2019-06-20
 */
public interface AdKuaiShouDao {

    /**
     * 根据imei查询android信息
     *
     * @param imei2
     * @returnAdKuaiShouRecordDTO
     */
    @TargetDataSource(name = "ds2")
    @Select("select * from ad_kuaishou_record where imei2 = #{imei2}")
    AdKuaiShouRecordDTO getKuaiShouRecordByIMEI(@Param("imei2") String imei2);

    /**
     * 根据idfa查询ios信息
     *
     * @param idfa2
     * @return AdKuaiShouRecordDTO
     */
    @TargetDataSource(name = "ds2")
    @Select("select * from ad_kuaishou_record where idfa2 = #{idfa2}")
    AdKuaiShouRecordDTO getKuaiShouRecordByIDFA(@Param("idfa2") String idfa2);

    /**
     * 新增快手广告信息
     *
     * @param adKuaiShouRecordDTO
     */
    @TargetDataSource
    @Insert("INSERT INTO `ad_kuaishou_record` (`aid`,`cid`,`did`,`dname`,`imei2`,`idfa2`,`mac`,`mac2`,`mac3`,`androidid2`,`idfa3`,`androidid3`,`imei3`,`ts`,`ip`,`callback`) " +
            "VALUES (#{aid},#{cid},#{did},#{dname},#{imei2},#{idfa2},#{mac},#{mac2},#{mac3},#{androidid2},#{idfa3},#{androidid3},#{imei3},#{ts},#{ip},#{callback})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void save(AdKuaiShouRecordDTO adKuaiShouRecordDTO);


    /**
     * 更新android上报信息
     *
     * @param imei2
     * @return
     */
    @TargetDataSource
    @Update("update ad_kuaishou_record set is_report = 1,update_time=now() where imei2=#{imei2}")
    void updateByIMEI(@Param("imei2") String imei2);

    /**
     * 更新ios上报信息
     *
     * @param idfa2
     * @return
     */
    @TargetDataSource
    @Update("update ad_kuaishou_record set is_report = 1,update_time=now() where idfa2=#{idfa2}")
    void updateByIDFA(@Param("idfa2") String idfa2);
}
