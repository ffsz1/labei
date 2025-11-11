package com.erban.admin.web.controller.mora;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.mora.MoraConfService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.dto.MoraConfDTO;
import com.erban.main.model.MoraConfig;
import com.erban.main.model.Users;
import com.erban.main.service.user.UsersService;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author chris
 * @Title: 猜拳管理
 */
@Controller
@RequestMapping("/admin/mora/conf")
public class MoraConfController extends BaseController {

    @Autowired
    private MoraConfService moraConfService;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private UsersService usersService;

    @RequestMapping("/getList")
    @ResponseBody
    public void getAll(String searchText) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<MoraConfDTO> pageInfo = moraConfService.getListWithPage(searchText, getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }


    @RequestMapping(value="/save")
    @ResponseBody
    public void save(MoraConfig moraConfig, boolean isEdit ){
        int result = -1;
        if(moraConfig != null){
            try{

                if(!isEdit){
                    Users users = usersService.getUsersByErBanNo(moraConfig.getUid());
                    if(users == null){
                        writeJson(false, "该用户不存在!");
                        return;
                    }
                    moraConfig.setUid(users.getUid());
                    MoraConfig temp = moraConfService.getByUid(users.getUid());
                    if(temp != null){
                        writeJson(false, "数据已存在,不能重复添加!");
                        return;
                    }
                }else{
                    Users users = usersService.getUsersByErBanNo(moraConfig.getUid());
                    moraConfig.setUid(users.getUid());
                }
                result = moraConfService.save(moraConfig,isEdit,getAdminId());
                if(result > 0){
                        Gson gson = new Gson();
                        jedisService.hset(RedisKey.mora_config.getKey(),String.valueOf(moraConfig.getUid()),gson.toJson(moraConfig));

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
            MoraConfig moraConfig = moraConfService.getById(id);
            int result = moraConfService.delete(id);
            if(result > 0) {
                jedisService.hdel(RedisKey.mora_config.getKey(),String.valueOf(moraConfig.getUid()));
                writeJson(true, "删除成功");
                return;
            }
        } catch (Exception e) {
            logger.error("Failed to del moraConfig, Cause by {}", e.getCause().getMessage());
        }
        writeJson(false, "删除失败");
    }


    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public void get(@RequestParam("id")Integer id)
    {
        JSONObject jsonObject = new JSONObject();
        MoraConfig moraConfig = moraConfService.getById(id);
        if(moraConfig != null) {
            Users users = usersService.getUsersByUid(moraConfig.getUid());
            moraConfig.setErbanNo(users.getErbanNo());
            jsonObject.put("entity", moraConfig);
        }
        writeJson(jsonObject.toJSONString());
    }
}
