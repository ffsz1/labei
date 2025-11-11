//
//  OCExampleViewController.h
//  JXPagingView
//
//  Created by jiaxin on 2018/8/27.
//  Copyright © 2018年 jiaxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HJHomeRoomCell.h"
#import "HJHomeCommonTopCell.h"
#import "HJHomePageInfo.h"
#import "HJHomeHeaderView.h"
#import "HJBannerInfo.h"
#import "HJHttpRequestHelper+Home.h"
#import "UITableView+MJRefresh.h"
#import "UIView+XCToast.h"
#import "HJWKWebViewController.h"
#import "HJEmptyView.h"
#import "HJHttpRequestHelper+Alert.h"
#import "HJHomeCoreClient.h"
#import "HJAuthCoreClient.h"
#import "HJImLoginCoreClient.h"
#import "HJIReachability.h"
#import "HJReachabilityCore.h"
#import "HJRoomCoreClient.h"
#import "HJVersionCoreHelp.h"
#import "HJUserCoreClient.h"


/**
 该库的JXPagerView不能保证为最新版本，强烈建议阅读JXPagingView库：https://github.com/pujiaxin33/JXPagingView，里面有更丰富的效果支持！！！
 */
@interface PagingViewController : UIViewController
@property (nonatomic,strong) UIView *topView;
@property (nonatomic,strong) UIButton *searchBtn;
@property (nonatomic,strong) UIButton *rankBtn;
@property (nonatomic, strong) SDCycleScrollView *banaCommonView;//banaer
@property (nonatomic,strong) HJEmptyView *tipView;
@property (nonatomic, strong) NSArray *bannerInfos;//广告图
@property (nonatomic,assign) int hotPage;
@property (nonatomic, strong) NSMutableArray *tagsListArr;//顶部标签栏数组
@property (nonatomic, strong) UIView *leftView;
@property (nonatomic, strong) UIView *middelView;
@property (nonatomic, strong) UIView *rightView;
@property (nonatomic, strong) UIView *rightView2;
@end
