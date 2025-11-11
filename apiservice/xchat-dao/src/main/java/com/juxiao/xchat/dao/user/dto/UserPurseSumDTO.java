package com.juxiao.xchat.dao.user.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户钱包
 *
 * @class: UserPurseDTO.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
@Getter
@Setter
public class UserPurseSumDTO {
    private Long goldSum;
    private Double diamondSum;
}