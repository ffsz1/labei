//
//  HJImageUploader.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJImageUploader.h"
#import "GTMBase64.h"
#include <CommonCrypto/CommonDigest.h>
#include <CommonCrypto/CommonHMAC.h>
#import "QNUploadManager.h"
#import "QNConfiguration.h"
#import "QNResponseInfo.h"
@implementation HJImageUploader
+ (void)uploadImage:(UIImage *)image succeed:(HJImageUploaderSucceed)succeedBlock  failed:(HJImageUploaderFailed)failedBlock
{
    NSString *token = [self token];
    
    QNConfiguration *config = [QNConfiguration build:^(QNConfigurationBuilder *builder) {
        builder.zone = [QNFixedZone zone2];
    }];
    QNUploadManager *upManager = [[QNUploadManager alloc] initWithConfiguration:config];
    NSData *data = UIImageJPEGRepresentation(image, 0.5);
    [upManager putData:data key:nil token:token complete:^(QNResponseInfo *info, NSString *key, NSDictionary *resp) {
        
        NSLog(@"info %@",info);
        NSLog(@"resp %@",resp);
        NSLog(@"key %@",key);
        
        if (resp) {
            NSString *url = [NSString stringWithFormat:@"%@/%@?imageslim",JX_IMAGE_HOST_URL,resp[@"key"]];
            
            if (succeedBlock) {
                succeedBlock(url);
            }
            
        }else{
            
            if (failedBlock) {
                failedBlock(info.statusCode,info.error.localizedDescription);
            }
            
        }
    } option:nil];

}

+ (NSString *)token{
    NSMutableDictionary *authInfo = [NSMutableDictionary dictionary];
    
    [authInfo setObject:JX_QN_BUCKET_NAME forKey:@"scope"];
    [authInfo
     setObject:[NSNumber numberWithLong:[[NSDate date] timeIntervalSince1970] +  24 * 3600]
     forKey:@"deadline"];
    NSData *jsonData =
    [NSJSONSerialization dataWithJSONObject:authInfo options:NSJSONWritingPrettyPrinted error:nil];
    NSString *encodedString = [HJImageUploader urlSafeBase64Encode:jsonData];
    
    NSString *encodedSignedString = [HJImageUploader HMACSHA1:JX_QN_SK_KEY text:encodedString];
    
    NSString *token = [NSString stringWithFormat:@"%@:%@:%@", JX_QN_AK_KEY, encodedSignedString, encodedString];
    return token;
}

+ (NSString *)urlSafeBase64Encode:(NSData *)text {
    
    NSString *base64 =
    [[NSString alloc] initWithData:[GTMBase64 encodeData:text] encoding:NSUTF8StringEncoding];
    base64 = [base64 stringByReplacingOccurrencesOfString:@"+" withString:@"-"];
    base64 = [base64 stringByReplacingOccurrencesOfString:@"/" withString:@"_"];
    return base64;
}



+ (NSString *)HMACSHA1:(NSString *)key text:(NSString *)text {
    
    const char *cKey = [key cStringUsingEncoding:NSUTF8StringEncoding];
    const char *cData = [text cStringUsingEncoding:NSUTF8StringEncoding];
    char cHMAC[CC_SHA1_DIGEST_LENGTH];
    CCHmac(kCCHmacAlgSHA1, cKey, strlen(cKey), cData, strlen(cData), cHMAC);
    NSData *HMAC = [[NSData alloc] initWithBytes:cHMAC length:CC_SHA1_DIGEST_LENGTH];
    NSString *hash = [self urlSafeBase64Encode:HMAC];
    return hash;
}

@end
