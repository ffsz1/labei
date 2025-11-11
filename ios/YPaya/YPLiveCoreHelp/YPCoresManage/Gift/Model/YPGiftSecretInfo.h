//
//  YPGiftSecretInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YPGiftSecretInfo : NSObject

@property (nonatomic, assign) NSInteger uid;
@property (nonatomic, copy) NSString *nick;
@property (nonatomic, copy) NSString *avatar;
@property (nonatomic, assign) NSInteger sendUid;
@property (nonatomic, copy) NSString *sendNick;
@property (nonatomic, copy) NSString *sendAvatar;
@property (nonatomic, assign) NSInteger giftId;
@property (nonatomic, copy) NSString *giftName;
@property (nonatomic, copy) NSString *picUrl;
@property (nonatomic, copy) NSString *vggUrl;

@property (nonatomic, assign) NSInteger giftNum;
@end
