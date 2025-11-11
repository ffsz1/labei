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
  if (getQueryString().detailsType == 2) {
    queryMyAssociationInfo();
  } else {
    queryRoomInfo();
  }
}, 10);

// 查询我的数据
function queryMyAssociationInfo() {
  const type = 1;
  const url = '/guild/hall/member/getGuildMemberPageInfo';
  const header = '';
  const data = {
    memberUid: getQueryString().uid
  };
  dispatchRequest(url, type, header, data);
}

// 查询厅数据
function queryRoomInfo() {
  const type = 1;
  const url = '/guild/hall/get';
  const header = '';
  const data = {
    uid: info.uid,
    hallId: getQueryString().hallId
  };
  dispatchRequest(url, type, header, data);
}

// 渲染个人数据
function renderMyData(data) {
  $('.info>img').addClass('round').attr('src', data.memberInfo.avatar);
  $('#desc-name').text(data.memberInfo.nick);
  $('#desc-id').text('ID：' + data.memberInfo.erbanNo);
  $('#today').text(data.memberInfo.memberTurnover.gold_day);
  $('#week').text(data.memberInfo.memberTurnover.gold_week);
  $('#total').text(data.memberInfo.memberTurnover.gold_all);
  $('.in').addClass('disppear');
  $('.details-flow').css('display', 'block');
  loadFlow(data.turnovers);
  if (getQueryString().accountType == 0 || getQueryString().accountType == 1) {
    $('.in').removeClass('disppear');
    $('.in>span').text('进入主页');
    $('.data').css('display', 'block');
    $('.data>.data-title>span').text('个人数据');
    $('.details-flow').css('display', 'block');
    loadFlow(data.turnovers);
  }
  document.title = data.memberInfo.nick;
}

// 渲染厅主页
function renderRoomData(data) {
  $('.info').addClass('white');
  $('.info>img').attr('src', data.hall.roomAvatar);
  $('.info>.describe').attr('data-id', data.hall.hallId);
  $('.info>.describe').attr('data-uid', data.hall.hallUid);
  $('#desc-name').text(data.hall.roomTitle);
  $('#desc-id').text('ID：' + data.hall.erbanNo);
  $('#desc-members').css('display', 'block').text('成员：' + data.hall.memberCount);
  $('#today').text(data.hall.hallTurnover.gold_day);
  $('#week').text(data.hall.hallTurnover.gold_week);
  $('#total').text(data.hall.hallTurnover.gold_all);
  $('.in>span').text('进入房间');
  if (getQueryString().detailsType == 0 || getQueryString().detailsType == 1) {
    $('.info').removeClass('white');
    $('.data').css('display', 'block');
    $('.data>.data-title>span').text('厅数据');
    $('.details-flow').css('display', 'block');
    loadFlow(data.turnovers);
  }
  document.title = data.hall.roomTitle;
}

// 成员
$('#desc-members').on('touchend', function() {
  // memberType [1.公会; 2.厅]
  window.location.href = `./members.html?memberType=2&accountType=${getQueryString().detailsType}&hallId=${$(this).parent().data().id}`;
});

// 加载每日流水
function loadFlow(data) {
  let html = '';
  if (data && data.length > 0) {
    for (const item of data) {
      html += `<div class="list-item">
        <span>${new Date(item.reportDate).format('yyyy-MM-dd')}</span>
        <span>${item.gold | 0}</span>
      </div>`;
    }
    $('.flow-list').append(html);
  }
}

// 进入个人主页
$('.in').on('touchend', function() {
  console.log($('.in>span').text())
  if ($('.in>span').text() == '进入主页') {
    if (browser.ios) {
      window.webkit.messageHandlers.openPersonPage.postMessage(getQueryString().uid);
    } else if (browser.android) {
      if (androidJsObj && (typeof androidJsObj === 'undefined' ? 'undefined' : _typeof(androidJsObj)) === 'object') {
        window.androidJsObj.openPersonPage(getQueryString().uid);
      }
    }
  } else if ($('.in>span').text() == '进入房间') {
    if (browser.ios) {
      window.webkit.messageHandlers.openRoom.postMessage($($(this).parent().find('.describe')[0]).data().uid);
    } else if (browser.android) {
      if (androidJsObj && (typeof androidJsObj === 'undefined' ? 'undefined' : _typeof(androidJsObj)) === 'object') {
        window.androidJsObj.openRoom($($(this).parent().find('.describe')[0]).data().uid);
      }
    }
  }
});