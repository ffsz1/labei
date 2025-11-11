package com.juxiao.xchat.service.api.event.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.juxiao.xchat.manager.common.event.vo.RankVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RankParentVo {

    private List<RankVo> rankVoList;
    private RankMineVo me;

    public RankParentVo setRankVoList(List<RankVo> rankVoList) {
        this.rankVoList = rankVoList;
        return this;
    }
}
