$(function () {
    var urlData = [];
    location.search.substring(1).split('&').forEach(function (item) {
        urlData.push(item.split('=')[0]);
        urlData.push(item.split('=')[1]);
    });
    $.ajax({
        url: allUrl() + '/stat/roomFlow/getOne',
        data: {
            roomUid: urlData[1] + '='
        },
        success: function (data) {
            var dayFlow = data.data;
            var $div = $('.list');
            var everyWeek;
            var everyMonth;
            var dd = new Date();
            $.each(dayFlow, function (i) {
                var $child;
                str = dayFlow[i].date.replace(/-/g, "/");
                var date = new Date(str);
                if (i == (dayFlow.length - 1)) {
                    $child = $('<dl><a><dt><div class="date2">' + (date.getMonth() + 1) + '-' + date.getDate() + '<br /><span class="notice">' + (2000 + date.getYear() - 100) + '</span></div></dt><dd><span class="money">+' + dayFlow[i].totalGoldNum + '</span><br/><span class="notice">房间流水</span></dd></a></dl>');
                } else {
                    str1 = dayFlow[i + 1].date.replace(/-/g, "/");
                    var lastDate = new Date(str1);
                    if (i != (dayFlow.length - 1) && date.getYear() == lastDate.getYear()) {
                        $child = $('<dl><a><dt><div class="date">' + (date.getMonth() + 1) + '-' + date.getDate() + '</div></dt><dd><span class="money">+' + dayFlow[i].totalGoldNum + '</span><br/><span class="notice">房间流水</span></dd></a></dl>');
                    } else {
                        $child = $('<dl><a><dt><div class="date2">' + (date.getMonth() + 1) + '-' + date.getDate() + '<br /><span class="notice">' + (2000 + date.getYear() - 100) + '</span></div></dt><dd><span class="money">+' + dayFlow[i].totalGoldNum + '</span><br/><span class="notice">房间流水</span></dd></a></dl>')
                    }
                }
                $div.append($child);
                if (i == 0 || date.getDay() == 0) {
                    everyWeek = new Object();
                    everyWeek.from = (date.getMonth() + 1) + '月' + date.getDate() + '日';
                    everyWeek.totalGoldNum = 0;
                }
                everyWeek.totalGoldNum += dayFlow[i].totalGoldNum;
                if (date.getDay() == 1 || i == (dayFlow.length - 1)) {
                    everyWeek.to = (date.getMonth() + 1) + '月' + date.getDate() + '日';
                    var child1 = $('<dl class="sum"><dt><div class="date3">周</div></dt><dd><span class="money">+' + everyWeek.totalGoldNum + '</span><br/><span class="notice">' + everyWeek.from + '至' + everyWeek.to + ' 周房间流水</span></dd></dl>');
                    $div.append(child1);
                }
                dd.setTime(date.getTime() + 1 * 24 * 60 * 60 * 1000);
                if (i == 0 || dd.getMonth() != date.getMonth()) {
                    everyMonth = new Object();
                    everyMonth.month = date.getMonth() + 1;
                    everyMonth.totalGoldNum = 0;
                }
                everyMonth.totalGoldNum += dayFlow[i].totalGoldNum;
                if (date.getDate() == 1 || i == (dayFlow.length - 1)) {
                    var child2 = $('<dl class="sum"><dt><div class="date3">月</div></dt><dd><span class="money">+' + everyMonth.totalGoldNum + '</span><br/><span class="notice">' + everyMonth.month + '月房间流水</span></dd></dl>');
                    $div.append(child2);
                }
            });
        }
    });
    // $('.ad').click(function() {
    //     location.href = 'https://www.mjiawl.com/front/literank/index.html'
    // })
});