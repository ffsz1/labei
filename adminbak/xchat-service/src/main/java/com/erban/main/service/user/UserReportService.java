package com.erban.main.service.user;

import com.erban.main.model.GiftSendRecordVo3;
import com.erban.main.model.UserReportRecord;
import com.erban.main.model.dto.UserReportRecordDTO;
import com.erban.main.mybatismapper.UserReportRecordMapper;
import com.erban.main.param.UserReportParam;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserReportService {

    @Autowired
    private UserReportRecordMapper userReportRecordMapper;

    public int save(UserReportParam reportParam) {
        UserReportRecord reportRecord = new UserReportRecord();
        BeanUtils.copyProperties(reportParam, reportRecord);
        reportRecord.setCreateDate(new Date());
        return userReportRecordMapper.insert(reportRecord);
    }


    public BusiResult selectRoomUidByList(Long roomId) {
        List<UserReportRecordDTO> userReportRecordDTOS = userReportRecordMapper.selectRoomId(roomId);
        if(userReportRecordDTOS==null || userReportRecordDTOS.size()==0){
            return new BusiResult(BusiStatus.STAT_ROOMFLOW_NOTEXIT);
        }
        List<UserReportRecordDTO> rets=new ArrayList<>(userReportRecordDTOS.size());
        userReportRecordDTOS.stream().forEach(item -> {
            item.setDate(DateTimeUtil.convertDate(item.getCreateTime(), "yyyy-MM-dd"));
            rets.add(item);
        });

        for (int i =0; i<userReportRecordDTOS.size() ; i++) {
            if(i==userReportRecordDTOS.size()-1) break;;
            long time1 = userReportRecordDTOS.get(i).getCreateTime().getTime();
            long time2 = userReportRecordDTOS.get(i+1).getCreateTime().getTime();
            long n = (time1 - time2) / (24 * 60 * 60 * 1000);
            if (n > 1) {//间隔n-1天
                for (int j = 1; j <= n-1; j++) {
                    UserReportRecordDTO giftSendRecordVo3 = new UserReportRecordDTO();
                    giftSendRecordVo3.setCreateTime(new Date(time1 - 24 * 60 * 60 * 1000 * j));
                    giftSendRecordVo3.setTotalGoldNum(0L);
                    giftSendRecordVo3.setDate(DateTimeUtil.convertDate(giftSendRecordVo3.getCreateTime(), "yyyy-MM-dd"));
                    rets.add(giftSendRecordVo3);
                }
            }
        }
        rets.sort(Comparator.comparing(UserReportRecordDTO::getDate).reversed());
        return new BusiResult(BusiStatus.SUCCESS, rets);
    }

    public BusiResult selectRoomUidByDetail(String date, Long roomId, Integer current, Integer size) {
        String beginDate=date+" 00:00:00";
        String endDate=date+" 23:59:59";
        List<UserReportRecordDTO> roomFlowDetail = userReportRecordMapper.selectRoomIdByList(roomId,beginDate, endDate, current,size);
        if(roomFlowDetail==null || roomFlowDetail.size()==0){
            return new BusiResult(BusiStatus.STAT_ROOMFLOW_DETAILNOTEXIT);
        }
        return new BusiResult(BusiStatus.SUCCESS,roomFlowDetail);
    }
}
