package com.erban.web.controller;


import com.erban.main.service.RankService;
import com.erban.main.vo.RankHomeVo;
import com.erban.main.vo.RankMineVo;
import com.erban.main.vo.RankParentVo;
import com.erban.main.vo.RankVo;
import com.google.common.collect.Lists;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/allrank")
public class RankController {
    public static final Logger logger = LoggerFactory.getLogger(RankController.class);
    @Autowired
    private RankService rankService;

    @ResponseBody
    @SignVerification
    @RequestMapping(value = "/ranklist", method = RequestMethod.GET)
    public BusiResult doRankList(int type,int datetype){
        rankService.doAllKindRankHomeVoJob();
        return new BusiResult(BusiStatus.SUCCESS,rankService.getRankListFromDb(type, datetype));
    }


    /**
     *  查询各类礼物排行榜信息，H5页专用
     *
     * @param uidEmpireLight
     * @param type 排行榜类型：1巨星榜；2贵族榜；3房间榜
     * @param datetype 	榜单统计周期类型：1、日榜；2周榜；3总榜
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/geth5", method = RequestMethod.GET)
    public BusiResult<RankParentVo> getH5RankParentVoList(Long uid,int type,int datetype,Integer pageSize){
        BusiResult busiResult=rankService.getH5RankParentVoList(uid,type,datetype,pageSize);
        return busiResult;
    }

    /**
     *  查询上周土豪排行榜信息，H5页专用
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getLastRank", method = RequestMethod.GET)
    public BusiResult<RankParentVo> getLastRankVoList(Long uid,Integer pageSize){
        BusiResult busiResult=rankService.getLastRankVoList(uid,pageSize);
        return busiResult;
    }

    @ResponseBody
    @SignVerification
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public BusiResult<RankHomeVo> getRankHomeVo(){
        BusiResult busiResult= new BusiResult(BusiStatus.SUCCESS);
        try {
            busiResult.setData(new RankHomeVo());
//            busiResult = rankService.getRankHomeVo();
        } catch (Exception e) {
            logger.error("getRankHomeVo error..",e);
        }
        return busiResult;
    }

    /**
     * 获取礼物排行榜信息，展示在APP首页
     *
     * @return
     */
    @ResponseBody
    @SignVerification
    @RequestMapping(value = "/homeV2", method = RequestMethod.GET)
    public BusiResult<RankHomeVo> getRankHomeVoV2(){
        BusiResult busiResult= new BusiResult(BusiStatus.SUCCESS);
        try {
            busiResult = rankService.getRankHomeVo();
        } catch (Exception e) {
            logger.error("getRankHomeVo error..",e);
        }
        return busiResult;
    }

    @ResponseBody
    @SignVerification
    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public BusiResult<RankHomeVo> refresh(){
        BusiResult busiResult= new BusiResult(BusiStatus.SUCCESS);
        try {
            rankService.doAllKindRankHomeVoJob();
        } catch (Exception e) {
            logger.error("getRankHomeVo error..",e);
        }
        return busiResult;
    }

    private RankHomeVo bulidMock(){
        RankHomeVo rankHomeVo=new RankHomeVo();



        List<RankVo> starList= Lists.newArrayList();
        List<RankVo> nobleList= Lists.newArrayList();
        List<RankVo> roomList= Lists.newArrayList();
        RankVo rankVo1=new RankVo();
        rankVo1.setErbanNo(850379L);
        rankVo1.setAvatar("https://nos.netease.com/nim/NDI3OTA4NQ==/bmltYV83Nzg4MzQyNDJfMTUwNzIwMzM4NTc2N184YmJlZmQ4ZS01YzdiLTRmMWMtYmFhOC0xMWRlNzg3MzRlMzk=");
        rankVo1.setNick("阿拉善");
        rankVo1.setGender(new Byte("1"));
        rankVo1.setTotalNum(9988923);

        RankVo rankVo2=new RankVo();
        rankVo2.setErbanNo(850379L);
        rankVo2.setAvatar("https://nos.netease.com/nim/NDI3OTA4NQ==/bmltYV83Nzg4MzQyNDJfMTUwNzIwMzM4NTc2N184YmJlZmQ4ZS01YzdiLTRmMWMtYmFhOC0xMWRlNzg3MzRlMzk=");
        rankVo2.setNick("阿拉善2");
        rankVo2.setGender(new Byte("1"));
        rankVo2.setTotalNum(99889);


        RankVo rankVo3=new RankVo();
        rankVo3.setErbanNo(850379L);
        rankVo3.setAvatar("https://nos.netease.com/nim/NDI3OTA4NQ==/bmltYV83Nzg4MzQyNDJfMTUwNzIwMzM4NTc2N184YmJlZmQ4ZS01YzdiLTRmMWMtYmFhOC0xMWRlNzg3MzRlMzk=");
        rankVo3.setNick("阿拉善3");
        rankVo3.setGender(new Byte("1"));
        rankVo3.setTotalNum(9988);

        RankVo rankVo4=new RankVo();
        rankVo4.setErbanNo(850379L);
        rankVo4.setAvatar("https://nos.netease.com/nim/NDI3OTA4NQ==/bmltYV83Nzg4MzQyNDJfMTUwNzIwMzM4NTc2N184YmJlZmQ4ZS01YzdiLTRmMWMtYmFhOC0xMWRlNzg3MzRlMzk=");
        rankVo4.setNick("阿拉善4");
        rankVo4.setGender(new Byte("2"));
        rankVo4.setTotalNum(998);
        starList.add(rankVo1);
        starList.add(rankVo2);
        starList.add(rankVo3);
        starList.add(rankVo4);

        nobleList.add(rankVo1);
        nobleList.add(rankVo2);
        nobleList.add(rankVo3);
        nobleList.add(rankVo4);

        roomList.add(rankVo1);
        roomList.add(rankVo2);
        roomList.add(rankVo3);
        roomList.add(rankVo4);

        rankHomeVo.setNobleList(nobleList);
        rankHomeVo.setRoomList(roomList);
        rankHomeVo.setStarList(starList);
        return rankHomeVo;
    }


    private RankParentVo bulidMockH5(){
        RankParentVo rankParentVo=new RankParentVo();



        List<RankVo> starList= Lists.newArrayList();
        List<RankVo> nobleList= Lists.newArrayList();
        List<RankVo> roomList= Lists.newArrayList();
        RankVo rankVo1=new RankVo();
        rankVo1.setErbanNo(850379L);
        rankVo1.setAvatar("https://nos.netease.com/nim/NDI3OTA4NQ==/bmltYV83Nzg4MzQyNDJfMTUwNzIwMzM4NTc2N184YmJlZmQ4ZS01YzdiLTRmMWMtYmFhOC0xMWRlNzg3MzRlMzk=");
        rankVo1.setNick("阿拉善");
        rankVo1.setGender(new Byte("1"));
        rankVo1.setTotalNum(9988923);

        RankVo rankVo2=new RankVo();
        rankVo2.setErbanNo(850379L);
        rankVo2.setAvatar("https://nos.netease.com/nim/NDI3OTA4NQ==/bmltYV83Nzg4MzQyNDJfMTUwNzIwMzM4NTc2N184YmJlZmQ4ZS01YzdiLTRmMWMtYmFhOC0xMWRlNzg3MzRlMzk=");
        rankVo2.setNick("阿拉善2");
        rankVo2.setGender(new Byte("1"));
        rankVo2.setTotalNum(99889);


        RankVo rankVo3=new RankVo();
        rankVo3.setErbanNo(850379L);
        rankVo3.setAvatar("https://nos.netease.com/nim/NDI3OTA4NQ==/bmltYV83Nzg4MzQyNDJfMTUwNzIwMzM4NTc2N184YmJlZmQ4ZS01YzdiLTRmMWMtYmFhOC0xMWRlNzg3MzRlMzk=");
        rankVo3.setNick("阿拉善3");
        rankVo3.setGender(new Byte("1"));
        rankVo3.setTotalNum(9988);

        RankVo rankVo4=new RankVo();
        rankVo4.setErbanNo(850379L);
        rankVo4.setAvatar("https://nos.netease.com/nim/NDI3OTA4NQ==/bmltYV83Nzg4MzQyNDJfMTUwNzIwMzM4NTc2N184YmJlZmQ4ZS01YzdiLTRmMWMtYmFhOC0xMWRlNzg3MzRlMzk=");
        rankVo4.setNick("阿拉善4");
        rankVo4.setGender(new Byte("2"));
        rankVo4.setTotalNum(998);
        starList.add(rankVo1);
        starList.add(rankVo2);
        starList.add(rankVo3);
        starList.add(rankVo4);

        nobleList.add(rankVo1);
        nobleList.add(rankVo2);
        nobleList.add(rankVo3);
        nobleList.add(rankVo4);

        roomList.add(rankVo1);
        roomList.add(rankVo2);
        roomList.add(rankVo3);
        roomList.add(rankVo4);
        rankParentVo.setRankVoList(roomList);
        RankMineVo me=new RankMineVo();

        me.setErbanNo(850379L);
        me.setAvatar("https://nos.netease.com/nim/NDI3OTA4NQ==/bmltYV83Nzg4MzQyNDJfMTUwNzIwMzM4NTc2N184YmJlZmQ4ZS01YzdiLTRmMWMtYmFhOC0xMWRlNzg3MzRlMzk=");
        me.setNick("阿拉善3");
        me.setGender(new Byte("1"));
        me.setTotalNum(9988);
        me.setSeqNo(109);
        me.setReachGoldNum(2349L);
        rankParentVo.setMe(me);
        return rankParentVo;
    }
}
