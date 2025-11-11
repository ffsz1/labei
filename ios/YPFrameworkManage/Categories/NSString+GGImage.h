//
//  NSString+GGImage.h
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSString (GGImage)

+ (NSString *)getLevelImageName:(NSInteger)level;
//财富等级
+ (NSString *)getMoneyLevelImageName:(NSInteger)level;
//财富等级文字
+ (NSString *)getMoneyLevelText:(NSInteger)level;
//魅力等级
+ (NSString *)getCharmLevelImageName:(NSInteger)level;
//魅力等级文字
+ (NSString *)getCharmLevelText:(NSInteger)level;

@end

NS_ASSUME_NONNULL_END
