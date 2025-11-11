//
//  Tikect.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BaseObject.h"

@interface HJTicketInfo : BaseObject<NSCopying,NSCopying>
@property(nonatomic, strong)NSString *ticket;
@property(nonatomic, strong)NSNumber *expires_in;
@end
