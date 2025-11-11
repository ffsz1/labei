//
//  BaseObject.h
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface BaseObject : NSObject
- (NSDictionary *)model2dictionary;
- (NSDictionary *)model2dictionaryForhj;//For hj
@end

NS_ASSUME_NONNULL_END
