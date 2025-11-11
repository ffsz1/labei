'use strict';

const modelBase = require('./model_base');
const F = require('../common/function');

module.exports = function (tableName, modelManager) {

    modelBase.call(this);
    this.table = tableName;
    let modelMap = modelManager.model_map;
    var model_map = modelMap;

};