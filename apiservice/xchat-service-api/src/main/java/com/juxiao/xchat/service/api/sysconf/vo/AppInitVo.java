package com.juxiao.xchat.service.api.sysconf.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.juxiao.xchat.dao.sysconf.dto.FaceJsonDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * APP初始化拉取的数据
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppInitVo {
    // 表情配置
    private FaceJsonDTO faceJson;
    // 闪屏配置
    private SplashVo splashVo;
    // 贵族资源ZIP包
    // private NobleZip nobleZip;
    // 贵族特权
    // private List<NobleRight> rights;
}
