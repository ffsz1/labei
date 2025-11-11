//
//  DESEncrypt.h
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DESEncrypt : NSObject
//加密方法
+(NSString *) encryptUseDES:(NSString *)plainText key:(NSString *)key;
//解密方法
+(NSString *) decryptUseDES:(NSString *)cipherText key:(NSString *)key;
@end
