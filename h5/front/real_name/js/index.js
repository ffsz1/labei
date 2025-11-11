// $(document).ready(function () {
    var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) {
        return typeof obj;
    } : function (obj) {
        return obj && typeof Symbol === "function" && obj.varructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
    };

    var browser = checkVersion();
    var info = {};
    var timeInterval = null;
    var flag = false;
    var flag1 = false;

    function getMessage(key, value) {
        info[key] = value;
        console.log(key, value, 'getMessage');
        if (info.uid && info.ticket && !flag) {
            flag = true;
            console.log(info);
            authenticationStatus();
        }
        if (info.phone && !flag1) {
            flag1 = true;
            selePhone();
        }
    }

    setTimeout(function () {
        if (browser.ios && window.webkit) {
            window.webkit.messageHandlers.getUserPhoneNumber.postMessage(null);
            window.webkit.messageHandlers.getUid.postMessage(null);
            window.webkit.messageHandlers.getTicket.postMessage(null);

        } else if (browser.android) {
            if (androidJsObj && (typeof androidJsObj === 'undefined' ? 'undefined' : _typeof(androidJsObj)) ===
                'object') {
                info.uid = parseInt(window.androidJsObj.getUid());
                info.ticket = window.androidJsObj.getTicket();
                info.phone = window.androidJsObj.getUserPhoneNumber();
                console.log(["info", info]);
                if (info.uid && info.ticket) {
                    authenticationStatus();
                }
                if (info.phone) {
                    selePhone();
                }
            }
        }
    }, 10);


    // info.ticket = 'eyJhbGciOiJIUzI1NiJ9.eyJ0aWNrZXRfdHlwZSI6bnVsbCwidWlkIjoyMDAwLCJ0aWNrZXRfaWQiOiIxYTZkZjRlOS1lMWE3LTRhNDEtOTQ5OC0zZWExMWYzMGE1NjgiLCJleHAiOjM2MDAsImNsaWVudF9pZCI6ImVyYmFuLWNsaWVudCJ9.0GXBmOBschXgSSMP1YqHNg3ChZZS_6eq6jIa_x-GNr8';
    // info.uid = '2000';
    // info.phone = '';
    // selePhone();
    // authenticationStatus();
    // $('body>div').hide();
    // $('.container').show();
    // showLayer(1)


    info.dom = null;

    function isChinese(temp) {
        var re = /[^\u4e00-\u9fa5]/;
        if (re.test(temp)) return false;
        return true;
    }


    $('.sfz input').attr('type', 'email');

    function isCardNo(card) {
        // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X  
        var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
        if (reg.test(card) === false) {
            console.log("身份证输入不合法");
            return false;
        } else {
            return true;
        }
    }

    function isPhone(num) {
        var valid_rule = /^1(3|4|5|6|7|8|9)\d{9}$/; // 手机号码校验规则
        if (!valid_rule.test(num)) {
            return false;
        } else {
            return true;
        }
    }

    function selePhone() {
        if (info.phone) {
            $('.phone input').val(info.phone).attr('disabled', '');
        }
    }

    $('.failed button').click(function () {
        $('.container').show();
        $('.failed').hide();
    });

    $('.file_ .bottom').click(function () {
        console.log(1111111, $(this).find('.tu').attr("src"));
        requestImg();
        info.dom = $(this).find('.tu');
        // onImageChooserResult(0, 'images/renzheng_guohui.png');
    });

    function onImageChooserResult(e, value) {
        console.log(e, value, '图片返回');
        // if(this.dom ){
        //     this.dom.attr('src',e)
        // }
        if (browser.ios) {
            info.dom.attr('src', value)
        } else if (browser.android) {
            info.dom.attr('src', e)
        }
    };

    // 调用原生方法调起本地相册
    function requestImg() {
        if (browser.ios) {
            window.webkit.messageHandlers.requestImageChooser.postMessage(null);
        } else if (browser.android) {
            if (androidJsObj && (typeof androidJsObj === 'undefined' ? 'undefined' : _typeof(androidJsObj)) === 'object') {
                window.androidJsObj.requestImageChooser();
            }
        }
    };

    function note_() {
        var type = 0;
        var url = '/user/realname/v1/getSmsCode';
        var header = '';
        var data = {};
        data.phone = $('.phone input').val();
        console.log('note_', data)
        if (browser.ios) {
            var obj = {};
            obj.requestMethod = type;
            obj.urlController = url;
            obj.headerMapString = header;
            obj.paramMapString = data;
            window.webkit.messageHandlers.httpRequest.postMessage(JSON.stringify(obj));
            // window.webkit.messageHandlers.httpRequest.postMessage(type,url,header,JSON.stringify(data));JSON.stringify(obj)
        } else if (browser.android) {
            if (androidJsObj && (typeof androidJsObj === 'undefined' ? 'undefined' : _typeof(androidJsObj)) === 'object') {
                window.androidJsObj.httpRequest(type, url, header, JSON.stringify(data));
            }
        }
    }

    var onHttpResponse = function (e, item) {
        console.log(e, '接口返回', item)
        var body = null;
        if (browser.ios) {
            body = item;
            if (body.urlController == "/user/realname/v1/getSmsCode") {
                var bodyString = body.bodyString;
                console.log(bodyString, 'bodyString');
                if (!body.isRequestError) {
                    $('.toast').text('发送成功').show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1200);
                } else {
                    // $('.toast').text(bodyString.message || '发送失败').show();
                    // setTimeout(function () {
                    //     $('.toast').hide();
                    // }, 1200);
                    clearInterval(timeInterval);
                    $('.code .getCode').text('获取').attr('onclick', 'time_()');
                }
            }
            if (body.urlController == "/user/realname/v1/save") {
                var bodyString = body.bodyString;
                console.log(bodyString, 'bodyString');
                if (!body.isRequestError) {
                    // var bodyString = JSON.parse(body.bodyString);
                    $('.toast').text('提交成功').show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1200);
                    authenticationStatus();
                } else {
                    // $('.toast').text(bodyString.message).show();
                    // setTimeout(function () {
                    //     $('.toast').hide();
                    // }, 1200);
                    $('.submit button').attr('onclick', 'submit()');
                }
            } else if (body.urlController == "/user/realname/v1/get") {
                var response = body.bodyString;
                console.log(response, 'bodyString');
                if (!body.isRequestError) {
                    var responseData = JSON.parse(body.bodyString);
                    if (responseData) {
                        $('body>div').hide();
                        $('.container').show();
    
                        if (responseData.auditStatus == 1) {
                            $('.success .menber>div>span:eq(0) span:eq(1)').text(responseData.realName);
                            $('.success .menber>div>span:eq(1) span:eq(1)').text(responseData.phone);
                            $('.success .menber>div>span:eq(2) span:eq(1)').text(responseData.idcardNo);
                        } else {
                            $('#reason').text(responseData.remark)
                        }
    
                        info.auditStatus = (responseData.auditStatus).toString();
                        showLayer(responseData.auditStatus);
                    } else {
                        $('body>div').hide();
                        $('.container').show();
                    }
                } else {
                    $('.submit button').attr('onclick', 'submit()');
                }
            }
        } else {
            body = JSON.parse(JSON.stringify(e));
            if (body.urlController == "/user/realname/v1/getSmsCode") {
                var bodyString = JSON.parse(body.bodyString);
                console.log(bodyString, 'bodyString');
                if (!body.isRequestError) {
                    if (bodyString.code == 200) {
                        $('.toast').text('发送成功').show();
                        setTimeout(function () {
                            $('.toast').hide();
                        }, 1200);
                    } else {
                        console.log(bodyString, bodyString.message, 'bodyString.message');
                        $('.toast').text(bodyString.message).show();
                        setTimeout(function () {
                            $('.toast').hide();
                        }, 1200);
                        clearInterval(timeInterval);
                        $('.code .getCode').text('获取').attr('onclick', 'time_()');
                    }
                } else {
                    $('.toast').text('发送失败').show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1200);
                    $('.code .getCode').text('获取').attr('onclick', 'time_()');
                }
            }
            if (body.urlController == "/user/realname/v1/save") {
                if (!body.isRequestError) {
                    var bodyString = JSON.parse(body.bodyString);
                    console.log(bodyString, 'bodyString');
                    if (bodyString.code == 200) {
                        $('.toast').text('提交成功').show();
                        setTimeout(function () {
                            $('.toast').hide();
                        }, 1200);
                        authenticationStatus();
                    } else {
                        $('.toast').text(bodyString.message).show();
                        setTimeout(function () {
                            $('.toast').hide();
                        }, 1200);
                        $('.submit button').attr('onclick', 'submit()');
                    }
                } else {
                    $('.toast').text('提交失败').show();
                    setTimeout(function () {
                        $('.toast').hide();
                    }, 1200);
                    $('.submit button').attr('onclick', 'submit()');
                }
            } else if (body.urlController == "/user/realname/v1/get") {
                var response = body.bodyString;
                console.log(response, 'bodyString2');
                if (!body.isRequestError) {
                    var responseData = JSON.parse(body.bodyString);
                    if (responseData.code == 200) {
                        $('body>div').hide();
                        $('.container').show();
    
                        if (responseData.data.auditStatus == 1) {
                            $('.success .menber>div>span:eq(0) span:eq(1)').text(responseData.data.realName);
                            $('.success .menber>div>span:eq(1) span:eq(1)').text(responseData.data.phone);
                            $('.success .menber>div>span:eq(2) span:eq(1)').text(responseData.data.idcardNo);
                        } else {
                            $('#reason').text(responseData.data.remark)
                        }
    
                        info.auditStatus = (responseData.data.auditStatus).toString();
                        showLayer(responseData.data.auditStatus);
                    } else {
                        $('body>div').hide();
                        $('.container').show();
                    }
                } else {
                    $('.submit button').attr('onclick', 'submit()');
                }
            }
        }
    };

    function time_() {
        //校验手机号
        if (!$('.phone input').val()) {
            console.log("没有输入手机号码");
            $Dialog({
                content: '请输入手机号码',
                diaClass: 'diaClass',
                buttons: [{
                    className: "confirm",
                    text: "确定",
                    callback: function () {

                    }
                }]
            })
            return
        } else {
            if (!isPhone($('.phone input').val())) {
                console.log('手机号码不符合规则');
                $Dialog({
                    content: '手机号码错误',
                    diaClass: 'diaClass',
                    buttons: [{
                        className: "confirm",
                        text: "确定",
                        callback: function () {

                        }
                    }]
                })
                return
            }
        }
        $('.code .getCode').attr('onclick', '');
        var second = 60;
        $('.code .getCode').text(second);
        timeInterval = setInterval(function () {
            if (second == 1) {
                clearInterval(timeInterval);
                $('.code .getCode').text('获取').attr('onclick', 'time_()');;
                return;
            }
            second--
            $('.code .getCode').text(second);
        }, 1000);
        note_();
    }

    function tester() {
        //校验姓名
        if (!$('.name input').val()) {
            console.log("没有姓名");
            $Dialog({
                content: '请输入姓名',
                diaClass: 'diaClass',
                buttons: [{
                    className: "confirm",
                    text: "确定",
                    callback: function () {

                    }
                }]
            })
            return
        } else {
            if (!isChinese($('.name input').val())) {
                console.log('姓名不符合规则');
                $Dialog({
                    content: '姓名只能使用中文',
                    diaClass: 'diaClass',
                    buttons: [{
                        className: "confirm",
                        text: "确定",
                        callback: function () {

                        }
                    }]
                })
                return
            }
        }
        //校验身份证
        if (!$('.sfz input').val()) {
            console.log("没有输入身份证号码");
            $Dialog({
                content: '请输入身份证号码',
                diaClass: 'diaClass',
                buttons: [{
                    className: "confirm",
                    text: "确定",
                    callback: function () {

                    }
                }]
            })
            return
        } else {
            if (!isCardNo($('.sfz input').val())) {
                console.log('身份证号码不符合规则');
                $Dialog({
                    content: '身份证号码错误',
                    diaClass: 'diaClass',
                    buttons: [{
                        className: "confirm",
                        text: "确定",
                        callback: function () {

                        }
                    }]
                })
                return
            }
        }
        //校验手机号
        // if (!$('.phone input').val()) {
        //     console.log("没有输入手机号码");
        //     $Dialog({
        //         content: '请输入手机号码',
        //         diaClass: 'diaClass',
        //         buttons: [{
        //             className: "confirm",
        //             text: "确定",
        //             callback: function () {

        //             }
        //         }]
        //     })
        //     return
        // } else {
        //     if (!isPhone($('.phone input').val())) {
        //         console.log('手机号码不符合规则');
        //         $Dialog({
        //             content: '手机号码错误',
        //             diaClass: 'diaClass',
        //             buttons: [{
        //                 className: "confirm",
        //                 text: "确定",
        //                 callback: function () {

        //                 }
        //             }]
        //         })
        //         return
        //     }
        // };
        // //验证码
        // if (!$('.code input').val()) {
        //     console.log("没有输入验证码");
        //     $Dialog({
        //         content: '请输入验证码',
        //         diaClass: 'diaClass',
        //         buttons: [{
        //             className: "confirm",
        //             text: "确定",
        //             callback: function () {

        //             }
        //         }]
        //     })
        //     return
        // }
        //身份证
        function imgRap(e) {
            console.log(e, '图片')
            if (e.indexOf('http') >= 0) {
                return false;
            } else {
                return true;
            }
        }
        if (imgRap($('.file_ .bottom:eq(0) .tu').attr('src')) || imgRap($('.file_ .bottom:eq(1) .tu').attr('src'))) {
            console.log("没有上传身份证");
            $Dialog({
                content: '请上传身份证',
                diaClass: 'diaClass',
                buttons: [{
                    className: "confirm",
                    text: "确定",
                    callback: function () {

                    }
                }]
            })
            return
        }
        return true
    }

    // 提交实名认证方法
    function submit() {
        var flag = tester();
        if (flag) {
            $('.submit button').attr('onclick', '');
            var type = 1;
            var url = '/user/realname/v1/save';
            var header = '';
            var data = {
                realName: $('.name input').val(), // 真实姓名
                idcardNo: $('.sfz input').val(), // 身份证号码
                // phone: $('.phone input').val(), // 手机号码
                // smsCode: $('.code input').val(), // 手机验证码
                idcardFront: $('.file_ .bottom:eq(0) .tu').attr('src'), // 正面照
                idcardOpposite: $('.file_ .bottom:eq(1) .tu').attr('src'), // 反面照
                // idcardHandheld: $('.file_ .bottom:eq(2) .tu').attr('src'), // 手持证件照
            };
            console.log(data, 'save')
            dispatchRequest(url, type, header, data);
        }
    };

    // 调用原生方法发送请求
    function dispatchRequest(url, method, header, params) {
        var type = method;
        var url = url;
        var header = header;
        var data = params;
        if (browser.ios) {
            var obj = {
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

    $('.phone input').on('keyup', function (e) {
        if ($('.phone input').val().trim().length > 0) {
            $('.code .getCode').addClass('active');
        } else {
            $('.code .getCode').removeClass('active');
        }
    });

    // 判断显示层
    function showLayer(status) {
        $('body>div').hide();
        switch (status) {
            case 0:
                $('.submitted').show();
                break;
            case 1:
                $('.success').show();
                break;
            case 2:
                $('.failed').show();
                break;
            default:
                return;
        }
    };

    // 判断实名认证状态
    function authenticationStatus() {
        var type = 0;
        var url = '/user/realname/v1/get';
        var header = '';
        var data = {
            uid: info.uid,
            ticket: info.ticket,
        };
        console.log(data, 'get')
        dispatchRequest(url, type, header, data);
    };
// });