
package com.erban.admin.main.service;

import com.erban.admin.main.service.base.BaseService;
import com.erban.main.model.NobleRes;
import com.erban.main.model.NobleResExample;
import com.erban.main.model.NobleRight;
import com.erban.main.mybatismapper.NobleResMapper;
import com.erban.main.service.noble.NobleResService;
import com.erban.main.service.noble.NobleRightService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 资源配置
 *
 */

@Service
public class ResourceAdminService extends BaseService {
    @Autowired
    private NobleResMapper nobleResMapper;

    @Autowired
    private NobleResService  nobleResService;

    @Autowired
    private NobleRightService nobleRightService;

    /**
     * 分页查询
     * @param pageNumber
     * @param pageSize
     * @param type
     * @param nobleId
     * @return
     */
    public PageInfo<NobleRes> getList(Integer pageNumber, Integer pageSize, Byte type, Integer nobleId){
        NobleResExample nobleResExample = new NobleResExample();
        nobleResExample.setOrderByClause("create_time DESC");
        NobleResExample.Criteria criteria = nobleResExample.createCriteria();
        if(type!=0){
            criteria.andResTypeEqualTo(type);
        }
        if(nobleId!=-1){
            criteria.andNobleIdEqualTo(nobleId);
        }
        if(nobleId !=-1 && type!=0){
            criteria.andNobleIdEqualTo(nobleId).andResTypeEqualTo(type);
        }
        PageHelper.startPage(pageNumber,pageSize);
        List<NobleRes> nobleRes = nobleResMapper.selectByExample(nobleResExample);
        return new PageInfo<>(nobleRes);
    }


    /**
     * 保存资源
     * @param nobleId
     * @param name
     * @param res
     * @param resType
     * @param status
     * @param isDyn
     * @param isDef
     * @return
     */
    public int saveResource(int nobleId,String name,String res,String preview,Byte resType, Byte status,Byte isDyn,Byte isDef,int seq){
        NobleRes nobleRes =new NobleRes();
        nobleRes.setNobleId(nobleId);
        NobleRight nobleRight = nobleRightService.getNobleRightByKey(nobleId);
        if(!StringUtils.isEmpty(nobleRight)){
            nobleRes.setNobleName(nobleRight.getName());
        }else{
            nobleRes.setNobleName("");
        }
        nobleRes.setName(name);
        nobleRes.setValue(res);
        nobleRes.setPreview(res);
        nobleRes.setPreview(preview);
        nobleRes.setResType(resType);
        nobleRes.setStatus(status);
        nobleRes.setIsDyn(isDyn);
        nobleRes.setIsDef(isDef);
        nobleRes.setSeq(seq);
        nobleRes.setCreateTime(new Date());
        int result = nobleResMapper.insert(nobleRes);
        return result ;
    }


    /**
     * 查询资源
     */
    public NobleRes getResList(Integer rowId){
        return nobleResService.getResOne(rowId);
    }

    /**
     * 编辑保存
     */
    public int updateRes(Integer rowId,int nobleType,byte resType,byte isDyn,byte isDef,byte status,String resName,String res,String preview,int seq){
        NobleRes resOne = nobleResService.getResOne(rowId);
        resOne.setNobleId(nobleType);
        resOne.setResType(resType);
        resOne.setIsDyn(isDyn);
        resOne.setIsDef(isDef);
        resOne.setStatus(status);
        if(nobleType==0){
            resOne.setName("");
        }
        resOne.setName(resName);
        resOne.setValue(res);
        resOne.setPreview(preview);
        resOne.setSeq(seq);
        return nobleResMapper.updateByPrimaryKey(resOne);
    }

    /**
     * 删除
     */
    public int delRes(Integer rowId){
        return nobleResMapper.deleteByPrimaryKey(rowId);
    }
}

