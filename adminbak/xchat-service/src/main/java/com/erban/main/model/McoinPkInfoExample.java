package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class McoinPkInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public McoinPkInfoExample() {
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

        public Criteria andTermIsNull() {
            addCriterion("term is null");
            return (Criteria) this;
        }

        public Criteria andTermIsNotNull() {
            addCriterion("term is not null");
            return (Criteria) this;
        }

        public Criteria andTermEqualTo(Integer value) {
            addCriterion("term =", value, "term");
            return (Criteria) this;
        }

        public Criteria andTermNotEqualTo(Integer value) {
            addCriterion("term <>", value, "term");
            return (Criteria) this;
        }

        public Criteria andTermGreaterThan(Integer value) {
            addCriterion("term >", value, "term");
            return (Criteria) this;
        }

        public Criteria andTermGreaterThanOrEqualTo(Integer value) {
            addCriterion("term >=", value, "term");
            return (Criteria) this;
        }

        public Criteria andTermLessThan(Integer value) {
            addCriterion("term <", value, "term");
            return (Criteria) this;
        }

        public Criteria andTermLessThanOrEqualTo(Integer value) {
            addCriterion("term <=", value, "term");
            return (Criteria) this;
        }

        public Criteria andTermIn(List<Integer> values) {
            addCriterion("term in", values, "term");
            return (Criteria) this;
        }

        public Criteria andTermNotIn(List<Integer> values) {
            addCriterion("term not in", values, "term");
            return (Criteria) this;
        }

        public Criteria andTermBetween(Integer value1, Integer value2) {
            addCriterion("term between", value1, value2, "term");
            return (Criteria) this;
        }

        public Criteria andTermNotBetween(Integer value1, Integer value2) {
            addCriterion("term not between", value1, value2, "term");
            return (Criteria) this;
        }

        public Criteria andTitleIsNull() {
            addCriterion("title is null");
            return (Criteria) this;
        }

        public Criteria andTitleIsNotNull() {
            addCriterion("title is not null");
            return (Criteria) this;
        }

        public Criteria andTitleEqualTo(String value) {
            addCriterion("title =", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotEqualTo(String value) {
            addCriterion("title <>", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThan(String value) {
            addCriterion("title >", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThanOrEqualTo(String value) {
            addCriterion("title >=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThan(String value) {
            addCriterion("title <", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThanOrEqualTo(String value) {
            addCriterion("title <=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLike(String value) {
            addCriterion("title like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotLike(String value) {
            addCriterion("title not like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleIn(List<String> values) {
            addCriterion("title in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotIn(List<String> values) {
            addCriterion("title not in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleBetween(String value1, String value2) {
            addCriterion("title between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotBetween(String value1, String value2) {
            addCriterion("title not between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andRedAnswerIsNull() {
            addCriterion("red_answer is null");
            return (Criteria) this;
        }

        public Criteria andRedAnswerIsNotNull() {
            addCriterion("red_answer is not null");
            return (Criteria) this;
        }

        public Criteria andRedAnswerEqualTo(String value) {
            addCriterion("red_answer =", value, "redAnswer");
            return (Criteria) this;
        }

        public Criteria andRedAnswerNotEqualTo(String value) {
            addCriterion("red_answer <>", value, "redAnswer");
            return (Criteria) this;
        }

        public Criteria andRedAnswerGreaterThan(String value) {
            addCriterion("red_answer >", value, "redAnswer");
            return (Criteria) this;
        }

        public Criteria andRedAnswerGreaterThanOrEqualTo(String value) {
            addCriterion("red_answer >=", value, "redAnswer");
            return (Criteria) this;
        }

        public Criteria andRedAnswerLessThan(String value) {
            addCriterion("red_answer <", value, "redAnswer");
            return (Criteria) this;
        }

        public Criteria andRedAnswerLessThanOrEqualTo(String value) {
            addCriterion("red_answer <=", value, "redAnswer");
            return (Criteria) this;
        }

        public Criteria andRedAnswerLike(String value) {
            addCriterion("red_answer like", value, "redAnswer");
            return (Criteria) this;
        }

        public Criteria andRedAnswerNotLike(String value) {
            addCriterion("red_answer not like", value, "redAnswer");
            return (Criteria) this;
        }

        public Criteria andRedAnswerIn(List<String> values) {
            addCriterion("red_answer in", values, "redAnswer");
            return (Criteria) this;
        }

        public Criteria andRedAnswerNotIn(List<String> values) {
            addCriterion("red_answer not in", values, "redAnswer");
            return (Criteria) this;
        }

        public Criteria andRedAnswerBetween(String value1, String value2) {
            addCriterion("red_answer between", value1, value2, "redAnswer");
            return (Criteria) this;
        }

        public Criteria andRedAnswerNotBetween(String value1, String value2) {
            addCriterion("red_answer not between", value1, value2, "redAnswer");
            return (Criteria) this;
        }

        public Criteria andBlueAnswerIsNull() {
            addCriterion("blue_answer is null");
            return (Criteria) this;
        }

        public Criteria andBlueAnswerIsNotNull() {
            addCriterion("blue_answer is not null");
            return (Criteria) this;
        }

        public Criteria andBlueAnswerEqualTo(String value) {
            addCriterion("blue_answer =", value, "blueAnswer");
            return (Criteria) this;
        }

        public Criteria andBlueAnswerNotEqualTo(String value) {
            addCriterion("blue_answer <>", value, "blueAnswer");
            return (Criteria) this;
        }

        public Criteria andBlueAnswerGreaterThan(String value) {
            addCriterion("blue_answer >", value, "blueAnswer");
            return (Criteria) this;
        }

        public Criteria andBlueAnswerGreaterThanOrEqualTo(String value) {
            addCriterion("blue_answer >=", value, "blueAnswer");
            return (Criteria) this;
        }

        public Criteria andBlueAnswerLessThan(String value) {
            addCriterion("blue_answer <", value, "blueAnswer");
            return (Criteria) this;
        }

        public Criteria andBlueAnswerLessThanOrEqualTo(String value) {
            addCriterion("blue_answer <=", value, "blueAnswer");
            return (Criteria) this;
        }

        public Criteria andBlueAnswerLike(String value) {
            addCriterion("blue_answer like", value, "blueAnswer");
            return (Criteria) this;
        }

        public Criteria andBlueAnswerNotLike(String value) {
            addCriterion("blue_answer not like", value, "blueAnswer");
            return (Criteria) this;
        }

        public Criteria andBlueAnswerIn(List<String> values) {
            addCriterion("blue_answer in", values, "blueAnswer");
            return (Criteria) this;
        }

        public Criteria andBlueAnswerNotIn(List<String> values) {
            addCriterion("blue_answer not in", values, "blueAnswer");
            return (Criteria) this;
        }

        public Criteria andBlueAnswerBetween(String value1, String value2) {
            addCriterion("blue_answer between", value1, value2, "blueAnswer");
            return (Criteria) this;
        }

        public Criteria andBlueAnswerNotBetween(String value1, String value2) {
            addCriterion("blue_answer not between", value1, value2, "blueAnswer");
            return (Criteria) this;
        }

        public Criteria andRedPicIsNull() {
            addCriterion("red_pic is null");
            return (Criteria) this;
        }

        public Criteria andRedPicIsNotNull() {
            addCriterion("red_pic is not null");
            return (Criteria) this;
        }

        public Criteria andRedPicEqualTo(String value) {
            addCriterion("red_pic =", value, "redPic");
            return (Criteria) this;
        }

        public Criteria andRedPicNotEqualTo(String value) {
            addCriterion("red_pic <>", value, "redPic");
            return (Criteria) this;
        }

        public Criteria andRedPicGreaterThan(String value) {
            addCriterion("red_pic >", value, "redPic");
            return (Criteria) this;
        }

        public Criteria andRedPicGreaterThanOrEqualTo(String value) {
            addCriterion("red_pic >=", value, "redPic");
            return (Criteria) this;
        }

        public Criteria andRedPicLessThan(String value) {
            addCriterion("red_pic <", value, "redPic");
            return (Criteria) this;
        }

        public Criteria andRedPicLessThanOrEqualTo(String value) {
            addCriterion("red_pic <=", value, "redPic");
            return (Criteria) this;
        }

        public Criteria andRedPicLike(String value) {
            addCriterion("red_pic like", value, "redPic");
            return (Criteria) this;
        }

        public Criteria andRedPicNotLike(String value) {
            addCriterion("red_pic not like", value, "redPic");
            return (Criteria) this;
        }

        public Criteria andRedPicIn(List<String> values) {
            addCriterion("red_pic in", values, "redPic");
            return (Criteria) this;
        }

        public Criteria andRedPicNotIn(List<String> values) {
            addCriterion("red_pic not in", values, "redPic");
            return (Criteria) this;
        }

        public Criteria andRedPicBetween(String value1, String value2) {
            addCriterion("red_pic between", value1, value2, "redPic");
            return (Criteria) this;
        }

        public Criteria andRedPicNotBetween(String value1, String value2) {
            addCriterion("red_pic not between", value1, value2, "redPic");
            return (Criteria) this;
        }

        public Criteria andBluePicIsNull() {
            addCriterion("blue_pic is null");
            return (Criteria) this;
        }

        public Criteria andBluePicIsNotNull() {
            addCriterion("blue_pic is not null");
            return (Criteria) this;
        }

        public Criteria andBluePicEqualTo(String value) {
            addCriterion("blue_pic =", value, "bluePic");
            return (Criteria) this;
        }

        public Criteria andBluePicNotEqualTo(String value) {
            addCriterion("blue_pic <>", value, "bluePic");
            return (Criteria) this;
        }

        public Criteria andBluePicGreaterThan(String value) {
            addCriterion("blue_pic >", value, "bluePic");
            return (Criteria) this;
        }

        public Criteria andBluePicGreaterThanOrEqualTo(String value) {
            addCriterion("blue_pic >=", value, "bluePic");
            return (Criteria) this;
        }

        public Criteria andBluePicLessThan(String value) {
            addCriterion("blue_pic <", value, "bluePic");
            return (Criteria) this;
        }

        public Criteria andBluePicLessThanOrEqualTo(String value) {
            addCriterion("blue_pic <=", value, "bluePic");
            return (Criteria) this;
        }

        public Criteria andBluePicLike(String value) {
            addCriterion("blue_pic like", value, "bluePic");
            return (Criteria) this;
        }

        public Criteria andBluePicNotLike(String value) {
            addCriterion("blue_pic not like", value, "bluePic");
            return (Criteria) this;
        }

        public Criteria andBluePicIn(List<String> values) {
            addCriterion("blue_pic in", values, "bluePic");
            return (Criteria) this;
        }

        public Criteria andBluePicNotIn(List<String> values) {
            addCriterion("blue_pic not in", values, "bluePic");
            return (Criteria) this;
        }

        public Criteria andBluePicBetween(String value1, String value2) {
            addCriterion("blue_pic between", value1, value2, "bluePic");
            return (Criteria) this;
        }

        public Criteria andBluePicNotBetween(String value1, String value2) {
            addCriterion("blue_pic not between", value1, value2, "bluePic");
            return (Criteria) this;
        }

        public Criteria andRedPollsIsNull() {
            addCriterion("red_polls is null");
            return (Criteria) this;
        }

        public Criteria andRedPollsIsNotNull() {
            addCriterion("red_polls is not null");
            return (Criteria) this;
        }

        public Criteria andRedPollsEqualTo(Integer value) {
            addCriterion("red_polls =", value, "redPolls");
            return (Criteria) this;
        }

        public Criteria andRedPollsNotEqualTo(Integer value) {
            addCriterion("red_polls <>", value, "redPolls");
            return (Criteria) this;
        }

        public Criteria andRedPollsGreaterThan(Integer value) {
            addCriterion("red_polls >", value, "redPolls");
            return (Criteria) this;
        }

        public Criteria andRedPollsGreaterThanOrEqualTo(Integer value) {
            addCriterion("red_polls >=", value, "redPolls");
            return (Criteria) this;
        }

        public Criteria andRedPollsLessThan(Integer value) {
            addCriterion("red_polls <", value, "redPolls");
            return (Criteria) this;
        }

        public Criteria andRedPollsLessThanOrEqualTo(Integer value) {
            addCriterion("red_polls <=", value, "redPolls");
            return (Criteria) this;
        }

        public Criteria andRedPollsIn(List<Integer> values) {
            addCriterion("red_polls in", values, "redPolls");
            return (Criteria) this;
        }

        public Criteria andRedPollsNotIn(List<Integer> values) {
            addCriterion("red_polls not in", values, "redPolls");
            return (Criteria) this;
        }

        public Criteria andRedPollsBetween(Integer value1, Integer value2) {
            addCriterion("red_polls between", value1, value2, "redPolls");
            return (Criteria) this;
        }

        public Criteria andRedPollsNotBetween(Integer value1, Integer value2) {
            addCriterion("red_polls not between", value1, value2, "redPolls");
            return (Criteria) this;
        }

        public Criteria andBluePollsIsNull() {
            addCriterion("blue_polls is null");
            return (Criteria) this;
        }

        public Criteria andBluePollsIsNotNull() {
            addCriterion("blue_polls is not null");
            return (Criteria) this;
        }

        public Criteria andBluePollsEqualTo(Integer value) {
            addCriterion("blue_polls =", value, "bluePolls");
            return (Criteria) this;
        }

        public Criteria andBluePollsNotEqualTo(Integer value) {
            addCriterion("blue_polls <>", value, "bluePolls");
            return (Criteria) this;
        }

        public Criteria andBluePollsGreaterThan(Integer value) {
            addCriterion("blue_polls >", value, "bluePolls");
            return (Criteria) this;
        }

        public Criteria andBluePollsGreaterThanOrEqualTo(Integer value) {
            addCriterion("blue_polls >=", value, "bluePolls");
            return (Criteria) this;
        }

        public Criteria andBluePollsLessThan(Integer value) {
            addCriterion("blue_polls <", value, "bluePolls");
            return (Criteria) this;
        }

        public Criteria andBluePollsLessThanOrEqualTo(Integer value) {
            addCriterion("blue_polls <=", value, "bluePolls");
            return (Criteria) this;
        }

        public Criteria andBluePollsIn(List<Integer> values) {
            addCriterion("blue_polls in", values, "bluePolls");
            return (Criteria) this;
        }

        public Criteria andBluePollsNotIn(List<Integer> values) {
            addCriterion("blue_polls not in", values, "bluePolls");
            return (Criteria) this;
        }

        public Criteria andBluePollsBetween(Integer value1, Integer value2) {
            addCriterion("blue_polls between", value1, value2, "bluePolls");
            return (Criteria) this;
        }

        public Criteria andBluePollsNotBetween(Integer value1, Integer value2) {
            addCriterion("blue_polls not between", value1, value2, "bluePolls");
            return (Criteria) this;
        }

        public Criteria andLotteryTimeIsNull() {
            addCriterion("lottery_time is null");
            return (Criteria) this;
        }

        public Criteria andLotteryTimeIsNotNull() {
            addCriterion("lottery_time is not null");
            return (Criteria) this;
        }

        public Criteria andLotteryTimeEqualTo(Date value) {
            addCriterion("lottery_time =", value, "lotteryTime");
            return (Criteria) this;
        }

        public Criteria andLotteryTimeNotEqualTo(Date value) {
            addCriterion("lottery_time <>", value, "lotteryTime");
            return (Criteria) this;
        }

        public Criteria andLotteryTimeGreaterThan(Date value) {
            addCriterion("lottery_time >", value, "lotteryTime");
            return (Criteria) this;
        }

        public Criteria andLotteryTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("lottery_time >=", value, "lotteryTime");
            return (Criteria) this;
        }

        public Criteria andLotteryTimeLessThan(Date value) {
            addCriterion("lottery_time <", value, "lotteryTime");
            return (Criteria) this;
        }

        public Criteria andLotteryTimeLessThanOrEqualTo(Date value) {
            addCriterion("lottery_time <=", value, "lotteryTime");
            return (Criteria) this;
        }

        public Criteria andLotteryTimeIn(List<Date> values) {
            addCriterion("lottery_time in", values, "lotteryTime");
            return (Criteria) this;
        }

        public Criteria andLotteryTimeNotIn(List<Date> values) {
            addCriterion("lottery_time not in", values, "lotteryTime");
            return (Criteria) this;
        }

        public Criteria andLotteryTimeBetween(Date value1, Date value2) {
            addCriterion("lottery_time between", value1, value2, "lotteryTime");
            return (Criteria) this;
        }

        public Criteria andLotteryTimeNotBetween(Date value1, Date value2) {
            addCriterion("lottery_time not between", value1, value2, "lotteryTime");
            return (Criteria) this;
        }

        public Criteria andCarveUpMcoinNumIsNull() {
            addCriterion("carve_up_mcoin_num is null");
            return (Criteria) this;
        }

        public Criteria andCarveUpMcoinNumIsNotNull() {
            addCriterion("carve_up_mcoin_num is not null");
            return (Criteria) this;
        }

        public Criteria andCarveUpMcoinNumEqualTo(Integer value) {
            addCriterion("carve_up_mcoin_num =", value, "carveUpMcoinNum");
            return (Criteria) this;
        }

        public Criteria andCarveUpMcoinNumNotEqualTo(Integer value) {
            addCriterion("carve_up_mcoin_num <>", value, "carveUpMcoinNum");
            return (Criteria) this;
        }

        public Criteria andCarveUpMcoinNumGreaterThan(Integer value) {
            addCriterion("carve_up_mcoin_num >", value, "carveUpMcoinNum");
            return (Criteria) this;
        }

        public Criteria andCarveUpMcoinNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("carve_up_mcoin_num >=", value, "carveUpMcoinNum");
            return (Criteria) this;
        }

        public Criteria andCarveUpMcoinNumLessThan(Integer value) {
            addCriterion("carve_up_mcoin_num <", value, "carveUpMcoinNum");
            return (Criteria) this;
        }

        public Criteria andCarveUpMcoinNumLessThanOrEqualTo(Integer value) {
            addCriterion("carve_up_mcoin_num <=", value, "carveUpMcoinNum");
            return (Criteria) this;
        }

        public Criteria andCarveUpMcoinNumIn(List<Integer> values) {
            addCriterion("carve_up_mcoin_num in", values, "carveUpMcoinNum");
            return (Criteria) this;
        }

        public Criteria andCarveUpMcoinNumNotIn(List<Integer> values) {
            addCriterion("carve_up_mcoin_num not in", values, "carveUpMcoinNum");
            return (Criteria) this;
        }

        public Criteria andCarveUpMcoinNumBetween(Integer value1, Integer value2) {
            addCriterion("carve_up_mcoin_num between", value1, value2, "carveUpMcoinNum");
            return (Criteria) this;
        }

        public Criteria andCarveUpMcoinNumNotBetween(Integer value1, Integer value2) {
            addCriterion("carve_up_mcoin_num not between", value1, value2, "carveUpMcoinNum");
            return (Criteria) this;
        }

        public Criteria andPkStatusIsNull() {
            addCriterion("pk_status is null");
            return (Criteria) this;
        }

        public Criteria andPkStatusIsNotNull() {
            addCriterion("pk_status is not null");
            return (Criteria) this;
        }

        public Criteria andPkStatusEqualTo(Byte value) {
            addCriterion("pk_status =", value, "pkStatus");
            return (Criteria) this;
        }

        public Criteria andPkStatusNotEqualTo(Byte value) {
            addCriterion("pk_status <>", value, "pkStatus");
            return (Criteria) this;
        }

        public Criteria andPkStatusGreaterThan(Byte value) {
            addCriterion("pk_status >", value, "pkStatus");
            return (Criteria) this;
        }

        public Criteria andPkStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("pk_status >=", value, "pkStatus");
            return (Criteria) this;
        }

        public Criteria andPkStatusLessThan(Byte value) {
            addCriterion("pk_status <", value, "pkStatus");
            return (Criteria) this;
        }

        public Criteria andPkStatusLessThanOrEqualTo(Byte value) {
            addCriterion("pk_status <=", value, "pkStatus");
            return (Criteria) this;
        }

        public Criteria andPkStatusIn(List<Byte> values) {
            addCriterion("pk_status in", values, "pkStatus");
            return (Criteria) this;
        }

        public Criteria andPkStatusNotIn(List<Byte> values) {
            addCriterion("pk_status not in", values, "pkStatus");
            return (Criteria) this;
        }

        public Criteria andPkStatusBetween(Byte value1, Byte value2) {
            addCriterion("pk_status between", value1, value2, "pkStatus");
            return (Criteria) this;
        }

        public Criteria andPkStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("pk_status not between", value1, value2, "pkStatus");
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
