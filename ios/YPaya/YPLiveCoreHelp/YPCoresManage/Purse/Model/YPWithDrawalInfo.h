//
//  WithDrawalList.h
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPBaseObject.h"

@interface YPWithDrawalInfo : YPBaseObject

@property (copy, nonatomic)NSString *cashProdId;
@property (copy, nonatomic)NSString *cashProdName;
@property (copy, nonatomic)NSString *diamondNum;
@property (copy, nonatomic)NSString *cashNum;
@property (copy, nonatomic)NSString *seqNo;

@end
