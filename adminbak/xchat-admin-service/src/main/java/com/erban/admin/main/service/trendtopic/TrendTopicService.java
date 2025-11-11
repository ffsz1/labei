package com.erban.admin.main.service.trendtopic;

import com.erban.main.config.SystemConfig;
import com.erban.main.model.TrendTopic;
import com.erban.main.mybatismapper.TrendTopicMapper;
import com.erban.main.param.neteasepush.NeteasePushParam;
import com.erban.main.param.neteasepush.NeteaseSendMsgParam;
import com.erban.main.service.SendSysMsgService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.constant.Attach;
import com.xchat.common.constant.Constant;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrendTopicService {
    @Autowired
    private TrendTopicMapper trendTopicMapper;

    @Autowired
    private SendSysMsgService sendSysMsgService;

    public PageInfo<TrendTopic> getListWithPage(Long uid, String state,
                                                String startDate, String endDate,int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<TrendTopic> list = trendTopicMapper.getAllByCheck(uid,state,startDate,endDate);
        return new PageInfo<>(list);
    }

    public BusiResult updateCheckSuccess(String ids, Integer adminId) throws Exception {
        if (StringUtils.isBlank(ids)) {
            return new BusiResult(BusiStatus.SUCCESS);
        }
        int res=0;
        String[] uidArr = ids.split(",");
        for(int i = 0; i < uidArr.length; i ++) {
            //updateCheckSuccess(Long.valueOf(uidArr[i]),adminId);
            TrendTopic trendTopic = trendTopicMapper.selectById(Long.valueOf(uidArr[i]));
            if(trendTopic!=null) {
                int res1 = trendTopicMapper.updateState(Long.valueOf(uidArr[i]), "1");
                if(res1>0) {
                    sendMsg(trendTopic.getUid(), "亲爱的声友用户,你发布的动态已审核通过~");
                    res=+1;
                }
            }
        }
        if(res>0){
            return new BusiResult(200,"成功审核通过"+res+"条记录",null);
        }else{
            return new BusiResult(505,"审核失败",null);
        }
    }

    public BusiResult updateCheckFailure(Long id, Integer adminId){
        TrendTopic trendTopic = trendTopicMapper.selectById(id);
        if(trendTopic!=null) {
            int res = trendTopicMapper.updateState(id, "2");
            if(res>0){
                sendMsg(trendTopic.getUid(),"抱歉，你发布的动态未审核通过~");
                return new BusiResult(BusiStatus.SUCCESS);
            }
        }else{
            return new BusiResult(BusiStatus.ADMIN_SHAREREGISTER_NOTEXIT);
        }
        return null;
    }

    private void sendMsg(Long uid, String msg) {
        // 发送消息给用户
        NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
        neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
        neteaseSendMsgParam.setOpe(0);
        neteaseSendMsgParam.setType(0);
        neteaseSendMsgParam.setTo(uid.toString());
        neteaseSendMsgParam.setBody(msg);
        sendSysMsgService.sendMsg(neteaseSendMsgParam);
    }
}
