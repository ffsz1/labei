//适配文件

function setRem() {
    var clientWidth = document.documentElement.clientWidth || document.body.clientWidth
    document.documentElement.style.fontSize = clientWidth / 10 + 'px'
    if (clientWidth > 750) clientWidth = 750
}
// 初始化
setRem()

//窗口改变时
window.onresize = function() {
    setRem()
}