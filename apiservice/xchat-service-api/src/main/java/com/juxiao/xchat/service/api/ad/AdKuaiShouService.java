package com.juxiao.xchat.service.api.ad;

import com.juxiao.xchat.dao.ad.domain.AdKuaiShouRecordDO;
import com.juxiao.xchat.dao.ad.dto.AdKuaiShouRecordDTO;

/**
 * @author chris
 * @date 2019-06-20
 */
public interface AdKuaiShouService {

    /**
     * 接收快手广告数据
     * @param  adKuaiShouRecordDO adKuaiShouRecordDO
     * @return int
     */
    int reciveKuaiShouMsg(AdKuaiShouRecordDO adKuaiShouRecordDO);

    /**
     * 获取记录
     * @param imei imei
     * @param idfa idfa
     * @return AdKuaiShouRecordDTO
     */
    AdKuaiShouRecordDTO getKuaiShouRecord(String imei , String idfa);
}
