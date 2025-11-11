

'use strict';

/**
 * Module dependencies.
 */

var thunkify = require('thunkify-wrap');
var ready = require('ready');
var mysql = require('mysql');
var config = require('../config');
var logs_obj = require('../libs/logs.js');
var C = require('../config/index');

var logs = new logs_obj();

var server = config.mysqlServers[0];

var innerCon = function(connection) {
    var con = connection;

    this.has_release = false;

    var innerquery = function (sql, values, cb) {
        if (typeof values === 'function') {
            cb = values;
            values = null;
        }
        if (typeof(values) == 'object') {
            if (logs.isNull(C.STOP_DEBUG)) logs.addLogs("debug/debug",[sql,values]);
        } else {
            if (logs.isNull(C.STOP_DEBUG)) logs.addLogs("debug/debug",[sql]);
        }
        con.query(sql,values, function(err,rows){

            // if (sql.toLocaleLowerCase().indexOf("duplicate") >= 0 && !logs.isNull(rows)) {
            //     logs.addLogs("debug/debug",["db mod duplicate:",rows]);
            //     if (rows.insertId == 0 && rows.affectedRows == 1) rows.affectedRows = 0;
            // }
            // if (sql.toLocaleLowerCase().indexOf("update") == 0 && !logs.isNull(rows)) {
            //     logs.addLogs("debug/debug",["db update:",rows]);
            //     if (rows.message.indexOf("Changed: 0") >= 0) rows.affectedRows = 0;
            // }
            cb(err,[rows,null]);
        });
    };

    this.query = thunkify(innerquery);

    this.end = function () {
        if (true == this.has_release) return;
        con.end();
        this.has_release = true;
    }
    this.release = function (name) {
        if (true == this.has_release) return;
        con.release();
        this.has_release = true;
    }
}

// TODO: query timeout
module.exports = function (ext_config=null){
    var poolconfig = {
        host: server.host,
        port: server.port,
        user: server.user,
        password: server.password,
        database: config.mysqlDatabase,
        connectionLimit: config.mysqlMaxConnections,
        charset: 'utf8mb4_unicode_ci',
        // charset: 'utf8_unicode_ci',
        multipleStatements: true,
        waitForConnections: true,
        acquireTimeout: config.mysqlQueryTimeout,
        connectTimeout: config.mysqlQueryTimeout
    };

    if (ext_config != null) poolconfig = ext_config;

    this.conf = poolconfig;

    var pool = mysql.createPool(poolconfig);

    var tranpoolconfig = poolconfig;

    var tranpool = mysql.createPool(tranpoolconfig);

    this.pool = pool;
    this.tranpool = tranpool;
    var that = this;

    this.addDebugLogs = function (data) {
        //logs.addLogs("debug/debug",data);
    };

    this.addLogs = function (data) {
        logs.addLogs("debug/debug",data);
        logs.addLogs("sys/sys",data);
    };

    this.addErrLogs = function (data) {
        logs.addLogs("debug/debug",data);
        logs.addLogs("sys/sys",data);
        logs.addLogs("err/err",data);
    };


    this.query = thunkify(function (sql, values, cb) {
        if (typeof(values) == 'object') {
            if (logs.isNull(C.STOP_DEBUG)) logs.addLogs("debug/debug",[sql,values]);
        } else {
            if (logs.isNull(C.STOP_DEBUG)) logs.addLogs("debug/debug",[sql]);
        }
        let start_time = new Date().getTime();
        if (typeof values === 'function') {
            cb = values;
            values = null;
        }
        pool.query(sql, values, function (err, rows) {
            let end_time = new Date().getTime();
            if (end_time >= start_time+C.slow_log_delta) {
                if (typeof(values) == 'object') {
                    if (end_time - start_time > C.slow_log_delta) logs.addLogs("slow/slow",[`usetime:${end_time-start_time}`,sql,values]);
                } else {
                    if (end_time - start_time > C.slow_log_delta) logs.addLogs("slow/slow",[`usetime:${end_time-start_time}`,sql]);
                }
            }
            if (!logs.isNull(err)) {
                that.addErrLogs([`usetime:${end_time-start_time}`,sql,err]);
            }
            // if (sql.toLocaleLowerCase().indexOf("duplicate") >= 0 && !logs.isNull(rows)) {
            //     logs.addLogs("debug/debug",["mod duplicate:",rows]);
            //     if (rows.insertId == 0 && rows.affectedRows == 1) rows.affectedRows = 0;
            // }
            // if (sql.toLocaleLowerCase().indexOf("update") == 0 && !logs.isNull(rows)) {
            //     logs.addLogs("debug/debug",["update:",rows]);
            //     if (rows.message.indexOf("Changed: 0") >= 0) rows.affectedRows = 0;
            // }
            cb(err, rows);
        });
    });


    this.getConnection = thunkify(function (name,cb) {
        tranpool.getConnection(function(err, connection) {
            var new_con = new innerCon(connection);
            cb(err,new_con);
        });
    });

    this.queryOne = thunkify(function (sql, values, cb) {
        if (typeof values === 'function') {
            cb = values;
            values = null;
        }
        that.query(sql, values, function (err, rows) {
            if (rows) {
                rows = rows[0];
            }
            cb(err, rows);
        });
    });

    this.escape = function (val) {
        return pool.escape(val);
    };

    ready(that);

    function init() {
        that.query('show tables', function (err, rows) {
            if (err) {
                console.error('[%s] [worker:%s] mysql init error: %s', Date(), process.pid, err);
                setTimeout(init, 1000);
                return;
            }
            console.log('[%s] [worker:%s] mysql ready, got %d tables', Date(), process.pid, rows.length);
            that.ready(true);
        });
    }
    init();

};



