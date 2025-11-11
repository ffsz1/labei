package com.erban.main.mybatismapper;


public interface UserPurseMapperMgr {

    int updateAddChargeGold(Long uid,Long goldNum);

    int updateAddNobleGold(Long uid,Long goldNum);

    /**
     * 扣减普通金币
     *
     * @param uid
     * @param goldNum
     * @return
     */
    int updateMinusChargeGold(Long uid,Long goldNum);

    /**
     * 扣减金币，首先扣减贵族金币，当贵族金币不足时，再减去普通金币
     *
     * @param uid
     * @param goldNum
     * @return
     */
    int updateMinusGold(Long uid,Long goldNum, Long chargeGold, Long nobleGold);

    /**
     * 扣减贵族金币
     *
     * @param uid
     * @param goldNum
     * @return
     */
    int updateMinusNobleGold(Long uid,Long goldNum);

    int updateAddDiamond(Long uid,double diamondNum);

    int updateMinusDiamond(Long uid,double diamondNum);

    /**
     * 兑换钻石为金币
     *
     * @param uid
     * @param diamonNum
     * @param goldNum
     * @return
     */
    int exchageDiamondToChargeGold(Long uid, double diamonNum, long goldNum);
    
}
