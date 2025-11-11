var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) {
    return typeof obj;
} : function (obj) {
    return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};
var share = {
    title: '会上瘾的拉贝语音！', // 分享标题
    link: window.location.href, // 分享链接
    imgUrl: allUrl() + '/home/images/logo.png',
    desc: '拉贝语音交友速配处CP，你想要的我们都有。' // 分享描述
};

function shareInfo() {
    var _url = allUrl() + '/mm/download/download.html';
    var info = {
        title: '会上瘾的拉贝语音！', // 分享标题
        showUrl: _url,
        imgUrl: allUrl() + '/home/images/logo.png',
        desc: '拉贝语音交友速配处CP，你想要的我们都有。' // 分享描述
    };
    return JSON.stringify(info);
}
var browser = checkVersion();
var info = {};
if (browser.app) {
    if (browser.ios) {
        var getMessage = function getMessage(key, value) {
            info[key] = value;
        };
        window.webkit.messageHandlers.getUid.postMessage(null);
    } else if (browser.android) {
        if (androidJsObj && (typeof androidJsObj === 'undefined' ? 'undefined' : _typeof(androidJsObj)) === 'object') {
            info.uid = parseInt(window.androidJsObj.getUid());
            try{
                window.androidJsObj.showShareButton(true)
            }
            catch(err){
                
            }
        }
    }
}
var refreshWeb = function refreshWeb() {
    location.reload();
    return '';
 };
setTimeout(()=>{
    var mySwiper = new Swiper('.swiper-container', {
        direction: 'vertical',
        loop: true,
        autoplay: true,
        observer: true,
        observeParents: true
    });
    $.ajax({
        url:allUrl() + '/draw/list',
        success:res=>{
            if(res.code === 200){
                let html = '';
                res.data.forEach(item => {
                    html += `
                    <div class="swiper-slide">
                        <p>恭喜</p>
                        <p>${item.userVo.nick}</p>
                        <p>获得</p>
                        <p>${item.drawPrizeName}</p>
                    </div>
                    `
                });
                $('#list-item').html(html)
            }
        }
    })
    $.ajax({
        type: 'get',
        url: allUrl() + '/draw/get',
        data: {
            uid: info.uid
        },
        success: function success(res) {
            if (res.code === 200) {
                $('#leftDrawNum-text').html(res.data.leftDrawNum + '次');
            }
        }
    });
    arrow = function arrow() {
        $('.arrow').removeAttr('onclick');
        $.ajax({
            type: 'get',
            url: allUrl() + '/draw/do',
            // url: 'http://beta.47huyu.cn/draw/do',
            data: {
                uid: info.uid
            },
            success: function success(res) {
                if (res.code === 200) {
                    var deg = 0;
                    $('#leftDrawNum-text').html(res.data.leftDrawNum + '次');
                    $('.tips-text').html("<p>\u606D\u559C\u60A8\u62BD\u4E2D</p><p>" + res.data.drawPrizeName + "</p>");
                    switch (res.data.drawPrizeId) {
                        case 0:
                            deg = 1368;
                            break;
                        case 8:
                            deg = 1152;
                 
                            break;
                        case 50:
                            deg = 1188;
                        
                            break;
                        case 100:
                            deg = 360 - 216 + 1080;
                   
                            break;
                        case 300:
                            deg = 360 - 144 + 1080;
                    
                            break;
                        case 1000:
                            deg = 360 - 324 + 1080;
                  
                            break;
                        case 3000:
                            deg = 360 - 36 + 1080;
            
                            break;
                        case 8888:
                            deg = 360 - 0 + 1080;
    
                            break;
                        case 10000:
                            deg = 360 - 180 + 1080;

                            break;
                        default:
                            break;
                    }
                    var timer = setInterval(function () {
                        switch ($('.turnable1').css('display')) {
                            case 'block':
                                $('.turnable1').hide();
                                $('.turnable2').show();
                                break;
                            case 'none':
                                $('.turnable1').show();
                                $('.turnable2').hide();
                                break;
                            default:
                                break;
                        }
                    }, 100);
                    $('.turnble-content').animate({ "-webkit-transform": "rotate(" + deg + "deg)" }, 3500, 'swing', function () {
                        clearInterval(timer);
                        if(res.data.drawPrizeId === 0){ 
                            $('#lose').show();
                        }else{
                            $('.prize-name').html(res.data.drawPrizeName);
                            $('#prize').show();
                        }
                        $('.arrow').attr('onclick','arrow()');
                        $('.turnble-content').css('transform','')
                    });
                } else if (res.code === 9000) {
                    $('.no-chance').html(res.message);
                    $('.no-chance').show();
                    setTimeout(function(){
                        $('.no-chance').hide();
                        $('.arrow').attr('onclick','arrow()');
                    },1500)
                }
            }
        });
    };
    toCharge = ()=>{
        if (browser.app) {
            if (browser.android) {
                window.androidJsObj.openChargePage();
            } else if (browser.ios) {
                window.webkit.messageHandlers.openChargePage.postMessage(null);
            }
        }
    }
    $('.clo').click(()=>{
        $('.mask').hide();
        $('.rule-tips').hide();      
    })
    $('.mask').click(()=>{
        $('.mask').hide();       
    })
    showRule = ()=>{
        $('.rule-tips').show();
    }
},50)
