'use strict';

var mysqlLib = require('../libs/mysql.js');
var C = require('../config');
var F = require('../common/function.js');
var _ = require('underscore');
_.str = require('underscore.string');
var redis = require('../libs/redis');
var redisLib = require('../libs/redis');
var redisClient = redisLib.redisClient;
var redisCo = redisLib.redisCo;
var dbcacheCli = redisLib.dbcacheCli;
var dbcacheCo = redisLib.dbcacheCo;
var server = C.mysqlServers[0];

var com_mysql = new mysqlLib();

function model_base() {

    this.table = '';

    this.uni_index = '';

    this.mysql = com_mysql;

    this.table_default_value_map = {};

    var that = this;


    this.getNextInsertId = function* (uniid) {
        var min = 99999999;
        var key = "mycatinsertid";
        var next_id = yield redisCo.HINCRBY(key,uniid,1);
        next_id = parseInt(next_id);
        if (next_id > 9007199254730993 && next_id % 100000 == 0) {
            yield redisCo.hmset(key,uniid,1);
        }
        return min+next_id;
    };

    this.getNextTransactionId = function* () {
        var key = "mysql_transaction_incr_key";
        var field = 'mysql_transaction';
        var next_id = yield redisCo.HINCRBY(key,field,1);
        next_id = parseInt(next_id);
        if (next_id > 9999999999999 && next_id % 100000 == 0) {
            yield redisCo.hmset(key,field,1);
        }
        return next_id;
    };

	/*
	 * 查询
	 * options object 查询操作,属性包括：
	 ** fields string 默认*
	 ** where string 默认空
	 ** values array 默认空数组
	 ** limit string 默认空
	 ** order string 默认空
	 ** use_redis bool 是否使用缓存 默认false
	 ** join 链表语句 默认空
	 ** as 表别名 默认空
	 ** sub_table 子查询
	 */
    this.query = function* (connection, options) {
        if(typeof options != 'object') F.throwErr('mysql query sql error:options is not object.');
        if(F.isNull(options.fields)) options.fields = '*';

        var key;
		/*
        if(F.isNull(connection) && F.isNull(options.forUpdate) && !F.isNull(options.use_redis) && F.isNull(options.join)
            && options.use_redis == true && C.is_open_sql_redis == true) {
            this.checkWhereToRedis(options);
            var fields_redis = this.formatFieldsToRedis(options.fields);console.log(fields_redis);
            var where_redis = this.formatWhereToRedis(options.where, options.values);console.log(where_redis);
            key = this.getQueryRedisKey(fields_redis, where_redis + '&', options);console.log(key);
            var res = yield redis.redisCo.get(key);
            if(!F.isNull(res)) {
                F.addDebugLogs(["redis get:",key]);
                return JSON.parse(res);
            }
        }*/

        var table = F.isNull(options.sub_table) ? this.table : options.sub_table;

        var server = C.mysqlServers[0];

        if(!F.isNull(server.is_mycat) && !F.isNull(options.group)) { // 兼容mycat 语法
            let group_dic = this.commaStrToDic(options.group);
            let field_dic = this.commaStrToDic(options.fields);
            for (let k in group_dic) {
                if (!(k in field_dic)) {
                    options.fields += "," + k;
                }
            }
        }

        if(!F.isNull(server.is_mycat) && !F.isNull(options.order)) { // 兼容mycat 语法
            let order_dic = this.commaStrToDic(options.order);
            let field_dic = this.commaStrToDic(options.fields);
            for (let k in order_dic) {
                if (!(k in field_dic)) {
                    options.fields += "," + k;
                }
            }
        }



        var sql = `SELECT ${options.fields} FROM ${table}`;

        if(!F.isNull(options.as)) {
            sql += ` as ${options.as}`;
        }
        if(!F.isNull(options.join)) {
            sql += ` ${options.join}`;
        }
        if(!F.isNull(options.where)) {
            sql += ` WHERE ${options.where}`;
        }else {
            options.values = new Array();
        }
        if(!F.isNull(options.group)) {
            sql += ` GROUP BY ${options.group}`;
        }
        if(!F.isNull(options.having)) {
            sql += ` HAVING ${options.having}`;
        }
        if(!F.isNull(options.order)) {
            sql += ` ORDER BY ${options.order}`;
        }
        if(!F.isNull(options.limit)) {
            sql += ` LIMIT ${options.limit}`;
        }
        if(!F.isNull(options.before)) {
            sql = options.before + ';' + sql;
        }

        var exec_conn = F.isNull(connection) ? that.mysql : connection;
        let start_time = new Date().getTime();
        var res = yield exec_conn.query(sql, options.values);
        if(!F.isNull(connection)) {
            let end_time = new Date().getTime();
            F.addDebugLogs([`con key:${connection.name}`,`usetime:${end_time-start_time}`,sql,options.values]);
            if (end_time - start_time > C.slow_log_delta) F.addSlowLogs([`con key:${connection.name}`,`usetime:${end_time-start_time}`,sql,options.values]);
        }

        if (!F.isNull(connection)) res = res[0];
		/*
        if(F.isNull(connection) && F.isNull(options.forUpdate) && !F.isNull(options.use_redis)
            && options.use_redis == true && C.is_open_sql_redis == true) {
            if (F.isNull(res)) {
                F.addErrLogs(["redis get:",key,"is null"]);
                return res;
            }
            yield redis.redisCo.set(key, JSON.stringify(res));
            var expire = 180;
            if (!F.isNull(options.expire)) expire = options.expire;
            yield redis.redisCo.expire(key, expire);
            yield this.setWhereGroup(options.where);
        }*/
        return res;
    }

    this.commaStrToDic = function (str) {
        let new_str = str;
        let item_list = new_str.split(',')
        let dic = {}
        for (let oi = 0; oi < item_list.length; oi++) {
            let item = item_list[oi];
            item = _.str.trim(item, ' ');
            if (F.isNull(item)) continue;
            let low_item = item;
            if (low_item.indexOf(" as ") >= 0) {
                let item_sub_list = low_item.split(" as ");
                let as_item = item_sub_list.pop();
                as_item = _.str.trim(as_item, ' ');
                dic[as_item] = 1;
                item = item.split('as')[0]
            }
            if (low_item.indexOf(" AS ") >= 0) {
                let item_sub_list = low_item.split(" AS ");
                let as_item = item_sub_list.pop();
                as_item = _.str.trim(as_item, ' ');
                dic[as_item] = 1;
                item = item.split('AS')[0]
            }
            if (item.indexOf("(") >= 0 || item.indexOf(")") >= 0) {
                continue;
            }
            dic[item.split(' ')[0]] = 1;
        }
        return dic;
    }

	/* 返回一条结果 */
    this.queryOne = function* (connection, options) {
        let res = yield this.query(connection, options);
        return F.isNull(res) ? {} : res[0];
    }

	/*
	 * 插入
	 * values object (such as:{"name":"test","age":23})
	 */
    this.insert = function* (connection, values){
        if(F.isNull(values)) F.throwErr('values is null');
        if(!F.isNull(that.preInsert)) {
            values = yield that.preInsert(connection, values);
        }
        var exec_conn = F.isNull(connection) ? that.mysql : connection;
        var server = C.mysqlServers[0];
	    if (!F.isNull(that.auto_index) && !(that.auto_index in values) && !F.isNull(server.is_mycat)) {
		    values[that.auto_index] = 'next value for MYCATSEQ_GLOBAL';//yield that.getNextInsertId(that.table);
	    }
        let newVals = [];
        var keys = Object.keys(values);
	    for(let i=0;i<keys.length;i++){
	        if(keys[i].indexOf("`")==-1){
                newVals["`"+keys[i]+"`"] = values[keys[i]];
            }else{
	            newVals[keys[i]] = values[keys[i]];
            }
        }
        values = newVals;
        keys = Object.keys(values);
        var vals = F.values(values);
        var keys_str = keys.join();
        var vals_str = "";
        for (var i = 0; i < keys.length; i++) {
            vals_str += "?,";
        }
        vals_str = _.str.trim(vals_str, ',');
        var sql = `INSERT INTO ${this.table}(${keys_str}) VALUES(${vals_str})`;
        let start_time = new Date().getTime();
        var res = yield exec_conn.query(sql, vals);
        var insert_res;
        if(!F.isNull(connection)) {
            let end_time = new Date().getTime();
            F.addDebugLogs([`con key:${connection.name}`,`usetime:${end_time-start_time}`,sql,vals]);
            if (end_time-start_time > C.slow_log_delta) F.addSlowLogs([`con key:${connection.name}`,`usetime:${end_time-start_time}`,sql,vals]);
            insert_res = res[0].insertId;
        }else {
            insert_res = res.insertId;
        }
        if (C.is_open_sql_redis == true && insert_res > 0) {
            let to_de_redkey = yield this.getInsertRedisDelKey(values);
            yield this.delDbcache(to_de_redkey, connection);
        }
        return res;
    }

    /**
     * 存在更新，不存在插入(注意，必须存在唯一索引才可使用)
     * @param connection
     * @param values 插入的数据values object (such as:{"name":"test","age":23})
     */
    this.insertOnUpdate = function* (connection, values){
        if(F.isNull(values)) F.throwErr('values is null');
        var exec_conn = F.isNull(connection) ? that.mysql : connection;
        var server = C.mysqlServers[0];
        if (!F.isNull(that.auto_index) && !(that.auto_index in values) && !F.isNull(server.is_mycat)) {
            values[that.auto_index] = 'next value for MYCATSEQ_GLOBAL';//yield that.getNextInsertId(that.table);
        }
        var keys = Object.keys(values);
        var vals = F.values(values);
        vals = vals.concat(vals);
        var keys_str = keys.join();
        var vals_str = "";
        var up_vals_str = "";
        for (var i = 0; i < keys.length; i++) {
            vals_str += "?,";
        }
        vals_str = _.str.trim(vals_str, ',');
        for(let key of keys){
            up_vals_str += `${key}=?,`;
        }
        up_vals_str = _.str.trim(up_vals_str, ',');

        var sql = `INSERT INTO ${this.table}(${keys_str}) VALUES(${vals_str}) ON DUPLICATE KEY UPDATE ${up_vals_str}`;
        let start_time = new Date().getTime();
        var res = yield exec_conn.query(sql, vals);
        var insert_res;
        if(!F.isNull(connection)) {
            let end_time = new Date().getTime();
            F.addDebugLogs([`con key:${connection.name}`,`usetime:${end_time-start_time}`,sql,vals]);
            if (end_time-start_time > C.slow_log_delta) F.addSlowLogs([`con key:${connection.name}`,`usetime:${end_time-start_time}`,sql,vals]);
            insert_res = res[0].insertId;
        }else {
            insert_res = res.insertId;
        }
        if (C.is_open_sql_redis == true && insert_res > 0) {
            let to_de_redkey = yield this.getInsertRedisDelKey(values);
            yield this.delDbcache(to_de_redkey, connection);
        }
        return res;
    }

	/*
	 * 批量插入
	 * values object (such as:[{"name":"test1","age":23},{"name":"test2","age":23},{"name":"test3","age":23}])
	 */
    this.insertAll = function* (connection, values){
        if(F.isNull(values)) F.throwErr('keys or values is null');
        var keys = Object.keys(values[0]);
        var vals =[];
        var vals_str = '';
        var keys_str = keys.join();
        for(var j=0; j<values.length; j++){
            vals = vals.concat(F.values(values[j]));
            vals_str += "(";
            for (var i = 0; i < keys.length; i++) {
                vals_str += "?,";
            }
            vals_str = _.str.trim(vals_str, ',');
            vals_str += "),";
        }
        vals_str = _.str.trim(vals_str, ',');
        var sql = `INSERT INTO ${this.table}(${keys_str}) VALUES ${vals_str}`;

        var exec_conn = F.isNull(connection) ? that.mysql : connection;
        let start_time = new Date().getTime();
        var res = yield exec_conn.query(sql, vals);
        if(!F.isNull(connection)) {
            let end_time = new Date().getTime();
            F.addDebugLogs([`con key:${connection.name}`,`usetime:${end_time-start_time}`,sql,vals]);
            if (end_time-start_time > C.slow_log_delta) F.addSlowLogs([`con key:${connection.name}`,`usetime:${end_time-start_time}`,sql,vals]);
        }
        return res;
    }

    this.getUpdateAfterDelRedisKeys = function* (option, before_values) {
        let keys = {};
        if(!F.isNull(option.update_values)) {
            keys = JSON.parse(JSON.stringify(option.update_values));
        }
        if(!F.isNull(option.update_string)) {
            let fields_arr = option.update_string.split(',');
            let index = 0;
            for (var i = 0; i < fields_arr.length; i++) {
                let field_arr = fields_arr[i].split('=');
                if(field_arr[1].indexOf('+') != -1 || field_arr[1].indexOf('-') != -1) continue;
                let field_name = _.str.trim(field_arr[0], ' ');
                let field_value;
                if(field_arr[1].indexOf('?') != -1) {
                    field_value = before_values[index++];
                }else {
                    field_value = _.str.trim(field_arr[1], ' ');
                }
                keys[field_name] = field_value;
            }
        }
        return yield this.getInsertRedisDelKey(keys, false);
    }

    this.getInsertRedisDelKey = function* (values, is_select_default = true) {
        let redis_key = new Array();
        let insert_flag =  yield redisLib.redisCo.get("dbcache_insert_flag#"+this.table);
        if (F.isNull(insert_flag)) return redis_key;
        let keys = {};
        if(is_select_default == true) {
            let default_value = yield this.getFieldDefaultValue(this.table);
            for (var field in default_value) {
                if(F.isNull(values[field])) {
                    if(default_value[field] != 'db_null') {
                        keys[field] = default_value[field];
                    }
                }else {
                    keys[field] = values[field];
                }
            }
        }else {
            keys = values;
        }

        for (var field in keys) {
            redis_key.push(F.vsprintf("dbcache_insert#%s#%s",[this.table + '.' + field, keys[field]]));
        }
        return redis_key;
    }

    /* 获取表字段默认值 
    * table 表名
    * field 查询的字段名 为空则查询所有
    */
    this.getFieldDefaultValue = function* (table, field = '') {
        if(field == '') {
            if(!F.isNull(this.table_default_value_map[table])) return this.table_default_value_map[table];

            let table_info = yield that.mysql.query('desc ' + table);
            if(F.isNull(table_info)) F.throwErr('ER_NO_SUCH_TABLE');

            let field_info = {};
            for (var i = 0; i < table_info.length; i++) {
                field_info[table_info[i].Field] = table_info[i].Default != null ? table_info[i].Default : 'db_null';
            }
            this.table_default_value_map[table] = field_info;
            return field_info;
        }
        
        try{
            let field_default = yield that.mysql.query(`select default(${field}) as default_value from ${table} limit 1`);
            if(F.isNull(field_default)) F.throwErr('ER_BAD_FIELD_ERROR or ER_NO_SUCH_TABLE');
            return field_default[0].default_value;
        }catch(e) {
            return 'db_null';
        }
    }

    this.getCond = function* (res,to_de_redkey) {
        var index_list = this.uni_index.split(",");
        var cond = "(";
        for (var i = 0; i < index_list.length; i++) {
            if (index_list[i] in res) {
                var idval = eval("res."+index_list[i]);
                cond = cond + "'"+idval+"',";
                let ckey = F.vsprintf("dbcache#%s.%s#%s",[this.table,index_list[i],idval]);
                to_de_redkey.push(ckey);
            } else {
                cond = cond +"null,"
            }
        }
        cond = _.str.trim(cond, ',');
        cond = cond + "),";
        return cond;
    }

    this.get_uni_index = function* (connection=null) {
        var exec_conn = F.isNull(connection) ? that.mysql : connection;
        var usage_table = 'KEY_COLUMN_USAGE';
        var server = C.mysqlServers[0];
        if (F.isNull(server.is_mycat)) {
            usage_table = "INFORMATION_SCHEMA.KEY_COLUMN_USAGE";
        }
        var sql = "SELECT COLUMN_NAME FROM " + usage_table +
            " where CONSTRAINT_SCHEMA = '"+that.mysql.conf.database+"' and TABLE_NAME = '"+this.table+"' and CONSTRAINT_NAME = 'PRIMARY'"
        var res = yield exec_conn.query(sql);
        if (!F.isNull(connection)) res = res[0];
	console.log("uniindex:",res);
        var COLUMN_NAME = '';
        for (var i = 0; i < res.length; i++) {
            COLUMN_NAME = COLUMN_NAME + res[i].COLUMN_NAME + ",";
        }
        COLUMN_NAME = _.str.trim(COLUMN_NAME, ',');
        return COLUMN_NAME;
    }

    this.getWhereSqlByUniIndex = function* (connection=null,options,to_de_redkey,update_data=null) {
        var exec_conn = F.isNull(connection) ? that.mysql : connection;
        var sql = `select ${this.uni_index} from ${this.table} where ${options.where}`
        if(!F.isNull(options.limit)) {
            sql += " limit " + options.limit;
        }
        var res = yield exec_conn.query(sql, options.values);
        if (!F.isNull(connection)) res = res[0];
        var in_str = ` where (${this.uni_index}) in (`;
        if (res.length > 0) {
            for (var i = 0; i < res.length; i++) {
                in_str  += yield this.getCond(res[i],to_de_redkey);
            }
        } else {
            in_str += yield this.getCond({},to_de_redkey);
        }
        in_str = _.str.trim(in_str, ',');
        in_str = in_str + ")";
        if (!F.isNull(update_data)) {
            in_str = in_str + " and " + options.where;  // 把update where条件中加入 主键索引 避免死锁
            update_data.push.apply(update_data, options.values);
        }
        return in_str;
    }


    this.splitUpdateStr = function* (options,update_key) {
        if(F.isNull(options.update_string)) return [];

        // find update key
        var sub_str = options.update_string.split(',');
        for (var j in sub_str) {
            var up_str = sub_str[j];
            if (F.isNull(up_str)) continue;
            var vlist = up_str.split("=");
            var key = vlist[0];
            key = _.str.trim(key, ' ');
            key = _.str.trim(key, ',');
            key = _.str.trim(key, ' ');
            update_key.push(this.table+"."+key);
        }

        var update_string = options.update_string;
        var re = new RegExp("\\?","g");
        var arr = update_string.match(re);
        if (F.isNull(arr)) return [];
        var arr_len = arr.length;
        var before_where_values = [];
        var where_values = [];
        for (var i = 0; i < options.values.length; i++) {
            if (i<arr_len) before_where_values.push(options.values[i]);
            else where_values.push(options.values[i]);
        }
        options.values = where_values;
        return before_where_values;
    }


    this.updateKeyMatchCond = function (cond,up_key_arr) {
        let cond_list = cond.split(",");
        for (var j = 0; j < cond_list.length; j++) {
            var cond_item = cond_list[j];
            for (var i = 0; i < up_key_arr.length; i++) {
                var up_key = up_key_arr[i];
                if (cond_item.indexOf(up_key) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    this.delTimeoutKey = function*(idx_value_key,cur_time) {
        let timeout_key_list = yield dbcacheCo.zrangebyscore([idx_value_key,0,cur_time]);
        for (var i = 0; i < timeout_key_list.length; i++) {
            let key_cond = timeout_key_list[i];
            dbcacheCli.zrem(idx_value_key,key_cond);
        }
    };

    this.delDbcache = function* (to_de_redkey,connection,update_key=[]) {
        let cur_time = new Date().getTime()/1000 - 600; // 因为服务是集群，担心时间有误差，给个10分钟缓冲
		let del_time = yield F.getNextNoRoundId(dbcacheCo,"dbcache"); //删除redis 时间 必须在sql执行之后
        for (let j = 0; j < to_de_redkey.length; j++) {
            let idx_value_key = to_de_redkey[j]; // idx
            yield this.delTimeoutKey(idx_value_key,cur_time);
            let del_value_list = yield dbcacheCo.zrangebyscore([idx_value_key,"("+cur_time,'+inf']);

            for (let k = 0; k < del_value_list.length; k++) {
                let key_cond = del_value_list[k];
                let key_cond_list = key_cond.split("#$&*^%dj53#");
                if (key_cond_list.length <= 1) continue;
                let real_key = key_cond_list[0];
                let cond = key_cond_list[1];
                if (F.isNull(cond)) continue;
                if (F.isNull(update_key) || this.updateKeyMatchCond(cond,update_key)) {
                    if (F.isNull(connection)) {
                        dbcacheCli.zrem(idx_value_key,key_cond);
						//dbcacheCli.expire(real_key, 600);// 因为hmset不会修改timeout，可能执行完下一句hmset立马timeout，所以得提前设置一下
                        dbcacheCli.hmset(real_key,"del_time",del_time, "value", "");
						//dbcacheCli.expire(real_key, 600);
                        //TODO//F.addDebugLogs(["dbcache del:",real_key]);
                    } else {
                        let delay_rem_arr = [];
                        if (!F.isNull(connection.delay_rem_arr)) delay_rem_arr = connection.delay_rem_arr;
                        delay_rem_arr.push(idx_value_key+"#&*%24dijk#"+key_cond);
                        connection.delay_rem_arr = delay_rem_arr;
                        let delay_del_arr = [];
                        if (!F.isNull(connection.delay_del_arr)) delay_del_arr = connection.delay_del_arr;
                        delay_del_arr.push(real_key);
                        connection.delay_del_arr = delay_del_arr;
                    }
                }
            }

        }
    };

    this.save = function* (dbConnection, option) {
        let res = yield this.update(dbConnection, option);
        return F.isNull(dbConnection) ? res.affectedRows : res[0].affectedRows;
    };

	/*
	 * 更新
	 * options object 查询操作,属性包括：
	 ** where string 不能为空
	 ** values array (such as:["test",23]) 默认空数组
	 ** update_values object (such as:{"name":"test","age":23}) 默认空
	 ** update_string string 自定义操作 (such as:"login_times = login_times + 1") 默认空
	 */
    this.update = function* (connection, options) {
        var update_data = new Array();
        var update_key = new Array();
        var update_sql = `UPDATE ${this.table} SET `;
        try {
            if (F.isNull(this.uni_index)) {
                this.uni_index = yield this.get_uni_index(connection);
            }
            if(typeof options != 'object') F.throwErr('mysql update sql error:options is not object.');
            if(F.isNull(options.where)) F.throwErr('mysql update sql error:where can not null.');
            if(F.isNull(options.values)) options.values = new Array();

            var has_update_values = F.isNull(options.update_values) ? false : true;
            if(has_update_values == true) {
                for(var field in options.update_values) {
                    update_data.push(options.update_values[field]);
                    update_sql += field + "=?,";
                    update_key.push(this.table+"."+field);
                }
                update_sql = _.str.trim(update_sql, ',');
            }

            if(!F.isNull(options.update_string)) {
                if(has_update_values == true) {
                    update_sql += ',' + options.update_string;
                }else{
                    update_sql += options.update_string;
                }
            }
            //update_sql += ` WHERE  (${this.uni_index}) in (select ${this.uni_index} from ( select ${this.uni_index} from ${this.table} where ${options.where}) as unidtmp)`;
            var before_values = yield this.splitUpdateStr(options,update_key);
            var to_de_after_redkey = yield this.getUpdateAfterDelRedisKeys(options, before_values);
            if (!F.isNull(before_values)) update_data.push.apply(update_data, before_values);
            var to_de_redkey = [];
            update_sql += yield this.getWhereSqlByUniIndex(connection,options,to_de_redkey,update_data);
            //update_data.push.apply(update_data, options.values);

            var exec_conn = F.isNull(connection) ? that.mysql : connection;

            let start_time = new Date().getTime();
            var res = yield exec_conn.query(update_sql, update_data);
            if(!F.isNull(connection)) {
                let end_time = new Date().getTime();
                F.addDebugLogs([`con key:${connection.name}`,`usetime:${end_time-start_time}`,update_sql,update_data]);
                if (end_time - start_time > C.slow_log_delta) F.addSlowLogs([`con key:${connection.name}`,`usetime:${end_time-start_time}`,update_sql,update_data]);
            }
            var affect_msg = '';
            if (!F.isNull(connection)) affect_msg = res[0].message;
            else affect_msg = res.message;
            //F.addDebugLogs(["dbcache bug del sql:",update_sql,update_data]);
            if (C.is_open_sql_redis == true && affect_msg.indexOf('Changed: 0 Warnings') == -1) {
                yield this.delDbcache(to_de_redkey,connection,update_key);
                yield this.delDbcache(to_de_after_redkey,connection);
            }
            // if (!F.isNull(connection))  {
            //     res[0].affectedRows = res[0].changedRows;
            // } else {
            //     res.affectedRows = res.changedRows;
            // }
            return res; // 最后返回 防止执行sql报错而删除redis
        } catch (e) {
            var use_conn = !F.isNull(connection)?'true':'false';
            F.addErrLogs(["update err:",use_conn,update_sql,update_data,e.stack]);
            throw e;
        }


        // if(F.isNull(connection) && C.is_open_sql_redis == true) {
        // 	// 获取更新缓存keys
        // 	var del_keys = yield this.getDeleteRedisKey(options.where, options.values);
        // 	if(!F.isNull(del_keys)) {
        // 		// 删除keys
        // 		yield redis.redisCo.del(del_keys);
        // 	}
        // }
    }

    this.safeUpdate = function* (connection, options) {
        var res = yield this.update(connection, options);
        if (!F.isNull(connection)) res = res[0];
        return res;
    }

	/*
	 * 删除
	 * options object 查询操作,属性包括：
	 ** where string
	 ** values array (such as:["test",23]) 默认空数组
	 ** limit string 默认空
	 */
    this.delete = function* (connection, options) {
        if (F.isNull(this.uni_index)) {
            this.uni_index = yield this.get_uni_index(connection);
        }
        if(typeof options != 'object') F.throwErr('mysql delete sql error:options is not object.');
        if(F.isNull(options.where)) F.throwErr('mysql delete sql error:where can not null.');
        if(F.isNull(options.values)) options.values = new Array();

        //var sql = `DELETE FROM ${this.table} WHERE (${this.uni_index}) in (select ${this.uni_index} from (select ${this.uni_index} from ${this.table} where ${options.where}) as unidtmp)`;
        var sql = `DELETE FROM ${this.table} `;
        var to_de_redkey = [];
        sql += yield this.getWhereSqlByUniIndex(connection,options,to_de_redkey);
        // if(!F.isNull(options.limit)) {
        // 	sql += " limit " + options.limit;
        // }

        var exec_conn = F.isNull(connection) ? that.mysql : connection;
        //return yield exec_conn.query(sql, options.values);
        let start_time = new Date().getTime();
        var res = yield exec_conn.query(sql);
        if(!F.isNull(connection)) {
            let end_time = new Date().getTime();
            F.addDebugLogs([`con key:${connection.name}`,`usetime:${end_time-start_time}`,sql]);
            if (end_time-start_time > C.slow_log_delta) F.addSlowLogs([`con key:${connection.name}`,`usetime:${end_time-start_time}`,sql]);
        }
        var affect_row = 0;
        if (!F.isNull(connection)) affect_row = res[0].affectedRows;
        else affect_row = res.affectedRows;
        if (C.is_open_sql_redis == true && affect_row > 0) {
            yield this.delDbcache(to_de_redkey,connection);
        }
        return res;
    }

    this.execute_raw = function* (sql,parameter) {
        return yield that.mysql.query(sql,parameter);
    };


    // 事务相关
    this.getConnection = function* (name) {
        return yield that.mysql.getConnection(name);
    };

    this.startTransaction = function* (key,name='') {
        if (!F.isNull(key)) name = key;
        //F.addLogs(['enter start TRANSACTION ',name]);
        //F.addErrLogs(["connection flag get ",name]);
        var conn = null;

        if (that.mysql.conf.waitForConnections) {
            conn = yield this.getConnection(name);
        } else {
            var trycount = 0;
            while (++trycount < 100) {
                try {
                    conn = yield mysqllib.getConnection(name);
                } catch(e) {
                    //F.addErrLogs(['try get conn fail ', name]);
                    yield F.sleep(10);
                }
            }
            if (F.isNull(conn)) return conn;
        }

        let tr_id = yield that.getNextTransactionId();
        try {
            //F.addLogs(['success get TRANSACTION']);
            conn.start_time = new Date().getTime();
            yield conn.query("START TRANSACTION");
            //F.addLogs(['success START TRANSACTION. key:', key]);
            if (!F.isNull(key)) { // 加排它锁
                F.addLogs(["locks: start ","key:",key]);
                //yield conn.query(`insert into lockcontrol (lockkey) values ('${key}') on DUPLICATE key UPDATE lockkey = lockkey`);
                yield conn.query(`insert into lockcontrol (lockkey) values ('${key}')`);
                F.addLogs(["locks: get lock suc ","key:",key]);
                conn.key = key;
                conn.name = key;
            }
            if (!F.isNull(name)) conn.name = name;
            conn.tr_id = tr_id;
            F.addLogs(['TRANSACTION log start', {tr_id:tr_id, start_time:conn.start_time, name:name, key:key}]);
            return conn;
        } catch(e) {
            if (!F.isNull(conn))  {
                yield conn.query(`DELETE from lockcontrol WHERE lockkey = '${key}';`);
                yield this.rollback(conn);
            }
            F.addErrLogs(["get connection err:",e.stack]);
            F.addLogs(['TRANSACTION log start_err', {tr_id:tr_id, name:name, key:key}]);
            throw e;
        }
    };

    this.addSlowLog = function* (conn,status) {
        var end_time = new Date().getTime();
        if (end_time-conn.start_time > C.slow_log_delta) F.addSlowLogs([`usetime:${end_time-conn.start_time}`,"conn name:",conn.name,"status:",status]);
        delete conn.start_time;
        delete conn.name;
    };

    this.commit = function* (conn) {
        if (F.isNull(conn)) return;
        if (conn.has_release == true) {
            F.addErrLogs(["conn commit again:",conn.name]);
            return;
        }
        var name = conn.name;
        var tr_id = conn.tr_id;
        try {
            if (!F.isNull(conn.key)) {
                //F.addLogs(["locks: commit ","key:",conn.key]);
                yield conn.query(`DELETE from lockcontrol WHERE lockkey = '${conn.key}';`);
                delete conn.key;
            }
            yield conn.query("commit");
            if (!F.isNull(conn.delay_rem_arr)) {
                for (let i = 0; i < conn.delay_rem_arr.length; i++) {
                    let del_key = conn.delay_rem_arr[i];
                    let key_list = del_key.split("#&*%24dijk#");
                    let idx_value_key = key_list[0];
                    let key_cond = key_list[1];
                    dbcacheCli.zrem(idx_value_key,key_cond);
                    //TODO//F.addDebugLogs(["del dbcache:",idx_value_key,key_cond]);
                }
            }
            if (!F.isNull(conn.delay_del_arr)) {
				let del_time = yield F.getNextNoRoundId(dbcacheCo,"dbcache"); //删除redis 时间 必须在sql执行之后
                for (let i = 0; i < conn.delay_del_arr.length; i++) {
                    let del_key = conn.delay_del_arr[i];
					//dbcacheCli.expire(del_key, 600); // 因为hmset不会修改timeout，可能执行完下一句hmset立马timeout，所以得提前设置一下
                    dbcacheCli.hmset(del_key,"del_time",del_time, "value", "");
					//dbcacheCli.expire(del_key, 600);
                    //TODO//F.addDebugLogs(["del dbcache:",del_key]);
                }
            }
            yield this.addSlowLog(conn,"commit");
            conn.release(name);
            F.addLogs(['TRANSACTION log commit', {tr_id:tr_id}]);
            //F.addLogs(['success end TRANSACTION commit']);
        } catch (e) {
            F.addErrLogs(["commit err:",e.stack]);
            F.addLogs(['TRANSACTION log commit_err', {tr_id:tr_id}]);
            conn.release(name);
            //F.addLogs(['success end TRANSACTION commit']);
            throw  e;
        }
    };

    this.rollback = function* (conn) {
        if (F.isNull(conn)) return;
        if (conn.has_release == true) {
            F.addErrLogs(["conn rollback again:",conn.name]);
            return;
        }
        var name = conn.name;
        var tr_id = conn.tr_id;
        try {
            if (!F.isNull(conn.key)) {
                F.addLogs(["locks: rollback ","key:",conn.key]);
                delete conn.key;
            }
            yield conn.query("rollback");
            yield this.addSlowLog(conn,"rollback");
            conn.release(name);
            F.addLogs(['TRANSACTION log rollback', {tr_id:tr_id}]);
            //F.addLogs(['success end TRANSACTION rollback']);
        } catch (e) {
            F.addErrLogs(["rollback err:",e.stack]);
            F.addLogs(['TRANSACTION log rollback_err', {tr_id:tr_id}]);
            conn.release(name);
            //F.addLogs(['success end TRANSACTION rollback']);
            throw e;
        }
    };

    // key: 锁得唯一名 fn昰要加锁的部分 必须是异步函数
    this.lock = function* (key,fn) {
        let dbConnection = yield this.startTransaction(key); //开启事物处理
        try {
            yield fn();
            yield this.commit(dbConnection);
        } catch (e) {
            F.addErrLogs(["error: happen in lock:",e]);
            yield this.rollback(dbConnection);
        }
    }






	/* 格式化fields 
	 * fields string
	 */
    this.formatFieldsToRedis = function (fields) {
        // 去掉多余的空格
        fields = fields.replace(new RegExp(' ','g'), '');
        var fields_array = fields.split(',');
        fields_array.sort();
        return fields_array.join(',');
    }

	/* 格式化where 
	 * where string
	 * values array
	 */
    this.formatWhereToRedis = function (where, values) {
        var where_array = this.getPregWhereArray(where, false);
        if(where_array.length != values.length) F.throwErr('sql error.where must use bind params.');

        for (var i = 0; i < where_array.length; i++) {
            where_array[i] += '=' + encodeURIComponent(values[i]);
        }
        where_array.sort();
        return where_array.join('&');
    }

    this.getPregWhereArray = function (where, sort = true) {
        // 根据' and '分解成数组
        var where_array = where.split(' and ');
        var fields_array = new Array();
        for (var i = 0; i < where_array.length; i++) {
            // 根据'='分解
            var field = where_array[i].substr(0, where_array[i].indexOf('='));
            field = _.str.trim(field, ' ');
            fields_array.push(field);
        }
        return sort == true ? fields_array.sort() : fields_array;
    }

	/* 获取mysql query redis key */
    this.getQueryRedisKey = function (fields, where, options) {
        var key = _.str.sprintf(C.redisPre.sql_redis_prefix, this.table);
        key += `#fields:${fields}#where:${where}#`;
        if(!F.isNull(options.limit)) key += `limit:${options.limit}#`;
        if(!F.isNull(options.order)) key += `limit:${options.order}#`;
        if(!F.isNull(options.group)) key += `limit:${options.group}#`;
        if(!F.isNull(options.having)) key += `limit:${options.having}#`;
        return key;
    }

	/* 设置where查询组合 */
    this.setWhereGroup = function* (where) {
        var where_array = this.getPregWhereArray(where);console.log(where_array);
        var where = where_array.join(',');
        var key = _.str.sprintf(C.sql_table_redis_prefix, this.table);
        yield redis.redisCo.sadd(key, where);
    }

	/* 获取需要更新的redis key */
    this.getDeleteRedisKey = function* (where, values) {
        var key = _.str.sprintf(C.sql_table_redis_prefix, this.table);
        var redis_key_group = yield redis.redisCo.smembers(key);
        if(F.isNull(redis_key_group)) return new Array();

        var sql = `select distinct %s from ${this.table} where ${where}`;
        var preg_where_array = new Array();
        for (var i = 0; i < redis_key_group.length; i++) {
            sql = _.str.sprintf(sql, redis_key_group[i]);
            var sql_res = yield that.mysql.query(sql, values);console.log(sql_res);
            if(!F.isNull(sql_res)) preg_where_array.push.apply(preg_where_array, sql_res);
        }
        console.log(preg_where_array);
        if(F.isNull(preg_where_array)) return new Array();

        var search_keys = new Array();
        for (var i = 0; i < preg_where_array.length; i++) {
            var search_key = '*#where:';
            for(var j in preg_where_array[i]) {
                search_key += j + '=' + encodeURIComponent(preg_where_array[i][j]) + '&';
            }console.log(search_key);
            var search_res = yield redis.redisCo.keys(search_key + '*');
            if(!F.isNull(search_res)) search_keys.push.apply(search_keys, search_res);
            console.log(search_key);
            console.log(search_res);
        }
        return search_keys;
    }

	/* 设置缓存检测 */
    this.checkWhereToRedis = function (options) {
        if(F.isNull(options)) F.throwErr('check set mysql redis error.options is null.');
        if(F.isNull(options.values)) F.throwErr('check set mysql redis error.options.values is null.');
        if(F.isNull(options.where)) F.throwErr('check set mysql redis error.options.where is null.');
        if(options.where.indexOf('(') >= 0) F.throwErr('check set mysql redis error.options.where can not has "()、<>、or"');
        if(options.where.indexOf('<') >= 0) F.throwErr('check set mysql redis error.options.where can not has "()、<>、or"');
        if(options.where.indexOf('>') >= 0) F.throwErr('check set mysql redis error.options.where can not has "()、<>、or"');
        if(options.where.indexOf(' or ') >= 0) F.throwErr('check set mysql redis error.options.where can not has "()、<>、or"');
    }

}

module.exports = model_base;
