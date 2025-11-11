package com.erban.admin.web.controller.mora;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.bo.MoraAwardBO;
import com.erban.admin.main.service.mora.MoraAwardService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.dto.MoraAwardDTO;
import com.erban.main.model.MoraAward;
import com.github.pagehelper.PageInfo;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author chris
 * @Title:
 * @date 2019-05-30
 * @time 11:20
 */
@Controller
@RequestMapping("/admin/mora/award")
public class MoraAwardController extends BaseController {


    @Autowired
    private MoraAwardService moraAwardService;

    @Autowired
    private JedisService jedisService;


    @RequestMapping("/getList")
    @ResponseBody
    public void getAll(String searchText) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<MoraAwardDTO> pageInfo = moraAwardService.getList(searchText, getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }





    @RequestMapping(value="/save")
    @ResponseBody
    public void save(MoraAwardBO moraAwardBO, boolean isEdit ){
        int result = -1;
        if(moraAwardBO != null){
            try{
                result = moraAwardService.save(moraAwardBO,isEdit,getAdminId());
                if(result > 0){
                    writeJson(true, "保存成功");
                    return;

                }else{
                    writeJson(false, "保存失败");
                    return;
                }
            } catch (Exception e){
                logger.warn("save fail,cause by " + e.getMessage(),e);
            }
        }else{
            writeJson(false, "参数不能为空!");
            return;
        }
        writeJson(true, "保存成功");
    }

    /**
     * 删除
     * @param id id
     */
    @RequestMapping(value = "del")
    @ResponseBody
    public void del(Integer id)
    {
        try {
            MoraAward moraAward = moraAwardService.getById(id);
            int result = moraAwardService.delete(id);
            if(result > 0) {
                jedisService.hdel(RedisKey.mora_award.getKey(),String.valueOf(moraAward.getProbability()));
                writeJson(true, "删除成功");
                return;
            }
        } catch (Exception e) {
            logger.error("Failed to del moraConfig, Cause by {}", e.getCause().getMessage());
        }
        writeJson(false, "删除失败");
    }

    @RequestMapping(value = "/use", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult use(Integer id) {
        return moraAwardService.use(id);
    }

}
