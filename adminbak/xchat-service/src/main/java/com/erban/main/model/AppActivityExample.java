package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppActivityExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AppActivityExample() {
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

        public Criteria andActIdIsNull() {
            addCriterion("act_id is null");
            return (Criteria) this;
        }

        public Criteria andActIdIsNotNull() {
            addCriterion("act_id is not null");
            return (Criteria) this;
        }

        public Criteria andActIdEqualTo(Integer value) {
            addCriterion("act_id =", value, "actId");
            return (Criteria) this;
        }

        public Criteria andActIdNotEqualTo(Integer value) {
            addCriterion("act_id <>", value, "actId");
            return (Criteria) this;
        }

        public Criteria andActIdGreaterThan(Integer value) {
            addCriterion("act_id >", value, "actId");
            return (Criteria) this;
        }

        public Criteria andActIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("act_id >=", value, "actId");
            return (Criteria) this;
        }

        public Criteria andActIdLessThan(Integer value) {
            addCriterion("act_id <", value, "actId");
            return (Criteria) this;
        }

        public Criteria andActIdLessThanOrEqualTo(Integer value) {
            addCriterion("act_id <=", value, "actId");
            return (Criteria) this;
        }

        public Criteria andActIdIn(List<Integer> values) {
            addCriterion("act_id in", values, "actId");
            return (Criteria) this;
        }

        public Criteria andActIdNotIn(List<Integer> values) {
            addCriterion("act_id not in", values, "actId");
            return (Criteria) this;
        }

        public Criteria andActIdBetween(Integer value1, Integer value2) {
            addCriterion("act_id between", value1, value2, "actId");
            return (Criteria) this;
        }

        public Criteria andActIdNotBetween(Integer value1, Integer value2) {
            addCriterion("act_id not between", value1, value2, "actId");
            return (Criteria) this;
        }

        public Criteria andActNameIsNull() {
            addCriterion("act_name is null");
            return (Criteria) this;
        }

        public Criteria andActNameIsNotNull() {
            addCriterion("act_name is not null");
            return (Criteria) this;
        }

        public Criteria andActNameEqualTo(String value) {
            addCriterion("act_name =", value, "actName");
            return (Criteria) this;
        }

        public Criteria andActNameNotEqualTo(String value) {
            addCriterion("act_name <>", value, "actName");
            return (Criteria) this;
        }

        public Criteria andActNameGreaterThan(String value) {
            addCriterion("act_name >", value, "actName");
            return (Criteria) this;
        }

        public Criteria andActNameGreaterThanOrEqualTo(String value) {
            addCriterion("act_name >=", value, "actName");
            return (Criteria) this;
        }

        public Criteria andActNameLessThan(String value) {
            addCriterion("act_name <", value, "actName");
            return (Criteria) this;
        }

        public Criteria andActNameLessThanOrEqualTo(String value) {
            addCriterion("act_name <=", value, "actName");
            return (Criteria) this;
        }

        public Criteria andActNameLike(String value) {
            addCriterion("act_name like", value, "actName");
            return (Criteria) this;
        }

        public Criteria andActNameNotLike(String value) {
            addCriterion("act_name not like", value, "actName");
            return (Criteria) this;
        }

        public Criteria andActNameIn(List<String> values) {
            addCriterion("act_name in", values, "actName");
            return (Criteria) this;
        }

        public Criteria andActNameNotIn(List<String> values) {
            addCriterion("act_name not in", values, "actName");
            return (Criteria) this;
        }

        public Criteria andActNameBetween(String value1, String value2) {
            addCriterion("act_name between", value1, value2, "actName");
            return (Criteria) this;
        }

        public Criteria andActNameNotBetween(String value1, String value2) {
            addCriterion("act_name not between", value1, value2, "actName");
            return (Criteria) this;
        }

        public Criteria andActStatusIsNull() {
            addCriterion("act_status is null");
            return (Criteria) this;
        }

        public Criteria andActStatusIsNotNull() {
            addCriterion("act_status is not null");
            return (Criteria) this;
        }

        public Criteria andActStatusEqualTo(Byte value) {
            addCriterion("act_status =", value, "actStatus");
            return (Criteria) this;
        }

        public Criteria andActStatusNotEqualTo(Byte value) {
            addCriterion("act_status <>", value, "actStatus");
            return (Criteria) this;
        }

        public Criteria andActStatusGreaterThan(Byte value) {
            addCriterion("act_status >", value, "actStatus");
            return (Criteria) this;
        }

        public Criteria andActStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("act_status >=", value, "actStatus");
            return (Criteria) this;
        }

        public Criteria andActStatusLessThan(Byte value) {
            addCriterion("act_status <", value, "actStatus");
            return (Criteria) this;
        }

        public Criteria andActStatusLessThanOrEqualTo(Byte value) {
            addCriterion("act_status <=", value, "actStatus");
            return (Criteria) this;
        }

        public Criteria andActStatusIn(List<Byte> values) {
            addCriterion("act_status in", values, "actStatus");
            return (Criteria) this;
        }

        public Criteria andActStatusNotIn(List<Byte> values) {
            addCriterion("act_status not in", values, "actStatus");
            return (Criteria) this;
        }

        public Criteria andActStatusBetween(Byte value1, Byte value2) {
            addCriterion("act_status between", value1, value2, "actStatus");
            return (Criteria) this;
        }

        public Criteria andActStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("act_status not between", value1, value2, "actStatus");
            return (Criteria) this;
        }

        public Criteria andActAlertVersionIsNull() {
            addCriterion("act_alert_version is null");
            return (Criteria) this;
        }

        public Criteria andActAlertVersionIsNotNull() {
            addCriterion("act_alert_version is not null");
            return (Criteria) this;
        }

        public Criteria andActAlertVersionEqualTo(String value) {
            addCriterion("act_alert_version =", value, "actAlertVersion");
            return (Criteria) this;
        }

        public Criteria andActAlertVersionNotEqualTo(String value) {
            addCriterion("act_alert_version <>", value, "actAlertVersion");
            return (Criteria) this;
        }

        public Criteria andActAlertVersionGreaterThan(String value) {
            addCriterion("act_alert_version >", value, "actAlertVersion");
            return (Criteria) this;
        }

        public Criteria andActAlertVersionGreaterThanOrEqualTo(String value) {
            addCriterion("act_alert_version >=", value, "actAlertVersion");
            return (Criteria) this;
        }

        public Criteria andActAlertVersionLessThan(String value) {
            addCriterion("act_alert_version <", value, "actAlertVersion");
            return (Criteria) this;
        }

        public Criteria andActAlertVersionLessThanOrEqualTo(String value) {
            addCriterion("act_alert_version <=", value, "actAlertVersion");
            return (Criteria) this;
        }

        public Criteria andActAlertVersionLike(String value) {
            addCriterion("act_alert_version like", value, "actAlertVersion");
            return (Criteria) this;
        }

        public Criteria andActAlertVersionNotLike(String value) {
            addCriterion("act_alert_version not like", value, "actAlertVersion");
            return (Criteria) this;
        }

        public Criteria andActAlertVersionIn(List<String> values) {
            addCriterion("act_alert_version in", values, "actAlertVersion");
            return (Criteria) this;
        }

        public Criteria andActAlertVersionNotIn(List<String> values) {
            addCriterion("act_alert_version not in", values, "actAlertVersion");
            return (Criteria) this;
        }

        public Criteria andActAlertVersionBetween(String value1, String value2) {
            addCriterion("act_alert_version between", value1, value2, "actAlertVersion");
            return (Criteria) this;
        }

        public Criteria andActAlertVersionNotBetween(String value1, String value2) {
            addCriterion("act_alert_version not between", value1, value2, "actAlertVersion");
            return (Criteria) this;
        }

        public Criteria andAlertWinIsNull() {
            addCriterion("alert_win is null");
            return (Criteria) this;
        }

        public Criteria andAlertWinIsNotNull() {
            addCriterion("alert_win is not null");
            return (Criteria) this;
        }

        public Criteria andAlertWinEqualTo(Boolean value) {
            addCriterion("alert_win =", value, "alertWin");
            return (Criteria) this;
        }

        public Criteria andAlertWinNotEqualTo(Boolean value) {
            addCriterion("alert_win <>", value, "alertWin");
            return (Criteria) this;
        }

        public Criteria andAlertWinGreaterThan(Boolean value) {
            addCriterion("alert_win >", value, "alertWin");
            return (Criteria) this;
        }

        public Criteria andAlertWinGreaterThanOrEqualTo(Boolean value) {
            addCriterion("alert_win >=", value, "alertWin");
            return (Criteria) this;
        }

        public Criteria andAlertWinLessThan(Boolean value) {
            addCriterion("alert_win <", value, "alertWin");
            return (Criteria) this;
        }

        public Criteria andAlertWinLessThanOrEqualTo(Boolean value) {
            addCriterion("alert_win <=", value, "alertWin");
            return (Criteria) this;
        }

        public Criteria andAlertWinIn(List<Boolean> values) {
            addCriterion("alert_win in", values, "alertWin");
            return (Criteria) this;
        }

        public Criteria andAlertWinNotIn(List<Boolean> values) {
            addCriterion("alert_win not in", values, "alertWin");
            return (Criteria) this;
        }

        public Criteria andAlertWinBetween(Boolean value1, Boolean value2) {
            addCriterion("alert_win between", value1, value2, "alertWin");
            return (Criteria) this;
        }

        public Criteria andAlertWinNotBetween(Boolean value1, Boolean value2) {
            addCriterion("alert_win not between", value1, value2, "alertWin");
            return (Criteria) this;
        }

        public Criteria andAlertWinPicIsNull() {
            addCriterion("alert_win_pic is null");
            return (Criteria) this;
        }

        public Criteria andAlertWinPicIsNotNull() {
            addCriterion("alert_win_pic is not null");
            return (Criteria) this;
        }

        public Criteria andAlertWinPicEqualTo(String value) {
            addCriterion("alert_win_pic =", value, "alertWinPic");
            return (Criteria) this;
        }

        public Criteria andAlertWinPicNotEqualTo(String value) {
            addCriterion("alert_win_pic <>", value, "alertWinPic");
            return (Criteria) this;
        }

        public Criteria andAlertWinPicGreaterThan(String value) {
            addCriterion("alert_win_pic >", value, "alertWinPic");
            return (Criteria) this;
        }

        public Criteria andAlertWinPicGreaterThanOrEqualTo(String value) {
            addCriterion("alert_win_pic >=", value, "alertWinPic");
            return (Criteria) this;
        }

        public Criteria andAlertWinPicLessThan(String value) {
            addCriterion("alert_win_pic <", value, "alertWinPic");
            return (Criteria) this;
        }

        public Criteria andAlertWinPicLessThanOrEqualTo(String value) {
            addCriterion("alert_win_pic <=", value, "alertWinPic");
            return (Criteria) this;
        }

        public Criteria andAlertWinPicLike(String value) {
            addCriterion("alert_win_pic like", value, "alertWinPic");
            return (Criteria) this;
        }

        public Criteria andAlertWinPicNotLike(String value) {
            addCriterion("alert_win_pic not like", value, "alertWinPic");
            return (Criteria) this;
        }

        public Criteria andAlertWinPicIn(List<String> values) {
            addCriterion("alert_win_pic in", values, "alertWinPic");
            return (Criteria) this;
        }

        public Criteria andAlertWinPicNotIn(List<String> values) {
            addCriterion("alert_win_pic not in", values, "alertWinPic");
            return (Criteria) this;
        }

        public Criteria andAlertWinPicBetween(String value1, String value2) {
            addCriterion("alert_win_pic between", value1, value2, "alertWinPic");
            return (Criteria) this;
        }

        public Criteria andAlertWinPicNotBetween(String value1, String value2) {
            addCriterion("alert_win_pic not between", value1, value2, "alertWinPic");
            return (Criteria) this;
        }

        public Criteria andAlertWinLocIsNull() {
            addCriterion("alert_win_loc is null");
            return (Criteria) this;
        }

        public Criteria andAlertWinLocIsNotNull() {
            addCriterion("alert_win_loc is not null");
            return (Criteria) this;
        }

        public Criteria andAlertWinLocEqualTo(Byte value) {
            addCriterion("alert_win_loc =", value, "alertWinLoc");
            return (Criteria) this;
        }

        public Criteria andAlertWinLocNotEqualTo(Byte value) {
            addCriterion("alert_win_loc <>", value, "alertWinLoc");
            return (Criteria) this;
        }

        public Criteria andAlertWinLocGreaterThan(Byte value) {
            addCriterion("alert_win_loc >", value, "alertWinLoc");
            return (Criteria) this;
        }

        public Criteria andAlertWinLocGreaterThanOrEqualTo(Byte value) {
            addCriterion("alert_win_loc >=", value, "alertWinLoc");
            return (Criteria) this;
        }

        public Criteria andAlertWinLocLessThan(Byte value) {
            addCriterion("alert_win_loc <", value, "alertWinLoc");
            return (Criteria) this;
        }

        public Criteria andAlertWinLocLessThanOrEqualTo(Byte value) {
            addCriterion("alert_win_loc <=", value, "alertWinLoc");
            return (Criteria) this;
        }

        public Criteria andAlertWinLocIn(List<Byte> values) {
            addCriterion("alert_win_loc in", values, "alertWinLoc");
            return (Criteria) this;
        }

        public Criteria andAlertWinLocNotIn(List<Byte> values) {
            addCriterion("alert_win_loc not in", values, "alertWinLoc");
            return (Criteria) this;
        }

        public Criteria andAlertWinLocBetween(Byte value1, Byte value2) {
            addCriterion("alert_win_loc between", value1, value2, "alertWinLoc");
            return (Criteria) this;
        }

        public Criteria andAlertWinLocNotBetween(Byte value1, Byte value2) {
            addCriterion("alert_win_loc not between", value1, value2, "alertWinLoc");
            return (Criteria) this;
        }

        public Criteria andSkipTypeIsNull() {
            addCriterion("skip_type is null");
            return (Criteria) this;
        }

        public Criteria andSkipTypeIsNotNull() {
            addCriterion("skip_type is not null");
            return (Criteria) this;
        }

        public Criteria andSkipTypeEqualTo(Byte value) {
            addCriterion("skip_type =", value, "skipType");
            return (Criteria) this;
        }

        public Criteria andSkipTypeNotEqualTo(Byte value) {
            addCriterion("skip_type <>", value, "skipType");
            return (Criteria) this;
        }

        public Criteria andSkipTypeGreaterThan(Byte value) {
            addCriterion("skip_type >", value, "skipType");
            return (Criteria) this;
        }

        public Criteria andSkipTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("skip_type >=", value, "skipType");
            return (Criteria) this;
        }

        public Criteria andSkipTypeLessThan(Byte value) {
            addCriterion("skip_type <", value, "skipType");
            return (Criteria) this;
        }

        public Criteria andSkipTypeLessThanOrEqualTo(Byte value) {
            addCriterion("skip_type <=", value, "skipType");
            return (Criteria) this;
        }

        public Criteria andSkipTypeIn(List<Byte> values) {
            addCriterion("skip_type in", values, "skipType");
            return (Criteria) this;
        }

        public Criteria andSkipTypeNotIn(List<Byte> values) {
            addCriterion("skip_type not in", values, "skipType");
            return (Criteria) this;
        }

        public Criteria andSkipTypeBetween(Byte value1, Byte value2) {
            addCriterion("skip_type between", value1, value2, "skipType");
            return (Criteria) this;
        }

        public Criteria andSkipTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("skip_type not between", value1, value2, "skipType");
            return (Criteria) this;
        }

        public Criteria andSkipUrlIsNull() {
            addCriterion("skip_url is null");
            return (Criteria) this;
        }

        public Criteria andSkipUrlIsNotNull() {
            addCriterion("skip_url is not null");
            return (Criteria) this;
        }

        public Criteria andSkipUrlEqualTo(String value) {
            addCriterion("skip_url =", value, "skipUrl");
            return (Criteria) this;
        }

        public Criteria andSkipUrlNotEqualTo(String value) {
            addCriterion("skip_url <>", value, "skipUrl");
            return (Criteria) this;
        }

        public Criteria andSkipUrlGreaterThan(String value) {
            addCriterion("skip_url >", value, "skipUrl");
            return (Criteria) this;
        }

        public Criteria andSkipUrlGreaterThanOrEqualTo(String value) {
            addCriterion("skip_url >=", value, "skipUrl");
            return (Criteria) this;
        }

        public Criteria andSkipUrlLessThan(String value) {
            addCriterion("skip_url <", value, "skipUrl");
            return (Criteria) this;
        }

        public Criteria andSkipUrlLessThanOrEqualTo(String value) {
            addCriterion("skip_url <=", value, "skipUrl");
            return (Criteria) this;
        }

        public Criteria andSkipUrlLike(String value) {
            addCriterion("skip_url like", value, "skipUrl");
            return (Criteria) this;
        }

        public Criteria andSkipUrlNotLike(String value) {
            addCriterion("skip_url not like", value, "skipUrl");
            return (Criteria) this;
        }

        public Criteria andSkipUrlIn(List<String> values) {
            addCriterion("skip_url in", values, "skipUrl");
            return (Criteria) this;
        }

        public Criteria andSkipUrlNotIn(List<String> values) {
            addCriterion("skip_url not in", values, "skipUrl");
            return (Criteria) this;
        }

        public Criteria andSkipUrlBetween(String value1, String value2) {
            addCriterion("skip_url between", value1, value2, "skipUrl");
            return (Criteria) this;
        }

        public Criteria andSkipUrlNotBetween(String value1, String value2) {
            addCriterion("skip_url not between", value1, value2, "skipUrl");
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
