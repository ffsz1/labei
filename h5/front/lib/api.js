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
        instance.interceptors.request.use((config) => {
            if (config.method == 'post') {
                config.headers = {
                    "Content-Type": "application/x-www-form-urlencoded"
                }
            }
            return config
        })
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
const formalUrl = "域名地址"
const formalUrl1 = "域名地址"
const baseUrl = '域名地址'
    //获取房间信息
function allrankGeth5(val) {
    return fetch({
        url: baseUrl + '/allrank/geth5',
        method: 'get',
        params: val
    })
};
function allRankRoom(val) {
    return fetch({
        url: baseUrl + '/roomctrb/queryByType',
        method: 'get',
        params: val
    })
}
//获取青少年接口信息
function getUsersTeensMode(val) {
    return fetch({
        url: baseUrl + '/users/teens/mode/getUsersTeensMode',
        method: 'get',
        params: val
    })
}
//设置青少年模式密码
function teensModeSave(val) {
    return fetch({
        url: baseUrl + '/users/teens/mode/save',
        method: 'post',
        params: val
    })
}
//关闭青少年模式密码
function closeTeensMode(val) {
    return fetch({
        url: baseUrl + '/users/teens/mode/closeTeensMode',
        method: 'post',
        params: val
    })
}
//校验青少年模式密码
function checkCipherCode(val) {
    return fetch({
        url: baseUrl + '/users/teens/mode/checkCipherCode',
        method: 'get',
        params: val
    })
}
//邀请好友
function invitedetail(val) {
    return fetch({
        url: baseUrl + '/statpacket/invitedetail',
        method: 'get',
        params: val
    })
}
//更新邀请码
function updateCode(val) {
    return fetch({
        url: baseUrl + '/user/update',
        method: 'post',
        data: qs.stringify(val)
    })
}
//检查手机类型
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