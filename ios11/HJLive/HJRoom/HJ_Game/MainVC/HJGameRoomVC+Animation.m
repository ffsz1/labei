//
//  HJGameRoomVC+Animation.m
//  HJLive
//
//  Created by feiyin on 2020/7/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGameRoomVC+Animation.h"
#import "HJRippleAnimationView.h"
#import <UIImageView+WebCache.h>

@interface HJGameRoomVC ()<HJRippleAnimationDelagte>

@end

@implementation HJGameRoomVC (Animation)


//更新房主说话状态动画
- (void)updateMainPositionMicroAnimation {
    if (![self.mainPositionSpeakingAnimationImageView isAnimating]) {
        NSMutableArray  *arrayM=[NSMutableArray array];
//        for (int i=0; i<11; i++) {
//            [arrayM addObject:[UIImage imageNamed:[NSString stringWithFormat:@"room_speaking_logo%d",i]]];
//        }
        
        for (int i=0; i<30; i++) {
            [arrayM addObject:[UIImage imageNamed:[NSString stringWithFormat:@"xc_room_speak_1_0000%d",i]]];
        }
        [self.mainPositionSpeakingAnimationImageView setAnimationImages:arrayM];
        [self.mainPositionSpeakingAnimationImageView setAnimationRepeatCount:1];
        [self.mainPositionSpeakingAnimationImageView setAnimationDuration:1.0f];
        [self.mainPositionSpeakingAnimationImageView startAnimating];
        
//        UserInfo *info = GetCore(HJImRoomCoreV2).roomOwnerInfo;
//        JXIMUserGenderType roomOwnerGender = JXIMUserGenderTypeMale;
//        if(info.gender == UserInfo_Female){
//            roomOwnerGender = JXIMUserGenderTypeFemale;
//        }
//
//        HJRippleAnimationView *animationView = [[HJRippleAnimationView alloc]initWithFrame:CGRectMake(0, 0, self.mainPositionSpeakingAnimationImageView.width, self.mainPositionSpeakingAnimationImageView.height) gender:roomOwnerGender];
//
//        animationView.delegate = self;
//        [self.mainPositionSpeakingAnimationImageView addSubview:animationView];
        
    }
    
}


- (void)addTheGiftAnimationWith:(CGPoint)orginPoint destinationPoint:(CGPoint)destinationPoint withGiftPic:(NSURL *)giftPic{
    __block UIImageView *giftImageView = [[UIImageView alloc]initWithFrame:CGRectMake(orginPoint.x - 27.5, orginPoint.y - 23.5, 55, 47)];

    [giftImageView sd_setImageWithURL:giftPic];
    giftImageView.alpha = 1;
    [self.navigationController.view addSubview:giftImageView];
    [self.view bringSubviewToFront:giftImageView];
    [UIView animateWithDuration:0.8 delay:0.5 options:UIViewAnimationOptionLayoutSubviews animations:^{
        giftImageView.frame = CGRectMake(self.view.center.x - 82.5, self.view.center.y - 70.5, 165, 141);
        
    } completion:^(BOOL finished) {

        [UIView animateWithDuration:0.8 delay:1 options:UIViewAnimationOptionLayoutSubviews animations:^{
            giftImageView.frame = CGRectMake(destinationPoint.x - 27.5, destinationPoint.y - 30.5, 55, 47);
        } completion:^(BOOL finished) {
            [giftImageView removeFromSuperview];
        }];
    }];
    
    
}

//- (void)rippleAnimationFinishedWithAnimationView:(UIView *)animationView{
//    [self.mainPositionSpeakingAnimationImageView removeAllSubviews];
//    [animationView removeFromSuperview];
//}

@end
