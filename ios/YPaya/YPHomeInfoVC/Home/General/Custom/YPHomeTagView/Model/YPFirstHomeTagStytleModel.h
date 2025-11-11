//
//  YPFirstHomeTagStytleModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPFirstHomeTagStytleModel : NSObject
@property (nonatomic,strong) UIColor *selColor;
@property (nonatomic,strong) UIColor *normalColor;

@property (nonatomic,strong) UIFont *selFont;
@property (nonatomic,strong) UIFont *normalFont;

@property (nonatomic,assign) CGFloat lineWidth;
@property (nonatomic,assign) CGFloat lineHeight;
@property (nonatomic,strong) UIColor *lineColor;
@property (nonatomic,assign) BOOL isPictureTitleColor;

//0上居中 1中居中 2低居中
@property (nonatomic,assign) NSInteger verticalAlignment;
@end

NS_ASSUME_NONNULL_END
