package com.juxiao.xchat.dao.admin.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AdminUser {
    private Integer id;
    private String username;
    private String password;
    private String headimg;
    private String email;
    private String rolestr;
    private Boolean status;
    private Date lastlogin;
    private Date createtime;
    private String phone;
    private String qq;
}