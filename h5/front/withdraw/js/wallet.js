var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) {
  return typeof obj;
} : function (obj) {
  return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};

var browser = checkVersion();
var initData = getQueryStringArgs();
console.log('initData', decodeURI(window.location.href), getQueryStringArgs(), initData);
var info = {
  diamondNum: initData.diamondNum || 0,
  goldNum: initData.goldNum || 0,
  nickName: initData.nick || '',
  erbanNo: initData.erbanNo || '',
  uid: initData.uid,
  avatar: initData.avatar,
  openId: null,
  openId: initData.openId,
  token: initData.token,
  code: initData.codeMatch,
  redbeanNum: initData.redbeanNum || 0,
  withdrawNum: 0,
  userInfo: {},
  isBindAlipay: false,
  zfbAcc: initData.zfbAcc,
  zfbName: initData.zfbName,
  zfbSele: initData.zfbSele || 0
};

(function () {
  if (info.type == 1) {
    info.diamondNum = info.diamondNum - info.withdrawNum > 0 ? toFiexd2(info.diamondNum - info.withdrawNum) : info.diamondNum - info.withdrawNum; //减去上次提现数额
  } else if (info.type == 3) {
    info.diamondNum = info.diamondNum - info.withdrawNum > 0 ? toFiexd2(info.diamondNum - info.withdrawNum) : info.diamondNum - info.withdrawNum; //减去上次提现数额
    info.goldNum = info.goldNum + parseFloat(info.withdrawNum);
  }
  $('.avatar>img').attr('src', info.avatar);
  $('.nick').text(info.nickName);
  $('.id').text('ID: ' + info.erbanNo);
  $('.diamond').text(info.diamondNum || 0);
})();

$("#exchange-btn").click(function () {
  const url = `./exchange.html?uid=${getQueryString().uid}&phone=${getQueryString().phone}&diamondNum=${getQueryString().diamondNum}&goldNum=${getQueryString().goldNum}&token=${getQueryString().token}` //通过URL传数据
  console.log(url)
  location.replace(url);
});

//解析URL拿到参数对象
function parseQueryString(url) {
  var result = {};
  var url = decodeURI(url);
  if (url.indexOf('?') > -1) {
    var str = url.split('?')[1];
    var temp = str.split('&');
    for (var i = 0; i < temp.length; i++) {
      var temp2 = temp[i].split('=');
      result[temp2[0]] = temp2[1];
    }
  }
  return result;
}

function getQueryStringArgs() {
  //取得查询字符串并去掉开头的问号
  var qs = (location.search.length > 0 ? location.search.substring(1) : ""),
    //保存数据的对象
    args = {},
    //取得每一项
    items = qs.length ? qs.split("&") : [],
    item = null,
    name = null,
    value = null,
    //在 for 循环中使用
    i = 0,
    len = items.length;
  //逐个将每一项添加到 args 对象中
  for (i = 0; i < len; i++) {
    item = items[i].split("=");
    name = decodeURIComponent(item[0]);
    value = decodeURIComponent(item[1]);
    if (name.length) {
      args[name] = value;
    }
  }

  return args;
}