#!/usr/bin/python
#-*- coding:utf-8 -*-

import os
import sys
import re
import json
import traceback

#reload(sys)
#sys.setdefaultencoding('utf-8') 

def getTraceStackMsg():
    tb = sys.exc_info()[2]
    msg = ''
    for i in traceback.format_tb(tb):
        msg += i
    return msg


sql_dic = {}

        
filepath = sys.argv[1]
file = open(filepath)
while 1:
    line = file.readline()
    if not line:
        break

    start = line.find('["SELECT')
    if start < 0: 
        start = line.find('["UPDATE')
    if start < 0: 
        start = line.find('["DELETE')
    if start < 0: 
        start = line.find('["INSERT')
    if start < 0:
        start = line.find('["select')
    if start < 0: 
        start = line.find('["update')
    if start < 0: 
        start = line.find('["delete')
    if start < 0: 
        start = line.find('["insert')
    if start < 0:
        continue;
    sql = line[start:]
    end1 = sql.find(",[")
    end2 = sql.find("where")
    end3 = sql.find("WHERE")
    end4 = sql.find("VALUES")
    end5 = sql.find("values")
    end1 = end1 if end1 >= 0 else 9999999
    end2 = end2 if end2 >= 0 else 9999999
    end3 = end3 if end3 >= 0 else 9999999
    end4 = end4 if end4 >= 0 else 9999999
    end5 = end5 if end5 >= 0 else 9999999
    end = min(end1,end2,end3,end4,end5)
    sql = sql[0:end]
    if sql in sql_dic:
        sql_dic[sql]+=1
    else:
        sql_dic[sql]=1


fpa=open("sql_sort.txt","w")
res_dic=sorted(sql_dic.items(), lambda x, y: cmp(x[1], y[1]), reverse=True)
for k,v in res_dic:
    print >> fpa, k, v
fpa.close()


















