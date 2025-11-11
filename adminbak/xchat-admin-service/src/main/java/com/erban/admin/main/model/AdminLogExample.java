package com.erban.admin.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminLogExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AdminLogExample() {
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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andOptUidIsNull() {
            addCriterion("opt_uid is null");
            return (Criteria) this;
        }

        public Criteria andOptUidIsNotNull() {
            addCriterion("opt_uid is not null");
            return (Criteria) this;
        }

        public Criteria andOptUidEqualTo(Integer value) {
            addCriterion("opt_uid =", value, "optUid");
            return (Criteria) this;
        }

        public Criteria andOptUidNotEqualTo(Integer value) {
            addCriterion("opt_uid <>", value, "optUid");
            return (Criteria) this;
        }

        public Criteria andOptUidGreaterThan(Integer value) {
            addCriterion("opt_uid >", value, "optUid");
            return (Criteria) this;
        }

        public Criteria andOptUidGreaterThanOrEqualTo(Integer value) {
            addCriterion("opt_uid >=", value, "optUid");
            return (Criteria) this;
        }

        public Criteria andOptUidLessThan(Integer value) {
            addCriterion("opt_uid <", value, "optUid");
            return (Criteria) this;
        }

        public Criteria andOptUidLessThanOrEqualTo(Integer value) {
            addCriterion("opt_uid <=", value, "optUid");
            return (Criteria) this;
        }

        public Criteria andOptUidIn(List<Integer> values) {
            addCriterion("opt_uid in", values, "optUid");
            return (Criteria) this;
        }

        public Criteria andOptUidNotIn(List<Integer> values) {
            addCriterion("opt_uid not in", values, "optUid");
            return (Criteria) this;
        }

        public Criteria andOptUidBetween(Integer value1, Integer value2) {
            addCriterion("opt_uid between", value1, value2, "optUid");
            return (Criteria) this;
        }

        public Criteria andOptUidNotBetween(Integer value1, Integer value2) {
            addCriterion("opt_uid not between", value1, value2, "optUid");
            return (Criteria) this;
        }

        public Criteria andOptClassIsNull() {
            addCriterion("opt_class is null");
            return (Criteria) this;
        }

        public Criteria andOptClassIsNotNull() {
            addCriterion("opt_class is not null");
            return (Criteria) this;
        }

        public Criteria andOptClassEqualTo(String value) {
            addCriterion("opt_class =", value, "optClass");
            return (Criteria) this;
        }

        public Criteria andOptClassNotEqualTo(String value) {
            addCriterion("opt_class <>", value, "optClass");
            return (Criteria) this;
        }

        public Criteria andOptClassGreaterThan(String value) {
            addCriterion("opt_class >", value, "optClass");
            return (Criteria) this;
        }

        public Criteria andOptClassGreaterThanOrEqualTo(String value) {
            addCriterion("opt_class >=", value, "optClass");
            return (Criteria) this;
        }

        public Criteria andOptClassLessThan(String value) {
            addCriterion("opt_class <", value, "optClass");
            return (Criteria) this;
        }

        public Criteria andOptClassLessThanOrEqualTo(String value) {
            addCriterion("opt_class <=", value, "optClass");
            return (Criteria) this;
        }

        public Criteria andOptClassLike(String value) {
            addCriterion("opt_class like", value, "optClass");
            return (Criteria) this;
        }

        public Criteria andOptClassNotLike(String value) {
            addCriterion("opt_class not like", value, "optClass");
            return (Criteria) this;
        }

        public Criteria andOptClassIn(List<String> values) {
            addCriterion("opt_class in", values, "optClass");
            return (Criteria) this;
        }

        public Criteria andOptClassNotIn(List<String> values) {
            addCriterion("opt_class not in", values, "optClass");
            return (Criteria) this;
        }

        public Criteria andOptClassBetween(String value1, String value2) {
            addCriterion("opt_class between", value1, value2, "optClass");
            return (Criteria) this;
        }

        public Criteria andOptClassNotBetween(String value1, String value2) {
            addCriterion("opt_class not between", value1, value2, "optClass");
            return (Criteria) this;
        }

        public Criteria andOptMethodIsNull() {
            addCriterion("opt_method is null");
            return (Criteria) this;
        }

        public Criteria andOptMethodIsNotNull() {
            addCriterion("opt_method is not null");
            return (Criteria) this;
        }

        public Criteria andOptMethodEqualTo(String value) {
            addCriterion("opt_method =", value, "optMethod");
            return (Criteria) this;
        }

        public Criteria andOptMethodNotEqualTo(String value) {
            addCriterion("opt_method <>", value, "optMethod");
            return (Criteria) this;
        }

        public Criteria andOptMethodGreaterThan(String value) {
            addCriterion("opt_method >", value, "optMethod");
            return (Criteria) this;
        }

        public Criteria andOptMethodGreaterThanOrEqualTo(String value) {
            addCriterion("opt_method >=", value, "optMethod");
            return (Criteria) this;
        }

        public Criteria andOptMethodLessThan(String value) {
            addCriterion("opt_method <", value, "optMethod");
            return (Criteria) this;
        }

        public Criteria andOptMethodLessThanOrEqualTo(String value) {
            addCriterion("opt_method <=", value, "optMethod");
            return (Criteria) this;
        }

        public Criteria andOptMethodLike(String value) {
            addCriterion("opt_method like", value, "optMethod");
            return (Criteria) this;
        }

        public Criteria andOptMethodNotLike(String value) {
            addCriterion("opt_method not like", value, "optMethod");
            return (Criteria) this;
        }

        public Criteria andOptMethodIn(List<String> values) {
            addCriterion("opt_method in", values, "optMethod");
            return (Criteria) this;
        }

        public Criteria andOptMethodNotIn(List<String> values) {
            addCriterion("opt_method not in", values, "optMethod");
            return (Criteria) this;
        }

        public Criteria andOptMethodBetween(String value1, String value2) {
            addCriterion("opt_method between", value1, value2, "optMethod");
            return (Criteria) this;
        }

        public Criteria andOptMethodNotBetween(String value1, String value2) {
            addCriterion("opt_method not between", value1, value2, "optMethod");
            return (Criteria) this;
        }

        public Criteria andOptMessIsNull() {
            addCriterion("opt_mess is null");
            return (Criteria) this;
        }

        public Criteria andOptMessIsNotNull() {
            addCriterion("opt_mess is not null");
            return (Criteria) this;
        }

        public Criteria andOptMessEqualTo(String value) {
            addCriterion("opt_mess =", value, "optMess");
            return (Criteria) this;
        }

        public Criteria andOptMessNotEqualTo(String value) {
            addCriterion("opt_mess <>", value, "optMess");
            return (Criteria) this;
        }

        public Criteria andOptMessGreaterThan(String value) {
            addCriterion("opt_mess >", value, "optMess");
            return (Criteria) this;
        }

        public Criteria andOptMessGreaterThanOrEqualTo(String value) {
            addCriterion("opt_mess >=", value, "optMess");
            return (Criteria) this;
        }

        public Criteria andOptMessLessThan(String value) {
            addCriterion("opt_mess <", value, "optMess");
            return (Criteria) this;
        }

        public Criteria andOptMessLessThanOrEqualTo(String value) {
            addCriterion("opt_mess <=", value, "optMess");
            return (Criteria) this;
        }

        public Criteria andOptMessLike(String value) {
            addCriterion("opt_mess like", value, "optMess");
            return (Criteria) this;
        }

        public Criteria andOptMessNotLike(String value) {
            addCriterion("opt_mess not like", value, "optMess");
            return (Criteria) this;
        }

        public Criteria andOptMessIn(List<String> values) {
            addCriterion("opt_mess in", values, "optMess");
            return (Criteria) this;
        }

        public Criteria andOptMessNotIn(List<String> values) {
            addCriterion("opt_mess not in", values, "optMess");
            return (Criteria) this;
        }

        public Criteria andOptMessBetween(String value1, String value2) {
            addCriterion("opt_mess between", value1, value2, "optMess");
            return (Criteria) this;
        }

        public Criteria andOptMessNotBetween(String value1, String value2) {
            addCriterion("opt_mess not between", value1, value2, "optMess");
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

        public Criteria andTmpintIsNull() {
            addCriterion("tmpint is null");
            return (Criteria) this;
        }

        public Criteria andTmpintIsNotNull() {
            addCriterion("tmpint is not null");
            return (Criteria) this;
        }

        public Criteria andTmpintEqualTo(Integer value) {
            addCriterion("tmpint =", value, "tmpint");
            return (Criteria) this;
        }

        public Criteria andTmpintNotEqualTo(Integer value) {
            addCriterion("tmpint <>", value, "tmpint");
            return (Criteria) this;
        }

        public Criteria andTmpintGreaterThan(Integer value) {
            addCriterion("tmpint >", value, "tmpint");
            return (Criteria) this;
        }

        public Criteria andTmpintGreaterThanOrEqualTo(Integer value) {
            addCriterion("tmpint >=", value, "tmpint");
            return (Criteria) this;
        }

        public Criteria andTmpintLessThan(Integer value) {
            addCriterion("tmpint <", value, "tmpint");
            return (Criteria) this;
        }

        public Criteria andTmpintLessThanOrEqualTo(Integer value) {
            addCriterion("tmpint <=", value, "tmpint");
            return (Criteria) this;
        }

        public Criteria andTmpintIn(List<Integer> values) {
            addCriterion("tmpint in", values, "tmpint");
            return (Criteria) this;
        }

        public Criteria andTmpintNotIn(List<Integer> values) {
            addCriterion("tmpint not in", values, "tmpint");
            return (Criteria) this;
        }

        public Criteria andTmpintBetween(Integer value1, Integer value2) {
            addCriterion("tmpint between", value1, value2, "tmpint");
            return (Criteria) this;
        }

        public Criteria andTmpintNotBetween(Integer value1, Integer value2) {
            addCriterion("tmpint not between", value1, value2, "tmpint");
            return (Criteria) this;
        }

        public Criteria andTmpstrIsNull() {
            addCriterion("tmpstr is null");
            return (Criteria) this;
        }

        public Criteria andTmpstrIsNotNull() {
            addCriterion("tmpstr is not null");
            return (Criteria) this;
        }

        public Criteria andTmpstrEqualTo(String value) {
            addCriterion("tmpstr =", value, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpstrNotEqualTo(String value) {
            addCriterion("tmpstr <>", value, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpstrGreaterThan(String value) {
            addCriterion("tmpstr >", value, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpstrGreaterThanOrEqualTo(String value) {
            addCriterion("tmpstr >=", value, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpstrLessThan(String value) {
            addCriterion("tmpstr <", value, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpstrLessThanOrEqualTo(String value) {
            addCriterion("tmpstr <=", value, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpstrLike(String value) {
            addCriterion("tmpstr like", value, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpstrNotLike(String value) {
            addCriterion("tmpstr not like", value, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpstrIn(List<String> values) {
            addCriterion("tmpstr in", values, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpstrNotIn(List<String> values) {
            addCriterion("tmpstr not in", values, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpstrBetween(String value1, String value2) {
            addCriterion("tmpstr between", value1, value2, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpstrNotBetween(String value1, String value2) {
            addCriterion("tmpstr not between", value1, value2, "tmpstr");
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
