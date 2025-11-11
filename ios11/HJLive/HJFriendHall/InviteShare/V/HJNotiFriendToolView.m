//
//  HJNotiFriendToolView.m
//  HJLive
//
//  Created by feiyin on 2020/7/19.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJNotiFriendToolView.h"

@interface HJNotiFriendToolView()
@end

@implementation HJNotiFriendToolView

- (void)awakeFromNib {
    [super awakeFromNib];
//    self.contentView.layer.cornerRadius = 18;
    UIView *paddingView = [[UIView alloc] initWithFrame:CGRectMake(0, 0,15, 0)];
     self.chatTextField.leftView = paddingView;
     self.chatTextField.leftViewMode = UITextFieldViewModeAlways;

    self.chatTextField.layer.cornerRadius = 16;
    self.chatTextField.layer.masksToBounds = YES;
    self.chatTextField.layer.borderColor = [UIColor colorWithHexString:@"cccccc"].CGColor;
    self.chatTextField.layer.borderWidth = 1;
//    self.TimeView.layer.cornerRadius = 18;
//    self.TimeView.layer.masksToBounds = YES;
    self.timeTextLabel.text = @"";
    self.timeTextLabel.font = [UIFont systemFontOfSize:15];
    self.timeTextLabel.textColor = [UIColor colorWithHexString:@"#E6C0FD"];
    self.TimeView.hidden = YES;
    self.contentView.hidden = false;
    
    [[[NSNotificationCenter defaultCenter] rac_addObserverForName:@"postData" object:nil] subscribeNext:^(NSNotification *notification) {
        self.timeTextLabel.text = notification.object[@"time"];
        if (!self.contentView.hidden) {
            self.contentView.hidden = YES;
            self.TimeView.hidden = false;
        }
    }];
    
    [[[NSNotificationCenter defaultCenter] rac_addObserverForName:@"postD" object:nil] subscribeNext:^(NSNotification *notification) {
        self.TimeView.hidden = YES;
        self.contentView.hidden = false;
    }];
}

- (void)startCountDownTime {
    
    NSUserDefaults *ur = [NSUserDefaults standardUserDefaults];
    
//    __block int time = 31;
    __block int time = 11;

    if ([ur objectForKey:@"publicChatHallTime"]) {
        int publicChatHallTime = [[ur objectForKey:@"publicChatHallTime"] intValue];
        time = publicChatHallTime+1;
    }
    
    
    self.TimeView.hidden = false;
    self.contentView.hidden = YES;

    //获取全局队列
    dispatch_queue_t global = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    
    dispatch_source_t timer = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, global);
    
    dispatch_source_set_timer(timer, DISPATCH_TIME_NOW, 1.0 * NSEC_PER_SEC, 0 * NSEC_PER_SEC);

    @weakify(self);
    dispatch_source_set_event_handler(timer, ^{
        @strongify(self);
        time --;

        if (time < 0) {
            
            dispatch_source_cancel(timer);
            
            dispatch_async(dispatch_get_main_queue(), ^{
                [[NSNotificationCenter defaultCenter] postNotificationName:@"postD" object:nil];
            });
        }else {
            dispatch_async(dispatch_get_main_queue(), ^{
                [[NSNotificationCenter defaultCenter] postNotificationName:@"postData" object:@{@"time": [NSString stringWithFormat:@"%dS",time]}];
            });
        }
    });
    dispatch_resume(timer);
}
- (IBAction)sendAction:(id)sender {
    
    if (self.didClickSendBtnActionBlock) {
        self.didClickSendBtnActionBlock();
    }
}

@end
