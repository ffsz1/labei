package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.domain.FeedbackDO;
import org.apache.ibatis.annotations.Insert;

/**
 * @class: FeedbackDao.java
 * @author: chenjunsheng
 * @date 2018/6/13
 */
public interface FeedbackDao {

    /**
     * 保存用户反馈信息
     *
     * @param feedbackDo
     */
    @TargetDataSource
    @Insert("INSERT INTO `feedback` (`feedback_id`,`uid`,`feedback_desc`,`img_url`,`contact`,`create_time`)VALUES(#{feedbackId,jdbcType=VARCHAR},#{uid,jdbcType=BIGINT},#{feedbackDesc,jdbcType=VARCHAR},#{imgUrl,jdbcType=VARCHAR},#{contact,jdbcType=VARCHAR},#{createTime,jdbcType=TIMESTAMP})")
    void save(FeedbackDO feedbackDo);

}
