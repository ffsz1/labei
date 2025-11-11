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

def getErrMsg():
    rootdir = '.'
    config_path = rootdir+'/config/index.js'
    content = open(config_path).read()
    start = content.find("err_msg:")
    end = content.find("}",start)
    err_content = content[start+9:end]
    errstr_list = err_content.split("\n")
    err_dic = {}
    for line in errstr_list:
        line = line.strip("\r\s ")
        if len(line) == 0 or not ":" in line or line.startswith("//"):
            continue
        line_list = line.split(":")
        k = line_list[0].strip('''"''')
        err_dic[k] = {"line":line,"used":0}
    return err_dic

def getFiles(module):
    rootdir = '.'
    route_dir = rootdir+'/'+module+'/'
    route_list = os.listdir(route_dir)
    route_file_list = []
    for route in route_list:
        if route.endswith('js'):
            route_file_list.append(route_dir+route)
    return route_file_list

def addCode(fun_dic,cur_m,m):
    fun_dic_val = fun_dic[cur_m]
    fun_list = {}
    if "code" in fun_dic_val:
        fun_list = fun_dic_val["code"]
    for sub in m:
        code = sub.strip("'\" ")
        fun_list[code] = {}
    fun_dic_val["code"] = fun_list

def addChild(fun_dic,cur_m,childname):
    fun_dic_val = fun_dic[cur_m]
    fun_list = {}
    if "child" in fun_dic_val:
        fun_list = fun_dic_val["child"]
    if "hastran" in fun_dic_val and fun_dic_val["hastran"] > 0:
        fun_list[childname] = {"aftertran":1}
    else:
        fun_list[childname] = {}
    fun_dic_val["child"] = fun_list

def findFun(line,fun_dic,cur_m,filepath,all_child={}):
    if line.startswith("//"):
        return
    if len(cur_m) == 0:
        return
    m = []
    if "throwErrCode" in line:
        m = re.findall(r'.*throwErrCode\([^\d]*(-?[1-9]\d*)[^\d]*\).*', line)
    m2 = re.findall(r'.*\.err_msg\[[^\d]*(-?[1-9]\d*)[^\d]*\].*', line)
    m = list(set(m).union(set(m2)))
    m3 = re.findall(r'.*[^\d]+(1[\d]{4})[^\d]+.*', line)
    m = list(set(m).union(set(m3)))
    if len(m) > 0:
        addCode(fun_dic,cur_m,m)
    if len(all_child) > 0:
        for funname in all_child:
            name_list = funname.split(".")
            justname = name_list[1]
            if funname+"(" in line:
                addChild(fun_dic,cur_m,funname)
            elif len(justname) > 5 and justname+"(" in line:
                addChild(fun_dic,cur_m,funname)
    #记录是否包含事务
    m4 = re.findall(r'.*db_pool.startTransaction.*', line)
    if len(m4) > 0:
        fun_dic_val = fun_dic[cur_m]
        last_tran_count = 0
        if "hastran" in fun_dic_val:
            last_tran_count = fun_dic_val["hastran"]
        last_tran_count += 1
        fun_dic_val["hastran"] = last_tran_count
        if "corun" in fun_dic_val:
            del fun_dic_val["corun"]     
    m5 = re.findall(r'.*corun tran.*', line)
    if len(m5) > 0:
        fun_dic_val = fun_dic[cur_m]
        fun_dic_val["corun"] = 1
    m6 = re.findall(r'.*db_pool.commit.*', line)
    m7 = re.findall(r'.*db_pool.rollback.*', line)
    if len(m6) > 0 or len(m7) > 0:
        fun_dic_val = fun_dic[cur_m]
        last_tran_count = fun_dic_val["hastran"]
        last_tran_count = last_tran_count - 1
        fun_dic_val["hastran"] = last_tran_count

def getAllHttpFun(filepath,all_child={}):
    fun_dic = {}
    line_list = open(filepath).readlines()
    last_m = ''
    cur_m = ''
    note = ''
    last_line = ''
    for line in line_list:
        line = line.strip("\r\n\t ")
        if last_line.startswith("/*") > 0:
            note = line.strip(" \s*")
        m = re.findall(r'.*\.(.+)\s*=\s*function\*\s*\(.*', line)
        if len(m) > 0:
            last_m = cur_m
            cur_m = m[0].strip("'\" ")
            cur_m = filepath.split('/').pop().split(".")[0]+"."+cur_m
            fun_dic[cur_m] = {"name":cur_m}
        if ':apiVer' in line:
            m = re.findall(r'.*regRequest\s*\((.+),\s*function\s*\*\s*\(\s*ctx\s*\).*', line)
            if len(m) > 0:
                last_m = cur_m
                cur_m = m[0].strip("'\" ")
                cur_m = cur_m.replace(':apiVer','v1')
                cur_m = filepath.split('/').pop().split(".")[0]+".js "+note+" "+cur_m
                fun_dic[cur_m] = {"name":cur_m}
        findFun(line,fun_dic,cur_m,filepath,all_child)
        last_line = line
                
    return fun_dic

def getHttpFunDic(file_dic,all_child={}):
    http_fun_dic = {}
    for k,v in file_dic.items():
        http_fun_dic.update(getAllHttpFun(k,all_child))
    return http_fun_dic

def getAllIMFun(filepath,all_child={}):
    fun_dic = {}
    line_list = open(filepath).readlines()
    last_m = ''
    cur_m = ''
    note = ''
    last_line = ''
    for line in line_list:
        line = line.strip("\r\n\t ")
        if last_line.startswith("/**") > 0:
            note = line.strip(" \s*")
        m = re.findall(r'.*\.(.+)\s*=\s*function\*\s*\(.*', line)
        if len(m) > 0:
            last_m = cur_m
            cur_m = m[0].strip("'\" ")
            cur_m = filepath.split('/').pop().split(".")[0]+"."+cur_m
            fun_dic[cur_m] = {"name":cur_m}
        m = re.findall(r'.*\.route\s*\((.+),\s*function\s*\*\s*\(\s*next\s*,\s*ctx\s*,\s*msg\s*,\s*cb\s*\).*', line)
        if len(m) > 0:
            last_m = cur_m
            cur_m = m[0].strip("'\" ")
            cur_m = filepath.split('/').pop().split(".")[0]+".js "+note+" "+cur_m
            fun_dic[cur_m] = {"name":cur_m}
        findFun(line,fun_dic,cur_m,filepath,all_child)
        last_line = line
    return fun_dic

def getIMFunDic(file_dic,all_child={}):
    im_fun_dic = {}
    for k,v in file_dic.items():
        im_fun_dic.update(getAllIMFun(k,all_child))
    return im_fun_dic

def getAllMgrFun(filepath,all_child={}):
    fun_dic = {}
    line_list = open(filepath).readlines()
    last_m = ''
    cur_m = ''
    for line in line_list:
        line = line.strip("\r\n\t ")
        m = re.findall(r'.*\.(.+)\s*=\s*function\*\s*\(.*', line)
        if len(m) > 0:
            last_m = cur_m
            cur_m = m[0].strip("'\" ")
            cur_m = filepath.split('/').pop().split(".")[0]+"."+cur_m
            fun_dic[cur_m] = {"name":cur_m}
        findFun(line,fun_dic,cur_m,filepath,all_child)
    return fun_dic

def getMgrFunDic(file_dic,all_child={}):
    mgr_fun_dic = {}
    for k,v in file_dic.items():
        mgr_fun_dic.update(getAllMgrFun(k,all_child))
    return mgr_fun_dic

def findAllCodes(nodek,nodev,all_node,has_contain_node):
    child_codes = {}
    if nodek in has_contain_node:
        return child_codes
    if "code" in nodev:
        child_codes = nodev["code"]
    if not "child" in nodev:
        return child_codes
    child_dic = nodev["child"]
    has_contain_node[nodek] = {}
    for k,v in child_dic.items():
        child_codes.update(findAllCodes(k,all_node[k],all_node,has_contain_node))
    #del has_contain_node[nodek]
    return child_codes

def findRouteChild(route_fun_dic,mgr_fun_dic):
    for k,v in route_fun_dic.items():
        has_contain_node = {}
        route_fun_dic[k]["code"] = findAllCodes(k,v,mgr_fun_dic,has_contain_node)

def writeErr(fun_dic,err_dic,fpa):
    fun_dic=sorted(fun_dic.iteritems(), key=lambda d:d[0])
    for k,v in fun_dic:
        if not "code" in v:
            continue
        code_dic = v["code"]
        if len(code_dic) == 0:
            continue
        code_dic=sorted(code_dic.iteritems(), key=lambda d:d[0])
        print >> fpa,"//",k
        for k,v in code_dic:
            try:
                err_dic[k]["used"] = 1
                print >> fpa, err_dic[k]["line"]
            except Exception, e:  
                print "eeeeeeeeeeeeeeee",str(e),getTraceStackMsg()
        print >> fpa,""

def writeCommonErr(err_dic,fpa):
    print >> fpa, "// 公用err"
    err_list=sorted(err_dic.iteritems(), key=lambda d:d[0])
    for k,v in err_list:
        if v["used"] == 0:
            print >> fpa, err_dic[k]["line"]

route_file_list = getFiles('routes')
http_file_dic = {}
im_file_dic = {}
for routefile in route_file_list:
    file_name = routefile.split('/').pop()
    if file_name.startswith('http'):
        http_file_dic[routefile] = {}
    elif file_name.startswith('im'):
        im_file_dic[routefile] = {}

#get function list
http_fun_dic = getHttpFunDic(http_file_dic)
#print json.dumps(http_fun_dic, indent=1);
im_fun_dic = getIMFunDic(im_file_dic)
#print json.dumps(im_fun_dic, indent=1);

mgr_file_list = getFiles('manager')
model_file_list = getFiles('model')
mgr_file_list = list(set(mgr_file_list).union(set(model_file_list))) 
mgr_file_dic = {}
for mgr_file in mgr_file_list:
    mgr_file_dic[mgr_file] = {}
mgr_fun_dic = getMgrFunDic(mgr_file_dic)
print json.dumps(mgr_fun_dic, indent=1)


http_fun_dic_withchild = getHttpFunDic(http_file_dic,mgr_fun_dic)
print json.dumps(http_fun_dic_withchild, indent=1);
im_fun_dic_withchild = getIMFunDic(im_file_dic,mgr_fun_dic)
print json.dumps(im_fun_dic_withchild, indent=1);

mgr_fun_dic_withchild = getMgrFunDic(mgr_file_dic,mgr_fun_dic)
print json.dumps(mgr_fun_dic_withchild, indent=1)

findRouteChild(http_fun_dic_withchild,mgr_fun_dic_withchild)
#print json.dumps(http_fun_dic_withchild, indent=1);

findRouteChild(im_fun_dic_withchild,mgr_fun_dic_withchild)
#print json.dumps(im_fun_dic_withchild, indent=1)

'''
err_dic = getErrMsg()
fpa=open("err_msg.txt","w")
writeErr(http_fun_dic_withchild,err_dic,fpa)
writeErr(im_fun_dic_withchild,err_dic,fpa)
writeCommonErr(err_dic,fpa)
fpa.close()
'''


def findTranList(nodek,nodev,all_node,tran_count,has_contain_node):
    dtran_route_list = [] #contain this
    if "corun" in nodev:
        tran_count = 0
    if "hastran" in nodev:
        tran_count = tran_count + 1
    if tran_count >= 2:
        return [nodev["name"]+" count>2"]
    if nodek in has_contain_node:
        last_count = has_contain_node[nodek]["trancount"]
        if tran_count - last_count > 0:
            return [nodev["name"]+" recur"]
        else:
            return []
    if not "child" in nodev:
        return []
    child_dic = nodev["child"]
    has_contain_node[nodek] = {"trancount":tran_count}
    for k,v in child_dic.items():
        real_count = tran_count
        if "hastran" in nodev and "aftertran" not in v and real_count >= 1:
            real_count = real_count - 1
        sub_route_list = findTranList(k,all_node[k],all_node,real_count,has_contain_node)
        for sub_route in sub_route_list:
            dtran_route_list.append(nodev["name"]+"; "+sub_route)
    del has_contain_node[nodek]
    return dtran_route_list


def findDubbleTran(route_fun_dic,mgr_fun_dic,fp):
    for k,v in route_fun_dic.items():
        tran_count = 0
        has_contain_node = {}
        dtran_route_list = findTranList(k,v,mgr_fun_dic,tran_count,has_contain_node)
        for dtran_route in dtran_route_list:
            print >> fp, dtran_route
        
fpa=open("dubbletran.txt","w")
findDubbleTran(http_fun_dic_withchild,mgr_fun_dic_withchild,fpa)
findDubbleTran(im_fun_dic_withchild,mgr_fun_dic_withchild,fpa)
fpa.close()
print "find dubble tran suc"



















