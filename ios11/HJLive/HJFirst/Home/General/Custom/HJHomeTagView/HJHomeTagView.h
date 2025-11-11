//
//  HJHomeTagView.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HJFirstHomeTagStytleModel.h"


typedef void(^SelItemCallBack)(NSInteger);
NS_ASSUME_NONNULL_BEGIN

@interface HJHomeTagView : UIView<UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout>

@property (nonatomic,strong) UICollectionView *collectView;
@property (nonatomic, strong) NSMutableArray *roomTagList;

@property (nonatomic,assign) NSInteger sel;

@property (nonatomic,copy) SelItemCallBack selItemCallBack;

@property (assign,nonatomic) BOOL isHomeSectionStyle;


@property (nonatomic,strong) HJFirstHomeTagStytleModel *stytle;

//设置选中位置
- (void)setScrollTag:(NSInteger)item;


@end

NS_ASSUME_NONNULL_END
