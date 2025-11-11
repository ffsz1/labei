package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LevelCharmExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public LevelCharmExample() {
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

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andLevelSeqIsNull() {
            addCriterion("level_seq is null");
            return (Criteria) this;
        }

        public Criteria andLevelSeqIsNotNull() {
            addCriterion("level_seq is not null");
            return (Criteria) this;
        }

        public Criteria andLevelSeqEqualTo(Integer value) {
            addCriterion("level_seq =", value, "levelSeq");
            return (Criteria) this;
        }

        public Criteria andLevelSeqNotEqualTo(Integer value) {
            addCriterion("level_seq <>", value, "levelSeq");
            return (Criteria) this;
        }

        public Criteria andLevelSeqGreaterThan(Integer value) {
            addCriterion("level_seq >", value, "levelSeq");
            return (Criteria) this;
        }

        public Criteria andLevelSeqGreaterThanOrEqualTo(Integer value) {
            addCriterion("level_seq >=", value, "levelSeq");
            return (Criteria) this;
        }

        public Criteria andLevelSeqLessThan(Integer value) {
            addCriterion("level_seq <", value, "levelSeq");
            return (Criteria) this;
        }

        public Criteria andLevelSeqLessThanOrEqualTo(Integer value) {
            addCriterion("level_seq <=", value, "levelSeq");
            return (Criteria) this;
        }

        public Criteria andLevelSeqIn(List<Integer> values) {
            addCriterion("level_seq in", values, "levelSeq");
            return (Criteria) this;
        }

        public Criteria andLevelSeqNotIn(List<Integer> values) {
            addCriterion("level_seq not in", values, "levelSeq");
            return (Criteria) this;
        }

        public Criteria andLevelSeqBetween(Integer value1, Integer value2) {
            addCriterion("level_seq between", value1, value2, "levelSeq");
            return (Criteria) this;
        }

        public Criteria andLevelSeqNotBetween(Integer value1, Integer value2) {
            addCriterion("level_seq not between", value1, value2, "levelSeq");
            return (Criteria) this;
        }

        public Criteria andLevelNameIsNull() {
            addCriterion("level_name is null");
            return (Criteria) this;
        }

        public Criteria andLevelNameIsNotNull() {
            addCriterion("level_name is not null");
            return (Criteria) this;
        }

        public Criteria andLevelNameEqualTo(String value) {
            addCriterion("level_name =", value, "levelName");
            return (Criteria) this;
        }

        public Criteria andLevelNameNotEqualTo(String value) {
            addCriterion("level_name <>", value, "levelName");
            return (Criteria) this;
        }

        public Criteria andLevelNameGreaterThan(String value) {
            addCriterion("level_name >", value, "levelName");
            return (Criteria) this;
        }

        public Criteria andLevelNameGreaterThanOrEqualTo(String value) {
            addCriterion("level_name >=", value, "levelName");
            return (Criteria) this;
        }

        public Criteria andLevelNameLessThan(String value) {
            addCriterion("level_name <", value, "levelName");
            return (Criteria) this;
        }

        public Criteria andLevelNameLessThanOrEqualTo(String value) {
            addCriterion("level_name <=", value, "levelName");
            return (Criteria) this;
        }

        public Criteria andLevelNameLike(String value) {
            addCriterion("level_name like", value, "levelName");
            return (Criteria) this;
        }

        public Criteria andLevelNameNotLike(String value) {
            addCriterion("level_name not like", value, "levelName");
            return (Criteria) this;
        }

        public Criteria andLevelNameIn(List<String> values) {
            addCriterion("level_name in", values, "levelName");
            return (Criteria) this;
        }

        public Criteria andLevelNameNotIn(List<String> values) {
            addCriterion("level_name not in", values, "levelName");
            return (Criteria) this;
        }

        public Criteria andLevelNameBetween(String value1, String value2) {
            addCriterion("level_name between", value1, value2, "levelName");
            return (Criteria) this;
        }

        public Criteria andLevelNameNotBetween(String value1, String value2) {
            addCriterion("level_name not between", value1, value2, "levelName");
            return (Criteria) this;
        }

        public Criteria andLevelGrpIsNull() {
            addCriterion("level_grp is null");
            return (Criteria) this;
        }

        public Criteria andLevelGrpIsNotNull() {
            addCriterion("level_grp is not null");
            return (Criteria) this;
        }

        public Criteria andLevelGrpEqualTo(String value) {
            addCriterion("level_grp =", value, "levelGrp");
            return (Criteria) this;
        }

        public Criteria andLevelGrpNotEqualTo(String value) {
            addCriterion("level_grp <>", value, "levelGrp");
            return (Criteria) this;
        }

        public Criteria andLevelGrpGreaterThan(String value) {
            addCriterion("level_grp >", value, "levelGrp");
            return (Criteria) this;
        }

        public Criteria andLevelGrpGreaterThanOrEqualTo(String value) {
            addCriterion("level_grp >=", value, "levelGrp");
            return (Criteria) this;
        }

        public Criteria andLevelGrpLessThan(String value) {
            addCriterion("level_grp <", value, "levelGrp");
            return (Criteria) this;
        }

        public Criteria andLevelGrpLessThanOrEqualTo(String value) {
            addCriterion("level_grp <=", value, "levelGrp");
            return (Criteria) this;
        }

        public Criteria andLevelGrpLike(String value) {
            addCriterion("level_grp like", value, "levelGrp");
            return (Criteria) this;
        }

        public Criteria andLevelGrpNotLike(String value) {
            addCriterion("level_grp not like", value, "levelGrp");
            return (Criteria) this;
        }

        public Criteria andLevelGrpIn(List<String> values) {
            addCriterion("level_grp in", values, "levelGrp");
            return (Criteria) this;
        }

        public Criteria andLevelGrpNotIn(List<String> values) {
            addCriterion("level_grp not in", values, "levelGrp");
            return (Criteria) this;
        }

        public Criteria andLevelGrpBetween(String value1, String value2) {
            addCriterion("level_grp between", value1, value2, "levelGrp");
            return (Criteria) this;
        }

        public Criteria andLevelGrpNotBetween(String value1, String value2) {
            addCriterion("level_grp not between", value1, value2, "levelGrp");
            return (Criteria) this;
        }

        public Criteria andAmountIsNull() {
            addCriterion("amount is null");
            return (Criteria) this;
        }

        public Criteria andAmountIsNotNull() {
            addCriterion("amount is not null");
            return (Criteria) this;
        }

        public Criteria andAmountEqualTo(Long value) {
            addCriterion("amount =", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotEqualTo(Long value) {
            addCriterion("amount <>", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThan(Long value) {
            addCriterion("amount >", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("amount >=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThan(Long value) {
            addCriterion("amount <", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThanOrEqualTo(Long value) {
            addCriterion("amount <=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountIn(List<Long> values) {
            addCriterion("amount in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotIn(List<Long> values) {
            addCriterion("amount not in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountBetween(Long value1, Long value2) {
            addCriterion("amount between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotBetween(Long value1, Long value2) {
            addCriterion("amount not between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andNeedMinIsNull() {
            addCriterion("need_min is null");
            return (Criteria) this;
        }

        public Criteria andNeedMinIsNotNull() {
            addCriterion("need_min is not null");
            return (Criteria) this;
        }

        public Criteria andNeedMinEqualTo(Long value) {
            addCriterion("need_min =", value, "needMin");
            return (Criteria) this;
        }

        public Criteria andNeedMinNotEqualTo(Long value) {
            addCriterion("need_min <>", value, "needMin");
            return (Criteria) this;
        }

        public Criteria andNeedMinGreaterThan(Long value) {
            addCriterion("need_min >", value, "needMin");
            return (Criteria) this;
        }

        public Criteria andNeedMinGreaterThanOrEqualTo(Long value) {
            addCriterion("need_min >=", value, "needMin");
            return (Criteria) this;
        }

        public Criteria andNeedMinLessThan(Long value) {
            addCriterion("need_min <", value, "needMin");
            return (Criteria) this;
        }

        public Criteria andNeedMinLessThanOrEqualTo(Long value) {
            addCriterion("need_min <=", value, "needMin");
            return (Criteria) this;
        }

        public Criteria andNeedMinIn(List<Long> values) {
            addCriterion("need_min in", values, "needMin");
            return (Criteria) this;
        }

        public Criteria andNeedMinNotIn(List<Long> values) {
            addCriterion("need_min not in", values, "needMin");
            return (Criteria) this;
        }

        public Criteria andNeedMinBetween(Long value1, Long value2) {
            addCriterion("need_min between", value1, value2, "needMin");
            return (Criteria) this;
        }

        public Criteria andNeedMinNotBetween(Long value1, Long value2) {
            addCriterion("need_min not between", value1, value2, "needMin");
            return (Criteria) this;
        }

        public Criteria andNeedMaxIsNull() {
            addCriterion("need_max is null");
            return (Criteria) this;
        }

        public Criteria andNeedMaxIsNotNull() {
            addCriterion("need_max is not null");
            return (Criteria) this;
        }

        public Criteria andNeedMaxEqualTo(Long value) {
            addCriterion("need_max =", value, "needMax");
            return (Criteria) this;
        }

        public Criteria andNeedMaxNotEqualTo(Long value) {
            addCriterion("need_max <>", value, "needMax");
            return (Criteria) this;
        }

        public Criteria andNeedMaxGreaterThan(Long value) {
            addCriterion("need_max >", value, "needMax");
            return (Criteria) this;
        }

        public Criteria andNeedMaxGreaterThanOrEqualTo(Long value) {
            addCriterion("need_max >=", value, "needMax");
            return (Criteria) this;
        }

        public Criteria andNeedMaxLessThan(Long value) {
            addCriterion("need_max <", value, "needMax");
            return (Criteria) this;
        }

        public Criteria andNeedMaxLessThanOrEqualTo(Long value) {
            addCriterion("need_max <=", value, "needMax");
            return (Criteria) this;
        }

        public Criteria andNeedMaxIn(List<Long> values) {
            addCriterion("need_max in", values, "needMax");
            return (Criteria) this;
        }

        public Criteria andNeedMaxNotIn(List<Long> values) {
            addCriterion("need_max not in", values, "needMax");
            return (Criteria) this;
        }

        public Criteria andNeedMaxBetween(Long value1, Long value2) {
            addCriterion("need_max between", value1, value2, "needMax");
            return (Criteria) this;
        }

        public Criteria andNeedMaxNotBetween(Long value1, Long value2) {
            addCriterion("need_max not between", value1, value2, "needMax");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Byte value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Byte value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Byte value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Byte value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Byte value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Byte> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Byte> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Byte value1, Byte value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andBroadcastIsNull() {
            addCriterion("broadcast is null");
            return (Criteria) this;
        }

        public Criteria andBroadcastIsNotNull() {
            addCriterion("broadcast is not null");
            return (Criteria) this;
        }

        public Criteria andBroadcastEqualTo(Byte value) {
            addCriterion("broadcast =", value, "broadcast");
            return (Criteria) this;
        }

        public Criteria andBroadcastNotEqualTo(Byte value) {
            addCriterion("broadcast <>", value, "broadcast");
            return (Criteria) this;
        }

        public Criteria andBroadcastGreaterThan(Byte value) {
            addCriterion("broadcast >", value, "broadcast");
            return (Criteria) this;
        }

        public Criteria andBroadcastGreaterThanOrEqualTo(Byte value) {
            addCriterion("broadcast >=", value, "broadcast");
            return (Criteria) this;
        }

        public Criteria andBroadcastLessThan(Byte value) {
            addCriterion("broadcast <", value, "broadcast");
            return (Criteria) this;
        }

        public Criteria andBroadcastLessThanOrEqualTo(Byte value) {
            addCriterion("broadcast <=", value, "broadcast");
            return (Criteria) this;
        }

        public Criteria andBroadcastIn(List<Byte> values) {
            addCriterion("broadcast in", values, "broadcast");
            return (Criteria) this;
        }

        public Criteria andBroadcastNotIn(List<Byte> values) {
            addCriterion("broadcast not in", values, "broadcast");
            return (Criteria) this;
        }

        public Criteria andBroadcastBetween(Byte value1, Byte value2) {
            addCriterion("broadcast between", value1, value2, "broadcast");
            return (Criteria) this;
        }

        public Criteria andBroadcastNotBetween(Byte value1, Byte value2) {
            addCriterion("broadcast not between", value1, value2, "broadcast");
            return (Criteria) this;
        }

        public Criteria andInteractionIsNull() {
            addCriterion("interaction is null");
            return (Criteria) this;
        }

        public Criteria andInteractionIsNotNull() {
            addCriterion("interaction is not null");
            return (Criteria) this;
        }

        public Criteria andInteractionEqualTo(Byte value) {
            addCriterion("interaction =", value, "interaction");
            return (Criteria) this;
        }

        public Criteria andInteractionNotEqualTo(Byte value) {
            addCriterion("interaction <>", value, "interaction");
            return (Criteria) this;
        }

        public Criteria andInteractionGreaterThan(Byte value) {
            addCriterion("interaction >", value, "interaction");
            return (Criteria) this;
        }

        public Criteria andInteractionGreaterThanOrEqualTo(Byte value) {
            addCriterion("interaction >=", value, "interaction");
            return (Criteria) this;
        }

        public Criteria andInteractionLessThan(Byte value) {
            addCriterion("interaction <", value, "interaction");
            return (Criteria) this;
        }

        public Criteria andInteractionLessThanOrEqualTo(Byte value) {
            addCriterion("interaction <=", value, "interaction");
            return (Criteria) this;
        }

        public Criteria andInteractionIn(List<Byte> values) {
            addCriterion("interaction in", values, "interaction");
            return (Criteria) this;
        }

        public Criteria andInteractionNotIn(List<Byte> values) {
            addCriterion("interaction not in", values, "interaction");
            return (Criteria) this;
        }

        public Criteria andInteractionBetween(Byte value1, Byte value2) {
            addCriterion("interaction between", value1, value2, "interaction");
            return (Criteria) this;
        }

        public Criteria andInteractionNotBetween(Byte value1, Byte value2) {
            addCriterion("interaction not between", value1, value2, "interaction");
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
