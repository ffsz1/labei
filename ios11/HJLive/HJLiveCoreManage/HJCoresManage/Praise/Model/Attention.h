//
//  Attention.h
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BaseObject.h"
#import "UserInfo.h"
#import "ChatRoomInfo.h"
@interface Attention : BaseObject

//avatar = "http://nim.nos.netease.com/NDI3OTA4NQ==/bmltd18wXzE1MDUzNzQ4MzA0OTdfNTIwZDRkMjQtOTdjNC00NzNkLWJmMTMtMTMyNmUxMDBkNzdk";
//fansNum = 1;
//gender = 1;
//nick = "\U5c0f\U9762\U5305\U54e5\U54e5";
//operatorStatus = 2;
//title = "\U5c0f\U9762\U5305\U54e5\U54e5\U7684\U623f\U95f4";
//type = 2;
//uid = 90003762;
//valid = 0;

@property (nonatomic, assign) UserID uid;
@property (nonatomic, assign) BOOL valid;
@property (nonatomic, copy) NSString *avatar;
@property (copy, nonatomic) NSString *nick;
@property (assign, nonatomic) UserGender gender;
@property (copy, nonatomic) NSString *title;
@property (assign, nonatomic) NSInteger operatorStatus;
@property (assign, nonatomic) RoomType type;
@property (assign, nonatomic) NSInteger fansNum;
@property (nonatomic, strong) ChatRoomInfo *userInRoom;
@property (nonatomic, assign) BOOL selected;
@property (nonatomic, assign) BOOL invited;

@property (nonatomic, assign) NSInteger experLevel;
@property (nonatomic, assign) NSInteger charmLevel;

@property (nonatomic, copy) NSString *erbanNo;

@end
