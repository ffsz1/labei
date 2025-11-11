package com.juxiao.xchat.manager.external.netease.vo;

import lombok.Data;

import java.util.List;

/**
 * @author chris
 * @date 2019-07-14
 */
@Data
public class CheckLabelsVO {

    /**
     * 分类信息，100：色情，200：广告，400：违禁，500：涉政，600：谩骂，700：灌水
     */
    private Integer label;

    /**
     * 分类级别，1：不确定，2：确定
     */
    private Integer level;

    /**
     * 线索信息，用于定位文本中有问题的部分
     */
    private List<CheckDetailsVO> details;
}
