package com.juxiao.xchat.manager.external.netease.ret;

import com.juxiao.xchat.manager.external.netease.vo.CheckLabelsVO;
import lombok.Data;

/**
 * @author chris
 * @date 2019-07-14
 */
@Data
public class TextCheckResult {

    /**
     * 检测结果，0：通过，1：嫌疑，2：不通过
     */
    private Integer action;

    private String taskId;

    private CheckLabelsVO labels;
}
