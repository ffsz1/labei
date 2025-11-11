/**
 * 提示框弹出
 * 结构
 * 基于jquery 或者 zepto,请与该文件前引入jquery.js或者zepto.js
 *<!-- <div id="dialog">
 *      <div class="dialog-mask"></div>
 *      <div class="dialog-box">
 *          <div class="dialog-container">
 *              <div class="dialog-container-title">是否退出家族</div>
 *              <div class="dialog-container-content">退出家族，需要族长同意，请耐心等待，如族长不同意，7天后自动退出</div>
 *          </div>
 *          <div class="dialog-btn">
 *              <button class="dialog-btn-cenfirm">确定</button>
 *              <button class="dialog-btn-cancel color-bold">取消</button>
 *          </div>
 *      </div>
 *  </div> --> 
 * 
 */
var Dialog = function(obj) {

    /**
     * 初始化
     */
    this.config = {
        title: "",
        content: '内容',
        mask: true,
        buttons: [{
            className: "dialog-btn-cenfirm",
            text: "确定",
            callback: function() {

            }
        }]
    };
    for (var i in obj) {
        this.config[i] = obj[i] || this.config[i]
    }
    this.body = $("body");
    this.dialogs = $("<div id='dialog'>");
    this.mask = $('<div class="dialog-mask">');
    this.dialogBox = $('<div class="dialog-box"></div>');
    this.dialogBox_ = $('<div class="dialog-box_"></div>');
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
            // console.log(1);
            this.creatButton(this.dialogBtn, this.config.buttons);
            this.dialogBox_.append(this.dialogBtn);
        }
        this.dialogBox.append(this.dialogBox_)
        this.dialogs.append(this.dialogBox)
        if (this.config.mask) {
            this.mask.on('touchend', function() {
                _this_.close();
                // console.log('close1')
            });
        }
        this.dialogs.append(this.mask)
        this.body.append(this.dialogs)
            // console.log(this.dialogs)
    },

    /**
     * 创建按钮
     */
    creatButton: function(footer, buttons) {
        var _this_ = this;
        if (buttons.length) {

        }
        //遍历出数组

        // console.log(buttons)
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
        // console.log('close')
        document.body.style.overflow = ''; //出现滚动条
        document.removeEventListener("touchmove", mo, false);
        this.dialogs.remove();
    },
}

function $Dialog(obj) {
    var dialog = new Dialog(obj);
    dialog.init(obj);
    // document.body.style.overflow = 'hidden'; //禁止滚动
    // document.addEventListener("touchmove", mo, false)
    // console.log(23132)
}

// 禁止默认事件
var mo = function(e) {
    e.preventDefault();
};

// 使用实例
// $Dialog({
//     title: "1",
//     content: '内容',
//     cancelClass: "color-bold",
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