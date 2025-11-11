//
//  YPRoomlotteryView.h
//  HJLive
//
//  Created by feiyin on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface YPRoomlotteryView : UIView
@property (nonatomic, copy) void(^closeRoomLotteryView)(void);
//@property (weak, nonatomic) IBOutlet NSLayoutConstraint *tenWid;
//@property (weak, nonatomic) IBOutlet NSLayoutConstraint *tenHeight;
@property (nonatomic, copy) void(^helpClickBlock)(void);
@property (nonatomic, copy) void(^rankClickBlock)(void);
@property (nonatomic, copy) void(^recordeClickBlock)(void);
@property (weak, nonatomic) IBOutlet UIView *roomLotteryOtherView;
@property (weak, nonatomic) IBOutlet UIButton *closeBtn;
@property (weak, nonatomic) IBOutlet UIImageView *boxImg;
@property (weak, nonatomic) IBOutlet UIView *tenView;

@property (weak, nonatomic) IBOutlet UIImageView *oneImg;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *oneHeightCons;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *oneWidthCons;
@property (weak, nonatomic) IBOutlet UIButton *tenSureBtn;
@property (weak, nonatomic) IBOutlet UIButton *oneoneBtn;
@property (weak, nonatomic) IBOutlet UIButton *tentenBtn;



@property (weak, nonatomic) IBOutlet UIImageView *tenImg1;
@property (weak, nonatomic) IBOutlet UILabel *tenText1;

@property (weak, nonatomic) IBOutlet UIImageView *tenImg2;
@property (weak, nonatomic) IBOutlet UILabel *tenText2;

@property (weak, nonatomic) IBOutlet UIImageView *tenImg3;
@property (weak, nonatomic) IBOutlet UILabel *tenText3;

@property (weak, nonatomic) IBOutlet UIImageView *tenImg4;
@property (weak, nonatomic) IBOutlet UILabel *tenText4;

@property (weak, nonatomic) IBOutlet UIImageView *tenImg5;
@property (weak, nonatomic) IBOutlet UILabel *tenText5;

@property (weak, nonatomic) IBOutlet UIImageView *tenImg6;
@property (weak, nonatomic) IBOutlet UILabel *tenText6;


@property (weak, nonatomic) IBOutlet UIView *v1;
@property (weak, nonatomic) IBOutlet UIView *v2;
@property (weak, nonatomic) IBOutlet UIView *v3;
@property (weak, nonatomic) IBOutlet UIView *v4;
@property (weak, nonatomic) IBOutlet UIView *v5;
@property (weak, nonatomic) IBOutlet UIView *v6;

@property (weak, nonatomic) IBOutlet UIButton *moneyBtn;

@property (weak, nonatomic) IBOutlet UILabel *roomlotteryLabel;
@property (weak, nonatomic) IBOutlet UILabel *showTip;
@property (weak, nonatomic) IBOutlet UIButton *setAnimationBtn;
@property (weak, nonatomic) IBOutlet UIButton *fiftyBtn;


@property (weak, nonatomic) IBOutlet UIButton *pickTenGiftBtn;


@property (weak, nonatomic) IBOutlet UIButton *pickOneGiftBtn;

@property (weak, nonatomic) IBOutlet UILabel *pickGiftNumLabel;

@property (weak, nonatomic) IBOutlet UILabel *pickConchNumLabel;

@property (weak, nonatomic) IBOutlet UIView *managerView;

@property (weak, nonatomic) IBOutlet UIButton *guizeBtn;

@property (weak, nonatomic) IBOutlet UIButton *paihangbangBtn;
@property (weak, nonatomic) IBOutlet UIButton *liwuBtn;

@property (weak, nonatomic) IBOutlet UIButton *jiluBtn;


@property (weak, nonatomic) IBOutlet UIView *guizeView;

@property (weak, nonatomic) IBOutlet UILabel *renqipiaoNumLabel;


@end
