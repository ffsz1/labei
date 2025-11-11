package com.erban.main.model;

import java.util.ArrayList;
import java.util.List;

public class McoinMissionExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public McoinMissionExample() {
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

        public Criteria andMissionNameIsNull() {
            addCriterion("mission_name is null");
            return (Criteria) this;
        }

        public Criteria andMissionNameIsNotNull() {
            addCriterion("mission_name is not null");
            return (Criteria) this;
        }

        public Criteria andMissionNameEqualTo(String value) {
            addCriterion("mission_name =", value, "missionName");
            return (Criteria) this;
        }

        public Criteria andMissionNameNotEqualTo(String value) {
            addCriterion("mission_name <>", value, "missionName");
            return (Criteria) this;
        }

        public Criteria andMissionNameGreaterThan(String value) {
            addCriterion("mission_name >", value, "missionName");
            return (Criteria) this;
        }

        public Criteria andMissionNameGreaterThanOrEqualTo(String value) {
            addCriterion("mission_name >=", value, "missionName");
            return (Criteria) this;
        }

        public Criteria andMissionNameLessThan(String value) {
            addCriterion("mission_name <", value, "missionName");
            return (Criteria) this;
        }

        public Criteria andMissionNameLessThanOrEqualTo(String value) {
            addCriterion("mission_name <=", value, "missionName");
            return (Criteria) this;
        }

        public Criteria andMissionNameLike(String value) {
            addCriterion("mission_name like", value, "missionName");
            return (Criteria) this;
        }

        public Criteria andMissionNameNotLike(String value) {
            addCriterion("mission_name not like", value, "missionName");
            return (Criteria) this;
        }

        public Criteria andMissionNameIn(List<String> values) {
            addCriterion("mission_name in", values, "missionName");
            return (Criteria) this;
        }

        public Criteria andMissionNameNotIn(List<String> values) {
            addCriterion("mission_name not in", values, "missionName");
            return (Criteria) this;
        }

        public Criteria andMissionNameBetween(String value1, String value2) {
            addCriterion("mission_name between", value1, value2, "missionName");
            return (Criteria) this;
        }

        public Criteria andMissionNameNotBetween(String value1, String value2) {
            addCriterion("mission_name not between", value1, value2, "missionName");
            return (Criteria) this;
        }

        public Criteria andMcoinAmountIsNull() {
            addCriterion("mcoin_amount is null");
            return (Criteria) this;
        }

        public Criteria andMcoinAmountIsNotNull() {
            addCriterion("mcoin_amount is not null");
            return (Criteria) this;
        }

        public Criteria andMcoinAmountEqualTo(Integer value) {
            addCriterion("mcoin_amount =", value, "mcoinAmount");
            return (Criteria) this;
        }

        public Criteria andMcoinAmountNotEqualTo(Integer value) {
            addCriterion("mcoin_amount <>", value, "mcoinAmount");
            return (Criteria) this;
        }

        public Criteria andMcoinAmountGreaterThan(Integer value) {
            addCriterion("mcoin_amount >", value, "mcoinAmount");
            return (Criteria) this;
        }

        public Criteria andMcoinAmountGreaterThanOrEqualTo(Integer value) {
            addCriterion("mcoin_amount >=", value, "mcoinAmount");
            return (Criteria) this;
        }

        public Criteria andMcoinAmountLessThan(Integer value) {
            addCriterion("mcoin_amount <", value, "mcoinAmount");
            return (Criteria) this;
        }

        public Criteria andMcoinAmountLessThanOrEqualTo(Integer value) {
            addCriterion("mcoin_amount <=", value, "mcoinAmount");
            return (Criteria) this;
        }

        public Criteria andMcoinAmountIn(List<Integer> values) {
            addCriterion("mcoin_amount in", values, "mcoinAmount");
            return (Criteria) this;
        }

        public Criteria andMcoinAmountNotIn(List<Integer> values) {
            addCriterion("mcoin_amount not in", values, "mcoinAmount");
            return (Criteria) this;
        }

        public Criteria andMcoinAmountBetween(Integer value1, Integer value2) {
            addCriterion("mcoin_amount between", value1, value2, "mcoinAmount");
            return (Criteria) this;
        }

        public Criteria andMcoinAmountNotBetween(Integer value1, Integer value2) {
            addCriterion("mcoin_amount not between", value1, value2, "mcoinAmount");
            return (Criteria) this;
        }

        public Criteria andFreebiesIdIsNull() {
            addCriterion("freebies_id is null");
            return (Criteria) this;
        }

        public Criteria andFreebiesIdIsNotNull() {
            addCriterion("freebies_id is not null");
            return (Criteria) this;
        }

        public Criteria andFreebiesIdEqualTo(Integer value) {
            addCriterion("freebies_id =", value, "freebiesId");
            return (Criteria) this;
        }

        public Criteria andFreebiesIdNotEqualTo(Integer value) {
            addCriterion("freebies_id <>", value, "freebiesId");
            return (Criteria) this;
        }

        public Criteria andFreebiesIdGreaterThan(Integer value) {
            addCriterion("freebies_id >", value, "freebiesId");
            return (Criteria) this;
        }

        public Criteria andFreebiesIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("freebies_id >=", value, "freebiesId");
            return (Criteria) this;
        }

        public Criteria andFreebiesIdLessThan(Integer value) {
            addCriterion("freebies_id <", value, "freebiesId");
            return (Criteria) this;
        }

        public Criteria andFreebiesIdLessThanOrEqualTo(Integer value) {
            addCriterion("freebies_id <=", value, "freebiesId");
            return (Criteria) this;
        }

        public Criteria andFreebiesIdIn(List<Integer> values) {
            addCriterion("freebies_id in", values, "freebiesId");
            return (Criteria) this;
        }

        public Criteria andFreebiesIdNotIn(List<Integer> values) {
            addCriterion("freebies_id not in", values, "freebiesId");
            return (Criteria) this;
        }

        public Criteria andFreebiesIdBetween(Integer value1, Integer value2) {
            addCriterion("freebies_id between", value1, value2, "freebiesId");
            return (Criteria) this;
        }

        public Criteria andFreebiesIdNotBetween(Integer value1, Integer value2) {
            addCriterion("freebies_id not between", value1, value2, "freebiesId");
            return (Criteria) this;
        }

        public Criteria andFreebiesTypeIsNull() {
            addCriterion("freebies_type is null");
            return (Criteria) this;
        }

        public Criteria andFreebiesTypeIsNotNull() {
            addCriterion("freebies_type is not null");
            return (Criteria) this;
        }

        public Criteria andFreebiesTypeEqualTo(Byte value) {
            addCriterion("freebies_type =", value, "freebiesType");
            return (Criteria) this;
        }

        public Criteria andFreebiesTypeNotEqualTo(Boolean value) {
            addCriterion("freebies_type <>", value, "freebiesType");
            return (Criteria) this;
        }

        public Criteria andFreebiesTypeGreaterThan(Boolean value) {
            addCriterion("freebies_type >", value, "freebiesType");
            return (Criteria) this;
        }

        public Criteria andFreebiesTypeGreaterThanOrEqualTo(Boolean value) {
            addCriterion("freebies_type >=", value, "freebiesType");
            return (Criteria) this;
        }

        public Criteria andFreebiesTypeLessThan(Boolean value) {
            addCriterion("freebies_type <", value, "freebiesType");
            return (Criteria) this;
        }

        public Criteria andFreebiesTypeLessThanOrEqualTo(Boolean value) {
            addCriterion("freebies_type <=", value, "freebiesType");
            return (Criteria) this;
        }

        public Criteria andFreebiesTypeIn(List<Boolean> values) {
            addCriterion("freebies_type in", values, "freebiesType");
            return (Criteria) this;
        }

        public Criteria andFreebiesTypeNotIn(List<Boolean> values) {
            addCriterion("freebies_type not in", values, "freebiesType");
            return (Criteria) this;
        }

        public Criteria andFreebiesTypeBetween(Boolean value1, Boolean value2) {
            addCriterion("freebies_type between", value1, value2, "freebiesType");
            return (Criteria) this;
        }

        public Criteria andFreebiesTypeNotBetween(Boolean value1, Boolean value2) {
            addCriterion("freebies_type not between", value1, value2, "freebiesType");
            return (Criteria) this;
        }

        public Criteria andCompletePicUrlIsNull() {
            addCriterion("complete_pic_url is null");
            return (Criteria) this;
        }

        public Criteria andCompletePicUrlIsNotNull() {
            addCriterion("complete_pic_url is not null");
            return (Criteria) this;
        }

        public Criteria andCompletePicUrlEqualTo(String value) {
            addCriterion("complete_pic_url =", value, "completePicUrl");
            return (Criteria) this;
        }

        public Criteria andCompletePicUrlNotEqualTo(String value) {
            addCriterion("complete_pic_url <>", value, "completePicUrl");
            return (Criteria) this;
        }

        public Criteria andCompletePicUrlGreaterThan(String value) {
            addCriterion("complete_pic_url >", value, "completePicUrl");
            return (Criteria) this;
        }

        public Criteria andCompletePicUrlGreaterThanOrEqualTo(String value) {
            addCriterion("complete_pic_url >=", value, "completePicUrl");
            return (Criteria) this;
        }

        public Criteria andCompletePicUrlLessThan(String value) {
            addCriterion("complete_pic_url <", value, "completePicUrl");
            return (Criteria) this;
        }

        public Criteria andCompletePicUrlLessThanOrEqualTo(String value) {
            addCriterion("complete_pic_url <=", value, "completePicUrl");
            return (Criteria) this;
        }

        public Criteria andCompletePicUrlLike(String value) {
            addCriterion("complete_pic_url like", value, "completePicUrl");
            return (Criteria) this;
        }

        public Criteria andCompletePicUrlNotLike(String value) {
            addCriterion("complete_pic_url not like", value, "completePicUrl");
            return (Criteria) this;
        }

        public Criteria andCompletePicUrlIn(List<String> values) {
            addCriterion("complete_pic_url in", values, "completePicUrl");
            return (Criteria) this;
        }

        public Criteria andCompletePicUrlNotIn(List<String> values) {
            addCriterion("complete_pic_url not in", values, "completePicUrl");
            return (Criteria) this;
        }

        public Criteria andCompletePicUrlBetween(String value1, String value2) {
            addCriterion("complete_pic_url between", value1, value2, "completePicUrl");
            return (Criteria) this;
        }

        public Criteria andCompletePicUrlNotBetween(String value1, String value2) {
            addCriterion("complete_pic_url not between", value1, value2, "completePicUrl");
            return (Criteria) this;
        }

        public Criteria andIncompletePicUrlIsNull() {
            addCriterion("incomplete_pic_url is null");
            return (Criteria) this;
        }

        public Criteria andIncompletePicUrlIsNotNull() {
            addCriterion("incomplete_pic_url is not null");
            return (Criteria) this;
        }

        public Criteria andIncompletePicUrlEqualTo(String value) {
            addCriterion("incomplete_pic_url =", value, "incompletePicUrl");
            return (Criteria) this;
        }

        public Criteria andIncompletePicUrlNotEqualTo(String value) {
            addCriterion("incomplete_pic_url <>", value, "incompletePicUrl");
            return (Criteria) this;
        }

        public Criteria andIncompletePicUrlGreaterThan(String value) {
            addCriterion("incomplete_pic_url >", value, "incompletePicUrl");
            return (Criteria) this;
        }

        public Criteria andIncompletePicUrlGreaterThanOrEqualTo(String value) {
            addCriterion("incomplete_pic_url >=", value, "incompletePicUrl");
            return (Criteria) this;
        }

        public Criteria andIncompletePicUrlLessThan(String value) {
            addCriterion("incomplete_pic_url <", value, "incompletePicUrl");
            return (Criteria) this;
        }

        public Criteria andIncompletePicUrlLessThanOrEqualTo(String value) {
            addCriterion("incomplete_pic_url <=", value, "incompletePicUrl");
            return (Criteria) this;
        }

        public Criteria andIncompletePicUrlLike(String value) {
            addCriterion("incomplete_pic_url like", value, "incompletePicUrl");
            return (Criteria) this;
        }

        public Criteria andIncompletePicUrlNotLike(String value) {
            addCriterion("incomplete_pic_url not like", value, "incompletePicUrl");
            return (Criteria) this;
        }

        public Criteria andIncompletePicUrlIn(List<String> values) {
            addCriterion("incomplete_pic_url in", values, "incompletePicUrl");
            return (Criteria) this;
        }

        public Criteria andIncompletePicUrlNotIn(List<String> values) {
            addCriterion("incomplete_pic_url not in", values, "incompletePicUrl");
            return (Criteria) this;
        }

        public Criteria andIncompletePicUrlBetween(String value1, String value2) {
            addCriterion("incomplete_pic_url between", value1, value2, "incompletePicUrl");
            return (Criteria) this;
        }

        public Criteria andIncompletePicUrlNotBetween(String value1, String value2) {
            addCriterion("incomplete_pic_url not between", value1, value2, "incompletePicUrl");
            return (Criteria) this;
        }

        public Criteria andSeqIsNull() {
            addCriterion("seq is null");
            return (Criteria) this;
        }

        public Criteria andSeqIsNotNull() {
            addCriterion("seq is not null");
            return (Criteria) this;
        }

        public Criteria andSeqEqualTo(Byte value) {
            addCriterion("seq =", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqNotEqualTo(Byte value) {
            addCriterion("seq <>", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqGreaterThan(Byte value) {
            addCriterion("seq >", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqGreaterThanOrEqualTo(Byte value) {
            addCriterion("seq >=", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqLessThan(Byte value) {
            addCriterion("seq <", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqLessThanOrEqualTo(Byte value) {
            addCriterion("seq <=", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqIn(List<Byte> values) {
            addCriterion("seq in", values, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqNotIn(List<Byte> values) {
            addCriterion("seq not in", values, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqBetween(Byte value1, Byte value2) {
            addCriterion("seq between", value1, value2, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqNotBetween(Byte value1, Byte value2) {
            addCriterion("seq not between", value1, value2, "seq");
            return (Criteria) this;
        }

        public Criteria andAndroidSchemeIsNull() {
            addCriterion("android_scheme is null");
            return (Criteria) this;
        }

        public Criteria andAndroidSchemeIsNotNull() {
            addCriterion("android_scheme is not null");
            return (Criteria) this;
        }

        public Criteria andAndroidSchemeEqualTo(String value) {
            addCriterion("android_scheme =", value, "androidScheme");
            return (Criteria) this;
        }

        public Criteria andAndroidSchemeNotEqualTo(String value) {
            addCriterion("android_scheme <>", value, "androidScheme");
            return (Criteria) this;
        }

        public Criteria andAndroidSchemeGreaterThan(String value) {
            addCriterion("android_scheme >", value, "androidScheme");
            return (Criteria) this;
        }

        public Criteria andAndroidSchemeGreaterThanOrEqualTo(String value) {
            addCriterion("android_scheme >=", value, "androidScheme");
            return (Criteria) this;
        }

        public Criteria andAndroidSchemeLessThan(String value) {
            addCriterion("android_scheme <", value, "androidScheme");
            return (Criteria) this;
        }

        public Criteria andAndroidSchemeLessThanOrEqualTo(String value) {
            addCriterion("android_scheme <=", value, "androidScheme");
            return (Criteria) this;
        }

        public Criteria andAndroidSchemeLike(String value) {
            addCriterion("android_scheme like", value, "androidScheme");
            return (Criteria) this;
        }

        public Criteria andAndroidSchemeNotLike(String value) {
            addCriterion("android_scheme not like", value, "androidScheme");
            return (Criteria) this;
        }

        public Criteria andAndroidSchemeIn(List<String> values) {
            addCriterion("android_scheme in", values, "androidScheme");
            return (Criteria) this;
        }

        public Criteria andAndroidSchemeNotIn(List<String> values) {
            addCriterion("android_scheme not in", values, "androidScheme");
            return (Criteria) this;
        }

        public Criteria andAndroidSchemeBetween(String value1, String value2) {
            addCriterion("android_scheme between", value1, value2, "androidScheme");
            return (Criteria) this;
        }

        public Criteria andAndroidSchemeNotBetween(String value1, String value2) {
            addCriterion("android_scheme not between", value1, value2, "androidScheme");
            return (Criteria) this;
        }

        public Criteria andIosSchemeIsNull() {
            addCriterion("ios_scheme is null");
            return (Criteria) this;
        }

        public Criteria andIosSchemeIsNotNull() {
            addCriterion("ios_scheme is not null");
            return (Criteria) this;
        }

        public Criteria andIosSchemeEqualTo(String value) {
            addCriterion("ios_scheme =", value, "iosScheme");
            return (Criteria) this;
        }

        public Criteria andIosSchemeNotEqualTo(String value) {
            addCriterion("ios_scheme <>", value, "iosScheme");
            return (Criteria) this;
        }

        public Criteria andIosSchemeGreaterThan(String value) {
            addCriterion("ios_scheme >", value, "iosScheme");
            return (Criteria) this;
        }

        public Criteria andIosSchemeGreaterThanOrEqualTo(String value) {
            addCriterion("ios_scheme >=", value, "iosScheme");
            return (Criteria) this;
        }

        public Criteria andIosSchemeLessThan(String value) {
            addCriterion("ios_scheme <", value, "iosScheme");
            return (Criteria) this;
        }

        public Criteria andIosSchemeLessThanOrEqualTo(String value) {
            addCriterion("ios_scheme <=", value, "iosScheme");
            return (Criteria) this;
        }

        public Criteria andIosSchemeLike(String value) {
            addCriterion("ios_scheme like", value, "iosScheme");
            return (Criteria) this;
        }

        public Criteria andIosSchemeNotLike(String value) {
            addCriterion("ios_scheme not like", value, "iosScheme");
            return (Criteria) this;
        }

        public Criteria andIosSchemeIn(List<String> values) {
            addCriterion("ios_scheme in", values, "iosScheme");
            return (Criteria) this;
        }

        public Criteria andIosSchemeNotIn(List<String> values) {
            addCriterion("ios_scheme not in", values, "iosScheme");
            return (Criteria) this;
        }

        public Criteria andIosSchemeBetween(String value1, String value2) {
            addCriterion("ios_scheme between", value1, value2, "iosScheme");
            return (Criteria) this;
        }

        public Criteria andIosSchemeNotBetween(String value1, String value2) {
            addCriterion("ios_scheme not between", value1, value2, "iosScheme");
            return (Criteria) this;
        }

        public Criteria andMissionTypeIsNull() {
            addCriterion("mission_type is null");
            return (Criteria) this;
        }

        public Criteria andMissionTypeIsNotNull() {
            addCriterion("mission_type is not null");
            return (Criteria) this;
        }

        public Criteria andMissionTypeEqualTo(Byte value) {
            addCriterion("mission_type =", value, "missionType");
            return (Criteria) this;
        }

        public Criteria andMissionTypeNotEqualTo(Boolean value) {
            addCriterion("mission_type <>", value, "missionType");
            return (Criteria) this;
        }

        public Criteria andMissionTypeGreaterThan(Boolean value) {
            addCriterion("mission_type >", value, "missionType");
            return (Criteria) this;
        }

        public Criteria andMissionTypeGreaterThanOrEqualTo(Boolean value) {
            addCriterion("mission_type >=", value, "missionType");
            return (Criteria) this;
        }

        public Criteria andMissionTypeLessThan(Boolean value) {
            addCriterion("mission_type <", value, "missionType");
            return (Criteria) this;
        }

        public Criteria andMissionTypeLessThanOrEqualTo(Boolean value) {
            addCriterion("mission_type <=", value, "missionType");
            return (Criteria) this;
        }

        public Criteria andMissionTypeIn(List<Boolean> values) {
            addCriterion("mission_type in", values, "missionType");
            return (Criteria) this;
        }

        public Criteria andMissionTypeNotIn(List<Boolean> values) {
            addCriterion("mission_type not in", values, "missionType");
            return (Criteria) this;
        }

        public Criteria andMissionTypeBetween(Boolean value1, Boolean value2) {
            addCriterion("mission_type between", value1, value2, "missionType");
            return (Criteria) this;
        }

        public Criteria andMissionTypeNotBetween(Boolean value1, Boolean value2) {
            addCriterion("mission_type not between", value1, value2, "missionType");
            return (Criteria) this;
        }

        public Criteria andMissionScopeIsNull() {
            addCriterion("mission_scope is null");
            return (Criteria) this;
        }

        public Criteria andMissionScopeIsNotNull() {
            addCriterion("mission_scope is not null");
            return (Criteria) this;
        }

        public Criteria andMissionScopeEqualTo(Boolean value) {
            addCriterion("mission_scope =", value, "missionScope");
            return (Criteria) this;
        }

        public Criteria andMissionScopeNotEqualTo(Boolean value) {
            addCriterion("mission_scope <>", value, "missionScope");
            return (Criteria) this;
        }

        public Criteria andMissionScopeGreaterThan(Boolean value) {
            addCriterion("mission_scope >", value, "missionScope");
            return (Criteria) this;
        }

        public Criteria andMissionScopeGreaterThanOrEqualTo(Boolean value) {
            addCriterion("mission_scope >=", value, "missionScope");
            return (Criteria) this;
        }

        public Criteria andMissionScopeLessThan(Boolean value) {
            addCriterion("mission_scope <", value, "missionScope");
            return (Criteria) this;
        }

        public Criteria andMissionScopeLessThanOrEqualTo(Boolean value) {
            addCriterion("mission_scope <=", value, "missionScope");
            return (Criteria) this;
        }

        public Criteria andMissionScopeIn(List<Boolean> values) {
            addCriterion("mission_scope in", values, "missionScope");
            return (Criteria) this;
        }

        public Criteria andMissionScopeNotIn(List<Boolean> values) {
            addCriterion("mission_scope not in", values, "missionScope");
            return (Criteria) this;
        }

        public Criteria andMissionScopeBetween(Boolean value1, Boolean value2) {
            addCriterion("mission_scope between", value1, value2, "missionScope");
            return (Criteria) this;
        }

        public Criteria andMissionScopeNotBetween(Boolean value1, Boolean value2) {
            addCriterion("mission_scope not between", value1, value2, "missionScope");
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
