//
//  HJMyTaskModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef NS_ENUM(NSUInteger, XCMyTaskUdStatus) {
    XCMyTaskUdStatusNotYet = 1, //未完成
    XCMyTaskUdStatusGetCorn,    //未领取奖励
    XCMyTaskUdStatusFinish,     //完成
};

typedef NS_ENUM(NSUInteger, XCMyTaskModelDutyType) {
    XCMyTaskModelDutyTypeSettingSign = 1, //设置个性签名
    XCMyTaskModelDutyTypeUploadPhoto,     //上传5张图片
    XCMyTaskModelDutyTypePraise,          //关注一个主播
    XCMyTaskModelDutyTypeSendAMsg,        //去大厅发言
    XCMyTaskModelDutyTypeBangdingPhone,   //绑定手机号码
    XCMyTaskModelDutyTypeWechatShare,     //朋友圈分享
    XCMyTaskModelDutyTypeQQShare,         //qq空间分享
    XCMyTaskModelDutyTypeZaDan,           //砸蛋一次
    XCMyTaskModelDutyTypeSendAGift,       //送一次礼物
    XCMyTaskModelDutyTypeTopUp            //充值
};

@interface HJMyTaskModel : NSObject

@property (nonatomic, assign) XCMyTaskModelDutyType dutyId;
@property (nonatomic, copy) NSString *dutyName;
@property (nonatomic, assign) NSInteger goldAmount;
@property (nonatomic, assign) XCMyTaskUdStatus udStatus;

@property (nonatomic, assign) BOOL isHandleRequst;

@end
