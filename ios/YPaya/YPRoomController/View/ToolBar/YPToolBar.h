//
//  YPToolBar.h
//  HJLive
//
//  Created by feiyin on 2020/7/15.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPToolBarButton.h"

@protocol XCToolBarDelegate

- (void)toolBar:(YPToolBarButton *)toolBar didSelectItem:(NSInteger)index;

@end

@interface YPToolBar : UIView

@property (nonatomic, strong) NSArray<YPToolBarButton *> * items;
@property (nonatomic, assign) CGFloat itemWidth;
@property (nonatomic, assign) CGFloat itemHeight;
@property (nonatomic, assign) NSInteger widthSpacing;
@property (nonatomic, weak) id<XCToolBarDelegate> delegate;
- (void)layoutTheViews;
@end
