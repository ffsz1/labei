const browser = checkVersion();
const info = {};

function getMessage(key, value) {
  info[key] = value;
  console.log(key, value, 'getMessage');
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
    }
  }
}, 10);

$('input[name="report"]').bind('change', function (e) {
  const val = $(e.target).val();
  if (val == 9) {
    $('.report-others').css({
      "display": "block"
    });
  } else {
    $('.report-others').css({
      "display": "none"
    });
  }
  $('.submit-btn').addClass('active')
});

$('.submit-btn').click(function () {
  if (isNaN(parseInt($("input[type='radio']:checked").val())) || !$('.submit-btn').hasClass('active')) {
    $('.toast').text('请选择举报类型!').show();
    setTimeout(function () {
      $('.toast').hide();
    }, 1200);
  } else if (parseInt($("input[type='radio']:checked").val()) == 9 && $('#others').val() == '') {
    $('.toast').text('请输入举报内容!').show();
    setTimeout(function () {
      $('.toast').hide();
    }, 1200);
  } else {
    submit();
  }
});

function submit() {
  var type = 1;
  var url = '/user/report/save';
  var header = '';
  var data = {
    reportUid: info.uid,
    ticket: info.ticket,
    reportType: parseInt($("input[type='radio']:checked").val()),
    remark: $('#others').val(),
    informantsId: parseInt(getQueryString().uid),
    type: getQueryString().type,
    phoneNo: info.phone
  };
  console.log(data);
  dispatchRequest(url, type, header, data);
}

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

var onHttpResponse = function (e, item) {
  console.log(e, '接口返回', item)
  var body = null;
  if (browser.ios) {
    body = item;
    if (body.urlController == "/user/report/save") {
      var bodyString = body.bodyString;
      console.log(bodyString, 'bodyString');
      if (!body.isRequestError) {
        $('.toast').text('举报成功').show();
        setTimeout(function () {
          $('.toast').hide();
        }, 1200);
      } else {
        $('.toast').text('举报失败').show();
        setTimeout(function () {
          $('.toast').hide();
        }, 1200);
      }
      $('#others').val("");
    }
  } else {
    body = JSON.parse(JSON.stringify(e));
    if (body.urlController == "/user/report/save") {
      var bodyString = JSON.parse(body.bodyString);
      console.log(bodyString, 'bodyString');
      if (!body.isRequestError) {
        if (bodyString.code == 200) {
          $('.toast').text('举报成功').show();
          setTimeout(function () {
            $('.toast').hide();
          }, 1200);
        } else {
          console.log(bodyString, bodyString.message, 'bodyString.message');
          $('.toast').text(bodyString.message).show();
          setTimeout(function () {
            $('.toast').hide();
          }, 1200);
        }
      } else {
        $('.toast').text('举报失败').show();
        setTimeout(function () {
          $('.toast').hide();
        }, 1200);
      }
      $('#others').val("");
    }
  }
};