//
//  YPNIMInputEmoticonTabView.h
//  YPNIMKit
//
//  Created by chris.
//  Copyright (c) 2015年 NetEase. All rights reserved.
//

#import <UIKit/UIKit.h>
@class YPNIMInputEmoticonTabView;

@protocol NIMInputEmoticonTabDelegate <NSObject>

- (void)tabView:(YPNIMInputEmoticonTabView *)tabView didSelectTabIndex:(NSInteger) index;

@end

@interface YPNIMInputEmoticonTabView : UIControl

@property (nonatomic,strong) UIButton * sendButton;

@property (nonatomic,weak)   id<NIMInputEmoticonTabDelegate>  delegate;

- (void)selectTabIndex:(NSInteger)index;

- (void)loadCatalogs:(NSArray*)emoticonCatalogs;

@end






