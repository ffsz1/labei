//
//  YPBadNetworkAlertView.m
//  HJLive
//
//  Created by feiyin on 2020/6/20.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBadNetworkAlertView.h"

@interface YPBadNetworkAlertView ()
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UILabel *descLabel;
@property (weak, nonatomic) IBOutlet UIButton *rightBtn;
@property (weak, nonatomic) IBOutlet UIButton *cancelBtn;



@end

@implementation YPBadNetworkAlertView

+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle]loadNibNamed:@"YPBadNetworkAlertView" owner:self options:nil].lastObject;
}


- (void)setTitle:(NSString *)title {
    _title = title;
    self.titleLabel.text = title;
}

- (void)setDesc:(NSString *)desc {
    _desc = desc;
    self.descLabel.text = desc;
}

- (IBAction)leftBtnClick:(UIButton *)sender {
    self.leftBlock();
}

- (IBAction)rightBtnClick:(UIButton *)sender {
    self.rightBlock();
}



@end
