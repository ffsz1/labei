// 账户类型
let accountType;
let pageNum = 1;

// 搜索公会
$('#search').on('click', function () {
  window.location.href = './components/search.html?accountType=' + accountType;
});

// 滚动到底部即加载下一页
$('.recommend-list').scroll(function () {
  var scrollTop = $(this).scrollTop();
  var windowHeight = $(this).height();
  if (Math.floor((scrollTop + windowHeight) + 1) == $('.list-item').length * $('.list-item').outerHeight(true)) {
    queryRecommendList(++pageNum);
  }
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

  queryMyAssociationInfo();
  queryRecommendList(pageNum);
}, 10);

// 查询公会信息
function queryMyAssociationInfo() {
  const type = 1;
  const url = '/guild/hall/member/getGuildMemberCommonInfo';
  const header = '';
  const data = {
    uid: info.uid
  };
  dispatchRequest(url, type, header, data);
}

// 查询推荐公会
function queryRecommendList(pageNum) {
  console.log(pageNum);
  const type = 1;
  const url = '/guild/getRecommendList';
  const header = '';
  const data = {
    pageNum: pageNum,
    pageSize: 10
  };
  dispatchRequest(url, type, header, data);
}

// 我的公会
function appendToAssociationInfo(data) {
  let htmlSnippets = ""
  accountType = data.memberType;
  if (data.memberType == 3) {
    htmlSnippets = `<div class="no-association">
      <img src="./img/alone.png" alt="alone">
      <p>暂无加入公会</p>
    </div>`;
  } else if (data.memberType == 2) {
    htmlSnippets = `<div class="member">
      <div class="info" onclick="goHomePage(this)">
        <img src="${data.memberInfo.guildLogoUrl}" alt="head">
        <div class="describe" data-id="${data.memberInfo.guildId}">
          <p id="desc-name">${data.memberInfo.guildName}</p>
          <p id="desc-id">公会ID：${data.memberInfo.guildNo}</p>
        </div>
      </div>
      <div class="data">
        <div class="data-title">
          <span>我的数据</span>
          <span onclick="goDetails(${data.memberInfo.uid})">详情&nbsp;&nbsp;></span>
        </div>
        <div class="data-grid">
          <div class="grid-item">
            <p>${(data.memberInfo.memberTurnover && data.memberInfo.memberTurnover.gold_day) | 0}</p>
            <p>今日流水</p>
          </div>
          <div class="grid-item">
            <p>${(data.memberInfo.memberTurnover && data.memberInfo.memberTurnover.gold_week) | 0}</p>
            <p>本周流水</p>
          </div>
          <div class="grid-item">
            <p>${(data.memberInfo.memberTurnover && data.memberInfo.memberTurnover.gold_all) | 0}</p>
            <p>总流水</p>
          </div>
        </div>
      </div>
    </div>`;
  } else if (data.memberType == 1) {
    htmlSnippets = `<div class="header">
      <div class="info" onclick="goHomePage(this)">
        <img src="${data.memberInfo.guildLogoUrl}" alt="head">
        <div class="describe" data-id="${data.memberInfo.guildId}">
          <p id="desc-name">${data.memberInfo.guildName}</p>
          <p id="desc-id">公会ID：${data.memberInfo.guildNo}</p>
        </div>
      </div>
      <div class="data">
        <div class="data-title">
          <span>厅数据</span>
        </div>
        <div class="data-grid">
          <div class="grid-item">
            <p>${(data.memberInfo.hallTurnover && data.memberInfo.hallTurnover.gold_day) | 0}</p>
            <p>今日流水</p>
          </div>
          <div class="grid-item">
            <p>${(data.memberInfo.hallTurnover && data.memberInfo.hallTurnover.gold_week) | 0}</p>
            <p>本周流水</p>
          </div>
          <div class="grid-item">
            <p>${(data.memberInfo.hallTurnover && data.memberInfo.hallTurnover.gold_all) | 0}</p>
            <p>总流水</p>
          </div>
        </div>
      </div>
    </div>`;
  } else if (data.memberType == 0) {
    htmlSnippets = `<div class="monitor">
      <div class="info" onclick="goHomePage(this, true)">
        <img src="${data.memberInfo.guildLogoUrl}" alt="head">
        <div class="describe" data-id="${data.memberInfo.guildId}">
          <p id="desc-name">${data.memberInfo.guildName}</p>
          <p id="desc-id">公会ID：${data.memberInfo.guildNo}</p>
        </div>
      </div>
      <div class="data">
        <div class="data-title">
          <span>公会数据</span>
        </div>
        <div class="data-grid">
          <div class="grid-item">
            <p>${(data.memberInfo.guildTurnover && data.memberInfo.guildTurnover.gold_day) | 0}</p>
            <p>今日流水</p>
          </div>
          <div class="grid-item">
            <p>${(data.memberInfo.guildTurnover && data.memberInfo.guildTurnover.gold_week) | 0}</p>
            <p>本周流水</p>
          </div>
          <div class="grid-item">
            <p>${(data.memberInfo.guildTurnover && data.memberInfo.guildTurnover.gold_all) | 0}</p>
            <p>总流水</p>
          </div>
        </div>
      </div>
      <div class="data" style="${data.memberInfo.hallTurnover ? 'display: block' : 'display: none'}">
        <div class="data-title">
          <span>厅数据</span>
        </div>
        <div class="data-grid">
          <div class="grid-item">
            <p>${(data.memberInfo.hallTurnover && data.memberInfo.hallTurnover.gold_day) | 0}</p>
            <p>今日流水</p>
          </div>
          <div class="grid-item">
            <p>${(data.memberInfo.hallTurnover && data.memberInfo.hallTurnover.gold_week) | 0}</p>
            <p>本周流水</p>
          </div>
          <div class="grid-item">
            <p>${(data.memberInfo.hallTurnover && data.memberInfo.hallTurnover.gold_all) | 0}</p>
            <p>总流水</p>
          </div>
        </div>
      </div>
    </div>`;
  }

  if (data.memberType == 0 || data.memberType == 1) {
    const html = `<div class="notice" onclick="goNotices()">
      <img src="./img/notice.png" alt="notice">
    </div>`;
    $('.association').append(html);
  }

  $('.association-info').append(htmlSnippets);
}

// 公会推荐
function appendToRecommendList(data) {
  let htmlSnippets = ""
  for (const item of data) {
    htmlSnippets += `<div class="list-item" onclick="goHomePage(this, false)">
      <img src="${item.logoUrl}" alt="head">
      <div class="describe" data-id="${item.id}">
        <p id="desc-name">${item.name}</p>
        <p id="desc-id">公会ID：${item.guildNo}</p>
      </div>
    </div>`;
  }

  $('.association-recommend>.recommend-list').append(htmlSnippets);
}

// 通知
function goNotices() {
  window.location.href = './components/notices.html';
};

// 公会主页 
function goHomePage(e, flag) {
  // homePageType [1.公会长; 2.厅主/成员]
  window.location.href = `./components/associationPage.html?type=${flag}&homePageType=${accountType}&id=${$($(e).find('.describe')[0]).data().id}`;
};

// 我的数据详情
function goDetails(uid) {
  // detailsType [0.公会长; 1.厅主; 2.成员(我的数据)]
  window.location.href = './components/details.html?detailsType=2&uid=' + uid;
}