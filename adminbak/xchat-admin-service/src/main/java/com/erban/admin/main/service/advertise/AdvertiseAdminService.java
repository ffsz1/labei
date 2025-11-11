package com.erban.admin.main.service.advertise;

import com.erban.admin.main.base.AbstractCoreService;
import com.erban.main.base.BaseMapper;
import com.erban.main.model.Advertise;
import com.erban.main.model.AdvertiseExample;
import com.erban.main.mybatismapper.AdvertiseMapper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AdvertiseAdminService extends AbstractCoreService<Advertise, AdvertiseExample>{
    @Autowired
    AdvertiseMapper advertiseMapper;

    @Override
    protected BaseMapper<Advertise, AdvertiseExample> getMapper() {
        return advertiseMapper;
    }

    @Override
    public Object getId(Advertise entity) {
        return entity.getAdvId();
    }

    public PageInfo<Advertise> getAdvertiseByPage(int page, int size)
    {
        AdvertiseExample example = new AdvertiseExample();
        example.setOrderByClause(" seq_no asc");
        return this.findByExample(example, page, size);
    }

    @Override
    public boolean beforeInsert(Advertise entity) {
        entity.setCreateTime(new Date());
        return super.beforeInsert(entity);
    }
}
