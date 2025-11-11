package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class StatShareChargeExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public StatShareChargeExample() {
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

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        public Criteria andShareChargeIdIsNull() {
            addCriterion("share_charge_id is null");
            return (Criteria) this;
        }

        public Criteria andShareChargeIdIsNotNull() {
            addCriterion("share_charge_id is not null");
            return (Criteria) this;
        }

        public Criteria andShareChargeIdEqualTo(Integer value) {
            addCriterion("share_charge_id =", value, "shareChargeId");
            return (Criteria) this;
        }

        public Criteria andShareChargeIdNotEqualTo(Integer value) {
            addCriterion("share_charge_id <>", value, "shareChargeId");
            return (Criteria) this;
        }

        public Criteria andShareChargeIdGreaterThan(Integer value) {
            addCriterion("share_charge_id >", value, "shareChargeId");
            return (Criteria) this;
        }

        public Criteria andShareChargeIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("share_charge_id >=", value, "shareChargeId");
            return (Criteria) this;
        }

        public Criteria andShareChargeIdLessThan(Integer value) {
            addCriterion("share_charge_id <", value, "shareChargeId");
            return (Criteria) this;
        }

        public Criteria andShareChargeIdLessThanOrEqualTo(Integer value) {
            addCriterion("share_charge_id <=", value, "shareChargeId");
            return (Criteria) this;
        }

        public Criteria andShareChargeIdIn(List<Integer> values) {
            addCriterion("share_charge_id in", values, "shareChargeId");
            return (Criteria) this;
        }

        public Criteria andShareChargeIdNotIn(List<Integer> values) {
            addCriterion("share_charge_id not in", values, "shareChargeId");
            return (Criteria) this;
        }

        public Criteria andShareChargeIdBetween(Integer value1, Integer value2) {
            addCriterion("share_charge_id between", value1, value2, "shareChargeId");
            return (Criteria) this;
        }

        public Criteria andShareChargeIdNotBetween(Integer value1, Integer value2) {
            addCriterion("share_charge_id not between", value1, value2, "shareChargeId");
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

        public Criteria andErbanNoIsNull() {
            addCriterion("erban_no is null");
            return (Criteria) this;
        }

        public Criteria andErbanNoIsNotNull() {
            addCriterion("erban_no is not null");
            return (Criteria) this;
        }

        public Criteria andErbanNoEqualTo(Integer value) {
            addCriterion("erban_no =", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoNotEqualTo(Integer value) {
            addCriterion("erban_no <>", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoGreaterThan(Integer value) {
            addCriterion("erban_no >", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoGreaterThanOrEqualTo(Integer value) {
            addCriterion("erban_no >=", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoLessThan(Integer value) {
            addCriterion("erban_no <", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoLessThanOrEqualTo(Integer value) {
            addCriterion("erban_no <=", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoIn(List<Integer> values) {
            addCriterion("erban_no in", values, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoNotIn(List<Integer> values) {
            addCriterion("erban_no not in", values, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoBetween(Integer value1, Integer value2) {
            addCriterion("erban_no between", value1, value2, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoNotBetween(Integer value1, Integer value2) {
            addCriterion("erban_no not between", value1, value2, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andNickIsNull() {
            addCriterion("nick is null");
            return (Criteria) this;
        }

        public Criteria andNickIsNotNull() {
            addCriterion("nick is not null");
            return (Criteria) this;
        }

        public Criteria andNickEqualTo(String value) {
            addCriterion("nick =", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickNotEqualTo(String value) {
            addCriterion("nick <>", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickGreaterThan(String value) {
            addCriterion("nick >", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickGreaterThanOrEqualTo(String value) {
            addCriterion("nick >=", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickLessThan(String value) {
            addCriterion("nick <", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickLessThanOrEqualTo(String value) {
            addCriterion("nick <=", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickLike(String value) {
            addCriterion("nick like", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickNotLike(String value) {
            addCriterion("nick not like", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickIn(List<String> values) {
            addCriterion("nick in", values, "nick");
            return (Criteria) this;
        }

        public Criteria andNickNotIn(List<String> values) {
            addCriterion("nick not in", values, "nick");
            return (Criteria) this;
        }

        public Criteria andNickBetween(String value1, String value2) {
            addCriterion("nick between", value1, value2, "nick");
            return (Criteria) this;
        }

        public Criteria andNickNotBetween(String value1, String value2) {
            addCriterion("nick not between", value1, value2, "nick");
            return (Criteria) this;
        }

        public Criteria andShareChannelIsNull() {
            addCriterion("share_channel is null");
            return (Criteria) this;
        }

        public Criteria andShareChannelIsNotNull() {
            addCriterion("share_channel is not null");
            return (Criteria) this;
        }

        public Criteria andShareChannelEqualTo(Byte value) {
            addCriterion("share_channel =", value, "shareChannel");
            return (Criteria) this;
        }

        public Criteria andShareChannelNotEqualTo(Byte value) {
            addCriterion("share_channel <>", value, "shareChannel");
            return (Criteria) this;
        }

        public Criteria andShareChannelGreaterThan(Byte value) {
            addCriterion("share_channel >", value, "shareChannel");
            return (Criteria) this;
        }

        public Criteria andShareChannelGreaterThanOrEqualTo(Byte value) {
            addCriterion("share_channel >=", value, "shareChannel");
            return (Criteria) this;
        }

        public Criteria andShareChannelLessThan(Byte value) {
            addCriterion("share_channel <", value, "shareChannel");
            return (Criteria) this;
        }

        public Criteria andShareChannelLessThanOrEqualTo(Byte value) {
            addCriterion("share_channel <=", value, "shareChannel");
            return (Criteria) this;
        }

        public Criteria andShareChannelIn(List<Byte> values) {
            addCriterion("share_channel in", values, "shareChannel");
            return (Criteria) this;
        }

        public Criteria andShareChannelNotIn(List<Byte> values) {
            addCriterion("share_channel not in", values, "shareChannel");
            return (Criteria) this;
        }

        public Criteria andShareChannelBetween(Byte value1, Byte value2) {
            addCriterion("share_channel between", value1, value2, "shareChannel");
            return (Criteria) this;
        }

        public Criteria andShareChannelNotBetween(Byte value1, Byte value2) {
            addCriterion("share_channel not between", value1, value2, "shareChannel");
            return (Criteria) this;
        }

        public Criteria andRegisterCountIsNull() {
            addCriterion("register_count is null");
            return (Criteria) this;
        }

        public Criteria andRegisterCountIsNotNull() {
            addCriterion("register_count is not null");
            return (Criteria) this;
        }

        public Criteria andRegisterCountEqualTo(Integer value) {
            addCriterion("register_count =", value, "registerCount");
            return (Criteria) this;
        }

        public Criteria andRegisterCountNotEqualTo(Integer value) {
            addCriterion("register_count <>", value, "registerCount");
            return (Criteria) this;
        }

        public Criteria andRegisterCountGreaterThan(Integer value) {
            addCriterion("register_count >", value, "registerCount");
            return (Criteria) this;
        }

        public Criteria andRegisterCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("register_count >=", value, "registerCount");
            return (Criteria) this;
        }

        public Criteria andRegisterCountLessThan(Integer value) {
            addCriterion("register_count <", value, "registerCount");
            return (Criteria) this;
        }

        public Criteria andRegisterCountLessThanOrEqualTo(Integer value) {
            addCriterion("register_count <=", value, "registerCount");
            return (Criteria) this;
        }

        public Criteria andRegisterCountIn(List<Integer> values) {
            addCriterion("register_count in", values, "registerCount");
            return (Criteria) this;
        }

        public Criteria andRegisterCountNotIn(List<Integer> values) {
            addCriterion("register_count not in", values, "registerCount");
            return (Criteria) this;
        }

        public Criteria andRegisterCountBetween(Integer value1, Integer value2) {
            addCriterion("register_count between", value1, value2, "registerCount");
            return (Criteria) this;
        }

        public Criteria andRegisterCountNotBetween(Integer value1, Integer value2) {
            addCriterion("register_count not between", value1, value2, "registerCount");
            return (Criteria) this;
        }

        public Criteria andMorethanChargeCountIsNull() {
            addCriterion("morethan_charge_count is null");
            return (Criteria) this;
        }

        public Criteria andMorethanChargeCountIsNotNull() {
            addCriterion("morethan_charge_count is not null");
            return (Criteria) this;
        }

        public Criteria andMorethanChargeCountEqualTo(Integer value) {
            addCriterion("morethan_charge_count =", value, "morethanChargeCount");
            return (Criteria) this;
        }

        public Criteria andMorethanChargeCountNotEqualTo(Integer value) {
            addCriterion("morethan_charge_count <>", value, "morethanChargeCount");
            return (Criteria) this;
        }

        public Criteria andMorethanChargeCountGreaterThan(Integer value) {
            addCriterion("morethan_charge_count >", value, "morethanChargeCount");
            return (Criteria) this;
        }

        public Criteria andMorethanChargeCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("morethan_charge_count >=", value, "morethanChargeCount");
            return (Criteria) this;
        }

        public Criteria andMorethanChargeCountLessThan(Integer value) {
            addCriterion("morethan_charge_count <", value, "morethanChargeCount");
            return (Criteria) this;
        }

        public Criteria andMorethanChargeCountLessThanOrEqualTo(Integer value) {
            addCriterion("morethan_charge_count <=", value, "morethanChargeCount");
            return (Criteria) this;
        }

        public Criteria andMorethanChargeCountIn(List<Integer> values) {
            addCriterion("morethan_charge_count in", values, "morethanChargeCount");
            return (Criteria) this;
        }

        public Criteria andMorethanChargeCountNotIn(List<Integer> values) {
            addCriterion("morethan_charge_count not in", values, "morethanChargeCount");
            return (Criteria) this;
        }

        public Criteria andMorethanChargeCountBetween(Integer value1, Integer value2) {
            addCriterion("morethan_charge_count between", value1, value2, "morethanChargeCount");
            return (Criteria) this;
        }

        public Criteria andMorethanChargeCountNotBetween(Integer value1, Integer value2) {
            addCriterion("morethan_charge_count not between", value1, value2, "morethanChargeCount");
            return (Criteria) this;
        }

        public Criteria andChargeCountIsNull() {
            addCriterion("charge_count is null");
            return (Criteria) this;
        }

        public Criteria andChargeCountIsNotNull() {
            addCriterion("charge_count is not null");
            return (Criteria) this;
        }

        public Criteria andChargeCountEqualTo(Integer value) {
            addCriterion("charge_count =", value, "chargeCount");
            return (Criteria) this;
        }

        public Criteria andChargeCountNotEqualTo(Integer value) {
            addCriterion("charge_count <>", value, "chargeCount");
            return (Criteria) this;
        }

        public Criteria andChargeCountGreaterThan(Integer value) {
            addCriterion("charge_count >", value, "chargeCount");
            return (Criteria) this;
        }

        public Criteria andChargeCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("charge_count >=", value, "chargeCount");
            return (Criteria) this;
        }

        public Criteria andChargeCountLessThan(Integer value) {
            addCriterion("charge_count <", value, "chargeCount");
            return (Criteria) this;
        }

        public Criteria andChargeCountLessThanOrEqualTo(Integer value) {
            addCriterion("charge_count <=", value, "chargeCount");
            return (Criteria) this;
        }

        public Criteria andChargeCountIn(List<Integer> values) {
            addCriterion("charge_count in", values, "chargeCount");
            return (Criteria) this;
        }

        public Criteria andChargeCountNotIn(List<Integer> values) {
            addCriterion("charge_count not in", values, "chargeCount");
            return (Criteria) this;
        }

        public Criteria andChargeCountBetween(Integer value1, Integer value2) {
            addCriterion("charge_count between", value1, value2, "chargeCount");
            return (Criteria) this;
        }

        public Criteria andChargeCountNotBetween(Integer value1, Integer value2) {
            addCriterion("charge_count not between", value1, value2, "chargeCount");
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
            addCriterionForJDBCDate("stat_date =", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("stat_date <>", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateGreaterThan(Date value) {
            addCriterionForJDBCDate("stat_date >", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("stat_date >=", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateLessThan(Date value) {
            addCriterionForJDBCDate("stat_date <", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("stat_date <=", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateIn(List<Date> values) {
            addCriterionForJDBCDate("stat_date in", values, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("stat_date not in", values, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("stat_date between", value1, value2, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("stat_date not between", value1, value2, "statDate");
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
