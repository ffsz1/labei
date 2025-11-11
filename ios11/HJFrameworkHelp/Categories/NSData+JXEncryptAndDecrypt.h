//
//  NSData+JXEncryptAndDecrypt.h
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSData (JXEncryptAndDecrypt)

#pragma mark - AES
/**
 获取AES256加密后的data(key长度非法, 返回nil)
 
 @param key 密钥(16, 24, 32字节 <128, 192, 256位>)
 @return AES256加密后的data(key长度非法, 返回nil)
 */
- (NSData *)jx_AES256EncryptWithKey:(NSString *)key;

/**
 获取AES256加密后的data(key或iv长度非法, 返回nil)
 
 @param key 密钥(16, 24, 32字节 <128, 192, 256位>)
 @param iv  初始化向量(16字节<128位>, 可传nil)
 @return AES256加密后的data(key或iv长度非法, 返回nil)
 */
- (NSData *)jx_AES256EncryptWithKey:(NSData *)key iv:(NSData *)iv;

/**
 获取AES256解密后的data(key长度非法, 返回nil)
 
 @param key 密钥(16, 24, 32字节 <128, 192, 256位>)
 @return AES256解密后的data(key长度非法, 返回nil)
 */
- (NSData *)jx_AES256DecryptWithKey:(NSString *)key;

/**
 获取AES256解密后的data(key或iv长度非法, 返回nil)
 
 @param key 密钥(16, 24, 32字节 <128, 192, 256位>)
 @param iv  初始化向量(16字节<128位>, 可传nil)
 @return AES256解密后的data(key或iv长度非法, 返回nil)
 */
- (NSData *)jx_AES256DecryptWithKey:(NSData *)key iv:(NSData *)iv;

#pragma mark - DES
/**
 获取DES加密后的data(key长度非法, 返回nil)
 
 @param key 密钥(8字节 <64位>)
 @return DES加密后的data(key长度非法, 返回nil)
 */
- (NSData *)jx_DESEncryptWithKey:(NSString *)key;

/**
 获取DES加密后的data(key或iv长度非法, 返回nil)
 
 @param key 密钥(8字节 <64位>)
 @param iv  初始化向量(8字节 <64位>, 可传nil)
 @return DES加密后的data(key或iv长度非法, 返回nil)
 */
- (NSData *)jx_DESEncryptWithKey:(NSData *)key iv:(NSData * _Nullable)iv;

/**
 获取DES解密后的data(key长度非法, 返回nil)
 
 @param key 密钥(8字节 <64位>)
 @return DES解密后的data(key长度非法, 返回nil)
 */
- (NSData *)jx_DESDecryptWithKey:(NSString *)key;

/**
 获取DES解密后的data(key或iv长度非法, 返回nil)
 
 @param key 密钥(8字节 <64位>)
 @param iv  初始化向量(8字节 <64位>, 可传nil)
 @return DES解密后的data(key或iv长度非法, 返回nil)
 */
- (NSData *)jx_DESDecryptWithkey:(NSData *)key iv:(NSData * _Nullable)iv;


@end

NS_ASSUME_NONNULL_END
