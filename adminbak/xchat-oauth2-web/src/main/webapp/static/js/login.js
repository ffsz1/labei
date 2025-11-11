
var REFRESH_TICKET_URL = "http://accounts.erd.yy.com/oauth/ticket";
var ACCESS_TOKEN_COOKIE_NAME = "__erd_sso_access_token";
var REFRESH_TOKEN_COOKIE_NAME = "__erd_sso_refresh_token";
var USERNAME_COOKIE_NAME = "__erd_sso_username";
var UID_COOKIE_NAME = "__erd_sso_uid";
var ticketPool = [];
var ticketPoolIndex = 0;

function isLogin(){
    var access_token = getCookie(ACCESS_TOKEN_COOKIE_NAME);
    if(access_token != null){
        return true;
    }
    return false;
}

function login(username,password,ccode){
    $.ajax( {
        url:LOGIN_URL,// 跳转到 action
        data:{
            client_id : CLIENT_ID,
            client_secret : CLIENT_SECRET,
            grant_type : "password",
            username : username,
            password: password
        },
        type:'post',
        cache:false,
        dataType:'json',
        success:function(result) {
            console.log(result);
            if(result.code ==200 ){
                setCookie(ACCESS_TOKEN_COOKIE_NAME,result.data.access_token,{expires:result.data.expires_in});
                setCookie(REFRESH_TOKEN_COOKIE_NAME,result.data.refresh_token,{expires:result.data.expires_in});
            }else{
                alert("登陆失败！");
            }
        },
        error : function() {
            alert("登陆异常！");
        }
    });
}
function logout(){
    var expire_time = (new Date().getTime() - 1);
    setCookie(ACCESS_TOKEN_COOKIE_NAME,"",{expires:expire_time});
    setCookie(REFRESH_TOKEN_COOKIE_NAME,"",{expires:expire_time});
    setCookie(USERNAME_COOKIE_NAME,"",{expires:expire_time});
    setCookie(UID_COOKIE_NAME,"",{expires:expire_time});
}
function refreshTikect(){
    var access_token = getCookie(ACCESS_TOKEN_COOKIE_NAME);
    $.ajax( {
        url:REFRESH_TICKET_URL,// 跳转到 action
        data:{
            client_id : CLIENT_ID,
            client_secret : CLIENT_SECRET,
            issue_type : "multi",
            access_token : access_token
        },
        type:'post',
        cache:false,
        dataType:'json',
        success:function(result) {
            if(result.code == 200 ){
                ticketPool = result.data.tickets;
            }else{
                alert("更新失败！");
            }
        },
        error : function() {
            alert("异常！");
        }
    });
}
function getTikect(){
    if(ticketPoolIndex < ticketPool.length){
        var ticket =  ticketPool[ticketPoolIndex].ticket;
        ticketPoolIndex++;
        console.log(ticket);
        if(ticketPoolIndex > ticketPool.length - 3){
            refreshTikect();
            ticketPoolIndex = 0;
        }
        return ticket;
    }
    return "";
}

function getCookie(cookieName){
    var cookieValue = "";
    var search = cookieName + "=";
    if(document.cookie.length > 0){
        var offset = document.cookie.indexOf(search);
        if (offset != -1){
            offset += search.length;
            var end = document.cookie.indexOf(";", offset);
            if (end == -1) end = document.cookie.length;
            cookieValue = unescape(document.cookie.substring(offset, end))
        }
    }
    return cookieValue;
}

function setCookie (key, value, options) {
    var days, time, result, decode

    // A key and value were given. Set cookie.
    if (arguments.length > 1 && String(value) !== "[object Object]") {
        // Enforce object
        options = $.extend({}, options)

        if (value === null || value === undefined) options.expires = -1

        if (typeof options.expires === 'number') {
            days = (options.expires * 1000)
            time = options.expires = new Date()

            time.setTime(time.getTime() + days)
        }

        value = String(value)

        return (document.cookie = [
            encodeURIComponent(key), '=',
            options.raw ? value : encodeURIComponent(value),
            options.expires ? '; expires=' + options.expires.toUTCString() : '',
            options.path ? '; path=' + options.path : '',
            options.domain ? '; domain=' + options.domain : '',
            options.secure ? '; secure' : ''
        ].join(''))
    }

    // Key and possibly options given, get cookie
    options = value || {}

    decode = options.raw ? function (s) { return s } : decodeURIComponent

    return (result = new RegExp('(?:^|; )' + encodeURIComponent(key) + '=([^;]*)').exec(document.cookie)) ? decode(result[1]) : null
}
