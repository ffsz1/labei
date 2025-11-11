//
//  YPFinishLiveView.m
//  HJLive
//
//  Created by feiyin on 2020/7/15.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPFinishLiveView.h"
#import "YPAvatarControl.h"
#import "YPUserCoreHelp.h"
#import "YPPurseCore.h"
#import "YPVersionCoreHelp.h"

#import "YPRoomViewControllerCenter.h"
#import "YPYYDefaultTheme.h"
#import "YPUserViewControllerFactory.h"
#import "YPMySpaceVC.h"
#import "YPYYViewControllerCenter.h"
#import "UIImageView+YYWebImage.h"
#import "UIImage+ImageEffects.h"


@interface YPFinishLiveView()
@property (weak, nonatomic) IBOutlet UIImageView *imageBg;
@property (weak, nonatomic) IBOutlet YPAvatarControl *avatar;
@property (weak, nonatomic) IBOutlet UILabel *nickLabel;
@property (weak, nonatomic) IBOutlet UIImageView *sexImage;
@property (weak, nonatomic) IBOutlet UIView *homePageBg;
@property (weak, nonatomic) IBOutlet UIView *exitBg;
@property (weak, nonatomic) IBOutlet UILabel *subTipsLabel;
@property (nonatomic, strong) UserInfo *userInfo;
@end

@implementation YPFinishLiveView
+ (instancetype)loadFromNIB
{
    return [[NSBundle mainBundle] loadNibNamed:@"YPFinishLiveView" owner:nil options:nil].firstObject;
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    [self initView];
}

- (void)initView
{
    
    self.avatar.layer.cornerRadius = 55;
    self.avatar.layer.masksToBounds = YES;
    
    self.homePageBg.layer.cornerRadius = 44;
    self.homePageBg.layer.masksToBounds = YES;
    
    self.exitBg.layer.cornerRadius = 44;
    self.exitBg.layer.masksToBounds = YES;
    self.exitBg.layer.borderColor = UIColorHex(FF0569).CGColor;
    self.exitBg.layer.borderWidth = 1;
    
    self.homePageBg.userInteractionEnabled = YES;
    UITapGestureRecognizer *rec = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onHomePageClicked:)];
    [self.homePageBg addGestureRecognizer:rec];
    
    self.exitBg.userInteractionEnabled = YES;
    UITapGestureRecognizer *rec1 = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onExitClicked:)];
    [self.exitBg addGestureRecognizer:rec1];
}

- (void)setUid:(UserID)uid
{
    _uid = uid;
    @weakify(self);
    [GetCore(YPUserCoreHelp) getUserInfo:self.uid refresh:YES success:^(UserInfo *info) {
        @strongify(self);
        self.userInfo = info;
        self.nickLabel.text = info.nick;
        [self.avatar setImageURL:[NSURL URLWithString:self.userInfo.avatar]];
        self.nickLabel.text = self.userInfo.nick;
        if (self.userInfo.gender == UserInfo_Male) {
            self.sexImage.image = [UIImage imageNamed:@"xc_room_icon_man"];
        } else {
            self.sexImage.image = [UIImage imageNamed:@"xc_room_icon_wem"];
        }
        [self.imageBg yy_setImageWithURL:[NSURL URLWithString:self.userInfo.avatar] placeholder:nil options:kNilOptions completion:^(UIImage * _Nullable image, NSURL * _Nonnull url, YYWebImageFromType from, YYWebImageStage stage, NSError * _Nullable error) {
            if (image != nil) {
                self.imageBg.image = [image applyBlurWithRadius:2 tintColor:[UIColor colorWithRed:0 green:0 blue:0 alpha:0.2] saturationDeltaFactor:1.8 maskImage:nil];
            }
        }];
    }];
}

- (void)onHomePageClicked:(id) tap
{
    
    YPMySpaceVC *vc = YPMeStoryBoard(@"YPMySpaceVC");
    vc.userID = self.uid;
    
    [[YPYYViewControllerCenter currentViewController].navigationController pushViewController:vc animated:YES];

    
}

- (void)onExitClicked:(id) tap
{
    [[YPRoomViewControllerCenter defaultCenter] dismissChannelViewWithQuitCurrentRoom:YES];
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
