var ua = window.navigator.userAgent.toLowerCase();
var wxCode = '';
var uidMatch = location.href.match(/\buid=([^&]+)/);
if (ua.match(/MicroMessenger/i) == 'micromessenger') {
    var codeMatch = location.href.match(/\bcode=([^&]+)/);
    var uidMatch = location.href.match(/\buid=([^&]+)/);
    if (codeMatch) {
        wxCode = codeMatch[1];
    } else if (uidMatch) {

    } else {
        console.log('./getOpenid.html' + encodeURI(callbackUrl))
        location.replace('./getOpenid.html' + encodeURI(callbackUrl));
    }
}