--------------批量修改帖子发帖人昵称
local result={}
local k1=KEYS[1] --未格式化key thread:{tid}:info
local k2=KEYS[2] --过期时间
local k3=KEYS[3] --修改值
local k4=KEYS[4] --修改字段
for i = 1, #ARGV do
  local k=string.format(k1,ARGV[i])
  local v1=redis.call('expire',k,k2)
  if v1==0 then

  else
      redis.call('HMSET',k,k4,k3)
  end
end
result.res='successful'
return cjson.encode(result)