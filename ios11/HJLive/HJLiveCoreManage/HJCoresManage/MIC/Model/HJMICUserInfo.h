//
//  HJMICUserInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseObject.h"
#import "UserInfo.h"

@interface HJMICUserInfo : BaseObject

@property(nonatomic, assign) UserID uid;
@property(nonatomic, copy) NSString *erbanNo;
@property(nonatomic, copy) NSString *nick;
@property(nonatomic, assign) long birth;
@property(nonatomic, copy) NSString *star;
@property(nonatomic, copy) NSString *signture;
@property(nonatomic, copy) NSString *userVoice;
@property(nonatomic, assign) NSInteger voiceDura;
@property(nonatomic, assign) UserGender gender;
@property(nonatomic, copy) NSString *avatar;
@property(nonatomic, copy) NSString *region;
@property(nonatomic, copy) NSString *userDesc;
@property(nonatomic, assign) long followNum;
@property(nonatomic, assign) long fansNum;
@property(nonatomic, assign) long fortune;
@property(nonatomic, assign) AccountType defUser;
@property(nonatomic, assign) NSInteger experLevel;
@property(nonatomic, assign) NSInteger charmLevel;
@property(nonatomic, copy) NSString *phone;
@property(nonatomic, copy) NSString *carName;
@property(nonatomic, copy) NSString *carUrl;
@property(nonatomic, copy) NSString *time;
@property(nonatomic, copy) NSString *headwearName;
@property(nonatomic, copy) NSString *headwearUrl;
@property(nonatomic, assign) long createTime;
@property (nonatomic, copy) NSString *email;
@property (nonatomic, assign) BOOL isLike;
@property (nonatomic, assign) long linkNum;
@property (nonatomic, assign) long onlineNum;
@property (nonatomic, copy) NSString *roomAvatar;
@property (nonatomic, copy) NSString *roomId;
@property (nonatomic, assign) BOOL operatorStatus;



@end
