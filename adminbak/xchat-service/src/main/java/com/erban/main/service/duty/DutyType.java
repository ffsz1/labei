package com.erban.main.service.duty;

import com.erban.main.service.SpringAppContext;
import com.erban.main.util.StringUtils;

public enum DutyType {

    user_desc(1, "UserDescDutyService"),

    private_photo(2, "PhotoDutyService"),

    like(3, "FansDutyService"),

    speak_in_public(4, "PublicSpeakDutyService"),

    phone_bind(5, "PhoneDutyService"),

    wx_mon_share(6, ""),

    qq_space_share(7, ""),

    gift_draw(8, ""),

    gift_send(9, ""),

    charge(10, ""),;

    private int dutyId;

    private String dutyServiceName;

    DutyType(Integer dutyId, String dutyServiceName) {
        this.dutyId = dutyId;
        this.dutyServiceName = dutyServiceName;
    }

    public int getDutyId() {
        return dutyId;
    }

    public DutyResultService getDutyService() {
        if (StringUtils.isBlank(dutyServiceName)) {
            return null;
        }
        return SpringAppContext.getBean(dutyServiceName, DutyResultService.class);
    }

    public static DutyType dutyIdOf(Integer dutyId) {
        if (dutyId == null) {
            return null;
        }
        for (DutyType type : DutyType.values()) {
            if (type.dutyId == dutyId) {
                return type;
            }
        }
        return null;
    }

}
