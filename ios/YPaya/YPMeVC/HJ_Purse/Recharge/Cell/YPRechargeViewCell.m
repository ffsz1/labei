//
//  YPRechargeViewCell.m
//  HJLive
//
//  Created by feiyin on 2020/6/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRechargeViewCell.h"
#import "YPYYDefaultTheme.h"
@implementation YPRechargeViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    self.rmbBtn.layer.borderWidth = 0.5;
    self.rmbBtn.layer.borderColor = [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FED700" alpha:1.0].CGColor;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)onRmbBtnClicked:(id)sender {
    if (self.delegate != nil) {
        [self.delegate onRmbSelected:self.index];
    }
}
@end
