//
//  YPTicketListInfoStorage.m
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPTicketListInfoStorage.h"
//
#define kFileName @"YPTicketListInfo.data"
#define kDataKey @"ticketListInfo"
#import "YPTicketListInfoStorage.h"

@implementation YPTicketListInfoStorage
+(NSString *) getFilePath{
    
    NSArray *array =  NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *path = [[array objectAtIndex:0] stringByAppendingPathComponent:kFileName];
    if (![[NSFileManager defaultManager] fileExistsAtPath:path]) {
        [[NSFileManager defaultManager] createFileAtPath:path contents:nil attributes:nil];
    }
    return path;
}

+ (YPTicketListInfo *)getCurrentTicketListInfo
{
    NSData *data = [[NSData alloc] initWithContentsOfFile:[self getFilePath]];
    
    NSKeyedUnarchiver *unarchiver = [[NSKeyedUnarchiver alloc] initForReadingWithData:data];
    //解档出数据模型
    YPTicketListInfo *info = [unarchiver decodeObjectForKey:kDataKey];
    [unarchiver finishDecoding];//一定不要忘记finishDecoding，否则会报错
    return info;
}

+ (void) saveTicketListInfo:(YPTicketListInfo *)ticketListInfo
{
    if (ticketListInfo == nil) {
        ticketListInfo = [[YPTicketListInfo alloc] init];
    }
    
    NSMutableData *data = [[NSMutableData alloc] init];
    NSKeyedArchiver *archiver = [[NSKeyedArchiver alloc] initForWritingWithMutableData:data];
    [archiver encodeObject:ticketListInfo forKey:kDataKey];
    [archiver finishEncoding];
    [data writeToFile:[self getFilePath] atomically:YES];
}

@end
