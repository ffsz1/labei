//
//  HJBaseCollectionVC.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJBaseViewController.h"
#import "UICollectionView+Refresh.h"
NS_ASSUME_NONNULL_BEGIN


@interface HJBaseCollectionVC : HJBaseViewController<UICollectionViewDelegate,UICollectionViewDataSource>
@property (nonatomic, strong) UICollectionView *collectionView;

-(UICollectionView *)collectionView;
/***集成头部刷新和底部刷新
 @param collectionView 要集成刷新的对象 collectionView
 ***/
- (void)setupRefreshTarget:(UICollectionView *)collectionView;

/**
 根据集成类型集成刷新
 @param type 集成的类型
 @param collectionView 要集成刷新的对象 collectionView
 RefreshTypeHeader 只有头
 RefreshTypeFooter 只有底部
 RefreshTypeHeaderAndFooter 有头也有底部
 */
- (void)setupRefreshTarget:(UICollectionView *)collectionView with:(RefreshType)type;
/**
 下拉刷新的回调 子类需要重写
 @param page 当前请求的页数
 */
- (void)pullDownRefresh:(int)page;

/**
 上拉刷新的回掉  子类需要重写
 @param page   当前请求的页数
 @param isLastPage yes:已经到了最后一页 NO：还没到
 */
- (void)pullUpRefresh:(int)page lastPage:(BOOL)isLastPage;

//刷新成功的回调方法中调用这个方法 status:0结束头部刷新,1 结束底部刷新;  totalPage: 总页数
- (void)successEndRefreshStatus:(int)status totalPage:(int)totalPage;

- (void)successEndRefreshStatus:(int)status hasMoreData:(BOOL)hasMore;

//刷新失败的回调方法中调用这个方法 status:0结束头部刷新 1 结束底部刷新
- (void)failEndRefreshStatus:(int)status;

@end
NS_ASSUME_NONNULL_END
