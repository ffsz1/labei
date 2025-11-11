//
//  StateButton.m
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "StateButton.h"
#import "YYDefaultTheme.h"

@implementation StateButton

- (void)awakeFromNib
{
    [super awakeFromNib];
    self.backgroundColor = [[YYDefaultTheme defaultTheme] colorWithHexString:@"#FFE5EF" alpha:1.0];
}

- (void)setButtonEnabled:(BOOL)enabled
{
    if (enabled) {
        self.backgroundColor = [[YYDefaultTheme defaultTheme] colorWithHexString:@"#FFE5EF" alpha:1.0];
    } else {
        self.backgroundColor = [[YYDefaultTheme defaultTheme] colorWithHexString:@"#FFE5EF" alpha:1.0];
    }
    [self setEnabled:enabled];
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
