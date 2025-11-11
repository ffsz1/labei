package com.juxiao.xchat.service.api.soundcard.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 声卡鉴定结果
 *
 * @class: SoundCardResultVO.java
 * @author: chenjunsheng
 * @date 2018/5/24
 */
@Getter
@Setter
public class SoundCardResultVO {
    /**
     * 主音色
     */
    private SoundCardTimbresVO main;
    /**
     * 辅助音色
     */
    private List<SoundCardTimbresVO> vices;
    /**
     * 最佳伴侣
     */
    private String bestPartner;
    /**
     * 心动值
     */
    private Double fascinates;
    /**
     * 攻/受
     */
    private Boolean top;
    /**
     * 扑倒值
     */
    private Double pounceOn;
    /**
     * 音色评价
     */
    private Integer starScore;

    public SoundCardResultVO() {
        vices = new ArrayList<>();
    }

    public void addVice(SoundCardTimbresVO timbres) {
        vices.add(timbres);
    }

    public void setVices(List<SoundCardTimbresVO> vices) {
        if (this.vices == null) {
            this.vices = vices;
        } else {
            this.vices.addAll(vices);
        }
    }

    public String getTopStr() {
        return top ? "攻" : "受";
    }
}
