'use strict';

const C = require('../config');
const F = require('../common/function');
const path = require('path');
const _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');
var co = require('co');

module.exports = function (app, commonManager) {

    let modelMap = app.model_mgr.model_map;
    let managerMap = commonManager.mgr_map;
    let model_map = app.model_mgr.model_map;
    let mgr_map = commonManager.mgr_map;

    let that = this;

    // redis key 前缀 - hash
    this.key_prefix_hash = '{rk}_hash_%s_%s'; // table increment

    // redis key 前缀 - set
    this.key_prefix_set = '{rk}_set_%s_%s_%s'; // table field value

    // redis hash自增key
    this.key_prefix_has_inc = '{rk}_hash_inc';

    // redis 临时区间结果集自增key
    this.key_prefix_tmp_set_inc = '{rk}_tmp_set_inc';

    // redis sort set key 前缀
    this.key_prefix_sort_set = '{rk}_zset_%s_%s'; // table field

    // 数据类型 s:字符串 n:运算数
    this.type = {s:'string', n:'number'};

    this.getKeyHash = function (table, increment) {
        return _.str.vsprintf(this.key_prefix_hash, [table, increment]);
    }

    this.getKeySet = function (table, field, value) {
        return _.str.vsprintf(this.key_prefix_set, [table, field, value]);
    }

    this.getKeySortSet = function (table, field) {
        return _.str.vsprintf(this.key_prefix_sort_set, [table, field]);
    }

    /**
     * 获取最优过期时间
     * @param key
     * @param expire
     * @returns {int}
     */
    this.getBestExpire = function* (key, expire) {
        return null; // 关闭超时
        let key_expire = yield managerMap.redis.ttl(key);
        if(key_expire < expire) {
            key_expire = expire;
        }else if(expire == -1) {
            key_expire = expire;
        }
        return key_expire;
    }

    /**
     * 获取数组交集
     * @param arrays
     * @returns {array}
     */
    this.intersection = function (arrays) {
        if(arrays.length == 0) return new Array();
        if(arrays.length == 1) return arrays[0];

        // 获取长度最小数组的索引
        let min_index = -1;
        for (let i = 0; i < arrays.length; i++) {
            if(min_index == -1 || arrays[min_index].length > arrays[i].length) {
                min_index = i;
            }
        }
        let intersection_array = new Array();
        for (let i = 0; i < arrays[min_index].length; i++) {
            let is_in = true; // 是否交集 默认true
            for (let j = 0; j < arrays.length; j++) {
                if(j == min_index) continue;
                if(arrays[j].indexOf(arrays[min_index][i]) == -1) {
                    is_in = false;
                    break;
                }
            }
            if(is_in == true && intersection_array.indexOf(arrays[min_index][i]) == -1) {
                intersection_array.push(arrays[min_index][i]);
            }
        }
        return intersection_array;
    }

    /**
     * 判断是否为数字类型
     * @param str
     * @returns {bool}
     */
    this.checkIsNumber = function (str) {
        if(F.isNull(str)) return false;
        let number_map = '1234567890.'; // .为了支持小数
        let is_number = true;
        for (let i = 0; i < str.length; i++) {
            if(number_map.indexOf(str.charAt(i)) == -1) {
                is_number = false;
                break;
            }
        }
        return is_number;
    }


    this.incr = 0;

    /**
     * 查询结果集
     * @param table
     * @param option
     * option包括：
     * fields 字符串 例如："name,age" 不能空
     * where 字符串 例如"name = ? and age > ?" 不能空 只支持 = > < >= <=
     * values 数组 例如['fdl', 2]
     * @returns {}
     */
    this.queryResultFromRedisDB = function* (table, option, isForQuery=false) {
        let key_array = []; // 等值操作符key集合
        let range_key_array = []; // 范围key集合
        // 解析where
        let where_array = option.where.split(' and ');
        // 多个查询条件使用inter
        for (let i = 0; i < where_array.length; i++) {
            let split_str;
            if(where_array[i].indexOf('>=') != -1) {
                split_str = '>=';
            }else if(where_array[i].indexOf('<=') != -1) {
                split_str = '<=';
            }else if(where_array[i].indexOf('>') != -1){
                split_str = '>';
            }else if(where_array[i].indexOf('<') != -1) {
                split_str = '<';
            }else if(where_array[i].indexOf('=') != -1) {
                split_str = '=';
            }else {
                F.throwErr('where sql err.');
            }
            let param_array = where_array[i].split(split_str);
            let field = _.str.trim(param_array[0], ' ');
            let value = _.str.trim(param_array[1], [' ', '"', '\'']);
            if(value == '?') {
                value = option.values.shift();
            }
            let option_min;
            let option_max;
            switch(split_str) {
                case '=':
                    key_array.push(this.getKeySet(table, field, value));
                    break;
                case '>':
                    option_min = '(' + value;
                    option_max = '+inf';
                    break;
                case '<':
                    option_min = '-inf';
                    option_max = '(' + value;
                    break;
                case '>=':
                    option_min = value;
                    option_max = '+inf';
                    break;
                case '<=':
                    option_min = '-inf';
                    option_max = value;
                    break;
            }
            if(split_str != '=') {
                let sort_set_key = this.getKeySortSet(table, field);
		range_key_array.push({"key":sort_set_key,"min":option_min,"max":option_max});
            }
        }
	let sort_field = "";
	let isdesc = "";
	let limit_first = "";
	let limit_second = "";
	if (isForQuery) {
	    if (!F.isNull(option.order)) {
                let order_array = option.order.split(',');
	        if (order_array.length > 1) F.throwErr("redis db can not contain multi order");
	        order_array = option.order.split(/\s+/);
                sort_field = _.str.trim(order_array[0], ' ');
                isdesc = _.str.trim(order_array[1], ' ');
	    }
	    if (!F.isNull(option.limit)) {
	        let limit_array = option.limit.split(',');
		if (limit_array.length > 2) F.throwErr("redis db limit format is wrong");
                limit_first = _.str.trim(limit_array[0], ' ');
                limit_second = _.str.trim(limit_second[1], ' ');
	    }
	}
        let query_res = yield managerMap.redis.mysinter(table,key_array,range_key_array,sort_field,isdesc,limit_first,limit_second);
        return query_res;
    }

    /**
     * 批量插入
     * * datas 数据对象数组
     */
    this.batchInsert = function* (table, option) {
        if (F.isNull(option) || F.isNull(option.datas)) F.throwErr('params err.');
	let res = [];
        for (let i = 0; i < option.datas.length; i++) {
            option.data = option.datas[i];
            let id = yield that.insertRedisDB(table, option);
	    res.push(id);
        }
	return res;
    }

    /**
     * 插入
     * @param table
     * @param option
     * option包括：
     * data 数据对象
     * expire 过期时间 -1为永不过期
     * index 数据描述对象 - 设置该值表示添加索引
     *     field:type type取值： n:number, s:string
     *     例如 {name:s, age:n}
     * @returns {}
     */
    this.insertRedisDB = function* (table, option) {
        if(F.isNull(table) || F.isNull(option) || F.isNull(option.data) || F.isNull(option.expire) || F.isNull(option.index)) {
            F.throwErr('params err.');
        }

        // 判断option.index值是否正确
	delete option.index.auto_id; // 先删除之前的auto_id index
        let type_arr = F.values(that.type);
        for (let field in option.index) {
            if(type_arr.indexOf(option.index[field]) == -1) F.throwErr('index err.');
            if(option.index[field] == that.type.n) {
                if(option.data.hasOwnProperty(field) == false) F.throwErr(field+' index no contain.');
                if(typeof(option.data[field]) != 'number') F.throwErr(field+' index type err.');
            }
        }

        // 添加 hash映射 set映射
	let key_hash_inc = 0;
	if (!F.isNull(option.data.auto_id)) key_hash_inc = option.data.auto_id;
        else key_hash_inc = yield managerMap.redis.hincrby(this.key_prefix_has_inc, table);
        let key_hash = this.getKeyHash(table, key_hash_inc);
	option.data.auto_id = key_hash_inc;
	option.index.auto_id = this.type.n;
        let fields_data = new Array();
        for(let field in option.data) {
            fields_data.push(field);
            fields_data.push(option.data[field]);

            // 判断是否添加索引
            if(option.index.hasOwnProperty(field) == false) continue;

            // 添加 set映射
            let key_set = this.getKeySet(table, field, option.data[field]);
            let expire_set = yield this.getBestExpire(key_set, option.expire);
            co(managerMap.redis.sadd(key_set, key_hash_inc, expire_set));

            // 判断是否number类型 添加number类型数据 sort set映射
            if(option.index[field] == that.type.n) {
                let ket_sort_set = this.getKeySortSet(table, field);
                let expire_sort_set = yield this.getBestExpire(ket_sort_set, option.expire);
                co(managerMap.redis.zadd(ket_sort_set, key_hash_inc, option.data[field], expire_sort_set));
            }
        }
        let expire_hash = yield this.getBestExpire(key_hash, option.expire);
        yield managerMap.redis.hmset(key_hash, fields_data, expire_hash);
        return {'auto_id':key_hash_inc};
    };

    /**
     * 查询
     * @param table
     * @param option
     * option包括：
     * fields 字符串 例如："name,age" 不能空
     * where 字符串 例如"name = ? and age > ?" 不能空 只支持 = > < >= <=
     * values 数组 例如['fdl', 2]
     * order 字符串 为空默认asc 排序字段必须在fields出现
     * limit exsamp:1,2 当获取一条数据时赋值1，为1时返回对象
     * @returns {*}
     */
    this.queryRedisDB = function* (table, option) {

        if (F.isNull(table)) F.throwErr('params err: table is null');
        if (F.isNull(option)) F.throwErr('params err: option is null');
        if (F.isNull(option.fields)) F.throwErr('params err: option.fields is null');
        if (F.isNull(option.where)) F.throwErr('params err: option.where is null');

        let query_res = yield this.queryResultFromRedisDB(table, option, true);
        if(F.isNull(query_res)) {
	    return [];
        }
        let fields = option.fields.split(',');
        for (let i = 0; i < fields.length; i++) {
            fields[i] = _.str.trim(fields[i], ' ');
        }
        let list = new Array();
        let redisCo = managerMap.redis.getRedisCo();
        let batchExe = redisCo.multi();
        for (let i = 0; i < query_res.length; i++) {
            let key_hash = this.getKeyHash(table, query_res[i]);
            let param = [key_hash];
            param.push.apply(param, fields);
            batchExe = batchExe.hmget(param);
        }
        let res_list = yield batchExe.exec();
        for (let i = 0; i < res_list.length; i++) {
            let data = res_list[i];
            let item = {};
            let isAllAtrNull = true;
            for (let j = 0; j < fields.length; j++) {
                item[fields[j]] = data[j];
                if (data[j] != null) isAllAtrNull = false;
            }
            if (!isAllAtrNull) list.push(item);
        }
	return list;
    };

    /**
     * 更新
     * @param table
     * @param option
     * option包括：
     * where 字符串 例如"name = ? and age > ?" 不能空 只支持 = > < >= <=
     * values 数组 例如['fdl', 2]
     * data 数据对象
     * expire 过期时间 -1为永不过期
     * index 数据描述对象 - 设置该值表示添加索引
     *     field:type type取值： n:number, s:string
     *     例如 {name:s, age:n}
     * @returns {int} 影响行数
     */
    this.updateRedisDB = function* (table, option) {
        if(F.isNull(table) || F.isNull(option) || F.isNull(option.where) || F.isNull(option.data) || F.isNull(option.expire) 
            || F.isNull(option.index)) {
            F.throwErr('params err.');
        }

        let query_res = yield this.queryResultFromRedisDB(table, option);
        if(F.isNull(query_res)) return 0;
        let del_key_array = new Array();
        let del_key_record = new Object();
        let update_data = new Array();
        for (let i = 0; i < query_res.length; i++) {
            let key_hash = this.getKeyHash(table, query_res[i]);
            let fields_keys = yield managerMap.redis.hkeys(key_hash);
            if(F.isNull(fields_keys)) continue;
            let fields_data = yield managerMap.redis.hmget(key_hash, fields_keys);
            if(F.isNull(fields_data)) continue;
            // 查询旧数据字段键值映射 添加到删除数据
            let insert_item = new Object();
            del_key_array.push(key_hash);
            for (let j = 0; j < fields_keys.length; j++) {
                if(option.data.hasOwnProperty(fields_keys[j])) { // 只删除更新字段的映射key
                    // 查询删除key
                    let key_set = this.getKeySet(table, fields_keys[j], fields_data[j]);
                    if(F.isNull(del_key_record[key_set])) {
                        del_key_array.push(key_set);
                        del_key_record[key_set] = 1;
                    }
                    if(typeof(option.data[fields_keys[j]]) == 'number') {
                        // 删除区间查询映射key
                        let key_sort_set = this.getKeySortSet(table, fields_keys[j]);
                        co(managerMap.redis.zrem(this.getKeySortSet(table, fields_keys[j]), query_res[i]));
                    }
                    insert_item[fields_keys[j]] = option.data[fields_keys[j]];
                }else {
                    insert_item[fields_keys[j]] = fields_data[j];
                }
            }
            // 检测更新字段中是否存在新增字段
            for (let field in option.data) {
                if(insert_item.hasOwnProperty(field) == false) {
                    insert_item[field] = option.data[field];
                }
            }
            update_data.push(insert_item);
        }
        yield managerMap.redis.del(del_key_array);
        
        // 添加更新的数据
        for (let i = 0; i < update_data.length; i++) {
            yield this.insertRedisDB(table, {
                'data': update_data[i],
                'expire': option.expire,
                'index':option.index
            });
        }

        return query_res.length;
    }

    /**
     * 删除
     * @param table
     * @param option
     * option包括：
     * where 字符串 例如"name = ? and age > ?" 不能空 只支持 = > < >= <=
     * @returns {int} 影响行数
     */
    this.deleteRedisDB = function* (table, option) {
        if(F.isNull(table) || F.isNull(option) || F.isNull(option.where)) F.throwErr('params err.');

        let query_res = yield this.queryResultFromRedisDB(table, option);
        if(F.isNull(query_res)) return 0;
	//F.addDebugLogs(["to del:",query_res]);
        let del_key_array = new Array();
        let del_key_record = new Object();
        for (let i = 0; i < query_res.length; i++) {
            let key_hash = this.getKeyHash(table, query_res[i]);
            let fields_keys = yield managerMap.redis.hkeys(key_hash);
            if(F.isNull(fields_keys)) continue;
            let fields_data = yield managerMap.redis.hmget(key_hash, fields_keys);
            if(F.isNull(fields_data)) continue;
            // 查询数据字段键值映射 添加到删除数据
            del_key_array.push(key_hash);
	    //F.addDebugLogs(["to del fields_data:",fields_data]);
            for (let j = 0; j < fields_keys.length; j++) {
                // 查询删除key
                let key_set = this.getKeySet(table, fields_keys[j], fields_data[j]);
	        //F.addDebugLogs(["to del key_set:",key_set]);
                if(F.isNull(del_key_record[key_set])) {
                    del_key_array.push(key_set);
                    del_key_record[key_set] = 1;
                }
                if(this.checkIsNumber(fields_data[j]) == true) {
                    // 删除区间查询映射key
                    let key_sort_set = this.getKeySortSet(table, fields_keys[j]);
	            //F.addDebugLogs(["to del key_sort_set:",key_sort_set,query_res[i],del_key_record[key_sort_set]]);
                    co(managerMap.redis.zrem(this.getKeySortSet(table, fields_keys[j]), query_res[i]));
                }
            }
        }
        yield managerMap.redis.del(del_key_array);

        return query_res.length;
    }

};
