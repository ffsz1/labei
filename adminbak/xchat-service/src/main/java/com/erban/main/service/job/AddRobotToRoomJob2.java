package com.erban.main.service.job;


import com.beust.jcommander.internal.Lists;
import com.erban.main.model.Users;
import com.erban.main.model.UsersExample;
import com.erban.main.mybatismapper.UsersMapper;
import com.erban.main.service.ErBanNetEaseService;
import com.erban.main.vo.RunningRoomVo;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class AddRobotToRoomJob2 implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddRobotToRoomJob2.class);
    private Type type = new TypeToken<List<String>>() {
    }.getType();
    //机器人最大数
    private static final Integer MAX_ROBOT_NUM = 20;
    //上一次房间数据
    private static Map<String, String> preRoom = null;
    private static Map<String, String> currentRoom = null;
    private static Map<String, String> runRobot = null;
    private static List<String> notRunRobot = null;
    @Autowired
    private JedisService jedisService;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private ErBanNetEaseService erBanNetEaseService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        /*
        1. 房间关闭问题
         */
        updateCurrentInfo();
        try {
            //当前没有在线房间
            if (CollectionUtils.isEmpty(currentRoom)) {
                LOGGER.info("----------当前没有在线房间----------");
                //清除所有机器人
                clearAllRobotInPreRoom();
                preRoom = null;
                return;
            } else {//当前有在线房间
                clearRobotInClosedRoom();
            }
            /*
            2. 机器人添加问题
                （1）随机获取一个房间
                （2）机器人操作
                    ① 移除机器人
                    ② 增加机器人
             */
            for (Map.Entry<String, String> currentEntry : currentRoom.entrySet()) {

                addRobotInRoom(currentEntry.getKey());

            }
//            String roomKey = getRoomIdWithRandom ( );
//            LOGGER.info ( "随机选择的房间是： " + roomKey );
            //对房间进行操作
//            addRobotInRoom ( roomKey );
            /*
            3. 将数据写入缓存
             */
            saveCurrentInfo();
            preRoom = clone(currentRoom);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 添加机器人操作
     * (先把旧的机器从run_robot找出来，在从not_run_robot中随机抽取一定数量的机器人填入，在最后再把旧的机器人插入到not_run_robot
     * 中（防止旧的机器人和新的机器人重叠）)
     * 每次往房间里增加一个机器人，当数量达到了设定值MAX_ROBOT_NUM，每次移除一个机器人，同时增加一个机器人，保持数量在MAX_ROBOT_NUM
     *
     * @param roomKey
     */
    private void addRobotInRoom(String roomKey) {

        Integer preRobotNum = 0;
        boolean overRobot = false;
        //判断房间是否存在机器人
        //如果有，移除
        List<String> oldRobotList = getRobotListInRoom(roomKey);
        if (!CollectionUtils.isEmpty(oldRobotList)) {
            //存在机器人
            preRobotNum = oldRobotList.size();
            //移除机器人
        }
        //房间机器人数量超过设定值，
        if (preRobotNum >= MAX_ROBOT_NUM) {
            overRobot = true;
        }
        //获取notRobot数量
        Integer notRobotNum = notRunRobot.size();
        //往房间里增加一定数量（大于或等于上一次的数量）机器人
        Random random = new Random();
        Integer addNum = 1;//random.nextInt ( MAX_ROBOT_NUM + 1 - preRobotNum ) + preRobotNum;
        if (notRobotNum==0) {
            //添加机器人的数量比空闲机器人的数量总数还要大
            notRunRobot=Lists.newArrayList();
            LOGGER.info("机器人库不足！！！" + "剩余：" + notRobotNum);
            return;
        }
        LOGGER.info("往房间增加：" + addNum + "个机器人");
        //随机从房间里删除一定数量的机器人
        List<String> deleteRobotList = new ArrayList<>();
        //产生addNum个数量的随机机器人
        List<String> robotList = new ArrayList<>();
        for (int i = 0; i < addNum; i++) {
            //添加addNum个机器人
            robotList.add(notRunRobot.get(random.nextInt(notRobotNum)));
            //随机删除addNum个机器人
            if (overRobot) {
                deleteRobotList.add(oldRobotList.get(random.nextInt(oldRobotList.size())));
            }
        }
        //TODO 云信操作
        String accidsDel = new Gson().toJson(deleteRobotList);
        String accidsAdd = new Gson().toJson(robotList);
        RunningRoomVo runningRoomVo = null;
        for (Map.Entry<String, String> currentEntry : currentRoom.entrySet()) {
            if (currentEntry.getKey().equals(roomKey)) {
                String value = currentEntry.getValue();
                runningRoomVo = new Gson().fromJson(value, RunningRoomVo.class);
            }
        }
        try {
            if (!CollectionUtils.isEmpty(deleteRobotList)) {
                erBanNetEaseService.deleteRobotToRoom(runningRoomVo.getRoomId(), accidsDel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            erBanNetEaseService.addRobotToRoom(runningRoomVo.getRoomId(), accidsAdd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (CollectionUtils.isEmpty(oldRobotList)) {
            oldRobotList = new ArrayList<>();
        }
        for (int i = 0; i < robotList.size(); i++) {
            oldRobotList.add(robotList.get(i));
        }
        for (int i = 0; i < deleteRobotList.size(); i++) {
            oldRobotList.remove(deleteRobotList.get(i));
        }
        insertRobotInCache(roomKey, oldRobotList, deleteRobotList);

    }

    /**
     * 随机获取一个房间Id
     *
     * @return 房间ID
     */
    private String getRoomIdWithRandom() {

        Random random = new Random();
        Integer roomNum = currentRoom.size();
        List<String> roomKeyList = new ArrayList<>();
        //rand.nextInt(MAX - MIN + 1) + MIN; // randNumber 将被赋值为一个 MIN 和 MAX 范围内的随机数
        Integer randNum = random.nextInt(roomNum) + 0;
        for (Map.Entry<String, String> roomEntry : currentRoom.entrySet()) {
            roomKeyList.add(roomEntry.getKey());
        }
        return roomKeyList.get(randNum);
    }

    /**
     * 清除上一次房间中的所有机器人
     */
    private void clearAllRobotInPreRoom() throws Exception {

        List<String> roomList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(preRoom)) {
            for (Map.Entry<String, String> preEntry : preRoom.entrySet()) {
                roomList.add(preEntry.getKey());
            }
            //TODO 清除上一次房间中的所有机器人
            clearRobotInClosedRoom(roomList);
        }
        //两次都没有在线的房间


    }

    /**
     * 从Redis中读取当前房间信息，机器人在房间的情况，机器人不在房间的情况
     */
    private void updateCurrentInfo() {

        //获取当前房间状态,在房间的机器人，不在房间的机器人
        currentRoom = jedisService.hgetAllBykey(RedisKey.room_running.getKey());
        runRobot = jedisService.hgetAllBykey(RedisKey.running_robot.getKey());
        notRunRobot = getNotRunRobot();
    }

    private void saveCurrentInfo() {
        jedisService.hwrite(RedisKey.running_robot.getKey(), runRobot);
        String notRunRobotStr = new Gson().toJson(notRunRobot);
        jedisService.set(RedisKey.not_running_robot.getKey(), notRunRobotStr);
    }

    /**
     * 当前有在线的房间
     * 1.查找是否存在关闭的房间？
     * 2.（清除残余机器人）
     */
    private void clearRobotInClosedRoom() throws Exception {

        List<String> closeRommList = new ArrayList<>();
        boolean hasClosedRoom = true;
        //没有上一次房间信息，说明属于都是新开的房(不存在关闭的房间（残留机器人）)
        if (preRoom == null) {
            LOGGER.info("----------上一次没有开房记录----------");
            preRoom = clone(currentRoom);
            return;
        }
        /**
         * 找出关闭房（上一次记录存在，当前记录不存在的房间）
         * 遍历上次信息，查看当前房是否存在，存在：房间未关闭；不存在:反之
         */
        //比较两次房间信息(uid是否相等)
        for (Map.Entry<String, String> preEntry : preRoom.entrySet()) {
            for (Map.Entry<String, String> entry : currentRoom.entrySet()) {
                //uid相等,两次房间相同
                if (entry.getKey().equals(preEntry.getKey())) {
                    LOGGER.info("前后两次有相同的房间：" + preEntry.getKey());
                    hasClosedRoom = false;
                    break;
                }
            }
            if (hasClosedRoom) {
                LOGGER.info("存在关闭房：" + preEntry.getKey());
                closeRommList.add(preEntry.getKey());
            }
            hasClosedRoom = true;
        }
        //TODO 清理关闭房间中机器人处理
        clearRobotInClosedRoom(closeRommList);

    }


    /**
     * 清除关闭房间中的机器人
     *
     * @param roomList 关闭房间列表（）
     */
    private void clearRobotInClosedRoom(List<String> roomList) throws Exception {

        for (int i = 0; i < roomList.size(); i++) {

            /*
            更新缓存中的机器人的信息
            (这里先不对redis直接操作，先对runRobot，和notRunRobot操作，最后再写进去)
            （一个房间可能存在很多机器人，所以先获取机器人列表）
             */
            //获取机器人列表
            List<String> robotList = getRobotListInRoom(roomList.get(i));
            //1.running_robot(删除正在房间的机器人)
            //2.not_running_robot（增加到不在房间的机器人列表中）（通过机器人的uid）
            deleteRobotInCache(roomList.get(i), robotList);
            //TODO 删除云信中机器人
            //删除当前房间机器人
            List<String> array = Lists.newArrayList();
            for (int j = 0; j < robotList.size(); j++) {
                array.add(robotList.get(j));
            }
            String accids = new Gson().toJson(array);
            RunningRoomVo runningRoomVo = null;
            for (Map.Entry<String, String> preEntry : preRoom.entrySet()) {
                if (preEntry.getKey().equals(roomList.get(i))) {
                    String value = preEntry.getValue();
                    runningRoomVo = new Gson().fromJson(value, RunningRoomVo.class);
                }
            }
            erBanNetEaseService.deleteRobotToRoom(runningRoomVo.getRoomId(), accids);

        }
    }

    /**
     * 把机器人从运行状态切换到空闲状态
     * （在房间 == 》 不在房间）
     *
     * @param roomKey   房间ID
     * @param robotList 机器人列表
     */
    private void deleteRobotInCache(String roomKey, List<String> robotList) {
        //删除run_robot
        runRobot.remove(roomKey);
        //遍历删除notRunRobot
        if (!CollectionUtils.isEmpty(robotList)) {
            for (int i = 0; i < robotList.size(); i++) {
                notRunRobot.add(robotList.get(i));
            }
        }
    }

    /**
     * 把机器人从空闲状态切换到运行状态
     * （不在房间 == 》在房间）
     * 移除机器人：从run_robot更新机器人列表，从not_run_robot移除并增加
     *
     * @param roomKey      房间ID
     * @param robotList    机器人列表
     * @param oldRobotList 旧的机器人
     */
    private void insertRobotInCache(String roomKey, List<String> robotList, List<String> oldRobotList) {

        //1.把房间里的机器人删除run_robot
        runRobot.remove(roomKey);
        //2.把机器人插入到房间run_robot
        String robotListStr = new Gson().toJson(robotList);
        runRobot.put(roomKey, robotListStr);
        //3.更新不在房间机器人列表not_run_robot
        if (!CollectionUtils.isEmpty(robotList)) {
            for (int i = 0; i < robotList.size(); i++) {
                //移除
                notRunRobot.remove(robotList.get(i));
            }
        }
        for (int i = 0; i < oldRobotList.size(); i++) {
            notRunRobot.add(oldRobotList.get(i));
        }

    }


    /**
     * 获取某个房间的机器人列表
     *
     * @param roomKey 房间ID
     * @return
     */
    private List<String> getRobotListInRoom(String roomKey) {


        List<String> robotList = new ArrayList<>();
        String robotListStr = "";
        //遍历running_robot,查找对应房间的机器人
        if (!CollectionUtils.isEmpty(runRobot)) {
            for (Map.Entry<String, String> runRobotEntry : runRobot.entrySet()) {
                if (runRobotEntry.getKey().equals(roomKey)) {
                    robotListStr = runRobotEntry.getValue();
                }
            }

        }
        robotList = new Gson().fromJson(robotListStr, type);
        return robotList;

    }

    //Map克隆方法：
    public <T extends Serializable> T clone(Map<String, String> obj) {
        T cloneObj = null;
        try {
            // 写入字节流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream obs = new ObjectOutputStream(out);
            obs.writeObject(obj);
            obs.close();

            // 分配内存，写入原始对象，生成新对象
            ByteArrayInputStream ios = new ByteArrayInputStream(out.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(ios);
            // 返回生成的新对象
            cloneObj = (T) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cloneObj;
    }

    /**
     * 获取没有在房间(空闲)的机器人
     *
     * @return
     */
    private List<String> getNotRunRobot() {

        List<String> notRunRobot = null;
        notRunRobot = getNotRunRobotCache();
        //从缓存中获取
        if (CollectionUtils.isEmpty(notRunRobot)) {
            //从数据库中获取
            notRunRobot = getNotRunRobotDB();
            if (CollectionUtils.isEmpty(notRunRobot)) {
                return null;
            } else {
                saveNotRunRobotCache(notRunRobot);
            }
        }
        return notRunRobot;
    }

    private void saveNotRunRobotCache(List<String> list) {

        String string = new Gson().toJson(list);
        jedisService.set(RedisKey.not_running_robot.getKey(), string);

    }

    private List<String> getNotRunRobotCache() {

        String notRunRobotStr = jedisService.read(RedisKey.not_running_robot.getKey());
        if (notRunRobotStr == null) {
            return null;
        }
        List<String> list = new Gson().fromJson(notRunRobotStr, type);
        return list;
    }

    private List<String> getNotRunRobotDB() {
        LOGGER.info("==========从数据库中获取空闲机器人列表==========");
        List<String> list = new ArrayList<>();
        UsersExample usersExample = new UsersExample();
        usersExample.createCriteria().andDefUserEqualTo((byte) 3);
        List<Users> usersList = usersMapper.selectByExample(usersExample);
        if (!CollectionUtils.isEmpty(list)) {
            return null;
        }
        for (int i = 0; i < usersList.size(); i++) {
            list.add(String.valueOf(usersList.get(i).getUid()));
        }
        return list;
    }

    public static void main(String[] args) {
        Random rand = new Random();
        for (int i = 0; i < 1000; i++) {
            //int randNumber = rand.nextInt(MAX - MIN + 1) + MIN; // randNumber 将被赋值为一个 MIN 和 MAX 范围内的随机数
            System.out.println(rand.nextInt(0) + 0);
        }

    }
}
