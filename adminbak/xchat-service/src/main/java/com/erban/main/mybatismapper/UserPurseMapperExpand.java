package com.erban.main.mybatismapper;

public interface UserPurseMapperExpand {

    Long getAllUserNum ( );

    Long getConsumeUserNum ( );

    int updatePurseGold(Long num, Long uid);

    int updatePurseDiamond(Double num, Long uid);


}
