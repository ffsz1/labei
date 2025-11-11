//
//  YPRoomPKJoinModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPRoomPKJoinModel : NSObject

@property (nonatomic, assign)NSInteger recordId;
@property (nonatomic, assign)NSString *uid;
@property (nonatomic, copy)NSString *avatar;
@property (nonatomic, copy)NSString *erBanNo;
@property (nonatomic, assign)NSInteger giftId;
@property (nonatomic, assign)NSInteger giftNum;
@property (nonatomic, copy)NSString *giftUrl;
@property (nonatomic, copy)NSString *opponentAvatar;
@property (nonatomic, copy)NSString *opponentErBanNo;
@property (nonatomic, assign)NSString *opponentUid;
@property (nonatomic, copy)NSString *nick;
@property (nonatomic, copy)NSString *opponentNick;


//{
//    avatar = "https://pic.hnyueqiang.com/FqFNxTnXkeLgmvAr7OkHGpffKJwX?imageslim";
//    erBanNo = 6519579;
//    giftId = 142;
//    giftName = "\U73ab\U7470";
//    giftNum = 23;
//    giftPic = "https://pic.hnyueqiang.com/FhUHn680ukrIgujA_dQD2mfzX_lt?imageslim";
//    opponentAvatar = "https://pic.hnyueqiang.com/FkuqEizmOR6bcP3RbPykuugVLWk3?imageslim";
//    opponentErBanNo = 1010786;
//    opponentUid = 10182;
//    uid = 10224;
//}

@end

NS_ASSUME_NONNULL_END
