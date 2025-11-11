package com.juxiao.xchat.manager.external.netease.ret;

import com.juxiao.xchat.manager.external.netease.vo.MicUserVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class QueueUser {

    private List<MicUserVo> list;
}
