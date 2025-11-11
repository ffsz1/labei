//
//  HJHomeCommonViewController.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "JXPagerView.h"
#import "JXCategoryView.h"
NS_ASSUME_NONNULL_BEGIN

@interface HJHomeCommonViewController : UIViewController<JXPagerViewListViewDelegate>
@property (nonatomic, strong) NSArray *bannerInfos;//广告图
@property (nonatomic, assign) int type;
@property (nonatomic, copy) void(^scrollCallback)(UIScrollView *scrollView);
@end

NS_ASSUME_NONNULL_END
