//
//  YYPhotoListDataProvider.h
//  YYMobile
//  图片列表数据源
//  Created by James Pend on 15/3/18.
//  Copyright (c) 2015年 YY.inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YYPhotoListDataProvider : NSObject

/**
 *  要显示的图片下标
 */
@property (nonatomic,assign) NSUInteger imageIndex;

/**
 *  要显示的图片url数组
 */
@property (nonatomic, copy) NSArray *imageUrlList;


/**
 *  图片的数量
 */
@property (nonatomic, assign,readonly) NSInteger imageCount;

/**
 *  当前页
 */
@property (nonatomic, assign) NSInteger currentPage;

/**
 *  总量
 */
@property (assign, nonatomic) NSInteger totalNum;


@end
