//
//  NSString+JsonToDic.h
//  HJLive
//
//  Created by FF on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSString (JsonToDic)

+ (NSDictionary *)dictionaryWithJsonString:(NSString *)jsonString;

@end
