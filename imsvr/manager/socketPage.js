'use strict';

const F = require('../common/function');
const C = require('../config');
const _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');
const co = require('co');

module.exports = function (app, commonManager) {

    let modelMap = app.model_mgr.model_map;
    let managerMap = commonManager.mgr_map;
    var model_map = app.model_mgr.model_map;
    var mgr_map = commonManager.mgr_map;
    
    this.page_name = {'app': 1};
    // socket状态 1在线 2关闭
    this.status = {'online':1, 'close':2};

    // redis key 有效时间（秒）
    this.redis_expire = 28800;
    let that = this;


};
