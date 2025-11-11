function fetch(options) {
    let header = {
        "Content-Type": "application/json"
    };
    return new Promise((resolve, reject) => {
        const instance = axios.create({
            //instance创建一个axios实例，可以自定义配置，可在 axios文档中查看详情
            //所有的请求都会带上这些配置，比如全局都要用的身份信息等。
            headers: header,
            timeout: 5 * 1000 // 30秒超时
        });

        instance(options)
            .then(response => {
                resolve(response);
            })
            .catch(error => {
                console.log("请求异常信息：" + error);
                reject(error);
            });
    });
}
const formalUrl = "https://www.yingyingyuyin.com"
const formalUrl1 = "https://www.yingyingyuyin.com"
const baseUrl = "http://39.108.183.8:8085"
    //获取房间信息
function apiGetUser(val) {
    return fetch({
        url: formalUrl + '/user/get',
        method: 'get',
        params: val
    })
}
//手机信息
function checkVersion() {
    var u = navigator.userAgent,
        app = navigator.appVersion;
    return {
        trident: u.indexOf('Trident') > -1, //IE内核
        presto: u.indexOf('Presto') > -1, //opera内核
        webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
        gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
        mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
        ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
        android: u.indexOf('Android') > -1 || u.indexOf('Adr') > -1, //android终端
        iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
        iPad: u.indexOf('iPad') > -1, //是否iPad
        webApp: u.indexOf('Safari') == -1, //是否web应该程序，没有头部与底部
        weixin: u.indexOf('MicroMessenger') > -1, //是否微信
        qq: u.match(/\sQQ/i) == " qq", //是否QQ
        app: u.indexOf('miaomiaoApp') > -1 //是否在app内
    };
}