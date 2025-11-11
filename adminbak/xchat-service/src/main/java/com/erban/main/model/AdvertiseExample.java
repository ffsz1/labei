package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdvertiseExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AdvertiseExample() {
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

        public Criteria andAdvIdIsNull() {
            addCriterion("adv_id is null");
            return (Criteria) this;
        }

        public Criteria andAdvIdIsNotNull() {
            addCriterion("adv_id is not null");
            return (Criteria) this;
        }

        public Criteria andAdvIdEqualTo(Integer value) {
            addCriterion("adv_id =", value, "advId");
            return (Criteria) this;
        }

        public Criteria andAdvIdNotEqualTo(Integer value) {
            addCriterion("adv_id <>", value, "advId");
            return (Criteria) this;
        }

        public Criteria andAdvIdGreaterThan(Integer value) {
            addCriterion("adv_id >", value, "advId");
            return (Criteria) this;
        }

        public Criteria andAdvIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("adv_id >=", value, "advId");
            return (Criteria) this;
        }

        public Criteria andAdvIdLessThan(Integer value) {
            addCriterion("adv_id <", value, "advId");
            return (Criteria) this;
        }

        public Criteria andAdvIdLessThanOrEqualTo(Integer value) {
            addCriterion("adv_id <=", value, "advId");
            return (Criteria) this;
        }

        public Criteria andAdvIdIn(List<Integer> values) {
            addCriterion("adv_id in", values, "advId");
            return (Criteria) this;
        }

        public Criteria andAdvIdNotIn(List<Integer> values) {
            addCriterion("adv_id not in", values, "advId");
            return (Criteria) this;
        }

        public Criteria andAdvIdBetween(Integer value1, Integer value2) {
            addCriterion("adv_id between", value1, value2, "advId");
            return (Criteria) this;
        }

        public Criteria andAdvIdNotBetween(Integer value1, Integer value2) {
            addCriterion("adv_id not between", value1, value2, "advId");
            return (Criteria) this;
        }

        public Criteria andAdvNameIsNull() {
            addCriterion("adv_name is null");
            return (Criteria) this;
        }

        public Criteria andAdvNameIsNotNull() {
            addCriterion("adv_name is not null");
            return (Criteria) this;
        }

        public Criteria andAdvNameEqualTo(String value) {
            addCriterion("adv_name =", value, "advName");
            return (Criteria) this;
        }

        public Criteria andAdvNameNotEqualTo(String value) {
            addCriterion("adv_name <>", value, "advName");
            return (Criteria) this;
        }

        public Criteria andAdvNameGreaterThan(String value) {
            addCriterion("adv_name >", value, "advName");
            return (Criteria) this;
        }

        public Criteria andAdvNameGreaterThanOrEqualTo(String value) {
            addCriterion("adv_name >=", value, "advName");
            return (Criteria) this;
        }

        public Criteria andAdvNameLessThan(String value) {
            addCriterion("adv_name <", value, "advName");
            return (Criteria) this;
        }

        public Criteria andAdvNameLessThanOrEqualTo(String value) {
            addCriterion("adv_name <=", value, "advName");
            return (Criteria) this;
        }

        public Criteria andAdvNameLike(String value) {
            addCriterion("adv_name like", value, "advName");
            return (Criteria) this;
        }

        public Criteria andAdvNameNotLike(String value) {
            addCriterion("adv_name not like", value, "advName");
            return (Criteria) this;
        }

        public Criteria andAdvNameIn(List<String> values) {
            addCriterion("adv_name in", values, "advName");
            return (Criteria) this;
        }

        public Criteria andAdvNameNotIn(List<String> values) {
            addCriterion("adv_name not in", values, "advName");
            return (Criteria) this;
        }

        public Criteria andAdvNameBetween(String value1, String value2) {
            addCriterion("adv_name between", value1, value2, "advName");
            return (Criteria) this;
        }

        public Criteria andAdvNameNotBetween(String value1, String value2) {
            addCriterion("adv_name not between", value1, value2, "advName");
            return (Criteria) this;
        }

        public Criteria andAdvIconIsNull() {
            addCriterion("adv_icon is null");
            return (Criteria) this;
        }

        public Criteria andAdvIconIsNotNull() {
            addCriterion("adv_icon is not null");
            return (Criteria) this;
        }

        public Criteria andAdvIconEqualTo(String value) {
            addCriterion("adv_icon =", value, "advIcon");
            return (Criteria) this;
        }

        public Criteria andAdvIconNotEqualTo(String value) {
            addCriterion("adv_icon <>", value, "advIcon");
            return (Criteria) this;
        }

        public Criteria andAdvIconGreaterThan(String value) {
            addCriterion("adv_icon >", value, "advIcon");
            return (Criteria) this;
        }

        public Criteria andAdvIconGreaterThanOrEqualTo(String value) {
            addCriterion("adv_icon >=", value, "advIcon");
            return (Criteria) this;
        }

        public Criteria andAdvIconLessThan(String value) {
            addCriterion("adv_icon <", value, "advIcon");
            return (Criteria) this;
        }

        public Criteria andAdvIconLessThanOrEqualTo(String value) {
            addCriterion("adv_icon <=", value, "advIcon");
            return (Criteria) this;
        }

        public Criteria andAdvIconLike(String value) {
            addCriterion("adv_icon like", value, "advIcon");
            return (Criteria) this;
        }

        public Criteria andAdvIconNotLike(String value) {
            addCriterion("adv_icon not like", value, "advIcon");
            return (Criteria) this;
        }

        public Criteria andAdvIconIn(List<String> values) {
            addCriterion("adv_icon in", values, "advIcon");
            return (Criteria) this;
        }

        public Criteria andAdvIconNotIn(List<String> values) {
            addCriterion("adv_icon not in", values, "advIcon");
            return (Criteria) this;
        }

        public Criteria andAdvIconBetween(String value1, String value2) {
            addCriterion("adv_icon between", value1, value2, "advIcon");
            return (Criteria) this;
        }

        public Criteria andAdvIconNotBetween(String value1, String value2) {
            addCriterion("adv_icon not between", value1, value2, "advIcon");
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

        public Criteria andSkipUriIsNull() {
            addCriterion("skip_uri is null");
            return (Criteria) this;
        }

        public Criteria andSkipUriIsNotNull() {
            addCriterion("skip_uri is not null");
            return (Criteria) this;
        }

        public Criteria andSkipUriEqualTo(String value) {
            addCriterion("skip_uri =", value, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSkipUriNotEqualTo(String value) {
            addCriterion("skip_uri <>", value, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSkipUriGreaterThan(String value) {
            addCriterion("skip_uri >", value, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSkipUriGreaterThanOrEqualTo(String value) {
            addCriterion("skip_uri >=", value, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSkipUriLessThan(String value) {
            addCriterion("skip_uri <", value, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSkipUriLessThanOrEqualTo(String value) {
            addCriterion("skip_uri <=", value, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSkipUriLike(String value) {
            addCriterion("skip_uri like", value, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSkipUriNotLike(String value) {
            addCriterion("skip_uri not like", value, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSkipUriIn(List<String> values) {
            addCriterion("skip_uri in", values, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSkipUriNotIn(List<String> values) {
            addCriterion("skip_uri not in", values, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSkipUriBetween(String value1, String value2) {
            addCriterion("skip_uri between", value1, value2, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSkipUriNotBetween(String value1, String value2) {
            addCriterion("skip_uri not between", value1, value2, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSeqNoIsNull() {
            addCriterion("seq_no is null");
            return (Criteria) this;
        }

        public Criteria andSeqNoIsNotNull() {
            addCriterion("seq_no is not null");
            return (Criteria) this;
        }

        public Criteria andSeqNoEqualTo(Integer value) {
            addCriterion("seq_no =", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoNotEqualTo(Integer value) {
            addCriterion("seq_no <>", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoGreaterThan(Integer value) {
            addCriterion("seq_no >", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoGreaterThanOrEqualTo(Integer value) {
            addCriterion("seq_no >=", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoLessThan(Integer value) {
            addCriterion("seq_no <", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoLessThanOrEqualTo(Integer value) {
            addCriterion("seq_no <=", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoIn(List<Integer> values) {
            addCriterion("seq_no in", values, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoNotIn(List<Integer> values) {
            addCriterion("seq_no not in", values, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoBetween(Integer value1, Integer value2) {
            addCriterion("seq_no between", value1, value2, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoNotBetween(Integer value1, Integer value2) {
            addCriterion("seq_no not between", value1, value2, "seqNo");
            return (Criteria) this;
        }

        public Criteria andAdvStatusIsNull() {
            addCriterion("adv_status is null");
            return (Criteria) this;
        }

        public Criteria andAdvStatusIsNotNull() {
            addCriterion("adv_status is not null");
            return (Criteria) this;
        }

        public Criteria andAdvStatusEqualTo(Byte value) {
            addCriterion("adv_status =", value, "advStatus");
            return (Criteria) this;
        }

        public Criteria andAdvStatusNotEqualTo(Byte value) {
            addCriterion("adv_status <>", value, "advStatus");
            return (Criteria) this;
        }

        public Criteria andAdvStatusGreaterThan(Byte value) {
            addCriterion("adv_status >", value, "advStatus");
            return (Criteria) this;
        }

        public Criteria andAdvStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("adv_status >=", value, "advStatus");
            return (Criteria) this;
        }

        public Criteria andAdvStatusLessThan(Byte value) {
            addCriterion("adv_status <", value, "advStatus");
            return (Criteria) this;
        }

        public Criteria andAdvStatusLessThanOrEqualTo(Byte value) {
            addCriterion("adv_status <=", value, "advStatus");
            return (Criteria) this;
        }

        public Criteria andAdvStatusIn(List<Byte> values) {
            addCriterion("adv_status in", values, "advStatus");
            return (Criteria) this;
        }

        public Criteria andAdvStatusNotIn(List<Byte> values) {
            addCriterion("adv_status not in", values, "advStatus");
            return (Criteria) this;
        }

        public Criteria andAdvStatusBetween(Byte value1, Byte value2) {
            addCriterion("adv_status between", value1, value2, "advStatus");
            return (Criteria) this;
        }

        public Criteria andAdvStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("adv_status not between", value1, value2, "advStatus");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNull() {
            addCriterion("start_time is null");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNotNull() {
            addCriterion("start_time is not null");
            return (Criteria) this;
        }

        public Criteria andStartTimeEqualTo(Date value) {
            addCriterion("start_time =", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotEqualTo(Date value) {
            addCriterion("start_time <>", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThan(Date value) {
            addCriterion("start_time >", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("start_time >=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThan(Date value) {
            addCriterion("start_time <", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThanOrEqualTo(Date value) {
            addCriterion("start_time <=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeIn(List<Date> values) {
            addCriterion("start_time in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotIn(List<Date> values) {
            addCriterion("start_time not in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeBetween(Date value1, Date value2) {
            addCriterion("start_time between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotBetween(Date value1, Date value2) {
            addCriterion("start_time not between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNull() {
            addCriterion("end_time is null");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNotNull() {
            addCriterion("end_time is not null");
            return (Criteria) this;
        }

        public Criteria andEndTimeEqualTo(Date value) {
            addCriterion("end_time =", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotEqualTo(Date value) {
            addCriterion("end_time <>", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThan(Date value) {
            addCriterion("end_time >", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("end_time >=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThan(Date value) {
            addCriterion("end_time <", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThanOrEqualTo(Date value) {
            addCriterion("end_time <=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIn(List<Date> values) {
            addCriterion("end_time in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotIn(List<Date> values) {
            addCriterion("end_time not in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeBetween(Date value1, Date value2) {
            addCriterion("end_time between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotBetween(Date value1, Date value2) {
            addCriterion("end_time not between", value1, value2, "endTime");
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
