package com.erban.main.vo;

/**
 * @author chris
 * @Title:
 * @date 2018/11/12
 * @time 14:23
 */
public class RoomFlowVo {

    private String name;
    private String type = "line";
    private String stack = "总量";
    private Long[] data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long[] getData() {
        return data;
    }

    public void setData(Long[] data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }
}
