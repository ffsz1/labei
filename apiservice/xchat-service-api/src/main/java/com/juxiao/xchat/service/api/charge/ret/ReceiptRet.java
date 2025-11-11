package com.juxiao.xchat.service.api.charge.ret;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Created by 北岭山下 on 2017/7/20.
 */
@Getter
@Setter
public class ReceiptRet {
    private int status;
    private Map<String, Object> receipt;
}