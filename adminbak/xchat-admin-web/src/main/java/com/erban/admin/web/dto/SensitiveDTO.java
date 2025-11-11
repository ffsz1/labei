package com.erban.admin.web.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * @author chris
 * @Title: 敏感词
 * @date 2018/11/15
 * @time 17:38
 */
public class SensitiveDTO {

    @Excel(name = "敏感词")
    private String sensitiveWord;

    public String getSensitiveWord() {
        return sensitiveWord;
    }

    public void setSensitiveWord(String sensitiveWord) {
        this.sensitiveWord = sensitiveWord;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
