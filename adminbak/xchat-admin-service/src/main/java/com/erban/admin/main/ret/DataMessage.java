package com.erban.admin.main.ret;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataMessage {

    /** 地区*/
    private String area;
    /** 国家*/
    private String country;
    /** 国家编号*/
    @JsonProperty("country_id")
    private String countryId;
    /** 省份编号*/
    @JsonProperty("region_id")
    private String regionId;
    /** 省份*/
    private String region;
    /** 城市*/
    private String city;
    /** 城市编号*/
    @JsonProperty("city_id")
    private String cityId;
    /** Long类型的IP*/
    @JsonProperty("long_ip")
    private String longIp;
    /** IP地址*/
    private String ip;
    /** 运营商*/
    private String isp;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getLongIp() {
        return longIp;
    }

    public void setLongIp(String longIp) {
        this.longIp = longIp;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }
}
