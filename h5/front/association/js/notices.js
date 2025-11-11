let pageNumber = 1;

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
  getNotices(1);
}, 10);

// 滚动到底部即加载下一页
$(window).scroll(function () {
  var scrollTop = $(this).scrollTop();
  var scrollHeight = $(document).height();
  var windowHeight = $(this).height();
  if (scrollTop + windowHeight == scrollHeight) {
    pageNumber++;
    getNotices();
  }
});

// 获取通知
function getNotices() {
  const type = 1;
  const url = '/guild/hall/apply/getApplyJoinRecords';
  const header = '';
  const data = {
    uid: info.uid,
    pageNum: pageNumber,
    pageSize: 10
  };
  dispatchRequest(url, type, header, data);
}

// 加载通知
function generateSnippets(data) {
  $('.notices-list').html("");

  let html = '';
  for (const item of data) {
    html += `<div class="item">
      <div class="item-content">
        <p class="info">${item.nick}（ID：${item.erbanNo}）申请${item.type ? '退出' : '加入'} ${item.hallTitle}</p>
        <p class="date">${new Date(item.createTime).format('yyyy-MM-dd hh:mm')}</p>
      </div>`;
    if (item.status == 0) {
      html += `<div class="button-group">
          <a href="javascript: check(true, ${item.id});">同意</a>
          <a href="javascript: check(false, ${item.id});">拒绝</a>
        </div>`;
    } else {
      html += `<div class="result">
          <p>
            <img src="${(item.status == 1 || item.status == 4) ? '../img/reject.png' : '../img/agree.png'}" alt="reject">
            <span>${item.approverNick ? item.approverNick : ''}</span>
            <span>${item.status == 1 ? '已拒绝' : item.status == 2 ? '已同意' : (item.status == 3 && item.type == 1) ? '已退出' : (item.status == 3 && item.type == 0) ? '已加入' : item.status == 4 ? '已失效' : '审核中'}</span>
          </p>
        </div>`;
    }
    html += `</div>`;
  }

  $('.notices-list').append(html);
}

function check(status, applyId) {
  const type = 1;
  const url = '/guild/hall/apply/verify';
  const header = '';
  const data = {
    uid: info.uid,
    applyId: applyId,
    isApproved: status
  };
  dispatchRequest(url, type, header, data);
}