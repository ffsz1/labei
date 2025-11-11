//
//  YPGiftAllMicroSendInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NSObject+YYModel.h"
#import "YPGiftReceiveInfo.h"


@interface YPGiftAllMicroSendInfo : YPGiftReceiveInfo
@property (nonatomic, copy) NSArray *targetUids;
@end
