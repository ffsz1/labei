package com.juxiao.xchat.service.api.sysconf.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.juxiao.xchat.dao.room.dto.RoomTagDTO;
import com.juxiao.xchat.dao.sysconf.dto.BannerDTO;
import com.juxiao.xchat.dao.sysconf.dto.IconDTO;
import com.juxiao.xchat.manager.common.room.vo.RoomVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class    HomeV2Vo {
    private List<BannerDTO> banners; // Banner列表
    private List<IconDTO> homeIcons;  // 热门下的房间列表
    private RankHomeVo rankHome;    // 排行榜列表
    private List<RoomVo> hotRooms;  // 首页推荐列表
    private List<RoomVo> listRoom;  // 热门房间列表
    private List<RoomVo> listGreenRoom;  // 绿色房间列表
    private List<RoomVo> recommendRooms; // 推荐房间
    private List<RoomTagDTO> roomTagList;
    /** 首页页面布局, 默认是0, 审核的页面布局会不一样 */
    private Integer viewType = 0;
    private List<RoomVo> agreeRecommendRooms; //购买,后台设置,活人达到推荐房间
}
