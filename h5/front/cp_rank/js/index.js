appFun('getUid', function (e) {
    info.uid = e;
    appFun('getTicket', function (e) {
        info.ticket = e;
        $(function () {
            obj.init()
        })
    })
})