package com.erban.admin.main.service.system;

import com.erban.admin.main.common.BusinessException;
import com.erban.main.model.AppSecret;
import com.erban.main.mybatismapper.AppSecretMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.utils.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2019-05-14
 * @time 09:31
 */
@Service
public class AppSecretService {

    @Autowired
    private AppSecretMapper appSecretMapper;

    /**
     * 分页查询版本密钥
     *
     * @param pageNun
     * @param pageSize
     * @return
     */
    public PageInfo page(int pageNun, int pageSize) {
        PageHelper.startPage(pageNun, pageSize);
        List<AppSecret> list = appSecretMapper.selectList();
        return new PageInfo(list);
    }

    /**
     * 新增密钥
     * @param os
     * @param appVersion
     * @param timeStamp
     * @param createTime
     */
    public void addAppSecret(String os, String appVersion, String timeStamp, Date createTime) {
        if (StringUtils.isBlank(os) || StringUtils.isBlank(appVersion)) {
            throw new BusinessException("参数异常");
        }
        AppSecret appSecret = new AppSecret();
        appSecret.setOs(os);
        appSecret.setAppVersion(appVersion);
        appSecret.setSignKey(DigestUtils.md5Hex(timeStamp));
        appSecret.setCreateTime(createTime);
        appSecretMapper.insertAppSecret(appSecret);
    }

    /**
     * 删除密钥
     *
     * @param signKey
     * @return
     */
    public int delete(String signKey) {
        if (StringUtils.isBlank(signKey)) {
            return 0;
        }
        AppSecret appSecret = new AppSecret();
        appSecret.setSignKey(signKey);
        return appSecretMapper.deleteByPrimaryKey(appSecret);
    }
}
