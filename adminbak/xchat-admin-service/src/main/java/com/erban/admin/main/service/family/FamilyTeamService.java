package com.erban.admin.main.service.family;

import com.erban.admin.main.dto.FamilyTeamDTO;
import com.erban.admin.main.mapper.FamilyGiftRecordMapper;
import com.erban.admin.main.mapper.FamilyJoinMapper;
import com.erban.admin.main.mapper.FamilyTeamMapper;
import com.erban.admin.main.model.FamilyJoin;
import com.erban.admin.main.model.FamilyTeam;
import com.erban.main.model.Users;
import com.erban.main.model.UsersExample;
import com.erban.main.mybatismapper.UsersMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.netease.neteaseacc.NetEaseBaseClient;
import com.xchat.common.netease.neteaseacc.result.NetEaseTeamRet;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author laizhilong
 * @Title:
 * @Package com.erban.admin.main.service.family
 * @date 2018/9/3
 * @time 19:58
 */
@Service
public class FamilyTeamService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FamilyTeamMapper familyTeamMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private FamilyJoinMapper familyJoinMapper;

    @Autowired
    private FamilyGiftRecordMapper familyGiftRecordMapper;


    private static final String CREATE_TEAM = "https://api.netease.im/nimserver/team/create.action";

    private static final String REMOVE_TEAM = "https://api.netease.im/nimserver/team/remove.action";



    public PageInfo<FamilyTeamDTO> getListWithPage(String searchText,Integer type ,int pageNumber, int pageSize, String startDateStr, String endDateStr) {
        if (pageNumber < 1 || pageSize < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        int offset = (pageNumber - 1) * pageSize;
        PageHelper.startPage(pageNumber, pageSize);
        if(!startDateStr.isEmpty() && !endDateStr.isEmpty()){
            startDateStr = startDateStr + "  00:00:00";
            endDateStr = endDateStr + "  23:59:59";
        }
        List<FamilyTeamDTO> list = familyTeamMapper.selectByList(searchText,type,startDateStr,endDateStr);
        list.forEach(item ->{
            UsersExample usersExample = new UsersExample();
            usersExample.createCriteria().andUidEqualTo(item.getUid());
            Users users = usersMapper.selectByExample(usersExample).get(0);
            item.setErbanNo(users.getErbanNo());
            item.setNike(users.getNick());
        });
        return new PageInfo<>(list);
    }

    public BusiResult DisplaysStatus(Long teamId) {
        FamilyTeam familyTeam = familyTeamMapper.selectById(teamId);
        if(familyTeam!=null){
            if(familyTeam.getDisplay() == 2){
                familyTeam.setDisplay(1);
            }else {
                familyTeam.setDisplay(2);
            }
            familyTeamMapper.updateByDisplay(familyTeam.getId(),familyTeam.getDisplay());
            return new BusiResult(BusiStatus.SUCCESS);
        }else{
            return new BusiResult(BusiStatus.ADMIN_ROOM_NOTEXIT);
        }
    }


    public BusiResult updateCheckSuccessStatus(Long teamId) {
        FamilyTeam familyTeam = familyTeamMapper.selectById(teamId);
        if(familyTeam!=null){
            FamilyJoin familyJoin = familyJoinMapper.selectByUid(familyTeam.getUid());
            if(familyJoin == null){
                familyTeam.setStatus(1);
                try {
                    NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(CREATE_TEAM);
                    Map<String,Object> param = new HashMap<>(16);
                    param.put("tname", familyTeam.getFamilyName());
                    param.put("owner", familyTeam.getUid());
                    param.put("members", "["+familyTeam.getUid()+"]");
                    param.put("announcement", familyTeam.getFamilyNotice());
                    param.put("msg", "欢迎你加入家族!");
                    param.put("magree", 1);
                    param.put("joinmode", 0);
                    param.put("icon", familyTeam.getFamilyLogo());
                    param.put("beinvitemode", 0);
                    param.put("invitemode", 0);
                    param.put("uptinfomode", 0);
                    param.put("upcustommode", 0);
                    String resultStr = netEaseBaseClient.buildHttpPostParam(param).executePost();
                    Gson gson = new Gson();
                    NetEaseTeamRet netEaseTeamRet = gson.fromJson(resultStr, NetEaseTeamRet.class);
                    logger.info("调用云信创建家族群响应结果,{}",resultStr);
                    if(netEaseTeamRet.getCode() == 200){
                        logger.info("调用云信创建家族群解析响应结果,{}",netEaseTeamRet.getTid());
                        int status = familyTeamMapper.updateStatusAndRoomId(familyTeam.getId(),familyTeam.getStatus(),Long.valueOf(netEaseTeamRet.getTid()));
                        if(status > 0){
                            int checkStatus = 1;
                            familyJoin = new FamilyJoin();
                            familyJoin.setUid(familyTeam.getUid());
                            familyJoin.setTeamId(familyTeam.getId());
                            familyJoin.setRoleStatus(1);
                            familyJoin.setCreateTime(new Date());
                            int result = familyJoinMapper.save(familyJoin);
                            if(result > 0){
                                familyJoinMapper.updateByFamilyApplyJoinRecord(familyTeam.getId(),familyTeam.getUid(),checkStatus);
                            }
                        }
                        return new BusiResult(BusiStatus.SUCCESS);
                    }else{
                        return new BusiResult(BusiStatus.SERVERERROR);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    return new BusiResult(BusiStatus.SERVERERROR);
                }

            }else{
                return new BusiResult(BusiStatus.FAMILY_JOIN_USERS);
            }
        }else{
            return new BusiResult(BusiStatus.NOTHAVINGLIST);
        }
    }



    public BusiResult updateCheckFailureStatus(Long teamId) {
        FamilyTeam familyTeam = familyTeamMapper.selectById(teamId);
        if(familyTeam!=null){
            familyTeam.setStatus(2);
            familyTeamMapper.updateStatus(familyTeam.getId(),familyTeam.getStatus());
            return new BusiResult(BusiStatus.SUCCESS);
        }else{
            return new BusiResult(BusiStatus.ADMIN_ROOM_NOTEXIT);
        }
    }

    public FamilyTeam get(Long id) {
        return this.familyTeamMapper.selectById(id);
    }

    public int update(FamilyTeam familyTeam) {
        return this.familyTeamMapper.update(familyTeam);
    }

    public FamilyTeam getFamilyId(Long familyId) {
        return this.familyTeamMapper.getFamilyId(familyId);
    }

    /**
     * 解散家族
     * @param teamId
     * @return
     */
    public BusiResult disband(Long teamId) {
        FamilyTeam familyTeam = this.familyTeamMapper.selectById(teamId);
        if(familyTeam == null){
            return new BusiResult(BusiStatus.SERVERERROR);
        }
        try {
            NetEaseBaseClient netEaseBaseClient = new NetEaseBaseClient(REMOVE_TEAM);
            Map<String,Object> param = new HashMap<>(16);
            param.put("tid",familyTeam.getRoomId());
            param.put("owner",familyTeam.getUid());
            String resultStr = netEaseBaseClient.buildHttpPostParam(param).executePost();
            Gson gson = new Gson();
            NetEaseTeamRet netEaseTeamRet = gson.fromJson(resultStr, NetEaseTeamRet.class);
            logger.info("调用云信解散家族群响应结果,{}",resultStr);
            if(netEaseTeamRet.getCode() == 200){
                int status = familyTeamMapper.deleteByTeamId(familyTeam.getId());
                if(status > 0){
                    List<Long> uidList = familyJoinMapper.selectTeamIdByFamilyJoinList(familyTeam.getId());
                    if(uidList != null && uidList.size() > 0) {
                        familyJoinMapper.deleteByTeamId(familyTeam.getId());
                    }
                    List<Long> uids = familyGiftRecordMapper.selectTeamIdByFamilyGiftRecordList(familyTeam.getId());
                    if(uids != null && uids.size() > 0) {
                        familyGiftRecordMapper.deleteByTeamId(familyTeam.getId());
                    }
                    return new BusiResult(BusiStatus.SUCCESS);
                }
                return new BusiResult(BusiStatus.SUCCESS);
            }else{
                return new BusiResult(BusiStatus.SERVERERROR);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }
}
