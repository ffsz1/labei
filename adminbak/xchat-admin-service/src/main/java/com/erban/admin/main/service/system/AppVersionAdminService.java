package com.erban.admin.main.service.system;

import com.erban.admin.main.base.AbstractCoreService;
import com.erban.admin.main.common.BusinessException;
import com.erban.main.base.BaseMapper;
import com.erban.main.model.AppVersion;
import com.erban.main.model.AppVersionExample;
import com.erban.main.mybatismapper.AppVersionMapper;
import com.erban.main.service.SysConfService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by PaperCut on 2018/1/20.
 */
@Service
public class AppVersionAdminService extends AbstractCoreService<AppVersion, AppVersionExample> {
    @Autowired
    AppVersionMapper appVersionMapper;

    @Autowired
    SysConfService sysConfService;

    public static final String force_type = "force";
    public static final String suggest_type = "suggest";

    public PageInfo<AppVersion> getVersionByPage(int page, int size) {
        PageHelper.startPage(page, size);
        AppVersionExample example = new AppVersionExample();
        example.setOrderByClause("create_time desc");
        return new PageInfo<>(appVersionMapper.selectByExample(example));
    }

    @Override
    public boolean beforeInsert(AppVersion entity) {
        entity.setCreateTime(new Date());
        return super.beforeInsert(entity);
    }

    @Override
    public Object getId(AppVersion entity) {
        return entity.getVersionId();
    }

    @Override
    protected BaseMapper<AppVersion, AppVersionExample> getMapper() {
        return appVersionMapper;
    }

    /**
     * 批量更新状态
     * @param ids
     * @param status
     * @return
     */
    private int updateStatusByIds(List<Integer> ids, byte status)
    {
        AppVersionExample example = new AppVersionExample();
        example.createCriteria().andVersionIdIn(ids);
        AppVersion appVersion = new AppVersion();
        appVersion.setStatus(status);
        return appVersionMapper.updateByExampleSelective(appVersion, example);
    }

    /**
     * 通过状态统计版本记录
     * @param status
     * @return
     */
    public int countByStatus(byte status)
    {
        AppVersionExample example = new AppVersionExample();
        example.createCriteria().andStatusEqualTo(status);
        return appVersionMapper.countByExample(example);
    }

    /**
     * 更新指定版本为审核中状态
     * @param id
     * @return
     */
    public int resetAudit(Integer id)
    {
        AppVersion appVersion = appVersionMapper.selectByPrimaryKey(id);
        // 更新指定id的版本状态为审核中
        int result = updateStatusById(id, Constant.AppVersion.audit);
        return result;
    }

    /**
     * 通过id更新状态
     * @param id
     * @param status
     * @return
     */
    private int updateStatusById(Integer id, byte status)
    {
        AppVersionExample example = new AppVersionExample();
        example.createCriteria().andVersionIdEqualTo(id);
        AppVersion appVersion = new AppVersion();
        appVersion.setStatus(status);
        return appVersionMapper.updateByExampleSelective(appVersion, example);
    }

    /**
     * 批量重置状态(只支持强制更新和建议更新)
     * @param type
     * @param ids
     * @return
     */
    public int resetStatus(String type, List<Integer> ids) {
        if(force_type.equalsIgnoreCase(type)) {
            // 设置为强制更新
            return updateStatusByIds(ids, Constant.AppVersion.forceupdate);
        } else if(suggest_type.equalsIgnoreCase(type)) {
            // 设置为建议更新
            return updateStatusByIds(ids, Constant.AppVersion.recommupdate);
        }
        return 0;
    }

    @Override
    public int save(AppVersion entity, boolean isEdit, boolean isSelective) {
        if(Constant.AppVersion.audit.equals(entity.getStatus())) {
            // 统计是否有审核状态中的记录
            int count = countByStatus(Constant.AppVersion.audit);
            if(count > 0) {
                throw new BusinessException("已存在审核状态中的版本记录.请修改后再试");
            }
        }

        // 如果之前版本是审核中版本,则去除当前审核版本
        AppVersion beforeVersion = get(entity.getVersionId());
        int result = super.save(entity, isEdit, isSelective);
        return result;
    }
}
