//
//  YPHomePageInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPChatRoomInfo.h"
#import "UserInfo.h"

@interface YPHomePageInfo : YPChatRoomInfo

@property (nonatomic, copy)NSString *avatar;
@property (nonatomic, copy)NSString *nick;
@property (nonatomic, copy) NSString *tagPict;
@property (nonatomic, copy) NSString *badge;
@property (nonatomic, assign) NSInteger age;
@property (nonatomic, copy) NSString *tagId;
@property (nonatomic, copy) NSString *optPic;
@property (nonatomic, copy) NSString *userDescription;
@property (nonatomic, assign) long voiceDuration;
@property (nonatomic, copy) NSString *userVoice;
@property (nonatomic, assign) BOOL isAttention;//是否关注
@property (nonatomic, copy) NSString *isleft;
@end
/*
 abChannelType = 1;
 avatar = "https://img.erbanyy.com/FlCig-PuqCn15T45kLBku4NASHmy?imageslim";
 backPic = "";
 calcSumDataIndex = 0;
 exceptionClose = 0;
 gender = 1;
 isExceptionClose = 0;
 meetingName = b532df45a53a498ea2a7066bc16ba69d;
 nick = yezi;
 officeUser = 1;
 onlineNum = 100;
 openTime = 1510303994000;
 operatorStatus = 2;
 roomDesc = "";
 roomId = 17948472;
 roomPwd = "";
 title = "yezi\U5565\U8bfe\U770b\U8003\U573a\U6ca1\U6253\U5f00\U54c7\U5494\U5494\U7684\U623f\U95f4";
 type = 2;
 uid = 90651;
 valid = 0;
 }
 );
 listRoom =     (
 {
 avatar = "https://img.erbanyy.com/FjihDeJMqD03nKUFZWQDErWh1ldc?imageslim";
 backPic = "";
 calcSumDataIndex = 0;
 gender = 1;
 isPermitRoom = 2;
 nick = "\U5b88\U62a4\U5929\U4f7f";
 onlineNum = 10;
 recomSeq = 100;
 roomDesc = "";
 roomId = 17966070;
 roomTag = "\U804a\U5929";
 score = "1.56862745";
 tagId = 8;
 tagPict = "https://img.erbanyy.com/tag%E8%81%8A%E5%A4%A9.png";
 title = "\U5b88\U62a4\U5929\U4f7f\U7684\U623f\U95f4";
 type = 3;
 uid = 90658;
 */
