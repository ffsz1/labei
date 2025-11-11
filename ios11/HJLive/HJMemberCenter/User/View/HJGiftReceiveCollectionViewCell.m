//
//  HJGiftReceiveCollectionViewCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGiftReceiveCollectionViewCell.h"

@implementation HJGiftReceiveCollectionViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    self.giftName.hidden = YES;
    self.giftNumber.layer.masksToBounds = YES;
    self.giftNumber.layer.cornerRadius = 1;
}

@end
