//
//  YPShareView.m
//  HJLive
//
//  Created by feiyin on 2020/6/30.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPShareView.h"

#import "YPverticalButton.h"

#import "YPJXShareHelper.h"

@interface YPShareView ()
@property (weak, nonatomic) IBOutlet UIView *shareView;

@property (weak, nonatomic) IBOutlet YPverticalButton *wxBtn;
@property (weak, nonatomic) IBOutlet YPverticalButton *wxCircleBtn;

@property (weak, nonatomic) IBOutlet YPverticalButton *qqBtn;
@property (weak, nonatomic) IBOutlet YPverticalButton *qqZoneBtn;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *left_qqBtn;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_shareView;

@property (strong,nonatomic) YPShareInfo *shareInfo;


@end

@implementation YPShareView

+ (void)show:(YPShareInfo *)shareInfo
{
    YPShareView *shareView = [[NSBundle mainBundle]loadNibNamed:@"YPShareView" owner:self options:nil][0];
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
    [[UIApplication sharedApplication].keyWindow addSubview:shareView];
    
    shareView.shareInfo = shareInfo;
    
    BOOL hasWx = [WXApi isWXAppInstalled];
    BOOL hasQQ = ([TencentOAuth iphoneQQInstalled] || [TencentOAuth iphoneTIMInstalled]);
    
    if (!hasWx) {
        shareView.wxBtn.hidden = YES;
        shareView.wxCircleBtn.hidden = YES;
        shareView.left_qqBtn.constant = -kScreenWidth/2;
    }
    
    if (!hasQQ) {
        shareView.qqBtn.hidden = YES;
        shareView.qqZoneBtn.hidden = YES;
    }
    
    
    shareView.bottom_shareView.constant = -185;
    
    [shareView layoutIfNeeded];
    
    [UIView animateWithDuration:0.5 delay:0 usingSpringWithDamping:0.7 initialSpringVelocity:0 options:UIViewAnimationOptionCurveEaseIn animations:^{
        
        
        
        shareView.bottom_shareView.constant = 0;
        
        [shareView layoutIfNeeded];
        
    } completion:^(BOOL finished) {
        
    }];
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    [self setBgRaduis];
    
}

- (void)setBgRaduis
{
    CGRect frame = CGRectMake(0, 0, kScreenWidth, 185);
    UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:frame byRoundingCorners:UIRectCornerTopRight | UIRectCornerTopLeft cornerRadii:CGSizeMake(15, 15)];
    
    CAShapeLayer * maskLayer = [[CAShapeLayer alloc]init];
    maskLayer.frame = frame;
    maskLayer.path = maskPath.CGPath;
    self.shareView.layer.mask = maskLayer;
}

- (void)close
{
    [self removeFromSuperview];
}

- (IBAction)wxBtnAction:(id)sender {
    [self shareWithType:JXShareTypeWXFriend];
    [self close];
}

- (IBAction)wxCircleBtnAction:(id)sender {
    [self shareWithType:JXShareTypeWXCircle];
    [self close];
}

- (IBAction)qqAction:(id)sender {
    [self shareWithType:JXShareTypeQQFriend];
    [self close];
}

- (IBAction)qqZoneAction:(id)sender {
    [self shareWithType:JXShareTypeQQZone];
    [self close];
}

- (void)shareWithType:(JXShareType)type
{
    UserID uid = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
    
    if (self.shareInfo.type == HJShareTypeRoom) {
        [YPJXShareHelper shareRoom:uid roomUid:self.shareInfo.roomOwnerUID platform:type];
    }else {
        NSString *url = [NSString stringWithFormat:@"%@?uid=%lld",self.shareInfo.showUrl,uid];
        [YPJXShareHelper shareH5WithTitle:self.shareInfo.title url:url imgUrl:self.shareInfo.imgUrl desc:self.shareInfo.desc platform:type];
    }
}

- (IBAction)cancelAction:(id)sender {
    [self close];
}

- (IBAction)tapAction:(id)sender {
    [self close];
}


@end
