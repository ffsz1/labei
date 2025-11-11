package com.erban.main.service;

import com.erban.main.config.WxConfig;
import com.erban.main.model.AppVersion;
import com.erban.main.model.AppVersionExample;
import com.erban.main.model.SysConf;
import com.erban.main.mybatismapper.AppVersionMapper;
import com.erban.main.service.base.BaseService;
import com.erban.main.util.HttpUtil;
import com.erban.main.util.IpUtil;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.AppVersionVo;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.BlankUtil;
import com.xchat.oauth2.service.core.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Service
public class AppVersionService extends BaseService {
    @Autowired
    WxConfig wxConfig;
    @Autowired
    private AppVersionMapper appVersionMapper;
    @Autowired
    private AppVersionUpdateConfService appVersionUpdateConfService;
    @Autowired
    private SysConfService sysConfService;

    /**
     * 判断是否为审核中版本
     *
     * @param version
     * @return
     */
    public boolean checkIsAuditingVersion(String version, HttpServletRequest request) {
        try {
            String clientIp = IpUtil.getRemoteIp(request);
            String str = jedisService.hget(RedisKey.client_ip.getKey(), clientIp);
            if (StringUtils.isBlank(str)) {
                String url = "http://ip.taobao.com/service/getIpInfo.php";
                String result = HttpUtil.get(url, "ip=" + clientIp);
                BusiResult busiResult = gson.fromJson(result, BusiResult.class);
                //解析相应内容（转换成json对象
                if (busiResult.getCode() == 0) {
                    Map<String, Object> map = (Map<String, Object>) busiResult.getData();
                    if (map.get("country") != null) {
                        str = map.get("country").toString();
                        jedisService.hset(RedisKey.client_ip.getKey(), clientIp, str);
                    }
                }
            }
            if ("美国".equals(str) || "香港".equals(str) || "台湾".equals(str)) {
                return true;
            }
        } catch (Exception e) {

        }
        String auditingVersion = sysConfService.getSysConfValueById(Constant.SysConfId.auditing_version);
        if (StringUtils.isEmpty(auditingVersion)) {
            throw new BusinessException("Failed to get auditing version.Please check auditing_version.");
        }
        if ("".equals(version.trim())) {
            logger.info("auditing_version is null or ''");
            return false;
        } else if (auditingVersion.equals(version.trim())) {
            logger.info("auditing_version : " + auditingVersion + ", version: " + version);
            return true;
        }
        return false;
    }

    /**
     * 获取版本信息(附带版本更新信息)
     *
     * @param appVersion
     * @param os
     * @return
     */
    public BusiResult<AppVersionVo> getAppVersionInfo(String appVersion, String os) {
        AppVersion version = getAppVersion(appVersion, os);
        if (version == null) {
            throw new BusinessException("Not found appVerison, appVersion: " + appVersion + ", os: " + os);
        }

        AppVersionVo appVersionVo = checkVersion((appVer) -> {
            // 该版本状态是否处于建议更新或强制更新状态
            return Constant.AppVersion.forceupdate.equals(appVer.getStatus())
                    || Constant.AppVersion.recommupdate.equals(appVer.getStatus());
        }, version);

        // 添加app房间踢人参数，重新进入房间的时间，单位为秒
        SysConf sysConf = sysConfService.getSysConfById("kick_waiting");
        if (sysConf != null) {
            appVersionVo.setKickWaiting(Integer.valueOf(sysConf.getConfigValue()));
        } else {
            appVersionVo.setKickWaiting(0);
        }

        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        busiResult.setData(appVersionVo);
        return busiResult;
    }

    /**
     * 根据版本号及os获取版本详情
     *
     * @param version
     * @param os
     * @return
     */
    private AppVersion getAppVersion(String version, String os) {
        AppVersion appVersion = getAppVersionCache(version, os);
        if (appVersion == null) {
            appVersion = queryAppVersionBy(version, os);
            if (appVersion != null) {
                saveAppVersionCache(appVersion);
            }
        }
        return appVersion;
    }

    private AppVersionVo checkVersion(Predicate<AppVersion> needUpdate, AppVersion appVersion) {
        AppVersionVo appVersionVo = covertAppVersionToVo(appVersion);
        // 判断该版本是否需要更新
        if (needUpdate.test(appVersion)) {
            // 获取当前os最新的可用版本
            AppVersion newestVersion = getNewestAppVersion(appVersion.getOs());
            if (newestVersion == null) {
                logger.error("Not found newestVersion. The version: " + appVersion.getVersion() + ", os: " + appVersion.getOs());
                throw new BusinessException("Not found newestVersion.");
            }

            // 将最新的版本及描述加入Vo
            appVersionVo.setUpdateVersion(newestVersion.getVersion());
            appVersionVo.setUpdateVersionDesc(newestVersion.getVersionDesc());
            appVersionVo.setDownloadUrl("http://res.91fb.com/formal.apk");
        } else {
            appVersionVo.setUpdateVersion("");
        }
        return appVersionVo;
    }

    /**
     * 获取当前os最新的版本
     *
     * @param os
     * @return
     */
    private AppVersion getNewestAppVersion(String os) {
        // 从配置表中获取当前最新的版本号
        String newestVersionValue = sysConfService.getSysConfValueById(Constant.SysConfId.newest_version);

        // 根据版本号获取版本信息
        return getAppVersionCache(newestVersionValue, os);
    }

    /**
     * 管理后台获取所有版本
     *
     * @return
     */
    public BusiResult<List<AppVersion>> getAllAppVersionList() {
        BusiResult<List<AppVersion>> busiResult = new BusiResult<List<AppVersion>>(BusiStatus.SUCCESS);
        List<AppVersion> appVersionList = getAllAppVersionListByDb();
        busiResult.setData(appVersionList);
        return busiResult;
    }

    public List<AppVersion> getAllAppVersionListByDb() {
        AppVersionExample appVersionExample = new AppVersionExample();
        List<AppVersion> appVersionList = appVersionMapper.selectByExample(appVersionExample);
        return appVersionList;
    }


    private AppVersionVo covertAppVersionToVo(AppVersion appVersion) {
        AppVersionVo appVersionVo = new AppVersionVo();
        appVersionVo.setOs(appVersion.getOs());
        appVersionVo.setStatus(appVersion.getStatus());
        appVersionVo.setVersion(appVersion.getVersion());
        appVersionVo.setVersionDesc(appVersion.getVersionDesc());
        return appVersionVo;
    }

    private AppVersion queryAppVersionBy(String version, String os) {
        AppVersionExample appVersionExample = new AppVersionExample();
        appVersionExample.createCriteria().andVersionEqualTo(version).andOsEqualTo(os);
        List<AppVersion> appVersionList = appVersionMapper.selectByExample(appVersionExample);
        if (CollectionUtils.isEmpty(appVersionList)) {
            return null;
        } else {
            return appVersionList.get(0);
        }

    }

    private AppVersion getAppVersionCache(String version, String os) {
        String key = version + os;
        String appVersionStr = jedisService.hget(RedisKey.app_version.getKey(), key);
        if (StringUtils.isEmpty(appVersionStr)) {
            return null;
        } else {
            AppVersion appVersion = gson.fromJson(appVersionStr, AppVersion.class);
            return appVersion;
        }
    }

    private void saveAppVersionCache(AppVersion appVersion) {
        if (appVersion == null) {
            return;
        }
        String version = appVersion.getVersion();
        String os = appVersion.getOs();
        String key = version + os;
        jedisService.hwrite(RedisKey.app_version.getKey(), key, gson.toJson(appVersion));
    }

    private void deleteAppVersionCache(String version, String os) {
        String key = version + os;
        jedisService.hdel(RedisKey.app_version.getKey(), key);
    }

    private void batchSaveAppVersion(List<AppVersion> appVersionList) {
        if (CollectionUtils.isEmpty(appVersionList)) {
            return;
        }
        for (AppVersion appVersion : appVersionList) {
            saveAppVersionCache(appVersion);
        }
    }

    private void refreshAppVersion() {
        List<AppVersion> appVersionList = getAllAppVersionListByDb();
        batchSaveAppVersion(appVersionList);
    }

    public void cleanCache() {
        jedisService.hdeleteKey(RedisKey.app_version.getKey());
        refreshAppVersion();
        jedisService.hdeleteKey(RedisKey.app_version_update.getKey());
        appVersionUpdateConfService.refreshAppVersionUpdateConf();
    }

    public void updateIOSAuditingVersion(int operType) {
        if (operType == 1) {//上审核

        } else {//下审核

        }
    }


    public BusiResult updateVersion(String version, Byte status) {
        AppVersion appVersion = new AppVersion();
        appVersion.setStatus(status);
        appVersion.setVersion(version);
        AppVersionExample appVersionExample = new AppVersionExample();
        appVersionExample.createCriteria().andVersionEqualTo(version);
        appVersionMapper.updateByExampleSelective(appVersion, appVersionExample);
//        deleteAppVersionCache(version,"iOS");
        refreshAppVersion();
        return new BusiResult(BusiStatus.SUCCESS);
    }


    public BusiResult checkVersion(String version) {
        if (BlankUtil.isBlank(version)) {
            return new BusiResult(BusiStatus.VERSIONISNULL);
        }
        String replace = version.replace(".", "");
        int versionInt = Integer.parseInt(replace);
        if (versionInt < 230) {
            return new BusiResult(BusiStatus.VERSIONTOLOW);
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }
}
