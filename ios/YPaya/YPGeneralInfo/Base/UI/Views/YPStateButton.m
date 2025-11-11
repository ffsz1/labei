//
//  YPStateButton.m
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPStateButton.h"
#import "YPYYDefaultTheme.h"

@implementation YPStateButton

- (void)awakeFromNib
{
    [super awakeFromNib];
    self.backgroundColor = [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FFE5EF" alpha:1.0];
}

- (void)setButtonEnabled:(BOOL)enabled
{
    if (enabled) {
        self.backgroundColor = [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FFE5EF" alpha:1.0];
    } else {
        self.backgroundColor = [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FFE5EF" alpha:1.0];
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
