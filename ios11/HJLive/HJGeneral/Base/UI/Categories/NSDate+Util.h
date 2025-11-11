//
//  NSDate+Util.h
//  HJLive
//
//  Created by feiyin on 2020/4/20.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSDate (Util)
+(NSInteger)getAge:(long )time;
+(NSInteger) getYear:(long )time;
+ (NSInteger) getMonth:(long )time;
+ (NSInteger) getDay:(long) time;
+(NSString *)calculateConstellationWithMonth:(long)time;
@end
