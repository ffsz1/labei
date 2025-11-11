package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatRoomAuctExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public StatRoomAuctExample() {
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

        public Criteria andRoomAuctIdIsNull() {
            addCriterion("room_auct_id is null");
            return (Criteria) this;
        }

        public Criteria andRoomAuctIdIsNotNull() {
            addCriterion("room_auct_id is not null");
            return (Criteria) this;
        }

        public Criteria andRoomAuctIdEqualTo(Long value) {
            addCriterion("room_auct_id =", value, "roomAuctId");
            return (Criteria) this;
        }

        public Criteria andRoomAuctIdNotEqualTo(Long value) {
            addCriterion("room_auct_id <>", value, "roomAuctId");
            return (Criteria) this;
        }

        public Criteria andRoomAuctIdGreaterThan(Long value) {
            addCriterion("room_auct_id >", value, "roomAuctId");
            return (Criteria) this;
        }

        public Criteria andRoomAuctIdGreaterThanOrEqualTo(Long value) {
            addCriterion("room_auct_id >=", value, "roomAuctId");
            return (Criteria) this;
        }

        public Criteria andRoomAuctIdLessThan(Long value) {
            addCriterion("room_auct_id <", value, "roomAuctId");
            return (Criteria) this;
        }

        public Criteria andRoomAuctIdLessThanOrEqualTo(Long value) {
            addCriterion("room_auct_id <=", value, "roomAuctId");
            return (Criteria) this;
        }

        public Criteria andRoomAuctIdIn(List<Long> values) {
            addCriterion("room_auct_id in", values, "roomAuctId");
            return (Criteria) this;
        }

        public Criteria andRoomAuctIdNotIn(List<Long> values) {
            addCriterion("room_auct_id not in", values, "roomAuctId");
            return (Criteria) this;
        }

        public Criteria andRoomAuctIdBetween(Long value1, Long value2) {
            addCriterion("room_auct_id between", value1, value2, "roomAuctId");
            return (Criteria) this;
        }

        public Criteria andRoomAuctIdNotBetween(Long value1, Long value2) {
            addCriterion("room_auct_id not between", value1, value2, "roomAuctId");
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

        public Criteria andTotalAuctMoneyIsNull() {
            addCriterion("total_auct_money is null");
            return (Criteria) this;
        }

        public Criteria andTotalAuctMoneyIsNotNull() {
            addCriterion("total_auct_money is not null");
            return (Criteria) this;
        }

        public Criteria andTotalAuctMoneyEqualTo(Integer value) {
            addCriterion("total_auct_money =", value, "totalAuctMoney");
            return (Criteria) this;
        }

        public Criteria andTotalAuctMoneyNotEqualTo(Integer value) {
            addCriterion("total_auct_money <>", value, "totalAuctMoney");
            return (Criteria) this;
        }

        public Criteria andTotalAuctMoneyGreaterThan(Integer value) {
            addCriterion("total_auct_money >", value, "totalAuctMoney");
            return (Criteria) this;
        }

        public Criteria andTotalAuctMoneyGreaterThanOrEqualTo(Integer value) {
            addCriterion("total_auct_money >=", value, "totalAuctMoney");
            return (Criteria) this;
        }

        public Criteria andTotalAuctMoneyLessThan(Integer value) {
            addCriterion("total_auct_money <", value, "totalAuctMoney");
            return (Criteria) this;
        }

        public Criteria andTotalAuctMoneyLessThanOrEqualTo(Integer value) {
            addCriterion("total_auct_money <=", value, "totalAuctMoney");
            return (Criteria) this;
        }

        public Criteria andTotalAuctMoneyIn(List<Integer> values) {
            addCriterion("total_auct_money in", values, "totalAuctMoney");
            return (Criteria) this;
        }

        public Criteria andTotalAuctMoneyNotIn(List<Integer> values) {
            addCriterion("total_auct_money not in", values, "totalAuctMoney");
            return (Criteria) this;
        }

        public Criteria andTotalAuctMoneyBetween(Integer value1, Integer value2) {
            addCriterion("total_auct_money between", value1, value2, "totalAuctMoney");
            return (Criteria) this;
        }

        public Criteria andTotalAuctMoneyNotBetween(Integer value1, Integer value2) {
            addCriterion("total_auct_money not between", value1, value2, "totalAuctMoney");
            return (Criteria) this;
        }

        public Criteria andTotalAuctCountIsNull() {
            addCriterion("total_auct_count is null");
            return (Criteria) this;
        }

        public Criteria andTotalAuctCountIsNotNull() {
            addCriterion("total_auct_count is not null");
            return (Criteria) this;
        }

        public Criteria andTotalAuctCountEqualTo(Integer value) {
            addCriterion("total_auct_count =", value, "totalAuctCount");
            return (Criteria) this;
        }

        public Criteria andTotalAuctCountNotEqualTo(Integer value) {
            addCriterion("total_auct_count <>", value, "totalAuctCount");
            return (Criteria) this;
        }

        public Criteria andTotalAuctCountGreaterThan(Integer value) {
            addCriterion("total_auct_count >", value, "totalAuctCount");
            return (Criteria) this;
        }

        public Criteria andTotalAuctCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("total_auct_count >=", value, "totalAuctCount");
            return (Criteria) this;
        }

        public Criteria andTotalAuctCountLessThan(Integer value) {
            addCriterion("total_auct_count <", value, "totalAuctCount");
            return (Criteria) this;
        }

        public Criteria andTotalAuctCountLessThanOrEqualTo(Integer value) {
            addCriterion("total_auct_count <=", value, "totalAuctCount");
            return (Criteria) this;
        }

        public Criteria andTotalAuctCountIn(List<Integer> values) {
            addCriterion("total_auct_count in", values, "totalAuctCount");
            return (Criteria) this;
        }

        public Criteria andTotalAuctCountNotIn(List<Integer> values) {
            addCriterion("total_auct_count not in", values, "totalAuctCount");
            return (Criteria) this;
        }

        public Criteria andTotalAuctCountBetween(Integer value1, Integer value2) {
            addCriterion("total_auct_count between", value1, value2, "totalAuctCount");
            return (Criteria) this;
        }

        public Criteria andTotalAuctCountNotBetween(Integer value1, Integer value2) {
            addCriterion("total_auct_count not between", value1, value2, "totalAuctCount");
            return (Criteria) this;
        }

        public Criteria andStatDateIsNull() {
            addCriterion("stat_date is null");
            return (Criteria) this;
        }

        public Criteria andStatDateIsNotNull() {
            addCriterion("stat_date is not null");
            return (Criteria) this;
        }

        public Criteria andStatDateEqualTo(Date value) {
            addCriterion("stat_date =", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateNotEqualTo(Date value) {
            addCriterion("stat_date <>", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateGreaterThan(Date value) {
            addCriterion("stat_date >", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateGreaterThanOrEqualTo(Date value) {
            addCriterion("stat_date >=", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateLessThan(Date value) {
            addCriterion("stat_date <", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateLessThanOrEqualTo(Date value) {
            addCriterion("stat_date <=", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateIn(List<Date> values) {
            addCriterion("stat_date in", values, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateNotIn(List<Date> values) {
            addCriterion("stat_date not in", values, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateBetween(Date value1, Date value2) {
            addCriterion("stat_date between", value1, value2, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateNotBetween(Date value1, Date value2) {
            addCriterion("stat_date not between", value1, value2, "statDate");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
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
