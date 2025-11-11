//
//  UserInfo+WCTTableCoding.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "UserInfo.h"
#import <WCDB/WCDB.h>

@interface UserInfo () <WCTTableCoding>

WCDB_PROPERTY(uid)
WCDB_PROPERTY(erbanNo)
WCDB_PROPERTY(nick)
WCDB_PROPERTY(birth)
WCDB_PROPERTY(star)
WCDB_PROPERTY(signture)
WCDB_PROPERTY(userVoice)
WCDB_PROPERTY(voiceDura)
WCDB_PROPERTY(gender)
WCDB_PROPERTY(avatar)
WCDB_PROPERTY(region)
WCDB_PROPERTY(userDesc)
WCDB_PROPERTY(followNum)
WCDB_PROPERTY(fansNum)
WCDB_PROPERTY(fortune)
WCDB_PROPERTY(defUser)
WCDB_PROPERTY(privatePhoto)
WCDB_PROPERTY(experLevel)
WCDB_PROPERTY(charmLevel)
WCDB_PROPERTY(carName)
WCDB_PROPERTY(carUrl)
WCDB_PROPERTY(phone)
WCDB_PROPERTY(time)

@end
