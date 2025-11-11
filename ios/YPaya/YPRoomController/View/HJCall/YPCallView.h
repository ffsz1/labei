//
//  YPCallView.h
//  HJLive
//
//  Created by feiyin on 2020/7/13.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
#import "YPIMQueueItem.h"
#import "YPDaCallIntoModel.h"
#import <UIKit/UIKit.h>
typedef NS_ENUM(NSUInteger, HJCallType) {
    HJCallTypeFirst,//20
    HJCallTypeSecond,//200
    HJCallTypeThird,//1000
    HJCallTypeDaCall,
    HJCallTypePickupConch,
};

typedef void(^TapCallTypeBlock)(HJCallType type,YPDaCallIntoModel* _Nullable model);
NS_ASSUME_NONNULL_BEGIN

@interface YPCallView : UIView
@property (nonatomic,strong) YPDaCallIntoModel* intoModel;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *mangerViewHeight;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_item_layout;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_lowest_layout;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *manage_height_layout;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *top_hailuoBtn_layout;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *top_remainLab_layout;

@property (weak, nonatomic) IBOutlet UIView *managerView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_manager_layout;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_view_layout;

@property (weak, nonatomic) IBOutlet UIButton *ownBtn;

@property (weak, nonatomic) IBOutlet UIButton *mai1btn;
@property (weak, nonatomic) IBOutlet UIButton *mai2Btn;

@property (weak, nonatomic) IBOutlet UIButton *mai3Btn;

@property (weak, nonatomic) IBOutlet UIButton *mai4Btn;
@property (weak, nonatomic) IBOutlet UIButton *mai5Btn;
@property (weak, nonatomic) IBOutlet UIButton *mai6Btn;
@property (weak, nonatomic) IBOutlet UIButton *mai7Btn;
@property (weak, nonatomic) IBOutlet UIButton *mai8Btn;

@property (weak, nonatomic) IBOutlet UIButton *mufaBtn;

@property (weak, nonatomic) IBOutlet UIButton *xiaochuanBtn;
@property (weak, nonatomic) IBOutlet UIButton *youtingBtn;
@property (weak, nonatomic) IBOutlet UIButton *dacallBtn;

@property (weak, nonatomic) IBOutlet UIButton *chongzhiBtn;

@property (weak, nonatomic) IBOutlet UIButton *guizeBtn;

@property (nonatomic,strong) NSMutableArray *micArr;
@property (nonatomic,strong) NSMutableArray *giftList;

@property (nonatomic,strong) YPIMQueueItem *queueItem;
@property (nonatomic,strong) YPIMQueueItem *queueItem2;
@property (nonatomic,strong) YPIMQueueItem *queueItem3;
@property (nonatomic,strong) YPIMQueueItem *queueItem4;
@property (nonatomic,strong) YPIMQueueItem *queueItem5;
@property (nonatomic,strong) YPIMQueueItem *queueItem6;
@property (nonatomic,strong) YPIMQueueItem *queueItem7;
@property (nonatomic,strong) YPIMQueueItem *queueItem8;

@property (weak, nonatomic) IBOutlet UIImageView *ownAvetarView;


@property (weak, nonatomic) IBOutlet UIImageView *ownAvatarImgView;


@property (weak, nonatomic) IBOutlet UIImageView *mai1Img;

@property (weak, nonatomic) IBOutlet UIImageView *mai2Img;
@property (weak, nonatomic) IBOutlet UIImageView *mai3Img;
@property (weak, nonatomic) IBOutlet UIImageView *mai4Img;
@property (weak, nonatomic) IBOutlet UIImageView *mai5Img;
@property (weak, nonatomic) IBOutlet UIImageView *mai6Img;
@property (weak, nonatomic) IBOutlet UIImageView *mai7Img;
@property (weak, nonatomic) IBOutlet UIImageView *mai8Img;


@property (weak, nonatomic) IBOutlet UIImageView *smallValueImageView;

@property (weak, nonatomic) IBOutlet UILabel *smallValueNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *smallValueLabel;


@property (weak, nonatomic) IBOutlet UIImageView *middelValueImageView;

@property (weak, nonatomic) IBOutlet UILabel *middelValueNameLabel;

@property (weak, nonatomic) IBOutlet UILabel *middelValueLabel;

@property (weak, nonatomic) IBOutlet UIImageView *bigValueImageView;

@property (weak, nonatomic) IBOutlet UILabel *bigValueNameLabel;

@property (weak, nonatomic) IBOutlet UILabel *bigValueLabel;


@property (weak, nonatomic) IBOutlet UIButton *jianhailuoBtn;//捡海螺Btn

@property (weak, nonatomic) IBOutlet UILabel *numRemainLabel;//打call剩余次数


@property (weak, nonatomic) IBOutlet UILabel *jinbiNumLabel;//金币数量




@property (copy, nonatomic) TapCallTypeBlock tapCallTypeBlock;

@property (strong, nonatomic)NSMutableSet* btnSet;
+ (void)showCall:(TapCallTypeBlock)tapCallTypeBlock list:(NSMutableArray*)list;

@end

NS_ASSUME_NONNULL_END
