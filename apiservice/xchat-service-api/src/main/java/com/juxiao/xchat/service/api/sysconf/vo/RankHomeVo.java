package com.juxiao.xchat.service.api.sysconf.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.juxiao.xchat.manager.common.event.vo.RankVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RankHomeVo {
    private List<RankVo> starList;
    private List<RankVo> nobleList;
    private List<RankVo> roomList;
}
