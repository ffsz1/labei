package com.erban.admin.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestPurseExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TestPurseExample() {
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

        public Criteria andErbanNoEqualTo(Long value) {
            addCriterion("erban_no =", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoNotEqualTo(Long value) {
            addCriterion("erban_no <>", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoGreaterThan(Long value) {
            addCriterion("erban_no >", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoGreaterThanOrEqualTo(Long value) {
            addCriterion("erban_no >=", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoLessThan(Long value) {
            addCriterion("erban_no <", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoLessThanOrEqualTo(Long value) {
            addCriterion("erban_no <=", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoIn(List<Long> values) {
            addCriterion("erban_no in", values, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoNotIn(List<Long> values) {
            addCriterion("erban_no not in", values, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoBetween(Long value1, Long value2) {
            addCriterion("erban_no between", value1, value2, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoNotBetween(Long value1, Long value2) {
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

        public Criteria andMysqlGoldIsNull() {
            addCriterion("mysql_gold is null");
            return (Criteria) this;
        }

        public Criteria andMysqlGoldIsNotNull() {
            addCriterion("mysql_gold is not null");
            return (Criteria) this;
        }

        public Criteria andMysqlGoldEqualTo(Long value) {
            addCriterion("mysql_gold =", value, "mysqlGold");
            return (Criteria) this;
        }

        public Criteria andMysqlGoldNotEqualTo(Long value) {
            addCriterion("mysql_gold <>", value, "mysqlGold");
            return (Criteria) this;
        }

        public Criteria andMysqlGoldGreaterThan(Long value) {
            addCriterion("mysql_gold >", value, "mysqlGold");
            return (Criteria) this;
        }

        public Criteria andMysqlGoldGreaterThanOrEqualTo(Long value) {
            addCriterion("mysql_gold >=", value, "mysqlGold");
            return (Criteria) this;
        }

        public Criteria andMysqlGoldLessThan(Long value) {
            addCriterion("mysql_gold <", value, "mysqlGold");
            return (Criteria) this;
        }

        public Criteria andMysqlGoldLessThanOrEqualTo(Long value) {
            addCriterion("mysql_gold <=", value, "mysqlGold");
            return (Criteria) this;
        }

        public Criteria andMysqlGoldIn(List<Long> values) {
            addCriterion("mysql_gold in", values, "mysqlGold");
            return (Criteria) this;
        }

        public Criteria andMysqlGoldNotIn(List<Long> values) {
            addCriterion("mysql_gold not in", values, "mysqlGold");
            return (Criteria) this;
        }

        public Criteria andMysqlGoldBetween(Long value1, Long value2) {
            addCriterion("mysql_gold between", value1, value2, "mysqlGold");
            return (Criteria) this;
        }

        public Criteria andMysqlGoldNotBetween(Long value1, Long value2) {
            addCriterion("mysql_gold not between", value1, value2, "mysqlGold");
            return (Criteria) this;
        }

        public Criteria andCacheGoldIsNull() {
            addCriterion("cache_gold is null");
            return (Criteria) this;
        }

        public Criteria andCacheGoldIsNotNull() {
            addCriterion("cache_gold is not null");
            return (Criteria) this;
        }

        public Criteria andCacheGoldEqualTo(Long value) {
            addCriterion("cache_gold =", value, "cacheGold");
            return (Criteria) this;
        }

        public Criteria andCacheGoldNotEqualTo(Long value) {
            addCriterion("cache_gold <>", value, "cacheGold");
            return (Criteria) this;
        }

        public Criteria andCacheGoldGreaterThan(Long value) {
            addCriterion("cache_gold >", value, "cacheGold");
            return (Criteria) this;
        }

        public Criteria andCacheGoldGreaterThanOrEqualTo(Long value) {
            addCriterion("cache_gold >=", value, "cacheGold");
            return (Criteria) this;
        }

        public Criteria andCacheGoldLessThan(Long value) {
            addCriterion("cache_gold <", value, "cacheGold");
            return (Criteria) this;
        }

        public Criteria andCacheGoldLessThanOrEqualTo(Long value) {
            addCriterion("cache_gold <=", value, "cacheGold");
            return (Criteria) this;
        }

        public Criteria andCacheGoldIn(List<Long> values) {
            addCriterion("cache_gold in", values, "cacheGold");
            return (Criteria) this;
        }

        public Criteria andCacheGoldNotIn(List<Long> values) {
            addCriterion("cache_gold not in", values, "cacheGold");
            return (Criteria) this;
        }

        public Criteria andCacheGoldBetween(Long value1, Long value2) {
            addCriterion("cache_gold between", value1, value2, "cacheGold");
            return (Criteria) this;
        }

        public Criteria andCacheGoldNotBetween(Long value1, Long value2) {
            addCriterion("cache_gold not between", value1, value2, "cacheGold");
            return (Criteria) this;
        }

        public Criteria andMysqlDiamondIsNull() {
            addCriterion("mysql_diamond is null");
            return (Criteria) this;
        }

        public Criteria andMysqlDiamondIsNotNull() {
            addCriterion("mysql_diamond is not null");
            return (Criteria) this;
        }

        public Criteria andMysqlDiamondEqualTo(Double value) {
            addCriterion("mysql_diamond =", value, "mysqlDiamond");
            return (Criteria) this;
        }

        public Criteria andMysqlDiamondNotEqualTo(Double value) {
            addCriterion("mysql_diamond <>", value, "mysqlDiamond");
            return (Criteria) this;
        }

        public Criteria andMysqlDiamondGreaterThan(Double value) {
            addCriterion("mysql_diamond >", value, "mysqlDiamond");
            return (Criteria) this;
        }

        public Criteria andMysqlDiamondGreaterThanOrEqualTo(Double value) {
            addCriterion("mysql_diamond >=", value, "mysqlDiamond");
            return (Criteria) this;
        }

        public Criteria andMysqlDiamondLessThan(Double value) {
            addCriterion("mysql_diamond <", value, "mysqlDiamond");
            return (Criteria) this;
        }

        public Criteria andMysqlDiamondLessThanOrEqualTo(Double value) {
            addCriterion("mysql_diamond <=", value, "mysqlDiamond");
            return (Criteria) this;
        }

        public Criteria andMysqlDiamondIn(List<Double> values) {
            addCriterion("mysql_diamond in", values, "mysqlDiamond");
            return (Criteria) this;
        }

        public Criteria andMysqlDiamondNotIn(List<Double> values) {
            addCriterion("mysql_diamond not in", values, "mysqlDiamond");
            return (Criteria) this;
        }

        public Criteria andMysqlDiamondBetween(Double value1, Double value2) {
            addCriterion("mysql_diamond between", value1, value2, "mysqlDiamond");
            return (Criteria) this;
        }

        public Criteria andMysqlDiamondNotBetween(Double value1, Double value2) {
            addCriterion("mysql_diamond not between", value1, value2, "mysqlDiamond");
            return (Criteria) this;
        }

        public Criteria andCacheDiamondIsNull() {
            addCriterion("cache_diamond is null");
            return (Criteria) this;
        }

        public Criteria andCacheDiamondIsNotNull() {
            addCriterion("cache_diamond is not null");
            return (Criteria) this;
        }

        public Criteria andCacheDiamondEqualTo(Double value) {
            addCriterion("cache_diamond =", value, "cacheDiamond");
            return (Criteria) this;
        }

        public Criteria andCacheDiamondNotEqualTo(Double value) {
            addCriterion("cache_diamond <>", value, "cacheDiamond");
            return (Criteria) this;
        }

        public Criteria andCacheDiamondGreaterThan(Double value) {
            addCriterion("cache_diamond >", value, "cacheDiamond");
            return (Criteria) this;
        }

        public Criteria andCacheDiamondGreaterThanOrEqualTo(Double value) {
            addCriterion("cache_diamond >=", value, "cacheDiamond");
            return (Criteria) this;
        }

        public Criteria andCacheDiamondLessThan(Double value) {
            addCriterion("cache_diamond <", value, "cacheDiamond");
            return (Criteria) this;
        }

        public Criteria andCacheDiamondLessThanOrEqualTo(Double value) {
            addCriterion("cache_diamond <=", value, "cacheDiamond");
            return (Criteria) this;
        }

        public Criteria andCacheDiamondIn(List<Double> values) {
            addCriterion("cache_diamond in", values, "cacheDiamond");
            return (Criteria) this;
        }

        public Criteria andCacheDiamondNotIn(List<Double> values) {
            addCriterion("cache_diamond not in", values, "cacheDiamond");
            return (Criteria) this;
        }

        public Criteria andCacheDiamondBetween(Double value1, Double value2) {
            addCriterion("cache_diamond between", value1, value2, "cacheDiamond");
            return (Criteria) this;
        }

        public Criteria andCacheDiamondNotBetween(Double value1, Double value2) {
            addCriterion("cache_diamond not between", value1, value2, "cacheDiamond");
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

        public Criteria andVersionIsNull() {
            addCriterion("version is null");
            return (Criteria) this;
        }

        public Criteria andVersionIsNotNull() {
            addCriterion("version is not null");
            return (Criteria) this;
        }

        public Criteria andVersionEqualTo(Long value) {
            addCriterion("version =", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotEqualTo(Long value) {
            addCriterion("version <>", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThan(Long value) {
            addCriterion("version >", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThanOrEqualTo(Long value) {
            addCriterion("version >=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThan(Long value) {
            addCriterion("version <", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThanOrEqualTo(Long value) {
            addCriterion("version <=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionIn(List<Long> values) {
            addCriterion("version in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotIn(List<Long> values) {
            addCriterion("version not in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionBetween(Long value1, Long value2) {
            addCriterion("version between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotBetween(Long value1, Long value2) {
            addCriterion("version not between", value1, value2, "version");
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
