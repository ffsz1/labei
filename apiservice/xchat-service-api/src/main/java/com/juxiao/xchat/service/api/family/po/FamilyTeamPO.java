package com.juxiao.xchat.service.api.family.po;

import com.juxiao.xchat.dao.family.dto.FamilyDTO;
import com.juxiao.xchat.dao.family.dto.FamilyJoinsDTO;
import lombok.Data;

import java.util.List;

/**
 * @author laizhilong
 * @Title:
 * @Package com.juxiao.xchat.service.api.family.po
 * @date 2018/9/6
 * @time 10:13
 */
@Data
public class FamilyTeamPO {

    private List<FamilyDTO> familyList;

    private FamilyJoinsDTO familyTeam;
}
