//
//  UILabel+ColorNumber.h
//  LHDuoBao
//
//  Created by jackyoupo on 15/11/6.
//  Copyright (c) 2015年 youpo. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UILabel (ColorNumber)
-(void)colorNumber:(int )num withColor:(UIColor *)numColor;

-(void)colorString:(NSString *)str secString:(NSString *)str2 witColor:(UIColor *)stringColor;
@end
