$('.type1,.type2').on('click', function() {
    var _this = $(this)
    _this.addClass('active').siblings().removeClass('active');
    if (_this.hasClass('type1')) {
        $('.con1').show().siblings('.con2').hide()
    } else {
        $('.con2').show().siblings('.con1').hide()
    }

})

$('.wechat').on('click', function() {
    $('.loginwechat').show().siblings().hide()
})
$('.ding').on('click', function() {
    $('.loginding').show().siblings().hide()
})
$('.i6').on('click', function() {
    $('.login1').show().siblings().hide()
})
var sending = false;
$('.send.active').on('click', function() {
    if (!sending) {
        sending = true;
        var _this = $(this);
        var t = 60;
        _this.removeClass('active').html(t + 's');
        var interval = setInterval(function() {
            t--;
            _this.html(t + 's');
            if (t == 0) {
                clearInterval(interval);
                _this.addClass('active').html('获取验证码');
                sending = false;
            }
        }, 1000);
    }
})