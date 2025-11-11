//
//  HJEggGiftCCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/11.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJEggGiftCCell.h"

#import "NSAttributedString+YYText.h"
#import "YYAnimatedImageView.h"


@implementation HJEggGiftCCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setInfo:(GiftInfo *)info
{
    _info = info;
    if (info) {
        
        [self.logo qn_setImageImageWithUrl:info.giftUrl placeholderImage:placeholder_image_square type:ImageTypeHomePageItem];
        
//        NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"%@ ",info.giftName]];
        
//        NSTextAttachment *attach = [[NSTextAttachment alloc] init];
//        attach.image = [UIImage imageNamed:@"xbd_room_egg_coin"];
//        attach.bounds = CGRectMake(0, 0, 12, 12);
//
        
//        NSAttributedString * imageString1 = [NSAttributedString attributedStringWithAttachment:attach];
        
        NSString *price = [NSString stringWithFormat:@" %.0f开心",info.goldPrice];
        
//        [str appendAttributedString:imageString1];
//        [str appendAttributedString:[[NSMutableAttributedString alloc] initWithString:price]];
        
//        self.nameLabel.attributedText = str;
        
        self.nameLabel.text = info.giftName;
        
        self.priceLabel.text = price;
        
    }
}

@end
