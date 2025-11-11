/**!
 * cnpmjs.org - dispatch.js
 *
 * Copyright(c) cnpmjs.org and other contributors.
 * MIT Licensed
 *
 * Authors:
 *  dead_horse <dead_horse@qq.com>
 *  fengmk2 <fengmk2@gmail.com> (http://fengmk2.github.com)
 */
/*******************  敏感字过滤服务,在生产环境中使用 ***************************/
'use strict';

/**
 * Module dependencies.
 */

var fs = require('fs');
var C = require('./config');

// 启动字词过滤服务器
var server = require('sc-filter').createServer();

if (C.wordFilter.unix_socket){ // Unix Socket 方式
  if(fs.existsSync(C.wordFilter.unix_socket)){
    fs.unlinkSync(C.wordFilter.unix_socket);
  }
  server.set('connectStr',C.wordFilter.unix_socket);
}else{ // TCP 方式
  server.set('connectStr',C.wordFilter.port);
}

server.loadDict(function loaded(){
  server.listen(server.get('connectStr'), function(){
    console.log('Dict server is listening on ' + server.get('connectStr'));
  });
});
