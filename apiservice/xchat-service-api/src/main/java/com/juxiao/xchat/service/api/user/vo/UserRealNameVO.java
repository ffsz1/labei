package com.juxiao.xchat.service.api.user.vo;

import com.juxiao.xchat.base.utils.DateFormatUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

@Data
@ApiModel(description = "实名认证返回结果")
public class UserRealNameVO {
    @ApiModelProperty(value = "用户实名")
    private String realName;

    @ApiModelProperty(value = "用户身份证号码")
    private String idcardNo;

    @ApiModelProperty(value = "用户实名认证审核状态：0，审核中；1，审核通过；2，审核拒绝")
    private Byte auditStatus;

    @ApiModelProperty(value = "实名认证时间")
    private Date createDate;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "审核不通过原因")
    private String remark;

    // public String getRealName() {
    //     if (StringUtils.isBlank(realName)) {
    //         return realName;
    //     }
    //     if (realName.length() == 1) {
    //         return "*";
    //     }
    //     if (realName.length() == 2) {
    //         return StringUtils.leftPad(StringUtils.right(realName, 1), StringUtils.length(realName), "*");
    //     }
    //
    //     String desensitize = StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(realName, 1),
    //     StringUtils.length(realName), "*"), "*");
    //     return StringUtils.left(realName, 1).concat(desensitize);
    // }

    public String getIdcardNo() {
        if (StringUtils.isBlank(idcardNo)) {
            return idcardNo;
        }
        String desensitize = StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(idcardNo, 4),
                StringUtils.length(idcardNo), "*"), "****");
        return StringUtils.left(idcardNo, 4).concat(desensitize);
    }

    public Byte getAuditStatus() {
        return auditStatus;
    }

    public String getCreateDate() {
        return DateFormatUtils.YYYY_MM_DD.date2Str(createDate);
    }

    public String getPhone() {
        if (StringUtils.isNotBlank(phone) && phone.length() >= 11) {
            return StringUtils.left(phone, 4)
                    .concat(StringUtils.leftPad(StringUtils.right(phone, 4), StringUtils.length(phone) - 4, "*"));
        }
        return phone;
    }
}
