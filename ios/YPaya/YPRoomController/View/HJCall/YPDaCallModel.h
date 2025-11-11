//
//  YPDaCallModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPDaCallModel : NSObject
//"avatar": "string",
//"conchNum": 0,
//"experLevel": 0,
//"giftId": 0,
//"giftName": "string",
//"giftNum": 0,
//"giftPic": "string",
//"giftSendTime": 0,
//"goldPrice": 0,
//"nick": "string",
//"roomId": 0,
//"targetAvatar": "string",
//"targetNick": "string",
//"targetUid": 0,
//"targetUids": [
//  0
//],
//"uid": 0,
//"useGiftPurseGold": 0,
//"userGiftPurseNum": 0,
//"userNo": 0
//、、、、、、、、、、、、、、、、、
//avatar = "http://qc422frwu.bkt.clouddn.com/FljLfoiQctPhlccXDEn-cannGxg4?imageslim";
//  conchNum = 3;
//  experLevel = 25;
//  giftId = 425;
//  giftName = "\U6728\U7b4f";
//  giftNum = 1;
//  giftPic = "http://qc422frwu.bkt.clouddn.com/FjG76J5s9U1aJ2kJ1-oMYmceOC35?imageslim";
//  giftSendTime = 1594799509285;
//  goldPrice = 20;
//  nick = "\U989c\U5982\U7389";
//  targetAvatar = "http://qc422frwu.bkt.clouddn.com/FnVrm8nOu5GSSdoOO6zc6XCv7H0Q?imageslim";
//  targetNick = "\U9648";
//  targetUid = 56;
//  uid = 5;
//  useGiftPurseGold = 20;

@property (nonatomic,assign) NSInteger conchNum;
@property (nonatomic,assign) NSInteger experLevel;
@property (nonatomic,assign) NSInteger giftId;
@property (nonatomic,strong) NSString* giftName;
@property (nonatomic,assign) NSInteger giftNum;
@property (nonatomic,strong) NSString* giftPic;
@property (nonatomic,assign) long giftSendTime;
@property (nonatomic,assign) NSInteger goldPrice;

@property (nonatomic,strong) NSString *nick;
@property (nonatomic,strong) NSString *targetAvatar;
@property (nonatomic,strong) NSString *targetNick;
@property (nonatomic,assign) NSInteger targetUid;
@property (nonatomic,assign) NSInteger uid;
@property (nonatomic,assign) NSInteger useGiftPurseGold;

@end

NS_ASSUME_NONNULL_END
