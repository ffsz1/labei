package com.juxiao.xchat.service.api.sysconf.conf;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.juxiao.xchat.dao.sysconf.dto.BannerDTO;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

/**
 * @Auther: alwyn
 * @Description: 审核的配置
 * @Date: 2018/10/15 20:02
 */
@Data
@Component
@ConfigurationProperties(prefix = "system.audit")
public class AuditConfig {

    /** 审核模式是否开启 */
    private boolean audit = true;
    /** 审核账号的最小等级 */
    private int auditLeftLevel = 10;
    /** 审核模式下的房间 */
    private Set<Long> auditRoomList = Sets.newHashSet();
    /** Banner列表 */
    private List<BannerDTO> banners = Lists.newArrayList();
    /** 审核白名单, 100723 是审核服的账号 */
    private List<Long> auditUidList = Lists.newArrayList(100723L, 1159414L, 1159934L, 1159936L);

    @PostConstruct
    public void init() {
        BannerDTO banner = new BannerDTO();
        banner = new BannerDTO();
        banner.setBannerId(2);
        banner.setBannerName("绿色公约");
        banner.setBannerPic("https://pic.chaoxuntech.com/green_convention.png?imageslim");
        banner.setSkipUri("https://v.xiumi.us/board/v5/3KgS3/133103002");
        banner.setSkipType(new Byte("3"));
        banner.setSeqNo(1);
        banner.setIsNewUser(new Byte("0"));
        banner.setViewType(1);
        banners.add(banner);

    }
}
