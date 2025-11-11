'use strict';

const C = require('../config');
const F = require('../common/function');
const fs=require('fs');
const path = require('path');
const _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');
var logs_obj = require('../libs/logs.js');
var logs = new logs_obj();

module.exports = function (app, commonManager) {

    let that = this;
    // 日志文件夹路径
    this.log_path = path.resolve(__dirname, '../logs');
    this.qn_curl_log_dir_path = this.log_path + '/qn_curl';
    // qn curl log路径
    this.qn_curl_log_path = this.qn_curl_log_dir_path + '/curl_qn_%s.log';
    // qn curl error log路径
    this.qn_curl_error_log_path = this.qn_curl_log_dir_path + '/curl_qn_error_%s.log';
    // qn curl 临时扣费失败路径
    this.qn_curl_interim_consume_fail_log = this.log_path + '/qn_curl_interim_consume_fail.log';
    // 表数据转移失败路径
    this.collect_move_table_fail_log = this.log_path + '/collect_move_table_fail.log';

    this.level = {debug: 'debug', info: 'info', warning: 'warning', error: 'error'};

    /**
     * 格式化内容
     * @param data object 例如{'name':'xxx'}
     * @returns {string}
     */
    this.formatData = function (data) {
        let data_str = F.fDatetime('yyyy-MM-dd HH:mm:ss') + ' ';
        for(let key in data) {
            let value = typeof data[key] == 'object' ? JSON.stringify(data[key]) : data[key];
            data_str += key + ':' + value + ' ';
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

    /**
     * 添加qn curl记录
     * @param url string
     * @param params 参数object
     * @param result curl返回结果object
     * @returns {}
     */
    this.addQnCurlLogs = function (url, params, result) {
        if(C.is_log_qn_curl != true) return ;
        if(!fs.existsSync(this.qn_curl_log_dir_path)) {
            fs.mkdirSync(this.qn_curl_log_dir_path);
        }
        let log_path = _.str.vsprintf(this.qn_curl_log_path, [F.fDatetime('yyyyMMdd')]);
        if(!fs.existsSync(log_path)) {
            fs.writeFile(log_path, '');
        }

        that.writeFile(log_path, {
            'url':url,
            'params':params,
            'result':result
        });
    };

    /**
     * 添加qn curl失败记录
     * @param url string
     * @param params 参数object
     * @param result curl返回结果object
     * @returns {}
     */
    this.addQnCurlErrorLogs = function (url, params, result) {
        let log_path = _.str.vsprintf(this.qn_curl_error_log_path, [F.fDatetime('yyyyMMdd')]);
        if(!fs.existsSync(this.qn_curl_log_dir_path)) {
            fs.mkdirSync(this.qn_curl_log_dir_path);
        }
        if(!fs.existsSync(log_path)) {
            fs.writeFile(log_path, '');
        }

        that.writeFile(log_path, {
            'url':url,
            'params':params,
            'result':result
        });
    };


    this.addLogs = function (file_prefix,data,level=that.level.info) {
        logs.addLogs(file_prefix,data,level);
    };

    /**
     * 添加qn 临时扣费失败记录
     * @param params 参数object
     * @param result curl返回结果object
     * @returns {}
     */
    this.addQnInterimConsumeFailLogs = function (params, result) {
        let log_path = this.qn_curl_interim_consume_fail_log;
        if(!fs.existsSync(log_path)) {
            fs.writeFile(log_path, '');
        }
        that.writeFile(log_path, {
            'params':params,
            'result':result
        });
    };

    /**
     * 添加男士进入房间流程log
     * @param title 说明
     * @param data
     * @returns {}
     */
    this.roomManInLogs = function (title, data) {
        let content = {'title':title};
        if(!F.isNull(data)) {
            for (let key in data) {
                content[key] = data[key];
            }
        }
        that.addLogs('room_man_in/process', content, that.level.debug);
        that.addLogs('sys/sys', content, that.level.debug);
    };

    /**
     * 扣费定时器报错日志
     * @param title 说明
     * @param data
     * @returns {}
     */
    this.timerErrLogs = function (title, data) {
        let content = {'title':title};
        if(!F.isNull(data)) {
            for (let key in data) {
                content[key] = data[key];
            }
        }
        that.addLogs('room_man_in/timer_err', content, that.level.debug);
        that.addLogs('sys/sys', content, that.level.debug);
    };

    /**
     * 扣费定时器日志
     * @param title 说明
     * @param data
     * @returns {}
     */
    this.timerConsumeLogs = function (title, data) {
        let content = {'title':title};
        if(!F.isNull(data)) {
            for (let key in data) {
                content[key] = data[key];
            }
        }
        that.addLogs('room_man_in/timer_consume', content, that.level.debug);
        that.addLogs('sys/sys', content, that.level.debug);
    };

    /**
     * 添加男士异常离开直播间流程log
     * @param title 说明
     * @param data
     * @returns {}
     */
    this.leaveRoomExceptionLogs = function (title, data) {
        let content = {'title':title};
        if(!F.isNull(data)) {
            for (let key in data) {
                content[key] = data[key];
            }
        }
        console.log(title);console.log(content);
        that.addLogs('room_man_leave_exception/process', content, that.level.debug);
    };

    /**
     * 获取logs日志列表
     * @param title 指定文件夹
     * @returns {*}
     */
    this.getLogsList = function (title = '') {
        // let logs_path = path.resolve(__dirname, '../logs/');
        let logs_path = 'logs/';
        let folders = fs.readdirSync(logs_path);
        let down_files = new Array();
        let file_path;
        folders.forEach(function(file, index) {
            file_path = logs_path + '/' + file;
            if(fs.statSync(file_path).isDirectory()) {
                let files = fs.readdirSync(file_path);
                down_files.push({'prefix':encodeURI(file_path), 'name':file, 'is_folder':true, 'sub_file':files});
            }else {
                down_files.push({'prefix':encodeURI(logs_path), 'name':file, 'is_folder':false, 'sub_file':[]});
            }
        });
        return down_files;
    };

    /**
     * 读取文件内容
     * @param filename
     * @returns {*}
     */
    this.readFile = function (filename) {
        let res;
        return new Promise(function(resolve, reject) {
            fs.readFile(filename, function(err, data) {
                if(err) {
                    res = {'result':false};
                }else {
                    res = {'result':true, 'data':data};
                }
                resolve(res);
            });
        });
    };

    /**
     * 添加表数据转移失败日志
     * @param params
     * @param errno
     * @param err_stack
     * @returns {}
     */
    this.addCollectMoveTableErrorLogs = function (params, errno, err_stack) {
        let log_path = this.collect_move_table_fail_log;
        if(!fs.existsSync(log_path)) {
            fs.writeFile(log_path, '');
        }

        that.writeFile(log_path, {
            'params':params,
            'errno':errno,
            'err_stack':err_stack
        });
    };

    /**
     * 获取文件列表
     * @param title 指定文件夹
     * @returns {*}
     */
    this.getFileList = function (title = '') {
        let folder_map = [
            'common',
            'config',
            'libs',
            'manager',
            'model',
            'routes'
        ];
        let down_files = new Array();
        for (var i = 0; i < folder_map.length; i++) {
            let folder_path = folder_map[i];
            let files = fs.readdirSync(folder_path);
            down_files.push({'prefix':encodeURI(folder_path), 'name':folder_map[i], 'is_folder':true, 'sub_file':files});
        }
        return down_files;
    };

    /**
     * 获取日志内容一行长度
     * @param content
     * @param find_str
     * @returns {int}
     */
    this.getLineLengthFromBuffer = function (content, find_str = F.fDatetime('yyyy-MM-dd')) {
        let index = -1; // 目标索引
        let pre_index_char; // 上一个字符
        let offset = 1; // 搜索位置偏移量 - 避免搜索第一个
        let count = 0; // 计数器 - 避免死锁
        while(true && count < 10) {
            index = content.indexOf(find_str, offset);
            if(index == -1) break;
            pre_index_char = content.substr(index - 1, 1);
            if(pre_index_char == '\n') break;

            offset = index + 1;
            count++;
        }

        return index;
    };

    /**
     * 读取文件内容
     * @param filename
     * @returns {*}
     */
    this.readFileContent = function (fs, fd, buffer, start, length, offset = null) {
        let res;
        return new Promise(function(resolve, reject) {
            fs.read(fd, buffer, start, length, offset, function (err, byte, read_buffer) {
                if(err) {
                    console.log('errrrr');
                    console.log(err);
                    res = {'result':false};
                }else {
                    res = {'result':true, 'buffer':read_buffer, 'byte':byte};
                }
                resolve(res);
            });
        });
    };

    /**
     * 获取fd
     * @param filename
     * @returns {*}
     */
    this.getFdFromFs = function (fs_param, path) {
        let res;
        return new Promise(function(resolve, reject) {
            fs_param.open(path, 'r', function (err, fd) {
                if(err) {
                    res = {'result':false};
                }else {
                    res = {'result':true, 'fd':fd};
                }
                resolve(res);
            });
        });
    };

    /**
     * 搜索日志内容
     * @param path
     * @param find_arr 查询的字符串数组
     * @param line_str 行开始的标识
     * @returns {*}
     */
    this.searchLogsContent = function* (path, find_arr, line_str) {
        // 重新排序搜素数组
        find_arr.sort(function(v1, v2) {
            return v1 < v2;
        });

        let fd_res = yield that.getFdFromFs(fs, path);
        let buffer_size = 1000;
        let buffer = new Buffer(buffer_size);
        let content_buffer;
        let content;
        let length;
        let pre_content = ''; // 上一次剩余字符串
        let content_array;
        let search_result = new Array();
        while(true) {
            content_array = new Array();
            content_buffer = yield that.readFileContent(fs, fd_res.fd, buffer, 0, buffer_size);
            if(content_buffer.result == false) break;
            if(content_buffer.byte == 0) break;

            content = content_buffer.buffer.slice(0, content_buffer.byte).toString();
            content = pre_content + content;
            pre_content = '';

            while(true) {
                length = that.getLineLengthFromBuffer(content, line_str);
                if(length == -1) {
                    pre_content = content.substr(0);
                    break;
                }
                content_array.push(content.substr(0, length));
                content = content.substr(length);
            }
            if(!F.isNull(content_array)) {
                // 搜索
                for (let i = 0; i < content_array.length; i++) {
                    let is_in = true;
                    for (let j = 0; j < find_arr.length; j++) {
                        if(content_array[i].indexOf(find_arr[j]) == -1) {
                            is_in = false;
                            break;
                        }
                    }
                    if(is_in == true) search_result.push(content_array[i]);
                }
            }
        }
        fs.close(fd_res.fd);
        return {search_result:search_result};
    };

};
