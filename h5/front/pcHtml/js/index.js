var item = new Object();
var _url = location.search;
if (_url.indexOf('?') != -1) {
    var str = _url.substr(1);
    if (_url.indexOf('link') != -1) {
        console.log(3212);
        strs = str.split('&');
        for (var i in strs) {
            item[strs[i].split('=')[0]] = decodeURI(strs[i].split('=')[1]);
        }
        $('.meijie').hide();
    } else {
        strs = str.split('&');
        for (var i in strs) {
            item[strs[i].split('=')[0]] = decodeURI(strs[i].split('=')[1]);
        }
        // $('.meijie').hide();
    }
}
item.platform = '';
item.gender = 0;
$('.form_datetime1').datetimepicker({
    minView: "month", //选择日期后，不会再跳转去选择时分秒 
    format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
    language: 'zh-CN', //汉化 
    autoclose: true //选择日期后自动关闭 
});
$('.form_datetime2').datetimepicker({
    minView: "month", //选择日期后，不会再跳转去选择时分秒 
    format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
    language: 'zh-CN', //汉化 
    autoclose: true //选择日期后自动关闭 
});
var time = +new Date(timestampToTime(new Date()))
$('.form_datetime1').datetimepicker('setStartDate', '2018-01-01');
$('.form_datetime2').datetimepicker('setStartDate', '2018-01-01');
$('.form_datetime1').datetimepicker('setEndDate', new Date());
$('.form_datetime2').datetimepicker('setEndDate', new Date());
var val1 = time - 30 * 24 * 60 * 60 * 1000;
var val2 = time;
console.log(timestampToTime(val1), timestampToTime(val2), time);
item.val1 = val1;
item.val2 = val2;
$('.inp_1').val(timestampToTime(val1));
$('.inp_2').val(timestampToTime(val2));
$('.form_datetime1').datetimepicker().on('changeDate', function(ev) {
    // if (ev.date.valueOf() < date - start - display.valueOf()) {
    if (item.val2 < ev.date.valueOf()) {
        console.log(222);
        alert('开始时间不能大于结束时间！');
        setTimeout(function() {
            $('.inp_1').val(timestampToTime(item.val1));
        }, 0)
        return false;
    }
    console.log(111, ev);
    item.val1 = ev.date.valueOf();
    // }
});
$('.form_datetime2').datetimepicker().on('changeDate', function(ev) {
    // if (ev.date.valueOf() < date - start - display.valueOf()) {
    console.log(111, ev);
    if (item.val1 > ev.date.valueOf()) {
        console.log(item.val1, ev.date.valueOf());
        alert('开始时间不能大于结束时间！');
        setTimeout(function() {
            $('.inp_2').val(timestampToTime(item.val2));
        }, 0)
        return false;
    }
    item.val2 = ev.date.valueOf();
    // }
});
//格式化时间 
function timestampToTime(timestamp) {
    var date = new Date(timestamp); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    var D = date.getDate() + ' ';
    var h = date.getHours() + ':';
    var m = date.getMinutes() + ':';
    var s = date.getSeconds();
    return Y + M + D;
}

function postlink() {
    var data = {};
    data.groupId = item.groupId + '=';
    $.ajax({
        type: 'post',
        url: allUrl() + '/outPut/values/getChannelList',
        data: data,
        asyc: true,
        success: function(res) {
            console.log(res);
            if (res.code = 200) {
                for (var i = 0; i < res.data.length; i++) {
                    var li_ = '<li><a href="#" data-item="">' + res.data[i].channel + '</a></li>';
                    $('.dropdown-menu3').append(li_);
                }
            }
        }
    });
}
postlink();

function postData() {
    var data = {};
    data.showType = 2;
    data.signBegin = timestampToTime(item.val1);
    data.signEnd = timestampToTime(item.val2);
    data.groupId = item.groupId + '=';
    if (item.platform) {
        data.os = item.platform;
    }
    if (item.gender) {
        data.gender = item.gender;
    }
    if (item.link) {
        data.medium = item.link;
    } else {
        if (item.linkedmeChannel) {
            data.linkedmeChannel = item.linkedmeChannel + '=';
        }
    }
    $.ajax({
        type: 'post',
        // type: 'get',
        url: allUrl() + '/outPut/values/getList',
        // url: 'http://10.0.0.204:3000/doDrow',
        data: data,
        asyc: true,
        success: function(res) {
            console.log(res);
            if (res.code = 200) {
                var box = $('.box_div2');
                $('.box_div2').html('');
                var item = res.data.list;
                var dom_ = null;
                $('.tj>div:eq(0) span:eq(1)').text(res.data.registerNum);
                $('.tj>div:eq(1) span:eq(1)').text(res.data.chargeUserNum);
                $('.tj>div:eq(2) span:eq(1)').text(res.data.totalAmount);
                for (var i in item) {
                    console.log(item[i])
                    dom_ = $('<div class="row row-data">');
                    dom_.append($('<div class="col-xs-2 col-sm-1 dataTime">').text(item[i].signDate)).append($('<div class="col-xs-2 col-sm-1">').text(item[i].signNum)).append($('<div class="col-xs-2 col-sm-1">').text(item[i].firstNum)).append($('<div class="col-xs-2 col-sm-1">').text(item[i].secondNum)).append($('<div class="col-xs-2 col-sm-1">').text(item[i].thirdNum)).append($('<div class="col-xs-2 col-sm-1">').text(item[i].fourthNum));
                    dom_.append($('<div class="col-xs-2 col-sm-1">').text(item[i].fifthNum)).append($('<div class="col-xs-2 col-sm-1">').text(item[i].sixthNum)).append($('<div class="col-xs-2 col-sm-1">').text(item[i].seventhNum)).append($('<div class="col-xs-2 col-sm-1">').text(item[i].eighthNum)).append($('<div class="col-xs-2 col-sm-1">').text(item[i].ninthNum)).append($('<div class="col-xs-2 col-sm-1">').text(item[i].tenthNum));
                    dom_.append($('<div class="col-xs-2 col-sm-1">').text(item[i].eleventhNum)).append($('<div class="col-xs-2 col-sm-1">').text(item[i].twelfthNum)).append($('<div class="col-xs-2 col-sm-1">').text(item[i].thirteenthNum)).append($('<div class="col-xs-2 col-sm-1">').text(item[i].fourteenthNum)).append($('<div class="col-xs-2 col-sm-1">').text(item[i].sumNum));
                    box.append(dom_);
                }

            }
        }
    });
}
postData();


$('.box').on('click', '.row-data>div', function(e) {
    console.log($(this).text(), $(this).index())
    console.log($(this).parent().find('.dataTime').text())
    if ($(this).index() == 1) {
        peopleNum($(this).parent().find('.dataTime').text())
    }
    if ($(this).index() > 1 && $(this).index() < 16) {
        console.log('1111');
        dateNum($(this).parent().find('.dataTime').text(), $(this).index() - 1)
    }
})
$('.box').click();

function peopleNum(e) {
    var data = {};
    data.groupId = item.groupId + '=';
    if (item.linkedmeChannel) {
        data.linkedmeChannel = item.linkedmeChannel + '=';
    }
    data.pageNum = 1;
    data.date = e;
    $.ajax({
        type: 'post',
        // type: 'get',
        url: allUrl() + '/outPut/values/listRegister',
        // url: 'http://10.0.0.204:3000/doDrow',
        data: data,
        success: function(res) {
            console.log(res);
            if (res.code = 200) {
                $('.container-date').show();
                var box = $('.con_box');
                $('.con_box').html('');
                var item = res.data;
                var dom_ = null;
                for (var i in item) {
                    // console.log(item[i])
                    dom_ = $('<div>');
                    dom_.append($('<img src="' + item[i].avatar + '">')).append($('<div class="right_date">' + '<span>' + item[i].nick + '</span><span>' + item[i].erban_no + '</span>' + '</div>'));
                    box.append(dom_);
                }
                $('.con_box').show();
            }
        }
    });
}
// peopleNum();


function dateNum(date, type) {
    var data = {};
    data.groupId = item.groupId + '=';
    if (item.linkedmeChannel) {
        data.linkedmeChannel = item.linkedmeChannel + '=';
    }
    data.pageNum = 1;
    data.date = date;
    data.days = type;
    $.ajax({
        type: 'post',
        // type: 'get',
        url: allUrl() + '/outPut/values/listCharge',
        // url: 'http://10.0.0.204:3000/doDrow',
        data: data,
        success: function(res) {
            console.log(res);
            if (res.code = 200) {
                $('.container-date').show();
                var box = $('.con_box_1');
                $('.con_box_1').html('');
                var item = res.data.recordList;
                var dom_ = null;
                box.append($('<div class="title">' + '<span>充值金额 : ' + (res.data.totalAmount || 0) + '</span>' + '<span>充值人数 : ' + res.data.totalUser + '</span>' + '</div>'))
                dom_ = $('<div class="bzx bzx_tit">');
                dom_.append($('<div class="left">' + '<span>' + '昵称' + '</span><span>' + '拉贝号' + '</span>' + '</div>')).append($('<div class="right_date">' + '<span>' + '充值金额' + '</span><span>' + '充值时间' + '</span>' + '</div>'));
                box.append(dom_);
                for (var i in item) {
                    // console.log(item[i])
                    dom_ = $('<div class="bzx">');
                    dom_.append($('<div class="left">' + '<span>' + item[i].nick + '</span><span>' + item[i].erban_no + '</span>' + '</div>')).append($('<div class="right_date">' + '<span>' + item[i].amount + '</span><span>' + item[i].create_time.slice(0, 10) + '</span>' + '</div>'));
                    box.append(dom_);
                }
                $('.con_box_1').show();
            }
        }
    });
}
// dateNum()
$('.con-mask').on('click', function() {
    $('.container-date').hide();
    $('.con_box_1').hide();
    $('.con_box').hide();
})
$('.dropdown-menu1 li a').dropdown().on('click', function(e) {
    console.log($(this).text());
    item.platform = $(this).attr('data-item');
    $('#dLabel_1').text($(this).text()).append('<span class="caret"></span>');
})
$('.dropdown-menu2 li a').dropdown().on('click', function(e) {
    console.log($(this).attr('data-item'));
    item.gender = $(this).attr('data-item');
    $('#dLabel_2').text($(this).text()).append('<span class="caret"></span>');
})
$('.dropdown-menu3').dropdown().on('click', 'li a', function(e) {
    console.log($(this).text());
    if ($(this).text() == '全部') {
        item.link = '';
    } else {
        item.link = $(this).text();
    }
    $('#dLabel_3').text($(this).text()).append('<span class="caret"></span>');
})