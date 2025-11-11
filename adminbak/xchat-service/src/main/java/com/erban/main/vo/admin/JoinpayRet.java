package com.erban.main.vo.admin;

public class JoinpayRet {
    // 应答码
    private String statusCode;
    // 应答信息
    private String message;
    // 应答数据
    private JoinpayTransferCallback data;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JoinpayTransferCallback getData() {
        return data;
    }

    public void setData(JoinpayTransferCallback data) {
        this.data = data;
    }
}
