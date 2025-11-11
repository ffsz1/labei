package com.erban.main.service.advertise;

import com.erban.main.model.Advertise;
import com.erban.main.model.AdvertiseExample;
import com.erban.main.mybatismapper.AdvertiseMapper;
import com.xchat.common.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by PaperCut on 2018/1/30.
 */
@Service
public class AdvertiseService {
    @Autowired
    AdvertiseMapper advertiseMapper;

    public List<Advertise> list()
    {
        AdvertiseExample example = new AdvertiseExample();
        example.createCriteria().andAdvStatusEqualTo(Constant.status.valid);
        example.setOrderByClause(" seq_no asc");
        return advertiseMapper.selectByExample(example);
    }
}
