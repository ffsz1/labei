//
//  HJTicketListInfoStorage.m
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJTicketListInfoStorage.h"
//
#define kFileName @"TicketListInfo.data"
#define kDataKey @"ticketListInfo"
#import "HJTicketListInfoStorage.h"

@implementation HJTicketListInfoStorage
+(NSString *) getFilePath{
    
    NSArray *array =  NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *path = [[array objectAtIndex:0] stringByAppendingPathComponent:kFileName];
    if (![[NSFileManager defaultManager] fileExistsAtPath:path]) {
        [[NSFileManager defaultManager] createFileAtPath:path contents:nil attributes:nil];
    }
    return path;
}

+ (TicketListInfo *)getCurrentTicketListInfo
{
    NSData *data = [[NSData alloc] initWithContentsOfFile:[self getFilePath]];
    
    NSKeyedUnarchiver *unarchiver = [[NSKeyedUnarchiver alloc] initForReadingWithData:data];
    //解档出数据模型
    TicketListInfo *info = [unarchiver decodeObjectForKey:kDataKey];
    [unarchiver finishDecoding];//一定不要忘记finishDecoding，否则会报错
    return info;
}

+ (void) saveTicketListInfo:(TicketListInfo *)ticketListInfo
{
    if (ticketListInfo == nil) {
        ticketListInfo = [[TicketListInfo alloc] init];
    }
    
    NSMutableData *data = [[NSMutableData alloc] init];
    NSKeyedArchiver *archiver = [[NSKeyedArchiver alloc] initForWritingWithMutableData:data];
    [archiver encodeObject:ticketListInfo forKey:kDataKey];
    [archiver finishEncoding];
    [data writeToFile:[self getFilePath] atomically:YES];
}

@end
