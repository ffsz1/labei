'use strict';

const modelBase = require('./model_base');
const F = require('../common/function');

module.exports = function (tableName, modelManager) {

    modelBase.call(this);
    this.table = tableName;
    let modelMap = modelManager.model_map;
    var model_map = modelMap;

    //状态 0:待执行 1：执行中 2：执行成功 3：执行失败'
    this.status = {noexcute: 0, excuting: 1, excuteSuc: 2, excuteFail:3};

};