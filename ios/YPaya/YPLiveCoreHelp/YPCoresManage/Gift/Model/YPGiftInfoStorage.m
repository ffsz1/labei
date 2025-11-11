//
//  YPGiftInfoStorage.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
#define kFileName @"GiftInfoList.data"
#define kMysticFileName @"MysticGiftInfoList.data"
#define kDiandianCoinFileName @"DiandianGiftInfoList.data"
#define kDataKey @"giftInfos"
#import "YPGiftInfoStorage.h"
#import "YPGiftInfo.h"
#import "NSObject+YYModel.h"
#import <UIImageView+WebCache.h>



@implementation YPGiftInfoStorage

+(NSString *)getFilePathWithtype:(GiftType)type {
    
    NSArray *array =  NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *fileName = nil;
    switch (type) {
        case GiftTypeNormal:
            fileName = kFileName;
            break;
        case GiftTypeMystic:
            fileName = kMysticFileName;
            break;
        case GiftTypeDiandianCoin:
            fileName = kDiandianCoinFileName;
            break;
        default:
            fileName = kFileName;
            break;
    }
    NSString *path = [[array objectAtIndex:0] stringByAppendingPathComponent:fileName];
    if (![[NSFileManager defaultManager] fileExistsAtPath:path]) {
        [[NSFileManager defaultManager] createFileAtPath:path contents:nil attributes:nil];
    }
    return path;
}

+ (NSMutableArray *)getGiftInfosWithtype:(GiftType)type
{
    NSData *data = [[NSData alloc] initWithContentsOfFile:[self getFilePathWithtype:type]];
    
    NSKeyedUnarchiver *unarchiver = [[NSKeyedUnarchiver alloc] initForReadingWithData:data];
    //解档出数据模型
    NSMutableArray *giftInfos = [NSMutableArray array];
    NSString *str = [unarchiver decodeObjectForKey:kDataKey];
    if (str.length > 0) {
        giftInfos = [[NSArray yy_modelArrayWithClass:[YPGiftInfo class] json:str] mutableCopy];
    }
    [unarchiver finishDecoding];//一定不要忘记finishDecoding，否则会报错
    return giftInfos;
}

+ (void)saveGiftInfos:(NSString *)json type:(GiftType)type
{
    NSMutableData *data = [[NSMutableData alloc] init];
    NSKeyedArchiver *archiver = [[NSKeyedArchiver alloc] initForWritingWithMutableData:data];
    [archiver encodeObject:json forKey:kDataKey];
    [archiver finishEncoding];
    [data writeToFile:[self getFilePathWithtype:type] atomically:YES];
}
@end
