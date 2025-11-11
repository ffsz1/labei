//
//  YPUserListCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPUserListCell.h"
#import "UIImage+extension.h"

@interface YPUserListCell ()



@end

@implementation YPUserListCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
//    [self clipImageView];
    
}

#pragma mark - Setter

- (void)setUrlStr:(NSString *)urlStr {
    _urlStr = urlStr;
    if (urlStr.length > 0) {
        [self.imageView yy_setImageWithURL:[NSURL URLWithString:urlStr] placeholder:[UIImage imageNamed:default_avatar]];
//        [self.imageView was_setCircleImageWithUrlString:urlStr placeholder:[UIImage imageNamed:default_avatar]];
    }
}

#pragma mark - Private Method

- (void)clipImageView {
    self.imageView.layer.cornerRadius = self.imageView.frame.size.width / 2;
    self.imageView.layer.borderWidth = 2;
    self.imageView.layer.borderColor = RGBACOLOR(255, 255, 255, 0.2).CGColor;
    self.imageView.layer.masksToBounds = YES;
}

@end
