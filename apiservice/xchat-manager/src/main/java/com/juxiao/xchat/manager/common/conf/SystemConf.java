package com.juxiao.xchat.manager.common.conf;

import com.juxiao.xchat.base.spring.support.YamlPropertyLoaderFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "common.system")
@PropertySource(value = "classpath:xchat.manager.yml", factory = YamlPropertyLoaderFactory.class)
public class SystemConf {
    /**
     * 正式环境
     */
    public final static String ENV_PROD = "prod";
    /**
     * 测试环境
     */
    public final static String ENV_TEST = "test";
    private String env;
    private String envName;
    private String secretaryUid;
    private String likeMsgUid;
    // 声优，收礼物方、订单服务者佣金
    private Double drawAkira;
    private Double appAkira;
    private Double roomOwnerAkira;
    private String defaultNick;
    private String defaultHead;

    /**
     * 大厅小秘书ID
     */
    private Long officialUid;
    /**
     * 大厅房间ID
     */
    private Long officialRoomId;
    /**
     * 大厅小秘书头像
     */
    private String officialAvatar;
    /**
     * 大厅小秘书昵称
     */
    private String officialNick;
    /**
     * 大厅小秘书文字颜色
     */
    private String txtColor;
    /**
     * 审核账号
     */
    private List<String> auditAccountList;

    private List<String> bundleIds;

    private List<String> iosChargeProds;
    /**
     * 相亲房tagId
     */
    private Integer xiangQinTagId;

    private String defaultPhoto;
    private String defaultHeadNan;
    private String defaultHeadNv;
    /**
     * 服务器域名或服务器IP，目前用于跳转到H5
     */
    private String serverDomain;
}
