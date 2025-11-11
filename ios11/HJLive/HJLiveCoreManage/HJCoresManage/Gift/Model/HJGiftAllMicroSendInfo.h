//
//  HJGiftAllMicroSendInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NSObject+YYModel.h"
#import "GiftReceiveInfo.h"


@interface HJGiftAllMicroSendInfo : GiftReceiveInfo
@property (nonatomic, copy) NSArray *targetUids;
@end
