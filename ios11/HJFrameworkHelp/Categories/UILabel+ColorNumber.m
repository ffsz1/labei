//
//  UILabel+ColorNumber.m
//  LHDuoBao
//
//  Created by jackyoupo on 15/11/6.
//  Copyright (c) 2015年 youpo. All rights reserved.
//

#import "UILabel+ColorNumber.h"

@implementation UILabel (ColorNumber)
-(void)colorNumber:(int)num withColor:(UIColor *)numColor
{
   NSRange range = [self.text rangeOfString:[NSString stringWithFormat:@"%d",num]];
    UIColor *mainColor = self.textColor;
    NSMutableAttributedString *mas = [[NSMutableAttributedString alloc]initWithString:self.text];
    [mas setAttributes:@{NSFontAttributeName :self.font,NSForegroundColorAttributeName :mainColor} range:NSMakeRange(0, self.text.length )];
 
    [mas setAttributes:@{NSForegroundColorAttributeName:numColor} range:range];
    self.attributedText = mas;
    
}
-(void)colorString:(NSString *)str secString:(NSString *)str2 witColor:(UIColor *)stringColor
{
    if (str) {
        //找到位置
        NSRange range = [self.text rangeOfString:str];
        NSRange range2 = [self.text rangeOfString:str2];
        UIColor *mainColor = self.textColor;
        
        
        NSMutableAttributedString *mas = [[NSMutableAttributedString alloc]initWithString:self.text];
        
        
        [mas setAttributes:@{NSFontAttributeName :self.font,NSForegroundColorAttributeName :mainColor} range:NSMakeRange(0, self.text.length - 1)];
        
        [mas setAttributes:@{NSForegroundColorAttributeName:stringColor} range:range];
        [mas setAttributes:@{NSForegroundColorAttributeName:stringColor} range:range2];
        
        self.attributedText = mas;
    }
}

@end
