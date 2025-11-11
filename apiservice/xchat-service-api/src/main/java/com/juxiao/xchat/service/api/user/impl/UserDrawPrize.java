package com.juxiao.xchat.service.api.user.impl;

import com.juxiao.xchat.base.constant.DrawProd;
import com.juxiao.xchat.base.spring.SpringAppContext;
import com.juxiao.xchat.base.utils.RandomUtils;
import com.juxiao.xchat.dao.user.UserDrawPrettyErbanNoDao;
import com.juxiao.xchat.dao.user.domain.UserDrawRecordDO;
import com.juxiao.xchat.dao.user.dto.UserDrawPrettyErbanNoDTO;
import com.juxiao.xchat.dao.user.dto.UserDrawRecordDTO;
import com.juxiao.xchat.dao.user.enumeration.UserDrawRecordStatus;
import org.slf4j.LoggerFactory;

import java.util.Date;

public enum UserDrawPrize {
    CHARGE_08(800, new double[]{0, 0.89, 0.99, 1}) {
        @Override
        public UserDrawRecordDO createDrawPrize(UserDrawRecordDTO recordDto) {
            UserDrawRecordDO recordDo = this.initRecordDo(recordDto.getRecordId(),recordDto.getType());
            double randomNumber = RandomUtils.randomDouble();
            LoggerFactory.getLogger(UserDrawPrize.class).info("[ 抽奖结果 ]随机数为：{}", randomNumber);
            if (randomNumber >= this.draw[1] && randomNumber <= this.draw[2]) {
                recordDo.setDrawPrizeId(DrawProd.gold8);
                recordDo.setDrawPrizeName(DrawProd.gold8 + "金币");
            } else if (randomNumber > this.draw[2] && randomNumber <= this.draw[3]) {
                recordDo.setDrawPrizeName(DrawProd.gold50 + "金币");
                recordDo.setDrawPrizeId(DrawProd.gold50);
            } else {
                recordDo.setDrawPrizeId(DrawProd.none);
                recordDo.setDrawPrizeName("谢谢参与，继续努力！");
                recordDo.setDrawStatus(UserDrawRecordStatus.NONE_PRIZE.getValue());
            }

            return recordDo;
        }
    },

    CHARGE_48(4800, new double[]{0, 0.41, 0.95, 1}) {
        @Override
        public UserDrawRecordDO createDrawPrize(UserDrawRecordDTO recordDto) {
            UserDrawRecordDO recordDo = this.initRecordDo(recordDto.getRecordId(),recordDto.getType());
            double randomNumber = RandomUtils.randomDouble();
            LoggerFactory.getLogger(UserDrawPrize.class).info("[ 抽奖结果 ]随机数为：{}", randomNumber);
            if (randomNumber > this.draw[1] && randomNumber <= this.draw[2]) {
                recordDo.setDrawPrizeId(DrawProd.gold8);
                recordDo.setDrawPrizeName(DrawProd.gold8 + "金币");
            } else if (randomNumber > this.draw[2] && randomNumber <= this.draw[3]) {
                recordDo.setDrawPrizeId(DrawProd.gold50);
                recordDo.setDrawPrizeName(DrawProd.gold50 + "金币");
            } else {
                recordDo.setDrawPrizeId(DrawProd.none);
                recordDo.setDrawPrizeName("谢谢参与，继续努力！");
                recordDo.setDrawStatus(UserDrawRecordStatus.NONE_PRIZE.getValue());
            }
            return recordDo;
        }
    },

    CHARGE_98(9800, new double[]{0, 0.24, 0.87, 0.98, 1}) {
        @Override
        public UserDrawRecordDO createDrawPrize(UserDrawRecordDTO recordDto) {
            UserDrawRecordDO recordDo = this.initRecordDo(recordDto.getRecordId(),recordDto.getType());
            double randomNumber = RandomUtils.randomDouble();

            if (randomNumber > this.draw[1] && randomNumber <= this.draw[2]) {
                recordDo.setDrawPrizeId(DrawProd.gold8);
                recordDo.setDrawPrizeName(DrawProd.gold8 + "金币");
            } else if (randomNumber > this.draw[2] && randomNumber <= this.draw[3]) {
                recordDo.setDrawPrizeId(DrawProd.gold50);
                recordDo.setDrawPrizeName(DrawProd.gold50 + "金币");
            } else if (randomNumber > this.draw[3] && randomNumber <= this.draw[4]) {
                recordDo.setDrawPrizeId(DrawProd.gold100);
                recordDo.setDrawPrizeName(DrawProd.gold100 + "金币");
            } else {
                recordDo.setDrawPrizeId(DrawProd.none);
                recordDo.setDrawPrizeName("谢谢参与，继续努力！");
                recordDo.setDrawStatus(UserDrawRecordStatus.NONE_PRIZE.getValue());
            }
            return recordDo;
        }
    },

    CHARGE_198(19800, new double[]{0, 0.72, 0.82, 0.97, 1}) {
        @Override
        public UserDrawRecordDO createDrawPrize(UserDrawRecordDTO recordDto) {
            UserDrawRecordDO recordDo = this.initRecordDo(recordDto.getRecordId(),recordDto.getType());
            double randomNumber = RandomUtils.randomDouble();
            if (randomNumber > this.draw[1] && randomNumber <= this.draw[2]) {
                recordDo.setDrawPrizeId(DrawProd.gold50);
                recordDo.setDrawPrizeName(DrawProd.gold50 + "金币");
            } else if (randomNumber > this.draw[2] && randomNumber <= this.draw[3]) {
                recordDo.setDrawPrizeId(DrawProd.gold100);
                recordDo.setDrawPrizeName(DrawProd.gold100 + "金币");
            } else if (randomNumber > this.draw[3] && randomNumber <= this.draw[4]) {
                recordDo.setDrawPrizeId(DrawProd.gold300);
                recordDo.setDrawPrizeName(DrawProd.gold300 + "金币");
            } else {
                recordDo.setDrawPrizeId(DrawProd.gold8);
                recordDo.setDrawPrizeName(DrawProd.gold8 + "金币");
            }
            return recordDo;
        }
    },

    CHARGE_498(49800, new double[]{0, 0.73, 0.88, 0.98, 1}) {
        @Override
        public UserDrawRecordDO createDrawPrize(UserDrawRecordDTO recordDto) {
            UserDrawRecordDO recordDo = this.initRecordDo(recordDto.getRecordId(),recordDto.getType());
            double randomNumber = RandomUtils.randomDouble();
            if (randomNumber > this.draw[1] && randomNumber <= this.draw[2]) {
                recordDo.setDrawPrizeId(DrawProd.gold100);
                recordDo.setDrawPrizeName(DrawProd.gold100 + "金币");
            } else if (randomNumber > this.draw[2] && randomNumber <= this.draw[3]) {
                recordDo.setDrawPrizeId(DrawProd.gold300);
                recordDo.setDrawPrizeName(DrawProd.gold300 + "金币");
            } else if (randomNumber > this.draw[3] && randomNumber <= this.draw[4]) {
                recordDo.setDrawPrizeId(DrawProd.gold1000);
                recordDo.setDrawPrizeName(DrawProd.gold1000 + "金币");
            } else {
                recordDo.setDrawPrizeId(DrawProd.gold50);
                recordDo.setDrawPrizeName(DrawProd.gold50 + "金币");
            }
            return recordDo;
        }
    },

    CHARGE_998(99800, new double[]{0, 0.57, 0.92, 0.99, 1}) {
        @Override
        public UserDrawRecordDO createDrawPrize(UserDrawRecordDTO recordDto) {
            UserDrawRecordDO recordDo = this.initRecordDo(recordDto.getRecordId(),recordDto.getType());
            double randomNumber = RandomUtils.randomDouble();
            if (randomNumber > this.draw[1] && randomNumber <= this.draw[2]) {
                recordDo.setDrawPrizeId(DrawProd.gold300);
                recordDo.setDrawPrizeName(DrawProd.gold300 + "金币");
            } else if (randomNumber > this.draw[2] && randomNumber <= this.draw[3]) {
                recordDo.setDrawPrizeId(DrawProd.gold1000);
                recordDo.setDrawPrizeName(DrawProd.gold1000 + "金币");
            } else if (randomNumber > this.draw[3] && randomNumber <= this.draw[4]) {
                recordDo.setDrawPrizeId(DrawProd.gold3000);
                recordDo.setDrawPrizeName(DrawProd.gold3000 + "金币");
            } else {
                recordDo.setDrawPrizeId(DrawProd.gold100);
                recordDo.setDrawPrizeName(DrawProd.gold100 + "金币");
            }
            return recordDo;
        }
    },

    CHARGE_4998(499800, new double[]{0, 0.42, 0.75, 0.87, 0.95, 1}) {
        @Override
        public UserDrawRecordDO createDrawPrize(UserDrawRecordDTO recordDto) {
            UserDrawRecordDO recordDo = this.initRecordDo(recordDto.getRecordId(),recordDto.getType());
            double randomNumber = RandomUtils.randomDouble();
            if (randomNumber >= this.draw[0] && randomNumber <= this.draw[1]) {
                recordDo.setDrawPrizeId(DrawProd.gold300);
                recordDo.setDrawPrizeName(DrawProd.gold300 + "金币");
            } else if (randomNumber > this.draw[1] && randomNumber <= this.draw[2]) {
                recordDo.setDrawPrizeId(DrawProd.gold1000);
                recordDo.setDrawPrizeName(DrawProd.gold1000 + "金币");
            } else if (randomNumber > this.draw[2] && randomNumber <= this.draw[3]) {
                recordDo.setDrawPrizeId(DrawProd.gold3000);
                recordDo.setDrawPrizeName(DrawProd.gold3000 + "金币");
            } else if (randomNumber > this.draw[3] && randomNumber <= this.draw[4]) {
                recordDo.setDrawPrizeId(DrawProd.gold8888);
                recordDo.setDrawPrizeName(DrawProd.gold8888 + "金币");
            } else if (randomNumber > this.draw[4] && randomNumber <= this.draw[5]) {
                UserDrawPrettyErbanNoDao prettyErbanNoDao = SpringAppContext.getBean(UserDrawPrettyErbanNoDao.class);
                UserDrawPrettyErbanNoDTO prettyErbanNo = prettyErbanNoDao.getNotUsePrettyErbanNo(Byte.parseByte("1"));
                if (prettyErbanNo != null) {
                    recordDo.setDrawPrizeName("七位靓号" + prettyErbanNo.getPrettyErbanNo());
                    recordDo.setDrawPrizeId(DrawProd.prettySeven);
                } else {
                    recordDo.setDrawPrizeId(DrawProd.gold1000);
                    recordDo.setDrawPrizeName(DrawProd.gold1000 + "金币");
                }

            }
            return recordDo;
        }
    },

    CHARGE_9999(999900, new double[]{0, 0.1, 0.6, 0.9, 1}) {
        @Override
        public UserDrawRecordDO createDrawPrize(UserDrawRecordDTO recordDto) {
            UserDrawRecordDO recordDo = this.initRecordDo(recordDto.getRecordId(),recordDto.getType());
            double randomNumber = RandomUtils.randomDouble();
            if (randomNumber > this.draw[1] && randomNumber <= this.draw[2]) {
                recordDo.setDrawPrizeId(DrawProd.gold3000);
                recordDo.setDrawPrizeName(DrawProd.gold3000 + "金币");
            } else if (randomNumber > this.draw[2] && randomNumber <= this.draw[3]) {
                recordDo.setDrawPrizeId(DrawProd.gold8888);
                recordDo.setDrawPrizeName(DrawProd.gold8888 + "金币");
            } else if (randomNumber > this.draw[3] && randomNumber <= this.draw[4]) {
                UserDrawPrettyErbanNoDao prettyErbanNoDao = SpringAppContext.getBean(UserDrawPrettyErbanNoDao.class);
                UserDrawPrettyErbanNoDTO prettyErbanNo = prettyErbanNoDao.getNotUsePrettyErbanNo(Byte.parseByte("1"));
                if (prettyErbanNo != null) {
                    recordDo.setDrawPrizeName("七位靓号" + prettyErbanNo.getPrettyErbanNo());
                    recordDo.setDrawPrizeId(DrawProd.prettySeven);
                } else {
                    recordDo.setDrawPrizeId(DrawProd.gold1000);
                    recordDo.setDrawPrizeName(DrawProd.gold1000 + "金币");
                }
            } else {
                recordDo.setDrawPrizeId(DrawProd.gold1000);
                recordDo.setDrawPrizeName(DrawProd.gold1000 + "金币");
            }
            return recordDo;
        }
    },

    CHARGE_30000(3000000, new double[]{0, 0.7, 1}) {
        @Override
        public UserDrawRecordDO createDrawPrize(UserDrawRecordDTO recordDto) {
            UserDrawRecordDO recordDo = this.initRecordDo(recordDto.getRecordId(),recordDto.getType());
            double randomNumber = RandomUtils.randomDouble();
            if (randomNumber <= this.draw[2] && randomNumber > this.draw[1]) {
                UserDrawPrettyErbanNoDao prettyErbanNoDao = SpringAppContext.getBean(UserDrawPrettyErbanNoDao.class);
                UserDrawPrettyErbanNoDTO prettyErbanNo = prettyErbanNoDao.getNotUsePrettyErbanNo(Byte.parseByte("1"));
                if (prettyErbanNo != null) {
                    recordDo.setDrawPrizeName("七位靓号" + prettyErbanNo.getPrettyErbanNo());
                    recordDo.setDrawPrizeId(DrawProd.prettySeven);
                } else {
                    recordDo.setDrawPrizeId(DrawProd.gold1000);
                    recordDo.setDrawPrizeName(DrawProd.gold1000 + "金币");
                }
            } else {
                recordDo.setDrawPrizeId(DrawProd.gold8888);
                recordDo.setDrawPrizeName(DrawProd.gold8888 + "金币");
            }
            return recordDo;
        }
    },

    CHARGE_60000(6000000, new double[]{0, 0.4, 1}) {
        @Override
        public UserDrawRecordDO createDrawPrize(UserDrawRecordDTO recordDto) {
            UserDrawRecordDO recordDo = this.initRecordDo(recordDto.getRecordId(),recordDto.getType());
            double randomNumber = RandomUtils.randomDouble();
            if (randomNumber > this.draw[1] && randomNumber <= this.draw[2]) {
                UserDrawPrettyErbanNoDao prettyErbanNoDao = SpringAppContext.getBean(UserDrawPrettyErbanNoDao.class);
                UserDrawPrettyErbanNoDTO prettyErbanNo = prettyErbanNoDao.getNotUsePrettyErbanNo(Byte.parseByte("1"));
                if (prettyErbanNo != null) {
                    recordDo.setDrawPrizeName("七位靓号" + prettyErbanNo.getPrettyErbanNo());
                    recordDo.setDrawPrizeId(DrawProd.prettySeven);
                } else {
                    recordDo.setDrawPrizeId(DrawProd.gold1000);
                    recordDo.setDrawPrizeName(DrawProd.gold1000 + "金币");
                }
            } else {
                recordDo.setDrawPrizeId(DrawProd.gold8888);
                recordDo.setDrawPrizeName(DrawProd.gold8888 + "金币");
            }
            return recordDo;
        }
    };

    protected int amount;

    protected double[] draw;

    UserDrawPrize(int amount, double[] draw) {
        this.amount = amount;
        this.draw = draw;
    }



    public abstract UserDrawRecordDO createDrawPrize(UserDrawRecordDTO recordDto);

    protected UserDrawRecordDO initRecordDo(Integer recordId, Byte type) {
        UserDrawRecordDO recordDo = new UserDrawRecordDO();
        recordDo.setRecordId(recordId);
        recordDo.setType(type);
        recordDo.setDrawStatus(UserDrawRecordStatus.HAS_PRIZE.getValue());
        recordDo.setUpdateTime(new Date());
        return recordDo;
    }


    public static UserDrawPrize amountOf(Integer amount) {
        if (amount != null) {
            for (UserDrawPrize prod : UserDrawPrize.values()) {
                if (prod.amount == amount.intValue()) {
                    return prod;
                }
            }
        }

        return CHARGE_48;
    }
}
