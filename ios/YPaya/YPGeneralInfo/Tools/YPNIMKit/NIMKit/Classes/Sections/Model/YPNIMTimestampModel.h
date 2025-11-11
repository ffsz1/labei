//
//  YPNIMTimestampModel.h
//  YPNIMKit
//
//  Created by chris.
//  Copyright (c) 2015年 NetEase. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NIMCellConfig.h"

@interface YPNIMTimestampModel : NSObject

- (CGFloat)height;

/**
 *  时间戳
 */
@property (nonatomic, assign) NSTimeInterval messageTime;

@end
