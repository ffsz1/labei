//
//  HJRechargeViewCell.m
//  HJLive
//
//  Created by feiyin on 2020/6/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRechargeViewCell.h"
#import "YYDefaultTheme.h"
@implementation HJRechargeViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    self.rmbBtn.layer.borderWidth = 0.5;
    self.rmbBtn.layer.borderColor = [[YYDefaultTheme defaultTheme] colorWithHexString:@"#FED700" alpha:1.0].CGColor;
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
