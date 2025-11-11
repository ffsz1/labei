package com.erban.admin.main.mapper;

import com.erban.admin.main.dto.MakeFriendDTO;
import com.erban.admin.main.model.MakeFriendRecom;
import com.erban.admin.main.model.MakeFriendRecomExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MakeFriendRecomMapper {
    int countByExample(MakeFriendRecomExample example);

    int deleteByExample(MakeFriendRecomExample example);

    int deleteByPrimaryKey(Long uid);

    int insert(MakeFriendRecom record);

    int insertSelective(MakeFriendRecom record);

    List<MakeFriendRecom> selectByExample(MakeFriendRecomExample example);

    MakeFriendRecom selectByPrimaryKey(Long uid);

    int updateByExampleSelective(@Param("record") MakeFriendRecom record, @Param("example") MakeFriendRecomExample example);

    int updateByExample(@Param("record") MakeFriendRecom record, @Param("example") MakeFriendRecomExample example);

    int updateByPrimaryKeySelective(MakeFriendRecom record);

    int updateByPrimaryKey(MakeFriendRecom record);

    List<MakeFriendDTO> listByUids(List<Long> uids);
}
