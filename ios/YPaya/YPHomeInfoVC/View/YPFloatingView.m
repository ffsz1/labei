//
//  YPFloatingView.m
//  HJLive
//
//  Created by feiyin on 2020/6/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPFloatingView.h"

#import "YPChatRoomInfo.h"
#import "YPRoomCoreV2Help.h"

#import "YPRoomViewControllerCenter.h"

#import "YPRoomPusher.h"



@implementation YPFloatingView

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    [self startPlayAnimation];
    self.logoImageView.layer.cornerRadius = self.logoImageView.width/2;
}

- (void)closeAction
{
    self.hidden = YES;
    
    YPChatRoomInfo *roomInfo = [GetCore(YPRoomCoreV2Help) getCurrentRoomInfo];
    if (roomInfo != nil) {
        [[YPRoomViewControllerCenter defaultCenter] dismissChannelViewWithQuitCurrentRoom:YES];
    }
    
    
}

- (IBAction)enterRoomAction:(id)sender {
    
    self.userInteractionEnabled = NO;
    YPChatRoomInfo *roomInfo = [GetCore(YPRoomCoreV2Help) getCurrentRoomInfo];
    if (roomInfo != nil) {
        [[YPRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomInfo:roomInfo];
        
//        [YPRoomPusher pushRoomByID:roomInfo.uid];
        
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            self.userInteractionEnabled = YES;
        });
    }
    else {
        self.userInteractionEnabled = YES;
    }
    
}

- (IBAction)closeBtnAction:(id)sender {
    
    [self closeAction];
    
}

- (void)startPlayAnimation {
    if ([self.logoImageView.layer.animationKeys containsObject:@"rotationZ"]) {
        [self.logoImageView startAnimating];
        return;
    }
    CABasicAnimation *animation =  [CABasicAnimation animationWithKeyPath:@"transform.rotation.z"];
    animation.fromValue = [NSNumber numberWithFloat:0.f];
    animation.toValue =  [NSNumber numberWithFloat: 2 * M_PI];
    animation.duration  = 10;
    animation.autoreverses = NO;
    animation.fillMode = kCAFillModeForwards;
    animation.removedOnCompletion = NO;
    animation.repeatCount = CGFLOAT_MAX;
    [self.logoImageView.layer addAnimation:animation forKey:@"rotationZ"];
}


-(void)touchesBegan:(NSSet*)touches withEvent:(UIEvent*)event {
    CGPoint pt = [[touches anyObject] locationInView:self];
    _startLocation = pt;
    [[self superview] bringSubviewToFront:self];
}

-(void)touchesMoved:(NSSet*)touches withEvent:(UIEvent*)event {
    CGPoint pt = [[touches anyObject] locationInView:self];
    float dx = pt.x - _startLocation.x;
    float dy = pt.y - _startLocation.y;
    CGPoint newcenter = CGPointMake(self.center.x + dx, self.center.y + dy);
    //
    float halfx = CGRectGetMidX(self.bounds);
    newcenter.x = MAX(halfx, newcenter.x);
    newcenter.x = MIN(self.superview.bounds.size.width - halfx-15, newcenter.x);
    //
    float halfy = CGRectGetMidY(self.bounds);
    newcenter.y = MAX(halfy, newcenter.y);
    newcenter.y = MIN(self.superview.bounds.size.height - halfy-44, newcenter.y);
    //
    
    CGFloat maxBottom = kScreenHeight - (iPhoneX ? (88 + 83) : (49 + 64)) - self.height;
    CGFloat bottom = self.superview.height - newcenter.y - 0.5 * self.height;
    if (maxBottom < bottom) {
        bottom = maxBottom;
    }
    newcenter.y = self.superview.height - bottom - 0.5 * self.height;
    
    self.center = newcenter;
    
    self.didMovePoint = CGPointMake(self.left, self.superview.height - self.bottom);
}

-(void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event {
    //    CGPoint point = self.center;
    //
    //    if (point.x>[self superview].width/2.0) {
    //        [UIView animateWithDuration:0.2 animations:^{
    //            self.left = [self superview].width- self.width - 17;
    //        }];
    //    }else{
    //        [UIView animateWithDuration:0.2 animations:^{
    //            self.left = 17;
    //        }];
    //    }
    //
    //
    
    self.didMovePoint = CGPointMake(self.left, self.superview.height - self.bottom);
}

-(void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event {
}

@end
