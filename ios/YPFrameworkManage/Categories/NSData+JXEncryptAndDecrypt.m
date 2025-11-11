//
//  NSData+JXEncryptAndDecrypt.m
//  HJLive
//
//  Created by FF on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "NSData+JXEncryptAndDecrypt.h"
#include <CommonCrypto/CommonCrypto.h>

@implementation NSData (JXEncryptAndDecrypt)

#pragma mark - AES
- (NSData *)jx_AES256EncryptWithKey:(NSString *)key {
    return [self jx_AES256EncryptWithKey:[key dataUsingEncoding:NSUTF8StringEncoding] iv:nil];
}

- (NSData *)jx_AES256EncryptWithKey:(NSData *)key iv:(NSData *)iv {
    if (key.length != 16 && key.length != 24 && key.length != 32) return nil;
    if (iv.length != 16 && iv.length != 0) return nil;
    
    NSData *result = nil;
    size_t bufferSize = self.length + kCCBlockSizeAES128;
    void *buffer = malloc(bufferSize);
    if (!buffer) return nil;
    size_t encryptedSize = 0;
    CCCryptorStatus cryptStatus = CCCrypt(kCCEncrypt,
                                          kCCAlgorithmAES128,
                                          kCCOptionPKCS7Padding,
                                          key.bytes,
                                          key.length,
                                          iv.bytes,
                                          self.bytes,
                                          self.length,
                                          buffer,
                                          bufferSize,
                                          &encryptedSize);
    if (cryptStatus == kCCSuccess) {
        result = [[NSData alloc] initWithBytes:buffer length:encryptedSize];
        free(buffer);
        return result;
    } else {
        free(buffer);
        return nil;
    }
}

- (NSData *)jx_AES256DecryptWithKey:(NSString *)key {
    return [self jx_AES256DecryptWithKey:[key dataUsingEncoding:NSUTF8StringEncoding] iv:nil];
}

- (NSData *)jx_AES256DecryptWithKey:(NSData *)key iv:(NSData *)iv {
    if (key.length != 16 && key.length != 24 && key.length != 32) return nil;
    if (iv.length != 16 && iv.length != 0) return nil;
    
    NSData *result = nil;
    size_t bufferSize = self.length + kCCBlockSizeAES128;
    void *buffer = malloc(bufferSize);
    if (!buffer) return nil;
    size_t decryptedSize = 0;
    CCCryptorStatus cryptStatus = CCCrypt(kCCDecrypt,
                                          kCCAlgorithmAES128,
                                          kCCOptionPKCS7Padding,
                                          key.bytes,
                                          key.length,
                                          iv.bytes,
                                          self.bytes,
                                          self.length,
                                          buffer,
                                          bufferSize,
                                          &decryptedSize);
    if (cryptStatus == kCCSuccess) {
        result = [[NSData alloc] initWithBytes:buffer length:decryptedSize];
        free(buffer);
        return result;
    } else {
        free(buffer);
        return nil;
    }
}

#pragma mark - DES
- (NSData *)jx_DESEncryptWithKey:(NSString *)key {
    return [self jx_DESEncryptWithKey:[key dataUsingEncoding:NSUTF8StringEncoding] iv:nil];
}

- (NSData *)jx_DESEncryptWithKey:(NSData *)key iv:(NSData * _Nullable)iv {
    if (key.length != 8) return nil;
    if (iv.length != 8 && iv.length != 0) return nil;
    
    NSData *result = nil;
    size_t bufferSize = self.length + kCCBlockSizeDES;
    void *buffer = malloc(bufferSize);
    if (!buffer) return nil;
    size_t encryptedSize = 0;
    CCCryptorStatus cryptStatus = CCCrypt(kCCEncrypt,
                                          kCCAlgorithmDES,
                                          kCCOptionPKCS7Padding|kCCOptionECBMode,
                                          key.bytes,
                                          key.length,
                                          iv.bytes,
                                          self.bytes,
                                          self.length,
                                          buffer,
                                          bufferSize,
                                          &encryptedSize);
    if (cryptStatus == kCCSuccess) {
        result = [[NSData alloc] initWithBytes:buffer length:encryptedSize];
        free(buffer);
        return result;
    } else {
        free(buffer);
        return nil;
    }
}

- (NSData *)jx_DESDecryptWithKey:(NSString *)key {
    return [self jx_DESDecryptWithkey:[key dataUsingEncoding:NSUTF8StringEncoding] iv:nil];
}

- (NSData *)jx_DESDecryptWithkey:(NSData *)key iv:(NSData * _Nullable)iv {
    if (key.length != 8) return nil;
    if (iv.length != 8 && iv.length != 0) return nil;
    
    NSData *result = nil;
    size_t bufferSize = self.length + kCCBlockSizeDES;
    void *buffer = malloc(bufferSize);
    if (!buffer) return nil;
    size_t decryptedSize = 0;
    CCCryptorStatus cryptStatus = CCCrypt(kCCDecrypt,
                                          kCCAlgorithmDES,
                                          kCCOptionPKCS7Padding|kCCOptionECBMode,
                                          key.bytes,
                                          key.length,
                                          iv.bytes,
                                          self.bytes,
                                          self.length,
                                          buffer,
                                          bufferSize,
                                          &decryptedSize);
    if (cryptStatus == kCCSuccess) {
        result = [[NSData alloc] initWithBytes:buffer length:decryptedSize];
        free(buffer);
        return result;
    } else {
        free(buffer);
        return nil;
    }
}

@end
