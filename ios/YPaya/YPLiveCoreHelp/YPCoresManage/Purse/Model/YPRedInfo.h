//
//  YPRedInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface YPRedInfo : NSObject
@property (copy, nonatomic) NSString *packetName;
@property (assign, nonatomic) UserID uid;
@property (assign, nonatomic) double packetNum;
@property (assign, nonatomic) BOOL needAlert;
@property (copy, nonatomic) NSString *createTime;
@property (assign, nonatomic) NSInteger type;

@end
