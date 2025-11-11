package com.tongdaxing.xchat_core.room.bean;

/**
 * Function:
 * Author: Edward on 2019/6/20
 */
public class ProbabilityInfo {

    /**
     * name : 低
     * probability : 3
     */

    private String name;
    private int probability;
    private boolean selected = false;//本地字段，用来标示选中状态

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }

    @Override
    public String toString() {
        return "ProbabilityInfo{" +
                "name='" + name + '\'' +
                ", probability=" + probability +
                ", selected=" + selected +
                '}';
    }
}


