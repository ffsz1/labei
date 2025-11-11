package com.erban.admin.main.service.charge;

import com.erban.admin.main.service.base.BaseService;
import com.erban.main.model.RedeemCode;
import com.erban.main.model.RedeemCodeExample;
import com.erban.main.mybatismapper.RedeemCodeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.utils.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service("redeemCodeService2")
public class RedeemCodeService extends BaseService {

    @Autowired
    private RedeemCodeMapper redeemCodeMapper;

    /**
     * 查询兑换码，分页返回
     *
     * @param code
     * @param uid
     * @param status
     * @return
     */
    public PageInfo<RedeemCode> getRedeemCodeList(String code, Long uid, Integer status, Integer pageNum,Integer pageSize) {
        RedeemCodeExample example = new RedeemCodeExample();
        example.setOrderByClause("create_time desc");
        RedeemCodeExample.Criteria criteria = example.createCriteria();
        if (!BlankUtil.isBlank(code)) {
            criteria.andCodeEqualTo(code);
        }
        if (uid != null) {
            criteria.andUseUidEqualTo(uid);
        }
        if (status != null) {
            criteria.andUseStatusEqualTo(status);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<RedeemCode> list = redeemCodeMapper.selectByExample(example);
        return new PageInfo<>(list);
    }

    public int insertRedeemCode(RedeemCode redeemCode) {
        return redeemCodeMapper.insertSelective(redeemCode);
    }

    // 数字字符加多一倍，平衡数字跟字母出现的概率
    private static final String[] CODE_STR = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J"
            ,"K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","0","1","2","3","4","5","6","7","8","9"};

    /**
     * 构建随机生成的字符串
     *
     * @param len 指定生成的字符串长度
     * @return
     */
    public static String buildRandomCode(int len) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            builder.append(CODE_STR[random.nextInt(CODE_STR.length)]);
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(buildRandomCode(16));
        }
    }
}
