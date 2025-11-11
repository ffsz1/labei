var config = require('../config');
var server = config.mysqlServers[0];
var model_base = require('./model_base.js');
const F = require('../common/function');
var co = require('co');
var redisLib = require('../libs/redis');
var redisClient = redisLib.redisClient;
var redisCo = redisLib.redisCo;
var C = require('../config');
var dbcacheCli = redisLib.dbcacheCli;
var dbcacheCo = redisLib.dbcacheCo;
var _ = require('underscore');
_.str = require('underscore.string');

function db_pool(table_name,model_mrg) {

    model_base.call(this);
    this.table = table_name;
    var model_map = model_mrg.model_map;

    this.addIdxDetailMap = function* (item,idx_list,idx_dbname_list,up_cond,expire,key) {
        for (let j = 0; j < idx_list.length; j++) {
            let idx = idx_list[j];
            let idx_dbname = idx_dbname_list[j];
            let idx_value = eval("item."+idx);
            if (F.isNull(idx_value) || F.isNull(idx_dbname) || F.isNull(up_cond)) {
                //TODO//F.addDebugLogs(["set dbcache null:",idx,idx_dbname,idx_value,up_cond]);
                return false;
            }
            // let ckey = F.vsprintf("dbcache#%s#%s#%s",[idx_dbname,idx_value,key]);
            // let cvalue = up_cond;
            // dbcacheCli.set(ckey, cvalue);
            // dbcacheCli.expire(ckey,expire);
            let ckey = F.vsprintf("dbcache#%s#%s",[idx_dbname,idx_value]);
            let cvalue = key+"#$&*^%dj53#"+up_cond;
            let expire_time = new Date().getTime()/1000 + expire;
            dbcacheCli.ZADD(ckey,expire_time,cvalue);
            dbcacheCli.expire(ckey,expire);
        }
        return true;
    }

    this.addIdxMap = function* (res,redis_param,expire,key) {
        if(F.isNull(redis_param.update_affect_cond)) return false;
        let idx_list = redis_param.idx.split(",");
        let idx_dbname_list = redis_param.idx_dbname.split(",");
        let up_cond = redis_param.update_affect_cond;
        if (Array.isArray(res)) {
            for (let i = 0; i < res.length; i++) {
                let item = res[i];
                let add_det_res = yield this.addIdxDetailMap(item,idx_list,idx_dbname_list,up_cond,expire,key);
                if (false == add_det_res) return false;
            }
        } else {
            let add_det_res = yield this.addIdxDetailMap(res,idx_list,idx_dbname_list,up_cond,expire,key);
            if (false == add_det_res) return false;
        }
        return true;
    }

    /**
     * 函数返回结果被redis缓存，注意：param_arr里不能传框架得对象，例如：不能包含事务connection
     * 例如 wrap('mgr_map.user_anchor.getIndexPageAnchorList',[ctx.I.start, ctx.I.step],redis_param,expire)
     * 因为mgr_map每个地方命名不一样，这里统一用这个命名
     * redis_param: {
     * idx: 数据集属性唯一索引 （如果返回值是数组，idx为每个元素（元素必须为字典）里的属性值，如果为字典，idx直接为字典里属性）
     * idx_dbname: idx 对应数据库名
     * update_affect_cond: 执行update时 如果这些属性名有变，则要删除该key
     * }
     */
    this.wrap = function* (fnname,param_arr=[],redis_param={},expire=300,cache_null=false) {
        let param_str = JSON.stringify(param_arr);
        let key = fnname+"("+param_str+")";
        let res = null;
		let next_sel_time = 0;
        if (C.is_open_sql_redis == true && !F.isNull(redis_param)) {
			let redis_res = yield dbcacheCo.hmget(key,["sel_time","del_time","value"]);
			if(!F.isNull(redis_res)) {
				let sel_time = redis_res[0];
				let del_time = redis_res[1];
				res = redis_res[2];
				let is_dirty = false; // 是否脏数据
				if (!F.isNull(sel_time) && !F.isNull(del_time) && parseInt(sel_time) < parseInt(del_time)) is_dirty = true;
				if (is_dirty == false && !F.isNull(res)) {
                    return JSON.parse(res);
				}
			}
            dbcacheCli.expire(key, expire); //提前延迟超时避免这过程有del操作没记录
			next_sel_time = yield F.getNextNoRoundId(dbcacheCo,"dbcache"); // 使用redis自增id来标示顺序,必须在select执行之前
        }
        var exe_cmd = fnname+"(";
        for (var i = 0; i < param_arr.length; i++) {
            if (i+1 == param_arr.length) exe_cmd += "param_arr["+i+"]";
            else exe_cmd += "param_arr["+i+"],";
        }
        exe_cmd += ")";
        //F.addDebugLogs(["exe cmd:",exe_cmd]);
        res = yield eval(exe_cmd);
        if (C.is_open_sql_redis == true && !F.isNull(redis_param)) {
            if (F.isNull(res)) {
                if (cache_null) {
                } else {
                    return res;
                }
            }

            if (!F.isNull(res)) {
                yield this.addIdxMap(res,redis_param,expire+60,key);
                //TODO//F.addDebugLogs(["dbcache bug set:",key]);

                //dbcacheCli.expire(key, expire);// 因为hmset不会修改timeout，可能执行完下一句hmset立马timeout，所以得提前设置一下
                yield this.dealRedisForInsert(redis_param, key, expire); // 处理insert redis的数据
            }

            yield dbcacheCo.hmset(key,"sel_time",next_sel_time, "value", JSON.stringify(res));
			dbcacheCli.expire(key, expire);
        }
        return res;
    }

    this.dealRedisForInsert = function* (redis_param, key, expire) {
        if(F.isNull(redis_param.insert_affect_cond) || F.isNull(redis_param.insert_affect_value)) return false;

        let where_fields_key = new Array();
        let where_fields_value = new Array();
        let insert_affect_cond_arr = redis_param.insert_affect_cond.split(',');
        for (var i = 0; i < insert_affect_cond_arr.length; i++) {
            where_fields_key.push(_.str.trim(insert_affect_cond_arr[i], ' '));
        }
        where_fields_value = redis_param.insert_affect_value;
        if(where_fields_key.length != where_fields_value.length) {
            F.addErrLogs(['wrap redis insert数组设置错误', {redis_param: redis_param}]);
            return false;
        }

        let insert_affect_table_dic = {};

        // 设置redis
        for (var i = 0; i < where_fields_key.length; i++) {
            let ckey = F.vsprintf("dbcache_insert#%s#%s",[where_fields_key[i], where_fields_value[i]]);
            let cvalue = key + "#$&*^%dj53#" + redis_param.update_affect_cond;
            let expire_time = new Date().getTime()/1000 + expire;
            dbcacheCli.ZADD(ckey, expire_time, cvalue);
            dbcacheCli.expire(ckey, expire);

            let table = where_fields_key[i].split('.')[0];
            if (table in insert_affect_table_dic) {
            } else {
                yield redisLib.redisCo.set("dbcache_insert_flag#"+table, "true"); // 添加此标志 db insert时才查redis并删除
                yield redisLib.redisCo.expire("dbcache_insert_flag#"+table, expire);
                insert_affect_table_dic[table] = 1;
            }
        }

        return true;
    }

    this.testwrap = function* (a0,a1,a2,a3,a4,a5,a6) {
        F.addDebugLogs(["test wrap:",a0,a1,a2,a3,a4,a5,a6]);
        return "model pool test wrap";
    }

    

}

module.exports = db_pool;
