//
//  YPPersonalPhotoCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPPersonalPhotoCell.h"

@implementation YPPersonalPhotoCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (IBAction)delegateButtonClick:(UIButton *)sender {
    if ([_delegate respondsToSelector:@selector(deletePhoto:)]) {
        [self.delegate deletePhoto:self.indexPath];
    }
}

@end
