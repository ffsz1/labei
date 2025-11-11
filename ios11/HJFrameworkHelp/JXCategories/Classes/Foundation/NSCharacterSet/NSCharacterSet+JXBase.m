//
//  NSCharacterSet+JXBase.m
//  JXCategories
//
//  Created by Colin on 2019/2/1.
//

#import "NSCharacterSet+JXBase.h"

@implementation NSCharacterSet (JXBase)

#pragma mark - Base
+ (NSCharacterSet *)jx_URLQueryUserInputAllowedCharacterSet {
    NSMutableCharacterSet *characterSet = [NSMutableCharacterSet jx_URLQueryUserInputAllowedCharacterSet];
    return characterSet.copy;
}

@end


@implementation NSMutableCharacterSet (JXBase)

#pragma mark - Base
+ (NSMutableCharacterSet *)jx_URLQueryUserInputAllowedCharacterSet {
    NSMutableCharacterSet *characterSet = [NSCharacterSet URLQueryAllowedCharacterSet].mutableCopy;
    [characterSet removeCharactersInString:@"#&="];
    return characterSet;
}

@end
