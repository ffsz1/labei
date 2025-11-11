local result={}
result.items={}
local k1=KEYS[1] --未格式化key lists:user:%d
local k2=KEYS[2] --过期时间
local keyArr={}
local outPut='output'
table.insert(keyArr,outPut)
table.insert(keyArr,#ARGV)
for i = 1, #ARGV do
  local k=string.format(k1,ARGV[i])
  local v1=redis.call('expire',k,k2)
  if v1==0 then

  else
      table.insert(keyArr,k)
  end
end
redis.call('DEL',outPut)
table.insert(keyArr,'WEIGHTS')
for i = 1, #ARGV do
    table.insert(keyArr,1)
end
redis.call('ZUNIONSTORE',keyArr)
--redis.call('ZUNIONSTORE','output',7,'lists:user:54','lists:user:427','lists:user:554','lists:user:563','lists:user:36','lists:user:795','lists:user:799','WEIGHTS',1,1,1,1,1,1,1)
result.items=redis.call('ZREVRANGE',outPut,0,-1,'WITHSCORES')
return cjson.encode(result)