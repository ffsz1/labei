package com.erban.admin.main.mapper;

import com.erban.admin.main.dto.UserBankCardDTO;
import com.erban.admin.main.dto.UsersAvatarDTO;
import com.erban.admin.main.dto.UsersPhotoDTO;
import com.erban.admin.main.model.AdminUser;
import com.erban.admin.main.model.AdminUserExample;
import com.erban.admin.main.vo.UsersVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapperExpand {
    List<UsersVo> selectUserWithGold(List<Long> erNos);

    List<UsersVo> selectUserWithDiamond(List<Long> erNos);

    UsersVo selectUidByErbanNo(@Param("erbanNo") Long erbanNo);

    UsersVo selectUidByUsers(@Param("uid") Long uid);

    List<UserBankCardDTO> findUserBankCard(@Param("erbanNoList") String[] erbanNoList);

    List<UsersAvatarDTO> findUsersAvatar(@Param("erbanNo") Long erbanNo,
                                         @Param("status") Integer status,
                                         @Param("startDate") String startDate,
                                         @Param("endDate") String endDate);

    List<UsersPhotoDTO> findUsersPhoto(@Param("erbanNo") Long erbanNo, @Param("status") Integer status, @Param(
            "startDate") String startDate,
                                       @Param("endDate") String endDate);
}
