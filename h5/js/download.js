 var config = {
     share: {
         title: '亲，我在拉贝星球等你哦',
         desc: '想要遇见喜欢的声音，就来拉贝星球，让我们不见不散。'
     }
 }
 var share = {
     title: config.share.title, // 分享标题
     link: window.location.href, // 分享链接
     imgUrl: allUrl() + '/home/images/logo.png',
     desc: config.share.desc // 分享描述
 };

 function shareInfo() {
     var _url = allUrl() + '/front/download/download.html';
     var info = {
         title: config.share.title, // 分享标题
         showUrl: _url,
         imgUrl: allUrl() + '/home/images/logo.png',
         desc: config.share.desc // 分享描述
     };
     return JSON.stringify(info);
 }

 function isRealNum(val) {
     if (val === "" || val == null) {
         return false;
     }
     if (!isNaN(val)) {
         return true;
     } else {
         return false;
     }
 }
 var browser = checkVersion();
 var urlData = new Array;
 var info = {};
 location.search.substring(1).split('&').forEach(function (item) {
     urlData.push(item.split('=')[0])
     urlData.push(item.split('=')[1])
 });


 linkedme.init("f93be285508646e4fdf55d563d4396f4", {
     type: "live"
 }, null);
 var data = {};
 data.type = "live";
 if (!isRealNum(Number(urlData[3]))) {
     urlData[3] = '';
 }
 data.params = '{"roomuid":"' + urlData[3] + '","uid":"' + urlData[1] + '","type":"' + 2 + '"}'
 linkedme.link(data, function (err, response) {
     if (err) {} else {
         info.url = response.url
     }
 }, false);

 $('.download').click(function(){
    location.href = info.url
 })