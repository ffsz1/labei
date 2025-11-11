//5.校验⻘青少年年模式密码
// POST /users/teens/mode/checkCipherCode
//必须引入jquery才可以用
//1：当输入框1输入1个字符后，自动切换光标到输入框2
// 设置⻘青少年年模式密码
// POST /users/teens/mode/save


info = {};
info.first_value = '';
info.isOpen = false;
// appFun('getUid', function(data) {
//         info.uid = data
//         appFun('getTicket', function(data) {
//             info.ticket = data
//             $(function() {
//                 initPage()
//             })

//         })
//     })
// info.uid = 10240;
// info.ticket = "eyJhbGciOiJIUzI1NiJ9.eyJ0aWNrZXRfdHlwZSI6bnVsbCwidWlkIjoxMDI0MCwidGlja2V0X2lkIjoiY2Y4YjQwMmQtODlkMC00OTgyLWExMGQtMWMwYTBhMjJiYzk5IiwiZXhwIjozNjAwLCJjbGllbnRfaWQiOiJlcmJhbi1jbGllbnQifQ.mJyw6dLYrTGNVYiPZRF8ou7iSw5Q_LzjqkmdTf5dC9o";

// initPage()
// info.ticket = '195e7339782f5e55b2bca93ef3926476'
// info.uid = '105'
// initPage()

function initPage() {
    $.ajax({
        url: allUrl() + '/users/teens/mode/getUsersTeensMode',
        method: 'get',
        datatype: 'json',
        data: {
            uid: info.uid,
            ticket: info.ticket,
        },
        success: function(res) {
            if (res.code === 200) {
                console.log(res)
                var data = res.data;
                if (data.cipherCode !== null) {
                    // console.log(data.cipherCode);
                    info.cipherCode = data.cipherCode;
                    info.first_value = info.cipherCode;
                    info.isOpen = true;

                    setTitle();
                } else {
                    setTitle();
                }
            }
        },
        error: function(err) {
            console.log(err)
        }
    })

}



$(document).ready(function() {
    $("[name='pwd1']").bind("input", function() {
        console.log($(this).val());
        if ($(this).val().length == 1) {
            info.input1 = $(this).val();
            // $('.dotted1').show()
            $("[name='pwd2']").focus();
            info.tab_index = 2;
        } else if ($(this).val().length == 0) {
            // info.tab_index = 1
            // $('.dotted1').hide()
        }
    })
    $("[name='pwd2']").bind("input", function() {
        console.log($(this).val());
        if ($(this).val().length == 1) {
            info.input2 = $(this).val();
            // $('.dotted2').show()
            $("[name='pwd3']").focus();
            info.tab_index = 3;

        } else if ($(this).val().length == 0) {
            // $("[name='pwd1']").focus();
            // info.tab_index = 1;

        }
    })
    $("[name='pwd3']").bind("input", function() {
        console.log($(this).val());
        if ($(this).val().length == 1) {
            // $('.dotted3').show()
            info.input3 = $(this).val();
            $("[name='pwd4']").focus();
            info.tab_index = 4;

        } else if ($(this).val().length == 0) {
            // $("[name='pwd2']").focus();
            // info.tab_index = 2;
        }
    })
    $("[name='pwd4']").bind("input", function() {
        if ($(this).val().length == 0) {
            // $("[name='pwd3']").focus();
            // info.tab_index = 3;
            $('#next').css('background', 'url(./img/btn_next_disable.png) 0/contain no-repeat')
        } else if ($(this).val().length == 1) {
            info.input4 = $(this).val();
            $('input').blur()
            console.log(info);
            $('#next').css('background', 'url(./img/btn_next_on.png) 0/contain no-repeat')
        }

    })
})
document.onkeyup = function() {
    setDotted()
}
document.onkeydown = function() {

    // var target = $('#pwd' + info.tab_index);
    if (event.keyCode == 8) {
        // switch (info.tab_index) {
        //     case 1:
        //         if (target.val() == '') {
        //             info.tab_index = 1;
        //         } else {
        //             target.val('')
        //             // $('.dotted' + info.tab_index).hide()
        //         }
        //         break;
        //     case 2:
        //         if (target.val() == '') {
        //             info.tab_index--;
        //             $('#pwd' + info.tab_index).focus()
        //         } else {
        //             // target.val('')
        //             // $('.dotted' + info.tab_index).hide()
        //         }
        //         break;
        //     case 3:
        //         if (target.val() == '') {
        //             info.tab_index--;
        //             $('#pwd' + info.tab_index).focus()
        //         } else {
        //             // target.val('')
        //             // $('.dotted' + info.tab_index).hide()
        //         }
        //         break;
        //     case 4:
        //         if (target.val() == '') {
        //             info.tab_index--;
        //             $('#pwd' + info.tab_index).focus()
        //         } else {
        //             // target.val('')
        //             // $('.dotted' + info.tab_index).hide()
        //         }
        //         break;
        // }
        $('.label,.dotted').click()
    }
}
$('.label,.dotted').on('click', function() {
    $('input').val('');
    $("[name='pwd1']").focus();
    $('.dotted1,.dotted2,.dotted3,.dotted4').hide()
    info.tab_index = 1;
})



function setDotted() {
    $('#pwd1').val().length != 1 ? $('.dotted1').hide() : $('.dotted1').show();
    $('#pwd2').val().length != 1 ? $('.dotted2').hide() : $('.dotted2').show();
    $('#pwd3').val().length != 1 ? $('.dotted3').hide() : $('.dotted3').show();
    $('#pwd4').val().length != 1 ? $('.dotted4').hide() : $('.dotted4').show();
}

function setTitle() {
    if (info.first_value.length == 4) {
        $('#section2-title').text('确认密码');
    } else {
        $('#section2-title').text('输入密码');
    }

    if (info.first_value === info.second_value || info.isOpen) {
        $('#section1-title').text('青少年模式已开启')
        $('#on_off').css('background', 'url(./img/bth_off.png) 0/100% no-repeat').text('关闭青少年模式').removeClass('on').addClass('off')

    } else {
        $('#section1-title').text('青少年模式未开启')
        $('#on_off').css('background', 'url(./img/bth_on.png) 0/100% no-repeat').text('开启青少年模式').removeClass('off').addClass('on')
    }



}

function open1(_this) {
    // window.androidJsObj.closeWin(true)
    if ($(_this).hasClass('on')) { //设置密码
        $('.section2').show();
        $('.section1').hide();
        $("[name='pwd1']").focus();
        info.tab_index = 1;
        // console.log($('#pwd1').val().length);
    } else { //校验密码
        $('.section2').show();
        $('.section1').hide();
        $("[name='pwd1']").focus();
        info.tab_index = 1;
        setTitle()
    }
}

function verifyNumber(val) {
    // alert(val.search(/\D/g) === -1)
    return val.search(/\D/g) === -1;
}

function goNext() {

    if (!verifyNumber($('#pwd1').val() + $('#pwd2').val() + $('#pwd3').val() + $('#pwd4').val())) {
        $toast.show({
            text: '密码只能是数字',
            time: 2000
        })
        $('#pwd1').click(); //清空密码输入
        return;
    }

    if (info.first_value == '') {
        info.first_value = $('#pwd1').val() + $('#pwd2').val() + $('#pwd3').val() + $('#pwd4').val();
        if (info.first_value.length == 4) {
            $('#section2-title').text('确认密码');
            $('#pwd1').click(); //清空密码输入
            $('#next').css('background', 'url(./img/btn_next_disable.png) 0/contain no-repeat')
        } else {
            info.first_value = '';
        }
    } else {

        info.second_value = $('#pwd1').val() + $('#pwd2').val() + $('#pwd3').val() + $('#pwd4').val();
        if (info.first_value === info.second_value && info.isOpen) {
            // $toast.show({
            //     text: '校验成功！',
            //     time: 2000
            // })
            delCipherCode(info.second_value);
            // setTimeout(function(){
            // window.androidJsObj.closeWin(true)
            //    alert('调用关闭');
            //     appFun('openTeenagerModelCallback')
            //     appFun('closeWin')
            //     window.androidJsObj.closeWin(true)
            // },2000)

        } else if (info.first_value === info.second_value) {
            setCipherCode(info.first_value)
        } else {
            $toast.show({
                text: '输入有误~',
                time: 2000
            })
            setTimeout(function() {

                //    alert('调用关闭');

                appFun('openTeenagerModelCallback')
                appFun('closeWin')
                window.androidJsObj.closeWin(true)
            }, 2000)
        }
    }


}

function setCipherCode(code) {
    $.ajax({
        url: allUrl() + '/users/teens/mode/save',
        method: 'post',
        datatype: 'json',
        data: {
            uid: info.uid,
            ticket: info.ticket,
            cipherCode: code
        },
        success: function(res) {
            if (res.code === 200) {
                console.log(res)
                $toast.show({
                    text: '设置成功！',
                    time: 2000
                })
                setTimeout(function() {
                    location.reload()
                        // window.androidJsObj.closeWin(true)
                }, 2000)
            }
        },
        error: function(err) {
            console.log(err)
        }
    })
}

function delCipherCode(code) {
    $.ajax({
        url: allUrl() + '/users/teens/mode/closeTeensMode',
        method: 'post',
        datatype: 'json',
        data: {
            uid: info.uid,
            ticket: info.ticket,
            cipherCode: code
        },
        success: function(res) {
            if (res.code === 200) {
                console.log(res)
                $toast.show({
                    text: '关闭成功！',
                    time: 2000
                })
                setTimeout(function() {
                    location.reload()
                        //  window.androidJsObj.closeWin(true)
                }, 2000)
            }
        },
        error: function(err) {
            console.log(err)
        }
    })
}