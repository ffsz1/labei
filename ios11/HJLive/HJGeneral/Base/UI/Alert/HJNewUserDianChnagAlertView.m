//
//  HJNewUserDianChnagAlertView.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJNewUserDianChnagAlertView.h"

@interface HJNewUserDianChnagAlertView ()

@property (weak, nonatomic) IBOutlet UIImageView *photoView;
@property (weak, nonatomic) IBOutlet UIButton *button;

@end

@implementation HJNewUserDianChnagAlertView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    self.photoView.layer.cornerRadius = 0.5 * (XC_SCREE_W - 138 - 138);
    self.photoView.layer.borderColor = UIColorHex(d0ebfc).CGColor;
    self.photoView.layer.borderWidth = 3.f;
}

- (IBAction)closeBtnAction:(id)sender {
    
    if (self.closeBtnActionBlock) {
        self.closeBtnActionBlock();
    }
}
- (IBAction)dianchangAction:(id)sender {
    
    if (self.dianchangActionBlock) {
        self.dianchangActionBlock();
    }
}

- (void)setAvatar:(NSString *)avatar {
    _avatar = avatar;
    [self.photoView qn_setImageImageWithUrl:avatar placeholderImage:default_bg type:ImageTypeHomePageItem];
}

- (void)setTitle:(NSString *)title {
    _title = title;
    
    [self.button setTitle:title.length ? title : @"" forState:UIControlStateNormal];
}

@end
