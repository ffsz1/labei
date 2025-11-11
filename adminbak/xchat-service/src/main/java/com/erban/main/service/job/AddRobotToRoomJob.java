package com.erban.main.service.job;

import com.beust.jcommander.internal.Lists;
import com.erban.main.model.Users;
import com.erban.main.service.ErBanNetEaseService;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.RunningRoomVo;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 随机加减机器人任务
 *
 * @author yanghaoyu
 */
public class AddRobotToRoomJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(AddRobotToRoomJob.class);

    @Autowired
    private JedisService jedisService;

    @Autowired
    private ErBanNetEaseService erBanNetEaseService;

    @Autowired
    private UsersService usersService;

    private Gson gson = new Gson();

    private static boolean flags = false;


    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        logger.info("正在执行随机加减机器人至房间定时任务.");
        //获取所有正在运行的房间
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<Long> notAddIds = Constant.IOSAuditAccount.auditAccountList;
        List<String> removeIds = Lists.newArrayList();
        List<String> addIds = Lists.newArrayList();
        Map<String, String> map = jedisService.hgetAllBykey(RedisKey.room_running.getKey());
        //判断上次添加机器人的房间是否有关闭的，如果有则找出来并把添加的机器人还原到not_running_robot中
        if (map != null && map.size() > 0) {
            flags = false;
            Map<String, String> map2 = jedisService.hgetAllBykey(RedisKey.running_robot.getKey());
            if (map2 != null && map2.size() > 0) {
                for (Map.Entry<String, String> entry : map2.entrySet()) {
                    boolean flag = false;
                    String roomUid = null;
                    for (Map.Entry<String, String> stringEntry : map.entrySet()) {
                        if (entry.getKey().equals(stringEntry.getKey())) {
                            flag = true;
                            break;
                        }
                        if (!flag) {
                            roomUid = entry.getKey();
                        }
                    }
                    if (!flag) {
                        //当前房间为关闭房间，将该房间的机器人id从running_robot中移动到not_running_robot.
                        jedisService.hdelete(RedisKey.running_robot.getKey(), roomUid, null);
                        removeIds.add(roomUid.toString());
                    }
                }
            }
            for (String removeId : removeIds) {
                map2.remove(removeId);
            }
            //加机器人操作
            //获得未被使用的机器人ids
            String idsNotRunStr = jedisService.read(RedisKey.not_running_robot.getKey());
            if (StringUtils.isEmpty(idsNotRunStr)) {
                idsNotRunStr = getRobotList();
            }
            List<String> ids = gson.fromJson(idsNotRunStr, type);
            //打乱机器人顺序
            Collections.shuffle(ids);
            //获取已存在机器人的房间
            for (Map.Entry<String, String> entry : map.entrySet()) {
                //获得当前房间
                if (notAddIds.contains(Long.parseLong(entry.getKey()))) {
                    continue;
                }
                RunningRoomVo runningRoomVo = gson.fromJson(entry.getValue(), RunningRoomVo.class);
                //判断当前房间是否有机器人
                String robotStr = null;
                for (Map.Entry<String, String> entrys : map2.entrySet()) {
                    if (entrys.getKey().equals(entry.getKey())) {
                        robotStr = entrys.getValue();
                        break;
                    }
                }
                List<String> idList = Lists.newArrayList();
                if (!StringUtils.isEmpty(robotStr)) {
                    idList = gson.fromJson(robotStr, type);
                }
                //判断当前房间机器人数量是不是大于20
                if (idList.size() < 20) {
                    //获取要添加的机器人
                    if (CollectionUtils.isEmpty(ids)) {
                        idsNotRunStr = getRobotList();
                        ids = gson.fromJson(idsNotRunStr, type);
                    }
                    String id = ids.remove(ids.size() - 1);
                    idList.add(id);
                    String idListStr = gson.toJson(idList);
                    try {
                        //将机器人添加到房间
                        List<String> array = Lists.newArrayList();
                        addIds.add(id);
                        array.add(id);
                        String accids = gson.toJson(array);
                        erBanNetEaseService.addRobotToRoom(runningRoomVo.getRoomId(), accids);
                        //将添加的机器人添加到running_robot缓存中
                        map2.put(runningRoomVo.getUid().toString(), idListStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            //减机器人操作
            Random random = new Random();
            if (map.size() <= 2) {
                int randomNum = random.nextInt(map.size());
                int num = 0;
                //房间数小于2
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (randomNum != num) {
                        num++;
                        continue;
                    }
                    //随机选取一个房间删除机器人
                    RunningRoomVo runningRoomVo = gson.fromJson(entry.getValue(), RunningRoomVo.class);
                    //获取当前房间的机器人列表
                    String idListStr = null;
                    for (Map.Entry<String, String> entrys : map2.entrySet()) {
                        if (entrys.getKey().equals(runningRoomVo.getUid().toString())) {
                            idListStr = entrys.getValue();
                        }
                    }
                    if (StringUtils.isEmpty(idListStr)) {
                        continue;
                    }
                    List<String> list = gson.fromJson(idListStr, type);
                    if (list.size() < 5) {
                        continue;
                    }
                    Collections.shuffle(list);
                    //得到删除的机器人id，并将其添加到not_running_robot缓存中
                    String id = null;
                    if (addIds.contains(list.get(list.size() - 1))) {
                        if (list.size() < 2) {
                            continue;
                        }
                        id = list.remove(list.size() - 2);
                    } else {
                        id = list.remove(list.size() - 1);
                    }
                    ids.add(id);
                    try {
                        //删除当前房间机器人
                        List<String> array = Lists.newArrayList();
                        array.add(id);
                        String accids = gson.toJson(array);
                        erBanNetEaseService.deleteRobotToRoom(runningRoomVo.getRoomId(), accids);
                        //将删除的机器人从running_robot中删除
                        idListStr = gson.toJson(list);
                        map2.put(runningRoomVo.getUid().toString(), idListStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            } else {
                List<Integer> lists = Lists.newArrayList();
                int roomSize = 0;
                while (roomSize < 2) {
                    int randomNum = random.nextInt(map.size());
                    int num = 0;
                    if (lists.contains(randomNum)) {
                        continue;
                    }
                    lists.add(randomNum);
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        if (randomNum != num) {
                            num++;
                            continue;
                        }
                        //随机选取一个房间删除机器人
                        RunningRoomVo runningRoomVo = gson.fromJson(entry.getValue(), RunningRoomVo.class);
                        //获取当前房间的机器人列表
                        String idListStr = null;
                        for (Map.Entry<String, String> entrys : map2.entrySet()) {
                            if (entry.getKey().equals(entrys.getKey())) {
                                idListStr = entrys.getValue();
                            }
                        }
                        if (StringUtils.isEmpty(idListStr)) {
                            continue;
                        }
                        List<String> list = gson.fromJson(idListStr, type);
                        Collections.shuffle(list);
                        //得到删除的机器人id，并将其添加到not_running_robot缓存中
                        String id = null;
                        if (addIds.contains(list.get(list.size() - 1))) {
                            if (list.size() < 2) {
                                continue;
                            }
                            id = list.remove(list.size() - 2);
                        } else {
                            id = list.remove(list.size() - 1);
                        }
                        ids.add(id);
                        roomSize++;
                        try {
                            List<String> array = Lists.newArrayList();
                            array.add(id);
                            String accids = gson.toJson(array);
                            //删除当前房间机器人
                            erBanNetEaseService.deleteRobotToRoom(runningRoomVo.getRoomId(), accids);
                            //将删除的机器人从running_robot中删除
                            if (!CollectionUtils.isEmpty(list)) {
                                idListStr = gson.toJson(list);
                                map2.put(runningRoomVo.getUid().toString(), idListStr);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
            jedisService.hwrite(RedisKey.running_robot.getKey(), map2);
            idsNotRunStr = gson.toJson(ids);
            jedisService.set(RedisKey.not_running_robot.getKey(), idsNotRunStr);
        } else {
            if (!flags) {
                logger.info("无人开房，删除已使用的机器人");
                jedisService.hdeleteKey(RedisKey.running_robot.getKey());
                flags = true;
            } else {
                logger.info("无人开房，无正在使用的机器人");
            }
        }
    }

    //获得100男性机器人，50女性机器人
    public String getRobotList() {
        Map<String, String> woManRobotMap = jedisService.hgetAll(RedisKey.add_woman.getKey());
        List<String> ids = Lists.newArrayList();
        List<String> ids2 = Lists.newArrayList();
        if (woManRobotMap != null && woManRobotMap.size() > 0) {
            for (Map.Entry<String, String> entry : woManRobotMap.entrySet()) {
                Type type = new TypeToken<List<String>>() {
                }.getType();
                ids = gson.fromJson(entry.getValue(), type);
                jedisService.hdelete(RedisKey.add_woman.getKey(), entry.getKey(), null);
                Integer i = Integer.parseInt(entry.getKey()) + 50;
                if (i > ids.size() - 1) {
                    jedisService.hwrite(RedisKey.add_woman.getKey(), 50 + "", entry.getValue());
                    ids = ids.subList(0, 50);
                } else {
                    jedisService.hwrite(RedisKey.add_woman.getKey(), i.toString(), entry.getValue());
                    ids = ids.subList(Integer.parseInt(entry.getKey()), i);
                }
            }
        } else {
            List<Users> list = usersService.getUsersByRobot((byte) 2);
            List<String> arrays = Lists.newArrayList();
            for (Users user : list) {
                arrays.add(user.getUid().toString());
            }
            String listStr = gson.toJson(arrays);
            jedisService.hwrite(RedisKey.add_woman.getKey(), 0 + "", listStr);
            ids.addAll(arrays);
        }
        Map<String, String> manRobotMap = jedisService.hgetAll(RedisKey.add_man.getKey());
        if (manRobotMap != null && manRobotMap.size() > 0) {
            for (Map.Entry<String, String> manEntry : manRobotMap.entrySet()) {
                Type type = new TypeToken<List<String>>() {
                }.getType();
                ids2 = gson.fromJson(manEntry.getValue(), type);
                jedisService.hdelete(RedisKey.add_man.getKey(), manEntry.getKey(), null);
                Integer i = Integer.parseInt(manEntry.getKey()) + 100;
                if (i > ids2.size() - 1) {
                    jedisService.hwrite(RedisKey.add_man.getKey(), 100 + "", manEntry.getValue());
                    ids2 = ids2.subList(0, 100);
                } else {
                    jedisService.hwrite(RedisKey.add_man.getKey(), i.toString(), manEntry.getValue());
                    ids2 = ids2.subList(Integer.parseInt(manEntry.getKey()), i);
                }
            }
        } else {
            List<Users> list = usersService.getUsersByRobot((byte) 1);
            List<String> arrays = Lists.newArrayList();
            for (Users user : list) {
                arrays.add(user.getUid().toString());
            }
            String listStr = gson.toJson(arrays);
            jedisService.hwrite(RedisKey.add_man.getKey(), 0 + "", listStr);
            ids2.addAll(arrays);
        }
        ids.addAll(ids2);
        String idsNotRunStr = gson.toJson(ids);
        jedisService.set(RedisKey.not_running_robot.getKey(), idsNotRunStr);
        return idsNotRunStr;
    }
}
