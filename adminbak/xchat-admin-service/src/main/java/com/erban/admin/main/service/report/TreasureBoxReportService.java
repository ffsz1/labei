package com.erban.admin.main.service.report;

import com.erban.admin.main.dto.TreasureBoxReportDTO;
import com.erban.admin.main.mapper.TreasureBoxMapperMgr;
import com.erban.admin.main.service.base.BaseService;
import com.erban.admin.main.service.room.conf.DrawGiftKey;
import com.erban.admin.main.vo.TreasureBoxVo;
import com.erban.main.model.UserDrawWhitelist;
import com.erban.main.model.UserDrawWhitelistExample;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.UserDrawGiftRecordMapper;
import com.erban.main.mybatismapper.UserDrawWhitelistMapper;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;

@Service
public class TreasureBoxReportService extends BaseService {
    @Autowired
    private TreasureBoxMapperMgr treasureBoxMapperMgr;

    @Autowired
    private UserDrawGiftRecordMapper userDrawGiftRecordMapper;

    @Autowired
    private UserDrawWhitelistMapper userDrawWhitelistMapper;

    @Autowired
    private UsersService usersService;

    /**
     * 获取报表列表
     *
     * @param pageSize      每页大小
     * @param pageNum       页码
     * @param os            操作系统
     * @param gender        性别
     * @param reportDate    开始时间
     * @param reportEndDate 结束时间
     * @param erbanNos       拉贝号
     * @param giftType      礼物类型
     * @param order         排序
     * @return
     */
    public BusiResult getList(Integer pageSize, Integer pageNum, String os, Byte gender, String reportDate,
                              String reportEndDate, String erbanNos, Byte giftType, String order) {
        if (StringUtils.isBlank(order)) {
            PageHelper.startPage(pageNum, pageSize);
        } else {
            PageHelper.startPage(pageNum, pageSize, "rate " + order);
        }

        Map<String, Object> map = new HashMap<>(16);
        if (StringUtils.isNotBlank(os)) {
            map.put("os", os);
        }

        if (gender != null) {
            map.put("gender", gender);
        }

        if (StringUtils.isNotBlank(reportDate) && StringUtils.isNotBlank(reportEndDate)) {
            map.put("startDate", reportDate + " 00:00:00");
            map.put("endDate", reportEndDate + " 23:59:59");
        }

        if (!StringUtils.isBlank(erbanNos)) {
            String[] erbanNoArray = erbanNos.split("\n");
            List<Long> erbanList = new ArrayList<>();
            for (int i = 0; i < erbanNoArray.length; i++) {
                erbanList.add(Long.valueOf(erbanNoArray[i]));
            }
            map.put("erbanNos", erbanList);
        }

        if (giftType != null) {
            map.put("giftType", giftType);
        }

        List<TreasureBoxVo> treasureBoxVoList = treasureBoxMapperMgr.selectByExample(map);
        if (treasureBoxVoList != null && treasureBoxVoList.size() > 0) {
            treasureBoxVoList.forEach(item -> {
                // NumberFormat numberFormat = NumberFormat.getInstance();
                // numberFormat.setMaximumFractionDigits(3);
                // numberFormat.setRoundingMode(RoundingMode.HALF_UP);
                // String result = numberFormat.format(item.getRate());
                item.setRate(item.getRate() + "%");
            });

            TreasureBoxVo treasureBoxVo = treasureBoxVoList.get(0);
            TreasureBoxVo temp = this.treasureBoxMapperMgr.selectByCount(map);
            treasureBoxVo.setUserNum(temp.getUserNum());
            // 总投入
            treasureBoxVo.setTotalInputNum(temp.getTotalInputNum());
            // 总产出
            treasureBoxVo.setTotalOutputNum(temp.getTotalOutputNum());

            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(3);
            numberFormat.setRoundingMode(RoundingMode.HALF_UP);
            String result =
                    numberFormat.format((float) temp.getTotalOutputNum() / (float) temp.getTotalInputNum() * 100);
            treasureBoxVo.setAverageRate(result + "%");
            Integer num = userDrawGiftRecordMapper.selectUserDrawGiftRecord(map);
            treasureBoxVoList.get(0).setFullNum(num);

            // if (order == 1) {
            //     treasureBoxVoList.sort((o1, o2) -> {
            //         double num1 = Double.valueOf(o1.getRate().substring(0, o1.getRate().indexOf("%")));
            //         double num2 = Double.valueOf(o2.getRate().substring(0, o2.getRate().indexOf("%")));
            //         if (num1 >= num2) {
            //             return 1;
            //         } else {
            //             return -1;
            //         }
            //     });
            // } else if (order == 2) {
            //     treasureBoxVoList.sort((o1, o2) -> {
            //         double num1 = Double.valueOf(o1.getRate().substring(0, o1.getRate().indexOf("%")));
            //         double num2 = Double.valueOf(o2.getRate().substring(0, o2.getRate().indexOf("%")));
            //         if (num1 >= num2) {
            //             return -1;
            //         } else {
            //             return 1;
            //         }
            //     });
            // }
        } else {
            TreasureBoxVo treasureBoxVo = new TreasureBoxVo();
            treasureBoxVo.setAverageRate("0%");
            treasureBoxVo.setRate("0%");
            treasureBoxVo.setFullNum(0);
            treasureBoxVo.setTotalInputNum(0L);
            treasureBoxVo.setTotalOutputNum(0L);
            treasureBoxVo.setUserNum(0);
            treasureBoxVoList = Lists.newLinkedList();
            treasureBoxVoList.add(treasureBoxVo);
        }

        return new BusiResult(BusiStatus.SUCCESS, new PageInfo<>(treasureBoxVoList));
    }

    /**
     * 查询拉贝信息
     *
     * @return
     */
    public BusiResult drawGift() {
        Map<String, Object> map = Maps.newHashMap();
        String total = jedisService.hget(RedisKey.gift_draw_num.getKey(), DrawGiftKey.total.toString());
        String output = jedisService.hget(RedisKey.gift_draw_output.getKey(), DrawGiftKey.total.toString());
        String giftNum = jedisService.hget(RedisKey.gift_draw_output.getKey(), DrawGiftKey.full_gift.toString());
        long userNum = jedisService.hlen(RedisKey.gift_draw_output.getKey());
        map.put("totalGold", total);
        map.put("outputGold", output);
        map.put("giftNum", giftNum);
        map.put("userNum", userNum - 2);
        return new BusiResult(BusiStatus.SUCCESS, map);
    }

    public List<TreasureBoxReportDTO> getExportList(String reportStartDate, String reportEndDate, String erbanNos, Byte giftType) {
        List<TreasureBoxReportDTO> treasureBoxReportDTOS = Lists.newArrayList();
        Map<String, Object> map = new HashMap<>(16);
        if (StringUtils.isNotBlank(erbanNos)) {
            String[] erbanNoArray = erbanNos.split("\r\n");
            List<Long> erbanList = new ArrayList<>();
            for (int i = 0; i < erbanNoArray.length; i++) {
                erbanList.add(Long.valueOf(erbanNoArray[i]));
            }
            map.put("erbanNos", erbanList);
        }

        if (StringUtils.isNotBlank(reportStartDate) && StringUtils.isNotBlank(reportEndDate)) {
            map.put("startDate", reportStartDate + " 00:00:00");
            map.put("endDate", reportEndDate + " 23:59:59");
        }

        if (giftType != null) {
            map.put("giftType", giftType);
        }

        List<TreasureBoxVo> treasureBoxVoList = treasureBoxMapperMgr.selectByExportExample(map);
        treasureBoxVoList.stream().forEach(item -> {
            // NumberFormat numberFormat = NumberFormat.getInstance();
            // numberFormat.setMaximumFractionDigits(2);
            // numberFormat.setRoundingMode(RoundingMode.HALF_UP);
            // String result = numberFormat.format(item.getRate());
            TreasureBoxReportDTO treasureBoxReportDTO = new TreasureBoxReportDTO();
            treasureBoxReportDTO.setErBanNo(item.getErbanNo());
            treasureBoxReportDTO.setNick(item.getNick());
            treasureBoxReportDTO.setInputGold(item.getInputNum());
            treasureBoxReportDTO.setOutPutGold(item.getOutputNum());
            treasureBoxReportDTO.setTime(item.getCreateTime());
            treasureBoxReportDTO.setRate(item.getRate() + "%");
            treasureBoxReportDTOS.add(treasureBoxReportDTO);
        });
        return treasureBoxReportDTOS;
    }

    public BusiResult getWhitelistList(Integer pageSize, Integer pageNum, String reportStartDate,
                                       String reportEndDate, String erbanNo) {
        Long uid = null;
        if (StringUtils.isNotBlank(erbanNo)) {
            uid = usersService.getUsersByErBanNo(Long.valueOf(erbanNo)).getUid();
        }
        List<Long> uidList = userDrawWhitelistMapper.selectByUids(uid);
        Map<String, Object> map = new HashMap<>(16);
        if (uidList.size() == 0) {
            return new BusiResult(BusiStatus.NOTHAVINGLIST);
        }
        if (StringUtils.isNotBlank(reportStartDate) && StringUtils.isNotBlank(reportEndDate)) {
            map.put("startDate", reportStartDate + " 00:00:00");
            map.put("endDate", reportEndDate + " 23:59:59");
        }
        map.put("uids", uidList);
        PageHelper.startPage(pageNum, pageSize);
        List<TreasureBoxVo> treasureBoxVoList = treasureBoxMapperMgr.selectByUsersWhitelisExample(map);
        if (treasureBoxVoList.size() > 0) {
            treasureBoxVoList.forEach(item -> {
                Users users = usersService.getUsersByUid(item.getUid());
                NumberFormat numberFormat = NumberFormat.getInstance();
                numberFormat.setMaximumFractionDigits(2);
                numberFormat.setRoundingMode(RoundingMode.HALF_UP);
                String result = numberFormat.format((float) item.getOutputNum() / (float) item.getInputNum() * 100);
                item.setRate(result + "%");
                item.setErbanNo(users.getErbanNo());
                item.setNick(users.getNick());
                item.setUid(users.getUid());
            });

            TreasureBoxVo treasureBoxVo = treasureBoxVoList.get(0);
            TreasureBoxVo temp = this.treasureBoxMapperMgr.selectByWhitelistCount(map);
            treasureBoxVo.setUserNum(temp.getUserNum());
            // 总投入
            treasureBoxVo.setTotalInputNum(temp.getTotalInputNum());
            // 总产出
            treasureBoxVo.setTotalOutputNum(temp.getTotalOutputNum());
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setRoundingMode(RoundingMode.HALF_UP);
            String result =
                    numberFormat.format((float) temp.getTotalOutputNum() / (float) temp.getTotalInputNum() * 100);
            treasureBoxVo.setAverageRate(result + "%");
            Integer num = userDrawGiftRecordMapper.selectUserDrawWhitelistGiftRecord(map);
            treasureBoxVo.setNum(num);
            return new BusiResult(BusiStatus.SUCCESS, new PageInfo<>(treasureBoxVoList));
        } else {
            TreasureBoxVo treasureBoxVo = new TreasureBoxVo();
            treasureBoxVo.setUserNum(0);
            treasureBoxVo.setTotalInputNum(0L);
            treasureBoxVo.setTotalOutputNum(0L);
            treasureBoxVo.setAverageRate(0 + "%");
            treasureBoxVo.setNum(0);
            return new BusiResult(BusiStatus.NOTHAVINGLIST);
        }
    }

    public BusiResult add(Long erbanNo, int adminId) {
        Users users = usersService.getUsresByErbanNo(erbanNo);
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        UserDrawWhitelistExample example = new UserDrawWhitelistExample();
        example.createCriteria().andUidEqualTo(users.getUid());
        List<UserDrawWhitelist> usersWithdrawWhitelists = userDrawWhitelistMapper.selectByExample(example);
        if (usersWithdrawWhitelists.size() > 0) {
            return new BusiResult(BusiStatus.USERS_WITHDRAW_WHITELIST_ISEXISTS_ERROR);
        }
        UserDrawWhitelist userDrawWhitelist = new UserDrawWhitelist();
        userDrawWhitelist.setAdminId(adminId);
        userDrawWhitelist.setCreateTime(new Date());
        userDrawWhitelist.setUid(users.getUid());
        int result = userDrawWhitelistMapper.insert(userDrawWhitelist);
        if (result > 0) {
            return new BusiResult(BusiStatus.SUCCESS);
        }
        return new BusiResult(BusiStatus.SERVERERROR);
    }

    public BusiResult delete(Long uid) {
        UserDrawWhitelistExample example = new UserDrawWhitelistExample();
        example.createCriteria().andUidEqualTo(uid);
        UserDrawWhitelist userDrawWhitelist = userDrawWhitelistMapper.selectByExample(example).get(0);
        int status = userDrawWhitelistMapper.deleteByPrimaryKey(userDrawWhitelist.getId());
        if (status > 0) {
            return new BusiResult(BusiStatus.SUCCESS);
        }
        return new BusiResult(BusiStatus.SERVERERROR);
    }

    public List<TreasureBoxReportDTO> getExportWhitelist(String reportStartDate, String reportEndDate, String erbanNo) {
        Long uid = null;
        List<TreasureBoxReportDTO> treasureBoxReportDTOS = Lists.newArrayList();
        Map<String, Object> map = new HashMap<>(16);
        if (StringUtils.isNotBlank(erbanNo)) {
            Users users = usersService.getUsersByErBanNo(Long.valueOf(erbanNo));
            uid = users.getUid();
        }
        List<Long> uidList = userDrawWhitelistMapper.selectByUids(uid);
        if (StringUtils.isNotBlank(reportStartDate) && StringUtils.isNotBlank(reportEndDate)) {
            map.put("startDate", reportStartDate + " 00:00:00");
            map.put("endDate", reportEndDate + " 23:59:59");
        }
        map.put("uids", uidList);
        List<TreasureBoxVo> treasureBoxVoList = treasureBoxMapperMgr.selectByExportWhitelistExample(map);
        treasureBoxVoList.stream().forEach(item -> {
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setRoundingMode(RoundingMode.HALF_UP);
            String result = numberFormat.format((float) item.getOutputNum() / (float) item.getInputNum() * 100);
            TreasureBoxReportDTO treasureBoxReportDTO = new TreasureBoxReportDTO();
            treasureBoxReportDTO.setErBanNo(item.getErbanNo());
            treasureBoxReportDTO.setNick(item.getNick());
            treasureBoxReportDTO.setInputGold(item.getInputNum());
            treasureBoxReportDTO.setOutPutGold(item.getOutputNum());
            treasureBoxReportDTO.setTime(item.getCreateTime());
            treasureBoxReportDTO.setRate(result + "%");
            treasureBoxReportDTOS.add(treasureBoxReportDTO);
        });
        return treasureBoxReportDTOS;
    }
}
