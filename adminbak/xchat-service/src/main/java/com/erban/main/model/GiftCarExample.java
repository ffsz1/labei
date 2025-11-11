package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GiftCarExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected String likeName;

    public String getLikeName() {
        return likeName;
    }

    public void setLikeName(String likeName) {
        this.likeName = likeName;
    }

    public GiftCarExample() {
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

        public Criteria andCarIdIsNull() {
            addCriterion("car_id is null");
            return (Criteria) this;
        }

        public Criteria andCarIdIsNotNull() {
            addCriterion("car_id is not null");
            return (Criteria) this;
        }

        public Criteria andCarIdEqualTo(Integer value) {
            addCriterion("car_id =", value, "carId");
            return (Criteria) this;
        }

        public Criteria andCarIdNotEqualTo(Integer value) {
            addCriterion("car_id <>", value, "carId");
            return (Criteria) this;
        }

        public Criteria andCarIdGreaterThan(Integer value) {
            addCriterion("car_id >", value, "carId");
            return (Criteria) this;
        }

        public Criteria andCarIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("car_id >=", value, "carId");
            return (Criteria) this;
        }

        public Criteria andCarIdLessThan(Integer value) {
            addCriterion("car_id <", value, "carId");
            return (Criteria) this;
        }

        public Criteria andCarIdLessThanOrEqualTo(Integer value) {
            addCriterion("car_id <=", value, "carId");
            return (Criteria) this;
        }

        public Criteria andCarIdIn(List<Integer> values) {
            addCriterion("car_id in", values, "carId");
            return (Criteria) this;
        }

        public Criteria andCarIdNotIn(List<Integer> values) {
            addCriterion("car_id not in", values, "carId");
            return (Criteria) this;
        }

        public Criteria andCarIdBetween(Integer value1, Integer value2) {
            addCriterion("car_id between", value1, value2, "carId");
            return (Criteria) this;
        }

        public Criteria andCarIdNotBetween(Integer value1, Integer value2) {
            addCriterion("car_id not between", value1, value2, "carId");
            return (Criteria) this;
        }

        public Criteria andCarNameIsNull() {
            addCriterion("car_name is null");
            return (Criteria) this;
        }

        public Criteria andCarNameIsNotNull() {
            addCriterion("car_name is not null");
            return (Criteria) this;
        }

        public Criteria andCarNameEqualTo(String value) {
            addCriterion("car_name =", value, "carName");
            return (Criteria) this;
        }

        public Criteria andCarNameNotEqualTo(String value) {
            addCriterion("car_name <>", value, "carName");
            return (Criteria) this;
        }

        public Criteria andCarNameGreaterThan(String value) {
            addCriterion("car_name >", value, "carName");
            return (Criteria) this;
        }

        public Criteria andCarNameGreaterThanOrEqualTo(String value) {
            addCriterion("car_name >=", value, "carName");
            return (Criteria) this;
        }

        public Criteria andCarNameLessThan(String value) {
            addCriterion("car_name <", value, "carName");
            return (Criteria) this;
        }

        public Criteria andCarNameLessThanOrEqualTo(String value) {
            addCriterion("car_name <=", value, "carName");
            return (Criteria) this;
        }

        public Criteria andCarNameLike(String value) {
            addCriterion("car_name like", value, "carName");
            return (Criteria) this;
        }

        public Criteria andCarNameNotLike(String value) {
            addCriterion("car_name not like", value, "carName");
            return (Criteria) this;
        }

        public Criteria andCarNameIn(List<String> values) {
            addCriterion("car_name in", values, "carName");
            return (Criteria) this;
        }

        public Criteria andCarNameNotIn(List<String> values) {
            addCriterion("car_name not in", values, "carName");
            return (Criteria) this;
        }

        public Criteria andCarNameBetween(String value1, String value2) {
            addCriterion("car_name between", value1, value2, "carName");
            return (Criteria) this;
        }

        public Criteria andCarNameNotBetween(String value1, String value2) {
            addCriterion("car_name not between", value1, value2, "carName");
            return (Criteria) this;
        }

        public Criteria andGoldPriceIsNull() {
            addCriterion("gold_price is null");
            return (Criteria) this;
        }

        public Criteria andGoldPriceIsNotNull() {
            addCriterion("gold_price is not null");
            return (Criteria) this;
        }

        public Criteria andGoldPriceEqualTo(Long value) {
            addCriterion("gold_price =", value, "goldPrice");
            return (Criteria) this;
        }

        public Criteria andGoldPriceNotEqualTo(Long value) {
            addCriterion("gold_price <>", value, "goldPrice");
            return (Criteria) this;
        }

        public Criteria andGoldPriceGreaterThan(Long value) {
            addCriterion("gold_price >", value, "goldPrice");
            return (Criteria) this;
        }

        public Criteria andGoldPriceGreaterThanOrEqualTo(Long value) {
            addCriterion("gold_price >=", value, "goldPrice");
            return (Criteria) this;
        }

        public Criteria andGoldPriceLessThan(Long value) {
            addCriterion("gold_price <", value, "goldPrice");
            return (Criteria) this;
        }

        public Criteria andGoldPriceLessThanOrEqualTo(Long value) {
            addCriterion("gold_price <=", value, "goldPrice");
            return (Criteria) this;
        }

        public Criteria andGoldPriceIn(List<Long> values) {
            addCriterion("gold_price in", values, "goldPrice");
            return (Criteria) this;
        }

        public Criteria andGoldPriceNotIn(List<Long> values) {
            addCriterion("gold_price not in", values, "goldPrice");
            return (Criteria) this;
        }

        public Criteria andGoldPriceBetween(Long value1, Long value2) {
            addCriterion("gold_price between", value1, value2, "goldPrice");
            return (Criteria) this;
        }

        public Criteria andGoldPriceNotBetween(Long value1, Long value2) {
            addCriterion("gold_price not between", value1, value2, "goldPrice");
            return (Criteria) this;
        }

        public Criteria andSeqNoIsNull() {
            addCriterion("seq_no is null");
            return (Criteria) this;
        }

        public Criteria andSeqNoIsNotNull() {
            addCriterion("seq_no is not null");
            return (Criteria) this;
        }

        public Criteria andSeqNoEqualTo(Integer value) {
            addCriterion("seq_no =", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoNotEqualTo(Integer value) {
            addCriterion("seq_no <>", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoGreaterThan(Integer value) {
            addCriterion("seq_no >", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoGreaterThanOrEqualTo(Integer value) {
            addCriterion("seq_no >=", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoLessThan(Integer value) {
            addCriterion("seq_no <", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoLessThanOrEqualTo(Integer value) {
            addCriterion("seq_no <=", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoIn(List<Integer> values) {
            addCriterion("seq_no in", values, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoNotIn(List<Integer> values) {
            addCriterion("seq_no not in", values, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoBetween(Integer value1, Integer value2) {
            addCriterion("seq_no between", value1, value2, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoNotBetween(Integer value1, Integer value2) {
            addCriterion("seq_no not between", value1, value2, "seqNo");
            return (Criteria) this;
        }

        public Criteria andCarStatusIsNull() {
            addCriterion("car_status is null");
            return (Criteria) this;
        }

        public Criteria andCarStatusIsNotNull() {
            addCriterion("car_status is not null");
            return (Criteria) this;
        }

        public Criteria andCarStatusEqualTo(Byte value) {
            addCriterion("car_status =", value, "carStatus");
            return (Criteria) this;
        }

        public Criteria andCarStatusNotEqualTo(Byte value) {
            addCriterion("car_status <>", value, "carStatus");
            return (Criteria) this;
        }

        public Criteria andCarStatusGreaterThan(Byte value) {
            addCriterion("car_status >", value, "carStatus");
            return (Criteria) this;
        }

        public Criteria andCarStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("car_status >=", value, "carStatus");
            return (Criteria) this;
        }

        public Criteria andCarStatusLessThan(Byte value) {
            addCriterion("car_status <", value, "carStatus");
            return (Criteria) this;
        }

        public Criteria andCarStatusLessThanOrEqualTo(Byte value) {
            addCriterion("car_status <=", value, "carStatus");
            return (Criteria) this;
        }

        public Criteria andCarStatusIn(List<Byte> values) {
            addCriterion("car_status in", values, "carStatus");
            return (Criteria) this;
        }

        public Criteria andCarStatusNotIn(List<Byte> values) {
            addCriterion("car_status not in", values, "carStatus");
            return (Criteria) this;
        }

        public Criteria andCarStatusBetween(Byte value1, Byte value2) {
            addCriterion("car_status between", value1, value2, "carStatus");
            return (Criteria) this;
        }

        public Criteria andCarStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("car_status not between", value1, value2, "carStatus");
            return (Criteria) this;
        }

        public Criteria andPicUrlIsNull() {
            addCriterion("pic_url is null");
            return (Criteria) this;
        }

        public Criteria andPicUrlIsNotNull() {
            addCriterion("pic_url is not null");
            return (Criteria) this;
        }

        public Criteria andPicUrlEqualTo(String value) {
            addCriterion("pic_url =", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlNotEqualTo(String value) {
            addCriterion("pic_url <>", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlGreaterThan(String value) {
            addCriterion("pic_url >", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlGreaterThanOrEqualTo(String value) {
            addCriterion("pic_url >=", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlLessThan(String value) {
            addCriterion("pic_url <", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlLessThanOrEqualTo(String value) {
            addCriterion("pic_url <=", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlLike(String value) {
            addCriterion("pic_url like", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlNotLike(String value) {
            addCriterion("pic_url not like", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlIn(List<String> values) {
            addCriterion("pic_url in", values, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlNotIn(List<String> values) {
            addCriterion("pic_url not in", values, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlBetween(String value1, String value2) {
            addCriterion("pic_url between", value1, value2, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlNotBetween(String value1, String value2) {
            addCriterion("pic_url not between", value1, value2, "picUrl");
            return (Criteria) this;
        }

        public Criteria andHasGifPicIsNull() {
            addCriterion("has_gif_pic is null");
            return (Criteria) this;
        }

        public Criteria andHasGifPicIsNotNull() {
            addCriterion("has_gif_pic is not null");
            return (Criteria) this;
        }

        public Criteria andHasGifPicEqualTo(Boolean value) {
            addCriterion("has_gif_pic =", value, "hasGifPic");
            return (Criteria) this;
        }

        public Criteria andHasGifPicNotEqualTo(Boolean value) {
            addCriterion("has_gif_pic <>", value, "hasGifPic");
            return (Criteria) this;
        }

        public Criteria andHasGifPicGreaterThan(Boolean value) {
            addCriterion("has_gif_pic >", value, "hasGifPic");
            return (Criteria) this;
        }

        public Criteria andHasGifPicGreaterThanOrEqualTo(Boolean value) {
            addCriterion("has_gif_pic >=", value, "hasGifPic");
            return (Criteria) this;
        }

        public Criteria andHasGifPicLessThan(Boolean value) {
            addCriterion("has_gif_pic <", value, "hasGifPic");
            return (Criteria) this;
        }

        public Criteria andHasGifPicLessThanOrEqualTo(Boolean value) {
            addCriterion("has_gif_pic <=", value, "hasGifPic");
            return (Criteria) this;
        }

        public Criteria andHasGifPicIn(List<Boolean> values) {
            addCriterion("has_gif_pic in", values, "hasGifPic");
            return (Criteria) this;
        }

        public Criteria andHasGifPicNotIn(List<Boolean> values) {
            addCriterion("has_gif_pic not in", values, "hasGifPic");
            return (Criteria) this;
        }

        public Criteria andHasGifPicBetween(Boolean value1, Boolean value2) {
            addCriterion("has_gif_pic between", value1, value2, "hasGifPic");
            return (Criteria) this;
        }

        public Criteria andHasGifPicNotBetween(Boolean value1, Boolean value2) {
            addCriterion("has_gif_pic not between", value1, value2, "hasGifPic");
            return (Criteria) this;
        }

        public Criteria andGifUrlIsNull() {
            addCriterion("gif_url is null");
            return (Criteria) this;
        }

        public Criteria andGifUrlIsNotNull() {
            addCriterion("gif_url is not null");
            return (Criteria) this;
        }

        public Criteria andGifUrlEqualTo(String value) {
            addCriterion("gif_url =", value, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andGifUrlNotEqualTo(String value) {
            addCriterion("gif_url <>", value, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andGifUrlGreaterThan(String value) {
            addCriterion("gif_url >", value, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andGifUrlGreaterThanOrEqualTo(String value) {
            addCriterion("gif_url >=", value, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andGifUrlLessThan(String value) {
            addCriterion("gif_url <", value, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andGifUrlLessThanOrEqualTo(String value) {
            addCriterion("gif_url <=", value, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andGifUrlLike(String value) {
            addCriterion("gif_url like", value, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andGifUrlNotLike(String value) {
            addCriterion("gif_url not like", value, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andGifUrlIn(List<String> values) {
            addCriterion("gif_url in", values, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andGifUrlNotIn(List<String> values) {
            addCriterion("gif_url not in", values, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andGifUrlBetween(String value1, String value2) {
            addCriterion("gif_url between", value1, value2, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andGifUrlNotBetween(String value1, String value2) {
            addCriterion("gif_url not between", value1, value2, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andHasVggPicIsNull() {
            addCriterion("has_vgg_pic is null");
            return (Criteria) this;
        }

        public Criteria andHasVggPicIsNotNull() {
            addCriterion("has_vgg_pic is not null");
            return (Criteria) this;
        }

        public Criteria andHasVggPicEqualTo(Boolean value) {
            addCriterion("has_vgg_pic =", value, "hasVggPic");
            return (Criteria) this;
        }

        public Criteria andHasVggPicNotEqualTo(Boolean value) {
            addCriterion("has_vgg_pic <>", value, "hasVggPic");
            return (Criteria) this;
        }

        public Criteria andHasVggPicGreaterThan(Boolean value) {
            addCriterion("has_vgg_pic >", value, "hasVggPic");
            return (Criteria) this;
        }

        public Criteria andHasVggPicGreaterThanOrEqualTo(Boolean value) {
            addCriterion("has_vgg_pic >=", value, "hasVggPic");
            return (Criteria) this;
        }

        public Criteria andHasVggPicLessThan(Boolean value) {
            addCriterion("has_vgg_pic <", value, "hasVggPic");
            return (Criteria) this;
        }

        public Criteria andHasVggPicLessThanOrEqualTo(Boolean value) {
            addCriterion("has_vgg_pic <=", value, "hasVggPic");
            return (Criteria) this;
        }

        public Criteria andHasVggPicIn(List<Boolean> values) {
            addCriterion("has_vgg_pic in", values, "hasVggPic");
            return (Criteria) this;
        }

        public Criteria andHasVggPicNotIn(List<Boolean> values) {
            addCriterion("has_vgg_pic not in", values, "hasVggPic");
            return (Criteria) this;
        }

        public Criteria andHasVggPicBetween(Boolean value1, Boolean value2) {
            addCriterion("has_vgg_pic between", value1, value2, "hasVggPic");
            return (Criteria) this;
        }

        public Criteria andHasVggPicNotBetween(Boolean value1, Boolean value2) {
            addCriterion("has_vgg_pic not between", value1, value2, "hasVggPic");
            return (Criteria) this;
        }

        public Criteria andVggUrlIsNull() {
            addCriterion("vgg_url is null");
            return (Criteria) this;
        }

        public Criteria andVggUrlIsNotNull() {
            addCriterion("vgg_url is not null");
            return (Criteria) this;
        }

        public Criteria andVggUrlEqualTo(String value) {
            addCriterion("vgg_url =", value, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andVggUrlNotEqualTo(String value) {
            addCriterion("vgg_url <>", value, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andVggUrlGreaterThan(String value) {
            addCriterion("vgg_url >", value, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andVggUrlGreaterThanOrEqualTo(String value) {
            addCriterion("vgg_url >=", value, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andVggUrlLessThan(String value) {
            addCriterion("vgg_url <", value, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andVggUrlLessThanOrEqualTo(String value) {
            addCriterion("vgg_url <=", value, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andVggUrlLike(String value) {
            addCriterion("vgg_url like", value, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andVggUrlNotLike(String value) {
            addCriterion("vgg_url not like", value, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andVggUrlIn(List<String> values) {
            addCriterion("vgg_url in", values, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andVggUrlNotIn(List<String> values) {
            addCriterion("vgg_url not in", values, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andVggUrlBetween(String value1, String value2) {
            addCriterion("vgg_url between", value1, value2, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andVggUrlNotBetween(String value1, String value2) {
            addCriterion("vgg_url not between", value1, value2, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitIsNull() {
            addCriterion("is_time_limit is null");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitIsNotNull() {
            addCriterion("is_time_limit is not null");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitEqualTo(Boolean value) {
            addCriterion("is_time_limit =", value, "isTimeLimit");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitNotEqualTo(Boolean value) {
            addCriterion("is_time_limit <>", value, "isTimeLimit");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitGreaterThan(Boolean value) {
            addCriterion("is_time_limit >", value, "isTimeLimit");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_time_limit >=", value, "isTimeLimit");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitLessThan(Boolean value) {
            addCriterion("is_time_limit <", value, "isTimeLimit");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitLessThanOrEqualTo(Boolean value) {
            addCriterion("is_time_limit <=", value, "isTimeLimit");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitIn(List<Boolean> values) {
            addCriterion("is_time_limit in", values, "isTimeLimit");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitNotIn(List<Boolean> values) {
            addCriterion("is_time_limit not in", values, "isTimeLimit");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitBetween(Boolean value1, Boolean value2) {
            addCriterion("is_time_limit between", value1, value2, "isTimeLimit");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_time_limit not between", value1, value2, "isTimeLimit");
            return (Criteria) this;
        }

        public Criteria andEffectiveTimeIsNull() {
            addCriterion("effective_time is null");
            return (Criteria) this;
        }

        public Criteria andEffectiveTimeIsNotNull() {
            addCriterion("effective_time is not null");
            return (Criteria) this;
        }

        public Criteria andEffectiveTimeEqualTo(Long value) {
            addCriterion("effective_time =", value, "effectiveTime");
            return (Criteria) this;
        }

        public Criteria andEffectiveTimeNotEqualTo(Long value) {
            addCriterion("effective_time <>", value, "effectiveTime");
            return (Criteria) this;
        }

        public Criteria andEffectiveTimeGreaterThan(Long value) {
            addCriterion("effective_time >", value, "effectiveTime");
            return (Criteria) this;
        }

        public Criteria andEffectiveTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("effective_time >=", value, "effectiveTime");
            return (Criteria) this;
        }

        public Criteria andEffectiveTimeLessThan(Long value) {
            addCriterion("effective_time <", value, "effectiveTime");
            return (Criteria) this;
        }

        public Criteria andEffectiveTimeLessThanOrEqualTo(Long value) {
            addCriterion("effective_time <=", value, "effectiveTime");
            return (Criteria) this;
        }

        public Criteria andEffectiveTimeIn(List<Long> values) {
            addCriterion("effective_time in", values, "effectiveTime");
            return (Criteria) this;
        }

        public Criteria andEffectiveTimeNotIn(List<Long> values) {
            addCriterion("effective_time not in", values, "effectiveTime");
            return (Criteria) this;
        }

        public Criteria andEffectiveTimeBetween(Long value1, Long value2) {
            addCriterion("effective_time between", value1, value2, "effectiveTime");
            return (Criteria) this;
        }

        public Criteria andEffectiveTimeNotBetween(Long value1, Long value2) {
            addCriterion("effective_time not between", value1, value2, "effectiveTime");
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

        public Criteria andAllowPurseIsNull() {
            addCriterion("allow_purse is null");
            return (Criteria) this;
        }

        public Criteria andAllowPurseIsNotNull() {
            addCriterion("allow_purse is not null");
            return (Criteria) this;
        }

        public Criteria andAllowPurseEqualTo(Byte value) {
            addCriterion("allow_purse =", value, "allowPurse");
            return (Criteria) this;
        }

        public Criteria andAllowPurseNotEqualTo(Byte value) {
            addCriterion("allow_purse <>", value, "allowPurse");
            return (Criteria) this;
        }

        public Criteria andAllowPurseGreaterThan(Byte value) {
            addCriterion("allow_purse >", value, "allowPurse");
            return (Criteria) this;
        }

        public Criteria andAllowPurseGreaterThanOrEqualTo(Byte value) {
            addCriterion("allow_purse >=", value, "allowPurse");
            return (Criteria) this;
        }

        public Criteria andAllowPurseLessThan(Byte value) {
            addCriterion("allow_purse <", value, "allowPurse");
            return (Criteria) this;
        }

        public Criteria andAllowPurseLessThanOrEqualTo(Byte value) {
            addCriterion("allow_purse <=", value, "allowPurse");
            return (Criteria) this;
        }

        public Criteria andAllowPurseIn(List<Byte> values) {
            addCriterion("allow_purse in", values, "allowPurse");
            return (Criteria) this;
        }

        public Criteria andAllowPurseNotIn(List<Byte> values) {
            addCriterion("allow_purse not in", values, "allowPurse");
            return (Criteria) this;
        }

        public Criteria andAllowPurseBetween(Byte value1, Byte value2) {
            addCriterion("allow_purse between", value1, value2, "allowPurse");
            return (Criteria) this;
        }

        public Criteria andAllowPurseNotBetween(Byte value1, Byte value2) {
            addCriterion("allow_purse not between", value1, value2, "allowPurse");
            return (Criteria) this;
        }

        public Criteria andLeftLevelIsNull() {
            addCriterion("left_level is null");
            return (Criteria) this;
        }

        public Criteria andLeftLevelIsNotNull() {
            addCriterion("left_level is not null");
            return (Criteria) this;
        }

        public Criteria andLeftLevelEqualTo(Integer value) {
            addCriterion("left_level =", value, "leftLevel");
            return (Criteria) this;
        }

        public Criteria andLeftLevelNotEqualTo(Integer value) {
            addCriterion("left_level <>", value, "leftLevel");
            return (Criteria) this;
        }

        public Criteria andLeftLevelGreaterThan(Integer value) {
            addCriterion("left_level >", value, "leftLevel");
            return (Criteria) this;
        }

        public Criteria andLeftLevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("left_level >=", value, "leftLevel");
            return (Criteria) this;
        }

        public Criteria andLeftLevelLessThan(Integer value) {
            addCriterion("left_level <", value, "leftLevel");
            return (Criteria) this;
        }

        public Criteria andLeftLevelLessThanOrEqualTo(Integer value) {
            addCriterion("left_level <=", value, "leftLevel");
            return (Criteria) this;
        }

        public Criteria andLeftLevelIn(List<Integer> values) {
            addCriterion("left_level in", values, "leftLevel");
            return (Criteria) this;
        }

        public Criteria andLeftLevelNotIn(List<Integer> values) {
            addCriterion("left_level not in", values, "leftLevel");
            return (Criteria) this;
        }

        public Criteria andLeftLevelBetween(Integer value1, Integer value2) {
            addCriterion("left_level between", value1, value2, "leftLevel");
            return (Criteria) this;
        }

        public Criteria andLeftLevelNotBetween(Integer value1, Integer value2) {
            addCriterion("left_level not between", value1, value2, "leftLevel");
            return (Criteria) this;
        }

        public Criteria andMarkPicIsNull() {
            addCriterion("mark_pic is null");
            return (Criteria) this;
        }

        public Criteria andMarkPicIsNotNull() {
            addCriterion("mark_pic is not null");
            return (Criteria) this;
        }

        public Criteria andMarkPicEqualTo(String value) {
            addCriterion("mark_pic =", value, "markPic");
            return (Criteria) this;
        }

        public Criteria andMarkPicNotEqualTo(String value) {
            addCriterion("mark_pic <>", value, "markPic");
            return (Criteria) this;
        }

        public Criteria andMarkPicGreaterThan(String value) {
            addCriterion("mark_pic >", value, "markPic");
            return (Criteria) this;
        }

        public Criteria andMarkPicGreaterThanOrEqualTo(String value) {
            addCriterion("mark_pic >=", value, "markPic");
            return (Criteria) this;
        }

        public Criteria andMarkPicLessThan(String value) {
            addCriterion("mark_pic <", value, "markPic");
            return (Criteria) this;
        }

        public Criteria andMarkPicLessThanOrEqualTo(String value) {
            addCriterion("mark_pic <=", value, "markPic");
            return (Criteria) this;
        }

        public Criteria andMarkPicLike(String value) {
            addCriterion("mark_pic like", value, "markPic");
            return (Criteria) this;
        }

        public Criteria andMarkPicNotLike(String value) {
            addCriterion("mark_pic not like", value, "markPic");
            return (Criteria) this;
        }

        public Criteria andMarkPicIn(List<String> values) {
            addCriterion("mark_pic in", values, "markPic");
            return (Criteria) this;
        }

        public Criteria andMarkPicNotIn(List<String> values) {
            addCriterion("mark_pic not in", values, "markPic");
            return (Criteria) this;
        }

        public Criteria andMarkPicBetween(String value1, String value2) {
            addCriterion("mark_pic between", value1, value2, "markPic");
            return (Criteria) this;
        }

        public Criteria andMarkPicNotBetween(String value1, String value2) {
            addCriterion("mark_pic not between", value1, value2, "markPic");
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
