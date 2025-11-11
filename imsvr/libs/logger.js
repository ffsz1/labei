/**

 var logger = require('./logger');
 var msg = '';
 logger.error(msg);
 */

'use strict';

/**
 * Module dependencies.
 */

var formater = require('error-formater');
var Logger = require('mini-logger');
var C = require('../config');

var isTEST = process.env.NODE_ENV === 'test';
var categories = ['user_error', 'admin_error'];

var logger = module.exports = Logger({
  categories: categories,
  dir: C.logdir,
  duration: '1d',
  format: '[{category}.]YYYY-MM-DD[.log]',
  stdout: C.debug && !isTEST,
  errorFormater: errorFormater
});

var to = [];
for (var user in C.admins) {
  to.push(C.admins[user]);
}

function errorFormater(err) {
  var msg = formater.both(err);
  return msg.text;
}
