//
//  HJPickupConchView.h
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef NS_ENUM(NSUInteger, HJPickConchType) {
    HJPickConchTypeFirst,//20
    HJPickConchTypeSecond,//200
    HJPickConchTypeThird//1000
};
typedef void(^TapConchTypeBlock)(HJPickConchType);
NS_ASSUME_NONNULL_BEGIN

@interface HJPickupConchView : UIView

@property (weak, nonatomic) IBOutlet UIView *conchView;

@property (weak, nonatomic) IBOutlet UIButton *rankingBtn;

@property (weak, nonatomic) IBOutlet UIButton *giftBtn;

@property (weak, nonatomic) IBOutlet UIButton *recordBtn;

@property (weak, nonatomic) IBOutlet UIButton *rulesBtn;

@property (weak, nonatomic) IBOutlet UILabel *numLabel;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_view_layout;

+ (void)showCall:(TapConchTypeBlock)tapConchTypeBlock;
@end

NS_ASSUME_NONNULL_END
