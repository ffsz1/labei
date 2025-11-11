//
//  HJBadNetworkAlertView.m
//  HJLive
//
//  Created by feiyin on 2020/6/20.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJBadNetworkAlertView.h"

@interface HJBadNetworkAlertView ()
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UILabel *descLabel;
@property (weak, nonatomic) IBOutlet UIButton *rightBtn;
@property (weak, nonatomic) IBOutlet UIButton *cancelBtn;



@end

@implementation HJBadNetworkAlertView

+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle]loadNibNamed:@"HJBadNetworkAlertView" owner:self options:nil].lastObject;
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
