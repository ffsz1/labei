package com.erban.main.service;

import com.erban.main.model.Feedback;
import com.erban.main.mybatismapper.FeedbackMapper;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.UserVo;
import com.xchat.common.UUIDUitl;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by 北岭山下 on 2017/7/18.
 */
@Service
public class FeedbackService {

        @Autowired
        private UsersService usersService;
        @Autowired
        private FeedbackMapper feedbackMapper;

        private static final Logger LOGGER = LoggerFactory.getLogger ( FeedbackService.class );
        public BusiResult feedbackUpdate ( Long uid, String feedbackDesc, String img,String contact){


                //判断用户是否存在
                //==============判断用户是否存在======================//
                UserVo userVo = usersService.getUserByUid ( uid ).getData ();
                if(userVo == null){
                        LOGGER.warn ( "用户："+uid+" ==============用户不存在==================" );
                        return new BusiResult( BusiStatus.USERNOTEXISTS);
                }
                //创建feedbackID
                String feedbackId = UUIDUitl.get ();
                //写入feedback相关信息
                Feedback feedback = new Feedback ();
                feedback.setFeedbackDesc ( feedbackDesc );
                feedback.setFeedbackId ( feedbackId );
                feedback.setImgUrl ( img );
                feedback.setContact ( contact );
                feedback.setUid ( uid );
                try{
                        insertFeedback (feedback);
                }catch(Exception e){
                        LOGGER.error (  "用户："+uid+"==============反馈失败=================" );
                        return new BusiResult ( BusiStatus.BUSIERROR );
                }
                return new BusiResult ( BusiStatus.SUCCESS );


        }
        //feedback创建(插入一条Feedback)
        private void insertFeedback ( Feedback feedback){
                feedback.setCreateTime ( new Date (  ) );
                feedbackMapper.insertSelective ( feedback );
        }
        //
        //更新一条Feedback
        /*
        private  int updateFeedBack(Feedback feedback){

        }*/
        //获取一条Feedback
        private Feedback getFeedbackById(String feedbackId){
                return feedbackMapper.selectByPrimaryKey ( feedbackId );
        }
        //
        //获取所有的Feedback信息
        /*
        private List<Feedback>getAllFeedBackList(){
        }*/


}
