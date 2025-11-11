//
//  HJPurseSwitchTitleView.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@class XCPurseSwitchModel;

@interface HJPurseSwitchTitleView : UIView

- (instancetype)initWithFrame:(CGRect)frame titleArr:(NSArray<NSString *> *)titleArr styleArr:(NSArray<XCPurseSwitchModel *> *)styleArr currentButton:(NSInteger)currentButton didClickButtonBlock:(void(^)(NSInteger index))didClickButtonBlock;

@property (nonatomic, assign) NSInteger currentButton;

@end

@interface XCPurseSwitchModel : NSObject

// 正常按钮文字颜色
@property (nonatomic, strong) UIColor *norTextColor;
// 选中按钮文字颜色
@property (nonatomic, strong) UIColor *selTextColor;
// 正常按钮背景颜色
@property (nonatomic, strong) UIColor *norTextBgColor;
// 选中按钮背景颜色
@property (nonatomic, strong) UIColor *selTextBgColor;
// 正常按钮文字大小
@property (nonatomic, strong) UIFont *norTextFont;
// 选中按钮文字大小
@property (nonatomic, strong) UIFont *selTextFont;

// 底部线宽度
@property (nonatomic, assign) CGFloat lineWidth;
// 底部线高度
@property (nonatomic, assign) CGFloat lineHeight;
// 按钮和线之间的偏移
@property (nonatomic, assign) CGFloat textLineOffsetY;
// 线条颜色
@property (nonatomic, strong) UIColor *lineColor;

// 按钮对齐方式
@property (nonatomic, assign) UIControlContentHorizontalAlignment textAlignment;

// 按钮宽度
@property (nonatomic, assign) CGFloat buttonWidth;


@end
