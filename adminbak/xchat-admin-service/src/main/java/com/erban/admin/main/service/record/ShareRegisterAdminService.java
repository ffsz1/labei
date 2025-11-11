package com.erban.admin.main.service.record;

import com.erban.admin.main.service.base.BaseService;
import com.erban.main.model.StatPacketRegister;
import com.erban.main.model.StatPacketRegisterExample;
import com.erban.main.model.UserConfigure;
import com.erban.main.mybatismapper.StatPacketRegisterMapper;
import com.erban.main.param.admin.UserAssociateParam;
import com.erban.main.service.user.UserConfigureService;
import com.erban.main.vo.admin.ShareRegisterVo;
import com.erban.main.vo.admin.StatShareRegisterVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 邀请注册
 */
@Service
public class ShareRegisterAdminService extends BaseService {
    @Autowired
    private StatPacketRegisterMapper statPacketRegisterMapper;
    @Autowired
    private UserConfigureService userConfigureService;

    /**
     * 邀请人统计
     *
     * @param userAssociateParam
     * @return
     */
    public BusiResult queryStat(UserAssociateParam userAssociateParam) {
        userAssociateParam.setBeginDate(userAssociateParam.getBeginDate()+" 00:00:00");
        userAssociateParam.setEndDate(userAssociateParam.getEndDate()+" 23:59:59");
        PageHelper.startPage(userAssociateParam.getPage(), userAssociateParam.getSize());
        List<StatShareRegisterVo> statShareRegisterVos = statPacketRegisterMapper.statByQuery(userAssociateParam);
        if (statShareRegisterVos == null || statShareRegisterVos.size() == 0) {
            return new BusiResult(BusiStatus.ADMIN_SHAREREGISTER_NOTEXIT);
        }
        UserConfigure userConfigure;
        for(StatShareRegisterVo statShareRegisterVo:statShareRegisterVos){
            userConfigure = userConfigureService.getOneByJedisId(statShareRegisterVo.getUid().toString());
            //是否有上级分成权限
            if(userConfigure==null||userConfigure.getSuperiorBouns().intValue()==0){

            }else{
                statShareRegisterVo.setLowerRegisterNum(jdbcTemplate.queryForObject("select COUNT(1) from stat_packet_register where uid in (select register_uid from stat_packet_register where uid = ? and create_time BETWEEN ? and ?) and create_time BETWEEN ? and ?", Integer.class, statShareRegisterVo.getUid(), userAssociateParam.getBeginDate(), userAssociateParam.getEndDate(), userAssociateParam.getBeginDate(), userAssociateParam.getEndDate()));
            }
        }
        StatShareRegisterVo statShareRegisterVo = statPacketRegisterMapper.countByquery(userAssociateParam);
        if(statShareRegisterVo!=null){
            statShareRegisterVos.get(0).setSumSharer(statShareRegisterVo.getSumSharer());
            statShareRegisterVos.get(0).setSumRegister(statShareRegisterVo.getSumRegister());
        }
        return new BusiResult(BusiStatus.SUCCESS, new PageInfo(statShareRegisterVos));
    }

    /**
     * 邀请明细
     * @param userAssociateParam
     * @return
     */
    public BusiResult getRegisterList(UserAssociateParam userAssociateParam){
        List<Long> uids=new ArrayList<>();
        StatPacketRegisterExample example =new StatPacketRegisterExample();
        StatPacketRegisterExample.Criteria criteria = example.createCriteria().andUidEqualTo(userAssociateParam.getUid());
        if(!StringUtils.isBlank(userAssociateParam.getBeginDate())){
            criteria.andCreateTimeGreaterThan(DateTimeUtil.convertStrToDate(userAssociateParam.getBeginDate()+" 00:00:00"));
        }
        if(!StringUtils.isBlank(userAssociateParam.getEndDate())){
            criteria.andCreateTimeLessThanOrEqualTo(DateTimeUtil.convertStrToDate(userAssociateParam.getEndDate()+" 23:59:59"));
        }
        List<StatPacketRegister> statPacketRegisters = statPacketRegisterMapper.selectByExample(example);
        for (StatPacketRegister s:statPacketRegisters) {
            uids.add(s.getRegisterUid());
        }
        PageHelper.startPage(userAssociateParam.getPage(), userAssociateParam.getSize());
        List<ShareRegisterVo> registerList = statPacketRegisterMapper.getRegisterList(uids);
        if(registerList==null || registerList.size()==0){
            return new BusiResult(BusiStatus.ADMIN_SHAREREGISTER_NOTEXITLIST);
        }
        return new BusiResult(BusiStatus.SUCCESS,new PageInfo(registerList));
    }
}
