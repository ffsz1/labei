package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderServExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public OrderServExample() {
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

        public Criteria andOrderIdIsNull() {
            addCriterion("order_id is null");
            return (Criteria) this;
        }

        public Criteria andOrderIdIsNotNull() {
            addCriterion("order_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrderIdEqualTo(Long value) {
            addCriterion("order_id =", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotEqualTo(Long value) {
            addCriterion("order_id <>", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThan(Long value) {
            addCriterion("order_id >", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThanOrEqualTo(Long value) {
            addCriterion("order_id >=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThan(Long value) {
            addCriterion("order_id <", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThanOrEqualTo(Long value) {
            addCriterion("order_id <=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdIn(List<Long> values) {
            addCriterion("order_id in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotIn(List<Long> values) {
            addCriterion("order_id not in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdBetween(Long value1, Long value2) {
            addCriterion("order_id between", value1, value2, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotBetween(Long value1, Long value2) {
            addCriterion("order_id not between", value1, value2, "orderId");
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

        public Criteria andProdUidIsNull() {
            addCriterion("prod_uid is null");
            return (Criteria) this;
        }

        public Criteria andProdUidIsNotNull() {
            addCriterion("prod_uid is not null");
            return (Criteria) this;
        }

        public Criteria andProdUidEqualTo(Long value) {
            addCriterion("prod_uid =", value, "prodUid");
            return (Criteria) this;
        }

        public Criteria andProdUidNotEqualTo(Long value) {
            addCriterion("prod_uid <>", value, "prodUid");
            return (Criteria) this;
        }

        public Criteria andProdUidGreaterThan(Long value) {
            addCriterion("prod_uid >", value, "prodUid");
            return (Criteria) this;
        }

        public Criteria andProdUidGreaterThanOrEqualTo(Long value) {
            addCriterion("prod_uid >=", value, "prodUid");
            return (Criteria) this;
        }

        public Criteria andProdUidLessThan(Long value) {
            addCriterion("prod_uid <", value, "prodUid");
            return (Criteria) this;
        }

        public Criteria andProdUidLessThanOrEqualTo(Long value) {
            addCriterion("prod_uid <=", value, "prodUid");
            return (Criteria) this;
        }

        public Criteria andProdUidIn(List<Long> values) {
            addCriterion("prod_uid in", values, "prodUid");
            return (Criteria) this;
        }

        public Criteria andProdUidNotIn(List<Long> values) {
            addCriterion("prod_uid not in", values, "prodUid");
            return (Criteria) this;
        }

        public Criteria andProdUidBetween(Long value1, Long value2) {
            addCriterion("prod_uid between", value1, value2, "prodUid");
            return (Criteria) this;
        }

        public Criteria andProdUidNotBetween(Long value1, Long value2) {
            addCriterion("prod_uid not between", value1, value2, "prodUid");
            return (Criteria) this;
        }

        public Criteria andRoomOwnerUidIsNull() {
            addCriterion("room_owner_uid is null");
            return (Criteria) this;
        }

        public Criteria andRoomOwnerUidIsNotNull() {
            addCriterion("room_owner_uid is not null");
            return (Criteria) this;
        }

        public Criteria andRoomOwnerUidEqualTo(Long value) {
            addCriterion("room_owner_uid =", value, "roomOwnerUid");
            return (Criteria) this;
        }

        public Criteria andRoomOwnerUidNotEqualTo(Long value) {
            addCriterion("room_owner_uid <>", value, "roomOwnerUid");
            return (Criteria) this;
        }

        public Criteria andRoomOwnerUidGreaterThan(Long value) {
            addCriterion("room_owner_uid >", value, "roomOwnerUid");
            return (Criteria) this;
        }

        public Criteria andRoomOwnerUidGreaterThanOrEqualTo(Long value) {
            addCriterion("room_owner_uid >=", value, "roomOwnerUid");
            return (Criteria) this;
        }

        public Criteria andRoomOwnerUidLessThan(Long value) {
            addCriterion("room_owner_uid <", value, "roomOwnerUid");
            return (Criteria) this;
        }

        public Criteria andRoomOwnerUidLessThanOrEqualTo(Long value) {
            addCriterion("room_owner_uid <=", value, "roomOwnerUid");
            return (Criteria) this;
        }

        public Criteria andRoomOwnerUidIn(List<Long> values) {
            addCriterion("room_owner_uid in", values, "roomOwnerUid");
            return (Criteria) this;
        }

        public Criteria andRoomOwnerUidNotIn(List<Long> values) {
            addCriterion("room_owner_uid not in", values, "roomOwnerUid");
            return (Criteria) this;
        }

        public Criteria andRoomOwnerUidBetween(Long value1, Long value2) {
            addCriterion("room_owner_uid between", value1, value2, "roomOwnerUid");
            return (Criteria) this;
        }

        public Criteria andRoomOwnerUidNotBetween(Long value1, Long value2) {
            addCriterion("room_owner_uid not between", value1, value2, "roomOwnerUid");
            return (Criteria) this;
        }

        public Criteria andOrderTypeIsNull() {
            addCriterion("order_type is null");
            return (Criteria) this;
        }

        public Criteria andOrderTypeIsNotNull() {
            addCriterion("order_type is not null");
            return (Criteria) this;
        }

        public Criteria andOrderTypeEqualTo(Byte value) {
            addCriterion("order_type =", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeNotEqualTo(Byte value) {
            addCriterion("order_type <>", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeGreaterThan(Byte value) {
            addCriterion("order_type >", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("order_type >=", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeLessThan(Byte value) {
            addCriterion("order_type <", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeLessThanOrEqualTo(Byte value) {
            addCriterion("order_type <=", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeIn(List<Byte> values) {
            addCriterion("order_type in", values, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeNotIn(List<Byte> values) {
            addCriterion("order_type not in", values, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeBetween(Byte value1, Byte value2) {
            addCriterion("order_type between", value1, value2, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("order_type not between", value1, value2, "orderType");
            return (Criteria) this;
        }

        public Criteria andObjIdIsNull() {
            addCriterion("obj_id is null");
            return (Criteria) this;
        }

        public Criteria andObjIdIsNotNull() {
            addCriterion("obj_id is not null");
            return (Criteria) this;
        }

        public Criteria andObjIdEqualTo(String value) {
            addCriterion("obj_id =", value, "objId");
            return (Criteria) this;
        }

        public Criteria andObjIdNotEqualTo(String value) {
            addCriterion("obj_id <>", value, "objId");
            return (Criteria) this;
        }

        public Criteria andObjIdGreaterThan(String value) {
            addCriterion("obj_id >", value, "objId");
            return (Criteria) this;
        }

        public Criteria andObjIdGreaterThanOrEqualTo(String value) {
            addCriterion("obj_id >=", value, "objId");
            return (Criteria) this;
        }

        public Criteria andObjIdLessThan(String value) {
            addCriterion("obj_id <", value, "objId");
            return (Criteria) this;
        }

        public Criteria andObjIdLessThanOrEqualTo(String value) {
            addCriterion("obj_id <=", value, "objId");
            return (Criteria) this;
        }

        public Criteria andObjIdLike(String value) {
            addCriterion("obj_id like", value, "objId");
            return (Criteria) this;
        }

        public Criteria andObjIdNotLike(String value) {
            addCriterion("obj_id not like", value, "objId");
            return (Criteria) this;
        }

        public Criteria andObjIdIn(List<String> values) {
            addCriterion("obj_id in", values, "objId");
            return (Criteria) this;
        }

        public Criteria andObjIdNotIn(List<String> values) {
            addCriterion("obj_id not in", values, "objId");
            return (Criteria) this;
        }

        public Criteria andObjIdBetween(String value1, String value2) {
            addCriterion("obj_id between", value1, value2, "objId");
            return (Criteria) this;
        }

        public Criteria andObjIdNotBetween(String value1, String value2) {
            addCriterion("obj_id not between", value1, value2, "objId");
            return (Criteria) this;
        }

        public Criteria andCurStatusIsNull() {
            addCriterion("cur_status is null");
            return (Criteria) this;
        }

        public Criteria andCurStatusIsNotNull() {
            addCriterion("cur_status is not null");
            return (Criteria) this;
        }

        public Criteria andCurStatusEqualTo(Byte value) {
            addCriterion("cur_status =", value, "curStatus");
            return (Criteria) this;
        }

        public Criteria andCurStatusNotEqualTo(Byte value) {
            addCriterion("cur_status <>", value, "curStatus");
            return (Criteria) this;
        }

        public Criteria andCurStatusGreaterThan(Byte value) {
            addCriterion("cur_status >", value, "curStatus");
            return (Criteria) this;
        }

        public Criteria andCurStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("cur_status >=", value, "curStatus");
            return (Criteria) this;
        }

        public Criteria andCurStatusLessThan(Byte value) {
            addCriterion("cur_status <", value, "curStatus");
            return (Criteria) this;
        }

        public Criteria andCurStatusLessThanOrEqualTo(Byte value) {
            addCriterion("cur_status <=", value, "curStatus");
            return (Criteria) this;
        }

        public Criteria andCurStatusIn(List<Byte> values) {
            addCriterion("cur_status in", values, "curStatus");
            return (Criteria) this;
        }

        public Criteria andCurStatusNotIn(List<Byte> values) {
            addCriterion("cur_status not in", values, "curStatus");
            return (Criteria) this;
        }

        public Criteria andCurStatusBetween(Byte value1, Byte value2) {
            addCriterion("cur_status between", value1, value2, "curStatus");
            return (Criteria) this;
        }

        public Criteria andCurStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("cur_status not between", value1, value2, "curStatus");
            return (Criteria) this;
        }

        public Criteria andChannelIdIsNull() {
            addCriterion("channel_id is null");
            return (Criteria) this;
        }

        public Criteria andChannelIdIsNotNull() {
            addCriterion("channel_id is not null");
            return (Criteria) this;
        }

        public Criteria andChannelIdEqualTo(String value) {
            addCriterion("channel_id =", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotEqualTo(String value) {
            addCriterion("channel_id <>", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdGreaterThan(String value) {
            addCriterion("channel_id >", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdGreaterThanOrEqualTo(String value) {
            addCriterion("channel_id >=", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdLessThan(String value) {
            addCriterion("channel_id <", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdLessThanOrEqualTo(String value) {
            addCriterion("channel_id <=", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdLike(String value) {
            addCriterion("channel_id like", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotLike(String value) {
            addCriterion("channel_id not like", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdIn(List<String> values) {
            addCriterion("channel_id in", values, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotIn(List<String> values) {
            addCriterion("channel_id not in", values, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdBetween(String value1, String value2) {
            addCriterion("channel_id between", value1, value2, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotBetween(String value1, String value2) {
            addCriterion("channel_id not between", value1, value2, "channelId");
            return (Criteria) this;
        }

        public Criteria andBeginServTimeIsNull() {
            addCriterion("begin_serv_time is null");
            return (Criteria) this;
        }

        public Criteria andBeginServTimeIsNotNull() {
            addCriterion("begin_serv_time is not null");
            return (Criteria) this;
        }

        public Criteria andBeginServTimeEqualTo(Date value) {
            addCriterion("begin_serv_time =", value, "beginServTime");
            return (Criteria) this;
        }

        public Criteria andBeginServTimeNotEqualTo(Date value) {
            addCriterion("begin_serv_time <>", value, "beginServTime");
            return (Criteria) this;
        }

        public Criteria andBeginServTimeGreaterThan(Date value) {
            addCriterion("begin_serv_time >", value, "beginServTime");
            return (Criteria) this;
        }

        public Criteria andBeginServTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("begin_serv_time >=", value, "beginServTime");
            return (Criteria) this;
        }

        public Criteria andBeginServTimeLessThan(Date value) {
            addCriterion("begin_serv_time <", value, "beginServTime");
            return (Criteria) this;
        }

        public Criteria andBeginServTimeLessThanOrEqualTo(Date value) {
            addCriterion("begin_serv_time <=", value, "beginServTime");
            return (Criteria) this;
        }

        public Criteria andBeginServTimeIn(List<Date> values) {
            addCriterion("begin_serv_time in", values, "beginServTime");
            return (Criteria) this;
        }

        public Criteria andBeginServTimeNotIn(List<Date> values) {
            addCriterion("begin_serv_time not in", values, "beginServTime");
            return (Criteria) this;
        }

        public Criteria andBeginServTimeBetween(Date value1, Date value2) {
            addCriterion("begin_serv_time between", value1, value2, "beginServTime");
            return (Criteria) this;
        }

        public Criteria andBeginServTimeNotBetween(Date value1, Date value2) {
            addCriterion("begin_serv_time not between", value1, value2, "beginServTime");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyIsNull() {
            addCriterion("total_money is null");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyIsNotNull() {
            addCriterion("total_money is not null");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyEqualTo(Long value) {
            addCriterion("total_money =", value, "totalMoney");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyNotEqualTo(Long value) {
            addCriterion("total_money <>", value, "totalMoney");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyGreaterThan(Long value) {
            addCriterion("total_money >", value, "totalMoney");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyGreaterThanOrEqualTo(Long value) {
            addCriterion("total_money >=", value, "totalMoney");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyLessThan(Long value) {
            addCriterion("total_money <", value, "totalMoney");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyLessThanOrEqualTo(Long value) {
            addCriterion("total_money <=", value, "totalMoney");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyIn(List<Long> values) {
            addCriterion("total_money in", values, "totalMoney");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyNotIn(List<Long> values) {
            addCriterion("total_money not in", values, "totalMoney");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyBetween(Long value1, Long value2) {
            addCriterion("total_money between", value1, value2, "totalMoney");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyNotBetween(Long value1, Long value2) {
            addCriterion("total_money not between", value1, value2, "totalMoney");
            return (Criteria) this;
        }

        public Criteria andServScoreIsNull() {
            addCriterion("serv_score is null");
            return (Criteria) this;
        }

        public Criteria andServScoreIsNotNull() {
            addCriterion("serv_score is not null");
            return (Criteria) this;
        }

        public Criteria andServScoreEqualTo(Integer value) {
            addCriterion("serv_score =", value, "servScore");
            return (Criteria) this;
        }

        public Criteria andServScoreNotEqualTo(Integer value) {
            addCriterion("serv_score <>", value, "servScore");
            return (Criteria) this;
        }

        public Criteria andServScoreGreaterThan(Integer value) {
            addCriterion("serv_score >", value, "servScore");
            return (Criteria) this;
        }

        public Criteria andServScoreGreaterThanOrEqualTo(Integer value) {
            addCriterion("serv_score >=", value, "servScore");
            return (Criteria) this;
        }

        public Criteria andServScoreLessThan(Integer value) {
            addCriterion("serv_score <", value, "servScore");
            return (Criteria) this;
        }

        public Criteria andServScoreLessThanOrEqualTo(Integer value) {
            addCriterion("serv_score <=", value, "servScore");
            return (Criteria) this;
        }

        public Criteria andServScoreIn(List<Integer> values) {
            addCriterion("serv_score in", values, "servScore");
            return (Criteria) this;
        }

        public Criteria andServScoreNotIn(List<Integer> values) {
            addCriterion("serv_score not in", values, "servScore");
            return (Criteria) this;
        }

        public Criteria andServScoreBetween(Integer value1, Integer value2) {
            addCriterion("serv_score between", value1, value2, "servScore");
            return (Criteria) this;
        }

        public Criteria andServScoreNotBetween(Integer value1, Integer value2) {
            addCriterion("serv_score not between", value1, value2, "servScore");
            return (Criteria) this;
        }

        public Criteria andServSocreDescIsNull() {
            addCriterion("serv_socre_desc is null");
            return (Criteria) this;
        }

        public Criteria andServSocreDescIsNotNull() {
            addCriterion("serv_socre_desc is not null");
            return (Criteria) this;
        }

        public Criteria andServSocreDescEqualTo(String value) {
            addCriterion("serv_socre_desc =", value, "servSocreDesc");
            return (Criteria) this;
        }

        public Criteria andServSocreDescNotEqualTo(String value) {
            addCriterion("serv_socre_desc <>", value, "servSocreDesc");
            return (Criteria) this;
        }

        public Criteria andServSocreDescGreaterThan(String value) {
            addCriterion("serv_socre_desc >", value, "servSocreDesc");
            return (Criteria) this;
        }

        public Criteria andServSocreDescGreaterThanOrEqualTo(String value) {
            addCriterion("serv_socre_desc >=", value, "servSocreDesc");
            return (Criteria) this;
        }

        public Criteria andServSocreDescLessThan(String value) {
            addCriterion("serv_socre_desc <", value, "servSocreDesc");
            return (Criteria) this;
        }

        public Criteria andServSocreDescLessThanOrEqualTo(String value) {
            addCriterion("serv_socre_desc <=", value, "servSocreDesc");
            return (Criteria) this;
        }

        public Criteria andServSocreDescLike(String value) {
            addCriterion("serv_socre_desc like", value, "servSocreDesc");
            return (Criteria) this;
        }

        public Criteria andServSocreDescNotLike(String value) {
            addCriterion("serv_socre_desc not like", value, "servSocreDesc");
            return (Criteria) this;
        }

        public Criteria andServSocreDescIn(List<String> values) {
            addCriterion("serv_socre_desc in", values, "servSocreDesc");
            return (Criteria) this;
        }

        public Criteria andServSocreDescNotIn(List<String> values) {
            addCriterion("serv_socre_desc not in", values, "servSocreDesc");
            return (Criteria) this;
        }

        public Criteria andServSocreDescBetween(String value1, String value2) {
            addCriterion("serv_socre_desc between", value1, value2, "servSocreDesc");
            return (Criteria) this;
        }

        public Criteria andServSocreDescNotBetween(String value1, String value2) {
            addCriterion("serv_socre_desc not between", value1, value2, "servSocreDesc");
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

        public Criteria andFinishTimeIsNull() {
            addCriterion("finish_time is null");
            return (Criteria) this;
        }

        public Criteria andFinishTimeIsNotNull() {
            addCriterion("finish_time is not null");
            return (Criteria) this;
        }

        public Criteria andFinishTimeEqualTo(Date value) {
            addCriterion("finish_time =", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeNotEqualTo(Date value) {
            addCriterion("finish_time <>", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeGreaterThan(Date value) {
            addCriterion("finish_time >", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("finish_time >=", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeLessThan(Date value) {
            addCriterion("finish_time <", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeLessThanOrEqualTo(Date value) {
            addCriterion("finish_time <=", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeIn(List<Date> values) {
            addCriterion("finish_time in", values, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeNotIn(List<Date> values) {
            addCriterion("finish_time not in", values, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeBetween(Date value1, Date value2) {
            addCriterion("finish_time between", value1, value2, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeNotBetween(Date value1, Date value2) {
            addCriterion("finish_time not between", value1, value2, "finishTime");
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
