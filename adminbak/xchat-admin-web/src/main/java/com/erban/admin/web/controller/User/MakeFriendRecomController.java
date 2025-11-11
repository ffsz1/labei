package com.erban.admin.web.controller.User;

import com.erban.admin.main.service.user.MakeFriendRecomService;
import com.erban.admin.web.base.BaseController;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: alwyn
 * @Description:
 * @Date: 2018/11/11 001112:05
 */
@Controller
@RequestMapping("admin/makeFriendRecom")
public class MakeFriendRecomController extends BaseController {

    @Autowired
    private MakeFriendRecomService recomService;

    /**
     * 列表查询
     * @param erbanNo
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(Long erbanNo) {
        //
        return recomService.getList(erbanNo, getPageNumber(), getPageSize());
    }

    /**
     * 保存推荐信息
     * @param uid
     * @param erbanNo
     * @param source
     * @return
     */
    @RequestMapping("saveUser")
    @ResponseBody
    public BusiResult saveUser(Long uid, Long erbanNo, Long source) {

        try {
            boolean flag = recomService.saveRecomUser(uid, erbanNo, source);
            if (flag) {
                return new BusiResult(BusiStatus.SUCCESS);
            }
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR, e.getMessage());
        }
        return new BusiResult(BusiStatus.SERVERERROR);
    }

    /**
     * 删除推荐用户信息
     * @param uid
     * @return
     */
    @RequestMapping("delUser")
    @ResponseBody
    public BusiResult delUser(Long uid) {
        try {
            boolean flag = recomService.delUser(uid);
            if (flag) {
                return new BusiResult(BusiStatus.SUCCESS);
            }
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR, e.getMessage());
        }
        return new BusiResult(BusiStatus.SERVERERROR);
    }

    /**
     * 给用户添加排序分值
     * @param uid
     * @param source
     * @return
     */
    @RequestMapping("addSource")
    @ResponseBody
    public BusiResult addSource(Long uid, Long source) {
        try {
            boolean flag = recomService.addSource(uid, source);
            if (flag) {
                return new BusiResult(BusiStatus.SUCCESS);
            }
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR, e.getMessage());
        }
        return new BusiResult(BusiStatus.SERVERERROR);
    }

    @RequestMapping("initSource")
    @ResponseBody
    public BusiResult initSource() {
        recomService.initCharmRecomSource();
        return new BusiResult(BusiStatus.SUCCESS);
    }

}
