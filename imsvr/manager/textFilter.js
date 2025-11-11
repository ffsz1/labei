'use strict';

const C = require('../config');
const F = require('../common/function');
const path = require('path');
const _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');
var wordFilter = require('../libs/wordfilter');

module.exports = function (app, commonManager) {

    let modelMap = app.model_mgr.model_map;
    let managerMap = commonManager.mgr_map;

    /**
     * 添加数据库长存定时器
     * @param timeout 多久后执行（毫秒）
     * @param key 要执行的cronjob映射名
     * @param data 执行时要用到的数据
     * @returns {{result: DBTimer id}}
     */
    this.scMatch = function* (message) {
        let check_word = yield wordFilter.scMatch(message);
        return check_word;
    };


};

