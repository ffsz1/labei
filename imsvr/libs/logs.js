'use strict';

const C = require('../config');
const fs=require('fs');
const path = require('path');
const _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');
var util = require("util");
var co = require('co');

module.exports = function () {

    let that = this;
    // 日志文件夹路径
    this.log_path = path.resolve(__dirname, '../logs');
    // qn curl log路径
    this.qn_curl_log_path = this.log_path + '/qn_curl/curl_qn_%s.log';
    // qn curl 临时扣费失败路径
    this.qn_curl_interim_consume_fail_log = this.log_path + '/qn_curl_interim_consume_fail.log';

    this.level = {debug: 'debug', info: 'info', warning: 'warning', error: 'error'};

    /**
     * 格式化内容
     * @param data object 例如{'name':'xxx'}
     * @returns {string}
     */
    this.formatData = function (data) {
        let cur_time = new Date();
        let data_str = cur_time.format('yyyy-MM-dd HH:mm:ss') + ' ';
        for(let key in data) {
            let value = typeof data[key] == 'object' ? JSON.stringify(data[key]) : data[key];
            data_str += key + ':' + value + '';
        }
        data_str += '\n';
        return data_str;
    };

    /**
     * 追加方式写入文件
     * @param file_path 文件路径
     * @param data object 例如{'name':'xxx'}
     * @returns {}
     */
    this.writeFile = function (file_path, data) {
        let data_str = that.formatData(data);
        fs.appendFileSync(file_path, data_str);
    };

    this.isNull = function(obj) {
        if (obj == null || typeof(obj) == "undefined" || obj.length == 0) {
            return true;
        }
        if(typeof(obj) == 'object' && !(obj instanceof Date) && Object.keys(obj).length == 0) {
            return true;
        }
        return false;
    };

    this.syncAddLogs = function (file_prefix,data,level=that.level.info) {
        // if (file_prefix == "debug/debug") console.log(data);
        if (this.isNull(file_prefix)) file_prefix = "sys_log";
        if (!this.isNull(C.NO_LOG_DEBUG) && file_prefix.indexOf("debug/debug") >= 0) return;
        let cur_time = new Date();
        //let content = typeof data == 'object' ? util.inspect(data,{depth:null}) : data;
        let content = typeof data == 'object' ? JSON.stringify(data) : data;
        let value = _.str.vsprintf("%s %s\n", [cur_time.format('yyyy-MM-dd HH:mm:ss'),content]);
        let relative_file_path = _.str.vsprintf("%s_%s_%s.log", [file_prefix,cur_time.format('yyyy-MM-dd'),level]);
        if(!fs.existsSync(this.log_path+"/"+relative_file_path)) {
            let dirs_list = file_prefix.split("/");
            dirs_list.pop();
            let dirs = "";
            for (let i = 0;i < dirs_list.length;++i) {
                if (this.isNull(dirs_list[i])) continue;
                dirs += "/" +dirs_list[i];
                if (!fs.existsSync(this.log_path+dirs)) {
                    console.log(this.log_path+dirs);
                    fs.mkdirSync(this.log_path+dirs);
                }
            }
            fs.writeFileSync(this.log_path+"/"+relative_file_path, '');
        }
        fs.appendFileSync(this.log_path+"/"+relative_file_path, value);
    };

    this.addLogs = function (file_prefix,data,level=that.level.info) {
        that.syncAddLogs(file_prefix,data,level);
    };


};
