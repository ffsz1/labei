//
//  HJToolBar.h
//  HJLive
//
//  Created by feiyin on 2020/7/15.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "HJToolBarButton.h"

@protocol XCToolBarDelegate

- (void)toolBar:(HJToolBarButton *)toolBar didSelectItem:(NSInteger)index;

@end

@interface HJToolBar : UIView

@property (nonatomic, strong) NSArray<HJToolBarButton *> * items;
@property (nonatomic, assign) CGFloat itemWidth;
@property (nonatomic, assign) CGFloat itemHeight;
@property (nonatomic, assign) NSInteger widthSpacing;
@property (nonatomic, weak) id<XCToolBarDelegate> delegate;
- (void)layoutTheViews;
@end
