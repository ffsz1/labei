$(function () {
    var urlData = new Array;
    location.search.substring(1).split('&').forEach(function(item) {
        urlData.push(item.split('=')[0])
        urlData.push(item.split('=')[1])
    });
    $.ajax({
        url:'/stat/roomFlow/getDetail/',
        data:{
           date: urlData[1],
            roomUid: urlData[3]+'='
        },
        success:function(data){
            var detail=data.data;
            var $div=$('.list3');
            $.each(detail,function(i){
                var $child=$('<dl><dt><img src='+detail[i].giftUrl+'></dt><dd><div class="time">'+detail[i].time+'</div><span class="money">+'+detail[i].totalGoldNum+'</span><br/><span class="notice">'+detail[i].sendNick+' 赠送 '+detail[i].receiveNick+' '+detail[i].giftName+'x'+detail[i].giftNum+'</span></dd></dl>');
                $div.append($child);
            })
        }
    });
})
