local result={}
result.items={}
result.loseItems={}
local k1=KEYS[1] --未格式化key user:%d:info
local k2=KEYS[2] --过期时间
local lose={}
for i = 1, #ARGV do
  local k=string.format(k1,ARGV[i])
  local v1=redis.call('expire',k,k2)
  if v1==0 then
      table.insert(result.items,{})
      lose[ARGV[i]]=i-1;
  else
      local info=hgetall(k)
      table.insert(result.items,info)
--      local info=redis.call('hmget',k,'uid','nickname','bodytype','face','threads','posts','friends','fans','tfavtimes')
--      table.insert(result.items,{['uid']=info[1],['nickname']=info[2],['bodytype']=info[3],['face']=info[4],['threads']=info[5],['posts']=info[6],['friends']=info[7],['fans']=info[8],['tfavtimes']=info[9]})
  end
end
result.loseItems=lose
return cjson.encode(result)