var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) {
  return typeof obj;
} : function (obj) {
  return obj && typeof Symbol === "function" && obj.varructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};

const browser = checkVersion();
const info = {};
let status = false;

function getMessage(key, value) {
  info[key] = value;
  console.log(key, value, 'getMessage');
}

const swiperH = new Swiper('.swiper-container', {
  direction: 'vertical',
  mousewheel: false,
  on: {
    touchMove: function (swiper, event) {
      var _viewHeight = document.getElementsByClassName('swiper-wrapper')[0].offsetHeight;
      var _contentHeight = document.getElementsByClassName('swiper-slide')[0].offsetHeight;

      if (swiperH.translate < 50 && swiperH.translate > 0) {
        $(".flush").text('下拉刷新...');
      } else if (swiperH.translate > 50) {
        $(".flush").text('释放刷新...');
      }
    },
    touchEnd: function (swiper, event) {
      if (swiperH.translate >= 50) {
        queryInfo();
      }
    },
  },
});

setTimeout(function () {
  if (browser.ios && window.webkit) {
    window.webkit.messageHandlers.getUid.postMessage(null);
    window.webkit.messageHandlers.getTicket.postMessage(null);
  } else if (browser.android) {
    if (androidJsObj && (typeof androidJsObj === 'undefined' ? 'undefined' : _typeof(androidJsObj)) ===
      'object') {
      info.uid = parseInt(window.androidJsObj.getUid());
      info.ticket = window.androidJsObj.getTicket();
      console.log(["info", info]);
    }
  }
  // queryInfo();
}, 10);

// IOS分享信息
function shareInfo() {
  var info = {
    webType: 1,
    title: '人月两团圆，双节共狂欢！拉贝星球邀您免费得装扮、金币和靓号～', // 分享标题
    showUrl: allUrl() + '/front/activity/nationalDay.html',
    imgUrl: allUrl() + '/front/activity/images/nationalDay/nationalDays.png',
    desc: '每天一点任务，收获大件礼物' // 分享描述
  };
  return JSON.stringify(info);
}

// // 分享页面
// $('.doMission').click(function () {
//   if (!status) {
//     if (browser.android) {
//       window.androidJsObj.openSharePage();
//     } else if (browser.ios) {
//       window.webkit.messageHandlers.openSharePage.postMessage(null);
//     }
//   }
// });

// // 兑换积分
// $('.bonus').click(function (e) {
//   $('.dialog-popup').show();
//   $('#text').text(`确定用${$(e.target).data().value}积分兑换${giftType($(e.target).data().id)}`);
//   $('#dialog-id').text($(e.target).data().id);
// });

function giftType(type) {
  switch (type) {
    case 1:
      return '限时头饰';
    case 2:
      return '限时座驾';
    case 3:
      return '50金币';
    case 4:
      return '商城头饰';
    case 5:
      return '商城座驾';
    case 6:
      return '30天靓号';
  }
}

// // 打开活动规则
// $('.rule-open').click(function () {
//   $('.rule-popup').show();
// });

// // 关闭活动规则
// $('.rule-close').click(function () {
//   $('.rule-popup').hide();
// });

// // 打开积分明细
// $('.details').click(function () {
//   $('.point-popup').show();
// });

// // 关闭积分明细
// $('.point-close').click(function () {
//   $('.point-popup').hide();
// });

// $('#confirm').click(function () {
//   exchange($('#dialog-id').text());
// });

// $('#cancel').click(function () {
//   $('.dialog-popup').hide();
// });

// 1.POST; 2.GET
function queryInfo() {
  const type = 2;
  const url = '/nationalDayActivity/getList';
  const header = '';
  const data = {
    uid: info.uid
  };
  dispatchRequest(url, type, header, data);
}

// 1.POST; 2.GET
function exchange(itemId) {
  const type = 1;
  const url = '/nationalDayActivity/exchange';
  const header = '';
  const data = {
    uid: info.uid,
    optionId: itemId
  };
  dispatchRequest(url, type, header, data);
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
    if (body.urlController == "/nationalDayActivity/getList") {
      const responseData = JSON.parse(body.bodyString);
      console.log(responseData, 'bodyString');
      if (!body.isRequestError) {
        $(".flush").text('刷新成功!');
        $('#one').text(responseData.task1);
        $('#two').text(responseData.task2);
        $('#three').text(responseData.task3);
        $('#four').text(responseData.task4);

        $('#one-img').attr('src', (responseData.task1Status && responseData.task1 == 3) ? './images/nationalDay/finish.png' : './images/nationalDay/go.png');
        if ((responseData.task1Status && responseData.task1 == 3)) $('#one-img').removeClass('doMission').addClass('finish');
        status = responseData.task1Status;

        $('#two-img').attr('src', (responseData.task2Status && responseData.task2 == 3) ? './images/nationalDay/finish.png' : './images/nationalDay/unfinish.png');
        if ((responseData.task2Status && responseData.task2 == 3)) $('#two-img').addClass('finish');

        $('#three-img').attr('src', (responseData.task3Status && responseData.task3 == 3) ? './images/nationalDay/finish.png' : './images/nationalDay/unfinish.png');
        if ((responseData.task3Status && responseData.task3 == 3)) $('#three-img').addClass('finish');

        $('#four-img').attr('src', (responseData.task4Status && responseData.task4 == 3) ? './images/nationalDay/finish.png' : './images/nationalDay/unfinish.png');
        if ((responseData.task4Status && responseData.task4 == 3)) $('#four-img').addClass('finish');

        $('#five-img').attr('src', (responseData.task5Status) ? './images/nationalDay/finish.png' : './images/nationalDay/unfinish.png');
        if ((responseData.task5Status)) $('#five-img').addClass('finish');

        $('#point').text(responseData.integralTotal);

        if (responseData.integralDetails.length != 0) {
          for (const item of responseData.integralDetails) {
            $('.messages').append(`<p>${item.remark}</p>`);
          }
        } else {
          $('.messages').append('<p>你还没有积分，快去完成任务获取吧~</p>');
        }
      } else {
        console.log(responseData, responseData.message, 'bodyString.message');
        $('.toast').text(responseData.message).show();
        setTimeout(function () {
          $('.toast').hide();
        }, 1200);
      }
    } else if (body.urlController == "/nationalDayActivity/exchange") {
      if (!body.isRequestError) {
        const responseData = JSON.parse(body.bodyString);
        if (responseData) {
          $('.toast').text('恭喜兑换成功，请到余额/背包查收吧!').show();
          queryInfo();
          setTimeout(function () {
            $('.toast').hide();
          }, 1200);
        } else {
          $('.toast').text(responseData.message.split(':')[1]).show();
          setTimeout(function () {
            $('.toast').hide();
          }, 1200);
        }
      } else {
        $('.toast').text('兑换失败!').show();
        setTimeout(function () {
          $('.toast').hide();
        }, 1200);
      }
      $('.dialog-popup').hide();
    }
  } else {
    body = JSON.parse(JSON.stringify(e));
    if (body.urlController == "/nationalDayActivity/getList") {
      const responseData = JSON.parse(body.bodyString);
      console.log(responseData, 'bodyString');
      if (!body.isRequestError) {
        if (responseData.code == 200) {
          $(".flush").text('刷新成功!');
          $('#one').text(responseData.data.task1);
          $('#two').text(responseData.data.task2);
          $('#three').text(responseData.data.task3);
          $('#four').text(responseData.data.task4);

          $('#one-img').attr('src', (responseData.data.task1Status && responseData.data.task1 == 3) ? './images/nationalDay/finish.png' : './images/nationalDay/go.png');
          if ((responseData.data.task1Status && responseData.data.task1 == 3)) $('#one-img').removeClass('doMission').addClass('finish');
          status = responseData.data.task1Status;

          $('#two-img').attr('src', (responseData.data.task2Status && responseData.data.task2 == 3) ? './images/nationalDay/finish.png' : './images/nationalDay/unfinish.png');
          if ((responseData.data.task2Status && responseData.data.task2 == 3)) $('#two-img').addClass('finish');

          $('#three-img').attr('src', (responseData.data.task3Status && responseData.data.task3 == 3) ? './images/nationalDay/finish.png' : './images/nationalDay/unfinish.png');
          if ((responseData.data.task3Status && responseData.data.task3 == 3)) $('#three-img').addClass('finish');

          $('#four-img').attr('src', (responseData.data.task4Status && responseData.data.task4 == 3) ? './images/nationalDay/finish.png' : './images/nationalDay/unfinish.png');
          if ((responseData.data.task4Status && responseData.data.task4 == 3)) $('#four-img').addClass('finish');

          $('#five-img').attr('src', (responseData.data.task5Status) ? './images/nationalDay/finish.png' : './images/nationalDay/unfinish.png');
          if ((responseData.data.task5Status)) $('#five-img').addClass('finish');

          $('#point').text(responseData.data.integralTotal);

          if (responseData.data.integralDetails.length != 0) {
            for (const item of responseData.data.integralDetails) {
              $('.messages').append(`<p>${item.remark}</p>`);
            }
          } else {
            $('.messages').append('<p>你还没有积分，快去完成任务获取吧~</p>');
          }
        } else {
          console.log(responseData, responseData.message, 'bodyString.message');
          $('.toast').text(responseData.message).show();
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
    } else if (body.urlController == "/nationalDayActivity/exchange") {
      if (!body.isRequestError) {
        const responseData = JSON.parse(body.bodyString);
        console.log(responseData, 'bodyString');
        if (responseData.code == 200) {
          $('.toast').text('恭喜兑换成功，请到余额/背包查收吧!').show();
          queryInfo();
          setTimeout(function () {
            $('.toast').hide();
          }, 1200);
        } else {
          $('.toast').text(responseData.message.split(':')[1]).show();
          setTimeout(function () {
            $('.toast').hide();
          }, 1200);
        }
      } else {
        $('.toast').text('兑换失败!').show();
        setTimeout(function () {
          $('.toast').hide();
        }, 1200);
      }
    }
    $('.dialog-popup').hide();
  }
};