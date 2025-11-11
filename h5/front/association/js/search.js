// 点击搜索
$('.search-icon').on('click', function() {
  searchAssociation();
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
  // searchAssociation();
}, 10);

// 搜索
function searchAssociation() {
  const type = 1;
  const url = '/guild/search';
  const header = '';
  const data = {
    key: $('#search').val(),
    uid: info.uid
  };
  console.log(data)
  dispatchRequest(url, type, header, data);
}

// 加载搜索结果
function appendToSearchResult(data) {
  $('.search-content').html("");
  
  let htmlSnippets = `<h3>搜索结果</h3><div class="search-result">`;
  if (data.length == 0) {
    htmlSnippets += '<h3>无搜索结果</h3>';
  } else {
    htmlSnippets += `<div class="result-list">`;
    for (const item of data) {
      htmlSnippets += `
      <div class="list-item" onclick="goHomePage(this, ${item.myGuild})">
        <img src="${item.logoUrl}" alt="head">
        <div class="describe" data-id="${item.id}">
          <p id="desc-name">${item.name}</p>
          <p id="desc-id">公会ID：${item.guildNo}</p>
        </div>
      </div>`;
    }
    htmlSnippets += '</div>';
  }
  htmlSnippets += '</div></div>';

  $('.search-content').append(htmlSnippets);
}

// 公会主页 
function goHomePage(e, flag) {
  // homePageType [1.公会长; 2.厅主/成员]
  window.location.href = `./associationPage.html?type=${flag}&homePageType=${getQueryString().accountType}&id=${$($(e).find('.describe')[0]).data().id}`;
};