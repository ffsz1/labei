var wuid, werbanNo, wtype, wbeginDate, wendDate;
/*充值金额明细*/
function getChargeAmountDetail(uid, erbanNo, beginDate, endDate) {
    $('#detailTable').bootstrapTable('destroy');
    $('#detailTable').bootstrapTable({
        columns: [
            {field: 'chargeRecordId', title: '订单ID', align: 'center', width: '8%'},
            {field: 'pingxxChargeId', title: 'ping++单号', align: 'center', width: '8%'},
            {field: 'erbanNo', title: '拉贝号', align: 'center', width: '8%'},
            {field: 'nick', title: '昵  称', align: 'center', width: '20%'},
            {field: 'phone', title: '电话', align: 'center', width: '5%'},
            {field: 'os', title: '平台', align: 'center', width: '8%'},
            {
                field: 'gender', title: '性别', align: 'center', width: '8%', formatter: function (val, row, index) {
                    if (val == 1) {
                        return '男';
                    } else if (val == 2) {
                        return '女';
                    } else {
                        return '其他';
                    }
                }
            },
            {field: 'clientIp', title: '用户IP', align: 'center', width: '8%'},
            {field: 'totalGold', title: '金币数量', align: 'center', width: '8%'},
            {
                field: 'chargeDesc',
                title: '产品描述',
                align: 'center',
                width: '8%',
                formatter: function (val, row, index) {
                    if (val != null) {
                        return val;
                    } else if (row.subject != null) {
                        return row.subject;
                    } else {
                        return '-';
                    }
                }
            },
            {
                field: 'amount', title: '金额(元)', align: 'center', width: '5%', formatter: function (val, row, index) {
                    return val / 100;
                }
            },
            {
                field: 'channel', title: '支付渠道', align: 'center', width: '10%', formatter: function (val, row, index) {
                    if (val == "wx") {
                        return "微信";
                    } else if (val == "alipay") {
                        return "支付宝";
                    } else if (val == "exchange") {
                        return "exchange";
                    } else if (val == "company") {
                        return "company";
                    } else if (val == "alipay_wap") {
                        return "支付宝H5";
                    } else if (val == "wx_wap") {
                        return "微信H5";
                    } else if (val = 'ios_pay') {
                        return 'iOS内购'
                    }
                }
            },
            {
                field: 'chargeStatus',
                title: '支付状态',
                align: 'center',
                width: '10%',
                formatter: function (val, row, index) {
                    if (val == 1) {
                        return "<span style=\"color:grey;\">未支付</span>";
                    } else if (val == 2) {
                        return "<span style=\"color:red;\">已支付</span>";
                    } else if (val == 3) {
                        return "<span style=\"color:yellow;\">支付异常</span>";
                    } else if (val == 4) {
                        return "<span style=\"color:green;\">支付超时</span>";
                    } else {
                        return '-';
                    }
                }
            },
            {
                field: 'createTime', title: '购买时间', align: 'center', width: '10%',
                formatter: function (val, row, index) {
                    if (val) {
                        var date = new Date(val);
                        return date.format("yyyy-MM-dd HH:mm:ss");
                    } else {
                        return '-';
                    }
                }
            },
            {
                field: 'updateTime', title: '回调时间', align: 'center', width: '10%',
                formatter: function (val, row, index) {
                    if (val) {
                        var date = new Date(val);
                        return date.format("yyyy-MM-dd HH:mm:ss");
                    } else {
                        return '-';
                    }
                }
            },

        ],
        cache: false,
        striped: true,
        // showRefresh: true,
        sortStable: true,
        pageSize: 10,
        pagination: true,
        pageList: [10, 20, 30, 50],
        search: false,
        sidePagination: "server", //表示服务端请求
        //设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder
        //设置为limit可以获取limit, offset, search, sort, order
        queryParamsType: "undefined",
        queryParams: function queryParams(params) {   //设置查询参数
            var param = {
                page: params.pageNumber,
                size: params.pageSize,
                type: 1,
                uid: uid,
                erbanNo: erbanNo,
                beginDate: beginDate,
                endDate: endDate,
            };
            return param;
        },
        uniqueId: 'id',
        toolbar: '#toolbar',
        url: '/admin/stat/detail.action',
        method: 'get',
        responseHandler: function (res) {
            if (res.code == 200) {
                console.log(res.data)
                return {
                    "total": res.data.total,//总页数
                    "rows": res.data.list  //数据
                };
            } else {
                $("#tipMsg").text(res.message + "[" + res.code + "]");
                $("#tipModal").modal('show');
                return {
                    "total": "",//总页数
                    "rows": []  //数据
                };
            }
        }
    });
    $("#detailModel").modal('show');
}

/*兑换金币*/
function getExechangeGoldDetail(uid, erbanNo, beginDate, endDate) {
    $('#detailTable').bootstrapTable('destroy');
    $('#detailTable').bootstrapTable({
        columns: [
            {field: 'recordId', title: 'ID', align: 'center', width: '3%'},
            {field: 'erbanNo', title: '拉贝号', align: 'center', width: '3%'},
            {field: 'nick', title: '昵称', align: 'center', width: '3%'},
            {field: 'phone', title: '电话', align: 'center', width: '3%'},
            {field: 'exDiamondNum', title: '钻石', align: 'center', width: '8%'},
            {field: 'exGoldNum', title: '金币', align: 'center', width: '8%'},
            {field: 'alipayAccount', title: '支付宝', align: 'center', width: '3%'},
            {field: 'alipayAccountName', title: '真实姓名', align: 'center', width: '3%'},
            {
                field: 'createTime', title: '创建时间', align: 'center', width: '10%',
                formatter: function (val, row, index) {
                    if (val) {
                        var date = new Date(val);
                        return date.format("yyyy-MM-dd HH:mm:ss");
                    } else {
                        return '-';
                    }
                }
            }
        ],
        cache: false,
        striped: true,
        // showRefresh: true,
        sortStable: true,
        pageSize: 10,
        pagination: true,
        pageList: [10, 20, 30, 50],
        search: false,
        sidePagination: "server", //表示服务端请求
        //设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder
        //设置为limit可以获取limit, offset, search, sort, order
        queryParamsType: "undefined",
        queryParams: function queryParams(params) {   //设置查询参数
            var param = {
                page: params.pageNumber,
                size: params.pageSize,
                type: 2,
                uid: uid,
                erbanNo: erbanNo,
                beginDate: beginDate,
                endDate: endDate,
            };
            return param;
        },
        uniqueId: 'id',
        toolbar: '#toolbar',
        url: '/admin/stat/detail.action',
        method: 'get',
        responseHandler: function (res) {
            if (res.code == 200) {
                console.log(res.data)
                return {
                    "total": res.data.total,//总页数
                    "rows": res.data.list  //数据
                };
            } else {
                $("#tipMsg").text(res.message + "[" + res.code + "]");
                $("#tipModal").modal('show');
                return {
                    "total": "",//总页数
                    "rows": []  //数据
                };
            }
        }
    });
    $("#detailModel").modal('show');
}

/*钻石提现*/
function getDiamondWithDrawDetail(uid, erbanNo, beginDate, endDate) {
    $('#detailTable').bootstrapTable('destroy');
    $('#detailTable').bootstrapTable({
        columns: [
            {field: 'billId', title: 'ID', align: 'center', width: '3%'},
            {field: 'erbanNo', title: '拉贝号', align: 'center', width: '3%'},
            {field: 'nick', title: '昵称', align: 'center', width: '3%'},
            {
                field: 'gender', title: '性别', align: 'center', width: '8%',
                formatter: function (val, row, index) {
                    if (val == 1) {
                        return '男';
                    } else if (val == 2) {
                        return '女';
                    } else if (val == 0) {
                        return '其他';
                    }
                }
            },
            {field: 'phone', title: '电话', align: 'center', width: '3%'},
            {field: 'alipayAccount', title: '支付宝', align: 'center', width: '3%'},
            {field: 'alipayAccountName', title: '真实姓名', align: 'center', width: '3%'},
            {field: 'diamondNum', title: '钻石数量', align: 'center', width: '5%'},
            {field: 'money', title: '提现金额', align: 'center', width: '8%'},
            {
                field: 'billStatus', title: '提现状态', align: 'center', width: '8%',
                formatter: function (val, row, index) {
                    if (val == 1) {
                        return '提现中';
                    } else if (val == 2) {
                        return '提现完成';
                    } else if (val == 3) {
                        return '提现异常';
                    } else {
                        return '-';
                    }
                }
            },
            {
                field: 'createTime', title: '创建时间', align: 'center', width: '10%',
                formatter: function (val, row, index) {
                    if (val) {
                        var date = new Date(val);
                        return date.format("yyyy-MM-dd HH:mm:ss");
                    } else {
                        return '-';
                    }
                }
            }
        ],
        cache: false,
        striped: true,
        // showRefresh: true,
        sortStable: true,
        pageSize: 10,
        pagination: true,
        pageList: [10, 20, 30, 50],
        search: false,
        sidePagination: "server", //表示服务端请求
        //设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder
        //设置为limit可以获取limit, offset, search, sort, order
        queryParamsType: "undefined",
        queryParams: function queryParams(params) {   //设置查询参数
            const param = {
                page: params.pageNumber,
                size: params.pageSize,
                type: 3,
                uid: uid,
                erbanNo: erbanNo,
                beginDate: beginDate,
                endDate: endDate,
            };
            return param;
        },
        uniqueId: 'id',
        toolbar: '#toolbar',
        url: '/admin/stat/detail.action',
        method: 'get',
        responseHandler: function (res) {
            if (res.code == 200) {
                console.log(res.data)
                return {
                    "total": res.data.total,//总页数
                    "rows": res.data.list  //数据
                };
            } else {
                $("#tipMsg").text(res.message + "[" + res.code + "]");
                $("#tipModal").modal('show');
                return {
                    "total": "",//总页数
                    "rows": []  //数据
                };
            }
        }
    });
    $("#detailModel").modal('show');
}

// 财富变化
function getExperChange(uid, erbanNo, beginDate, endDate) {
    wuid = uid;
    werbanNo = erbanNo;
    wtype = 4;
    wbeginDate = beginDate;
    wendDate = endDate;
    $('#detailTable').bootstrapTable('destroy');
    $('#detailTable').bootstrapTable({
        columns: [
            {field: 'sendNo', title: '赠拉贝号', align: 'center', width: 120},
            {field: 'sendNick', title: '赠昵称', align: 'center', width: 150},
            {field: 'reciveNo', title: '收拉贝号', align: 'center', width: 120},
            {field: 'reciveNick', title: '收昵称', align: 'center', width: 150},
            {field: 'roomNo', title: '房间ID', align: 'center', width: 120},
            {field: 'roomNick', title: '房间名称', align: 'center', width: 150},
            {field: 'giftName', title: '礼物名称', align: 'center', width: 150},
            {
                field: 'giftType', title: '礼物类型', align: 'center', width: 120,
                formatter: function (val) {
                    if (val == 1) {
                        return '打Call礼物';
                    } else if (val == 2) {
                        return '普通礼物';
                    } else if (val == 3) {
                        return '捡海螺礼物'
                    } else if (val = 4) {
                        return '活动礼物';
                    }
                }
            },
            {field: 'giftNum', title: '礼物数量', align: 'center', width: 120},
            {field: 'totalGoldNum', title: '总金币', align: 'center', width: 120},
            {
                field: 'createTime', title: '创建时间', align: 'center', width: 200,
                formatter: function (val, row, index) {
                    if (val) {
                        const date = new Date(val);
                        return date.format("yyyy-MM-dd HH:mm:ss");
                    } else {
                        return '-';
                    }
                }
            },
        ],
        cache: false,
        striped: true,
        // showRefresh: true,
        sortStable: true,
        pageSize: 50,
        pagination: true,
        pageList: [10, 20, 30, 50],
        search: false,
        sidePagination: "server",
        // 设置为undefined可以获取pageNumber, pageSize, searchText, sortName, sortOrder
        // 设置为limit可以获取limit, offset, search, sort, order
        queryParamsType: "undefined",
        queryParams: function queryParams(params) {
            const param = {
                page: params.pageNumber,
                size: params.pageSize,
                type: 4,
                uid: uid,
                erbanNo: erbanNo,
                beginDate: beginDate,
                endDate: endDate,
            };
            return param;
        },
        uniqueId: 'id',
        toolbar: '#toolbar',
        url: '/admin/stat/detail.action',
        method: 'get',
        responseHandler: function (res) {
            if (res.code == 200) {
                console.log(res.data)
                return {
                    "total": res.data.total,
                    "rows": res.data.list
                };
            } else {
                $("#tipMsg").text(res.message + "[" + res.code + "]");
                $("#tipModal").modal('show');
                return {
                    "total": "",
                    "rows": []
                };
            }
        }
    });
    $("#detailModel").modal('show');
}

// 魅力变化
function getCharmChange(uid, erbanNo, beginDate, endDate) {
    wuid = uid;
    wtype = 5;
    werbanNo = erbanNo;
    wbeginDate = beginDate;
    wendDate = endDate;
    $('#detailTable').bootstrapTable('destroy');
    $('#detailTable').bootstrapTable({
        columns: [
            {field: 'reciveNo', title: '收拉贝号', align: 'center', width: 120},
            {field: 'reciveNick', title: '收昵称', align: 'center', width: 150},
            {field: 'sendNo', title: '赠拉贝号', align: 'center', width: 120},
            {field: 'sendNick', title: '赠昵称', align: 'center', width: 150},
            {field: 'roomNo', title: '房间ID', align: 'center', width: 120},
            {field: 'roomNick', title: '房间名称', align: 'center', width: 150},
            {field: 'giftName', title: '礼物名称', align: 'center', width: 150},
            {
                field: 'giftType', title: '礼物类型', align: 'center', width: 120,
                formatter: function (val) {
                    if (val == 1) {
                        return '打Call礼物';
                    } else if (val == 2) {
                        return '普通礼物';
                    } else if (val == 3) {
                        return '捡海螺礼物'
                    } else if (val = 4) {
                        return '活动礼物';
                    }
                }
            },
            {field: 'giftNum', title: '礼物数量', align: 'center', width: 120},
            {field: 'totalGoldNum', title: '总金币', align: 'center', width: 120},
            {
                field: 'createTime', title: '创建时间', align: 'center', width: 200,
                formatter: function (val, row, index) {
                    if (val) {
                        const date = new Date(val);
                        return date.format("yyyy-MM-dd HH:mm:ss");
                    } else {
                        return '-';
                    }
                }
            },
        ],
        cache: false,
        striped: true,
        // showRefresh: true,
        sortStable: true,
        pageSize: 50,
        pagination: true,
        pageList: [10, 20, 30, 50],
        search: false,
        sidePagination: "server",
        // 设置为undefined可以获取pageNumber, pageSize, searchText, sortName, sortOrder
        // 设置为limit可以获取limit, offset, search, sort, order
        queryParamsType: "undefined",
        queryParams: function queryParams(params) {
            const param = {
                page: params.pageNumber,
                size: params.pageSize,
                type: 5,
                uid: uid,
                erbanNo: erbanNo,
                beginDate: beginDate,
                endDate: endDate,
            };
            return param;
        },
        uniqueId: 'id',
        toolbar: '#toolbar',
        url: '/admin/stat/detail.action',
        method: 'get',
        responseHandler: function (res) {
            if (res.code == 200) {
                return {
                    "total": res.data.total,
                    "rows": res.data.list
                };
            } else {
                $("#tipMsg").text(res.message + "[" + res.code + "]");
                $("#tipModal").modal('show');
                return {
                    "total": "",
                    "rows": []
                };
            }
        }
    });
    $("#detailModel").modal('show');
}

// 导出流水
function exportDetails() {
    const url = `/admin/stat/exportDetailExcel?uid=${wuid}&erbanNo=${werbanNo}&type=${wtype}&beginDate=${wbeginDate}endDate=${wendDate}`;
    window.location.href = url;
};
