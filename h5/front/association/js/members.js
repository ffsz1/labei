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
  if (getQueryString().memberType == 1) {
    getAssociationMembersList();
  } else {
    getRoomMembersList();
  }
}, 10);

// 公会成员列表
function getAssociationMembersList() {
  const type = 1;
  const url = '/guild/hall/member/getGuildMembers';
  const header = '';
  const data = {
    guildId: getQueryString().guildId
  };
  dispatchRequest(url, type, header, data);
}

// 厅成员列表
function getRoomMembersList() {
  const type = 1;
  const url = '/guild/hall/member/getHallMembers';
  const header = '';
  const data = {
    hallId: getQueryString().hallId
  };
  dispatchRequest(url, type, header, data);
}

// 成员列表
function renderMembersList(data) {
  let htmlSnippets = "";
  for (const item of data) {
    htmlSnippets += `
    <div class="list-item" onclick="goMemberPage(${item.uid})">
      <img src="${item.avatar}" alt="head">
      <div class="describe" data-uid="${item.uid}">
        <p id="desc-name">
          <span>${item.nick}</span>
          ${item.type == 0 && getQueryString().memberType == 1 ? `<img src="../img/monitor.png" alt="monitor" class="monitor">` : 
            item.type == 1 && getQueryString().memberType == 2 ? '<img src="../img/header.png" alt="header" class="monitor">' : ''}
        </p>
        <p id="desc-id">ID：${item.erbanNo}</p>
      </div>
    </div>`;
  }

  $('.members-list').append(htmlSnippets);
}

// 成员页
function goMemberPage(uid) {
  if ((getQueryString().accountType == 0 || getQueryString().accountType == 1) && getQueryString().memberType == 2) {
    window.location.href = `./details.html?detailsType=2&accountType=${getQueryString().accountType}&uid=${uid}`;
  } else {
    if (browser.ios) {
      window.webkit.messageHandlers.openPersonPage.postMessage(uid);
    } else if (browser.android) {
      if (androidJsObj && (typeof androidJsObj === 'undefined' ? 'undefined' : _typeof(androidJsObj)) === 'object') {
        window.androidJsObj.openPersonPage(uid);
      }
    }
  }
}