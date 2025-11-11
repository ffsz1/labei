//
//  YPEggGiftCCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/11.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPEggGiftCCell.h"

#import "NSAttributedString+YYText.h"
#import "YYAnimatedImageView.h"


@implementation YPEggGiftCCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setInfo:(YPGiftInfo *)info
{
    _info = info;
    if (info) {
        
        [self.logo qn_setImageImageWithUrl:info.giftUrl placeholderImage:placeholder_image_square type:ImageTypeHomePageItem];
        
//        NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"%@ ",info.giftName]];
        
//        NSTextAttachment *attach = [[NSTextAttachment alloc] init];
//        attach.image = [UIImage imageNamed:@"yp_room_egg_coin"];
//        attach.bounds = CGRectMake(0, 0, 12, 12);
//
        
//        NSAttributedString * imageString1 = [NSAttributedString attributedStringWithAttachment:attach];
        
        NSString *price = [NSString stringWithFormat:@" %.0f",info.goldPrice];
        
//        [str appendAttributedString:imageString1];
//        [str appendAttributedString:[[NSMutableAttributedString alloc] initWithString:price]];
        
//        self.nameLabel.attributedText = str;
        
        self.nameLabel.text = info.giftName;
        
        self.priceLabel.text = price;
        
    }
}

@end
