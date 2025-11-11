package com.juxiao.xchat.dao.admin.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AdminMenu {
    private Integer id;
    private Integer parentid;
    private String name;
    private String path;
    private String icon;
    private Boolean status;
    private Integer showorder;
    private Date createtime;
    private String description;
}