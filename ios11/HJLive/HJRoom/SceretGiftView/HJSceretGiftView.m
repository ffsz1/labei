//
//  HJSceretGiftView.m
//  HJLive
//
//  Created by feiyin on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJSceretGiftView.h"

@interface HJSceretGiftView ()

@property (weak, nonatomic) IBOutlet UILabel *giftNameLabel;
@property (weak, nonatomic) IBOutlet UIImageView *giftImageView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *giftImageViewW;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *giftImageViewH;

@end

@implementation HJSceretGiftView

- (void)awakeFromNib {
    
    [super awakeFromNib];
    
    self.giftImageViewW.constant = 149.f;
    self.giftImageViewH.constant = 128.f;
    
}

- (IBAction)closeBtnAction:(id)sender {
    if (self.closeActionBlock) {
        self.closeActionBlock();
    }
}

- (void)setInfo:(HJGiftSecretInfo *)info {
    
    _info = info;
    self.giftNameLabel.text = [NSString stringWithFormat:@"%@X%zd",info.giftName ? info.giftName : @"", info.giftNum];
    [self.giftImageView sd_setImageWithURL:[NSURL URLWithString:info.picUrl] placeholderImage:[UIImage imageNamed:placeholder_image_square]];
}

- (void)showPhoto {
    
//    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1.f * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
//
//        @weakify(self);
//        [UIView animateWithDuration:1.f animations:^{
//            @strongify(self);
//            self.giftImageViewW.constant = 149.f;
//            self.giftImageViewH.constant = 128.f;
//        }];
//    });
}

@end
