package com.erban.main.mybatismapper;

import com.erban.main.model.StatPacketRegister;
import com.erban.main.model.StatPacketRegisterExample;
import com.erban.main.param.admin.UserAssociateParam;
import com.erban.main.vo.admin.ShareRegisterVo;
import com.erban.main.vo.admin.StatShareRegisterVo;

import java.util.List;

public interface StatPacketRegisterMapper {
    int deleteByPrimaryKey(String registerId);

    int insert(StatPacketRegister record);

    int insertSelective(StatPacketRegister record);

    List<StatPacketRegister> selectByExample(StatPacketRegisterExample example);

    StatPacketRegister selectByPrimaryKey(String registerId);

    int updateByPrimaryKeySelective(StatPacketRegister record);

    int updateByPrimaryKey(StatPacketRegister record);

    List<StatShareRegisterVo> statByQuery(UserAssociateParam userAssociateParam);
    StatShareRegisterVo countByquery(UserAssociateParam userAssociateParam);

    List<ShareRegisterVo> getRegisterList(List<Long> uids);
}
