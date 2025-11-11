//
//  YPNoticeView.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPNoticeView.h"

@interface YPNoticeView ()
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UILabel *messageLabel;
@property (weak, nonatomic) IBOutlet UIButton *updateButton;
@end

@implementation YPNoticeView

+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle]loadNibNamed:@"YPNoticeView" owner:self options:nil].lastObject;
}

- (IBAction)updateBtn:(UIButton *)sender {
    self.noticeBlock();
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
