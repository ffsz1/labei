package com.erban.admin.main.service.system;

import com.erban.admin.main.mapper.AdminLogMapper;
import com.erban.admin.main.model.AdminLog;
import com.erban.admin.main.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AdminLogService extends BaseService {

    @Autowired
    private AdminLogMapper adminLogMapper;

    public int insertLog(Integer uid, String clazz, String method, String mess) {
        return insertLog(uid, clazz, method, mess, null, null);
    }

    public int insertLog(Integer uid, String clazz, String method, String mess, String tmpstr, Integer tmpint) {
        AdminLog adminLog = new AdminLog();
        adminLog.setCreateTime(new Date());
        adminLog.setOptClass(clazz);
        adminLog.setOptMethod(method);
        adminLog.setOptMess(mess);
        adminLog.setOptUid(uid);
        adminLog.setTmpint(tmpint);
        adminLog.setTmpstr(tmpstr);
        return adminLogMapper.insertSelective(adminLog);
    }
}
