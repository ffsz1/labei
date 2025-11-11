package com.juxiao.xchat.service.api.wish;

import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.wish.domain.Wish;
import com.juxiao.xchat.dao.wish.dto.WishDTO;
import com.juxiao.xchat.dao.wish.query.Page;
import com.juxiao.xchat.dao.wish.query.WishQuery;
import com.juxiao.xchat.service.api.wish.bo.PageBo;
import com.juxiao.xchat.service.api.wish.bo.WishBo;
import com.juxiao.xchat.service.api.wish.vo.PageInfo;
import com.juxiao.xchat.service.api.wish.vo.WishVo;

import java.util.List;

public interface WishService {

    /**
     * 保存和记录心愿
     * @param wishBo
     */
    int save(WishBo wishBo);

    /**
     * 获取 心愿列表 分页
     * @param wishQuery 查询条件
     * @param pageBo 分页参数
     * @return 分页信息
     */
    PageInfo<WishDTO> listWish(WishQuery wishQuery, PageBo pageBo);

    /**
     * 获取 心愿列表 分页  带个性标签
     * @param wishQuery 查询条件
     * @param pageBo 分页参数
     * @return 分页信息 带个性标签
     */
    List<WishVo> listWish2(WishQuery wishQuery, PageBo pageBo);

    /**
     * 查询 单个心愿
     * @param uid 用户id
     * @return
     */
    WebServiceMessage getWish(Long uid);
}
