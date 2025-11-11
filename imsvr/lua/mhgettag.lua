local result={}
result.items={}
result.loseItems={}
local k1=KEYS[1]--未格式化key
local k2=KEYS[2]--type
local k3=KEYS[3]--过期时间
local lose={}
for i = 1, #ARGV do
  local k=string.format(k1,ARGV[i],k2)
  local v1=redis.call('expire',k,k3)
  if v1==0 then
      table.insert(result.items,{})
      lose[ARGV[i]]=i-1;
  else
      local info=hgetall(k)
      table.insert(result.items,info)
--      local info=redis.call('hmget',k,'tagid','tagtitle','tagcount','pincount','tagimg')
--      table.insert(result.items,{['tagid']=info[1],['tagtitle']=info[2],['tagcount']=info[3],['pincount']=info[4],['tagimg']=info[5]})
  end
end
result.loseItems=lose
return cjson.encode(result)