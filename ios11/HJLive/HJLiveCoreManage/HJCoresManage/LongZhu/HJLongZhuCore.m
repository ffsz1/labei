//
//  HJLongZhuCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJLongZhuCore.h"
#import "HJLongZhuCoreClient.h"

#import "HJImMessageCoreClient.h"
#import "HJImMessageCore.h"
#import "HJImMessageSendCoreClient.h"

#import "HJRoomLongZhuMsgModel.h"
#import "NSObject+YYModel.h"
#import "HJHttpRequestHelper+LongZhu.h"

@interface HJLongZhuCore ()<HJImMessageCoreClient,HJImMessageSendCoreClient>

@end

@implementation HJLongZhuCore

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (instancetype)init {
    if (self = [super init]) {
        AddCoreClient(HJImMessageCoreClient, self);
        AddCoreClient(HJImMessageSendCoreClient, self);
    }
    return self;
}

/**
 获取速配状态
 
 @param roomId 房间id
 */
- (void)getStateWithRoomId:(NSInteger)roomId {
    
    [HJHttpRequestHelper getStateWithRoomId:roomId success:^(NSDictionary *result) {
        
        NotifyCoreClient(HJLongZhuCoreClient, @selector(getStateSuccessWithResult:), getStateSuccessWithResult:result);
    } failure:^(NSNumber *code, NSString *msg) {
        
        NotifyCoreClient(HJLongZhuCoreClient, @selector(getStateFailedWithMessage:), getStateFailedWithMessage:msg);
    }];
}

/**
 取消解签
 
 @param roomId 房间id
 @param type 1 速配  2 选择
 @param result 如果type=1传空  type=2传选择的数
 */
- (void)cancelChooseResultWithRoomId:(NSInteger)roomId
                                type:(NSInteger)type
                              result:(NSString *)result {
    
    NSString *reStr = [result copy];
    
    if (type == 1) {
        NSInteger num = [result integerValue];
        NSInteger num1 = num/ 100;
        NSInteger num2 = (num - num1 * 100) / 10;
        NSInteger num3 = num - num1 * 100 - num2 * 10;
        reStr = [NSString stringWithFormat:@"%zd,%zd,%zd",num1,num2,num3];
    }
    
    [HJHttpRequestHelper cancelChooseResultWithRoomId:roomId type:type result:reStr success:^(NSInteger state) {
        NotifyCoreClient(HJLongZhuCoreClient, @selector(cancelChooseResultSuccessWithResult:type:), cancelChooseResultSuccessWithResult:state type:type);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJLongZhuCoreClient, @selector(cancelChooseResultFailedWithMessage:type:), cancelChooseResultFailedWithMessage:msg type:type);
    }];
}

/**
 获取速配随机数/保存自己选择的数
 
 @param roomId 房间id
 @param type 1 速配  2 选择
 @param result 如果type=1传空  type=2传选择的数
 */
- (void)getChooseResultWithRoomId:(NSInteger)roomId
                             type:(NSInteger)type
                           result:(NSString *)result {
    
    [HJHttpRequestHelper getChooseResultWithRoomId:roomId type:type result:result success:^(NSInteger state) {
        NotifyCoreClient(HJLongZhuCoreClient, @selector(getChooseResultSuccessWithResult:type:), getChooseResultSuccessWithResult:state type:type);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJLongZhuCoreClient, @selector(getChooseResultFailedWithMessage:type:), getChooseResultFailedWithMessage:msg type:type);
    }];
}

/**
 展示结果
 
 @param result 展示的数字
 @param roomId 房间id
 @param type type 1 速配  2 选择
 */
- (void)confirmResult:(NSString *)result
               roomId:(NSInteger)roomId
                 type:(NSInteger)type {
    
    NSString *reStr = [result copy];
    
    if (type == 1) {
        NSInteger num = [result integerValue];
        NSInteger num1 = num/ 100;
        NSInteger num2 = (num - num1 * 100) / 10;
        NSInteger num3 = num - num1 * 100 - num2 * 10;
        reStr = [NSString stringWithFormat:@"%zd,%zd,%zd",num1,num2,num3];
    }
    
    [HJHttpRequestHelper confirmResult:reStr roomId:roomId type:type success:^{
        NotifyCoreClient(HJLongZhuCoreClient, @selector(confirmResultSuccessWithType:), confirmResultSuccessWithType:type);
    } failure:^(NSNumber *code, NSString *msg) {
        NotifyCoreClient(HJLongZhuCoreClient, @selector(confirmResultFailedWithMessage:type:), confirmResultFailedWithMessage:msg type:type);
    }];
}

- (UIImage *)composeImgWithImge1:(UIImage *)img1 img2:(UIImage *)img2 img3:(UIImage *)img3 {
    
    CGFloat w = 42;
    CGFloat h = 66.5;
    
    UIGraphicsBeginImageContext(CGSizeMake(90, 130));
    [img1 drawInRect:CGRectMake(25, 0, w, h)];
    [img2 drawInRect:CGRectMake(5, 50, w, h)];
    [img3 drawInRect:CGRectMake(45, 50, w, h)];
    UIImage *resultImg = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return resultImg;
}

#pragma mark - ImMessageCoreClient
- (void)onRecvChatRoomCustomMsg:(HJIMMessage *)msg {
    if (msg.messageType == HJIMMessageTypeCustom) {
        JXIMCustomObject *obj = msg.messageObject;
        if (obj.attachment != nil && [obj.attachment isKindOfClass:[Attachment class]]) {
            Attachment *attachment = (Attachment *)obj.attachment;
            if (attachment.first == Custom_Noti_Header_LongZhu) {
                if (attachment.second == Custom_Noti_Sub_LongZhu_Supei || attachment.second == Custom_Noti_Sub_LongZhu_Choose) {
                    HJRoomLongZhuMsgModel *model = [HJRoomLongZhuMsgModel yy_modelWithJSON:attachment.data];
                    
                    NotifyCoreClient(HJLongZhuCoreClient, @selector(onReceiveLongZhuWithModel:isSuPei:), onReceiveLongZhuWithModel:model isSuPei:attachment.second == Custom_Noti_Sub_LongZhu_Supei);
                }
            }
        }
    }
}

- (void)onSendMessageSuccess:(NIMMessage *)msg {
    if (msg.messageType == NIMMessageTypeCustom) {
        NIMCustomObject *obj = (NIMCustomObject *)msg.messageObject;
        if (obj.attachment != nil && [obj.attachment isKindOfClass:[Attachment class]]) {
            Attachment *attachment = (Attachment *)obj.attachment;
            if (attachment.first == Custom_Noti_Header_LongZhu) {
                if (attachment.second == Custom_Noti_Sub_LongZhu_Supei || attachment.second == Custom_Noti_Sub_LongZhu_Choose) {
                    HJRoomLongZhuMsgModel *model = [HJRoomLongZhuMsgModel yy_modelWithJSON:attachment.data];
                    
                    NotifyCoreClient(HJLongZhuCoreClient, @selector(onReceiveLongZhuWithModel:isSuPei:), onReceiveLongZhuWithModel:model isSuPei:attachment.second == Custom_Noti_Sub_LongZhu_Supei);
                }
            }
        }
    }
}


@end
