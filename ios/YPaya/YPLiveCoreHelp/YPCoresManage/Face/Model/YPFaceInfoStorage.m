//
//  FaceInfoStorage.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#define kFileName @"FaceInfoList.data"
#define kDataKey @"faceInfos"
#import "YPFaceInfoStorage.h"
#import "YPFaceInfo.h"
#import "NSObject+YYModel.h"
#import "DESEncrypt.h"
#define EncodeKey @"1ea53d260ecf11e7b56e00163e046a26123"
//#import <JXKeychain/JXKeychain.h>
#import "JXKeychain.h"
#import "YPFaceConfigInfo.h"

@implementation YPFaceInfoStorage

+ (NSString *)getFilePath {
    NSArray *array =  NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *path = [[array objectAtIndex:0] stringByAppendingPathComponent:kFileName];
    if (![[NSFileManager defaultManager] fileExistsAtPath:path]) {
        [[NSFileManager defaultManager] createFileAtPath:path contents:nil attributes:nil];
    }
    return path;
}

+ (NSMutableArray *)getFaceInfos
{
//    NSData *data = [[NSData alloc] initWithContentsOfFile:[self getFilePath]];
//
//    NSKeyedUnarchiver *unarchiver = [[NSKeyedUnarchiver alloc] initForReadingWithData:data];
//    //解档出数据模型
    NSMutableArray *faceInfos = [NSMutableArray array];
//    NSString *str = [unarchiver decodeObjectForKey:kDataKey];
    NSString *encodeJson = [JXKeychain passwordForService:@"json" account:@"face"];
    NSString *decodeJson = [DESEncrypt decryptUseDES:encodeJson key:EncodeKey];
    if (decodeJson.length > 0) {
        faceInfos = [[NSArray yy_modelArrayWithClass:[YPFaceConfigInfo class] json:decodeJson] mutableCopy];
    }
    return faceInfos;
}

+ (void)saveFaceInfos:(NSString *)json {
    NSString *encodeJson = [DESEncrypt encryptUseDES:json key:EncodeKey];
    [JXKeychain setPassword:encodeJson forService:@"json" account:@"face"];
}

@end
