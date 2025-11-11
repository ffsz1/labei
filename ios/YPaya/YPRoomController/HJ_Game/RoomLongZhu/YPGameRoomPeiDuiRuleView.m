//
//  YPGameRoomPeiDuiRuleView.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPGameRoomPeiDuiRuleView.h"

@interface YPGameRoomPeiDuiRuleView ()

@property (weak, nonatomic) IBOutlet UIButton *closeBtn;

@end

@implementation YPGameRoomPeiDuiRuleView

- (void)awakeFromNib {
    
    [super awakeFromNib];
    
    self.closeBtn.layer.borderColor = UIColorHex(ffffff).CGColor;
    
    [self addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
        
    }]];
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
- (IBAction)closeBtnAction:(id)sender {
    
    [self removeFromSuperview];
}

@end
