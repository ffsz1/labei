$(function() {
    var _url = location.search;
    var theRequest = new Object();
    if (_url.indexOf('?') != -1) {
        var str = _url.substr(1);
        strs = str.split('&');
        for (var i in strs) {
            theRequest[strs[i].split('=')[0]] = decodeURI(strs[i].split('=')[1]);
        }
    }
    $.ajax({
            url: '/stat/roomFlow/getRoomUidByList',
            data: {
                roomUid: theRequest.roomUid + '='
            },
            success: function(data) {
                var riliushui = data.data;
                var $div = $('.list');
                var meizhou;
                var meiyue;
                var dd = new Date();
                $.each(riliushui, function(i) {
                    var $child;
                    str = riliushui[i].date.replace(/-/g, "/");
                    var date = new Date(str);
                    if (i == (riliushui.length - 1)) {
                        $child = $('<dl><a><dt><div class="date2">' + (date.getMonth() + 1) + '-' + date.getDate() + '<br /><span class="notice">' + (2000 + date.getYear() - 100) + '</span></div></dt><dd><span class="money">+' + riliushui[i].totalGoldNum + '</span><br/><span class="notice">房间流水</span></dd></a></dl>');
                    } else {
                        str1 = riliushui[i + 1].date.replace(/-/g, "/");
                        var lastDate = new Date(str1);
                        if (i != (riliushui.length - 1) && date.getYear() == lastDate.getYear()) {
                            $child = $('<dl><a><dt><div class="date">' + (date.getMonth() + 1) + '-' + date.getDate() + '</div></dt><dd><span class="money">+' + riliushui[i].totalGoldNum + '</span><br/><span class="notice">房间流水</span></dd></a></dl>');
                        } else {
                            $child = $('<dl><a><dt><div class="date2">' + (date.getMonth() + 1) + '-' + date.getDate() + '<br /><span class="notice">' + (2000 + date.getYear() - 100) + '</span></div></dt><dd><span class="money">+' + riliushui[i].totalGoldNum + '</span><br/><span class="notice">房间流水</span></dd></a></dl>')
                        }
                    }
                    $div.append($child);
                    if (i == 0 || date.getDay() == 0) {
                        meizhou = new Object();
                        meizhou.from = (date.getMonth() + 1) + '月' + date.getDate() + '日';
                        meizhou.totalGoldNum = 0;
                    }
                    meizhou.totalGoldNum += riliushui[i].totalGoldNum;
                    if (date.getDay() == 1 || i == (riliushui.length - 1)) {
                        meizhou.to = (date.getMonth() + 1) + '月' + date.getDate() + '日';
                        var child1 = $('<dl class="sum"><dt><div class="date3">周</div></dt><dd><span class="money">+' + meizhou.totalGoldNum + '</span><br/><span class="notice">' + meizhou.from + '至' + meizhou.to + ' 周房间流水</span></dd></dl>');
                        $div.append(child1);
                    }
                    dd.setTime(date.getTime() + 1 * 24 * 60 * 60 * 1000);
                    if (i == 0 || dd.getMonth() != date.getMonth()) {
                        meiyue = new Object();
                        meiyue.month = date.getMonth() + 1;
                        meiyue.totalGoldNum = 0;
                    }
                    meiyue.totalGoldNum += riliushui[i].totalGoldNum;
                    if (date.getDate() == 1 || i == (riliushui.length - 1)) {
                        var child2 = $('<dl class="sum"><dt><div class="date3">月</div></dt><dd><span class="money">+' + meiyue.totalGoldNum + '</span><br/><span class="notice">' + meiyue.month + '月房间流水</span></dd></dl>');
                        $div.append(child2);
                    }
                })
            }
        })
        // $('.ad').click(function() {
        //     location.href = 'https://www.hulelive.com/mm/literank/index.html'
        // })
})