package com.juxiao.xchat.dao.item.enumeration;

import com.juxiao.xchat.dao.admin.domain.AdminDictKey;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AdminDict extends AdminDictKey {
    private String dictval;
    private Boolean status;
    private Integer showorder;
    private Date createtime;
    private String description;
}