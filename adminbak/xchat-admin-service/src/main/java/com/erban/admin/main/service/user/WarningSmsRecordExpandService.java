package com.erban.admin.main.service.user;

import com.erban.main.dto.WarningSmsRecordDTO;
import com.erban.main.mybatismapper.WarningSmsRecordMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2018/9/13
 * @time 上午11:33
 */
@Service
public class WarningSmsRecordExpandService {

    @Autowired
    private WarningSmsRecordMapper warningSmsRecordMapper;


    public PageInfo<WarningSmsRecordDTO> getListWithPage(String searchText, int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        int offset = (pageNumber - 1) * pageSize;
        PageHelper.startPage(pageNumber, pageSize);

        List<WarningSmsRecordDTO> list = warningSmsRecordMapper.selectByPage(searchText);
        return new PageInfo<>(list);
    }

    public List<WarningSmsRecordDTO> selectByList() {
        return this.warningSmsRecordMapper.selectByPage(null);
    }
}
