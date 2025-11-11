//
//  YPAvatarMicroViewCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/8.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPAvatarMicroViewCell.h"

@interface YPAvatarMicroViewCell ()

@property (weak, nonatomic) IBOutlet UIImageView *avatarImageView;
//@property (weak, nonatomic) IBOutlet NSLayoutConstraint *avatarImageViewW;
//@property (weak, nonatomic) IBOutlet NSLayoutConstraint *avatarImageViewH;
//@property (weak, nonatomic) IBOutlet NSLayoutConstraint *microLeading;
//@property (weak, nonatomic) IBOutlet UIImageView *memberTypeTagImageView;
@property (weak, nonatomic) IBOutlet UILabel *microPositionLabel;
@property (weak, nonatomic) IBOutlet UIImageView *selecteImageView;

@end


@implementation YPAvatarMicroViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    self.avatarImageView.layer.cornerRadius = self.avatarImageView.frame.size.width / 2;
    self.avatarImageView.layer.masksToBounds = YES;
    self.avatarImageView.layer.borderWidth = 0.5f;
    self.avatarImageView.layer.borderColor = UIColorHex(FF0267).CGColor;
    self.microPositionLabel.layer.masksToBounds = YES;
    self.microPositionLabel.layer.cornerRadius = self.microPositionLabel.width / 2;
}

- (void)setPositionText:(NSString *)text type:(XCAvatarMicroViewType)type isSelect:(BOOL)isSelect{
    self.microPositionLabel.text = text;
    if (type == XCAvatarMicroViewTypeAll) {
        if (isSelect == YES) {
            self.microPositionLabel.textColor = UIColorHex(ffffff);
            self.microPositionLabel.backgroundColor = UIColorHex(FF237B);
        } else {
            self.microPositionLabel.textColor = UIColorHex(FF0267);
            self.microPositionLabel.backgroundColor = UIColorHex(E7D8FE);
        }
    } else {
        self.microPositionLabel.textColor = UIColorHex(FF237B);
        self.microPositionLabel.backgroundColor = UIColorHex(E7D8FE);
        [self setupIsSelect:isSelect];
    }
}

- (void)setupIsSelect:(BOOL)isSelect { 
    self.selecteImageView.hidden = !isSelect;
    if (isSelect) {
        self.avatarImageView.layer.borderWidth = 1.5f;
        self.avatarImageView.layer.borderColor = UIColorHex(FF0267).CGColor;
    } else {
        self.avatarImageView.layer.borderWidth = 0.5;
        self.avatarImageView.layer.borderColor = UIColorHex(FF0267).CGColor;
    }
}

- (void)setupAvatarUrl:(NSString *)url {
    [self.avatarImageView qn_setImageImageWithUrl:url placeholderImage:default_avatar type:ImageTypeUserIcon];
}

- (void)resetAvatarSelectStyle {
    self.selecteImageView.hidden = YES;
    self.avatarImageView.layer.borderWidth = 0.5;
    self.avatarImageView.layer.borderColor = UIColorHex(FF0267).CGColor;
    [self.avatarImageView setImage:[UIImage imageNamed:@"yp_login_logo"]];
}

@end
