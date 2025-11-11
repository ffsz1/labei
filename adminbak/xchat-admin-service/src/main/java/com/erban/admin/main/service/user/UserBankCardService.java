package com.erban.admin.main.service.user;

import com.erban.admin.main.dto.UserBankCardDTO;
import com.erban.admin.main.mapper.UserMapperExpand;
import com.erban.admin.main.service.system.AdminUserService;
import com.erban.main.model.UserBankCard;
import com.erban.main.model.UserBankCardExample;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.UserBankCardMapper;
import com.erban.main.service.user.UsersService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserBankCardService {

    private final Logger log = LoggerFactory.getLogger(UserBankCardService.class);
    @Autowired
    private UserBankCardMapper userBankCardMapper;
    @Autowired
    private UserMapperExpand userMapperExpand;
    @Autowired
    private UsersService usersService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AdminUserService adminUserService;

    public PageInfo<UserBankCardDTO> findUserBankCard(String noList, int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        PageHelper.startPage(pageNumber, pageSize);
        String[] erbanNoList = null;
        if (StringUtils.isNotBlank(noList)) {
            erbanNoList = noList.split(",");
        }
        List<UserBankCardDTO> list = userMapperExpand.findUserBankCard(erbanNoList);
        return new PageInfo<>(list);
    }

    public BusiResult save(UserBankCardDTO userBankCardDTO) {
        UserBankCard userBankCard;
        if (userBankCardDTO.getId() == null) {
            Users users = usersService.getUsersByErBanNo(userBankCardDTO.getErbanNo());
            if (users == null) {
                return new BusiResult(BusiStatus.USERNOTEXISTS);
            }
            UserBankCardExample example = new UserBankCardExample();
            example.createCriteria().andUidEqualTo(users.getUid()).andBankCardEqualTo(userBankCardDTO.getBankCard());
            List<UserBankCard> list = userBankCardMapper.selectByExample(example);
            if (list != null && list.size() > 0) {
                return new BusiResult(BusiStatus.ROBOT_NOTEXIT, "该用户已经绑定这张银行卡", "");
            }
            userBankCard = new UserBankCard();
            BeanUtils.copyProperties(userBankCardDTO, userBankCard);
            userBankCard.setUid(users.getUid());
            userBankCard.setCreateTime(new Date());
            userBankCard.setIsUse(false);
            userBankCardMapper.insert(userBankCard);
        } else {
            UserBankCardExample example = new UserBankCardExample();
            example.createCriteria().andUidEqualTo(userBankCardDTO.getUid()).andBankCardEqualTo(userBankCardDTO.getBankCard()).andIdNotEqualTo(userBankCardDTO.getId());
            List<UserBankCard> list = userBankCardMapper.selectByExample(example);
            if (list != null && list.size() > 0) {
                return new BusiResult(BusiStatus.ROBOT_NOTEXIT, "该用户已经绑定这张银行卡", "");
            }
            userBankCard = new UserBankCard();
            BeanUtils.copyProperties(userBankCardDTO, userBankCard);
            userBankCardMapper.updateByPrimaryKeySelective(userBankCard);
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public BusiResult use(Integer id) {
        UserBankCard userBankCard = userBankCardMapper.selectByPrimaryKey(id);
        if (userBankCard != null) {
            jdbcTemplate.update("update user_bank_card set is_use = 0 where uid = ?", userBankCard.getUid());
            userBankCard.setIsUse(true);
            userBankCardMapper.updateByPrimaryKeySelective(userBankCard);
            return new BusiResult(BusiStatus.SUCCESS);
        }
        return new BusiResult(BusiStatus.NOTEXISTS);
    }

    public BusiResult getByUid(Integer id) {
        UserBankCard userBankCard = userBankCardMapper.selectByPrimaryKey(id);
        if (userBankCard != null) {
            UserBankCardDTO userBankCardDTO = new UserBankCardDTO();
            BeanUtils.copyProperties(userBankCard, userBankCardDTO);
            Users users = usersService.getUsersByUid(userBankCard.getUid());
            if (users == null) {
                return new BusiResult(BusiStatus.USERNOTEXISTS);
            }
            userBankCardDTO.setErbanNo(users.getErbanNo());
            return new BusiResult(BusiStatus.SUCCESS, userBankCardDTO);
        }
        return new BusiResult(BusiStatus.NOTEXISTS);
    }

    public BusiResult delete(Integer id, Integer adminId) {
        UserBankCard userBankCard = userBankCardMapper.selectByPrimaryKey(id);
        log.info("删除银行信息操作:" + adminUserService.getAdminUserById(adminId).getUsername() + "执行删除 UID为:["+userBankCard.getUid()+"] 的银行卡信息：[" + userBankCard.getBankCardName() +"]  [" + userBankCard.getBankCard() + "] ["+userBankCard.getOpenBankCode()+"]" + "操作人:" + adminUserService.getAdminUserById(adminId).getUsername());
        if (userBankCard != null) {
            String delete = "delete from user_bank_card where id = ?";
            jdbcTemplate.update(delete,id);
            if(userBankCard.getIsUse() == true){
                UserBankCardExample example = new UserBankCardExample();
                example.createCriteria().andUidEqualTo(userBankCard.getUid());
                userBankCard = userBankCardMapper.selectByExample(example).get(0);
                jdbcTemplate.update("update user_bank_card set is_use = 1 where id = ?", userBankCard.getId());
            }
            return new BusiResult(BusiStatus.SUCCESS);
        }
        return new BusiResult(BusiStatus.NOTEXISTS);
    }
}
