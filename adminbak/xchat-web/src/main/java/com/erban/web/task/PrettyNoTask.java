package com.erban.web.task;

import com.erban.main.param.neteasepush.NeteaseSendMsgParam;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.prettyNo.PrettyNoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PrettyNoTask extends BaseTask
{
    private static final Logger logger = LoggerFactory.getLogger(PrettyNoTask.class);

    @Autowired
    private PrettyNoService prettyNoService;

    @Scheduled(cron="0 */2 * * * ?")
    public void scanPrettyNo()
    {
        try {
            logger.info("开始扫描靓号=========");
//            this.prettyNoService.scanPrettyNo();
            logger.info("完成扫描靓号==========");
        } catch (Exception e) {
            logger.error("扫描靓号出错...", e);
        }
    }
}
