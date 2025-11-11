//
//  HJFirstHomeViewController.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "JXPagerView.h"
#import "JXCategoryView.h"
NS_ASSUME_NONNULL_BEGIN

@interface HJFirstHomeViewController : UIViewController<JXPagerViewListViewDelegate>
@property (nonatomic, copy) void(^scrollCallback)(UIScrollView *scrollView);
@property (nonatomic,assign) BOOL isDownLoad;
@end

NS_ASSUME_NONNULL_END
