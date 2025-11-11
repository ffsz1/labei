//
//  NSString+HMLifeRegular.h
//  HealthMall
//
//  Created by LiGuiZhi on 2017/8/14.
//  Copyright © 2017年 HealthMall. All rights reserved.
//

#import <Foundation/Foundation.h>


/**
 正则表达示/验证
 */
@interface NSString (HMLifeRegular)


/**
 纯数字 0~9
 */
- (BOOL)isValidateNumber;

/**
 验证中文字符
 */
- (BOOL)isChineseCharacter;

/**
 手机号码验证，11位
 */
- (BOOL)isPhoneNumber;

//正则匹配用户身份证号15或18位
-(BOOL)isValidateIDCard;

@end
