// 更多
$('.more').on('touchend', function () {
  $('.pop-up, .dialog').slideDown();
});

// 更多弹框退出公会
$('.out').on('touchend', function () {
  $('.out').hide();
  $('.dialog>.confirm').show();
});

// 更多弹框取消 
$('.dialog>.cancel').on('touchend', function () {
  $('.out').show();
  $('.dialog>.confirm').hide();
  $('.pop-up, .dialog').slideUp();
});

// 更多弹框确认
$('.dialog>.confirm').on('touchend', function () {
  quitTheUnion();
  $('.dialog').hide();
  $('.out').show();
  $('.dialog>.confirm').hide();
});

// 申请加入
$('.join-us').on('touchend', function () {
  $('#room').val("");
  $('#reason').val("");
  $('.pop-up, .notice').show();
});

// 提交申请
$('.submit').on('touchend', function () {
  applyForMembership();
});

// 取消 | 确定
$('.button-group>.cancel, .button-group>.confirm').on('touchend', function () {
  $('.pop-up, .notice, .pop-up>.tips').hide(500);
});

// 成员
$('#desc-members').on('touchend', function () {
  // memberType [1.公会; 2.厅]
  window.location.href = `./members.html?memberType=1&accountType=${getQueryString().homePageType}&guildId=${$(this).parent().data().id}`;
});

// 我的厅主页
$('.my-room-info>.info').on('touchend', function() {
  // detailsType [0.公会长; 1.厅主; 2.成员(我的数据)]
  window.location.href = `./details.html?detailsType=${getQueryString().homePageType == 2 ? 3 : getQueryString().homePageType}&hallId=${$($(this).find('.describe')[0]).data().id}`;
});

setTimeout(function () {
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

  console.log(location.href)
  getAssociationInfo();
}, 10);

// 获取公会信息
function getAssociationInfo() {
  const type = 1;
  const url = '/guild/get';
  const header = '';
  const data = {
    uid: info.uid,
    guildId: getQueryString().id
  };
  dispatchRequest(url, type, header, data);
}

// 渲染数据
function renderData(data) {
  // 标题
  document.title = data.guild.name;
  // 公会
  $('.not-visitor>.info>img').attr('src', data.guild.logoUrl);
  $('.not-visitor>.info>.describe').attr('data-id', data.guild.id);
  $('.not-visitor>.info #desc-name').text(data.guild.name);
  $('.not-visitor>.info #desc-id').text('ID：' + data.guild.guildNo);
  $('.not-visitor>.info #desc-members').text('成员: ' + data.guild.memberCount);
  // 厅列表
  $('.association-page-room>.room-title>span').text(`厅列表 (${data.guild.hallCount})`);
  appendToRoomList(data.halls);
  // 加载厅选项
  loadRoomOption(data.halls);
  // 我的厅
  if (data.myHall) {
    $('.my-room').css('display', 'block');
    $('.my-room .info>img').attr('src', data.myHall.roomAvatar);
    $('.my-room .info>.describe').attr('data-id', data.myHall.hallId);
    $('.my-room .info #desc-name').text(data.myHall.roomTitle);
    $('.my-room .info #desc-id').text('ID：' + data.myHall.erbanNo);
  }
  // 申请加入
  if (!data.hasJoin) {
    $('.join-us').css('display', 'block');
  }
  // 更多
  if (info.uid != data.guild.presidentUid && data.myHall && info.uid != data.myHall.hallUid) {
    $('.more').css('display', 'block');
  }
}

// 加载厅列表
function appendToRoomList(data) {
  let htmlSnippets = '';
  for (const item of data) {
    htmlSnippets += `
    <div class="list-item" onclick="openRoom(${item.hallUid}, this)">
      <img src="${item.roomAvatar}" alt="head">
      <div class="describe" data-id="${item.hallId}">
        <p id="desc-name">${item.roomTitle}</p>
        <p id="desc-id">ID：${item.erbanNo}</p>
      </div>
    </div>`;
  }

  $('.association-page-room>.room-list').append(htmlSnippets);
}

// 加载厅选项
function loadRoomOption(data) {
  let html = "";
  for (const item of data) {
    html += `<option value="${item.hallId}">${item.roomTitle}</option>`;
  }
  $('#room').append(html);
}

// 申请加入公会
function applyForMembership() {
  if ($('#room').val() != '') {
    const type = 1;
    const url = '/guild/hall/apply/applyJoinHall';
    const header = '';
    const data = {
      uid: info.uid,
      hallId: $('#room').val(),
      reason: $('#reason').val()
    };
    dispatchRequest(url, type, header, data);
  } else if ($('#room').val() == '') {
    $('.toast').text('请选择加入的厅~').show();
    setTimeout(function () {
        $('.toast').hide();
    }, 1200);
  }
}

// 退出公会
function quitTheUnion() {
  const type = 1;
  const url = '/guild/hall/apply/applyExitHall';
  const header = '';
  const data = {
    uid: info.uid
  };
  dispatchRequest(url, type, header, data);
}

// 进入房间
function openRoom(uid, e) {
  if (getQueryString().homePageType == 0 && getQueryString().type == 'true') {
    // detailsType [0.公会长; 1.厅主; 2.成员(我的数据)]
    window.location.href = `./details.html?detailsType=${getQueryString().homePageType == 2 ? 3 : getQueryString().homePageType}&hallId=${$($(e).find('.describe')[0]).data().id}`;
  } else {
    if (browser.ios) {
      window.webkit.messageHandlers.openRoom.postMessage(uid);
    } else if (browser.android) {
      if (androidJsObj && (typeof androidJsObj === 'undefined' ? 'undefined' : _typeof(androidJsObj)) === 'object') {
        window.androidJsObj.openRoom(uid);
      }
    }
  }
}