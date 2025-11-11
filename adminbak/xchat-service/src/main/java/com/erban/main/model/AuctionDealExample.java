package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuctionDealExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AuctionDealExample() {
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

        public Criteria andAuctIdIsNull() {
            addCriterion("auct_id is null");
            return (Criteria) this;
        }

        public Criteria andAuctIdIsNotNull() {
            addCriterion("auct_id is not null");
            return (Criteria) this;
        }

        public Criteria andAuctIdEqualTo(String value) {
            addCriterion("auct_id =", value, "auctId");
            return (Criteria) this;
        }

        public Criteria andAuctIdNotEqualTo(String value) {
            addCriterion("auct_id <>", value, "auctId");
            return (Criteria) this;
        }

        public Criteria andAuctIdGreaterThan(String value) {
            addCriterion("auct_id >", value, "auctId");
            return (Criteria) this;
        }

        public Criteria andAuctIdGreaterThanOrEqualTo(String value) {
            addCriterion("auct_id >=", value, "auctId");
            return (Criteria) this;
        }

        public Criteria andAuctIdLessThan(String value) {
            addCriterion("auct_id <", value, "auctId");
            return (Criteria) this;
        }

        public Criteria andAuctIdLessThanOrEqualTo(String value) {
            addCriterion("auct_id <=", value, "auctId");
            return (Criteria) this;
        }

        public Criteria andAuctIdLike(String value) {
            addCriterion("auct_id like", value, "auctId");
            return (Criteria) this;
        }

        public Criteria andAuctIdNotLike(String value) {
            addCriterion("auct_id not like", value, "auctId");
            return (Criteria) this;
        }

        public Criteria andAuctIdIn(List<String> values) {
            addCriterion("auct_id in", values, "auctId");
            return (Criteria) this;
        }

        public Criteria andAuctIdNotIn(List<String> values) {
            addCriterion("auct_id not in", values, "auctId");
            return (Criteria) this;
        }

        public Criteria andAuctIdBetween(String value1, String value2) {
            addCriterion("auct_id between", value1, value2, "auctId");
            return (Criteria) this;
        }

        public Criteria andAuctIdNotBetween(String value1, String value2) {
            addCriterion("auct_id not between", value1, value2, "auctId");
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

        public Criteria andAuctUidIsNull() {
            addCriterion("auct_uid is null");
            return (Criteria) this;
        }

        public Criteria andAuctUidIsNotNull() {
            addCriterion("auct_uid is not null");
            return (Criteria) this;
        }

        public Criteria andAuctUidEqualTo(Long value) {
            addCriterion("auct_uid =", value, "auctUid");
            return (Criteria) this;
        }

        public Criteria andAuctUidNotEqualTo(Long value) {
            addCriterion("auct_uid <>", value, "auctUid");
            return (Criteria) this;
        }

        public Criteria andAuctUidGreaterThan(Long value) {
            addCriterion("auct_uid >", value, "auctUid");
            return (Criteria) this;
        }

        public Criteria andAuctUidGreaterThanOrEqualTo(Long value) {
            addCriterion("auct_uid >=", value, "auctUid");
            return (Criteria) this;
        }

        public Criteria andAuctUidLessThan(Long value) {
            addCriterion("auct_uid <", value, "auctUid");
            return (Criteria) this;
        }

        public Criteria andAuctUidLessThanOrEqualTo(Long value) {
            addCriterion("auct_uid <=", value, "auctUid");
            return (Criteria) this;
        }

        public Criteria andAuctUidIn(List<Long> values) {
            addCriterion("auct_uid in", values, "auctUid");
            return (Criteria) this;
        }

        public Criteria andAuctUidNotIn(List<Long> values) {
            addCriterion("auct_uid not in", values, "auctUid");
            return (Criteria) this;
        }

        public Criteria andAuctUidBetween(Long value1, Long value2) {
            addCriterion("auct_uid between", value1, value2, "auctUid");
            return (Criteria) this;
        }

        public Criteria andAuctUidNotBetween(Long value1, Long value2) {
            addCriterion("auct_uid not between", value1, value2, "auctUid");
            return (Criteria) this;
        }

        public Criteria andAuctMoneyIsNull() {
            addCriterion("auct_money is null");
            return (Criteria) this;
        }

        public Criteria andAuctMoneyIsNotNull() {
            addCriterion("auct_money is not null");
            return (Criteria) this;
        }

        public Criteria andAuctMoneyEqualTo(Long value) {
            addCriterion("auct_money =", value, "auctMoney");
            return (Criteria) this;
        }

        public Criteria andAuctMoneyNotEqualTo(Long value) {
            addCriterion("auct_money <>", value, "auctMoney");
            return (Criteria) this;
        }

        public Criteria andAuctMoneyGreaterThan(Long value) {
            addCriterion("auct_money >", value, "auctMoney");
            return (Criteria) this;
        }

        public Criteria andAuctMoneyGreaterThanOrEqualTo(Long value) {
            addCriterion("auct_money >=", value, "auctMoney");
            return (Criteria) this;
        }

        public Criteria andAuctMoneyLessThan(Long value) {
            addCriterion("auct_money <", value, "auctMoney");
            return (Criteria) this;
        }

        public Criteria andAuctMoneyLessThanOrEqualTo(Long value) {
            addCriterion("auct_money <=", value, "auctMoney");
            return (Criteria) this;
        }

        public Criteria andAuctMoneyIn(List<Long> values) {
            addCriterion("auct_money in", values, "auctMoney");
            return (Criteria) this;
        }

        public Criteria andAuctMoneyNotIn(List<Long> values) {
            addCriterion("auct_money not in", values, "auctMoney");
            return (Criteria) this;
        }

        public Criteria andAuctMoneyBetween(Long value1, Long value2) {
            addCriterion("auct_money between", value1, value2, "auctMoney");
            return (Criteria) this;
        }

        public Criteria andAuctMoneyNotBetween(Long value1, Long value2) {
            addCriterion("auct_money not between", value1, value2, "auctMoney");
            return (Criteria) this;
        }

        public Criteria andServDuraIsNull() {
            addCriterion("serv_dura is null");
            return (Criteria) this;
        }

        public Criteria andServDuraIsNotNull() {
            addCriterion("serv_dura is not null");
            return (Criteria) this;
        }

        public Criteria andServDuraEqualTo(Integer value) {
            addCriterion("serv_dura =", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraNotEqualTo(Integer value) {
            addCriterion("serv_dura <>", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraGreaterThan(Integer value) {
            addCriterion("serv_dura >", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraGreaterThanOrEqualTo(Integer value) {
            addCriterion("serv_dura >=", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraLessThan(Integer value) {
            addCriterion("serv_dura <", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraLessThanOrEqualTo(Integer value) {
            addCriterion("serv_dura <=", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraIn(List<Integer> values) {
            addCriterion("serv_dura in", values, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraNotIn(List<Integer> values) {
            addCriterion("serv_dura not in", values, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraBetween(Integer value1, Integer value2) {
            addCriterion("serv_dura between", value1, value2, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraNotBetween(Integer value1, Integer value2) {
            addCriterion("serv_dura not between", value1, value2, "servDura");
            return (Criteria) this;
        }

        public Criteria andMinRaiseMoneyIsNull() {
            addCriterion("min_raise_money is null");
            return (Criteria) this;
        }

        public Criteria andMinRaiseMoneyIsNotNull() {
            addCriterion("min_raise_money is not null");
            return (Criteria) this;
        }

        public Criteria andMinRaiseMoneyEqualTo(Long value) {
            addCriterion("min_raise_money =", value, "minRaiseMoney");
            return (Criteria) this;
        }

        public Criteria andMinRaiseMoneyNotEqualTo(Long value) {
            addCriterion("min_raise_money <>", value, "minRaiseMoney");
            return (Criteria) this;
        }

        public Criteria andMinRaiseMoneyGreaterThan(Long value) {
            addCriterion("min_raise_money >", value, "minRaiseMoney");
            return (Criteria) this;
        }

        public Criteria andMinRaiseMoneyGreaterThanOrEqualTo(Long value) {
            addCriterion("min_raise_money >=", value, "minRaiseMoney");
            return (Criteria) this;
        }

        public Criteria andMinRaiseMoneyLessThan(Long value) {
            addCriterion("min_raise_money <", value, "minRaiseMoney");
            return (Criteria) this;
        }

        public Criteria andMinRaiseMoneyLessThanOrEqualTo(Long value) {
            addCriterion("min_raise_money <=", value, "minRaiseMoney");
            return (Criteria) this;
        }

        public Criteria andMinRaiseMoneyIn(List<Long> values) {
            addCriterion("min_raise_money in", values, "minRaiseMoney");
            return (Criteria) this;
        }

        public Criteria andMinRaiseMoneyNotIn(List<Long> values) {
            addCriterion("min_raise_money not in", values, "minRaiseMoney");
            return (Criteria) this;
        }

        public Criteria andMinRaiseMoneyBetween(Long value1, Long value2) {
            addCriterion("min_raise_money between", value1, value2, "minRaiseMoney");
            return (Criteria) this;
        }

        public Criteria andMinRaiseMoneyNotBetween(Long value1, Long value2) {
            addCriterion("min_raise_money not between", value1, value2, "minRaiseMoney");
            return (Criteria) this;
        }

        public Criteria andDealMoneyIsNull() {
            addCriterion("deal_money is null");
            return (Criteria) this;
        }

        public Criteria andDealMoneyIsNotNull() {
            addCriterion("deal_money is not null");
            return (Criteria) this;
        }

        public Criteria andDealMoneyEqualTo(Long value) {
            addCriterion("deal_money =", value, "dealMoney");
            return (Criteria) this;
        }

        public Criteria andDealMoneyNotEqualTo(Long value) {
            addCriterion("deal_money <>", value, "dealMoney");
            return (Criteria) this;
        }

        public Criteria andDealMoneyGreaterThan(Long value) {
            addCriterion("deal_money >", value, "dealMoney");
            return (Criteria) this;
        }

        public Criteria andDealMoneyGreaterThanOrEqualTo(Long value) {
            addCriterion("deal_money >=", value, "dealMoney");
            return (Criteria) this;
        }

        public Criteria andDealMoneyLessThan(Long value) {
            addCriterion("deal_money <", value, "dealMoney");
            return (Criteria) this;
        }

        public Criteria andDealMoneyLessThanOrEqualTo(Long value) {
            addCriterion("deal_money <=", value, "dealMoney");
            return (Criteria) this;
        }

        public Criteria andDealMoneyIn(List<Long> values) {
            addCriterion("deal_money in", values, "dealMoney");
            return (Criteria) this;
        }

        public Criteria andDealMoneyNotIn(List<Long> values) {
            addCriterion("deal_money not in", values, "dealMoney");
            return (Criteria) this;
        }

        public Criteria andDealMoneyBetween(Long value1, Long value2) {
            addCriterion("deal_money between", value1, value2, "dealMoney");
            return (Criteria) this;
        }

        public Criteria andDealMoneyNotBetween(Long value1, Long value2) {
            addCriterion("deal_money not between", value1, value2, "dealMoney");
            return (Criteria) this;
        }

        public Criteria andDealUidIsNull() {
            addCriterion("deal_uid is null");
            return (Criteria) this;
        }

        public Criteria andDealUidIsNotNull() {
            addCriterion("deal_uid is not null");
            return (Criteria) this;
        }

        public Criteria andDealUidEqualTo(Long value) {
            addCriterion("deal_uid =", value, "dealUid");
            return (Criteria) this;
        }

        public Criteria andDealUidNotEqualTo(Long value) {
            addCriterion("deal_uid <>", value, "dealUid");
            return (Criteria) this;
        }

        public Criteria andDealUidGreaterThan(Long value) {
            addCriterion("deal_uid >", value, "dealUid");
            return (Criteria) this;
        }

        public Criteria andDealUidGreaterThanOrEqualTo(Long value) {
            addCriterion("deal_uid >=", value, "dealUid");
            return (Criteria) this;
        }

        public Criteria andDealUidLessThan(Long value) {
            addCriterion("deal_uid <", value, "dealUid");
            return (Criteria) this;
        }

        public Criteria andDealUidLessThanOrEqualTo(Long value) {
            addCriterion("deal_uid <=", value, "dealUid");
            return (Criteria) this;
        }

        public Criteria andDealUidIn(List<Long> values) {
            addCriterion("deal_uid in", values, "dealUid");
            return (Criteria) this;
        }

        public Criteria andDealUidNotIn(List<Long> values) {
            addCriterion("deal_uid not in", values, "dealUid");
            return (Criteria) this;
        }

        public Criteria andDealUidBetween(Long value1, Long value2) {
            addCriterion("deal_uid between", value1, value2, "dealUid");
            return (Criteria) this;
        }

        public Criteria andDealUidNotBetween(Long value1, Long value2) {
            addCriterion("deal_uid not between", value1, value2, "dealUid");
            return (Criteria) this;
        }

        public Criteria andAuctDescIsNull() {
            addCriterion("auct_desc is null");
            return (Criteria) this;
        }

        public Criteria andAuctDescIsNotNull() {
            addCriterion("auct_desc is not null");
            return (Criteria) this;
        }

        public Criteria andAuctDescEqualTo(String value) {
            addCriterion("auct_desc =", value, "auctDesc");
            return (Criteria) this;
        }

        public Criteria andAuctDescNotEqualTo(String value) {
            addCriterion("auct_desc <>", value, "auctDesc");
            return (Criteria) this;
        }

        public Criteria andAuctDescGreaterThan(String value) {
            addCriterion("auct_desc >", value, "auctDesc");
            return (Criteria) this;
        }

        public Criteria andAuctDescGreaterThanOrEqualTo(String value) {
            addCriterion("auct_desc >=", value, "auctDesc");
            return (Criteria) this;
        }

        public Criteria andAuctDescLessThan(String value) {
            addCriterion("auct_desc <", value, "auctDesc");
            return (Criteria) this;
        }

        public Criteria andAuctDescLessThanOrEqualTo(String value) {
            addCriterion("auct_desc <=", value, "auctDesc");
            return (Criteria) this;
        }

        public Criteria andAuctDescLike(String value) {
            addCriterion("auct_desc like", value, "auctDesc");
            return (Criteria) this;
        }

        public Criteria andAuctDescNotLike(String value) {
            addCriterion("auct_desc not like", value, "auctDesc");
            return (Criteria) this;
        }

        public Criteria andAuctDescIn(List<String> values) {
            addCriterion("auct_desc in", values, "auctDesc");
            return (Criteria) this;
        }

        public Criteria andAuctDescNotIn(List<String> values) {
            addCriterion("auct_desc not in", values, "auctDesc");
            return (Criteria) this;
        }

        public Criteria andAuctDescBetween(String value1, String value2) {
            addCriterion("auct_desc between", value1, value2, "auctDesc");
            return (Criteria) this;
        }

        public Criteria andAuctDescNotBetween(String value1, String value2) {
            addCriterion("auct_desc not between", value1, value2, "auctDesc");
            return (Criteria) this;
        }

        public Criteria andRoomIdIsNull() {
            addCriterion("room_id is null");
            return (Criteria) this;
        }

        public Criteria andRoomIdIsNotNull() {
            addCriterion("room_id is not null");
            return (Criteria) this;
        }

        public Criteria andRoomIdEqualTo(Long value) {
            addCriterion("room_id =", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdNotEqualTo(Long value) {
            addCriterion("room_id <>", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdGreaterThan(Long value) {
            addCriterion("room_id >", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdGreaterThanOrEqualTo(Long value) {
            addCriterion("room_id >=", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdLessThan(Long value) {
            addCriterion("room_id <", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdLessThanOrEqualTo(Long value) {
            addCriterion("room_id <=", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdIn(List<Long> values) {
            addCriterion("room_id in", values, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdNotIn(List<Long> values) {
            addCriterion("room_id not in", values, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdBetween(Long value1, Long value2) {
            addCriterion("room_id between", value1, value2, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdNotBetween(Long value1, Long value2) {
            addCriterion("room_id not between", value1, value2, "roomId");
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

        public Criteria andDealTimeIsNull() {
            addCriterion("deal_time is null");
            return (Criteria) this;
        }

        public Criteria andDealTimeIsNotNull() {
            addCriterion("deal_time is not null");
            return (Criteria) this;
        }

        public Criteria andDealTimeEqualTo(Date value) {
            addCriterion("deal_time =", value, "dealTime");
            return (Criteria) this;
        }

        public Criteria andDealTimeNotEqualTo(Date value) {
            addCriterion("deal_time <>", value, "dealTime");
            return (Criteria) this;
        }

        public Criteria andDealTimeGreaterThan(Date value) {
            addCriterion("deal_time >", value, "dealTime");
            return (Criteria) this;
        }

        public Criteria andDealTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("deal_time >=", value, "dealTime");
            return (Criteria) this;
        }

        public Criteria andDealTimeLessThan(Date value) {
            addCriterion("deal_time <", value, "dealTime");
            return (Criteria) this;
        }

        public Criteria andDealTimeLessThanOrEqualTo(Date value) {
            addCriterion("deal_time <=", value, "dealTime");
            return (Criteria) this;
        }

        public Criteria andDealTimeIn(List<Date> values) {
            addCriterion("deal_time in", values, "dealTime");
            return (Criteria) this;
        }

        public Criteria andDealTimeNotIn(List<Date> values) {
            addCriterion("deal_time not in", values, "dealTime");
            return (Criteria) this;
        }

        public Criteria andDealTimeBetween(Date value1, Date value2) {
            addCriterion("deal_time between", value1, value2, "dealTime");
            return (Criteria) this;
        }

        public Criteria andDealTimeNotBetween(Date value1, Date value2) {
            addCriterion("deal_time not between", value1, value2, "dealTime");
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
