//
//  UICollectionView+Refresh.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef enum : NSUInteger {
    RefreshTypeHeader,
    RefreshTypeFooter,
    RefreshTypeHeaderAndFooter
} RefreshType;

typedef void(^headerRefreshBlock)(int page);
typedef void(^footerRefreshBlock)(int page,BOOL isLastPage);

@interface UICollectionView (Refresh)

//当前是第几页
@property (nonatomic, assign)int currentPage;
//总共有多少页
@property (nonatomic, assign)int totalPage;
//是否还有更多数据
@property (nonatomic, assign)BOOL hasMore;

@property (nonatomic, copy)headerRefreshBlock headerRefreshHandle;
@property (nonatomic, copy)footerRefreshBlock footerRefreshHandle;

//是否正在刷新
@property (nonatomic, assign)BOOL isLoading;

//集成下拉刷新上拉加载功能
- (void)setupRefreshFunctionWith:(RefreshType)type;

//下拉刷新
- (void)pullDownRefresh:(headerRefreshBlock)block;

//上拉加载
- (void)pullUpRefresh:(footerRefreshBlock)block;

//数据请求回来后结束刷新的回调  totalPage 总页数
- (void)endRefreshStatus:(int)status totalPage:(int)totalPage;
- (void)endRefreshStatus:(int)status hasMoreData:(BOOL)hasMore;

- (void)endRefreshStatus:(int)status;
@end
