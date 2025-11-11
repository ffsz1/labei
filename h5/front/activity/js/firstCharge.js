var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function(obj) {
	return typeof obj;
} : function(obj) {
	return obj && typeof Symbol === "function" && obj.varructor === Symbol && obj !== Symbol.prototype ? "symbol" :
		typeof obj;
};

const browser = checkVersion();
const info = {};
const fakeNews = [
	"恭喜菠萝菠萝开礼盒得到幸运草头饰",
	"恭喜米锅儿开礼盒得到许愿星头饰",
	"恭喜曼巴小帅哥开礼盒得到炫酷超跑座驾",
	"恭喜冲鸭开礼盒得到水果girl礼物",
	"恭喜小涵涵开礼盒得到水果girl礼物",
	"恭喜话筒开礼盒得到炫酷超跑座驾",
	"恭喜小可爱开礼盒得到许愿星头饰",
	"恭喜湘哥开礼盒得到幸运草头饰",
	"恭喜wind开礼盒得到炫酷超跑座驾",
	"恭喜陈凉凉开礼盒得到水果girl礼物"
];

$('.header .btn-group .btn').click(function() {
	if ($(this).find('p').text() == 'charge') {
		$(this).find('img').attr('src', './images/firstChargeImages/charge_button_selected.png');
		$(this).next().find('img').attr('src', './images/firstChargeImages/lucky_box.png');
		$('.charge').css('display', 'block');
		$('.box').css('display', 'none');
	} else if ($(this).find('p').text() == 'box') {
		$(this).find('img').attr('src', './images/firstChargeImages/lucky_box_selected.png');
		$(this).prev().find('img').attr('src', './images/firstChargeImages/charge_button.png');
		$('.box').css('display', 'block');
		$('.charge').css('display', 'none');
	}
	getCount();
});

// 顶部跑马灯
(function() {
	let ramdomNumber = 0;
	setInterval(function() {
		$('.display-board').css('display', 'block');
		$('.display-board').html(fakeNews[ramdomNumber]);
		ramdomNumber = Math.ceil(Math.random() * fakeNews.length + 1);
	}, 3000);
})();

// 打开活动规则
$('#rule').click(function() {
	$('.fade-rule').fadeIn(500);
});

// 关闭活动规则
$('.rule>.close-btn>img').click(function() {
	$('.fade-rule').fadeOut(500);
});

// 点击充值
$('.footer').click(function() {
	$('.fade-notice').fadeIn(500);
	// todo 上线删除掉
	// testCharge();
	// 拉起充值
	// if (browser.ios && window.webkit) {
	// $('.fade-notice').fadeIn(500);
	// } else if (browser.android) {
	// 	window.androidJsObj.openChargePage();
	// }
});

// 关闭充值提示弹框
$('.notice>.close-btn>img').click(function() {
	$('.fade-notice').fadeOut(500);
});

// 点击已完成充值
$('#charge_finish').click(function() {
	$('.fade-notice').fadeOut(500);
	$('#btn_lucky').find('img').attr('src', './images/firstChargeImages/lucky_box_selected.png');
	$('#btn_charge').find('img').attr('src', './images/firstChargeImages/charge_button.png');
	$('.box').css('display', 'block');
	$('.charge').css('display', 'none');
	getCount();
});

// 点击领取幸运
$('#lucky_btn').click(function() {
	draw();
});

// 关闭领取幸运
$('.gift>.close-btn>img').click(function() {
	$('.fade-gift').fadeOut(500);
});

// 点击礼物记录
$('#gift-record').click(function() {
	getGiftRecord();
	$('.fade-record').fadeIn(500);
});

// 关闭礼物记录
$('.record>.close-btn>img').click(function() {
	$('.fade-record').fadeOut(500);
});

// 复制公众号
$('#copy').click(function() {
    window.getSelection().removeAllRanges(); //这段代码必须放在前面否则无效
    const Url2 = document.getElementById("copy-text"); //要复制文字的节点
    const range = document.createRange();
    // 选中需要复制的节点
    range.selectNode(Url2);
    // 执行选中元素
    window.getSelection().addRange(range);
    // 执行 copy 操作
    const successful = document.execCommand('Copy');

    // 移除选中的元素
    window.getSelection().removeAllRanges();

    $('.toast').text('复制成功!').show();
    setTimeout(function() {
        $('.toast').hide();
    }, 1200);
});
//  ------浏览器复制------
// const clipboard = new Clipboard('#copy');
// // 复制成功
// clipboard.on('success', function(e) {
//     console.info('Action:', e.action);
//     console.info('Text:', e.text);
//     console.info('Trigger:', e.trigger);
//     $('.toast').text('复制成功!').show();
//     setTimeout(function () {
//         $('.toast').hide();
//     }, 1200);
//     e.clearSelection();
// });
// // 复制失败
// clipboard.on('error', function(e) {
//     console.error('Action:', e.action);
//     console.error('Trigger:', e.trigger);
//     $('.toast').text('复制失败!').show();
//     setTimeout(function () {
//         $('.toast').hide();
//     }, 1200);
// });

function getMessage(key, value) {
	info[key] = value;
	console.log(key, value, 'getMessage');
}

setTimeout(function() {
	if (browser.ios && window.webkit) {
		window.webkit.messageHandlers.getUid.postMessage(null);
		window.webkit.messageHandlers.getTicket.postMessage(null);
	} else if (browser.android) {
		if (androidJsObj && (typeof androidJsObj === 'undefined' ? 'undefined' : _typeof(androidJsObj)) ===
			'object') {
			info.uid = parseInt(window.androidJsObj.getUid());
			info.ticket = window.androidJsObj.getTicket();
			console.log(["info", info]);
		}
	}
	getCount();
}, 10);

// 获取累计充值和礼盒次数
function getCount() {
	const type = 2;
	const url = '/draw/userBoxInfo';
	const header = '';
	const data = {
		uid: info.uid
	};
	dispatchRequest(url, type, header, data);
}

// 礼盒记录
function getGiftRecord() {
	const type = 2;
	const url = '/draw/boxDrawList';
	const header = '';
	const data = {
		uid: info.uid
	};
	dispatchRequest(url, type, header, data);
}

// 抽奖
function draw() {
	const type = 1;
	const url = '/draw/doBoxDraw';
	const header = '';
	const data = {
		uid: info.uid
	};
	dispatchRequest(url, type, header, data);
}

// 充值测试
function testCharge() {
	const type = 1;
	const url = '/charge/testCharge';
	const header = '';
	const data = {
		chargeProdId: 21,
		uid: info.uid
	};
	dispatchRequest(url, type, header, data);
}

// 调用原生方法发送请求
function dispatchRequest(requestURL, method, requestHeader, params) {
	const type = method;
	const url = requestURL;
	const header = requestHeader;
	const data = params;
	if (browser.ios) {
		const obj = {
			requestMethod: type,
			urlController: url,
			headerMapString: header,
			paramMapString: data,
		};
		window.webkit.messageHandlers.httpRequest.postMessage(JSON.stringify(obj));
		// window.webkit.messageHandlers.httpRequest.postMessage(type,url,header,JSON.stringify(data));JSON.stringify(obj)
	} else if (browser.android) {
		if (androidJsObj && (typeof androidJsObj === 'undefined' ? 'undefined' : _typeof(androidJsObj)) === 'object') {
			window.androidJsObj.httpRequest(type, url, header, JSON.stringify(data));
		}
	}
}

// 回调响应事件
var onHttpResponse = function(e, item) {
	console.log(e, '接口返回', item);
	let body = null;
	if (browser.ios) {
		body = item;
		if (body.urlController == "/draw/userBoxInfo") {
			const responseData = JSON.parse(body.bodyString);
			console.log(responseData, 'bodyString');
			if (!body.isRequestError) {
				$('#cumulative-number').text(responseData.userCharge > 1000 ? 1000 : responseData.userCharge);
				console.log($('#cumulative-number').text())
				$('.lucky_button>p>span').text(responseData.boxNum);
			} else {
				$('.toast').text(responseData.message).show();
				setTimeout(function() {
					$('.toast').hide();
				}, 1200);
			}
		} else if (body.urlController == "/draw/boxDrawList") {
			const responseData = JSON.parse(body.bodyString);
			console.log(responseData, 'bodyString');
			if (!body.isRequestError) {
				$('.fade-record>.record>.record-content').html('')
				if (responseData) {
					let html =
						`<div class="table-head">
                                    <p>
                                        <span>时间</span>
                                        <span>记录</span>
                                    </p>
                                </div>
                                <div class="table-content">`;
					for (let index = 0; index < responseData.length; index++) {
						const element = responseData[index];
						html +=
							`<p>
                                    <span>${moment(element.createTime).format("MM月DD日")}</span>
                                    <span>${element.desc}</span>
                                </p>`;
					}
					html += '</div>';
					$('.fade-record>.record>.record-content').append(html);
				} else {
					$('.fade-record>.record>.record-content').append(
						'<p style="text-align: center; display: block; margin-top: 1.5rem;">什么都没有哦~</p>');
				}
			} else {
				$('.toast').text(responseData.message).show();
				setTimeout(function() {
					$('.toast').hide();
				}, 1200);
			}
		} else if (body.urlController == "/draw/doBoxDraw") {
			const response = body.bodyString;
			console.log(response, 'bodyString');
			if (!body.isRequestError) {
				const responseData = JSON.parse(body.bodyString);
				if (responseData) {
					$('.fade-gift>.gift>.gift-content>img').attr('src', responseData.prizePic);
                    $('.fade-gift>.gift>.gift-content').find('p').html(
                        `${responseData.prizeName}<span>${responseData.prizeDate}天</span>`);
					$('.lucky_button>p>span').text(responseData.boxNum);
					$('#cumulative-number').text(responseData.userCharge > 1000 ? 1000 : responseData.userCharge);
					$('.fade-gift').fadeIn(500);
					fakeNews.unshift(`恭喜陈XX开礼盒得到${responseData.prizeName + giftType(responseData.prizeType)}`);
				} else {
					$('.toast').text(responseData.message).show();
					setTimeout(function() {
						$('.toast').hide();
					}, 1200);
				}
			}
		}
	} else {
		body = JSON.parse(JSON.stringify(e));
		if (body.urlController == "/draw/userBoxInfo") {
			const responseData = JSON.parse(body.bodyString);
			console.log(responseData, 'bodyString');
			if (!body.isRequestError) {
				if (responseData.code == 200) {
					$('#cumulative-number').text(responseData.data.userCharge > 1000 ? 1000 : responseData.data.userCharge);
					console.log($('#cumulative-number').text())
					$('.lucky_button>p>span').text(responseData.data.boxNum);
				} else {
					$('.toast').text(responseData.message).show();
					setTimeout(function() {
						$('.toast').hide();
					}, 1200);
				}
			} else {
				$('.toast').text('获取数据失败!').show();
				setTimeout(function() {
					$('.toast').hide();
				}, 1200);
			}
		} else if (body.urlController == "/draw/boxDrawList") {
			if (!body.isRequestError) {
				const responseData = JSON.parse(body.bodyString);
				console.log(responseData, 'bodyString');
				if (responseData.code == 200) {
					$('.fade-record>.record>.record-content').html('');
					if (responseData.data) {
						let html =
							`<div class="table-head">
                                        <p>
                                            <span>时间</span>
                                            <span>记录</span>
                                        </p>
                                    </div>
                                    <div class="table-content">`;
						for (let index = 0; index < responseData.data.length; index++) {
                            const element = responseData.data[index];
                            console.log(element)
							html +=
								`<p>
                                        <span>${moment(element.createTime).format("MM月DD日")}</span>
                                        <span>${element.desc}</span>
                                    </p>`;
						}
						html += '</div>';
						$('.fade-record>.record>.record-content').append(html);
					} else {
						$('.fade-record>.record>.record-content').append(
							'<p style="text-align: center; display: block; margin-top: 1.5rem;">什么都没有哦~</p>');
					}
				} else {
					$('.toast').text(responseData.message).show();
					setTimeout(function() {
						$('.toast').hide();
					}, 1200);
				}
			} else {
				$('.toast').text('获取礼盒记录失败!').show();
				setTimeout(function() {
					$('.toast').hide();
				}, 1200);
			}
		} else if (body.urlController == "/draw/doBoxDraw") {
			if (!body.isRequestError) {
				const responseData = JSON.parse(body.bodyString);
				console.log(body, 'getUserRecommend');
				if (responseData.code == 200) {
					$('.fade-gift>.gift>.gift-content>img').attr('src', responseData.data.prizePic);
					$('.fade-gift>.gift>.gift-content').find('p').html(
						`${responseData.data.prizeName}<span>${responseData.data.prizeDate}天</span>`);
					$('.lucky_button>p>span').text(responseData.data.boxNum);
					$('#cumulative-number').text(responseData.data.userCharge > 1000 ? 1000 : responseData.data.userCharge);
					$('.fade-gift').fadeIn(500);
					fakeNews.unshift(`恭喜陈XX开礼盒得到${responseData.data.prizeName + giftType(responseData.data.prizeType)}`);
				} else {
					$('.toast').text(responseData.message).show();
					setTimeout(function() {
						$('.toast').hide();
					}, 1200);
				}
			} else {
				$('.toast').text('领取失败!').show();
				setTimeout(function() {
					$('.toast').hide();
				}, 1200);
			}
		}
	}
};

function giftType(type) {
	if (type == 1) {
		return '头饰';
	} else if (type == 2) {
		return '座驾'
	} else {
		return '礼物'
	}
}
