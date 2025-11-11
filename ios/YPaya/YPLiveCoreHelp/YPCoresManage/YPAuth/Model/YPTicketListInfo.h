//
//  YPTicketListInfo.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPBaseObject.h"

@interface YPTicketListInfo : YPBaseObject<NSCopying,NSCopying>
@property(nonatomic, strong) NSString *issue_type;
@property(nonatomic, strong) NSArray* tickets;
@end
