//
//  YPYYPhotoListDataProvider.m
//  YYMobile
//
//  Created by James Pend on 15/3/18.
//  Copyright (c) 2015年 YY.inc. All rights reserved.
//

#import "YPYYPhotoListDataProvider.h"

@interface YPYYPhotoListDataProvider()

@end

@implementation YPYYPhotoListDataProvider

- (instancetype)init
{
    self = [super init];
    if (self) {
        self.imageUrlList = [[NSArray alloc] init];
        self.imageIndex = 0;
        self.currentPage = 1;
    }
    return self;
}

- (NSInteger)currentPage
{
    return self.imageIndex+1;
}

- (NSInteger)imageCount
{
    return self.imageUrlList.count;
}

@end
