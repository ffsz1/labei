//
//  NSString+JXCrypto.m
//  JXCategories
//
//  Created by Colin on 2019/1/25.
//

#import "NSString+JXCrypto.h"
#import "NSData+JXCrypto.h"

@implementation NSString (JXCrypto)

#pragma mark - Base64
- (NSString *)jx_base64EncodedString {
    return [[self dataUsingEncoding:NSUTF8StringEncoding] jx_base64EncodedString];
}

+ (NSString *)jx_stringWithBase64EncodedString:(NSString *)base64EncodedString {
    NSData *data = [NSData jx_dataWithBase64EncodedString:base64EncodedString];
    return [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
}

#pragma mark - MD2
- (NSString *)jx_MD2String {
    return [[self dataUsingEncoding:NSUTF8StringEncoding] jx_MD2String];
}

#pragma mark - MD4
- (NSString *)jx_MD4String {
    return [[self dataUsingEncoding:NSUTF8StringEncoding] jx_MD4String];
}

#pragma mark - MD5
- (NSString *)jx_MD5String {
    return [[self dataUsingEncoding:NSUTF8StringEncoding] jx_MD5String];
}

- (NSString *)jx_MD5StringWithKey:(NSString *)key {
    return [[self dataUsingEncoding:NSUTF8StringEncoding]
            jx_MD5StringWithKey:key];
}

#pragma mark - SHA1
- (NSString *)jx_SHA1String {
    return [[self dataUsingEncoding:NSUTF8StringEncoding] jx_SHA1String];
}

- (NSString *)jx_SHA1StringWithKey:(NSString *)key {
    return [[self dataUsingEncoding:NSUTF8StringEncoding]
            jx_SHA1StringWithKey:key];
}

#pragma mark - SHA224
- (NSString *)jx_SHA224String {
    return [[self dataUsingEncoding:NSUTF8StringEncoding] jx_SHA224String];
}

- (NSString *)jx_SHA224StringWithKey:(NSString *)key {
    return [[self dataUsingEncoding:NSUTF8StringEncoding]
            jx_SHA224StringWithKey:key];
}

#pragma mark - SHA256
- (NSString *)jx_SHA256String {
    return [[self dataUsingEncoding:NSUTF8StringEncoding] jx_SHA256String];
}

- (NSString *)jx_SHA256StringWithKey:(NSString *)key {
    return [[self dataUsingEncoding:NSUTF8StringEncoding]
            jx_SHA256StringWithKey:key];
}

#pragma mark - SHA384
- (NSString *)jx_SHA384String {
    return [[self dataUsingEncoding:NSUTF8StringEncoding] jx_SHA384String];
}

- (NSString *)jx_SHA384StringWithKey:(NSString *)key {
    return [[self dataUsingEncoding:NSUTF8StringEncoding]
            jx_SHA384StringWithKey:key];
}

#pragma mark - SHA512
- (NSString *)jx_SHA512String {
    return [[self dataUsingEncoding:NSUTF8StringEncoding] jx_SHA512String];
}

- (NSString *)jx_SHA512StringWithKey:(NSString *)key {
    return [[self dataUsingEncoding:NSUTF8StringEncoding]
            jx_SHA512StringWithKey:key];
}

#pragma mark - CRC32
- (NSString *)jx_crc32String {
    return [[self dataUsingEncoding:NSUTF8StringEncoding] jx_CRC32String];
}

@end
