'use strict';
var C = require('../config');
var F = require('../common/function');
var _ = require('underscore');
_.str = require('underscore.string');
var async_request = require('request');
var crypto = require('crypto');
var co = require('co');

function cronJobMgr(app, common_mgr) {

    var model_map = app.model_mgr.model_map;
    var mgr_map = common_mgr.mgr_map;

    this.job_list = [];
    var that = this;


    // fn必须是异步函数
    this.addJob = function (time_str,fn) {
        //if (F.isTime(time_str)) {
            this.job_list.push([time_str,fn]);
        //} else {
            //F.log("error:","add job err: timestr %s format not right",[time_str]);
        //}
    };

    this.isSubMatch = function(systime,selftime) {
        systime = systime.toString();
        selftime = selftime.toString();
        if ("*" == selftime) return true;
        if (systime == selftime) return true;
        if (selftime.indexOf("*/") == 0) {
            var ival = parseInt(systime);
            selftime = selftime.replace("*/",ival.toString()+"%");
            var res = eval(selftime);
            return res == 0;
        }
        return false;
    };
    this.isMatch = function(systime,selftime) {
        var [y,M,d,h,m,s] = selftime.split(/[- :]/);
        var [sysy,sysM,sysd,sysh,sysm,syss] = systime.split(/[- :]/);
        if (that.isSubMatch(sysy,y) && that.isSubMatch(sysM,M) && that.isSubMatch(sysd,d) &&
            that.isSubMatch(sysh,h) && that.isSubMatch(sysm,m) && that.isSubMatch(syss,s)) {
            return true;
        }
        //console.log(selftime,systime,"not match");
        return false;
    };

    this.execJob = function* (cur_time_str) {
        for (var i = 0; i < that.job_list.length; ++i) {
            var [time_str,fn] = that.job_list[i];
            if (that.isMatch(cur_time_str,time_str)) {
                try {
                    co(fn(cur_time_str));
                } catch (e) {
                    console.log(e);
                    F.addErrLogs(["execJob err:",e.stack]);
                }
            }
        }
    };

    var cur_time = null; // 计数时间 秒,为了保证每秒都执行
    var sys_time = null; // 系统时间 秒,为了保证跟上系统时间

    this.start = function() {
        var genfn =  function* () {
            yield mgr_map.dbTimer.revertLastRunningJob();
            while(1) {
                yield F.sleep(1000);
                if (cur_time == null) cur_time = parseInt(new Date().getTime()/1000);
                sys_time = parseInt(new Date().getTime()/1000);
                //F.addErrLogs(["exe job:",cur_time,sys_time]);
                while (cur_time <= sys_time) {
                    var runtime = new Date();
                    runtime.setTime(cur_time*1000);
                    var cur_time_str = runtime.format("yyyy-MM-dd HH:mm:ss");
                    //F.addErrLogs(["exe job cur_time_str:",cur_time_str]);
                    co(that.execJob(cur_time_str));
                    cur_time = cur_time + 1;
                }
            }
        };
        co(genfn());
    }

};

module.exports = cronJobMgr;
