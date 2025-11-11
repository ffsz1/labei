/*充值金额明细*/
function getFamilySendGiftRecord(teamId) {
    $('#detailTable').bootstrapTable('destroy');
    $('#detailTable').bootstrapTable({
        columns: [
            {field: 'erbanNo', title: '拉贝号', align: 'center', width: '8%'},
            {field: 'uid', title: 'uid', align: 'center', width: '8%'},
            {field: 'nick', title: '昵  称', align: 'center',width: '20%'},
            {field: 'familyId', title: '家族🆔', align: 'center',width: '5%'},
            {field: 'familyName', title: '家族名', align: 'center',width: '8%'},
            {
                field: 'avatar',
                title: '头像',
                align: 'center',
                valign: 'middle',
                formatter: function (val, row, index) {
                    return "<img src='" + val + "' width='40' height='40'>";
                }
            },
            {field: 'num', title: '数量', align: 'center', width: '8%'},
            {field: 'giftName', title: '礼物名称', align: 'center', width: '8%'},
            {field: 'createTime',title: '创建时间',align: 'center',width: '10%',
                formatter: function (val,row,index) {
                    if(val){
                        var date = new Date(val);
                        return date.format("yyyy-MM-dd hh:mm:ss");
                    }else{
                        return '-';
                    }
                }
            },
        ],
        cache: false,
        striped: true,
        // showRefresh: true,
        sortStable:true,
        pageSize: 10,
        pagination: true,
        pageList: [10, 20, 30, 50],
        search: false,
        sidePagination: "server", //表示服务端请求
        //设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder
        //设置为limit可以获取limit, offset, search, sort, order
        queryParamsType: "undefined",
        queryParams: function queryParams(params) {   //设置查询参数
            var param = {
                page: params.pageNumber,
                size: params.pageSize,
                teamId:teamId,
            };
            return param;
        },
        uniqueId: 'id',
        url: '/admin/familyFlow/detail.action',
        method: 'get',
        responseHandler: function(res) {
            if(res.code==200){
                console.log(res.data)
                return {
                    "total": res.data.total,//总页数
                    "rows": res.data.list  //数据
                };
            }else {
                $("#tipMsg").text(res.message+"["+res.code+"]");
                $("#tipModal").modal('show');
                return {
                    "total": "",//总页数
                    "rows": []  //数据
                };
            }
        }
    });
    $("#detailModel").modal('show');
}
