package com.erban.main.service.test;

import com.aliyun.openservices.ons.api.*;

import java.util.Properties;

/**
 * Created by liuguofu on 2017/10/1.
 */
public class ConsumerTest {
    static String AccessKeySecret="x8YNPXkTmZsHfRL95KR49vMjbgAvNG";
    static String AccessKeySecretID="LTAI9dJXyTIlGzJT";
    public static void main(String[] args) {

        Properties properties = new Properties();
        // 您在MQ控制台创建的ConsumerId
        properties.put(PropertyKeyConst.ConsumerId, "CID_gift_send_consumer");
        // 鉴权用AccessKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey,AccessKeySecretID);
        // 鉴权用SecretKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, AccessKeySecret);
        // 设置 TCP 接入域名（此处以公共云公网环境接入为例）
        properties.put(PropertyKeyConst.ONSAddr,
                "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet");
        Consumer consumer = ONSFactory.createConsumer(properties);
        consumer.subscribe("erban_test_gift", "*", new MessageListener() {
            public Action consume(Message message, ConsumeContext context) {
                System.out.println("Receive: " + message);
                return Action.CommitMessage;
            }
        });
        consumer.start();
        System.out.println("Consumer Started");
    }
}
