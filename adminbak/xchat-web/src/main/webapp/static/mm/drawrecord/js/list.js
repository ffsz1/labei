$(function () {
    this.page = 1;
    var _url = location.search;
    var theRequest = new Object();
    if (_url.indexOf('?') != -1) {
        var str = _url.substr(1);
        strs = str.split('&');
        for (var i in strs) {
            theRequest[strs[i].split('=')[0]] = decodeURI(strs[i].split('=')[1]);
        }
    }
    getData(1);

    function getData(page) {
        $.ajax({
            url: '/stat/roomFlow/getRoomIdByDetail',
            data: {
                roomUid: theRequest.roomUid + '=',
                current: page,
                date:theRequest.date
            },
            success: function (res) {
                if (res.code === 200) {
                    this.page++;
                    var html = '';
                    res.data.forEach(function (item) {
                        html += `
                            <li>
                                <span class="nick-name">
                                <p>${item.nick}</p>
                                </span>
                                <span class="money">
                                <p>${item.totalGoldNum}</p>
                                <p>${item.frequency}次</p>
                                </span>
                                <span class="time">${item.date}</span>
                            </li>
                        `
                    });
                    $('.container').html($('.container').html() + html)
                }
            }
        })
    }

    $(window).scroll(function () {
        if ($(window).scrollTop() + $(window).height() == $('.container').height()) {
            getData(this.page)
        }
    });
})
