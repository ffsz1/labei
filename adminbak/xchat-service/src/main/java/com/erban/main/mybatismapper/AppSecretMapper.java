package com.erban.main.mybatismapper;

import com.erban.main.model.AppSecret;

import java.util.List;

public interface AppSecretMapper {

    List selectList();

    int deleteByPrimaryKey(AppSecret appSecret);

    int insertAppSecret(AppSecret appSecret);

}
