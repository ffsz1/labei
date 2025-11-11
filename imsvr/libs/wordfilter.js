/**
 * example:
 var wordFilter = require('../libs/wordfilter');//加载字词过滤器客户端
 let checkWord = yield wordFilter.scMatch(str);
 if (checkWord.length>0){
    errors.param = i;
    errors.level = 1;
    errors.msg = '含有争议或敏感词汇['+checkWord[0][1]+']，请修改后再次提交';
    break;
  }
 */

'use strict';

var C = require('../config');
//加载词语过滤客户端
var scFilter = require('sc-filter').createClient();
if (C.wordFilter.unix_socket){ // Unix Socket 方式
  scFilter.connect(C.wordFilter.unix_socket);
}else{ // TCP 方式
  scFilter.connect(C.wordFilter.port,'localhost');
}
/**
 * @todo 过滤关键字
 * @param str
 * @returns {Function}
 */
function scMatch(str){
  return function(cb) {
    scFilter.match(str,function(result){
      cb(null,result);
    });
  }
}

exports.scMatch=scMatch;