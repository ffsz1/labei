//
//  YPChatBillInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YPChatBillInfo : NSObject

@property (strong, nonatomic) NSString *srcAvatar;
@property (strong, nonatomic) NSString *srcNick;
@property (strong, nonatomic) NSString *targetAvatar;
@property (strong, nonatomic) NSString *targetNick;
@property (strong, nonatomic) NSString *goldNum;
@property (strong, nonatomic) NSString *diamondNum;
@property (assign, nonatomic) NSInteger recordTime;
//@property (assign, nonatomic) NSInteger expendType;
@end
