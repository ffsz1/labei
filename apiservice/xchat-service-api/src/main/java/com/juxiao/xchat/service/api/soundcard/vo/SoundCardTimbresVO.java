package com.juxiao.xchat.service.api.soundcard.vo;

/**
 * 声卡音色
 *
 * @class: SoundCardTimbresVO.java
 * @author: chenjunsheng
 * @date 2018/5/24
 */
public class SoundCardTimbresVO {
    /**
     * 音色名称
     */
    private String name;
    /**
     * 音色百分比
     */
    private Integer percent;

    public SoundCardTimbresVO(String name, Integer percent) {
        this.name = name;
        this.percent = percent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }
}
