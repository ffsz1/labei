package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class StatPacketActivityExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public StatPacketActivityExample() {
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

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
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

        public Criteria andShareCountIsNull() {
            addCriterion("share_count is null");
            return (Criteria) this;
        }

        public Criteria andShareCountIsNotNull() {
            addCriterion("share_count is not null");
            return (Criteria) this;
        }

        public Criteria andShareCountEqualTo(Integer value) {
            addCriterion("share_count =", value, "shareCount");
            return (Criteria) this;
        }

        public Criteria andShareCountNotEqualTo(Integer value) {
            addCriterion("share_count <>", value, "shareCount");
            return (Criteria) this;
        }

        public Criteria andShareCountGreaterThan(Integer value) {
            addCriterion("share_count >", value, "shareCount");
            return (Criteria) this;
        }

        public Criteria andShareCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("share_count >=", value, "shareCount");
            return (Criteria) this;
        }

        public Criteria andShareCountLessThan(Integer value) {
            addCriterion("share_count <", value, "shareCount");
            return (Criteria) this;
        }

        public Criteria andShareCountLessThanOrEqualTo(Integer value) {
            addCriterion("share_count <=", value, "shareCount");
            return (Criteria) this;
        }

        public Criteria andShareCountIn(List<Integer> values) {
            addCriterion("share_count in", values, "shareCount");
            return (Criteria) this;
        }

        public Criteria andShareCountNotIn(List<Integer> values) {
            addCriterion("share_count not in", values, "shareCount");
            return (Criteria) this;
        }

        public Criteria andShareCountBetween(Integer value1, Integer value2) {
            addCriterion("share_count between", value1, value2, "shareCount");
            return (Criteria) this;
        }

        public Criteria andShareCountNotBetween(Integer value1, Integer value2) {
            addCriterion("share_count not between", value1, value2, "shareCount");
            return (Criteria) this;
        }

        public Criteria andSharePacketCountIsNull() {
            addCriterion("share_packet_count is null");
            return (Criteria) this;
        }

        public Criteria andSharePacketCountIsNotNull() {
            addCriterion("share_packet_count is not null");
            return (Criteria) this;
        }

        public Criteria andSharePacketCountEqualTo(Integer value) {
            addCriterion("share_packet_count =", value, "sharePacketCount");
            return (Criteria) this;
        }

        public Criteria andSharePacketCountNotEqualTo(Integer value) {
            addCriterion("share_packet_count <>", value, "sharePacketCount");
            return (Criteria) this;
        }

        public Criteria andSharePacketCountGreaterThan(Integer value) {
            addCriterion("share_packet_count >", value, "sharePacketCount");
            return (Criteria) this;
        }

        public Criteria andSharePacketCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("share_packet_count >=", value, "sharePacketCount");
            return (Criteria) this;
        }

        public Criteria andSharePacketCountLessThan(Integer value) {
            addCriterion("share_packet_count <", value, "sharePacketCount");
            return (Criteria) this;
        }

        public Criteria andSharePacketCountLessThanOrEqualTo(Integer value) {
            addCriterion("share_packet_count <=", value, "sharePacketCount");
            return (Criteria) this;
        }

        public Criteria andSharePacketCountIn(List<Integer> values) {
            addCriterion("share_packet_count in", values, "sharePacketCount");
            return (Criteria) this;
        }

        public Criteria andSharePacketCountNotIn(List<Integer> values) {
            addCriterion("share_packet_count not in", values, "sharePacketCount");
            return (Criteria) this;
        }

        public Criteria andSharePacketCountBetween(Integer value1, Integer value2) {
            addCriterion("share_packet_count between", value1, value2, "sharePacketCount");
            return (Criteria) this;
        }

        public Criteria andSharePacketCountNotBetween(Integer value1, Integer value2) {
            addCriterion("share_packet_count not between", value1, value2, "sharePacketCount");
            return (Criteria) this;
        }

        public Criteria andLatestShareDateIsNull() {
            addCriterion("latest_share_date is null");
            return (Criteria) this;
        }

        public Criteria andLatestShareDateIsNotNull() {
            addCriterion("latest_share_date is not null");
            return (Criteria) this;
        }

        public Criteria andLatestShareDateEqualTo(Date value) {
            addCriterionForJDBCDate("latest_share_date =", value, "latestShareDate");
            return (Criteria) this;
        }

        public Criteria andLatestShareDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("latest_share_date <>", value, "latestShareDate");
            return (Criteria) this;
        }

        public Criteria andLatestShareDateGreaterThan(Date value) {
            addCriterionForJDBCDate("latest_share_date >", value, "latestShareDate");
            return (Criteria) this;
        }

        public Criteria andLatestShareDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("latest_share_date >=", value, "latestShareDate");
            return (Criteria) this;
        }

        public Criteria andLatestShareDateLessThan(Date value) {
            addCriterionForJDBCDate("latest_share_date <", value, "latestShareDate");
            return (Criteria) this;
        }

        public Criteria andLatestShareDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("latest_share_date <=", value, "latestShareDate");
            return (Criteria) this;
        }

        public Criteria andLatestShareDateIn(List<Date> values) {
            addCriterionForJDBCDate("latest_share_date in", values, "latestShareDate");
            return (Criteria) this;
        }

        public Criteria andLatestShareDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("latest_share_date not in", values, "latestShareDate");
            return (Criteria) this;
        }

        public Criteria andLatestShareDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("latest_share_date between", value1, value2, "latestShareDate");
            return (Criteria) this;
        }

        public Criteria andLatestShareDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("latest_share_date not between", value1, value2, "latestShareDate");
            return (Criteria) this;
        }

        public Criteria andPacketCountIsNull() {
            addCriterion("packet_count is null");
            return (Criteria) this;
        }

        public Criteria andPacketCountIsNotNull() {
            addCriterion("packet_count is not null");
            return (Criteria) this;
        }

        public Criteria andPacketCountEqualTo(Integer value) {
            addCriterion("packet_count =", value, "packetCount");
            return (Criteria) this;
        }

        public Criteria andPacketCountNotEqualTo(Integer value) {
            addCriterion("packet_count <>", value, "packetCount");
            return (Criteria) this;
        }

        public Criteria andPacketCountGreaterThan(Integer value) {
            addCriterion("packet_count >", value, "packetCount");
            return (Criteria) this;
        }

        public Criteria andPacketCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("packet_count >=", value, "packetCount");
            return (Criteria) this;
        }

        public Criteria andPacketCountLessThan(Integer value) {
            addCriterion("packet_count <", value, "packetCount");
            return (Criteria) this;
        }

        public Criteria andPacketCountLessThanOrEqualTo(Integer value) {
            addCriterion("packet_count <=", value, "packetCount");
            return (Criteria) this;
        }

        public Criteria andPacketCountIn(List<Integer> values) {
            addCriterion("packet_count in", values, "packetCount");
            return (Criteria) this;
        }

        public Criteria andPacketCountNotIn(List<Integer> values) {
            addCriterion("packet_count not in", values, "packetCount");
            return (Criteria) this;
        }

        public Criteria andPacketCountBetween(Integer value1, Integer value2) {
            addCriterion("packet_count between", value1, value2, "packetCount");
            return (Criteria) this;
        }

        public Criteria andPacketCountNotBetween(Integer value1, Integer value2) {
            addCriterion("packet_count not between", value1, value2, "packetCount");
            return (Criteria) this;
        }

        public Criteria andRegisterCoutIsNull() {
            addCriterion("register_cout is null");
            return (Criteria) this;
        }

        public Criteria andRegisterCoutIsNotNull() {
            addCriterion("register_cout is not null");
            return (Criteria) this;
        }

        public Criteria andRegisterCoutEqualTo(Integer value) {
            addCriterion("register_cout =", value, "registerCout");
            return (Criteria) this;
        }

        public Criteria andRegisterCoutNotEqualTo(Integer value) {
            addCriterion("register_cout <>", value, "registerCout");
            return (Criteria) this;
        }

        public Criteria andRegisterCoutGreaterThan(Integer value) {
            addCriterion("register_cout >", value, "registerCout");
            return (Criteria) this;
        }

        public Criteria andRegisterCoutGreaterThanOrEqualTo(Integer value) {
            addCriterion("register_cout >=", value, "registerCout");
            return (Criteria) this;
        }

        public Criteria andRegisterCoutLessThan(Integer value) {
            addCriterion("register_cout <", value, "registerCout");
            return (Criteria) this;
        }

        public Criteria andRegisterCoutLessThanOrEqualTo(Integer value) {
            addCriterion("register_cout <=", value, "registerCout");
            return (Criteria) this;
        }

        public Criteria andRegisterCoutIn(List<Integer> values) {
            addCriterion("register_cout in", values, "registerCout");
            return (Criteria) this;
        }

        public Criteria andRegisterCoutNotIn(List<Integer> values) {
            addCriterion("register_cout not in", values, "registerCout");
            return (Criteria) this;
        }

        public Criteria andRegisterCoutBetween(Integer value1, Integer value2) {
            addCriterion("register_cout between", value1, value2, "registerCout");
            return (Criteria) this;
        }

        public Criteria andRegisterCoutNotBetween(Integer value1, Integer value2) {
            addCriterion("register_cout not between", value1, value2, "registerCout");
            return (Criteria) this;
        }

        public Criteria andTodayRegisterCountIsNull() {
            addCriterion("today_register_count is null");
            return (Criteria) this;
        }

        public Criteria andTodayRegisterCountIsNotNull() {
            addCriterion("today_register_count is not null");
            return (Criteria) this;
        }

        public Criteria andTodayRegisterCountEqualTo(Integer value) {
            addCriterion("today_register_count =", value, "todayRegisterCount");
            return (Criteria) this;
        }

        public Criteria andTodayRegisterCountNotEqualTo(Integer value) {
            addCriterion("today_register_count <>", value, "todayRegisterCount");
            return (Criteria) this;
        }

        public Criteria andTodayRegisterCountGreaterThan(Integer value) {
            addCriterion("today_register_count >", value, "todayRegisterCount");
            return (Criteria) this;
        }

        public Criteria andTodayRegisterCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("today_register_count >=", value, "todayRegisterCount");
            return (Criteria) this;
        }

        public Criteria andTodayRegisterCountLessThan(Integer value) {
            addCriterion("today_register_count <", value, "todayRegisterCount");
            return (Criteria) this;
        }

        public Criteria andTodayRegisterCountLessThanOrEqualTo(Integer value) {
            addCriterion("today_register_count <=", value, "todayRegisterCount");
            return (Criteria) this;
        }

        public Criteria andTodayRegisterCountIn(List<Integer> values) {
            addCriterion("today_register_count in", values, "todayRegisterCount");
            return (Criteria) this;
        }

        public Criteria andTodayRegisterCountNotIn(List<Integer> values) {
            addCriterion("today_register_count not in", values, "todayRegisterCount");
            return (Criteria) this;
        }

        public Criteria andTodayRegisterCountBetween(Integer value1, Integer value2) {
            addCriterion("today_register_count between", value1, value2, "todayRegisterCount");
            return (Criteria) this;
        }

        public Criteria andTodayRegisterCountNotBetween(Integer value1, Integer value2) {
            addCriterion("today_register_count not between", value1, value2, "todayRegisterCount");
            return (Criteria) this;
        }

        public Criteria andLatestRegisterDateIsNull() {
            addCriterion("latest_register_date is null");
            return (Criteria) this;
        }

        public Criteria andLatestRegisterDateIsNotNull() {
            addCriterion("latest_register_date is not null");
            return (Criteria) this;
        }

        public Criteria andLatestRegisterDateEqualTo(Date value) {
            addCriterionForJDBCDate("latest_register_date =", value, "latestRegisterDate");
            return (Criteria) this;
        }

        public Criteria andLatestRegisterDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("latest_register_date <>", value, "latestRegisterDate");
            return (Criteria) this;
        }

        public Criteria andLatestRegisterDateGreaterThan(Date value) {
            addCriterionForJDBCDate("latest_register_date >", value, "latestRegisterDate");
            return (Criteria) this;
        }

        public Criteria andLatestRegisterDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("latest_register_date >=", value, "latestRegisterDate");
            return (Criteria) this;
        }

        public Criteria andLatestRegisterDateLessThan(Date value) {
            addCriterionForJDBCDate("latest_register_date <", value, "latestRegisterDate");
            return (Criteria) this;
        }

        public Criteria andLatestRegisterDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("latest_register_date <=", value, "latestRegisterDate");
            return (Criteria) this;
        }

        public Criteria andLatestRegisterDateIn(List<Date> values) {
            addCriterionForJDBCDate("latest_register_date in", values, "latestRegisterDate");
            return (Criteria) this;
        }

        public Criteria andLatestRegisterDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("latest_register_date not in", values, "latestRegisterDate");
            return (Criteria) this;
        }

        public Criteria andLatestRegisterDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("latest_register_date between", value1, value2, "latestRegisterDate");
            return (Criteria) this;
        }

        public Criteria andLatestRegisterDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("latest_register_date not between", value1, value2, "latestRegisterDate");
            return (Criteria) this;
        }

        public Criteria andChargeBonusIsNull() {
            addCriterion("charge_bonus is null");
            return (Criteria) this;
        }

        public Criteria andChargeBonusIsNotNull() {
            addCriterion("charge_bonus is not null");
            return (Criteria) this;
        }

        public Criteria andChargeBonusEqualTo(Double value) {
            addCriterion("charge_bonus =", value, "chargeBonus");
            return (Criteria) this;
        }

        public Criteria andChargeBonusNotEqualTo(Double value) {
            addCriterion("charge_bonus <>", value, "chargeBonus");
            return (Criteria) this;
        }

        public Criteria andChargeBonusGreaterThan(Double value) {
            addCriterion("charge_bonus >", value, "chargeBonus");
            return (Criteria) this;
        }

        public Criteria andChargeBonusGreaterThanOrEqualTo(Double value) {
            addCriterion("charge_bonus >=", value, "chargeBonus");
            return (Criteria) this;
        }

        public Criteria andChargeBonusLessThan(Double value) {
            addCriterion("charge_bonus <", value, "chargeBonus");
            return (Criteria) this;
        }

        public Criteria andChargeBonusLessThanOrEqualTo(Double value) {
            addCriterion("charge_bonus <=", value, "chargeBonus");
            return (Criteria) this;
        }

        public Criteria andChargeBonusIn(List<Double> values) {
            addCriterion("charge_bonus in", values, "chargeBonus");
            return (Criteria) this;
        }

        public Criteria andChargeBonusNotIn(List<Double> values) {
            addCriterion("charge_bonus not in", values, "chargeBonus");
            return (Criteria) this;
        }

        public Criteria andChargeBonusBetween(Double value1, Double value2) {
            addCriterion("charge_bonus between", value1, value2, "chargeBonus");
            return (Criteria) this;
        }

        public Criteria andChargeBonusNotBetween(Double value1, Double value2) {
            addCriterion("charge_bonus not between", value1, value2, "chargeBonus");
            return (Criteria) this;
        }

        public Criteria andTodayChargeBonusIsNull() {
            addCriterion("today_charge_bonus is null");
            return (Criteria) this;
        }

        public Criteria andTodayChargeBonusIsNotNull() {
            addCriterion("today_charge_bonus is not null");
            return (Criteria) this;
        }

        public Criteria andTodayChargeBonusEqualTo(Double value) {
            addCriterion("today_charge_bonus =", value, "todayChargeBonus");
            return (Criteria) this;
        }

        public Criteria andTodayChargeBonusNotEqualTo(Double value) {
            addCriterion("today_charge_bonus <>", value, "todayChargeBonus");
            return (Criteria) this;
        }

        public Criteria andTodayChargeBonusGreaterThan(Double value) {
            addCriterion("today_charge_bonus >", value, "todayChargeBonus");
            return (Criteria) this;
        }

        public Criteria andTodayChargeBonusGreaterThanOrEqualTo(Double value) {
            addCriterion("today_charge_bonus >=", value, "todayChargeBonus");
            return (Criteria) this;
        }

        public Criteria andTodayChargeBonusLessThan(Double value) {
            addCriterion("today_charge_bonus <", value, "todayChargeBonus");
            return (Criteria) this;
        }

        public Criteria andTodayChargeBonusLessThanOrEqualTo(Double value) {
            addCriterion("today_charge_bonus <=", value, "todayChargeBonus");
            return (Criteria) this;
        }

        public Criteria andTodayChargeBonusIn(List<Double> values) {
            addCriterion("today_charge_bonus in", values, "todayChargeBonus");
            return (Criteria) this;
        }

        public Criteria andTodayChargeBonusNotIn(List<Double> values) {
            addCriterion("today_charge_bonus not in", values, "todayChargeBonus");
            return (Criteria) this;
        }

        public Criteria andTodayChargeBonusBetween(Double value1, Double value2) {
            addCriterion("today_charge_bonus between", value1, value2, "todayChargeBonus");
            return (Criteria) this;
        }

        public Criteria andTodayChargeBonusNotBetween(Double value1, Double value2) {
            addCriterion("today_charge_bonus not between", value1, value2, "todayChargeBonus");
            return (Criteria) this;
        }

        public Criteria andLatestChargeBonusDateIsNull() {
            addCriterion("latest_charge_bonus_date is null");
            return (Criteria) this;
        }

        public Criteria andLatestChargeBonusDateIsNotNull() {
            addCriterion("latest_charge_bonus_date is not null");
            return (Criteria) this;
        }

        public Criteria andLatestChargeBonusDateEqualTo(Date value) {
            addCriterionForJDBCDate("latest_charge_bonus_date =", value, "latestChargeBonusDate");
            return (Criteria) this;
        }

        public Criteria andLatestChargeBonusDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("latest_charge_bonus_date <>", value, "latestChargeBonusDate");
            return (Criteria) this;
        }

        public Criteria andLatestChargeBonusDateGreaterThan(Date value) {
            addCriterionForJDBCDate("latest_charge_bonus_date >", value, "latestChargeBonusDate");
            return (Criteria) this;
        }

        public Criteria andLatestChargeBonusDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("latest_charge_bonus_date >=", value, "latestChargeBonusDate");
            return (Criteria) this;
        }

        public Criteria andLatestChargeBonusDateLessThan(Date value) {
            addCriterionForJDBCDate("latest_charge_bonus_date <", value, "latestChargeBonusDate");
            return (Criteria) this;
        }

        public Criteria andLatestChargeBonusDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("latest_charge_bonus_date <=", value, "latestChargeBonusDate");
            return (Criteria) this;
        }

        public Criteria andLatestChargeBonusDateIn(List<Date> values) {
            addCriterionForJDBCDate("latest_charge_bonus_date in", values, "latestChargeBonusDate");
            return (Criteria) this;
        }

        public Criteria andLatestChargeBonusDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("latest_charge_bonus_date not in", values, "latestChargeBonusDate");
            return (Criteria) this;
        }

        public Criteria andLatestChargeBonusDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("latest_charge_bonus_date between", value1, value2, "latestChargeBonusDate");
            return (Criteria) this;
        }

        public Criteria andLatestChargeBonusDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("latest_charge_bonus_date not between", value1, value2, "latestChargeBonusDate");
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

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
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
