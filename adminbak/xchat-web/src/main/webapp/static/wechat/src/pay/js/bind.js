var bindUrl = '/popstars/follow';
var validateUrl = '';
var baseUrl = 'http://www.erbanyy.com/popstars/geterban?user=';
var $num = $('#num');
var $get = $('#get');
var $info = $('.info');
var $result = $('.result');
var $infoWrapper = $('.info-wrapper');
var $resultWrapper = $('.result-wrapper');
var $mask = $('.mask');
var $confirm = $('#confirm');
var $closeInfo = $('.close-info');
var $closeResult = $('.close-result');

$closeInfo.on('click', function () {
	$infoWrapper.hide();
  $mask.hide();
})

$closeResult.on('click', function () {
	$resultWrapper.hide();
  $mask.hide();
  window.location.reload();
})

// 根据环境判断是否改变网址
function changeUrl(){
	var type = EnvCheck();
	if(type == 'test'){
		baseUrl = 'http://beta.erbanyy.com//popstars/geterban?user='
	}
	console.log(baseUrl);
}
changeUrl();

$get.on('click', function () {
	var reg = /^[0-9]*$/,
      _num = $num.val()
      ;

  if (_num === '' || !reg.test(_num)) {
		$info.html('请输入正确的手机号或者拉贝号');
		$infoWrapper.show();
    $mask.show();
    $confirm.hide();
  } else {
    validateUrl = baseUrl + _num;
  }

  console.log(validateUrl);

  $.ajax({
    type: 'GET',
    url: validateUrl,
    asyc: true,
    success: function (res) {
    	if (res.code == 200) {
				var data = res.data;
				console.log($num.val());
				$info.html('请你确认拉贝账号<div class="name">' + data.nick +'（拉贝号：' + data.erbanNo + '）</div>');
				$infoWrapper.show();
		    $mask.show();
		    $confirm.show();
		    validateUrl = '/popstars/geterban?user=';
    	} else {
    		console.log(res);
    		console.log($num.val());
				$info.html('账号不存在');
				$infoWrapper.show();
		    $mask.show();
		    $confirm.hide();
		    $num.val('');
		    validateUrl = '/popstars/geterban?user=';
    	}
		},
    error: function (res) {
			console.log(res);
    }
  });
})

$confirm.on('click', function () {
	var user = $num.val();
  var openid = localStorage.getItem('openid');
  var sex = localStorage.getItem('sex');

  bindUrl = bindUrl + '?user=' + user + '&openId=' + openid + '&wxGender=' + 1;//1=sex

 //  $infoWrapper.hide();
	// $resultWrapper.show();
 //  $mask.show();
console.log(bindUrl);
  $.ajax({
    type: 'POST',
    url: bindUrl,
    asyc: true,
    success: function (res) {
      if (res.code == 200) {
  			$result.html('<strong>恭喜！</strong>您已经领取成功了<br/>可到【我的钱包】查看');
			  $infoWrapper.hide();
				$resultWrapper.show();
			  $mask.show();
			  $num.val('');
      } else {
  			$result.html('<strong>SORRY！</strong>您已经领过金币喔<br/>可到【我的钱包】查看');
			  $infoWrapper.hide();
				$resultWrapper.show();
			  $mask.show();
			  $num.val('');
      }
    },
    error: function (res) {
    	console.log(res);
    }
  });

});


function convert () {
	var url = window.location.href;
	// var url = 'http://beta.erbanyy.com/wechat/src/pay/bind.html?subscribe=subscribe&openid=openid&sex=sex&language=language&city=city&province=province&country=country&headimgurl=headimgurl&subscribe_time&unionid=unionid&remark=remark&groupid=groupid&tagid_list=tagid_list';
	var data = url.split('?')[1];
	var dataArr = data.split('&');
	var dataObj = {};
	for (var i = 0; i < dataArr.length; i++) {
		var params = dataArr[i].split('=');
		dataObj[params[0]] = params[1];
	};

	return dataObj;
}

// 根据域名适配环境
function EnvCheck() {
  if(window.location.href){
    var _url = window.location.href;
    var res = _url.match(/beta/);
    if(res){
      return 'test';
    }else{
      return 'live';
    }
  }
}

$(function () {
	var userData = convert();

	//将获取到的微信用户资料存储在localstorage
	if (!localStorage.getItem('openid')) {
  	localStorage.setItem('openid', userData.openid);
	}
	if (!localStorage.getItem('sex')) {
		localStorage.setItem('sex', userData.sex);
	}
});

