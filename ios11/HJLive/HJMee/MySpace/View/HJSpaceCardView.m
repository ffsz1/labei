//
//  HJSpaceCardView.m
//  HJLive
//
//  Created by feiyin on 2020/5/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJSpaceCardView.h"

#import "HJMySpaceVC.h"
#import "HJSessionViewController.h"

#import "HJverticalButton.h"

#import "HJHttpRequestHelper+User.h"
#import "UIView+getTopVC.h"
#import "HJUserHandler.h"

#import "HJReportItemView.h"
#import "NSString+GGImage.h"

#import "HJWKWebViewController.h"
#import "HJHttpRequestHelper+Praise.h"
@interface HJSpaceCardView ()


@property (weak, nonatomic) IBOutlet NSLayoutConstraint *viewHeight;
@property (weak, nonatomic) IBOutlet UIImageView *topImageBg;
@property (weak, nonatomic) IBOutlet UIImageView *bigImgBg;

@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *idLabel;

@property (weak, nonatomic) IBOutlet UIImageView *sexImageView;
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UIImageView *charmImageView;
@property (weak, nonatomic) IBOutlet UIImageView *richImageView;

@property (nonatomic, assign) BOOL isAttentioned;
@property (weak, nonatomic) IBOutlet UIImageView *followImgView;
@property (weak, nonatomic) IBOutlet UILabel *followLabel;
@property (weak, nonatomic) IBOutlet HJverticalButton *followBtn;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *width_name;

@property (nonatomic,assign) UserID uid;
@property (nonatomic,strong) UserInfo *info;
@property (nonatomic,copy) SendGiftBlock sendGiftBlock;
@property (nonatomic,copy) RoomOwnerFollow roomOwnerFollowBlock;


@property (weak, nonatomic) IBOutlet GGImageView *headAvatar;

@end

@implementation HJSpaceCardView

+ (void)show:(UserID)uid sendGiftBlock:(SendGiftBlock)sendGiftBlock roomFollow:(RoomOwnerFollow)roomFollowBlock onView:(UIView *)onView
{
    HJSpaceCardView *shareView = [[NSBundle mainBundle]loadNibNamed:@"HJSpaceCardView" owner:self options:nil][0];
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
    [[UIApplication sharedApplication].keyWindow addSubview:shareView];
    //    [onView addSubview:shareView];
    
    shareView.uid = uid;
    
    //    shareView.itemView.uid = uid;
    if (sendGiftBlock) {
        shareView.sendGiftBlock = sendGiftBlock;
    }
    
    if (roomFollowBlock) {
        shareView.roomOwnerFollowBlock = roomFollowBlock;
        //        shareView.itemView.roomOwnerFollowBlock = roomFollowBlock;
    }
    
    BOOL isMySpace = (uid==GetCore(HJAuthCoreHelp).getUid.userIDValue)?YES:NO;
    
    if (isMySpace) {
        //        shareView.height_contentView.constant = 232;
        //        shareView.menuView.hidden = YES;
        //        shareView.itemView.hidden = YES;
        shareView.viewHeight.constant = 100;
        [shareView.topImageBg setImage:[UIImage imageNamed:@"hj_me_space_cardBgradius"]];
        shareView.bigImgBg.hidden = YES;
    }
    //    shareView.itemView.hidden = YES;
    
    //    shareView.bottom_contentView.constant = -shareView.height_contentView.constant;
    //    [shareView layoutIfNeeded];
    
    //    [UIView animateWithDuration:0.3 animations:^{
    //        shareView.bottom_contentView.constant = 0;
    //
    //        [shareView layoutIfNeeded];
    //    } completion:^(BOOL finished) {
    //
    //    }];
    
    
}

- (void)close
{
    
    [self removeFromSuperview];
    //    [UIView animateWithDuration:0.3 animations:^{
    //        self.bottom_contentView.constant = -self.height_contentView.constant-10;
    //        [self layoutIfNeeded];
    //    } completion:^(BOOL finished) {
    //        [self removeFromSuperview];
    //
    //    }];
    
}

//关闭
- (IBAction)clickCloseBtn:(id)sender {
    [self close];
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    [self setBgRaduis];
    
    //    [self.itemView mas_remakeConstraints:^(MASConstraintMaker *make) {
    //        make.size.mas_equalTo(CGSizeMake(54, 28));
    //        make.right.mas_equalTo(self).mas_offset(-15);
    //        make.top.mas_equalTo(self).mas_offset(28+(iPhoneX?34:0));
    //    }];
    
}

- (void)setBgRaduis
{
    //背景图左上、右上圆角
    CGRect frame = CGRectMake(0, 0, kScreenWidth, 275);
    UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:frame byRoundingCorners:UIRectCornerTopRight | UIRectCornerTopLeft cornerRadii:CGSizeMake(15, 15)];
    
    CAShapeLayer * maskLayer = [[CAShapeLayer alloc]init];
    maskLayer.frame = frame;
    maskLayer.path = maskPath.CGPath;
    //    self.contentView.layer.mask = maskLayer;
    
    //我的空间按钮样式
    //    _mySpaceBtn.layer.shadowColor = [UIColor colorWithRed:0/255.0 green:0/255.0 blue:0/255.0 alpha:0.03].CGColor;
    //    _mySpaceBtn.layer.shadowOffset = CGSizeMake(0,10);
    //    _mySpaceBtn.layer.shadowRadius = 10;
    //    _mySpaceBtn.layer.shadowOpacity = 1;
    //    _mySpaceBtn.layer.cornerRadius = 20;
    
    //菜单图左上、右上圆角
    //    CGRect frame2 = CGRectMake(0, 0, kScreenWidth, 114);
    //    UIBezierPath *maskPath2 = [UIBezierPath bezierPathWithRoundedRect:frame2 byRoundingCorners:UIRectCornerTopRight | UIRectCornerTopLeft cornerRadii:CGSizeMake(15, 15)];
    //
    //    CAShapeLayer * maskLayer2 = [[CAShapeLayer alloc]init];
    //    maskLayer2.frame = frame2;
    //    maskLayer2.path = maskPath2.CGPath;
    //    self.menuView.layer.mask = maskLayer2;
    
}
- (IBAction)bgTapAction:(id)sender {
    
    [self close];
    
}
- (IBAction)reportAction:(id)sender {
    
    NSInteger type = 1;
    HJWKWebViewController *viewController = [[HJWKWebViewController alloc] init];
    NSString *URLString = [NSString stringWithFormat:@"%@/front/report/index.html?uid=%lld&type=%ld", [HJHttpRequestHelper getHostUrl],self.uid,type];
    viewController.url = [NSURL URLWithString:URLString];
    [[self topViewController].navigationController pushViewController:viewController animated:YES];
    [self close];
}

- (IBAction)copyAction:(id)sender {
    
    UIPasteboard * pastboard = [UIPasteboard generalPasteboard];
    pastboard.string = _info.erbanNo;
    [MBProgressHUD showSuccess:@"复制成功" toView:[UIApplication sharedApplication].keyWindow];
    
}

- (IBAction)enterMySpaceAction:(id)sender {
    
    [self showSpaceVC];
    [self close];
}

- (IBAction)chatAction:(id)sender {
    
    NIMSession *session = [NIMSession session:[NSString stringWithFormat:@"%lld",self.info.uid] type:NIMSessionTypeP2P];
    HJSessionViewController *vc = [[HJSessionViewController alloc] initWithSession:session];
    [[self topViewController].navigationController pushViewController:vc animated:YES];
    
    [self close];
    
}

- (IBAction)followAction:(id)sender {
    
    self.followBtn.userInteractionEnabled = NO;
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        self.followBtn.userInteractionEnabled = YES;
    });
    
    
    
    __weak typeof(self)weakSelf = self;
    if (self.followBtn.selected) {
        
        [HJUserHandler showCancelAttentionAlert:self.info.uid cancelFollowBlock:^{
            //            weakSelf.followBtn.selected = NO;
            
            [MBProgressHUD hideHUD];
            weakSelf.isAttentioned = NO;
            if (weakSelf.roomOwnerFollowBlock) {
                weakSelf.roomOwnerFollowBlock(NO);
            }
        }];
        
        
    }else{
        [MBProgressHUD showMessage:@"关注中..." toView:[UIApplication sharedApplication].keyWindow];
        
        [HJUserHandler follow:self.info.uid followSucceed:^{
            [MBProgressHUD hideHUDForView:[UIApplication sharedApplication].keyWindow];
            
            //            weakSelf.followBtn.selected = YES;
            weakSelf.isAttentioned = YES;
            if (weakSelf.roomOwnerFollowBlock) {
                weakSelf.roomOwnerFollowBlock(YES);
            }
        }];
    }
    
}

- (void)setIsAttentioned:(BOOL)isAttentioned
{
    _isAttentioned = isAttentioned;
    
    self.followBtn.selected = isAttentioned;
    if (isAttentioned) {
        self.followImgView.image = [UIImage imageNamed:@"hj_space_followed"];
        self.followLabel.text = @"已关注";
    }else{
        self.followImgView.image = [UIImage imageNamed:@"hj_space_follow"];
        self.followLabel.text = @"关注";
    }
}

- (IBAction)sendGiftAction:(id)sender {
    
    if (self.sendGiftBlock) {
        self.sendGiftBlock(self.info.uid);
    }
    
    [self close];
    
}

- (IBAction)spaceAction:(id)sender {
    
    [self showSpaceVC];
    [self close];
    
}

- (void)showSpaceVC
{
    HJMySpaceVC *vc = HJMeStoryBoard(@"HJMySpaceVC");
    vc.userID = self.uid;
    [[self topViewController].navigationController pushViewController:vc animated:YES];
}

- (void)getData
{
    __weak typeof(self)weakSelf = self;
    [HJHttpRequestHelper getUserInfo:self.uid success:^(UserInfo *userInfo) {
        
        weakSelf.info = userInfo;
        
    } failure:^(NSNumber *resCode, NSString *message) {
        
    }];
}

- (void)setUid:(UserID)uid
{
    _uid = uid;
    
    [self getData];
}

- (void)setInfo:(UserInfo *)info
{
    _info = info;
    if (info) {
        self.nameLabel.text = info.nick;
        
        CGSize size = [self.nameLabel.text boundingRectWithSize:CGSizeMake(0, self.nameLabel.height) options:(NSStringDrawingUsesLineFragmentOrigin|NSStringDrawingUsesFontLeading) attributes:@{NSFontAttributeName:self.nameLabel.font} context:nil].size;
        self.width_name.constant = (kScreenWidth - size.width)<164?kScreenWidth-164:size.width;
        
        
        self.sexImageView.image = [UIImage imageNamed:info.gender==UserInfo_Male?@"hj_home_attend_man":@"hj_home_attend_woman"];
        
        [self.avatarImageView qn_setImageImageWithUrl:info.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
        if (info.headwearUrl != nil &&  info.headwearUrl.length>0) {
            self.headAvatar.hidden = NO;
            [self.headAvatar qn_setImageImageWithUrl:info.headwearUrl placeholderImage:default_avatar type:ImageTypeUserIcon];
        }else{
            self.headAvatar.hidden = YES;
        }
        
        self.idLabel.text = [NSString stringWithFormat:@"ID:%@",info.erbanNo];
        //
        //        self.fanLabel.text = [NSString stringWithFormat:@"%ld",info.fansNum];
        ////        self.followLabel.text = [NSString stringWithFormat:@"%ld",info.followNum];
        //        self.friendLabel.text = [NSString stringWithFormat:@"%ld",info.liveness];
        
        self.followBtn.selected = info.isFan;
        
        //        self.isAttentioned = info.isFan;
        [self checkAttend];
        
        if (_info.experLevel>0) {
            self.richImageView.image = [UIImage imageNamed:[NSString getMoneyLevelImageName:_info.experLevel]];
        }else{
            self.richImageView.hidden = YES;
        }
        
        if (_info.charmLevel>0) {
            self.charmImageView.image = [UIImage imageNamed:[NSString getCharmLevelImageName:_info.charmLevel]];
            
        }else{
            self.charmImageView.hidden = YES;
            //            self.left_rich.constant = -43;
        }
        
    }
}

- (void)checkAttend
{
    __weak typeof(self)weakSelf = self;
    [HJHttpRequestHelper isLike:[GetCore(HJAuthCoreHelp).getUid userIDValue] isLikeUid:self.uid success:^(BOOL isLike) {
        weakSelf.isAttentioned = isLike;
    } failure:^(NSNumber *resCode, NSString *message) {
        weakSelf.isAttentioned = NO;
    }];
}

//- (HJReportItemView *)itemView
//{
//    if (!_itemView) {
//        _itemView = [[NSBundle mainBundle] loadNibNamed:@"HJReportItemView" owner:self options:nil][0];
//
//        __weak typeof(self)weakSelf = self;
//        _itemView.spaceCardViewCancelBlock = ^(BOOL isFoloow) {
//            weakSelf.followBtn.selected = isFoloow;
//        };
//
//        [self addSubview:_itemView];
//    }
//    return _itemView;
//}


@end
