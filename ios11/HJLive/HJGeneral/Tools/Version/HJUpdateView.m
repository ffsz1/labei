//
//  HJUpdateView.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJUpdateView.h"

@interface HJUpdateView ()

@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UILabel *messageLabel;
@property (weak, nonatomic) IBOutlet UIButton *updateButton;
@property (weak, nonatomic) IBOutlet UIButton *cancelButton;


@end


@implementation HJUpdateView

+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle]loadNibNamed:@"HJUpdateView" owner:self options:nil].lastObject;
}

- (IBAction)updateBtn:(UIButton *)sender {
    self.updateBlock();
}

- (IBAction)cancelBtnClick:(UIButton *)sender {
    [self removeFromSuperview];
}

- (void)setTitle:(NSString *)title {
    _title = title;
    self.titleLabel.text = title;
}

- (void)setMessage:(NSString *)message {
    _message = message;
    self.messageLabel.text = message;
}




@end
