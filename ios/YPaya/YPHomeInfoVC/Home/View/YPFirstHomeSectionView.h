//
//  YPFirstHomeSectionView.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef void(^HJHomeSectionDetailBlock)(void);
typedef void(^SectionLeftBtnBlock)(void);
typedef void(^SectionMiddleBtnBlock)(void);
typedef void(^SectionRightBtnBlock)(void);
typedef void(^SectionSexBtnBlock)(void);
NS_ASSUME_NONNULL_BEGIN

@interface YPFirstHomeSectionView : UIView
@property(nonatomic,strong) UIImageView *logoImageView;
@property(nonatomic,strong) UILabel *tipLabel;
@property(nonatomic,strong) UIImageView *arrowImageView;
@property(nonatomic,strong) UIButton *detailBtn;
@property(nonatomic,strong) UIButton *leftBtn;
@property(nonatomic,strong) UIButton *middleBtn;
@property(nonatomic,strong) UIButton *rightBtn;

@property(nonatomic,copy) HJHomeSectionDetailBlock detailBlock;
@property(nonatomic,copy) SectionLeftBtnBlock leftBtnBlock;
@property(nonatomic,copy) SectionMiddleBtnBlock middleBtnBlock;
@property(nonatomic,copy) SectionRightBtnBlock rightBtnBlock;
@property(nonatomic,copy) SectionSexBtnBlock sexBtnBlock;

@property(nonatomic,strong) UIView *lineView;
@property(nonatomic,strong) UIView *lineView2;
@property(nonatomic,strong) UIView *lineView3;

@property(nonatomic,strong) UIImageView *sexImageView;
@property(nonatomic,strong) UIImageView *sexImageViewFlag;
@property(nonatomic,strong) UIButton *sexButton;
@end

NS_ASSUME_NONNULL_END
