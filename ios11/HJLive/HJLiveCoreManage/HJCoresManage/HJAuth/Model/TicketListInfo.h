//
//  TicketListInfo.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BaseObject.h"

@interface TicketListInfo : BaseObject<NSCopying,NSCopying>
@property(nonatomic, strong) NSString *issue_type;
@property(nonatomic, strong) NSArray* tickets;
@end
