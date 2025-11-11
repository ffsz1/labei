//
//  BaseObject.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseObject.h"
#import <YYModel.h>

@implementation BaseObject

- (NSDictionary *)model2dictionary{
    id jsonData = [self yy_modelToJSONObject];
    if ([jsonData isKindOfClass:[NSDictionary class]]) {
        return (NSDictionary *)jsonData;
    }else{
        return nil;
    }
}

- (NSDictionary *)model2dictionaryForhj{
    id jsonData = [self yy_modelToJSONObject];
    if ([jsonData isKindOfClass:[NSDictionary class]]) {
        return (NSDictionary *)jsonData;
    }else{
        return nil;
    }
}
@end
