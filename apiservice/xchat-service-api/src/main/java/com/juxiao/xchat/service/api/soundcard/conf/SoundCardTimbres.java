package com.juxiao.xchat.service.api.soundcard.conf;

import java.util.List;
import java.util.Set;

/**
 * 声鉴卡音色配置
 *
 * @class: SoundCardTimbres.java
 * @author: chenjunsheng
 * @date 2018/5/25
 */
public class SoundCardTimbres {
    /**
     * 主音色
     */
    private String main;
    /**
     * 辅音色
     */
    private List<String> vice;
    /**
     * 辅音色为攻的音色
     */
    private Set<String> top;
    /**
     * 辅音色为受的音色
     */
    private Set<String> bottom;
    /**
     * 推荐伴侣
     */
    private List<String> partners;

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public List<String> getVice() {
        return vice;
    }

    public void setVice(List<String> vice) {
        this.vice = vice;
    }

    public Set<String> getTop() {
        return top;
    }

    public void setTop(Set<String> top) {
        this.top = top;
    }

    public Set<String> getBottom() {
        return bottom;
    }

    public void setBottom(Set<String> bottom) {
        this.bottom = bottom;
    }

    public List<String> getPartners() {
        return partners;
    }

    public void setPartners(List<String> partners) {
        this.partners = partners;
    }
}
