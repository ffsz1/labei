package com.erban.main.model;

import java.util.ArrayList;
import java.util.List;

public class NobleRightExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public NobleRightExample() {
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

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andOpenGoldIsNull() {
            addCriterion("open_gold is null");
            return (Criteria) this;
        }

        public Criteria andOpenGoldIsNotNull() {
            addCriterion("open_gold is not null");
            return (Criteria) this;
        }

        public Criteria andOpenGoldEqualTo(Long value) {
            addCriterion("open_gold =", value, "openGold");
            return (Criteria) this;
        }

        public Criteria andOpenGoldNotEqualTo(Long value) {
            addCriterion("open_gold <>", value, "openGold");
            return (Criteria) this;
        }

        public Criteria andOpenGoldGreaterThan(Long value) {
            addCriterion("open_gold >", value, "openGold");
            return (Criteria) this;
        }

        public Criteria andOpenGoldGreaterThanOrEqualTo(Long value) {
            addCriterion("open_gold >=", value, "openGold");
            return (Criteria) this;
        }

        public Criteria andOpenGoldLessThan(Long value) {
            addCriterion("open_gold <", value, "openGold");
            return (Criteria) this;
        }

        public Criteria andOpenGoldLessThanOrEqualTo(Long value) {
            addCriterion("open_gold <=", value, "openGold");
            return (Criteria) this;
        }

        public Criteria andOpenGoldIn(List<Long> values) {
            addCriterion("open_gold in", values, "openGold");
            return (Criteria) this;
        }

        public Criteria andOpenGoldNotIn(List<Long> values) {
            addCriterion("open_gold not in", values, "openGold");
            return (Criteria) this;
        }

        public Criteria andOpenGoldBetween(Long value1, Long value2) {
            addCriterion("open_gold between", value1, value2, "openGold");
            return (Criteria) this;
        }

        public Criteria andOpenGoldNotBetween(Long value1, Long value2) {
            addCriterion("open_gold not between", value1, value2, "openGold");
            return (Criteria) this;
        }

        public Criteria andRenewGoldIsNull() {
            addCriterion("renew_gold is null");
            return (Criteria) this;
        }

        public Criteria andRenewGoldIsNotNull() {
            addCriterion("renew_gold is not null");
            return (Criteria) this;
        }

        public Criteria andRenewGoldEqualTo(Long value) {
            addCriterion("renew_gold =", value, "renewGold");
            return (Criteria) this;
        }

        public Criteria andRenewGoldNotEqualTo(Long value) {
            addCriterion("renew_gold <>", value, "renewGold");
            return (Criteria) this;
        }

        public Criteria andRenewGoldGreaterThan(Long value) {
            addCriterion("renew_gold >", value, "renewGold");
            return (Criteria) this;
        }

        public Criteria andRenewGoldGreaterThanOrEqualTo(Long value) {
            addCriterion("renew_gold >=", value, "renewGold");
            return (Criteria) this;
        }

        public Criteria andRenewGoldLessThan(Long value) {
            addCriterion("renew_gold <", value, "renewGold");
            return (Criteria) this;
        }

        public Criteria andRenewGoldLessThanOrEqualTo(Long value) {
            addCriterion("renew_gold <=", value, "renewGold");
            return (Criteria) this;
        }

        public Criteria andRenewGoldIn(List<Long> values) {
            addCriterion("renew_gold in", values, "renewGold");
            return (Criteria) this;
        }

        public Criteria andRenewGoldNotIn(List<Long> values) {
            addCriterion("renew_gold not in", values, "renewGold");
            return (Criteria) this;
        }

        public Criteria andRenewGoldBetween(Long value1, Long value2) {
            addCriterion("renew_gold between", value1, value2, "renewGold");
            return (Criteria) this;
        }

        public Criteria andRenewGoldNotBetween(Long value1, Long value2) {
            addCriterion("renew_gold not between", value1, value2, "renewGold");
            return (Criteria) this;
        }

        public Criteria andOpenReturnIsNull() {
            addCriterion("open_return is null");
            return (Criteria) this;
        }

        public Criteria andOpenReturnIsNotNull() {
            addCriterion("open_return is not null");
            return (Criteria) this;
        }

        public Criteria andOpenReturnEqualTo(Long value) {
            addCriterion("open_return =", value, "openReturn");
            return (Criteria) this;
        }

        public Criteria andOpenReturnNotEqualTo(Long value) {
            addCriterion("open_return <>", value, "openReturn");
            return (Criteria) this;
        }

        public Criteria andOpenReturnGreaterThan(Long value) {
            addCriterion("open_return >", value, "openReturn");
            return (Criteria) this;
        }

        public Criteria andOpenReturnGreaterThanOrEqualTo(Long value) {
            addCriterion("open_return >=", value, "openReturn");
            return (Criteria) this;
        }

        public Criteria andOpenReturnLessThan(Long value) {
            addCriterion("open_return <", value, "openReturn");
            return (Criteria) this;
        }

        public Criteria andOpenReturnLessThanOrEqualTo(Long value) {
            addCriterion("open_return <=", value, "openReturn");
            return (Criteria) this;
        }

        public Criteria andOpenReturnIn(List<Long> values) {
            addCriterion("open_return in", values, "openReturn");
            return (Criteria) this;
        }

        public Criteria andOpenReturnNotIn(List<Long> values) {
            addCriterion("open_return not in", values, "openReturn");
            return (Criteria) this;
        }

        public Criteria andOpenReturnBetween(Long value1, Long value2) {
            addCriterion("open_return between", value1, value2, "openReturn");
            return (Criteria) this;
        }

        public Criteria andOpenReturnNotBetween(Long value1, Long value2) {
            addCriterion("open_return not between", value1, value2, "openReturn");
            return (Criteria) this;
        }

        public Criteria andRenewReturnIsNull() {
            addCriterion("renew_return is null");
            return (Criteria) this;
        }

        public Criteria andRenewReturnIsNotNull() {
            addCriterion("renew_return is not null");
            return (Criteria) this;
        }

        public Criteria andRenewReturnEqualTo(Long value) {
            addCriterion("renew_return =", value, "renewReturn");
            return (Criteria) this;
        }

        public Criteria andRenewReturnNotEqualTo(Long value) {
            addCriterion("renew_return <>", value, "renewReturn");
            return (Criteria) this;
        }

        public Criteria andRenewReturnGreaterThan(Long value) {
            addCriterion("renew_return >", value, "renewReturn");
            return (Criteria) this;
        }

        public Criteria andRenewReturnGreaterThanOrEqualTo(Long value) {
            addCriterion("renew_return >=", value, "renewReturn");
            return (Criteria) this;
        }

        public Criteria andRenewReturnLessThan(Long value) {
            addCriterion("renew_return <", value, "renewReturn");
            return (Criteria) this;
        }

        public Criteria andRenewReturnLessThanOrEqualTo(Long value) {
            addCriterion("renew_return <=", value, "renewReturn");
            return (Criteria) this;
        }

        public Criteria andRenewReturnIn(List<Long> values) {
            addCriterion("renew_return in", values, "renewReturn");
            return (Criteria) this;
        }

        public Criteria andRenewReturnNotIn(List<Long> values) {
            addCriterion("renew_return not in", values, "renewReturn");
            return (Criteria) this;
        }

        public Criteria andRenewReturnBetween(Long value1, Long value2) {
            addCriterion("renew_return between", value1, value2, "renewReturn");
            return (Criteria) this;
        }

        public Criteria andRenewReturnNotBetween(Long value1, Long value2) {
            addCriterion("renew_return not between", value1, value2, "renewReturn");
            return (Criteria) this;
        }

        public Criteria andScreenMedalIsNull() {
            addCriterion("screen_medal is null");
            return (Criteria) this;
        }

        public Criteria andScreenMedalIsNotNull() {
            addCriterion("screen_medal is not null");
            return (Criteria) this;
        }

        public Criteria andScreenMedalEqualTo(Byte value) {
            addCriterion("screen_medal =", value, "screenMedal");
            return (Criteria) this;
        }

        public Criteria andScreenMedalNotEqualTo(Byte value) {
            addCriterion("screen_medal <>", value, "screenMedal");
            return (Criteria) this;
        }

        public Criteria andScreenMedalGreaterThan(Byte value) {
            addCriterion("screen_medal >", value, "screenMedal");
            return (Criteria) this;
        }

        public Criteria andScreenMedalGreaterThanOrEqualTo(Byte value) {
            addCriterion("screen_medal >=", value, "screenMedal");
            return (Criteria) this;
        }

        public Criteria andScreenMedalLessThan(Byte value) {
            addCriterion("screen_medal <", value, "screenMedal");
            return (Criteria) this;
        }

        public Criteria andScreenMedalLessThanOrEqualTo(Byte value) {
            addCriterion("screen_medal <=", value, "screenMedal");
            return (Criteria) this;
        }

        public Criteria andScreenMedalIn(List<Byte> values) {
            addCriterion("screen_medal in", values, "screenMedal");
            return (Criteria) this;
        }

        public Criteria andScreenMedalNotIn(List<Byte> values) {
            addCriterion("screen_medal not in", values, "screenMedal");
            return (Criteria) this;
        }

        public Criteria andScreenMedalBetween(Byte value1, Byte value2) {
            addCriterion("screen_medal between", value1, value2, "screenMedal");
            return (Criteria) this;
        }

        public Criteria andScreenMedalNotBetween(Byte value1, Byte value2) {
            addCriterion("screen_medal not between", value1, value2, "screenMedal");
            return (Criteria) this;
        }

        public Criteria andRoomMedalIsNull() {
            addCriterion("room_medal is null");
            return (Criteria) this;
        }

        public Criteria andRoomMedalIsNotNull() {
            addCriterion("room_medal is not null");
            return (Criteria) this;
        }

        public Criteria andRoomMedalEqualTo(Byte value) {
            addCriterion("room_medal =", value, "roomMedal");
            return (Criteria) this;
        }

        public Criteria andRoomMedalNotEqualTo(Byte value) {
            addCriterion("room_medal <>", value, "roomMedal");
            return (Criteria) this;
        }

        public Criteria andRoomMedalGreaterThan(Byte value) {
            addCriterion("room_medal >", value, "roomMedal");
            return (Criteria) this;
        }

        public Criteria andRoomMedalGreaterThanOrEqualTo(Byte value) {
            addCriterion("room_medal >=", value, "roomMedal");
            return (Criteria) this;
        }

        public Criteria andRoomMedalLessThan(Byte value) {
            addCriterion("room_medal <", value, "roomMedal");
            return (Criteria) this;
        }

        public Criteria andRoomMedalLessThanOrEqualTo(Byte value) {
            addCriterion("room_medal <=", value, "roomMedal");
            return (Criteria) this;
        }

        public Criteria andRoomMedalIn(List<Byte> values) {
            addCriterion("room_medal in", values, "roomMedal");
            return (Criteria) this;
        }

        public Criteria andRoomMedalNotIn(List<Byte> values) {
            addCriterion("room_medal not in", values, "roomMedal");
            return (Criteria) this;
        }

        public Criteria andRoomMedalBetween(Byte value1, Byte value2) {
            addCriterion("room_medal between", value1, value2, "roomMedal");
            return (Criteria) this;
        }

        public Criteria andRoomMedalNotBetween(Byte value1, Byte value2) {
            addCriterion("room_medal not between", value1, value2, "roomMedal");
            return (Criteria) this;
        }

        public Criteria andUserMedalIsNull() {
            addCriterion("user_medal is null");
            return (Criteria) this;
        }

        public Criteria andUserMedalIsNotNull() {
            addCriterion("user_medal is not null");
            return (Criteria) this;
        }

        public Criteria andUserMedalEqualTo(Byte value) {
            addCriterion("user_medal =", value, "userMedal");
            return (Criteria) this;
        }

        public Criteria andUserMedalNotEqualTo(Byte value) {
            addCriterion("user_medal <>", value, "userMedal");
            return (Criteria) this;
        }

        public Criteria andUserMedalGreaterThan(Byte value) {
            addCriterion("user_medal >", value, "userMedal");
            return (Criteria) this;
        }

        public Criteria andUserMedalGreaterThanOrEqualTo(Byte value) {
            addCriterion("user_medal >=", value, "userMedal");
            return (Criteria) this;
        }

        public Criteria andUserMedalLessThan(Byte value) {
            addCriterion("user_medal <", value, "userMedal");
            return (Criteria) this;
        }

        public Criteria andUserMedalLessThanOrEqualTo(Byte value) {
            addCriterion("user_medal <=", value, "userMedal");
            return (Criteria) this;
        }

        public Criteria andUserMedalIn(List<Byte> values) {
            addCriterion("user_medal in", values, "userMedal");
            return (Criteria) this;
        }

        public Criteria andUserMedalNotIn(List<Byte> values) {
            addCriterion("user_medal not in", values, "userMedal");
            return (Criteria) this;
        }

        public Criteria andUserMedalBetween(Byte value1, Byte value2) {
            addCriterion("user_medal between", value1, value2, "userMedal");
            return (Criteria) this;
        }

        public Criteria andUserMedalNotBetween(Byte value1, Byte value2) {
            addCriterion("user_medal not between", value1, value2, "userMedal");
            return (Criteria) this;
        }

        public Criteria andUserPageIsNull() {
            addCriterion("user_page is null");
            return (Criteria) this;
        }

        public Criteria andUserPageIsNotNull() {
            addCriterion("user_page is not null");
            return (Criteria) this;
        }

        public Criteria andUserPageEqualTo(Byte value) {
            addCriterion("user_page =", value, "userPage");
            return (Criteria) this;
        }

        public Criteria andUserPageNotEqualTo(Byte value) {
            addCriterion("user_page <>", value, "userPage");
            return (Criteria) this;
        }

        public Criteria andUserPageGreaterThan(Byte value) {
            addCriterion("user_page >", value, "userPage");
            return (Criteria) this;
        }

        public Criteria andUserPageGreaterThanOrEqualTo(Byte value) {
            addCriterion("user_page >=", value, "userPage");
            return (Criteria) this;
        }

        public Criteria andUserPageLessThan(Byte value) {
            addCriterion("user_page <", value, "userPage");
            return (Criteria) this;
        }

        public Criteria andUserPageLessThanOrEqualTo(Byte value) {
            addCriterion("user_page <=", value, "userPage");
            return (Criteria) this;
        }

        public Criteria andUserPageIn(List<Byte> values) {
            addCriterion("user_page in", values, "userPage");
            return (Criteria) this;
        }

        public Criteria andUserPageNotIn(List<Byte> values) {
            addCriterion("user_page not in", values, "userPage");
            return (Criteria) this;
        }

        public Criteria andUserPageBetween(Byte value1, Byte value2) {
            addCriterion("user_page between", value1, value2, "userPage");
            return (Criteria) this;
        }

        public Criteria andUserPageNotBetween(Byte value1, Byte value2) {
            addCriterion("user_page not between", value1, value2, "userPage");
            return (Criteria) this;
        }

        public Criteria andOpenEffectIsNull() {
            addCriterion("open_effect is null");
            return (Criteria) this;
        }

        public Criteria andOpenEffectIsNotNull() {
            addCriterion("open_effect is not null");
            return (Criteria) this;
        }

        public Criteria andOpenEffectEqualTo(Byte value) {
            addCriterion("open_effect =", value, "openEffect");
            return (Criteria) this;
        }

        public Criteria andOpenEffectNotEqualTo(Byte value) {
            addCriterion("open_effect <>", value, "openEffect");
            return (Criteria) this;
        }

        public Criteria andOpenEffectGreaterThan(Byte value) {
            addCriterion("open_effect >", value, "openEffect");
            return (Criteria) this;
        }

        public Criteria andOpenEffectGreaterThanOrEqualTo(Byte value) {
            addCriterion("open_effect >=", value, "openEffect");
            return (Criteria) this;
        }

        public Criteria andOpenEffectLessThan(Byte value) {
            addCriterion("open_effect <", value, "openEffect");
            return (Criteria) this;
        }

        public Criteria andOpenEffectLessThanOrEqualTo(Byte value) {
            addCriterion("open_effect <=", value, "openEffect");
            return (Criteria) this;
        }

        public Criteria andOpenEffectIn(List<Byte> values) {
            addCriterion("open_effect in", values, "openEffect");
            return (Criteria) this;
        }

        public Criteria andOpenEffectNotIn(List<Byte> values) {
            addCriterion("open_effect not in", values, "openEffect");
            return (Criteria) this;
        }

        public Criteria andOpenEffectBetween(Byte value1, Byte value2) {
            addCriterion("open_effect between", value1, value2, "openEffect");
            return (Criteria) this;
        }

        public Criteria andOpenEffectNotBetween(Byte value1, Byte value2) {
            addCriterion("open_effect not between", value1, value2, "openEffect");
            return (Criteria) this;
        }

        public Criteria andOpenNoticeIsNull() {
            addCriterion("open_notice is null");
            return (Criteria) this;
        }

        public Criteria andOpenNoticeIsNotNull() {
            addCriterion("open_notice is not null");
            return (Criteria) this;
        }

        public Criteria andOpenNoticeEqualTo(Byte value) {
            addCriterion("open_notice =", value, "openNotice");
            return (Criteria) this;
        }

        public Criteria andOpenNoticeNotEqualTo(Byte value) {
            addCriterion("open_notice <>", value, "openNotice");
            return (Criteria) this;
        }

        public Criteria andOpenNoticeGreaterThan(Byte value) {
            addCriterion("open_notice >", value, "openNotice");
            return (Criteria) this;
        }

        public Criteria andOpenNoticeGreaterThanOrEqualTo(Byte value) {
            addCriterion("open_notice >=", value, "openNotice");
            return (Criteria) this;
        }

        public Criteria andOpenNoticeLessThan(Byte value) {
            addCriterion("open_notice <", value, "openNotice");
            return (Criteria) this;
        }

        public Criteria andOpenNoticeLessThanOrEqualTo(Byte value) {
            addCriterion("open_notice <=", value, "openNotice");
            return (Criteria) this;
        }

        public Criteria andOpenNoticeIn(List<Byte> values) {
            addCriterion("open_notice in", values, "openNotice");
            return (Criteria) this;
        }

        public Criteria andOpenNoticeNotIn(List<Byte> values) {
            addCriterion("open_notice not in", values, "openNotice");
            return (Criteria) this;
        }

        public Criteria andOpenNoticeBetween(Byte value1, Byte value2) {
            addCriterion("open_notice between", value1, value2, "openNotice");
            return (Criteria) this;
        }

        public Criteria andOpenNoticeNotBetween(Byte value1, Byte value2) {
            addCriterion("open_notice not between", value1, value2, "openNotice");
            return (Criteria) this;
        }

        public Criteria andNobleGiftIsNull() {
            addCriterion("noble_gift is null");
            return (Criteria) this;
        }

        public Criteria andNobleGiftIsNotNull() {
            addCriterion("noble_gift is not null");
            return (Criteria) this;
        }

        public Criteria andNobleGiftEqualTo(Byte value) {
            addCriterion("noble_gift =", value, "nobleGift");
            return (Criteria) this;
        }

        public Criteria andNobleGiftNotEqualTo(Byte value) {
            addCriterion("noble_gift <>", value, "nobleGift");
            return (Criteria) this;
        }

        public Criteria andNobleGiftGreaterThan(Byte value) {
            addCriterion("noble_gift >", value, "nobleGift");
            return (Criteria) this;
        }

        public Criteria andNobleGiftGreaterThanOrEqualTo(Byte value) {
            addCriterion("noble_gift >=", value, "nobleGift");
            return (Criteria) this;
        }

        public Criteria andNobleGiftLessThan(Byte value) {
            addCriterion("noble_gift <", value, "nobleGift");
            return (Criteria) this;
        }

        public Criteria andNobleGiftLessThanOrEqualTo(Byte value) {
            addCriterion("noble_gift <=", value, "nobleGift");
            return (Criteria) this;
        }

        public Criteria andNobleGiftIn(List<Byte> values) {
            addCriterion("noble_gift in", values, "nobleGift");
            return (Criteria) this;
        }

        public Criteria andNobleGiftNotIn(List<Byte> values) {
            addCriterion("noble_gift not in", values, "nobleGift");
            return (Criteria) this;
        }

        public Criteria andNobleGiftBetween(Byte value1, Byte value2) {
            addCriterion("noble_gift between", value1, value2, "nobleGift");
            return (Criteria) this;
        }

        public Criteria andNobleGiftNotBetween(Byte value1, Byte value2) {
            addCriterion("noble_gift not between", value1, value2, "nobleGift");
            return (Criteria) this;
        }

        public Criteria andSpecialFaceIsNull() {
            addCriterion("special_face is null");
            return (Criteria) this;
        }

        public Criteria andSpecialFaceIsNotNull() {
            addCriterion("special_face is not null");
            return (Criteria) this;
        }

        public Criteria andSpecialFaceEqualTo(Byte value) {
            addCriterion("special_face =", value, "specialFace");
            return (Criteria) this;
        }

        public Criteria andSpecialFaceNotEqualTo(Byte value) {
            addCriterion("special_face <>", value, "specialFace");
            return (Criteria) this;
        }

        public Criteria andSpecialFaceGreaterThan(Byte value) {
            addCriterion("special_face >", value, "specialFace");
            return (Criteria) this;
        }

        public Criteria andSpecialFaceGreaterThanOrEqualTo(Byte value) {
            addCriterion("special_face >=", value, "specialFace");
            return (Criteria) this;
        }

        public Criteria andSpecialFaceLessThan(Byte value) {
            addCriterion("special_face <", value, "specialFace");
            return (Criteria) this;
        }

        public Criteria andSpecialFaceLessThanOrEqualTo(Byte value) {
            addCriterion("special_face <=", value, "specialFace");
            return (Criteria) this;
        }

        public Criteria andSpecialFaceIn(List<Byte> values) {
            addCriterion("special_face in", values, "specialFace");
            return (Criteria) this;
        }

        public Criteria andSpecialFaceNotIn(List<Byte> values) {
            addCriterion("special_face not in", values, "specialFace");
            return (Criteria) this;
        }

        public Criteria andSpecialFaceBetween(Byte value1, Byte value2) {
            addCriterion("special_face between", value1, value2, "specialFace");
            return (Criteria) this;
        }

        public Criteria andSpecialFaceNotBetween(Byte value1, Byte value2) {
            addCriterion("special_face not between", value1, value2, "specialFace");
            return (Criteria) this;
        }

        public Criteria andEnterNoticeIsNull() {
            addCriterion("enter_notice is null");
            return (Criteria) this;
        }

        public Criteria andEnterNoticeIsNotNull() {
            addCriterion("enter_notice is not null");
            return (Criteria) this;
        }

        public Criteria andEnterNoticeEqualTo(Byte value) {
            addCriterion("enter_notice =", value, "enterNotice");
            return (Criteria) this;
        }

        public Criteria andEnterNoticeNotEqualTo(Byte value) {
            addCriterion("enter_notice <>", value, "enterNotice");
            return (Criteria) this;
        }

        public Criteria andEnterNoticeGreaterThan(Byte value) {
            addCriterion("enter_notice >", value, "enterNotice");
            return (Criteria) this;
        }

        public Criteria andEnterNoticeGreaterThanOrEqualTo(Byte value) {
            addCriterion("enter_notice >=", value, "enterNotice");
            return (Criteria) this;
        }

        public Criteria andEnterNoticeLessThan(Byte value) {
            addCriterion("enter_notice <", value, "enterNotice");
            return (Criteria) this;
        }

        public Criteria andEnterNoticeLessThanOrEqualTo(Byte value) {
            addCriterion("enter_notice <=", value, "enterNotice");
            return (Criteria) this;
        }

        public Criteria andEnterNoticeIn(List<Byte> values) {
            addCriterion("enter_notice in", values, "enterNotice");
            return (Criteria) this;
        }

        public Criteria andEnterNoticeNotIn(List<Byte> values) {
            addCriterion("enter_notice not in", values, "enterNotice");
            return (Criteria) this;
        }

        public Criteria andEnterNoticeBetween(Byte value1, Byte value2) {
            addCriterion("enter_notice between", value1, value2, "enterNotice");
            return (Criteria) this;
        }

        public Criteria andEnterNoticeNotBetween(Byte value1, Byte value2) {
            addCriterion("enter_notice not between", value1, value2, "enterNotice");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundIsNull() {
            addCriterion("room_background is null");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundIsNotNull() {
            addCriterion("room_background is not null");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundEqualTo(Byte value) {
            addCriterion("room_background =", value, "roomBackground");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundNotEqualTo(Byte value) {
            addCriterion("room_background <>", value, "roomBackground");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundGreaterThan(Byte value) {
            addCriterion("room_background >", value, "roomBackground");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundGreaterThanOrEqualTo(Byte value) {
            addCriterion("room_background >=", value, "roomBackground");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundLessThan(Byte value) {
            addCriterion("room_background <", value, "roomBackground");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundLessThanOrEqualTo(Byte value) {
            addCriterion("room_background <=", value, "roomBackground");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundIn(List<Byte> values) {
            addCriterion("room_background in", values, "roomBackground");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundNotIn(List<Byte> values) {
            addCriterion("room_background not in", values, "roomBackground");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundBetween(Byte value1, Byte value2) {
            addCriterion("room_background between", value1, value2, "roomBackground");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundNotBetween(Byte value1, Byte value2) {
            addCriterion("room_background not between", value1, value2, "roomBackground");
            return (Criteria) this;
        }

        public Criteria andMicDecorateIsNull() {
            addCriterion("mic_decorate is null");
            return (Criteria) this;
        }

        public Criteria andMicDecorateIsNotNull() {
            addCriterion("mic_decorate is not null");
            return (Criteria) this;
        }

        public Criteria andMicDecorateEqualTo(Byte value) {
            addCriterion("mic_decorate =", value, "micDecorate");
            return (Criteria) this;
        }

        public Criteria andMicDecorateNotEqualTo(Byte value) {
            addCriterion("mic_decorate <>", value, "micDecorate");
            return (Criteria) this;
        }

        public Criteria andMicDecorateGreaterThan(Byte value) {
            addCriterion("mic_decorate >", value, "micDecorate");
            return (Criteria) this;
        }

        public Criteria andMicDecorateGreaterThanOrEqualTo(Byte value) {
            addCriterion("mic_decorate >=", value, "micDecorate");
            return (Criteria) this;
        }

        public Criteria andMicDecorateLessThan(Byte value) {
            addCriterion("mic_decorate <", value, "micDecorate");
            return (Criteria) this;
        }

        public Criteria andMicDecorateLessThanOrEqualTo(Byte value) {
            addCriterion("mic_decorate <=", value, "micDecorate");
            return (Criteria) this;
        }

        public Criteria andMicDecorateIn(List<Byte> values) {
            addCriterion("mic_decorate in", values, "micDecorate");
            return (Criteria) this;
        }

        public Criteria andMicDecorateNotIn(List<Byte> values) {
            addCriterion("mic_decorate not in", values, "micDecorate");
            return (Criteria) this;
        }

        public Criteria andMicDecorateBetween(Byte value1, Byte value2) {
            addCriterion("mic_decorate between", value1, value2, "micDecorate");
            return (Criteria) this;
        }

        public Criteria andMicDecorateNotBetween(Byte value1, Byte value2) {
            addCriterion("mic_decorate not between", value1, value2, "micDecorate");
            return (Criteria) this;
        }

        public Criteria andMicHaloIsNull() {
            addCriterion("mic_halo is null");
            return (Criteria) this;
        }

        public Criteria andMicHaloIsNotNull() {
            addCriterion("mic_halo is not null");
            return (Criteria) this;
        }

        public Criteria andMicHaloEqualTo(Byte value) {
            addCriterion("mic_halo =", value, "micHalo");
            return (Criteria) this;
        }

        public Criteria andMicHaloNotEqualTo(Byte value) {
            addCriterion("mic_halo <>", value, "micHalo");
            return (Criteria) this;
        }

        public Criteria andMicHaloGreaterThan(Byte value) {
            addCriterion("mic_halo >", value, "micHalo");
            return (Criteria) this;
        }

        public Criteria andMicHaloGreaterThanOrEqualTo(Byte value) {
            addCriterion("mic_halo >=", value, "micHalo");
            return (Criteria) this;
        }

        public Criteria andMicHaloLessThan(Byte value) {
            addCriterion("mic_halo <", value, "micHalo");
            return (Criteria) this;
        }

        public Criteria andMicHaloLessThanOrEqualTo(Byte value) {
            addCriterion("mic_halo <=", value, "micHalo");
            return (Criteria) this;
        }

        public Criteria andMicHaloIn(List<Byte> values) {
            addCriterion("mic_halo in", values, "micHalo");
            return (Criteria) this;
        }

        public Criteria andMicHaloNotIn(List<Byte> values) {
            addCriterion("mic_halo not in", values, "micHalo");
            return (Criteria) this;
        }

        public Criteria andMicHaloBetween(Byte value1, Byte value2) {
            addCriterion("mic_halo between", value1, value2, "micHalo");
            return (Criteria) this;
        }

        public Criteria andMicHaloNotBetween(Byte value1, Byte value2) {
            addCriterion("mic_halo not between", value1, value2, "micHalo");
            return (Criteria) this;
        }

        public Criteria andChatBubbleIsNull() {
            addCriterion("chat_bubble is null");
            return (Criteria) this;
        }

        public Criteria andChatBubbleIsNotNull() {
            addCriterion("chat_bubble is not null");
            return (Criteria) this;
        }

        public Criteria andChatBubbleEqualTo(Byte value) {
            addCriterion("chat_bubble =", value, "chatBubble");
            return (Criteria) this;
        }

        public Criteria andChatBubbleNotEqualTo(Byte value) {
            addCriterion("chat_bubble <>", value, "chatBubble");
            return (Criteria) this;
        }

        public Criteria andChatBubbleGreaterThan(Byte value) {
            addCriterion("chat_bubble >", value, "chatBubble");
            return (Criteria) this;
        }

        public Criteria andChatBubbleGreaterThanOrEqualTo(Byte value) {
            addCriterion("chat_bubble >=", value, "chatBubble");
            return (Criteria) this;
        }

        public Criteria andChatBubbleLessThan(Byte value) {
            addCriterion("chat_bubble <", value, "chatBubble");
            return (Criteria) this;
        }

        public Criteria andChatBubbleLessThanOrEqualTo(Byte value) {
            addCriterion("chat_bubble <=", value, "chatBubble");
            return (Criteria) this;
        }

        public Criteria andChatBubbleIn(List<Byte> values) {
            addCriterion("chat_bubble in", values, "chatBubble");
            return (Criteria) this;
        }

        public Criteria andChatBubbleNotIn(List<Byte> values) {
            addCriterion("chat_bubble not in", values, "chatBubble");
            return (Criteria) this;
        }

        public Criteria andChatBubbleBetween(Byte value1, Byte value2) {
            addCriterion("chat_bubble between", value1, value2, "chatBubble");
            return (Criteria) this;
        }

        public Criteria andChatBubbleNotBetween(Byte value1, Byte value2) {
            addCriterion("chat_bubble not between", value1, value2, "chatBubble");
            return (Criteria) this;
        }

        public Criteria andEnterHideIsNull() {
            addCriterion("enter_hide is null");
            return (Criteria) this;
        }

        public Criteria andEnterHideIsNotNull() {
            addCriterion("enter_hide is not null");
            return (Criteria) this;
        }

        public Criteria andEnterHideEqualTo(Byte value) {
            addCriterion("enter_hide =", value, "enterHide");
            return (Criteria) this;
        }

        public Criteria andEnterHideNotEqualTo(Byte value) {
            addCriterion("enter_hide <>", value, "enterHide");
            return (Criteria) this;
        }

        public Criteria andEnterHideGreaterThan(Byte value) {
            addCriterion("enter_hide >", value, "enterHide");
            return (Criteria) this;
        }

        public Criteria andEnterHideGreaterThanOrEqualTo(Byte value) {
            addCriterion("enter_hide >=", value, "enterHide");
            return (Criteria) this;
        }

        public Criteria andEnterHideLessThan(Byte value) {
            addCriterion("enter_hide <", value, "enterHide");
            return (Criteria) this;
        }

        public Criteria andEnterHideLessThanOrEqualTo(Byte value) {
            addCriterion("enter_hide <=", value, "enterHide");
            return (Criteria) this;
        }

        public Criteria andEnterHideIn(List<Byte> values) {
            addCriterion("enter_hide in", values, "enterHide");
            return (Criteria) this;
        }

        public Criteria andEnterHideNotIn(List<Byte> values) {
            addCriterion("enter_hide not in", values, "enterHide");
            return (Criteria) this;
        }

        public Criteria andEnterHideBetween(Byte value1, Byte value2) {
            addCriterion("enter_hide between", value1, value2, "enterHide");
            return (Criteria) this;
        }

        public Criteria andEnterHideNotBetween(Byte value1, Byte value2) {
            addCriterion("enter_hide not between", value1, value2, "enterHide");
            return (Criteria) this;
        }

        public Criteria andRankHideIsNull() {
            addCriterion("rank_hide is null");
            return (Criteria) this;
        }

        public Criteria andRankHideIsNotNull() {
            addCriterion("rank_hide is not null");
            return (Criteria) this;
        }

        public Criteria andRankHideEqualTo(Byte value) {
            addCriterion("rank_hide =", value, "rankHide");
            return (Criteria) this;
        }

        public Criteria andRankHideNotEqualTo(Byte value) {
            addCriterion("rank_hide <>", value, "rankHide");
            return (Criteria) this;
        }

        public Criteria andRankHideGreaterThan(Byte value) {
            addCriterion("rank_hide >", value, "rankHide");
            return (Criteria) this;
        }

        public Criteria andRankHideGreaterThanOrEqualTo(Byte value) {
            addCriterion("rank_hide >=", value, "rankHide");
            return (Criteria) this;
        }

        public Criteria andRankHideLessThan(Byte value) {
            addCriterion("rank_hide <", value, "rankHide");
            return (Criteria) this;
        }

        public Criteria andRankHideLessThanOrEqualTo(Byte value) {
            addCriterion("rank_hide <=", value, "rankHide");
            return (Criteria) this;
        }

        public Criteria andRankHideIn(List<Byte> values) {
            addCriterion("rank_hide in", values, "rankHide");
            return (Criteria) this;
        }

        public Criteria andRankHideNotIn(List<Byte> values) {
            addCriterion("rank_hide not in", values, "rankHide");
            return (Criteria) this;
        }

        public Criteria andRankHideBetween(Byte value1, Byte value2) {
            addCriterion("rank_hide between", value1, value2, "rankHide");
            return (Criteria) this;
        }

        public Criteria andRankHideNotBetween(Byte value1, Byte value2) {
            addCriterion("rank_hide not between", value1, value2, "rankHide");
            return (Criteria) this;
        }

        public Criteria andSpecialServiceIsNull() {
            addCriterion("special_service is null");
            return (Criteria) this;
        }

        public Criteria andSpecialServiceIsNotNull() {
            addCriterion("special_service is not null");
            return (Criteria) this;
        }

        public Criteria andSpecialServiceEqualTo(Byte value) {
            addCriterion("special_service =", value, "specialService");
            return (Criteria) this;
        }

        public Criteria andSpecialServiceNotEqualTo(Byte value) {
            addCriterion("special_service <>", value, "specialService");
            return (Criteria) this;
        }

        public Criteria andSpecialServiceGreaterThan(Byte value) {
            addCriterion("special_service >", value, "specialService");
            return (Criteria) this;
        }

        public Criteria andSpecialServiceGreaterThanOrEqualTo(Byte value) {
            addCriterion("special_service >=", value, "specialService");
            return (Criteria) this;
        }

        public Criteria andSpecialServiceLessThan(Byte value) {
            addCriterion("special_service <", value, "specialService");
            return (Criteria) this;
        }

        public Criteria andSpecialServiceLessThanOrEqualTo(Byte value) {
            addCriterion("special_service <=", value, "specialService");
            return (Criteria) this;
        }

        public Criteria andSpecialServiceIn(List<Byte> values) {
            addCriterion("special_service in", values, "specialService");
            return (Criteria) this;
        }

        public Criteria andSpecialServiceNotIn(List<Byte> values) {
            addCriterion("special_service not in", values, "specialService");
            return (Criteria) this;
        }

        public Criteria andSpecialServiceBetween(Byte value1, Byte value2) {
            addCriterion("special_service between", value1, value2, "specialService");
            return (Criteria) this;
        }

        public Criteria andSpecialServiceNotBetween(Byte value1, Byte value2) {
            addCriterion("special_service not between", value1, value2, "specialService");
            return (Criteria) this;
        }

        public Criteria andGoodNumIsNull() {
            addCriterion("good_num is null");
            return (Criteria) this;
        }

        public Criteria andGoodNumIsNotNull() {
            addCriterion("good_num is not null");
            return (Criteria) this;
        }

        public Criteria andGoodNumEqualTo(Byte value) {
            addCriterion("good_num =", value, "goodNum");
            return (Criteria) this;
        }

        public Criteria andGoodNumNotEqualTo(Byte value) {
            addCriterion("good_num <>", value, "goodNum");
            return (Criteria) this;
        }

        public Criteria andGoodNumGreaterThan(Byte value) {
            addCriterion("good_num >", value, "goodNum");
            return (Criteria) this;
        }

        public Criteria andGoodNumGreaterThanOrEqualTo(Byte value) {
            addCriterion("good_num >=", value, "goodNum");
            return (Criteria) this;
        }

        public Criteria andGoodNumLessThan(Byte value) {
            addCriterion("good_num <", value, "goodNum");
            return (Criteria) this;
        }

        public Criteria andGoodNumLessThanOrEqualTo(Byte value) {
            addCriterion("good_num <=", value, "goodNum");
            return (Criteria) this;
        }

        public Criteria andGoodNumIn(List<Byte> values) {
            addCriterion("good_num in", values, "goodNum");
            return (Criteria) this;
        }

        public Criteria andGoodNumNotIn(List<Byte> values) {
            addCriterion("good_num not in", values, "goodNum");
            return (Criteria) this;
        }

        public Criteria andGoodNumBetween(Byte value1, Byte value2) {
            addCriterion("good_num between", value1, value2, "goodNum");
            return (Criteria) this;
        }

        public Criteria andGoodNumNotBetween(Byte value1, Byte value2) {
            addCriterion("good_num not between", value1, value2, "goodNum");
            return (Criteria) this;
        }

        public Criteria andPreventIsNull() {
            addCriterion("prevent is null");
            return (Criteria) this;
        }

        public Criteria andPreventIsNotNull() {
            addCriterion("prevent is not null");
            return (Criteria) this;
        }

        public Criteria andPreventEqualTo(Byte value) {
            addCriterion("prevent =", value, "prevent");
            return (Criteria) this;
        }

        public Criteria andPreventNotEqualTo(Byte value) {
            addCriterion("prevent <>", value, "prevent");
            return (Criteria) this;
        }

        public Criteria andPreventGreaterThan(Byte value) {
            addCriterion("prevent >", value, "prevent");
            return (Criteria) this;
        }

        public Criteria andPreventGreaterThanOrEqualTo(Byte value) {
            addCriterion("prevent >=", value, "prevent");
            return (Criteria) this;
        }

        public Criteria andPreventLessThan(Byte value) {
            addCriterion("prevent <", value, "prevent");
            return (Criteria) this;
        }

        public Criteria andPreventLessThanOrEqualTo(Byte value) {
            addCriterion("prevent <=", value, "prevent");
            return (Criteria) this;
        }

        public Criteria andPreventIn(List<Byte> values) {
            addCriterion("prevent in", values, "prevent");
            return (Criteria) this;
        }

        public Criteria andPreventNotIn(List<Byte> values) {
            addCriterion("prevent not in", values, "prevent");
            return (Criteria) this;
        }

        public Criteria andPreventBetween(Byte value1, Byte value2) {
            addCriterion("prevent between", value1, value2, "prevent");
            return (Criteria) this;
        }

        public Criteria andPreventNotBetween(Byte value1, Byte value2) {
            addCriterion("prevent not between", value1, value2, "prevent");
            return (Criteria) this;
        }

        public Criteria andRecomRoomIsNull() {
            addCriterion("recom_room is null");
            return (Criteria) this;
        }

        public Criteria andRecomRoomIsNotNull() {
            addCriterion("recom_room is not null");
            return (Criteria) this;
        }

        public Criteria andRecomRoomEqualTo(Byte value) {
            addCriterion("recom_room =", value, "recomRoom");
            return (Criteria) this;
        }

        public Criteria andRecomRoomNotEqualTo(Byte value) {
            addCriterion("recom_room <>", value, "recomRoom");
            return (Criteria) this;
        }

        public Criteria andRecomRoomGreaterThan(Byte value) {
            addCriterion("recom_room >", value, "recomRoom");
            return (Criteria) this;
        }

        public Criteria andRecomRoomGreaterThanOrEqualTo(Byte value) {
            addCriterion("recom_room >=", value, "recomRoom");
            return (Criteria) this;
        }

        public Criteria andRecomRoomLessThan(Byte value) {
            addCriterion("recom_room <", value, "recomRoom");
            return (Criteria) this;
        }

        public Criteria andRecomRoomLessThanOrEqualTo(Byte value) {
            addCriterion("recom_room <=", value, "recomRoom");
            return (Criteria) this;
        }

        public Criteria andRecomRoomIn(List<Byte> values) {
            addCriterion("recom_room in", values, "recomRoom");
            return (Criteria) this;
        }

        public Criteria andRecomRoomNotIn(List<Byte> values) {
            addCriterion("recom_room not in", values, "recomRoom");
            return (Criteria) this;
        }

        public Criteria andRecomRoomBetween(Byte value1, Byte value2) {
            addCriterion("recom_room between", value1, value2, "recomRoom");
            return (Criteria) this;
        }

        public Criteria andRecomRoomNotBetween(Byte value1, Byte value2) {
            addCriterion("recom_room not between", value1, value2, "recomRoom");
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
