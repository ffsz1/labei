package com.juxiao.xchat.dao.ad.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @date 2019-06-20
 */
@Data
public class AdKuaiShouRecordDO {

    private Integer id;
    private Byte isReport;
    private String aid;
    private String cid;
    private String did;
    private String dname;
    private String imei2;
    private String idfa2;
    private String mac;
    private String mac2;
    private String mac3;
    private String androidid2;
    private String idfa3;
    private String androidid3;
    private String imei3;
    private String ts;
    private String ip;
    private String callback;
    private Date createTime;
    private Date updateTime;
}
