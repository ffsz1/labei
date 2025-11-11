//
//  YPSignnAlterView.h
//  HJLive
//
//  Created by apple on 2019/5/23.
//

#import <UIKit/UIKit.h>

#import "GGMaskView.h"
#import "YPHttpRequestHelper+Sign.h"

typedef void(^FinishBlock)(void);

NS_ASSUME_NONNULL_BEGIN

@interface YPSignnAlterView : GGMaskView
@property (weak, nonatomic) IBOutlet GGLabel *dayLabel;
@property (weak, nonatomic) IBOutlet UILabel *coinLabel;
@property (weak, nonatomic) IBOutlet GGButton *btn1;
@property (weak, nonatomic) IBOutlet GGButton *btn2;
@property (weak, nonatomic) IBOutlet GGButton *btn3;
@property (weak, nonatomic) IBOutlet GGButton *btn4;
@property (weak, nonatomic) IBOutlet GGButton *btn5;
@property (weak, nonatomic) IBOutlet GGButton *btn6;
@property (weak, nonatomic) IBOutlet UIButton *btn7;

@property (strong,nonatomic) YPMMHomeInfoModel *model;

+ (void)show;

+ (void)showFromHome:(FinishBlock)finishBlock;

@end

NS_ASSUME_NONNULL_END
