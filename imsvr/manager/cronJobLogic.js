'use strict';
var C = require('../config');
var F = require('../common/function');
var _ = require('underscore');
_.str = require('underscore.string');
var async_request = require('request');
var crypto = require('crypto');
var co = require('co');

module.exports = function (app, common_mgr) {

    var model_map = app.model_mgr.model_map;
    var mgr_map = common_mgr.mgr_map;
    var db_timer_db = model_map.db_timer;
    var db_timer_bak_db = model_map.db_timer_bak;

    var dbTimerPrefix = F.isNull(C.dbTimerPrefix)?"":C.dbTimerPrefix;

    var last_allow_time = 0;
    let to_delete_job_ids = [];

    var that = this;

    this.get_stop_flag = function* () {
        // constant 加了 stop_cron 则不执行db定时器
        // let stop_cron = yield model_map.constant.query('', {
        //     fields: 'id',
        //     where: '`key` = ?',
        //     values: ['stop_cron']
        // });
        // return stop_cron;
        let job_stop_flag = yield mgr_map.redis.getJobStopFlag();
        return !F.isNull(job_stop_flag);
    };

    this.canExeJob = function* () {
        let job_count = 100;
        let cur_time = new Date().getTime();
        if (cur_time > last_allow_time + 10*1000) { // 超过10秒redis控制失效 解决mysql redis不同步问题
        } else {
            job_count = yield mgr_map.redis.checkJobs(cur_time+15000); // redis 控制 查看15秒内有无可执行任务
            if (F.isNull(job_count) || 0 == job_count) return 0;
            F.addDebugLogs(["job_count:",job_count]);
	    job_count = job_count + 10;
        }
        last_allow_time = cur_time;
        return job_count;
    }

    this.getTimerNextExeJob = function* (job_count) {
        let jobs = [];
        let cur_time = new Date().getTime() + 15000; // 抽出15秒内要执行的job
        var dbConnection = null;
        try {
            let job_list = yield db_timer_db.query(dbConnection,{
                fields: '*',
                order: 'next_exe_timestamp',
                where: 'dbTimerPrefix = ? and `status` = ? and next_exe_timestamp <= ?',
                values: [dbTimerPrefix, db_timer_db.status.noexcute,cur_time],
                //forUpdate: true,
                //limit: job_count
            });
	    if (F.isNull(job_list)) return jobs;
	    let job_id_list = [];
	    for (let i = 0; i < job_list.length; i++) {
	        job_id_list.push(job_list[i].id);
	    }
	    let upres =  yield db_timer_db.execute_raw(
			    "update db_timer set `status` = ? where id in (?) and `status` = ?",
			    [db_timer_db.status.excuting,job_id_list,db_timer_db.status.noexcute]);
	    for (let i = 0; i < job_list.length; i++) {
	        if (i + 1 == job_list.length) yield mgr_map.redis.delJob(job_list[i].id);
		else co(mgr_map.redis.delJob(job_list[i].id));
	    }
	    jobs = job_list;
            F.addOtherLogs('job/job',["exec job:",job_id_list]);

        } catch (e) {
            console.trace(e.stack);
            F.addErrLogs(["db_timer_exe err:",e.stack]);
            F.throwErr(e.toString());
        }
        return jobs;
    }

    this.db_timer_exe_detail = function* (next_job) {
        let err = '';
        try {
            let job_data = JSON.parse(next_job.data);
            let fn = eval("mgr_map."+next_job.key);
            yield fn(job_data);
        } catch (e) {
            err = e.stack;
            F.addErrLogs(["db_timer_exe_detail err:",e.stack]);
            if (F.isNull(e.code)) yield mgr_map.notice.sendDingTalk(err); // 钉钉预警
        }

        return err;
    };

    this.update_job_status = function* (next_job,err) {
        let new_status = F.isNull(err)?db_timer_db.status.excuteSuc:db_timer_db.status.excuteFail;
        if (!F.isNull(err)) {
            yield db_timer_bak_db.insert(null,{
                '`key`':next_job.key,
                'oldid':next_job.id,
                '`data`':next_job.data,
                'next_exe_timestamp':next_job.next_exe_timestamp,
                'next_exe_time':next_job.next_exe_time,
                '`status`':new_status,
                'fail_reason':C.inner_host+":"+app.port+" "+err,
		'dbTimerPrefix':dbTimerPrefix
            });
        }
	to_delete_job_ids.push(next_job.id);
        //yield db_timer_db.delete(null,{
        //    where: 'id = ?',
        //    values: [next_job.id]
        //})
    }

    this.realDeteleJob = function* () {
        if (to_delete_job_ids.length > 0) {
            let jobIds = JSON.parse(JSON.stringify(to_delete_job_ids));
	    let delLength = jobIds.length;// 这一步很关键因为下面一步会被协程切换
	    to_delete_job_ids.splice(0, delLength);
	    co(function* () {
                F.addOtherLogs('job/job',["del job:",jobIds]);
		yield db_timer_db.execute_raw(
			"delete from db_timer where id in (?)",[jobIds]);

	    });
	}
    }

    this.async_run = function* (next_job) {
        let err = yield that.db_timer_exe_detail(next_job);
        yield that.update_job_status(next_job,err);
    }

    // 每秒轮询执行db定时器得任务
    this.db_timer_exe = function* (cur_time) {

        co(that.realDeteleJob()); // 删除已完成的任务

        try {

            let wait_time = F.RandomNumBoth(0,800);
            yield F.sleep(wait_time); // 保证每个进程的job getter都有随机机会抢到执行

            let lock = yield mgr_map.redis.setnx("dbtimer_lock", 60*60);
            if(lock == false) return true; // 一刻只保留一个job getter就够了

            let job_count = yield that.canExeJob();
            if (job_count > 0) {
                let next_job_list = yield that.getTimerNextExeJob(job_count);
		for (let i = 0; i < next_job_list.length; i++) {
		    let next_job = next_job_list[i];
                    co(function* () {
                        let cur_time = new Date().getTime();
                        let wait_time = next_job.next_exe_timestamp - cur_time;
                        if (wait_time > 0) {
                            yield F.sleep(wait_time); // 还没到执行时间
                        }
                        yield that.async_run(next_job);
                    });
		}
            }

            yield mgr_map.redis.del("dbtimer_lock");
        } catch (e) {
            F.addErrLogs(["db_timer_exe err:",e.stack]);
            yield mgr_map.redis.del("dbtimer_lock");
        }

    };



    this.batchAddJob = function() {
        // db 定时器
        mgr_map.cronJob.addJob("*-*-* *:*:*", that.db_timer_exe);

        // 删除一个月前的db_timer_bak 每天执行一次 03:25:00
        mgr_map.cronJob.addJob("*-*-* 12:25:00", mgr_map.dbTimer.dealDbTimeerBakData);


        //mgr_map.cronJob.addJob("*-*-* *:*:*/3", mgr_map.dbTimer.addRobotTime);
        //mgr_map.cronJob.addJob("*-*-* *:*:*/60", mgr_map.dbTimer.addRobotTime2);
        //mgr_map.cronJob.addJob("*-*-* *:*:*/60", mgr_map.dbTimer.addRobotTime3);



        //mgr_map.cronJob.addJob("*-*-* *:*:*/3", mgr_map.dbTimer.exeBakRtmpJpg);

        //mgr_map.cronJob.addJob("*-*-* *:*:*", mgr_map.dbTimer.yace);


        //mgr_map.cronJob.addJob("*-*-* *:*:*/5", mgr_map.room.testpush);


        //mgr_map.cronJob.addJob("*-*-* 03:05:00", mgr_map.growing.batchLowerIntimacy);

        //// 定时QN私信汇报 每10分钟
        //mgr_map.cronJob.addJob("*-*-* *:*/10:00", mgr_map.consume.timerToQnCollect);

        //// 每20秒检查公告并发送
        //mgr_map.cronJob.addJob("*-*-* *:*:*/20", mgr_map.room.periodsendAnnouncement);
        //
        //// 删除创建10天前的游客账号 每天执行一次 03:15:00
        //mgr_map.cronJob.addJob("*-*-* 03:15:00", mgr_map.collect.dealVisitorAccount);


        //// 删除10天前的login_event 每天执行一次 03:30:00
        //mgr_map.cronJob.addJob("*-*-* 03:30:00", mgr_map.loginEvent.dealLoginEventData);

        //// 格式化观众每日被动私信新关系数 每天执行一次 00:00:00
        //mgr_map.cronJob.addJob("*-*-* 00:00:00", mgr_map.user.initDayMessageNum);
    };

};

