/**
 * 提示框弹出
 * 结构
 * 基于jquery 或者 zepto,请与该文件前引入jquery.js或者zepto.js
// 使用实例
// $Dialog({
//     title: '',
//     content: '',
//     diaClass:'',
//     buttons: [{
//         className: "",
//         text: "确定",
//         callback: function() {

//         }
//     }, {
//         className: "color-bold",
//         text: "取消",
//         callback: function() {

//         }
//     }]
// })
/**
 * Dialog 
 * 2019/01/23 曾智勇
 */
var Dialog = function(obj) {

    /**
     * 初始化
     * diaClass  大盒子样式
     */
    this.config = {
        title: "",
        content: '',
        mask: false,
        buttons: [{
            className: "dialog-btn-cenfirm",
            text: "确定",
            callback: function() {

            }
        }]
    };
    //合并对象 ，object.assign 有兼容问题，不可用
    for (var i in obj) {
        this.config[i] = obj[i] || this.config[i]
    }
    this.body = $("body");
    this.dialogs = $("<div id='dialog'>");
    this.mask = $('<div class="dialog-mask">');
    this.dialogBox = $('<div class="dialog-box"></div>');
    this.dialogBox_ = $('<div class="dialog-box_"></div>');
    this.config.diaClass?this.dialogBox_.addClass(this.config.diaClass):'';
    this.dialogContainer = $('<div class="dialog-container">');
    this.containerTitle = $('<div class="dialog-container-title">');
    this.containerContent = $('<div class="dialog-container-content">');
    this.dialogBtn = $('<div class="dialog-btn">');

}

Dialog.prototype = {

    /**
     * 处理依赖
     */
    init: function(obj) {
        var _this_ = this
        if (this.config.title) {
            this.containerTitle.text(this.config.title)
            this.dialogContainer.append(this.containerTitle)
        }
        if (this.config.content) {
            this.containerContent.html(this.config.content)
            this.dialogContainer.append(this.containerContent)
        }
        this.dialogBox_.append(this.dialogContainer)
        if (this.config.buttons) {
            this.creatButton(this.dialogBtn, this.config.buttons);
            this.dialogBox_.append(this.dialogBtn);
        }
        this.dialogBox.append(this.dialogBox_)
        this.dialogs.append(this.dialogBox)
        if (this.config.mask) {
            this.mask.on('touchend', function() {
                _this_.close();
            });
        }
        this.dialogs.append(this.mask)
        this.body.append(this.dialogs)
    },

    /**
     * 创建按钮
     */
    creatButton: function(footer, buttons) {
        var _this_ = this;
        // if (buttons.length) {
        // }
        //遍历出数组
        $(buttons).each(function(index, element, item) {
            var className = element.className ? element.className : "";
            var text = element.text ? element.text : "button" + index;
            var callback = element.callback ? element.callback : null;
            var singleButton = $("<button>");
            singleButton.addClass(className).text(text)
                //如果有回调函数，按钮绑定回调函数
            if (callback) {
                singleButton.on('click', function(e) {
                    e.stopPropagation();
                    callback();
                    console.log('text: ' + text)
                    _this_.close();
                });
            }
            //否则默认为关闭弹出框
            else {
                singleButton.on('click', function(e) {
                    e.stopPropagation();
                    console.log('默认关闭')
                    _this_.close();
                });
            }
            footer.append(singleButton);
        });
    },

    /**
     * 移除弹框
     */
    close: function() {
        console.log('close')
        document.body.style.overflow = ''; //出现滚动条
        document.removeEventListener("touchmove", mo, false);
        this.dialogs.remove();
    },
}

window.$Dialog=function $Dialog(obj) {
    var dialog = new Dialog(obj);
    dialog.init(obj);
}

// 禁止默认事件
var mo = function(e) {
    e.preventDefault();
    e.stopPropagation();
};


/**
 * loading 
 * 2019/01/23 曾智勇
 */
var lo_time = null;
var lo_setTime =null;
window.loading = function loading(time) {
    clearInterval(lo_time);
    clearTimeout(lo_setTime);
    lo_time=null;
    lo_setTime=null;
    var i = 1;
    if($('.loading').length){
        $('.loading').append($('<img class="loading-img">'));
    }else{
        $("body").append('<div class="loading">');
        $('.loading').append($('<img class="loading-img">'));
    }
    $('.loading .loading-img').attr('src', 'https://www.tiannb.com/front/lib/toolKit/images/loading/loading.gif');
    // lo_time = setInterval(function() {
    //     if (i < 1) { i = 1 };
    //     if (i == 12) { i = 1 };
    //     i++;
    //     //线下起服务
    //     // $('.loading .loading-img').attr('src', '../../lib/loading/images/sync_anima_progress_' + i + '.png');
    //     //线上或file打开
	// 	console.log(i)
    //     $('.loading .loading-img').attr('src', 'https://www.tiannb.com/front/lib/toolKit/images/loading/sync_anima_progress_' + i + '.png');
    // }, 200)
    $('.loading').show();
    lo_setTime =setTimeout(function() {
        console.log('清除');
        // if(lo_time){
            // clearInterval(lo_t/ime);
            // lo_time=null;
        // }
        $('.loading').hide();
    }, time||10000)
    console.log("启动定时器");
}


window.clearLoading = function clearLoading(obj) {
    setTimeout(function() {
        if(lo_time){
            clearInterval(lo_time);
            lo_time=null;
        }
        if(lo_setTime){
            clearTimeout(lo_setTime);
            lo_setTime=null;
        }
        $('.loading').hide();
        if(obj&&obj.dom){
            $(obj.dom).show();
        }
    }, 300)
}

/**
 * toast 
 * 2019/01/23 曾智勇
 */
$('body').append('<div class="toast">');
var $toast = {
    show: function (obj) {
        $('.toast').text(obj.text).show();
        setTimeout(function () {
            $('.toast').hide();
        }, obj.time)
    }
};