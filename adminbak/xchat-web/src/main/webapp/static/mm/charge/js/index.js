$(function () {
  var info = getQueryString();
  if (EnvCheck() == 'test') {}
  var successUrl = '';
  var browser = checkVersion();
  var chargeList = [];
  if (EnvCheck() == 'test') {
    successUrl = 'http://beta.47huyu.cn/mm/charge/success.html';
  } else {
    successUrl = 'https://www.47huyu.cn/mm/charge/success.html';
  }
  function getBaseMsg() {
    var ajaxUser = $.get('/user/get', {
      uid: info.uid,
      ticket: info.ticket
    }, function (res) {
      if (res.code !== 200) {
        $('.toast').html(res.message);
        $('.toast').show();
        setTimeout(function () {
          $('.toast').hide();
        }, 1500)
      }
    });
    var ajaxMonList = $.get('/chargeprod/list', {
      channelType: 1
    }, function (res) {
      if (res.code == 200) {
        chargeList = res.data;
      }
    });
    var ajaxUserPurse = $.get('/purse/query', {
      uid: info.uid,
      ticket: info.ticket
    });
    $.when(ajaxUser, ajaxMonList, ajaxUserPurse).done(function (ajax1, ajax2, ajax3) {
      var res1 = ajax1[0],
        res2 = ajax2[0],
        res3 = ajax3[0];
      renderUser(res1.data, res3.data);
      renderChargeList(res2.data, true);
    })
  }
  console.log()
  setTimeout(function () {
    getBaseMsg();
  }, 100)
  $('.tab-wrapper .tab').on('click', function () {
    $(this).addClass('active').siblings('.tab').removeClass('active');
    renderChargeList(chargeList);
  })
  $('.list-wrapper ul').on('click', 'li', function () {
    $(this).addClass('active').siblings('li').removeClass('active');
  })
  $('.sure').on('click', function () {
    var $actLi = $('.list-wrapper ul li.active');
    var $actTab = $('.tab-wrapper .tab.active');
    var prodId = $actLi.data('prodId'),
      channel = $actTab.data('channel');
    if (!prodId) {
      $('.toast').html('请选择充值金额');
      $('.toast').show();
      setTimeout(function () {
        $('.toast').hide();
      }, 1500)
      return;
    }

    $('#loading').show();
    $.ajax({
      url: '/charge/apply',
      dataType: 'json',
      type: 'post',
      data: {
        uid: info.uid,
        ticket: info.ticket,
        chargeProdId: prodId,
        payChannel: channel,
        successUrl: successUrl
      },
      success: function (res) {
        if (res.code == 200) {
          pingpp.createPayment(res.data, function (result, err) {
            if (result == "success") {
              // 只有微信公众号 (wx_pub)、QQ 公众号 (qpay_pub)支付成功的结果会在这里返回，其他的支付结果都会跳转到 extra 中对应的 URL
            } else if (result == "fail") {

            } else if (result == "cancel") {
              // 微信公众号支付取消支付
            }
          });
        } else {
          $('#loading').hide();
          $('.toast').html(res.message);
          $('.toast').show();
          setTimeout(function () {
            $('.toast').hide();
          }, 1500)
        }
      }
    })
  })
})

function renderUser(userData, userPurse) {
  var $user = $('.user-wrapper');
  $user.find('.user-avatar img').attr('src', userData.avatar);
  $user.find('.nick span').html(userData.nick);
  $user.find('.totalMon .left span').html(userPurse.goldNum.toFixed(2));
  if (userPurse.nobleGoldNum > 0) {
    $user.find('.mon-tips span').html(userPurse.nobleGoldNum.toFixed(2));
    $user.find('.mon-tips').show();
  }
}

function renderChargeList(chargeList, first) {
  var $ul = $('.list-wrapper ul');
  var tabIndex = $('.tab-wrapper .tab.active').index();
  $ul.html('');
  for (var i = 0; i < chargeList.length; i++) {
    // if(tabIndex == 0 && chargeList[i].money > 10000){
    //   break;
    // }
    var $li = $('<li />');
    // if (first && i == 1) {
    //   $li.addClass('active');
    // }
    $li.data('prodId', chargeList[i].chargeProdId);
    var str = '<div class="money-list"><span class="money">' + chargeList[i].prodName + '</span><span class="tag"></span></div><div class="money-real">¥' + chargeList[i].money + '</div>';
    $li.html(str);
    if (chargeList[i].prodDesc) {
      $li.find('.tag').html(chargeList[i].prodDesc);
    }
    $ul.append($li);
  }
}

function shareInfo() {

}

function showTitleRightNoticeFuck() {

}

function getMessage(key, value) {
  info[key] = value;
}
