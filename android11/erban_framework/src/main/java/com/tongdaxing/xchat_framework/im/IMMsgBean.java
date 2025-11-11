package com.tongdaxing.xchat_framework.im;


import com.tongdaxing.xchat_framework.util.util.Json;

public class IMMsgBean {
    public int id;
    public String route;
    public Json resData;

    public IMMsgBean(String data) {
        this(Json.parse(data));


    }

    public IMMsgBean(Json json) {

        id = json.num(IMKey.id);
        route = json.str(IMKey.route);
        resData = json.json_ok(IMKey.res_data);
    }

    public int getId() {
        return id;
    }

    public String getRoute() {
        return route;
    }

    public Json getResData() {
        return resData;
    }


}
