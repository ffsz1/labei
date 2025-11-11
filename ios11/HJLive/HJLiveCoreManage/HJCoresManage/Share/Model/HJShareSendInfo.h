//
//  ShareSendInfo.h
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface HJShareSendInfo : NSObject

@property (assign, nonatomic) UserID uid;
@property (copy, nonatomic) NSString *nick;
@property (assign, nonatomic) UserID targetUid;
@property (copy, nonatomic) NSString *targetNick;
@property(nonatomic, strong)NSDictionary *encodeAttachemt;
@end
