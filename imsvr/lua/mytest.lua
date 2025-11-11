local threadList={}
threadList.items={{['tid']=1,['post']='post1'},{['tid']=2,['post']='post2'} }
table.insert(threadList.items,{['tid']=3,['post']=3})
threadList.loseds={['120']=2,['121']=7,['122']=9}
return cjson.encode(threadList)