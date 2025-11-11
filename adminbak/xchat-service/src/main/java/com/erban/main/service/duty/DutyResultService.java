package com.erban.main.service.duty;

/**
 * 新手任务接口
 *
 * @class: DutyResultService.java
 * @author: chenjunsheng
 * @date 2018/8/9
 */
public interface DutyResultService {

    /**
     * 获取用户完成状态
     *
     * @param uid
     * @return
     */
    Byte checkUserDutyStatus(Long uid);

}
