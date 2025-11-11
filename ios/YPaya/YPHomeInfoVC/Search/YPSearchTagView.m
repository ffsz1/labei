//
//  YPSearchTagView.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPSearchTagView.h"

@implementation YPSearchTagView

-(void)setArr:(NSArray *)arr {
    _arr = arr;
    [self removeAllSubviews];
    CGFloat marginX = 15;
    CGFloat marginY = 10;
    CGFloat height = 28;
    UIButton * markBtn;
    for (int i = 0; i < _arr.count; i++) {
        CGFloat width =  [self calculateString:_arr[i] Width:12] +35;
        UIButton * tagBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        if (!markBtn) {
            tagBtn.frame = CGRectMake(marginX, marginY, width, height);
        }else{
            if (markBtn.frame.origin.x + markBtn.frame.size.width + marginX + width + marginX > kScreenWidth) {
                tagBtn.frame = CGRectMake(marginX, markBtn.frame.origin.y + markBtn.frame.size.height + marginY, width, height);
            }else{
                tagBtn.frame = CGRectMake(markBtn.frame.origin.x + markBtn.frame.size.width + marginX, markBtn.frame.origin.y, width, height);
            }
        }
        [tagBtn setTitle:_arr[i] forState:UIControlStateNormal];
        tagBtn.titleLabel.font = [UIFont systemFontOfSize:14];
        [tagBtn setTitleColor:UIColorHex(1A1A1A) forState:UIControlStateNormal];
        tagBtn.backgroundColor = UIColorHex(F0F0F0);
        [self makeCornerRadius:2 borderColor:UIColorHex(AAAAAA) layer:tagBtn.layer borderWidth:0];
        markBtn = tagBtn;
        
        @weakify(self);
        [tagBtn addBlockForControlEvents:UIControlEventTouchUpInside block:^(UIButton *  _Nonnull sender) {
            @strongify(self);
            if ([self.delegate respondsToSelector:@selector(handleSelectTag:)]) {
                [self.delegate handleSelectTag:i];
            }
        }];

        [self addSubview:markBtn];
    }
    CGRect rect = self.frame;
    rect.size.height = markBtn.frame.origin.y + markBtn.frame.size.height + marginY;
    self.frame = rect;
}



- (void)makeCornerRadius:(CGFloat)radius borderColor:(UIColor *)borderColor layer:(CALayer *)layer borderWidth:(CGFloat)borderWidth {
    layer.cornerRadius = radius;
    layer.masksToBounds = YES;
    layer.borderColor = borderColor.CGColor;
    layer.borderWidth = borderWidth;
}

-(CGFloat)calculateString:(NSString *)str Width:(NSInteger)font {
    CGSize size = [str boundingRectWithSize:CGSizeMake(kScreenWidth, 100000) options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName : [UIFont systemFontOfSize:font]} context:nil].size;
    return size.width;
}


@end
