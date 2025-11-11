//
//  YPJXLogManager.m
//  XChat
//
//  Created by apple on 2019/4/16.
//  Copyright © 2019 XC. All rights reserved.
//

#import "YPJXLogManager.h"

#import "ZipArchive.h"
#import "HJNotificationCoreClient.h"
#import "YPAttachment.h"
#import "NSObject+YYModel.h"
#import "YPFileCore.h"
#import "HttpRequestHelper+Log.h"

// 日志保留最大天数
static const int LogMaxSaveDay = 15;
// 日志文件保存目录
static const NSString* LogFilePath = @"/Documents/JXLog/";

@interface YPJXLogManager ()<HJNotificationCoreClient>

// 日期格式化
@property (nonatomic, strong) NSDateFormatter *dateFormatter;
// 时间格式化
@property (nonatomic, strong) NSDateFormatter *timeFormatter;

// 日志的目录路径
@property (nonatomic, copy) NSString *basePath;

@end

@implementation YPJXLogManager

/**
 *  获取单例实例
 *
 *  @return 单例实例
 */
+ (instancetype) sharedInstance{
    
    static YPJXLogManager* instance = nil;
    
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        if (!instance) {
            instance = [[YPJXLogManager alloc]init];
        }
    });
    
    return instance;
}


// 获取当前时间
+ (NSDate*)getCurrDate{
    
    NSDate *date = [NSDate date];
    NSTimeZone *zone = [NSTimeZone systemTimeZone];
    NSInteger interval = [zone secondsFromGMTForDate: date];
    NSDate *localeDate = [date dateByAddingTimeInterval: interval];
    
    return localeDate;
}

#pragma mark - Init

- (instancetype)init{
    
    self = [super init];
    if (self) {
        
        AddCoreClient(HJNotificationCoreClient, self);
        
        // 创建日期格式化
        NSDateFormatter* dateFormatter = [[NSDateFormatter alloc]init];
        [dateFormatter setDateFormat:@"yyyy-MM-dd"];
        // 设置时区，解决8小时
        [dateFormatter setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"UTC"]];
        self.dateFormatter = dateFormatter;
        
        // 创建时间格式化
        NSDateFormatter* timeFormatter = [[NSDateFormatter alloc]init];
        [timeFormatter setDateFormat:@"HH:mm:ss"];
        [timeFormatter setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"UTC"]];
        self.timeFormatter = timeFormatter;
        
        // 日志的目录路径
        self.basePath = [NSString stringWithFormat:@"%@%@",NSHomeDirectory(),LogFilePath];
    }
    return self;
}

- (void)dealloc {
    RemoveCoreClientAll(self);
}


#pragma mark - Method

/**
 *  写入日志
 *
 *  @param module 模块名称
 *  @param logStr 日志信息,动态参数
 */
- (void)logInfo:(NSString*)module logStr:(NSString*)logStr, ...{
    
#pragma mark - 获取参数
    
    NSMutableString* parmaStr = [NSMutableString string];
    // 声明一个参数指针
    va_list paramList;
    // 获取参数地址，将paramList指向logStr
    va_start(paramList, logStr);
    id arg = logStr;
    
    @try {
        // 遍历参数列表
        while (arg) {
            [parmaStr appendString:arg];
            // 指向下一个参数，后面是参数类似
            arg = va_arg(paramList, NSString*);
        }
        
    } @catch (NSException *exception) {
        
        [parmaStr appendString:@"【记录日志异常】"];
    } @finally {
        
        // 将参数列表指针置空
        va_end(paramList);
    }
    
    // 获取当前日期做为文件名
    NSString* fileName = [NSString stringWithFormat:@"%@.log",[self.dateFormatter stringFromDate:[NSDate date]]];
    NSString* filePath = [NSString stringWithFormat:@"%@%@",self.basePath,fileName];
    
    // [时间]-[模块]-日志内容
    NSString* timeStr = [self.timeFormatter stringFromDate:[YPJXLogManager getCurrDate]];
    NSString* writeStr = [NSString stringWithFormat:@"[%@]-[%@]-[%@]-%@\n",timeStr,module,[GetCore(YPAuthCoreHelp) getUid],parmaStr];
    
    // 写入数据
    [self writeFile:filePath stringData:writeStr];
    
    NSLog(@"写入日志:%@",filePath);
}

/**
 *  清空过期的日志
 */
- (void)clearExpiredLog{
    
    // 获取日志目录下的所有文件
    NSArray* files = [[NSFileManager defaultManager] contentsOfDirectoryAtPath:self.basePath error:nil];
    for (NSString* file in files) {
        
        NSDate* date = [self.dateFormatter dateFromString:[file stringByReplacingOccurrencesOfString:@".log" withString:@""]];
        if (date) {
            NSTimeInterval oldTime = [date timeIntervalSince1970];
            NSTimeInterval currTime = [[YPJXLogManager getCurrDate] timeIntervalSince1970];
            
            NSTimeInterval second = currTime - oldTime;
            int day = (int)second / (24 * 3600);
            if (day >= LogMaxSaveDay) {
                // 删除该文件
                [[NSFileManager defaultManager] removeItemAtPath:[NSString stringWithFormat:@"%@/%@",self.basePath,file] error:nil];
                NSLog(@"[%@]日志文件已被删除！",file);
            }
        }
    }
    
    
}

#pragma mark - Private

/**
 *  处理是否需要上传日志
 *
 *  @param resultDic 包含获取日期的字典
 */
- (void)uploadLog:(NSDictionary*)resultDic{
    
    if (!resultDic) {
        return;
    }
    
    // 0不拉取，1拉取N天，2拉取全部
    int type = [resultDic[@"type"] intValue];
    // 压缩文件是否创建成功
    NSString *zipNmae = nil;
    if (type == 1) {
        // 拉取指定日期的
        
        // "dates": ["2017-03-01", "2017-03-11"]
        NSArray* dates = resultDic[@"dates"];
        
        // 压缩日志
        zipNmae = [self compressLog:dates];
    }else if(type == 2){
        // 拉取全部
        
        // 压缩日志
        zipNmae = [self compressLog:nil];
    }
    
    if (zipNmae.length) {
        // 上传
        [self uploadLogToServerWithZipFileName:zipNmae];
    }
}

/**
 *  压缩日志
 *
 *  @param dates 日期时间段，空代表全部
 *
 *  @return 执行结果
 */
- (NSString *)compressLog:(NSArray*)dates{
    
    // 先清理几天前的日志
    [self clearExpiredLog];
    
    // 获取日志目录下的所有文件
    NSArray* files = [[NSFileManager defaultManager] contentsOfDirectoryAtPath:self.basePath error:nil];
    // 压缩包文件路径
    NSDate *datenow = [NSDate date];
    NSString *timeSp = [NSString stringWithFormat:@"%ld", (long)([datenow timeIntervalSince1970]*1000)];
    NSString *zipFileName = [NSString stringWithFormat:@"%@.zip",timeSp];
    NSString * zipFile = [self.basePath stringByAppendingString:zipFileName] ;
    
    ZipArchive* zip = [[ZipArchive alloc] init];
    // 创建一个zip包
    BOOL created = [zip CreateZipFile2:zipFile];
    if (!created) {
        // 关闭文件
        [zip CloseZipFile2];
        return nil;
    }
    
    if (dates) {
        // 拉取指定日期的
        for (NSString* fileName in files) {
            if ([dates containsObject:fileName]) {
                // 将要被压缩的文件
                NSString *file = [self.basePath stringByAppendingString:fileName];
                // 判断文件是否存在
                if ([[NSFileManager defaultManager] fileExistsAtPath:file]) {
                    // 将日志添加到zip包中
                    [zip addFileToZip:file newname:fileName];
                }
            }
        }
    }else{
        // 全部
        for (NSString* fileName in files) {
            // 将要被压缩的文件
            NSString *file = [self.basePath stringByAppendingString:fileName];
            // 判断文件是否存在
            if ([[NSFileManager defaultManager] fileExistsAtPath:file]) {
                // 将日志添加到zip包中
                [zip addFileToZip:file newname:fileName];
            }
        }
    }
    
    NSString *documentPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) lastObject];
    
    // 添加云信日志
    NSString *nimFileName = @"NIMSDK";
    NSString *nimFilePath = [documentPath stringByAppendingPathComponent:nimFileName];
    if ([[NSFileManager defaultManager] fileExistsAtPath:nimFilePath]) {
        NSDirectoryEnumerator *myDirectoryEnumerator = [[NSFileManager defaultManager] enumeratorAtPath:nimFilePath];

        BOOL isDir = NO;
        BOOL isExist = NO;

        //列举目录内容，可以遍历子目录
        for (NSString *path in myDirectoryEnumerator.allObjects) {
            isExist = [[NSFileManager defaultManager] fileExistsAtPath:[NSString stringWithFormat:@"%@/%@", nimFilePath, path] isDirectory:&isDir];
            if (isDir) {
                NSLog(@"%@", path);    // 目录路径
            } else {
                NSLog(@"%@", path);    // 文件路径
                [zip addFileToZip:[NSString stringWithFormat:@"%@/%@",nimFilePath,path] newname:[NSString stringWithFormat:@"%@/%@",nimFileName,path]];
            }
        }
    }
    
    // 添加声网日志
    NSString *agoraFileName = @"Agorasdk.log";
    NSString *agoraFilePath = [documentPath stringByAppendingPathComponent:agoraFileName];
    if ([[NSFileManager defaultManager] fileExistsAtPath:agoraFilePath]) {
        [zip addFileToZip:agoraFilePath newname:agoraFileName];
    }
    
    // 关闭文件
    [zip CloseZipFile2];
    return zipFileName;
}

/**
 *  上传日志到服务器
 */
- (void)uploadLogToServerWithZipFileName:(NSString *)zipFileName {
    
    @weakify(self);
    NSString* zipFilePath = [self.basePath stringByAppendingString:zipFileName];
    [GetCore(YPFileCore) uploadFileWithPath:zipFilePath fileName:zipFileName success:^(NSString *url) {
        [YPHttpRequestHelper clientLogSaveWithUrl:url success:^{
            @strongify(self);
            [self deleteZipFileWithZipFileName:zipFileName];
        } failure:^(NSNumber * _Nonnull code, NSString * _Nonnull msg) {
            @strongify(self);
            [self deleteZipFileWithZipFileName:zipFileName];
        }];
    } failure:^(NSNumber *resCode, NSString *message) {
    }];
}

/**
 *  删除日志压缩文件
 */
- (void)deleteZipFileWithZipFileName:(NSString *)zipFileName {
    
    NSString* zipFilePath = [self.basePath stringByAppendingString:zipFileName];
    if ([[NSFileManager defaultManager] fileExistsAtPath:zipFilePath]) {
        [[NSFileManager defaultManager] removeItemAtPath:zipFilePath error:nil];
    }
}

/**
 *  写入字符串到指定文件，默认追加内容
 *
 *  @param filePath   文件路径
 *  @param stringData 待写入的字符串
 */
- (void)writeFile:(NSString*)filePath stringData:(NSString*)stringData{
    
    // 待写入的数据
    NSData* writeData = [stringData dataUsingEncoding:NSUTF8StringEncoding];
    
    // NSFileManager 用于处理文件
    BOOL createPathOk = YES;
    if (![[NSFileManager defaultManager] fileExistsAtPath:[filePath stringByDeletingLastPathComponent] isDirectory:&createPathOk]) {
        // 目录不存先创建
        [[NSFileManager defaultManager] createDirectoryAtPath:[filePath stringByDeletingLastPathComponent] withIntermediateDirectories:YES attributes:nil error:nil];
    }
    if(![[NSFileManager defaultManager] fileExistsAtPath:filePath]){
        // 文件不存在，直接创建文件并写入
        [writeData writeToFile:filePath atomically:NO];
    }else{
        
        // NSFileHandle 用于处理文件内容
        // 读取文件到上下文，并且是更新模式
        NSFileHandle* fileHandler = [NSFileHandle fileHandleForUpdatingAtPath:filePath];
        
        // 跳到文件末尾
        [fileHandler seekToEndOfFile];
        
        // 追加数据
        [fileHandler writeData:writeData];
        
        // 关闭文件
        [fileHandler closeFile];
    }
}

- (void)uploadLogWithDays:(NSInteger)days {
    
    NSMutableDictionary *dic = [NSMutableDictionary dictionary];
    if (days >= LogMaxSaveDay || days <= 0) {
        dic[@"type"] = @2;
    }
    else {
        dic[@"type"] = @1;
        dic[@"dates"] = [self getDatesWithStart:[self getTimeAfterNowWithDay:days - 1] end:[NSDate date]];
    }
    
    [self uploadLog:dic];
}

- (NSArray*)getDatesWithStart:(NSDate *)start end:(NSDate *)end {
    
    NSCalendar *calendar = [[NSCalendar alloc] initWithCalendarIdentifier: NSCalendarIdentifierGregorian];
    
    //字符串转时间
    NSDateFormatter *matter = [[NSDateFormatter alloc] init];
    matter.dateFormat = @"yyyy-MM-dd";
    
    NSMutableArray *componentAarray = [NSMutableArray array];
    NSComparisonResult result = [start compare:end];
    NSDateComponents *comps;
    while (result != NSOrderedDescending) {
        comps = [calendar components:NSCalendarUnitYear | NSCalendarUnitMonth | NSCalendarUnitDay |  NSCalendarUnitWeekday fromDate:start];
        [componentAarray addObject:[NSString stringWithFormat:@"%@.log",[matter stringFromDate:start]]];
        
        //后一天
        [comps setDay:([comps day]+1)];
        start = [calendar dateFromComponents:comps];
        
        //对比日期大小
        result = [start compare:end];
    }
    return componentAarray;
}

- (NSDate *)getTimeAfterNowWithDay:(NSInteger)day
{
    NSDate *nowDate = [NSDate date];
    NSDate *theDate;
    
    if(day!=0)
    {
        NSTimeInterval  oneDay = 24*60*60*1;  //1天的长度
        theDate = [nowDate initWithTimeIntervalSinceNow: -oneDay*day ];
    }
    else
    {
        theDate = nowDate;
    }
    return theDate;
}

#pragma mark - ImMessageCoreClient
- (void)onRecvCustomP2PNoti:(NIMCustomSystemNotification *)notification {
    YPAttachment *attachment = [YPAttachment yy_modelWithJSON:notification.content];
    if (attachment != nil && [attachment isKindOfClass:[YPAttachment class]]) {
        if (attachment.first == Custom_Noti_Header_UploadDaily && attachment.second == Custom_Noti_Header_UploadDaily) {
            NSDictionary *dic = attachment.data;
            if ([dic isKindOfClass:[NSDictionary class]]) {
                NSInteger day = [dic[@"day"] integerValue];

                if (day <= 0 || day >= LogMaxSaveDay) {
                    // 拉取全部
                    [self uploadLogWithDays:LogMaxSaveDay];
                }
                else {
                    // 拉取指定天数
                    [self uploadLogWithDays:day];
                }
            }
            else {
                // 拉取全部
                [self uploadLogWithDays:LogMaxSaveDay];
            }
        }
    }
}

@end
