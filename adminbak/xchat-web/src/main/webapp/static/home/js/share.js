var url = window.location.href;

//全局变量，用于设置分享信息
var G_SHARE_INFO = {};
var share = {
    title: '拉贝，寻找最能愉悦您的声音', // 分享标题
    link: url, // 分享链接
    imgUrl: 'http://www.erbanyy.com/home/images/logo.png',
    desc: '这里有最性感的声线，来寻找最能愉悦您的快感体验吧'// 分享描述
};

//获取【点击下载按钮】
var jumpAndroid = $('#jump_android a');
var jumpIos = $('#jump_ios a');

//遮罩层和二维码
var qrcode = $('#qrcode');

var env = EnvCheck();
console.log(env);
//深度链接相关参数
var linkData = {
  type: env,  //"live"表示使用线上模式,"test"表示测试模式.【可选】,
  channel: "1",
  ios_custom_url: "https://itunes.apple.com/cn/app/id1252542069?mt=8", // 自定义iOS平台下App的下载地址，如果是AppStore的下载地址可以不用填写，【可选】
// data.android_custom_url = "";// 自定义安卓平台下App的下载地址，【可选】
};

// 获取地址后面的参数
// var locationParams = getQueryString();
// console.log(locationParams);
// if(!$.isEmptyObject(locationParams)){
//   linkData.tags = locationParams.fromWhere;
//   linkData.channel = locationParams.linkedmeChannel;
//   linkData.params = '{"uid":"'+locationParams.shareUid+'"}';
// }

//判断移动端/pc端
function device(){
  // 返回运行浏览器的操作系统平台
  var thisOS=navigator.platform;
  var flag = false;
  var os=new Array("iPhone","iPod","iPad","android","Nokia","SymbianOS","Symbian","Windows Phone","Phone","Linux armv71","MAUI","UNTRUSTED/1.0","Windows CE","BlackBerry","IEMobile");
  for(var i=0;i<os.length;i++){
    if(thisOS.match(os[i])){   
      flag = true;
    }
  }
  var check = navigator.appVersion;
  if( check.match(/linux/i) ){
    if(check.match(/mobile/i) || check.match(/X11/i)) {
      flag = true;
    }  
  }
  // Array.prototype.in_array = function(e){
  //   for(i=0;i<this.length;i++){
  //     if(this[i] == e)
  //       return true;
  //   }
  //   return false;
  // }
  return flag;
}  

/*
 * 微信、QQ分享
 */
$().ready(function () {
  var url = encodeURIComponent(location.href.split('#')[0]);
  var data ="url="+url;
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
        // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
      }
    })
});
	
wx.ready(function(){
  //分享到朋友圈
  wx.onMenuShareTimeline({
    title: share.title, // 分享标题
    link: share.link, // 分享链接
    imgUrl: share.imgUrl, // 分享图标
    success: function () {
      // 用户确认分享后执行的回调函数
    },
    cancel: function () {
      // 用户取消分享后执行的回调函数
    }
  });
  //分享给朋友
  wx.onMenuShareAppMessage({
    title: share.title, // 分享标题
    desc: share.desc, // 分享描述
    link: share.link, // 分享链接
    imgUrl: share.imgUrl, // 分享图标
    type: 'link', // 分享类型,music、video或link，不填默认为link
    dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
    success: function () {
    },
    cancel: function () {
      // 用户取消分享后执行的回调函数
    }
  });
  //分享到QQ
  wx.onMenuShareQQ({
    title: share.title, // 分享标题
    desc: share.desc, // 分享描述
    link: share.link, // 分享链接
    imgUrl: share.imgUrl, // 分享图标
    success: function () {
      // 用户确认分享后执行的回调函数
    },
    cancel: function () {
      // 用户取消分享后执行的回调函数
    }
  });
});

/*
 * linkedme_key: 每个app会分配唯一一个linkedme key，用户在linkedme官网创建app之后可以在设置菜单里面找到linkedme_key 【必选】
    data: 初始化linkedme对象参数，比如测试时需要添加data.type="test",上线时需要修改为"live",如果传null,默认为"live" 【可选】
    callback: 回调函数 【可选】
 */
linkedme.init("115e8aa7e2d130cc039d59543bfae19c", {type: env}, null);

linkedme.link(linkData, function(err, response){
  if(err){
    // 生成深度链接失败，返回错误对象err
    console.log('err:',err);
  } else {
    if (!device()) {
      jumpAndroid.attr("href","javascript:void(0)");
      jumpIos.attr("href","javascript:void(0)");
      $('#qrcode').qrcode({
        render: "canvas",
        text: response.url,
        width: "150",               //二维码的宽度
        height: "150",              //二维码的高度
        background : "#fff",       //二维码的后景色
        foreground : "#000",        //二维码的前景色
        src: 'home/images/logo.png'//二维码中间的图片
      });
      $('#qrcode').show();  
    } else {
      jumpAndroid.attr("href",response.url);
      jumpIos.attr("href",response.url);
      console.log(jumpAndroid.attr('href'));
    }
  }
},false);

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



