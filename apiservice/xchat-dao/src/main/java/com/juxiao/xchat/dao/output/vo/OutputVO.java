package com.juxiao.xchat.dao.output.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2018/10/17
 * @time 16:33
 */
@Data
public class OutputVO {

    private List<OutputValueVO> list;
    private Long registerNum;
    private Long chargeUserNum;
    private Long totalAmount;
}
