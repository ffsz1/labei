package com.erban.admin.main.service.charge;

import com.erban.admin.main.service.base.BaseService;
import com.erban.main.model.ChargeProdExample;
import com.erban.main.mybatismapper.ChargeProdMapper;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 后台管理-充值记录service  create by zhaomiao 2018/2/27
 */

@Service
public class ChargeProdAdminService extends BaseService {
    @Autowired
    private ChargeProdMapper chargeProdMapper;


    /**
     * get 所有充值产品
     * @param
     * @return
     */
    public BusiResult getAll() {
        ChargeProdExample example = new ChargeProdExample();
        example.setOrderByClause(" seq_no asc ");
        example.createCriteria().andProdStatusEqualTo((byte)1);
        return new BusiResult(BusiStatus.SUCCESS, chargeProdMapper.selectByExample(example));
    }

}
