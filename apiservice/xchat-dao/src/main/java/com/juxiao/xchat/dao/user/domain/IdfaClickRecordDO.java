package com.juxiao.xchat.dao.user.domain;

import lombok.Data;

/**
 * @author chris
 * @date 2019-06-20
 */
@Data
public class IdfaClickRecordDO {

    private Long id;
    private String appid;
    private String idfa;
    private String idfamd5;
    private String clicktime;
}
