local result={}
result.items={}
result.loseItems={}
local k1=KEYS[1]--未格式化key
local k2=KEYS[2]--过期时间
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
--      local info=redis.call('hmget',k,'tid','pid','uid','nickname','face','timeline','replies','liketimes','imgext','post','closed','status','digest')
--      table.insert(result.items,{['tid']=info[1],['pid']=info[2],['uid']=info[3],['nickname']=info[4],['face']=info[5],['timeline']=info[6],['replies']=info[7],['liketimes']=info[8],['imgext']=info[9],['post']=info[10],['closed']=info[11],['status']=info[12],['digest']=info[13]})
  end
end
result.loseItems=lose
return cjson.encode(result)