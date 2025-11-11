/**
 * example:
 var redis=require('../libs/redis.js');
 var uid=yield redis.redisCo.get('siss:user:token_'+tokenKey);
 redis.redisClient.expire('siss:user:token_'+tokenKey,C.session.ttl);
 */

'use strict';

var fs = require('fs');
var path = require('path');
var redis = require('redis');
var C = require('../config');
var ready = require('ready');
var F = require('../common/function');

//require('redis-lua2').attachLua(redis);
//runOnce(redis);
var redisClient;
var dbcacheCli; // 独立mysqlcache用
var javaRedisClient; // 连接java服务器使用的 redis接口

if (C.redis.unix_socket){
  redisClient = redis.createClient(C.redis.unix_socket,C.redis.options);
  dbcacheCli = redis.createClient(C.redis.unix_socket,C.redis.options);
  javaRedisClient = redis.createClient(C.javaredis.unix_socket,C.javaredis.options);
}else{
  redisClient = redis.createClient(C.redis.port,C.redis.host,C.redis.options);
  dbcacheCli = redis.createClient(C.redis.port,C.redis.host,C.redis.options);
  javaRedisClient = redis.createClient(C.javaredis.port,C.javaredis.host,C.javaredis.options);
}
redisClient.select(C.redis.db,function(){
    console.log('redis select db is '+C.redis.db);
});

dbcacheCli.select(C.redis.db_cache,function(){
    console.log('dbcache redis select db is '+ parseInt(C.redis.db_cache));
});

javaRedisClient.select(C.javaredis.db,function(){
    console.log('java redis select db is '+ parseInt(C.javaredis.db));
});

var wrapper = require('co-redis');
var redisCo = wrapper(redisClient);
var dbcacheCo = wrapper(dbcacheCli);// 独立mysqlcache用
var javaRedisCo = wrapper(javaRedisClient);//连接java服务器使用的 redis



exports.redisCo=redisCo;
exports.redisClient=redisClient;

exports.dbcacheCo=dbcacheCo;// 独立mysqlcache用
exports.dbcacheCli=dbcacheCli;// 独立mysqlcache用

exports.javaRedisCo=javaRedisCo;// 连接java服务器使用的 redis  协程
exports.javaRedisClient=javaRedisClient;// 连接java服务器使用的 redis 异步


var hook_fun = function*(cmd,cmd_name,args){
    let start_time = new Date().getTime();
    let res = yield cmd.apply(redisCo,args)
    let end_time = new Date().getTime();
    //F.addDebugLogs(["##redis",cmd_name,args,(end_time-start_time)]);
    if (end_time - start_time > 30) F.addOtherLogs("slow/slow",["##redis",cmd_name,args,"utime",(end_time-start_time)]);
    return res;
};

var reg_hook = function(cmd_name) {
    let s = `redisCo.imraw${cmd_name} = redisCo.${cmd_name};redisCo.${cmd_name}=function*(){return yield hook_fun(redisCo.imraw${cmd_name},cmd_name,arguments);}`;
    eval(s);
};

reg_hook("hget");
reg_hook("INCR");
reg_hook("HINCRBY");
reg_hook("expire");
reg_hook("hmset");
reg_hook("hmget");
reg_hook("hkeys");
reg_hook("hgetall");
reg_hook("hdel");
reg_hook("set");
reg_hook("get");
reg_hook("del");
reg_hook("ZADD");
reg_hook("zrangebyscore");
reg_hook("zcount");
reg_hook("zrem");
reg_hook("zscore");
reg_hook("zcard");
reg_hook("zrange");
reg_hook("ttl");
reg_hook("sadd");
reg_hook("smembers");
reg_hook("sdiff");
reg_hook("sinter");
reg_hook("exists");
reg_hook("ZRANK");
reg_hook("lpush");
reg_hook("lrem");
reg_hook("llen");
reg_hook("hset");
reg_hook("ZSCORE");
reg_hook("ZREVRANGE");
reg_hook("ZCOUNT");
reg_hook("zremrangebyscore");




/*************以下包装redis lua 扩展函数**************/
var tpl_wrap = function(fn) {
        var str = fn.toString();
        var tpl = str.substring(str.indexOf("*") + 1);
        return tpl.substring(0,tpl.length-3);
    };

exports.sha_map = {};


var mysinter_lua = tpl_wrap(function(){/*
local nousekey = KEYS[1] 
local tablename = ARGV[1]
local key     = ARGV[2] -- set key array json
local key2    = ARGV[3] -- sort set json array json {key:x,min:x,max:x}
local sort_field     = ARGV[4]
local isdesc     = ARGV[5]
local limit_first = ARGV[6]
local limit_second = ARGV[7]

local argv_null_count = 0

if sort_field == nil or sort_field == "" then
    sort_field = "auto_id"
    argv_null_count = argv_null_count + 1
end

if isdesc == nil or isdesc == "" then
    isdesc = "asc"
    argv_null_count = argv_null_count + 1
end

if limit_first == nil or limit_first == "" then
    limit_first = 0
    argv_null_count = argv_null_count + 1
end

if limit_second == nil or limit_second == "" then
    limit_second = 99999999
    argv_null_count = argv_null_count + 1
end

local final_res = {}
local has_set_res = false --是否已经设置第一个结果集
local set_keys = cjson.decode(key);
local set_keys_len = #set_keys
if set_keys_len > 0 then
    has_set_res = true
    local set_res = redis.call('sinter', unpack(set_keys));
    for i, val in pairs(set_res) do
        final_res[val] = 1
    end
end

local sset_keys = cjson.decode(key2)
for i, val in pairs(sset_keys) do
    local ssetKey = val["key"]
    local min = val["min"]
    local max = val["max"]
    local sset_res = redis.call('zrangebyscore', ssetKey, min, max)
    if has_set_res == false then --no set_keys
        has_set_res = true
        for i2, val2 in pairs(sset_res) do
	    final_res[val2] = 1
	end
    else
        local merge_res = {}
        for i2, val2 in pairs(sset_res) do
            if final_res[val2] then
                merge_res[val2] = 1
            end
        end
        final_res = merge_res
    end
end

local final_ids = {}
if argv_null_count == 4 then
    for i, val in pairs(final_res) do
        table.insert(final_ids,i)
    end
else
    local k = {}
    for i, val in pairs(final_res) do
        local score = redis.call('ZSCORE','{rk}_zset_'..tablename..'_'..sort_field,i);
	if score == nil then score = 0 end
	table.insert(k,{i,score})
    end
    if isdesc == "asc" then
        table.sort(k,function (x,y) return x[2]<y[2] end)
    else
        table.sort(k,function (x,y) return x[2]>y[2] end)
    end
    local c = 0
    for i, val in pairs(k) do
        if i >= limit_first+1 then
	    table.insert(final_ids,val[1])
	    c = c + 1
	end
	if c >= limit_second then break end
    end
end

return cjson.encode(final_ids) 
*/});
redisClient.script('load', mysinter_lua, function(err,sha){
    if (err) {
        console.log("load mysinter lua err:",err);
    } else {
        exports.sha_map.mysinter_sha = sha;
    }
    console.log("mysinter lua sha:",sha);
});



//zrangebyscorestore zrangebyscore并存到另外的set 返回set key
var zrangebyscorestore_lua = tpl_wrap(function(){/*
local key     = KEYS[1]
local key2    = KEYS[2]
local min     = ARGV[1]
local max     = ARGV[2]
local res = redis.call('zrangebyscore', KEYS[1], min, max);
for i, val in pairs(res) do
    redis.call('sadd', KEYS[2], val);
end
redis.call('expire', KEYS[2], 30);
return key2
*/});
redisClient.script('load', zrangebyscorestore_lua, function(err,sha){
    if (err) {
        console.log("load zrangebyscorestore lua err:",err);
    } else {
        exports.sha_map.zrangebyscorestore_sha = sha;
    }
    console.log("zrangebyscorestore lua sha:",sha);
});



// 封装setnx+expire函数
var setnxex_lua = tpl_wrap(function(){/*
local key     = KEYS[1]
local content = ARGV[1]
local ttl     = ARGV[2]
local lockSet = redis.call('setnx', KEYS[1], content)
if lockSet == 1 then
  redis.call('expire', KEYS[1], ttl)
end
return lockSet
*/});
redisClient.script('load', setnxex_lua, function(err,sha) {
    if (err) {
        console.log("load setnxex lua err:",err);
    } else {
        exports.sha_map.setnxex_sha = sha;
    }
    console.log("setnxex lua sha:",sha);
});

// 封装lock函数
var lock_lua = tpl_wrap(function(){/*
local key     = KEYS[1]
local pid     = KEYS[2]
local json_str = ARGV[1]
local ttl     = ARGV[2]
if key == nil or json_str == nil or ttl == nil then
    return 0
end
local ret,errmsg = pcall(cjson.decode,json_str)
if ret == false then
    return 0
end
local js = cjson.decode(json_str)
if js == nil or js["time"] == nil or js["pid"] == nil then
    return 0
end
local lockSet = redis.call('setnx', KEYS[1], json_str)
if lockSet == 1 then
  redis.call('expire', KEYS[1], ttl)
  redis.call('zadd', KEYS[2], js["time"], key)
end
return lockSet
*/});
redisClient.script('load', lock_lua, function(err,sha){
    if (err) {
        console.log("load lock lua err:",err);
    } else {
        exports.sha_map.lock_sha = sha;
    }
    console.log("lock lua sha:",sha);
});


// 封装unlock函数
var unlock_lua = tpl_wrap(function(){/*
local key     = KEYS[1] 
local pid = KEYS[2]
local json_str = ARGV[1]
if key == nil or json_str == nil then
    return 0 
end
local ret,errmsg = pcall(cjson.decode,json_str)
if ret == false then
    return 0
end
local js = cjson.decode(json_str)
if js == nil or js["time"] == nil or js["pid"] == nil then
    return 0
end
local redis_json_str = redis.call("get", KEYS[1])
if redis_json_str == nil then
    redis.call("zrem", KEYS[2], key)
    return 0
end
local ret,errmsg = pcall(cjson.decode,redis_json_str)
if ret == false then
    return 0
end
local redis_js = cjson.decode(redis_json_str)
if js["time"] == redis_js["time"] and js["pid"] == redis_js["pid"] then
    redis.call("zrem", KEYS[2], key)
    return redis.call("del", KEYS[1])
elseif redis_js["time"] == nil or redis_js["pid"] == nil then 
    redis.call("zrem", KEYS[2], key)
    return redis.call("del", KEYS[1])
elseif js["pid"] == redis_js["pid"] and js["svrRestartTime"] ~= nil and tonumber(redis_js["time"]) < tonumber(js["svrRestartTime"]) then
    -- same svr and set time before svr start, must del
    redis.call("zrem", KEYS[2], key)
    return redis.call("del", KEYS[1])
else
    return 0
end
*/});
var ready = false;
redisClient.script('load', unlock_lua, function(err,sha){
    if (err) {
        console.log("load unlock lua err:",err);
    } else {
        exports.sha_map.unlock_sha = sha;
    }
    console.log("unlock lua sha:",sha);
    ready = true;
});

var deasync = require('deasync');
while (true) {
    if (ready) break;
    deasync.sleep(200);
}





