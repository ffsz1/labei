//
//  YPNIMKitRobotTemplateLayout.m
//  YPNIMKit
//
//  Created by chris on 2017/6/25.
//  Copyright © 2017年 NetEase. All rights reserved.
//

#import "YPNIMKitRobotTemplateLayout.h"
#import "YPNIMKitResourceResizer.h"

@implementation YPNIMKitRobotTemplateLayout

- (instancetype)init
{
    self = [super init];
    if (self)
    {
        _items = [[NSMutableArray alloc] init];
    }
    return self;
}

@end


@implementation NIMKitRobotTemplateItem

- (NSString *)thumbUrl
{
    return [[YPNIMKitResourceResizer sharedResizer] imageThumbnailURL:self.url];
}


-(void)setValue:(id)value forUndefinedKey:(nonnull NSString *)key{
    if ([key isEqualToString:@"type"])
    {
        if ([value isEqualToString:@"block"])
        {
            self.itemType = NIMKitRobotTemplateItemTypeLinkBlock;
        }
        if ([value isEqualToString:@"url"])
        {
            self.itemType = NIMKitRobotTemplateItemTypeLinkURL;
        }
    }
}


- (nullable id)valueForUndefinedKey:(NSString *)key{
    return nil;
}


@end


@implementation NIMKitRobotTemplateLinkItem

- (instancetype)init
{
    self = [super init];
    if (self)
    {
        _items = [[NSMutableArray alloc] init];
    }
    return self;
}

@end
