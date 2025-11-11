//
//  PinyingUtils.h
//  SDKTestApp
//
//  Created by Huangqun on 12-12-12.
//  Copyright (c) 2012年 YY Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface PinyingUtils : NSObject
+(NSString *) getFirstPinyinSortingKeyOfHanziString :(NSString*) hanziString;
+(NSString *)getPinyinSortingKeyOfHanziString:(NSString*) hanziString;
@end
