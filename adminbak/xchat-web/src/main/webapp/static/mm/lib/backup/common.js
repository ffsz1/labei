function render (templateId, templateData, target) {
  var html = template(templateId, templateData);
  target.innerHTML += html;
}

function dateFormat (date, fmt) {
  date = new Date(date);
  var o = {
    'M+': date.getMonth() + 1,
    'd+': date.getDate(),
    'h+': date.getHours(),
    'm+': date.getMinutes(),
    's+': date.getSeconds()
  };

  // 补全0
  function padLeftZero(str) {
    return('00' + str).substr(str.length);
  }

  // 年份
  if(/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
  }

  // 月日时分秒
  for(var k in o) {
    if(new RegExp('(' + k + ')').test(fmt)) {
      var str = o[k] + '';
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : padLeftZero(str));
    }
  }

  date = o = padLeftZero = null;
  return fmt;
}

function convert (_url) {
	var patt=/\d+/;
	var num=_url.match(patt);
	var rs={};
	rs.uid=num[0];
	return rs;	
}

// 传递分享信息给客户端,showUrl为分享的页面链接,为空时表示不分享
// function shareInfo () {
//   var _url = 'http://www.erbanyy.com/modules/bonus/fight.html';
//   var res = EnvCheck();
//   if (res == 'test'){
//     _url = 'http://beta.erbanyy.com/modules/bonus/fight.html';
//   }
//   var info = {
//     title: '耳伴与你一起红',
//     imgUrl: 'http://www.erbanyy.com/home/images/logo.png',
//     desc: '登录即送20红包，每天还有分享红包，邀请红包，分成红包，四重红包大礼等你来拿',
//     showUrl: _url
//   };
//   return JSON.stringify(info);
// }

// 根据域名适配环境
function EnvCheck() {
  if(window.location.href){
    var _url = window.location.href;
    var res = _url.match(/beta/);
    if(res){
      return 'test';
    }else{
      return 'live';
    }
  }
}

// 获取地址栏参数
function getQueryString(){
  var _url = location.search;
  var theRequest = new Object();
  if(_url.indexOf('?') != -1){
    var str = _url.substr(1);
    strs = str.split('&');
    for(var i in strs){
      theRequest[strs[i].split('=')[0]] = decodeURI(strs[i].split('=')[1]);
    }
  }
  return theRequest;
}

// 判断浏览器内核，手机类型
function checkVersion(){
  var u = navigator.userAgent, app = navigator.appVersion;
  return {
    trident: u.indexOf('Trident') > -1, //IE内核
    presto: u.indexOf('Presto') > -1, //opera内核
    webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
    gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1,//火狐内核
    mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
    ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
    android: u.indexOf('Android') > -1 || u.indexOf('Adr') > -1, //android终端
    iPhone: u.indexOf('iPhone') > -1 , //是否为iPhone或者QQHD浏览器
    iPad: u.indexOf('iPad') > -1, //是否iPad
    webApp: u.indexOf('Safari') == -1, //是否web应该程序，没有头部与底部
    weixin: u.indexOf('MicroMessenger') > -1, //是否微信
    qq: u.match(/\sQQ/i) == " qq", //是否QQ
    app: u.indexOf('erbanApp') > -1 //是否在app内
  };
}

// 图片预加载
function preloadImage(obj){
  var loadLength = 0,newImages = [];
  for(var i = 0;i < obj.imageArr.length;i++){
    newImages[i] = new Image();
    newImages[i].src = obj.imageArr[i];
    newImages[i].onload = newImages[i].onerror = function(){
      loadLength++;
      typeof obj.preloadPreFunc === 'function' && obj.preloadPreFunc(loadLength);
      if(loadLength == obj.imageArr.length){
        typeof obj.doneFunc === 'function' && obj.doneFunc();
      }
    }
  }
}

// 判断是否在App内
function isApp() {
  var androidBol = false;
  var osBol = false;
  if(window.androidJsObj && typeof window.androidJsObj === 'object'){
    androidBol = true;
  }
  if(window.webkit){
    console.log(window.webkit);
    osBol = true;
  }
  return (androidBol || osBol);

}

function erbanMask(channel,tags,params) {
  //此函数用于一般的耳伴底层面罩
  var browser = checkVersion();
  var env = EnvCheck();
  params = params? params:0;
  if(!browser.app){
    $('#mask').css('display','flex');
    var linkData = {
      type: env,
      channel: channel,
      tags: tags,
      ios_custom_url: "https://itunes.apple.com/cn/app/id1252542069?mt=8",
      params: '{"uid":"' + params + '"}'
    };

    linkedme.init("115e8aa7e2d130cc039d59543bfae19c", {type: env}, null);

    linkedme.link(linkData, function(err, response){
      if(err){
        // 生成深度链接失败，返回错误对象err
        console.log('err:',err);
      } else {
        console.log(response);
        $('#download a').attr("href",response.url);
      }
    },false);
  }else{
    $('#mask').hide();
  }
}
function wxConfig() {
  var wxurl = encodeURIComponent(location.href.split('#')[0]);
  var data ="url=" + wxurl;
  console.log(data);
  $.ajax({
    type:'GET',
    url: '/wx/config',
    data: data,
    asyc: true,
    success: function (data) {
      if(data.code = 200){
        wx.config({
          debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
          appId: 'wx009d793f92c24eec', // 必填，公众号的唯一标识
          timestamp: data.data.timestamp, // 必填，生成签名的时间戳
          nonceStr: data.data.nonceStr, // 必填，生成签名的随机串
          signature: data.data.signature,// 必填，签名，见附录1
          jsApiList: data.data.jsApiList // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        });
        wx.error(function(res){
          console.log('config error,msg:'+res);
        });
      }
    },
    error:function(res){
      console.log('config error,msg:'+res);
    }
  })
}

function refreshWeb() {
  window.location.href = window.location.href;
}

function shareInfo(urlMsg) {
  if(urlMsg){
    var env = EnvCheck();
    if(env == 'test'){
      return 'http://beta.erbanyy.com/' + urlMsg;
    }else{
      return 'https://www.erbanyy.com/' + urlMsg;
    }
  }
}
