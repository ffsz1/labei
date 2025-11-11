//
//  UserInfo.mm
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "UserInfo+WCTTableCoding.h"
#import "UserInfo.h"
#import <WCDB/WCDB.h>
#import "UserID.h"
#import "NSObject+YYModel.h"
#import "UserPhoto.h"

@implementation UserInfo

WCDB_IMPLEMENTATION(UserInfo)
WCDB_SYNTHESIZE(UserInfo, uid)
WCDB_SYNTHESIZE(UserInfo, erbanNo)
WCDB_SYNTHESIZE(UserInfo, nick)
WCDB_SYNTHESIZE(UserInfo, birth)
WCDB_SYNTHESIZE(UserInfo, star)
WCDB_SYNTHESIZE(UserInfo, signture)
WCDB_SYNTHESIZE(UserInfo, userVoice)
WCDB_SYNTHESIZE(UserInfo, voiceDura)
WCDB_SYNTHESIZE(UserInfo, gender)
WCDB_SYNTHESIZE(UserInfo, avatar)
WCDB_SYNTHESIZE(UserInfo, region)
WCDB_SYNTHESIZE(UserInfo, userDesc)
WCDB_SYNTHESIZE(UserInfo, followNum)
WCDB_SYNTHESIZE(UserInfo, fansNum)
WCDB_SYNTHESIZE(UserInfo, fortune)
WCDB_SYNTHESIZE(UserInfo, defUser)
WCDB_SYNTHESIZE(UserInfo, privatePhoto)
WCDB_SYNTHESIZE(UserInfo, experLevel)
WCDB_SYNTHESIZE(UserInfo, charmLevel)
WCDB_SYNTHESIZE(UserInfo, carName)
WCDB_SYNTHESIZE(UserInfo, carUrl)
WCDB_SYNTHESIZE(UserInfo, phone)
WCDB_SYNTHESIZE(UserInfo, time)
WCDB_SYNTHESIZE(UserInfo, headwearName)
WCDB_SYNTHESIZE(UserInfo, headwearUrl)
WCDB_SYNTHESIZE(UserInfo, createTime)

WCDB_PRIMARY(UserInfo, uid)
WCDB_INDEX(UserInfo, "_index", uid)

+ (nullable NSDictionary<NSString *, id> *)modelContainerPropertyGenericClass {
    return @{
             @"privatePhoto":UserPhoto.class
             };
}

@end
