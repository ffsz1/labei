//
//  HJPersonalLevelChirldVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJPersonalLevelChirldVC.h"
#import "HJLevelModel.h"
#import "HJPersonalLevelTipVC.h"
#import "HJUserCoreClient.h"
#import "HJUserCoreHelp.h"

@interface HJPersonalLevelChirldVC ()

@property (weak, nonatomic) IBOutlet UIView *lineView;
@property (weak, nonatomic) IBOutlet UILabel *currentLv;
@property (weak, nonatomic) IBOutlet UIProgressView *propressView;
//@property (weak, nonatomic) IBOutlet NSLayoutConstraint *left_levelLabel;
@property (weak, nonatomic) IBOutlet UILabel *nextLevel;
@end

@implementation HJPersonalLevelChirldVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.avatarImg.layer.borderColor = [UIColor whiteColor].CGColor;
    self.avatarImg.layer.borderWidth = 2;
    
    [self addCore];
    [self loadData];
    
    [self setTiptext];
    
    
    [self addShadowToView:self.bView withColor:[UIColor blackColor]];
    
}

- (void)setTiptext
{
    if (_type == 0) {
        self.upview.backgroundColor = [UIColor colorWithHexString:@"#81A6FF"];
        if (kScreenWidth>380) {
//            self.pregress_top_layout.constant = 26;
            self.height_layout.constant = 430;
//            self.upview_height_layout.constant = 200;
        }else{
//            self.pregress_top_layout.constant = 10;
            self.height_layout.constant = 400;
//            self.upview_height_layout.constant = 160;
        }
        self.imgview.image = [UIImage imageNamed:@"yp_me_dengjiguize"];

    }else{
        self.upview.backgroundColor = [UIColor colorWithHexString:@"#FF81A4"];
      
        self.imgview.image = [UIImage imageNamed:@"yp_me_meiliguize"];
        if (kScreenWidth>380) {
//            self.pregress_top_layout.constant = 26;
            self.height_layout.constant = 490;
//            self.upview_height_layout.constant = 200;
        }else{
//            self.pregress_top_layout.constant = 10;
            self.height_layout.constant = 414;
//            self.upview_height_layout.constant = 160;
        }
    }
}

/// 添加四边阴影效果
- (void)addShadowToView:(UIView *)theView withColor:(UIColor *)theColor {
    // 阴影颜色
    theView.layer.shadowColor = theColor.CGColor;
    // 阴影偏移，默认(0, -3)
    theView.layer.shadowOffset = CGSizeMake(0,0);
    // 阴影透明度，默认0
    theView.layer.shadowOpacity = 0.5;
    // 阴影半径，默认3
    theView.layer.shadowRadius = 5;
}


- (void)dealloc {
    RemoveCoreClientAll(self);
}

#pragma mark - 请求回调
-(void)onGetRichLevelSuccess:(HJLevelModel *)model {
    self.richLevel = model;
    if (self.type == 0) {
        NSInteger experLevel = model.level;
        self.lvImg.image = [UIImage imageNamed:[NSString getMoneyLevelImageName:experLevel]];
        
        self.currentLv.text = [NSString stringWithFormat:@"Lv.%ld",(long)experLevel];
        self.nextLevel.text = [NSString stringWithFormat:@"Lv.%ld",(long)experLevel+1];
        
        self.propressView.progress = model.levelPercent;
//        self.left_levelLabel.constant = (kScreenWidth-100)*model.levelPercent;
        
        [self.shengjiTip setTitle:[NSString stringWithFormat:@"距离升级还差%@经验值",model.leftGoldNum] forState:UIControlStateNormal];
        
        self.leftCons.constant = (XC_SCREE_W - 40)*model.levelPercent;
    }
}

-(void)onGetMeiliLevelSuccess:(HJLevelModel *)model {
    self.meliLevel = model;
    if (self.type == 1) {
//        self.lineView.hidden = YES;
        NSInteger experLevel = model.level;
        self.lvImg.image = [UIImage imageNamed:[NSString getCharmLevelImageName:experLevel]];
        self.currentLv.text = [NSString stringWithFormat:@"Lv.%ld",(long)experLevel];
        self.nextLevel.text = [NSString stringWithFormat:@"Lv.%ld",(long)experLevel+1];
        
        self.propressView.progress = model.levelPercent;
//        self.left_levelLabel.constant = (kScreenWidth-100)*model.levelPercent;

        [self.shengjiTip setTitle:[NSString stringWithFormat:@"距离升级还差%@经验值",model.leftGoldNum] forState:UIControlStateNormal];
        
        self.leftCons.constant = (XC_SCREE_W - 40)*model.levelPercent;
    }
}

- (void)addCore {
    AddCoreClient(HJUserCoreClient, self);
}

- (void)loadData {
    [GetCore(HJUserCoreHelp) getRichLevel];
    [GetCore(HJUserCoreHelp) getMeiliLevel];
    
    @weakify(self);
    [GetCore(HJUserCoreHelp) getUserInfo:[GetCore(HJAuthCoreHelp) getUid].userIDValue refresh:YES success:^(UserInfo *info) {
        @strongify(self);
        self.userInfo = info;
        
        
        
        [self.avatarImg sd_setImageWithURL:[NSURL URLWithString:self.userInfo.avatar] placeholderImage:[UIImage imageNamed:default_avatar]];
        
        
  
    }];
}

- (void)setupType:(NSInteger)type model:(HJLevelModel *)model {
    if (type == 0) {
        [self.shengjiTip setTitle:[NSString stringWithFormat:@"离升级还需消耗%@开心",model.leftGoldNum] forState:UIControlStateNormal];
    } else {
        [self.shengjiTip setTitle:[NSString stringWithFormat:@"离升级还需消耗%@开心",model.leftGoldNum] forState:UIControlStateNormal];
    }
}

- (void)setType:(NSInteger)type
{
    _type = type;
    [self setTiptext];
}

- (IBAction)jumpLevelTip:(UIButton *)sender {
    
//    if (self.type == 1) {
//        return;
//    }
    
    HJPersonalLevelTipVC *tip = [HJPersonalLevelTipVC new];
    tip.type = self.type;
    [self.navigationController pushViewController:tip animated:YES];
}

@end
