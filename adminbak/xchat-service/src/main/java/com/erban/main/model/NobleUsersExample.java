package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NobleUsersExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public NobleUsersExample() {
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

        public Criteria andNobleIdIsNull() {
            addCriterion("noble_id is null");
            return (Criteria) this;
        }

        public Criteria andNobleIdIsNotNull() {
            addCriterion("noble_id is not null");
            return (Criteria) this;
        }

        public Criteria andNobleIdEqualTo(Integer value) {
            addCriterion("noble_id =", value, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdNotEqualTo(Integer value) {
            addCriterion("noble_id <>", value, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdGreaterThan(Integer value) {
            addCriterion("noble_id >", value, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("noble_id >=", value, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdLessThan(Integer value) {
            addCriterion("noble_id <", value, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdLessThanOrEqualTo(Integer value) {
            addCriterion("noble_id <=", value, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdIn(List<Integer> values) {
            addCriterion("noble_id in", values, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdNotIn(List<Integer> values) {
            addCriterion("noble_id not in", values, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdBetween(Integer value1, Integer value2) {
            addCriterion("noble_id between", value1, value2, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdNotBetween(Integer value1, Integer value2) {
            addCriterion("noble_id not between", value1, value2, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleNameIsNull() {
            addCriterion("noble_name is null");
            return (Criteria) this;
        }

        public Criteria andNobleNameIsNotNull() {
            addCriterion("noble_name is not null");
            return (Criteria) this;
        }

        public Criteria andNobleNameEqualTo(String value) {
            addCriterion("noble_name =", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameNotEqualTo(String value) {
            addCriterion("noble_name <>", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameGreaterThan(String value) {
            addCriterion("noble_name >", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameGreaterThanOrEqualTo(String value) {
            addCriterion("noble_name >=", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameLessThan(String value) {
            addCriterion("noble_name <", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameLessThanOrEqualTo(String value) {
            addCriterion("noble_name <=", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameLike(String value) {
            addCriterion("noble_name like", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameNotLike(String value) {
            addCriterion("noble_name not like", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameIn(List<String> values) {
            addCriterion("noble_name in", values, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameNotIn(List<String> values) {
            addCriterion("noble_name not in", values, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameBetween(String value1, String value2) {
            addCriterion("noble_name between", value1, value2, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameNotBetween(String value1, String value2) {
            addCriterion("noble_name not between", value1, value2, "nobleName");
            return (Criteria) this;
        }

        public Criteria andExpireIsNull() {
            addCriterion("expire is null");
            return (Criteria) this;
        }

        public Criteria andExpireIsNotNull() {
            addCriterion("expire is not null");
            return (Criteria) this;
        }

        public Criteria andExpireEqualTo(Date value) {
            addCriterion("expire =", value, "expire");
            return (Criteria) this;
        }

        public Criteria andExpireNotEqualTo(Date value) {
            addCriterion("expire <>", value, "expire");
            return (Criteria) this;
        }

        public Criteria andExpireGreaterThan(Date value) {
            addCriterion("expire >", value, "expire");
            return (Criteria) this;
        }

        public Criteria andExpireGreaterThanOrEqualTo(Date value) {
            addCriterion("expire >=", value, "expire");
            return (Criteria) this;
        }

        public Criteria andExpireLessThan(Date value) {
            addCriterion("expire <", value, "expire");
            return (Criteria) this;
        }

        public Criteria andExpireLessThanOrEqualTo(Date value) {
            addCriterion("expire <=", value, "expire");
            return (Criteria) this;
        }

        public Criteria andExpireIn(List<Date> values) {
            addCriterion("expire in", values, "expire");
            return (Criteria) this;
        }

        public Criteria andExpireNotIn(List<Date> values) {
            addCriterion("expire not in", values, "expire");
            return (Criteria) this;
        }

        public Criteria andExpireBetween(Date value1, Date value2) {
            addCriterion("expire between", value1, value2, "expire");
            return (Criteria) this;
        }

        public Criteria andExpireNotBetween(Date value1, Date value2) {
            addCriterion("expire not between", value1, value2, "expire");
            return (Criteria) this;
        }

        public Criteria andBadgeIdIsNull() {
            addCriterion("badge_id is null");
            return (Criteria) this;
        }

        public Criteria andBadgeIdIsNotNull() {
            addCriterion("badge_id is not null");
            return (Criteria) this;
        }

        public Criteria andBadgeIdEqualTo(Integer value) {
            addCriterion("badge_id =", value, "badgeId");
            return (Criteria) this;
        }

        public Criteria andBadgeIdNotEqualTo(Integer value) {
            addCriterion("badge_id <>", value, "badgeId");
            return (Criteria) this;
        }

        public Criteria andBadgeIdGreaterThan(Integer value) {
            addCriterion("badge_id >", value, "badgeId");
            return (Criteria) this;
        }

        public Criteria andBadgeIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("badge_id >=", value, "badgeId");
            return (Criteria) this;
        }

        public Criteria andBadgeIdLessThan(Integer value) {
            addCriterion("badge_id <", value, "badgeId");
            return (Criteria) this;
        }

        public Criteria andBadgeIdLessThanOrEqualTo(Integer value) {
            addCriterion("badge_id <=", value, "badgeId");
            return (Criteria) this;
        }

        public Criteria andBadgeIdIn(List<Integer> values) {
            addCriterion("badge_id in", values, "badgeId");
            return (Criteria) this;
        }

        public Criteria andBadgeIdNotIn(List<Integer> values) {
            addCriterion("badge_id not in", values, "badgeId");
            return (Criteria) this;
        }

        public Criteria andBadgeIdBetween(Integer value1, Integer value2) {
            addCriterion("badge_id between", value1, value2, "badgeId");
            return (Criteria) this;
        }

        public Criteria andBadgeIdNotBetween(Integer value1, Integer value2) {
            addCriterion("badge_id not between", value1, value2, "badgeId");
            return (Criteria) this;
        }

        public Criteria andBadgeIsNull() {
            addCriterion("badge is null");
            return (Criteria) this;
        }

        public Criteria andBadgeIsNotNull() {
            addCriterion("badge is not null");
            return (Criteria) this;
        }

        public Criteria andBadgeEqualTo(String value) {
            addCriterion("badge =", value, "badge");
            return (Criteria) this;
        }

        public Criteria andBadgeNotEqualTo(String value) {
            addCriterion("badge <>", value, "badge");
            return (Criteria) this;
        }

        public Criteria andBadgeGreaterThan(String value) {
            addCriterion("badge >", value, "badge");
            return (Criteria) this;
        }

        public Criteria andBadgeGreaterThanOrEqualTo(String value) {
            addCriterion("badge >=", value, "badge");
            return (Criteria) this;
        }

        public Criteria andBadgeLessThan(String value) {
            addCriterion("badge <", value, "badge");
            return (Criteria) this;
        }

        public Criteria andBadgeLessThanOrEqualTo(String value) {
            addCriterion("badge <=", value, "badge");
            return (Criteria) this;
        }

        public Criteria andBadgeLike(String value) {
            addCriterion("badge like", value, "badge");
            return (Criteria) this;
        }

        public Criteria andBadgeNotLike(String value) {
            addCriterion("badge not like", value, "badge");
            return (Criteria) this;
        }

        public Criteria andBadgeIn(List<String> values) {
            addCriterion("badge in", values, "badge");
            return (Criteria) this;
        }

        public Criteria andBadgeNotIn(List<String> values) {
            addCriterion("badge not in", values, "badge");
            return (Criteria) this;
        }

        public Criteria andBadgeBetween(String value1, String value2) {
            addCriterion("badge between", value1, value2, "badge");
            return (Criteria) this;
        }

        public Criteria andBadgeNotBetween(String value1, String value2) {
            addCriterion("badge not between", value1, value2, "badge");
            return (Criteria) this;
        }

        public Criteria andCardbgIdIsNull() {
            addCriterion("cardbg_id is null");
            return (Criteria) this;
        }

        public Criteria andCardbgIdIsNotNull() {
            addCriterion("cardbg_id is not null");
            return (Criteria) this;
        }

        public Criteria andCardbgIdEqualTo(Integer value) {
            addCriterion("cardbg_id =", value, "cardbgId");
            return (Criteria) this;
        }

        public Criteria andCardbgIdNotEqualTo(Integer value) {
            addCriterion("cardbg_id <>", value, "cardbgId");
            return (Criteria) this;
        }

        public Criteria andCardbgIdGreaterThan(Integer value) {
            addCriterion("cardbg_id >", value, "cardbgId");
            return (Criteria) this;
        }

        public Criteria andCardbgIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("cardbg_id >=", value, "cardbgId");
            return (Criteria) this;
        }

        public Criteria andCardbgIdLessThan(Integer value) {
            addCriterion("cardbg_id <", value, "cardbgId");
            return (Criteria) this;
        }

        public Criteria andCardbgIdLessThanOrEqualTo(Integer value) {
            addCriterion("cardbg_id <=", value, "cardbgId");
            return (Criteria) this;
        }

        public Criteria andCardbgIdIn(List<Integer> values) {
            addCriterion("cardbg_id in", values, "cardbgId");
            return (Criteria) this;
        }

        public Criteria andCardbgIdNotIn(List<Integer> values) {
            addCriterion("cardbg_id not in", values, "cardbgId");
            return (Criteria) this;
        }

        public Criteria andCardbgIdBetween(Integer value1, Integer value2) {
            addCriterion("cardbg_id between", value1, value2, "cardbgId");
            return (Criteria) this;
        }

        public Criteria andCardbgIdNotBetween(Integer value1, Integer value2) {
            addCriterion("cardbg_id not between", value1, value2, "cardbgId");
            return (Criteria) this;
        }

        public Criteria andCardbgIsNull() {
            addCriterion("cardbg is null");
            return (Criteria) this;
        }

        public Criteria andCardbgIsNotNull() {
            addCriterion("cardbg is not null");
            return (Criteria) this;
        }

        public Criteria andCardbgEqualTo(String value) {
            addCriterion("cardbg =", value, "cardbg");
            return (Criteria) this;
        }

        public Criteria andCardbgNotEqualTo(String value) {
            addCriterion("cardbg <>", value, "cardbg");
            return (Criteria) this;
        }

        public Criteria andCardbgGreaterThan(String value) {
            addCriterion("cardbg >", value, "cardbg");
            return (Criteria) this;
        }

        public Criteria andCardbgGreaterThanOrEqualTo(String value) {
            addCriterion("cardbg >=", value, "cardbg");
            return (Criteria) this;
        }

        public Criteria andCardbgLessThan(String value) {
            addCriterion("cardbg <", value, "cardbg");
            return (Criteria) this;
        }

        public Criteria andCardbgLessThanOrEqualTo(String value) {
            addCriterion("cardbg <=", value, "cardbg");
            return (Criteria) this;
        }

        public Criteria andCardbgLike(String value) {
            addCriterion("cardbg like", value, "cardbg");
            return (Criteria) this;
        }

        public Criteria andCardbgNotLike(String value) {
            addCriterion("cardbg not like", value, "cardbg");
            return (Criteria) this;
        }

        public Criteria andCardbgIn(List<String> values) {
            addCriterion("cardbg in", values, "cardbg");
            return (Criteria) this;
        }

        public Criteria andCardbgNotIn(List<String> values) {
            addCriterion("cardbg not in", values, "cardbg");
            return (Criteria) this;
        }

        public Criteria andCardbgBetween(String value1, String value2) {
            addCriterion("cardbg between", value1, value2, "cardbg");
            return (Criteria) this;
        }

        public Criteria andCardbgNotBetween(String value1, String value2) {
            addCriterion("cardbg not between", value1, value2, "cardbg");
            return (Criteria) this;
        }

        public Criteria andZonebgIdIsNull() {
            addCriterion("zonebg_id is null");
            return (Criteria) this;
        }

        public Criteria andZonebgIdIsNotNull() {
            addCriterion("zonebg_id is not null");
            return (Criteria) this;
        }

        public Criteria andZonebgIdEqualTo(Integer value) {
            addCriterion("zonebg_id =", value, "zonebgId");
            return (Criteria) this;
        }

        public Criteria andZonebgIdNotEqualTo(Integer value) {
            addCriterion("zonebg_id <>", value, "zonebgId");
            return (Criteria) this;
        }

        public Criteria andZonebgIdGreaterThan(Integer value) {
            addCriterion("zonebg_id >", value, "zonebgId");
            return (Criteria) this;
        }

        public Criteria andZonebgIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("zonebg_id >=", value, "zonebgId");
            return (Criteria) this;
        }

        public Criteria andZonebgIdLessThan(Integer value) {
            addCriterion("zonebg_id <", value, "zonebgId");
            return (Criteria) this;
        }

        public Criteria andZonebgIdLessThanOrEqualTo(Integer value) {
            addCriterion("zonebg_id <=", value, "zonebgId");
            return (Criteria) this;
        }

        public Criteria andZonebgIdIn(List<Integer> values) {
            addCriterion("zonebg_id in", values, "zonebgId");
            return (Criteria) this;
        }

        public Criteria andZonebgIdNotIn(List<Integer> values) {
            addCriterion("zonebg_id not in", values, "zonebgId");
            return (Criteria) this;
        }

        public Criteria andZonebgIdBetween(Integer value1, Integer value2) {
            addCriterion("zonebg_id between", value1, value2, "zonebgId");
            return (Criteria) this;
        }

        public Criteria andZonebgIdNotBetween(Integer value1, Integer value2) {
            addCriterion("zonebg_id not between", value1, value2, "zonebgId");
            return (Criteria) this;
        }

        public Criteria andZonebgIsNull() {
            addCriterion("zonebg is null");
            return (Criteria) this;
        }

        public Criteria andZonebgIsNotNull() {
            addCriterion("zonebg is not null");
            return (Criteria) this;
        }

        public Criteria andZonebgEqualTo(String value) {
            addCriterion("zonebg =", value, "zonebg");
            return (Criteria) this;
        }

        public Criteria andZonebgNotEqualTo(String value) {
            addCriterion("zonebg <>", value, "zonebg");
            return (Criteria) this;
        }

        public Criteria andZonebgGreaterThan(String value) {
            addCriterion("zonebg >", value, "zonebg");
            return (Criteria) this;
        }

        public Criteria andZonebgGreaterThanOrEqualTo(String value) {
            addCriterion("zonebg >=", value, "zonebg");
            return (Criteria) this;
        }

        public Criteria andZonebgLessThan(String value) {
            addCriterion("zonebg <", value, "zonebg");
            return (Criteria) this;
        }

        public Criteria andZonebgLessThanOrEqualTo(String value) {
            addCriterion("zonebg <=", value, "zonebg");
            return (Criteria) this;
        }

        public Criteria andZonebgLike(String value) {
            addCriterion("zonebg like", value, "zonebg");
            return (Criteria) this;
        }

        public Criteria andZonebgNotLike(String value) {
            addCriterion("zonebg not like", value, "zonebg");
            return (Criteria) this;
        }

        public Criteria andZonebgIn(List<String> values) {
            addCriterion("zonebg in", values, "zonebg");
            return (Criteria) this;
        }

        public Criteria andZonebgNotIn(List<String> values) {
            addCriterion("zonebg not in", values, "zonebg");
            return (Criteria) this;
        }

        public Criteria andZonebgBetween(String value1, String value2) {
            addCriterion("zonebg between", value1, value2, "zonebg");
            return (Criteria) this;
        }

        public Criteria andZonebgNotBetween(String value1, String value2) {
            addCriterion("zonebg not between", value1, value2, "zonebg");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundIdIsNull() {
            addCriterion("room_background_id is null");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundIdIsNotNull() {
            addCriterion("room_background_id is not null");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundIdEqualTo(Integer value) {
            addCriterion("room_background_id =", value, "roomBackgroundId");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundIdNotEqualTo(Integer value) {
            addCriterion("room_background_id <>", value, "roomBackgroundId");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundIdGreaterThan(Integer value) {
            addCriterion("room_background_id >", value, "roomBackgroundId");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("room_background_id >=", value, "roomBackgroundId");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundIdLessThan(Integer value) {
            addCriterion("room_background_id <", value, "roomBackgroundId");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundIdLessThanOrEqualTo(Integer value) {
            addCriterion("room_background_id <=", value, "roomBackgroundId");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundIdIn(List<Integer> values) {
            addCriterion("room_background_id in", values, "roomBackgroundId");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundIdNotIn(List<Integer> values) {
            addCriterion("room_background_id not in", values, "roomBackgroundId");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundIdBetween(Integer value1, Integer value2) {
            addCriterion("room_background_id between", value1, value2, "roomBackgroundId");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundIdNotBetween(Integer value1, Integer value2) {
            addCriterion("room_background_id not between", value1, value2, "roomBackgroundId");
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

        public Criteria andRoomBackgroundEqualTo(String value) {
            addCriterion("room_background =", value, "roomBackground");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundNotEqualTo(String value) {
            addCriterion("room_background <>", value, "roomBackground");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundGreaterThan(String value) {
            addCriterion("room_background >", value, "roomBackground");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundGreaterThanOrEqualTo(String value) {
            addCriterion("room_background >=", value, "roomBackground");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundLessThan(String value) {
            addCriterion("room_background <", value, "roomBackground");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundLessThanOrEqualTo(String value) {
            addCriterion("room_background <=", value, "roomBackground");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundLike(String value) {
            addCriterion("room_background like", value, "roomBackground");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundNotLike(String value) {
            addCriterion("room_background not like", value, "roomBackground");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundIn(List<String> values) {
            addCriterion("room_background in", values, "roomBackground");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundNotIn(List<String> values) {
            addCriterion("room_background not in", values, "roomBackground");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundBetween(String value1, String value2) {
            addCriterion("room_background between", value1, value2, "roomBackground");
            return (Criteria) this;
        }

        public Criteria andRoomBackgroundNotBetween(String value1, String value2) {
            addCriterion("room_background not between", value1, value2, "roomBackground");
            return (Criteria) this;
        }

        public Criteria andMicDecorateIdIsNull() {
            addCriterion("mic_decorate_id is null");
            return (Criteria) this;
        }

        public Criteria andMicDecorateIdIsNotNull() {
            addCriterion("mic_decorate_id is not null");
            return (Criteria) this;
        }

        public Criteria andMicDecorateIdEqualTo(Integer value) {
            addCriterion("mic_decorate_id =", value, "micDecorateId");
            return (Criteria) this;
        }

        public Criteria andMicDecorateIdNotEqualTo(Integer value) {
            addCriterion("mic_decorate_id <>", value, "micDecorateId");
            return (Criteria) this;
        }

        public Criteria andMicDecorateIdGreaterThan(Integer value) {
            addCriterion("mic_decorate_id >", value, "micDecorateId");
            return (Criteria) this;
        }

        public Criteria andMicDecorateIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("mic_decorate_id >=", value, "micDecorateId");
            return (Criteria) this;
        }

        public Criteria andMicDecorateIdLessThan(Integer value) {
            addCriterion("mic_decorate_id <", value, "micDecorateId");
            return (Criteria) this;
        }

        public Criteria andMicDecorateIdLessThanOrEqualTo(Integer value) {
            addCriterion("mic_decorate_id <=", value, "micDecorateId");
            return (Criteria) this;
        }

        public Criteria andMicDecorateIdIn(List<Integer> values) {
            addCriterion("mic_decorate_id in", values, "micDecorateId");
            return (Criteria) this;
        }

        public Criteria andMicDecorateIdNotIn(List<Integer> values) {
            addCriterion("mic_decorate_id not in", values, "micDecorateId");
            return (Criteria) this;
        }

        public Criteria andMicDecorateIdBetween(Integer value1, Integer value2) {
            addCriterion("mic_decorate_id between", value1, value2, "micDecorateId");
            return (Criteria) this;
        }

        public Criteria andMicDecorateIdNotBetween(Integer value1, Integer value2) {
            addCriterion("mic_decorate_id not between", value1, value2, "micDecorateId");
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

        public Criteria andMicDecorateEqualTo(String value) {
            addCriterion("mic_decorate =", value, "micDecorate");
            return (Criteria) this;
        }

        public Criteria andMicDecorateNotEqualTo(String value) {
            addCriterion("mic_decorate <>", value, "micDecorate");
            return (Criteria) this;
        }

        public Criteria andMicDecorateGreaterThan(String value) {
            addCriterion("mic_decorate >", value, "micDecorate");
            return (Criteria) this;
        }

        public Criteria andMicDecorateGreaterThanOrEqualTo(String value) {
            addCriterion("mic_decorate >=", value, "micDecorate");
            return (Criteria) this;
        }

        public Criteria andMicDecorateLessThan(String value) {
            addCriterion("mic_decorate <", value, "micDecorate");
            return (Criteria) this;
        }

        public Criteria andMicDecorateLessThanOrEqualTo(String value) {
            addCriterion("mic_decorate <=", value, "micDecorate");
            return (Criteria) this;
        }

        public Criteria andMicDecorateLike(String value) {
            addCriterion("mic_decorate like", value, "micDecorate");
            return (Criteria) this;
        }

        public Criteria andMicDecorateNotLike(String value) {
            addCriterion("mic_decorate not like", value, "micDecorate");
            return (Criteria) this;
        }

        public Criteria andMicDecorateIn(List<String> values) {
            addCriterion("mic_decorate in", values, "micDecorate");
            return (Criteria) this;
        }

        public Criteria andMicDecorateNotIn(List<String> values) {
            addCriterion("mic_decorate not in", values, "micDecorate");
            return (Criteria) this;
        }

        public Criteria andMicDecorateBetween(String value1, String value2) {
            addCriterion("mic_decorate between", value1, value2, "micDecorate");
            return (Criteria) this;
        }

        public Criteria andMicDecorateNotBetween(String value1, String value2) {
            addCriterion("mic_decorate not between", value1, value2, "micDecorate");
            return (Criteria) this;
        }

        public Criteria andChatBubbleIdIsNull() {
            addCriterion("chat_bubble_id is null");
            return (Criteria) this;
        }

        public Criteria andChatBubbleIdIsNotNull() {
            addCriterion("chat_bubble_id is not null");
            return (Criteria) this;
        }

        public Criteria andChatBubbleIdEqualTo(Integer value) {
            addCriterion("chat_bubble_id =", value, "chatBubbleId");
            return (Criteria) this;
        }

        public Criteria andChatBubbleIdNotEqualTo(Integer value) {
            addCriterion("chat_bubble_id <>", value, "chatBubbleId");
            return (Criteria) this;
        }

        public Criteria andChatBubbleIdGreaterThan(Integer value) {
            addCriterion("chat_bubble_id >", value, "chatBubbleId");
            return (Criteria) this;
        }

        public Criteria andChatBubbleIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("chat_bubble_id >=", value, "chatBubbleId");
            return (Criteria) this;
        }

        public Criteria andChatBubbleIdLessThan(Integer value) {
            addCriterion("chat_bubble_id <", value, "chatBubbleId");
            return (Criteria) this;
        }

        public Criteria andChatBubbleIdLessThanOrEqualTo(Integer value) {
            addCriterion("chat_bubble_id <=", value, "chatBubbleId");
            return (Criteria) this;
        }

        public Criteria andChatBubbleIdIn(List<Integer> values) {
            addCriterion("chat_bubble_id in", values, "chatBubbleId");
            return (Criteria) this;
        }

        public Criteria andChatBubbleIdNotIn(List<Integer> values) {
            addCriterion("chat_bubble_id not in", values, "chatBubbleId");
            return (Criteria) this;
        }

        public Criteria andChatBubbleIdBetween(Integer value1, Integer value2) {
            addCriterion("chat_bubble_id between", value1, value2, "chatBubbleId");
            return (Criteria) this;
        }

        public Criteria andChatBubbleIdNotBetween(Integer value1, Integer value2) {
            addCriterion("chat_bubble_id not between", value1, value2, "chatBubbleId");
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

        public Criteria andChatBubbleEqualTo(String value) {
            addCriterion("chat_bubble =", value, "chatBubble");
            return (Criteria) this;
        }

        public Criteria andChatBubbleNotEqualTo(String value) {
            addCriterion("chat_bubble <>", value, "chatBubble");
            return (Criteria) this;
        }

        public Criteria andChatBubbleGreaterThan(String value) {
            addCriterion("chat_bubble >", value, "chatBubble");
            return (Criteria) this;
        }

        public Criteria andChatBubbleGreaterThanOrEqualTo(String value) {
            addCriterion("chat_bubble >=", value, "chatBubble");
            return (Criteria) this;
        }

        public Criteria andChatBubbleLessThan(String value) {
            addCriterion("chat_bubble <", value, "chatBubble");
            return (Criteria) this;
        }

        public Criteria andChatBubbleLessThanOrEqualTo(String value) {
            addCriterion("chat_bubble <=", value, "chatBubble");
            return (Criteria) this;
        }

        public Criteria andChatBubbleLike(String value) {
            addCriterion("chat_bubble like", value, "chatBubble");
            return (Criteria) this;
        }

        public Criteria andChatBubbleNotLike(String value) {
            addCriterion("chat_bubble not like", value, "chatBubble");
            return (Criteria) this;
        }

        public Criteria andChatBubbleIn(List<String> values) {
            addCriterion("chat_bubble in", values, "chatBubble");
            return (Criteria) this;
        }

        public Criteria andChatBubbleNotIn(List<String> values) {
            addCriterion("chat_bubble not in", values, "chatBubble");
            return (Criteria) this;
        }

        public Criteria andChatBubbleBetween(String value1, String value2) {
            addCriterion("chat_bubble between", value1, value2, "chatBubble");
            return (Criteria) this;
        }

        public Criteria andChatBubbleNotBetween(String value1, String value2) {
            addCriterion("chat_bubble not between", value1, value2, "chatBubble");
            return (Criteria) this;
        }

        public Criteria andMicHaloIdIsNull() {
            addCriterion("mic_halo_id is null");
            return (Criteria) this;
        }

        public Criteria andMicHaloIdIsNotNull() {
            addCriterion("mic_halo_id is not null");
            return (Criteria) this;
        }

        public Criteria andMicHaloIdEqualTo(Integer value) {
            addCriterion("mic_halo_id =", value, "micHaloId");
            return (Criteria) this;
        }

        public Criteria andMicHaloIdNotEqualTo(Integer value) {
            addCriterion("mic_halo_id <>", value, "micHaloId");
            return (Criteria) this;
        }

        public Criteria andMicHaloIdGreaterThan(Integer value) {
            addCriterion("mic_halo_id >", value, "micHaloId");
            return (Criteria) this;
        }

        public Criteria andMicHaloIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("mic_halo_id >=", value, "micHaloId");
            return (Criteria) this;
        }

        public Criteria andMicHaloIdLessThan(Integer value) {
            addCriterion("mic_halo_id <", value, "micHaloId");
            return (Criteria) this;
        }

        public Criteria andMicHaloIdLessThanOrEqualTo(Integer value) {
            addCriterion("mic_halo_id <=", value, "micHaloId");
            return (Criteria) this;
        }

        public Criteria andMicHaloIdIn(List<Integer> values) {
            addCriterion("mic_halo_id in", values, "micHaloId");
            return (Criteria) this;
        }

        public Criteria andMicHaloIdNotIn(List<Integer> values) {
            addCriterion("mic_halo_id not in", values, "micHaloId");
            return (Criteria) this;
        }

        public Criteria andMicHaloIdBetween(Integer value1, Integer value2) {
            addCriterion("mic_halo_id between", value1, value2, "micHaloId");
            return (Criteria) this;
        }

        public Criteria andMicHaloIdNotBetween(Integer value1, Integer value2) {
            addCriterion("mic_halo_id not between", value1, value2, "micHaloId");
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

        public Criteria andMicHaloEqualTo(String value) {
            addCriterion("mic_halo =", value, "micHalo");
            return (Criteria) this;
        }

        public Criteria andMicHaloNotEqualTo(String value) {
            addCriterion("mic_halo <>", value, "micHalo");
            return (Criteria) this;
        }

        public Criteria andMicHaloGreaterThan(String value) {
            addCriterion("mic_halo >", value, "micHalo");
            return (Criteria) this;
        }

        public Criteria andMicHaloGreaterThanOrEqualTo(String value) {
            addCriterion("mic_halo >=", value, "micHalo");
            return (Criteria) this;
        }

        public Criteria andMicHaloLessThan(String value) {
            addCriterion("mic_halo <", value, "micHalo");
            return (Criteria) this;
        }

        public Criteria andMicHaloLessThanOrEqualTo(String value) {
            addCriterion("mic_halo <=", value, "micHalo");
            return (Criteria) this;
        }

        public Criteria andMicHaloLike(String value) {
            addCriterion("mic_halo like", value, "micHalo");
            return (Criteria) this;
        }

        public Criteria andMicHaloNotLike(String value) {
            addCriterion("mic_halo not like", value, "micHalo");
            return (Criteria) this;
        }

        public Criteria andMicHaloIn(List<String> values) {
            addCriterion("mic_halo in", values, "micHalo");
            return (Criteria) this;
        }

        public Criteria andMicHaloNotIn(List<String> values) {
            addCriterion("mic_halo not in", values, "micHalo");
            return (Criteria) this;
        }

        public Criteria andMicHaloBetween(String value1, String value2) {
            addCriterion("mic_halo between", value1, value2, "micHalo");
            return (Criteria) this;
        }

        public Criteria andMicHaloNotBetween(String value1, String value2) {
            addCriterion("mic_halo not between", value1, value2, "micHalo");
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

        public Criteria andGoodNumIsNull() {
            addCriterion("good_num is null");
            return (Criteria) this;
        }

        public Criteria andGoodNumIsNotNull() {
            addCriterion("good_num is not null");
            return (Criteria) this;
        }

        public Criteria andGoodNumEqualTo(Long value) {
            addCriterion("good_num =", value, "goodNum");
            return (Criteria) this;
        }

        public Criteria andGoodNumNotEqualTo(Long value) {
            addCriterion("good_num <>", value, "goodNum");
            return (Criteria) this;
        }

        public Criteria andGoodNumGreaterThan(Long value) {
            addCriterion("good_num >", value, "goodNum");
            return (Criteria) this;
        }

        public Criteria andGoodNumGreaterThanOrEqualTo(Long value) {
            addCriterion("good_num >=", value, "goodNum");
            return (Criteria) this;
        }

        public Criteria andGoodNumLessThan(Long value) {
            addCriterion("good_num <", value, "goodNum");
            return (Criteria) this;
        }

        public Criteria andGoodNumLessThanOrEqualTo(Long value) {
            addCriterion("good_num <=", value, "goodNum");
            return (Criteria) this;
        }

        public Criteria andGoodNumIn(List<Long> values) {
            addCriterion("good_num in", values, "goodNum");
            return (Criteria) this;
        }

        public Criteria andGoodNumNotIn(List<Long> values) {
            addCriterion("good_num not in", values, "goodNum");
            return (Criteria) this;
        }

        public Criteria andGoodNumBetween(Long value1, Long value2) {
            addCriterion("good_num between", value1, value2, "goodNum");
            return (Criteria) this;
        }

        public Criteria andGoodNumNotBetween(Long value1, Long value2) {
            addCriterion("good_num not between", value1, value2, "goodNum");
            return (Criteria) this;
        }

        public Criteria andRecomCountIsNull() {
            addCriterion("recom_count is null");
            return (Criteria) this;
        }

        public Criteria andRecomCountIsNotNull() {
            addCriterion("recom_count is not null");
            return (Criteria) this;
        }

        public Criteria andRecomCountEqualTo(Byte value) {
            addCriterion("recom_count =", value, "recomCount");
            return (Criteria) this;
        }

        public Criteria andRecomCountNotEqualTo(Byte value) {
            addCriterion("recom_count <>", value, "recomCount");
            return (Criteria) this;
        }

        public Criteria andRecomCountGreaterThan(Byte value) {
            addCriterion("recom_count >", value, "recomCount");
            return (Criteria) this;
        }

        public Criteria andRecomCountGreaterThanOrEqualTo(Byte value) {
            addCriterion("recom_count >=", value, "recomCount");
            return (Criteria) this;
        }

        public Criteria andRecomCountLessThan(Byte value) {
            addCriterion("recom_count <", value, "recomCount");
            return (Criteria) this;
        }

        public Criteria andRecomCountLessThanOrEqualTo(Byte value) {
            addCriterion("recom_count <=", value, "recomCount");
            return (Criteria) this;
        }

        public Criteria andRecomCountIn(List<Byte> values) {
            addCriterion("recom_count in", values, "recomCount");
            return (Criteria) this;
        }

        public Criteria andRecomCountNotIn(List<Byte> values) {
            addCriterion("recom_count not in", values, "recomCount");
            return (Criteria) this;
        }

        public Criteria andRecomCountBetween(Byte value1, Byte value2) {
            addCriterion("recom_count between", value1, value2, "recomCount");
            return (Criteria) this;
        }

        public Criteria andRecomCountNotBetween(Byte value1, Byte value2) {
            addCriterion("recom_count not between", value1, value2, "recomCount");
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

        public Criteria andRenewTimeIsNull() {
            addCriterion("renew_time is null");
            return (Criteria) this;
        }

        public Criteria andRenewTimeIsNotNull() {
            addCriterion("renew_time is not null");
            return (Criteria) this;
        }

        public Criteria andRenewTimeEqualTo(Date value) {
            addCriterion("renew_time =", value, "renewTime");
            return (Criteria) this;
        }

        public Criteria andRenewTimeNotEqualTo(Date value) {
            addCriterion("renew_time <>", value, "renewTime");
            return (Criteria) this;
        }

        public Criteria andRenewTimeGreaterThan(Date value) {
            addCriterion("renew_time >", value, "renewTime");
            return (Criteria) this;
        }

        public Criteria andRenewTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("renew_time >=", value, "renewTime");
            return (Criteria) this;
        }

        public Criteria andRenewTimeLessThan(Date value) {
            addCriterion("renew_time <", value, "renewTime");
            return (Criteria) this;
        }

        public Criteria andRenewTimeLessThanOrEqualTo(Date value) {
            addCriterion("renew_time <=", value, "renewTime");
            return (Criteria) this;
        }

        public Criteria andRenewTimeIn(List<Date> values) {
            addCriterion("renew_time in", values, "renewTime");
            return (Criteria) this;
        }

        public Criteria andRenewTimeNotIn(List<Date> values) {
            addCriterion("renew_time not in", values, "renewTime");
            return (Criteria) this;
        }

        public Criteria andRenewTimeBetween(Date value1, Date value2) {
            addCriterion("renew_time between", value1, value2, "renewTime");
            return (Criteria) this;
        }

        public Criteria andRenewTimeNotBetween(Date value1, Date value2) {
            addCriterion("renew_time not between", value1, value2, "renewTime");
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
