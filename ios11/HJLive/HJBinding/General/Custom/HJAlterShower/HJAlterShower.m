//
//  HJAlterShower.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJAlterShower.h"

@interface HJAlterShower ()
//文字提示样式
@property (weak, nonatomic) IBOutlet UIView *tipView;
@property (weak, nonatomic) IBOutlet UILabel *tipLabel;

//文字+确定
@property (weak, nonatomic) IBOutlet UIView *sureView;
@property (weak, nonatomic) IBOutlet UILabel *sureLabel;

//文字+确定+取消
@property (weak, nonatomic) IBOutlet UIView *alterView;
@property (weak, nonatomic) IBOutlet UILabel *alterLabel;

@property (nonatomic,copy) HJAlterShowerBlock sureBlock;
@property (nonatomic,copy) HJAlterShowerBlock cancelBlock;

@end

@implementation HJAlterShower

+ (void)showText:(NSString *)text sure:(HJAlterShowerBlock)sure cancel:(HJAlterShowerBlock)cancel
{
    HJAlterShower *shower = [[NSBundle mainBundle] loadNibNamed:@"HJAlterShower" owner:self options:nil][0];
    shower.frame = CGRectMake(0, 0, kScreenWidth, kScreenHeight);
    shower.sureBlock = sure;
    shower.cancelBlock = cancel;
    [[UIApplication sharedApplication].keyWindow addSubview:shower];
    
    if (sure && cancel) {
        shower.tipView.hidden = YES;
        shower.sureView.hidden = YES;
        shower.alterView.hidden = NO;
        shower.alterLabel.text = text;
        shower.alterView.layer.cornerRadius = 10;
        shower.alterView.layer.masksToBounds = YES;

    }else if (sure && !cancel){
        shower.tipView.hidden = YES;
        shower.sureView.hidden = NO;
        shower.alterView.hidden = YES;
        shower.sureLabel.text = text;
        shower.sureView.layer.cornerRadius = 10;
        shower.sureView.layer.masksToBounds = YES;
    }else{
        shower.tipView.hidden = NO;
        shower.sureView.hidden = YES;
        shower.alterView.hidden = YES;
        shower.tipLabel.text = text;
    }
    
    [shower show];
}

- (void)show
{
    self.hidden = NO;
    self.alpha = 0;
    [UIView animateWithDuration:0.3 animations:^{
        self.alpha = 1;
    } completion:^(BOOL finished) {
        
        if (self.sureBlock == nil && self.cancelBlock == nil){
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                [self dismiss];
            });
            
        }
        
    }];
}

- (void)dismiss
{    
    self.alpha = 1;
    [UIView animateWithDuration:0.3 animations:^{
        self.alpha = 0;
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
    }];
}

//确定
- (IBAction)sureAction:(id)sender {
    
    if (self.sureBlock) {
        self.sureBlock();
    }
    
    [self dismiss];
    
}

//取消
- (IBAction)cancelAction:(id)sender {
    
    if (self.cancelBlock) {
        self.cancelBlock();
    }
    
    [self dismiss];
    
}

@end
