package com.tongdaxing.xchat_framework.im;


import com.tongdaxing.xchat_framework.util.util.Json;

public class IMReportBean extends IMMsgBean {


    private ReportData reportData;

    public ReportData getReportData() {
        return reportData;
    }

    public IMReportBean(String data) {
        super(data);
        reportData = new ReportData(resData);
    }

    public IMReportBean(Json data) {
        super(data);
        reportData = new ReportData(resData);
    }

    public class ReportData {
        public int errno;
        public String errmsg;
        public Json data;
        public Json errdata;

        public ReportData(Json json) {
            if (json == null)
                return;
            errno = json.num(IMKey.errno);
            errmsg = json.str(IMKey.errmsg);
            errdata = json.json_ok(IMKey.errdata);
            data = json.json_ok(IMKey.data);
        }
    }
}
