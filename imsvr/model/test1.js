'use strict';

const modelBase = require('./model_base');
const F = require('../common/function');
var mysqlLib = require('../libs/mysql.js');
var config = require('../config');
var server = config.mysqlServers[0];

module.exports = function (tableName, modelManager) {

    modelBase.call(this);
    this.table = tableName;

    

    this.uni_index = 'id';
    this.auto_index = 'id';

    //var new_config = {
    //  host: '127.0.0.1',
    //  port: 8066,
    //  user: 'root',
    //  password: '123456',
    //  database: 'TESTDB',
    //  connectionLimit: 50,
    //  //charset: 'utf8mb4_unicode_ci',
    //  charset: 'utf8_unicode_ci',
    //  multipleStatements: true,                                                                                       
    //  waitForConnections: true,                                                                                       
    //  acquireTimeout: 5000,
    //  connectTimeout: 5000
    //};                                                                                                                

    //this.mysql = new mysqlLib(new_config); 


    let model_map = modelManager.model_map;

};
