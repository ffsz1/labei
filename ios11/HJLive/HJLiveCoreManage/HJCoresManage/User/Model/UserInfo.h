//
//  UserInfo.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//


#import "BaseObject.h"
#import "UserPhoto.h"
#import "UserGift.h"
typedef enum {
    UserInfo_Male = 1,
    UserInfo_Female = 2
}UserGender;

typedef enum {
    AccountType_Common = 1,
    AccountType_Official = 2,
    AccountType_Robot = 3
}AccountType;

//"code": 200,
//"message": "success",
//"data": {
//    "uid": 900000,
//    "erbanNo": "666666",
//    "birth": "1990-01-01",
//    "star": "白羊座",
//    "nick": "你妹的",
//    "signture": "你妹的说好的幸福呢",
//    "userVoice": "http://xxxx.com",
//    "voiceDura":10,
//    "gender": "1",
//    "avatar": "http://xxxx.com",
//    "region": "广东广州",
//    "userDesc": "简单的自我介绍"
//    "followNum": 2,
//    "fansNum": 1,
//    "defUser":”1”,// 1普通账号，2官方账号，3机器账号
//    "fortune":19827   //人气值
//    
//}
//}

@interface UserInfo : BaseObject
@property (nonatomic, assign) UserID uid;
@property (nonatomic, strong) NSString *erbanNo;
@property (nonatomic, strong) NSString *nick;
@property (nonatomic, assign) long birth;
@property (nonatomic, strong) NSString *star;
@property (nonatomic, strong) NSString *signture;
@property (nonatomic, strong) NSString *userVoice;
@property (nonatomic, assign) NSInteger voiceDura;
@property (nonatomic, assign) UserGender gender;
@property (nonatomic, strong) NSString *avatar;
@property (nonatomic, strong) NSString *region;
@property (nonatomic, strong) NSString *userDesc;
@property (nonatomic, assign) long followNum;
@property (nonatomic, assign) long fansNum;
@property (nonatomic, assign) long fortune;
@property (nonatomic, assign) AccountType defUser;
@property (nonatomic, strong) NSArray<UserPhoto *> *privatePhoto;
@property (nonatomic, assign) NSInteger experLevel;
@property (nonatomic, assign) NSInteger charmLevel;
@property (nonatomic, copy) NSString *phone;
@property (nonatomic, copy) NSString *carName;
@property (nonatomic, copy) NSString *carUrl;
@property (nonatomic, copy) NSString *time;
@property (nonatomic, copy) NSString *headwearName;
@property (nonatomic, copy) NSString *headwearUrl;
@property (nonatomic, assign) long createTime;
@property (nonatomic, assign) BOOL findNewUsers;
@property (nonatomic, assign) long liveness;

@property (nonatomic, copy) NSString *shareCode;

//检测是否绑定微信或者QQ
@property (nonatomic, assign) BOOL hasWx;
@property (nonatomic, assign) BOOL hasQq;


//首页关注-是否关注字段
@property (nonatomic, assign) BOOL isFan;

@end

