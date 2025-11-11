package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RoomOpenHistExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RoomOpenHistExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andHistIdIsNull() {
            addCriterion("hist_id is null");
            return (Criteria) this;
        }

        public Criteria andHistIdIsNotNull() {
            addCriterion("hist_id is not null");
            return (Criteria) this;
        }

        public Criteria andHistIdEqualTo(String value) {
            addCriterion("hist_id =", value, "histId");
            return (Criteria) this;
        }

        public Criteria andHistIdNotEqualTo(String value) {
            addCriterion("hist_id <>", value, "histId");
            return (Criteria) this;
        }

        public Criteria andHistIdGreaterThan(String value) {
            addCriterion("hist_id >", value, "histId");
            return (Criteria) this;
        }

        public Criteria andHistIdGreaterThanOrEqualTo(String value) {
            addCriterion("hist_id >=", value, "histId");
            return (Criteria) this;
        }

        public Criteria andHistIdLessThan(String value) {
            addCriterion("hist_id <", value, "histId");
            return (Criteria) this;
        }

        public Criteria andHistIdLessThanOrEqualTo(String value) {
            addCriterion("hist_id <=", value, "histId");
            return (Criteria) this;
        }

        public Criteria andHistIdLike(String value) {
            addCriterion("hist_id like", value, "histId");
            return (Criteria) this;
        }

        public Criteria andHistIdNotLike(String value) {
            addCriterion("hist_id not like", value, "histId");
            return (Criteria) this;
        }

        public Criteria andHistIdIn(List<String> values) {
            addCriterion("hist_id in", values, "histId");
            return (Criteria) this;
        }

        public Criteria andHistIdNotIn(List<String> values) {
            addCriterion("hist_id not in", values, "histId");
            return (Criteria) this;
        }

        public Criteria andHistIdBetween(String value1, String value2) {
            addCriterion("hist_id between", value1, value2, "histId");
            return (Criteria) this;
        }

        public Criteria andHistIdNotBetween(String value1, String value2) {
            addCriterion("hist_id not between", value1, value2, "histId");
            return (Criteria) this;
        }

        public Criteria andUidIsNull() {
            addCriterion("uid is null");
            return (Criteria) this;
        }

        public Criteria andUidIsNotNull() {
            addCriterion("uid is not null");
            return (Criteria) this;
        }

        public Criteria andUidEqualTo(Long value) {
            addCriterion("uid =", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotEqualTo(Long value) {
            addCriterion("uid <>", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidGreaterThan(Long value) {
            addCriterion("uid >", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidGreaterThanOrEqualTo(Long value) {
            addCriterion("uid >=", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLessThan(Long value) {
            addCriterion("uid <", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLessThanOrEqualTo(Long value) {
            addCriterion("uid <=", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidIn(List<Long> values) {
            addCriterion("uid in", values, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotIn(List<Long> values) {
            addCriterion("uid not in", values, "uid");
            return (Criteria) this;
        }

        public Criteria andUidBetween(Long value1, Long value2) {
            addCriterion("uid between", value1, value2, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotBetween(Long value1, Long value2) {
            addCriterion("uid not between", value1, value2, "uid");
            return (Criteria) this;
        }

        public Criteria andRoomIdIsNull() {
            addCriterion("room_id is null");
            return (Criteria) this;
        }

        public Criteria andRoomIdIsNotNull() {
            addCriterion("room_id is not null");
            return (Criteria) this;
        }

        public Criteria andRoomIdEqualTo(Long value) {
            addCriterion("room_id =", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdNotEqualTo(Long value) {
            addCriterion("room_id <>", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdGreaterThan(Long value) {
            addCriterion("room_id >", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdGreaterThanOrEqualTo(Long value) {
            addCriterion("room_id >=", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdLessThan(Long value) {
            addCriterion("room_id <", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdLessThanOrEqualTo(Long value) {
            addCriterion("room_id <=", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdIn(List<Long> values) {
            addCriterion("room_id in", values, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdNotIn(List<Long> values) {
            addCriterion("room_id not in", values, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdBetween(Long value1, Long value2) {
            addCriterion("room_id between", value1, value2, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdNotBetween(Long value1, Long value2) {
            addCriterion("room_id not between", value1, value2, "roomId");
            return (Criteria) this;
        }

        public Criteria andMeetingNameIsNull() {
            addCriterion("meeting_name is null");
            return (Criteria) this;
        }

        public Criteria andMeetingNameIsNotNull() {
            addCriterion("meeting_name is not null");
            return (Criteria) this;
        }

        public Criteria andMeetingNameEqualTo(String value) {
            addCriterion("meeting_name =", value, "meetingName");
            return (Criteria) this;
        }

        public Criteria andMeetingNameNotEqualTo(String value) {
            addCriterion("meeting_name <>", value, "meetingName");
            return (Criteria) this;
        }

        public Criteria andMeetingNameGreaterThan(String value) {
            addCriterion("meeting_name >", value, "meetingName");
            return (Criteria) this;
        }

        public Criteria andMeetingNameGreaterThanOrEqualTo(String value) {
            addCriterion("meeting_name >=", value, "meetingName");
            return (Criteria) this;
        }

        public Criteria andMeetingNameLessThan(String value) {
            addCriterion("meeting_name <", value, "meetingName");
            return (Criteria) this;
        }

        public Criteria andMeetingNameLessThanOrEqualTo(String value) {
            addCriterion("meeting_name <=", value, "meetingName");
            return (Criteria) this;
        }

        public Criteria andMeetingNameLike(String value) {
            addCriterion("meeting_name like", value, "meetingName");
            return (Criteria) this;
        }

        public Criteria andMeetingNameNotLike(String value) {
            addCriterion("meeting_name not like", value, "meetingName");
            return (Criteria) this;
        }

        public Criteria andMeetingNameIn(List<String> values) {
            addCriterion("meeting_name in", values, "meetingName");
            return (Criteria) this;
        }

        public Criteria andMeetingNameNotIn(List<String> values) {
            addCriterion("meeting_name not in", values, "meetingName");
            return (Criteria) this;
        }

        public Criteria andMeetingNameBetween(String value1, String value2) {
            addCriterion("meeting_name between", value1, value2, "meetingName");
            return (Criteria) this;
        }

        public Criteria andMeetingNameNotBetween(String value1, String value2) {
            addCriterion("meeting_name not between", value1, value2, "meetingName");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Byte value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Byte value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Byte value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Byte value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Byte value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Byte> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Byte> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Byte value1, Byte value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyIsNull() {
            addCriterion("reward_money is null");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyIsNotNull() {
            addCriterion("reward_money is not null");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyEqualTo(Long value) {
            addCriterion("reward_money =", value, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyNotEqualTo(Long value) {
            addCriterion("reward_money <>", value, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyGreaterThan(Long value) {
            addCriterion("reward_money >", value, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyGreaterThanOrEqualTo(Long value) {
            addCriterion("reward_money >=", value, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyLessThan(Long value) {
            addCriterion("reward_money <", value, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyLessThanOrEqualTo(Long value) {
            addCriterion("reward_money <=", value, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyIn(List<Long> values) {
            addCriterion("reward_money in", values, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyNotIn(List<Long> values) {
            addCriterion("reward_money not in", values, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyBetween(Long value1, Long value2) {
            addCriterion("reward_money between", value1, value2, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyNotBetween(Long value1, Long value2) {
            addCriterion("reward_money not between", value1, value2, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andServDuraIsNull() {
            addCriterion("serv_dura is null");
            return (Criteria) this;
        }

        public Criteria andServDuraIsNotNull() {
            addCriterion("serv_dura is not null");
            return (Criteria) this;
        }

        public Criteria andServDuraEqualTo(Integer value) {
            addCriterion("serv_dura =", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraNotEqualTo(Integer value) {
            addCriterion("serv_dura <>", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraGreaterThan(Integer value) {
            addCriterion("serv_dura >", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraGreaterThanOrEqualTo(Integer value) {
            addCriterion("serv_dura >=", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraLessThan(Integer value) {
            addCriterion("serv_dura <", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraLessThanOrEqualTo(Integer value) {
            addCriterion("serv_dura <=", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraIn(List<Integer> values) {
            addCriterion("serv_dura in", values, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraNotIn(List<Integer> values) {
            addCriterion("serv_dura not in", values, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraBetween(Integer value1, Integer value2) {
            addCriterion("serv_dura between", value1, value2, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraNotBetween(Integer value1, Integer value2) {
            addCriterion("serv_dura not between", value1, value2, "servDura");
            return (Criteria) this;
        }

        public Criteria andCloseTypeIsNull() {
            addCriterion("close_type is null");
            return (Criteria) this;
        }

        public Criteria andCloseTypeIsNotNull() {
            addCriterion("close_type is not null");
            return (Criteria) this;
        }

        public Criteria andCloseTypeEqualTo(Byte value) {
            addCriterion("close_type =", value, "closeType");
            return (Criteria) this;
        }

        public Criteria andCloseTypeNotEqualTo(Byte value) {
            addCriterion("close_type <>", value, "closeType");
            return (Criteria) this;
        }

        public Criteria andCloseTypeGreaterThan(Byte value) {
            addCriterion("close_type >", value, "closeType");
            return (Criteria) this;
        }

        public Criteria andCloseTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("close_type >=", value, "closeType");
            return (Criteria) this;
        }

        public Criteria andCloseTypeLessThan(Byte value) {
            addCriterion("close_type <", value, "closeType");
            return (Criteria) this;
        }

        public Criteria andCloseTypeLessThanOrEqualTo(Byte value) {
            addCriterion("close_type <=", value, "closeType");
            return (Criteria) this;
        }

        public Criteria andCloseTypeIn(List<Byte> values) {
            addCriterion("close_type in", values, "closeType");
            return (Criteria) this;
        }

        public Criteria andCloseTypeNotIn(List<Byte> values) {
            addCriterion("close_type not in", values, "closeType");
            return (Criteria) this;
        }

        public Criteria andCloseTypeBetween(Byte value1, Byte value2) {
            addCriterion("close_type between", value1, value2, "closeType");
            return (Criteria) this;
        }

        public Criteria andCloseTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("close_type not between", value1, value2, "closeType");
            return (Criteria) this;
        }

        public Criteria andOpenTimeIsNull() {
            addCriterion("open_time is null");
            return (Criteria) this;
        }

        public Criteria andOpenTimeIsNotNull() {
            addCriterion("open_time is not null");
            return (Criteria) this;
        }

        public Criteria andOpenTimeEqualTo(Date value) {
            addCriterion("open_time =", value, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeNotEqualTo(Date value) {
            addCriterion("open_time <>", value, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeGreaterThan(Date value) {
            addCriterion("open_time >", value, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("open_time >=", value, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeLessThan(Date value) {
            addCriterion("open_time <", value, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeLessThanOrEqualTo(Date value) {
            addCriterion("open_time <=", value, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeIn(List<Date> values) {
            addCriterion("open_time in", values, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeNotIn(List<Date> values) {
            addCriterion("open_time not in", values, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeBetween(Date value1, Date value2) {
            addCriterion("open_time between", value1, value2, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeNotBetween(Date value1, Date value2) {
            addCriterion("open_time not between", value1, value2, "openTime");
            return (Criteria) this;
        }

        public Criteria andCloseTimeIsNull() {
            addCriterion("close_time is null");
            return (Criteria) this;
        }

        public Criteria andCloseTimeIsNotNull() {
            addCriterion("close_time is not null");
            return (Criteria) this;
        }

        public Criteria andCloseTimeEqualTo(Date value) {
            addCriterion("close_time =", value, "closeTime");
            return (Criteria) this;
        }

        public Criteria andCloseTimeNotEqualTo(Date value) {
            addCriterion("close_time <>", value, "closeTime");
            return (Criteria) this;
        }

        public Criteria andCloseTimeGreaterThan(Date value) {
            addCriterion("close_time >", value, "closeTime");
            return (Criteria) this;
        }

        public Criteria andCloseTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("close_time >=", value, "closeTime");
            return (Criteria) this;
        }

        public Criteria andCloseTimeLessThan(Date value) {
            addCriterion("close_time <", value, "closeTime");
            return (Criteria) this;
        }

        public Criteria andCloseTimeLessThanOrEqualTo(Date value) {
            addCriterion("close_time <=", value, "closeTime");
            return (Criteria) this;
        }

        public Criteria andCloseTimeIn(List<Date> values) {
            addCriterion("close_time in", values, "closeTime");
            return (Criteria) this;
        }

        public Criteria andCloseTimeNotIn(List<Date> values) {
            addCriterion("close_time not in", values, "closeTime");
            return (Criteria) this;
        }

        public Criteria andCloseTimeBetween(Date value1, Date value2) {
            addCriterion("close_time between", value1, value2, "closeTime");
            return (Criteria) this;
        }

        public Criteria andCloseTimeNotBetween(Date value1, Date value2) {
            addCriterion("close_time not between", value1, value2, "closeTime");
            return (Criteria) this;
        }

        public Criteria andDuraIsNull() {
            addCriterion("dura is null");
            return (Criteria) this;
        }

        public Criteria andDuraIsNotNull() {
            addCriterion("dura is not null");
            return (Criteria) this;
        }

        public Criteria andDuraEqualTo(Double value) {
            addCriterion("dura =", value, "dura");
            return (Criteria) this;
        }

        public Criteria andDuraNotEqualTo(Double value) {
            addCriterion("dura <>", value, "dura");
            return (Criteria) this;
        }

        public Criteria andDuraGreaterThan(Double value) {
            addCriterion("dura >", value, "dura");
            return (Criteria) this;
        }

        public Criteria andDuraGreaterThanOrEqualTo(Double value) {
            addCriterion("dura >=", value, "dura");
            return (Criteria) this;
        }

        public Criteria andDuraLessThan(Double value) {
            addCriterion("dura <", value, "dura");
            return (Criteria) this;
        }

        public Criteria andDuraLessThanOrEqualTo(Double value) {
            addCriterion("dura <=", value, "dura");
            return (Criteria) this;
        }

        public Criteria andDuraIn(List<Double> values) {
            addCriterion("dura in", values, "dura");
            return (Criteria) this;
        }

        public Criteria andDuraNotIn(List<Double> values) {
            addCriterion("dura not in", values, "dura");
            return (Criteria) this;
        }

        public Criteria andDuraBetween(Double value1, Double value2) {
            addCriterion("dura between", value1, value2, "dura");
            return (Criteria) this;
        }

        public Criteria andDuraNotBetween(Double value1, Double value2) {
            addCriterion("dura not between", value1, value2, "dura");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}
