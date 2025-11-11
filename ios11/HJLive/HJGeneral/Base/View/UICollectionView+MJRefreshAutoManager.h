//
//  UICollectionView+MJRefreshAutoManager.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef NS_ENUM(NSInteger, MJFooterRefreshState) {
    MJFooterRefreshStateNormal,
    MJFooterRefreshStateLoadMore,
    MJFooterRefreshStateNoMore
};

@interface UICollectionView (MJRefreshAutoManager)
@property (nonatomic,assign)MJFooterRefreshState footRefreshState;
@end
