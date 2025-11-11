local result={}
--result.items={}
result.loseItems={}
local k1=KEYS[1] --未格式化key user:%d:info
local k2=KEYS[2] --过期时间
local lose={}
for i = 1, #ARGV do
  local k=string.format(k1,ARGV[i])
  local v1=redis.call('expire',k,k2)
  if v1==0 then
      table.insert(lose,ARGV[i])
  else

  end
end
result.loseItems=lose
return cjson.encode(result)