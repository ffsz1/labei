//
//  HJRoomNoticeView.m
//  HJLive
//
//  Created by feiyin on 2020/7/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRoomNoticeView.h"

@interface HJRoomNoticeView ()
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UITextView *textView;
@property (weak, nonatomic) IBOutlet UIView *bgView;

@end

@implementation HJRoomNoticeView

+ (void)show:(NSString *)title content:(NSString *)content
{
    HJRoomNoticeView *shareView = [[NSBundle mainBundle]loadNibNamed:@"HJRoomNoticeView" owner:self options:nil][0];
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
    [[UIApplication sharedApplication].keyWindow addSubview:shareView];
    
    shareView.titleLabel.text = title;
    shareView.textView.text = content;
    
    
    //左右晃动动画
    CAKeyframeAnimation * keyAnimaion = [CAKeyframeAnimation animation];
    keyAnimaion.keyPath = @"transform.rotation";
    keyAnimaion.values = @[@(-0.6 / 180.0 * M_PI),@(0.6 /180.0 * M_PI),@(-0.6/ 180.0 * M_PI),@(0 /180.0 * M_PI)];//度数转弧度
    keyAnimaion.removedOnCompletion = NO;
    keyAnimaion.fillMode = kCAFillModeForwards;
    keyAnimaion.duration = 0.3;
    keyAnimaion.repeatCount = 2;//动画次数
    [shareView.bgView.layer addAnimation:keyAnimaion forKey:nil];
    
}

- (void)close
{

    [self removeFromSuperview];
    
//    CGFloat t =4.0;
//    CGAffineTransform translateRight  =CGAffineTransformTranslate(CGAffineTransformIdentity, t,0.0);
//    CGAffineTransform translateLeft =CGAffineTransformTranslate(CGAffineTransformIdentity,-t,0.0);
//    self.bgView.transform = translateLeft;
//    [UIView animateWithDuration:0.07 delay:0.0 options:UIViewAnimationOptionAutoreverse | UIViewAnimationOptionRepeat animations:^{
//        [UIView setAnimationRepeatCount:2.0];
//        self.bgView.transform = translateRight;
//    } completion:^(BOOL finished){
//        if(finished){
//            [UIView animateWithDuration:0.05 delay:0.0 options:UIViewAnimationOptionBeginFromCurrentState animations:^{
//                self.bgView.transform =CGAffineTransformIdentity;
//            } completion:NULL];
//        }
//    }];
    
    

    
}



- (IBAction)closeBtnAction:(id)sender {
    
    [self close];
    
}



@end
