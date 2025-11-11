var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) {
    return typeof obj;
} : function (obj) {
    return obj && typeof Symbol === "function" && obj.varructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};

const browser = checkVersion();
const info = {};

function getMessage(key, value) {
    info[key] = value;
    console.log(key, value, 'getMessage');
}

// 调用原生方法发送请求
function dispatchRequest(requestURL, method, requestHeader, params) {
    const type = method;
    const url = requestURL;
    const header = requestHeader;
    const data = params;
    if (browser.ios) {
        const obj = {
            requestMethod: type,
            urlController: url,
            headerMapString: header,
            paramMapString: data,
        };
        window.webkit.messageHandlers.httpRequest.postMessage(JSON.stringify(obj));
        // window.webkit.messageHandlers.httpRequest.postMessage(type,url,header,JSON.stringify(data));JSON.stringify(obj)
    } else if (browser.android) {
        if (androidJsObj && (typeof androidJsObj === 'undefined' ? 'undefined' : _typeof(androidJsObj)) === 'object') {
            window.androidJsObj.httpRequest(type, url, header, JSON.stringify(data));
        }
    }
}

// 回调响应事件
var onHttpResponse = function (e, item) {
    console.log(e, '接口返回', item);
    let body = null;
    if (browser.ios) {
        body = item;
        if (body.urlController == "/guild/hall/member/getGuildMemberCommonInfo") {   // 我的公会
            const responseData = JSON.parse(body.bodyString);
            console.log(responseData, 'bodyString');
            if (!body.isRequestError) {
                appendToAssociationInfo(responseData);
            } else {
                $('.toast').text(responseData.message.split(':')[1]).show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == "/guild/getRecommendList") {   // 公会推荐
            const responseData = JSON.parse(body.bodyString);
            if (!body.isRequestError) {
                console.log(responseData, 'bodyString');
                appendToRecommendList(responseData);
            } else {
                $('.toast').text(responseData.message.split(':')[1]).show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == "/guild/search") {   // 搜索公会
            const responseData = JSON.parse(body.bodyString);
            if (!body.isRequestError) {
                appendToSearchResult(responseData);
            } else {
                $('.toast').text(responseData.message.split(':')[1]).show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == '/guild/hall/apply/getApplyJoinRecords') {   // 公会通知
            const responseData = JSON.parse(body.bodyString);
            if (!body.isRequestError) {
                generateSnippets(responseData);
            } else {
                $('.toast').text(responseData.message.split(':')[1]).show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == '/guild/hall/apply/verify') {   // 通知审核
            if (!body.isRequestError) {
                $('.notices-list').html('');
            }
            getNotices();
        } else if (body.urlController == "/guild/get") {   // 公会主页
            const responseData = JSON.parse(body.bodyString);
            if (!body.isRequestError) {
                renderData(responseData);
            } else {
                $('.toast').text(responseData.message.split(':')[1]).show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == "/guild/hall/apply/applyJoinHall") {   // 申请加入公会
            if (!body.isRequestError) {
                $('.notice').hide();
                $('.pop-up>.tips').show();
            } else {
                $('.pop-up, .notice, .pop-up>.tips').hide();
                $('.toast').text(responseData.message.split(':')[1]).show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == "/guild/hall/apply/applyExitHall") {   // 申请退出公会
            if (!body.isRequestError) {
                $('.pop-up').show();
                $('.pop-up>.tips>.content').text('已提交退出申请，审核结果请留意小秘书通知');
                $('.pop-up>.tips').show();
            } else {
                $('.pop-up, .notice, .pop-up>.tips').hide();
                $('.toast').text(responseData.message.split(':')[1]).show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == '/guild/hall/member/getGuildMembers') {   // 公会成员
            const responseData = JSON.parse(body.bodyString);
            if (!body.isRequestError) {
                document.title = '公会成员';
                renderMembersList(responseData);
            } else {
                $('.toast').text(responseData.message.split(':')[1]).show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == '/guild/hall/get') {   // 厅主页
            const responseData = JSON.parse(body.bodyString);
            if (!body.isRequestError) {
                renderRoomData(responseData);
            } else {
                $('.toast').text(responseData.message.split(':')[1]).show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == '/guild/hall/member/getHallMembers') {   // 厅成员
            const responseData = JSON.parse(body.bodyString);
            if (!body.isRequestError) {
                document.title = '厅成员';
                renderMembersList(responseData);
            } else {
                $('.toast').text(responseData.message.split(':')[1]).show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == '/guild/hall/member/getGuildMemberPageInfo') {   // 我的数据详情
            const responseData = JSON.parse(body.bodyString);
            if (!body.isRequestError) {
                renderMyData(responseData);
            } else {
                $('.toast').text(responseData.message.split(':')[1]).show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        }
    } else {
        body = JSON.parse(JSON.stringify(e));
        if (body.urlController == "/guild/hall/member/getGuildMemberCommonInfo") {   // 我的公会
            const responseData = JSON.parse(body.bodyString);
            console.log(responseData, 'bodyString');
            if (!body.isRequestError) {
                if (responseData.code == 200) {
                    appendToAssociationInfo(responseData.data);
                } else {
                    $('.toast').text(responseData.message.split(':')[1]).show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1200);
                }
            } else {
                $('.toast').text('获取数据失败!').show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == "/guild/getRecommendList") {   // 公会推荐
            if (!body.isRequestError) {
                const responseData = JSON.parse(body.bodyString);
                console.log(responseData, 'bodyString');
                if (responseData.code == 200) {
                    appendToRecommendList(responseData.data)
                } else {
                    $('.toast').text(responseData.message.split(':')[1]).show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1200);
                }
            } else {
                $('.toast').text('获取数据失败!').show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == "/guild/search") {   // 搜索公会
            if (!body.isRequestError) {
                const responseData = JSON.parse(body.bodyString);
                if (responseData.code == 200) {
                    appendToSearchResult(responseData.data)
                } else {
                    $('.toast').text(responseData.message.split(':')[1]).show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1200);
                }
            } else {
                $('.toast').text('获取数据失败!').show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == '/guild/hall/apply/getApplyJoinRecords') {   // 公会通知
            if (!body.isRequestError) {
                const responseData = JSON.parse(body.bodyString);
                if (responseData.code == 200) {
                    generateSnippets(responseData.data)
                } else {
                    $('.toast').text(responseData.message.split(':')[1]).show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1200);
                }
            } else {
                $('.toast').text('获取数据失败!').show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == '/guild/hall/apply/verify') {   // 通知审核
            if (!body.isRequestError) {
                const responseData = JSON.parse(body.bodyString);
                if (responseData.code == 200) {
                    $('.notices-list').html('');
                } else {
                    $('.toast').text(responseData.message.split(':')[1]).show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1200);
                }
                getNotices();
            } else {
                $('.toast').text('获取数据失败!').show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == "/guild/get") {   // 公会主页
            if (!body.isRequestError) {
                const responseData = JSON.parse(body.bodyString);
                if (responseData.code == 200) {
                    renderData(responseData.data)
                } else {
                    $('.toast').text(responseData.message.split(':')[1]).show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1200);
                }
            } else {
                $('.toast').text('获取数据失败!').show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == "/guild/hall/apply/applyJoinHall") {   // 申请加入公会
            if (!body.isRequestError) {
                const responseData = JSON.parse(body.bodyString);
                if (responseData.code == 200) {
                    $('.notice').hide();
                    $('.pop-up>.tips').show();
                } else {
                    $('.pop-up, .notice').hide();
                    $('.toast').text(responseData.message.split(':')[1]).show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1200);
                }
            } else {
                $('.pop-up, .notice').hide();
                $('.toast').text('操作失败!').show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == "/guild/hall/apply/applyExitHall") {   // 申请退出公会
            if (!body.isRequestError) {
                const responseData = JSON.parse(body.bodyString);
                if (responseData.code == 200) {
                    $('.pop-up').show();
                    $('.pop-up>.tips>.content').text('已提交退出申请，审核结果请留意小秘书通知');
                    $('.pop-up>.tips').show();
                } else {
                    $('.pop-up, .dialog').hide();
                    $('.out').show();
                    $('.dialog>.confirm').hide();
                    $('.toast').text(responseData.message.split(':')[1]).show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1200);
                }
            } else {
                $('.pop-up, .dialog').hide();
                $('.out').show();
                $('.dialog>.confirm').hide();
                $('.toast').text('操作失败!').show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == '/guild/hall/member/getGuildMembers') {   // 公会成员
            if (!body.isRequestError) {
                const responseData = JSON.parse(body.bodyString);
                if (responseData.code == 200) {
                    document.title = '公会成员';
                    renderMembersList(responseData.data)
                } else {
                    $('.toast').text(responseData.message.split(':')[1]).show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1200);
                }
            } else {
                $('.toast').text('获取数据失败!').show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == '/guild/hall/get') {   // 厅主页
            if (!body.isRequestError) {
                const responseData = JSON.parse(body.bodyString);
                if (responseData.code == 200) {
                    renderRoomData(responseData.data);
                } else {
                    $('.toast').text(responseData.message.split(':')[1]).show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1200);
                }
            } else {
                $('.toast').text('获取数据失败!').show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == '/guild/hall/member/getHallMembers') {   // 厅成员
            if (!body.isRequestError) {
                const responseData = JSON.parse(body.bodyString);
                if (responseData.code == 200) {
                    document.title = '厅成员';
                    renderMembersList(responseData.data);
                } else {
                    $('.toast').text(responseData.message.split(':')[1]).show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1200);
                }
            } else {
                $('.toast').text('获取数据失败!').show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        } else if (body.urlController == '/guild/hall/member/getGuildMemberPageInfo') {   // 我的数据详情
            if (!body.isRequestError) {
                const responseData = JSON.parse(body.bodyString);
                if (responseData.code == 200) {
                    renderMyData(responseData.data);
                } else {
                    $('.toast').text(responseData.message.split(':')[1]).show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1200);
                }
            } else {
                $('.toast').text('获取数据失败!').show();
                setTimeout(function () {
                    $('.toast').hide();
                }, 1200);
            }
        }
    }
};

// 返回上一页
$('#back').on('click', function () {
    window.history.go(-1);
});

// 日期格式化
Date.prototype.format = function (fmt) { //author: meizz 
    const o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (const k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}