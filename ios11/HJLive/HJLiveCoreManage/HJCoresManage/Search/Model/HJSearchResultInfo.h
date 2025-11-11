//
//  SearchReusltInfo.h
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseObject.h"
#import "UserInfo.h"
#import "ChatRoomInfo.h"

@interface HJSearchResultInfo : BaseObject

//avatar = "http://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83erONCy5fvOX0D9K5qblvUhxpp82vlicWZN9OmDkh7EBJb8gExueRWCCUDHTibP0vd0YK5U88ib8Y11jQ/0";
//erbanNo = 172558;
//fansNum = 1;
//gender = 1;
//nick = "\U7b80\U5355";
//uid = 90009499;

//uid    用户uid    是    [string]
//2    roomId    房间ID    是    [string]
//3    title    房间名称    是    [string]
//4    type    房间类型    是    [string]
//5    valid    房间是否在线    是    [string]
//6    avatar    用户头像    是    [string]
//7    nick    用户昵称    是    [string]
//8    erbanNo    耳伴号    是    [string]
//9    fansNum    用户粉丝数    是    [string]
//10    gender

@property (copy, nonatomic) NSString *avatar;
@property (copy, nonatomic) NSString *erbanNo;
@property (copy, nonatomic) NSString *fansNum;
@property (assign, nonatomic) UserGender gender;
@property (copy, nonatomic) NSString *nick;
@property (assign, nonatomic) UserID uid;
@property (copy, nonatomic) NSString *title;
@property (assign, nonatomic) RoomType type;
@property (assign, nonatomic) BOOL valid;
@property (assign, nonatomic) NSInteger roomId;
@property (copy, nonatomic) NSString *tagPict;


@end
