package com.erban.admin.main.service.system;

import com.beust.jcommander.internal.Maps;
import com.erban.main.model.ClientLog;
import com.erban.main.model.ClientLogExample;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.ClientLogMapper;
import com.erban.main.param.neteasepush.NeteasePushParam;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.user.UsersService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.constant.Attach;
import com.xchat.common.constant.Constant;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author chris
 * @Title:
 * @date 2019-05-13
 * @time 14:27
 */
@Service
public class ClientLogService {


    @Autowired
    private UsersService usersService;
    @Autowired
    private ClientLogMapper clientLogMapper;

    @Autowired
    private SendSysMsgService sendSysMsgService;



    public PageInfo<ClientLog> listLog(int pageNum, int pageSize, String erbanNo) {
        PageHelper.startPage(pageNum, pageSize);
        ClientLogExample example = new ClientLogExample();
        ClientLogExample.Criteria criteria = example.createCriteria();
        if (erbanNo != null) {
            criteria.andErbanNoLike(erbanNo + "%");
        }
        List<ClientLog> list = clientLogMapper.selectByExample(example);
        return new PageInfo<>(list);
    }

    /**
     * 获取客户端日志
     *
     * @param erbanNo
     * @return
     */
    public BusiResult getLog(Long erbanNo, Integer num) {
        Users users = usersService.getUsersByErBanNo(erbanNo);
        if (users == null) {
            return new BusiResult(BusiStatus.NOTEXISTS);
        }
        Attach attach = new Attach();
        attach.setFirst(Constant.DefMsgType.CLIENT_UPLOAD_LOG);
        attach.setSecond(Constant.DefMsgType.CLIENT_UPLOAD_LOG);
        Map<String, Object> param = Maps.newHashMap();
        param.put("days", num == null ? 3 : num);
        attach.setData(param);
        NeteasePushParam neteasePushParam = new NeteasePushParam();
        neteasePushParam.setFrom(Constant.official.uid.toString());
        neteasePushParam.setTo(users.getUid().toString());
        neteasePushParam.setMsgtype(0);
        neteasePushParam.setSave(2);
        neteasePushParam.setAttach(attach);
        int code = sendSysMsgService.sendSysAttachMsg(neteasePushParam);
        return BusiResult.SUCCESS(code);
    }
}
