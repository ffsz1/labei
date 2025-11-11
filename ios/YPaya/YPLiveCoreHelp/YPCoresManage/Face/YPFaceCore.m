//
//  YPFaceCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPFaceCore.h"
#import "YPHttpRequestHelper+Face.h"
#import "YPFaceInfoStorage.h"
#import "YPFaceReceiveInfo.h"
#import "YPFaceSendInfo.h"
#import "YPAttachment.h"
#import "NSObject+YYModel.h"
//#import "RoomQueueCore.h"
#import "YPFaceInfo.h"
#import "YPChatRoomInfo.h"
#import "YPImRoomCoreV2.h"
#import "YPUserCoreHelp.h"
#import "YPAuthCoreHelp.h"
#import "YPImMessageCore.h"
#import "HJImMessageCoreClient.h"
#import "YPFacePlayInfo.h"
#import "HJFaceCoreClient.h"
#import "YPFaceConfigInfo.h"
#import "YYWebResourceDownloader.h"
#import "CommonFileUtils.h"
#import <SSZipArchive.h>
#import "HJFaceSourceClient.h"
#import "HJAuthCoreClient.h"
#import "HJImMessageSendCoreClient.h"

@interface YPFaceCore()
<
HJImMessageCoreClient,
HJAuthCoreClient,
SSZipArchiveDelegate
>

#define RETRYCOUNT 5;

@property(nonatomic, strong)NSMutableArray *faceInfos;
@property(nonatomic, strong)NSMutableArray *tempFaceInfos; //获取下来的新数据，需要等新的zip包下载完才会去复制到faceInfos
@property (nonatomic ,strong)dispatch_source_t timer;
@property (nonatomic ,strong)dispatch_source_t testTimer; //测试用定时器
@property(atomic, strong)NSMutableArray *receiveFace;
@property (strong, nonatomic) NSCache *faceCache;
@property (nonatomic, assign) NSInteger *requestJsonCount; //请求json重试计数器

@end

@implementation YPFaceCore
{
    dispatch_queue_t zipQueue;
}

- (instancetype)init {
    self = [super init];
    if (self) {
        AddCoreClient(HJImMessageCoreClient, self);
        AddCoreClient(HJAuthCoreClient, self);
        AddCoreClient(HJImMessageSendCoreClient, self);
        zipQueue = dispatch_queue_create("com.yy.face.xcface.unzipFace", DISPATCH_QUEUE_SERIAL);
        _faceInfos = [YPFaceInfoStorage getFaceInfos];
        [self requestFaceJson];
    }
    return self;
}

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)requestFaceJson {
    @weakify(self);
    self.isLoadFace = NO;
    self.requestJsonCount += 1;
    [YPHttpRequestHelper getTheFaceJson:^(NSArray *faceJsonList) {
        @strongify(self);
        self.tempFaceInfos = [self sortFaceInfosWithfaceInfoArr:[faceJsonList mutableCopy]];
        [self faceDownloadManager];
        NotifyCoreClient(HJFaceCoreClient, @selector(onGetFaceJsonSuccess), onGetFaceJsonSuccess);
    } failure:^(NSNumber *resCode, NSString *message) {
        @strongify(self);
        if ((int)self.requestJsonCount < 10) {
            [self performSelector:@selector(requestFaceJson) withObject:nil afterDelay:3];
        }
    }];
}

- (void)faceDownloadManager {
    NSString *version = [[NSUserDefaults standardUserDefaults]objectForKey:@"faceJsonVersion"];
    if (version) {
        if ([version integerValue] < [self.version integerValue]) {
            [self downloadZipForFace];
            return;
        }else {
            YPFaceConfigInfo *info = nil;
            NSArray *faces = [self getFaceInfos];
            if (faces.count) {
                NSMutableArray<YPFaceConfigInfo *> *faceInfos = [faces firstObject];
                if (faceInfos.count) {
                    info = [faceInfos firstObject];
                }
            }
            
            if (info) {
                self.destinationUrl = [self getDestinationUrlStr];
                UIImage *image = [self findFaceIconImageById:info.id];
                if (image) {
                    self.isLoadFace = YES;
                    [self syncTheTmpJsonToNormalJson];
                    NotifyCoreClient(HJFaceSourceClient, @selector(loadFaceSourceSuccess), loadFaceSourceSuccess);
                }
                else {
                    [self downloadZipForFace];
                }
            }
            else {
                [self performSelector:@selector(requestFaceJson) withObject:nil afterDelay:3];
            }
            
        }
    }else {
        [self downloadZipForFace];
    }

    
}

- (void)syncTheTmpJsonToNormalJson {
    self.faceInfos = [self.tempFaceInfos mutableCopy];
    [YPFaceInfoStorage saveFaceInfos:[self.faceInfos yy_modelToJSONString]];
}

- (void)downloadZipForFace {
    if ([self.zipUrl absoluteString].length > 0 && [[self.zipUrl absoluteString] containsString:@"http"]) {
        @weakify(self);
        [[YYWebResourceDownloader sharedDownloader] downloadWithURL:self.zipUrl fileName:@"face.zip" options:(YYWebResourceDownloaderOptions)YYWebResourceDownloaderProgressiveDownload progress:^(int64_t received, int64_t expected, CGFloat progress) {
            
        } completion:^(NSURL *filePath, NSError *error, BOOL finished) {

            @strongify(self);
            if (error == nil) {
                NSString *desPath = [self getFaceImagePath];
                [CommonFileUtils createDirForPath:desPath];
                NSString *filePathStr = [filePath path];
                NSString *fileMD5Str = [CommonFileUtils getFileMD5WithPath:filePathStr];
                fileMD5Str = [fileMD5Str uppercaseString];
                if (![self.zipMd5 isEqualToString:fileMD5Str]) { //MD5校验 如果不相等就重新下载
                    [self performSelector:@selector(downloadZipForFace) withObject:nil afterDelay:3];
                }else {
                    dispatch_async(zipQueue, ^{ //子线程解压
//                        BOOL unzipSuccess = [SSZipArchive unzipFileAtPath:filePathStr toDestination:desPath];
                        [SSZipArchive unzipFileAtPath:filePathStr toDestination:desPath overwrite:YES password:nil progressHandler:^(NSString * _Nonnull entry, unz_file_info zipInfo, long entryNumber, long total) {
                            
                        } completionHandler:^(NSString * _Nonnull path, BOOL succeeded, NSError * _Nullable error) {
                            dispatch_main_sync_safe(^{
                                if (succeeded) {
                                    self.isLoadFace = YES;
                                    [self syncTheTmpJsonToNormalJson];
                                    self.destinationUrl = [self getDestinationUrlStr];
                                    [[NSUserDefaults standardUserDefaults]setObject:self.version forKey:@"faceJsonVersion"];
                                    NotifyCoreClient(HJFaceSourceClient, @selector(loadFaceSourceSuccess), loadFaceSourceSuccess);
                                }else {
                                    //                        [self downloadZipForFace];
                                    [self performSelector:@selector(downloadZipForFace) withObject:nil afterDelay:3];
                                }
                                
                            });

                        }];
                        
                    });
                    
                }
            }else {
                [self performSelector:@selector(downloadZipForFace) withObject:nil afterDelay:3];
            }
            
            
        }];
    }else {
        self.isLoadFace = NO;
    }
    
}

- (NSString *)getFaceImagePath {
    NSString *path = @"Documents/Face/";
    NSString *savePath = [NSHomeDirectory() stringByAppendingPathComponent:path];
    return savePath;
}

- (NSString *)getDestinationUrlStr {
    NSString *path = @"Documents/Face/";
    NSString *savePath = [NSHomeDirectory() stringByAppendingPathComponent:path];
    NSFileManager *fm =  [NSFileManager defaultManager];
    NSArray *arr = [fm contentsOfDirectoryAtPath:savePath error:nil];
//    NSMutableArray *tempArr = [NSMutableArray array];
    
    for (int i = 0; i < arr.count; i++) {
        NSString *item = arr[i];
        if (![item containsString:@"."]) {
//            [tempArr addObject:item];
            
            
            NSArray *faceArr1 = [self getFaceInfos];
            if (faceArr1.count) {
                NSArray *faceArr2 = [faceArr1 firstObject];
                if (faceArr2.count) {
                    
                    NSString *newSavePath = [savePath stringByAppendingString:[NSString stringWithFormat:@"/%@",item]];
                    
                    YPFaceConfigInfo *configInfo = [faceArr2 firstObject];
                    NSString *faceName = [NSString stringWithFormat:@"%@_%d_%ld",configInfo.pinyin,configInfo.id,configInfo.iconPos];
                    
                    NSString *dirName = [NSString stringWithFormat:@"%@_%d",configInfo.pinyin,configInfo.id];
                    NSString *targetPath = [NSString stringWithFormat:@"%@/%@/%@",newSavePath,dirName,faceName];
                    
                    UIImage *face = [UIImage imageWithContentsOfFile:targetPath];
                    
                    if (face) {
                        savePath = newSavePath;
                        break;
                    }
                    else {
                        continue;
                    }
                }
            }
            
            
            break;
        }
    }
    
//    while(tempArr.count < 5 && tempArr){
//        if (tempArr.count > 0) {
//            for (NSString *item in arr) {
//                if (![item containsString:@"."]) {
//                    savePath = [savePath stringByAppendingString:[NSString stringWithFormat:@"/%@",tempArr[0]]];
//                }
//            }
//
//        }
//        tempArr = [[fm contentsOfDirectoryAtPath:savePath error:nil] mutableCopy];
//    }
    
    return savePath;
}


- (YPFaceConfigInfo *)findFaceInfoById:(NSInteger)faceId{
    if (self.faceInfos != nil) {
        for (int i = 0; i < self.faceInfos.count; i++) {
            YPFaceConfigInfo *faceInfo = self.faceInfos[i];
            if (faceInfo.id == faceId) {
                return faceInfo;
            }
        }
    }
    return nil;
}


//查找表情iCON图片对象
- (UIImage *)findFaceIconImageById:(NSInteger)faceId {
    YPFaceConfigInfo *configInfo = [self findFaceInfoById:faceId];
    NSString *faceName = [NSString stringWithFormat:@"%@_%d_%ld",configInfo.pinyin,configInfo.id,configInfo.iconPos];
    UIImage  *face;
    face = [self.faceCache objectForKey:faceName];
    if (face) {
        return face;
    }else {
        NSString *dirName = [NSString stringWithFormat:@"%@_%d",configInfo.pinyin,configInfo.id];
        NSString *targetPath = [NSString stringWithFormat:@"%@/%@/%@",self.destinationUrl,dirName,faceName];
        face = [UIImage imageWithContentsOfFile:targetPath];
        if (face) {
            [self.faceCache setObject:face forKey:faceName];
        }
        return face;
    }
}

//查找图片数组
- (NSMutableArray<UIImage *> *)findFaceFrameArrByFaceId:(NSInteger)faceId {
    YPFaceConfigInfo *configInfo = [self findFaceInfoById:faceId];
    return [self findFaceFrameArrByConfig:configInfo];
}

//查找图片数组
- (NSMutableArray<UIImage *> *)findFaceFrameArrByConfig:(YPFaceConfigInfo *)configInfo {
    NSMutableArray *faceArr = [NSMutableArray array];
    for (int i = (short)configInfo.animStartPos; i <= (short)configInfo.animEndPos; i++) {
        [faceArr addObject:[self findFaceImageByConfig:configInfo index:i]];
    }
    return faceArr;
}

//查找图片
- (UIImage *)findFaceImageById:(NSInteger)faceId index:(NSInteger)index {
    YPFaceConfigInfo *configInfo = [self findFaceInfoById:faceId];
    return [self findFaceImageByConfig:configInfo index:index];
}

//查找图片
- (UIImage *)findFaceImageByConfig:(YPFaceConfigInfo *)configInfo index:(NSInteger)index {
    NSString *faceName = [NSString stringWithFormat:@"%@_%d_%ld",configInfo.pinyin,configInfo.id,(long)index];
    UIImage  *face;
    face = [self.faceCache objectForKey:faceName];
    if (face) {
        return face;
    }else {
        NSString *dirName = [NSString stringWithFormat:@"%@_%d",configInfo.pinyin,configInfo.id];
        NSString *targetPath = [NSString stringWithFormat:@"%@/%@/%@",self.destinationUrl,dirName,faceName];
//        if ([dirName isEqualToString:@"puke_22"]) {
//
//        } 
        face = [UIImage imageWithContentsOfFile:targetPath];
        if (face) {
            [self.faceCache setObject:face forKey:faceName];
        }
        return face;
    }
}

- (NSMutableArray *)sortFaceInfosWithfaceInfoArr:(NSMutableArray *)faceInfoArr {
    NSMutableArray *temp = [NSMutableArray array];
    NSMutableArray *temp2 = [NSMutableArray array]; //运气表情
    for (YPFaceConfigInfo *item in faceInfoArr) {
        if (item.resultCount <= 0) {
            [temp addObject:item];
        }else {
            [temp2 addObject:item];
        }
    }
    [temp addObjectsFromArray:temp2];
    return temp;
}

//过滤隐藏的表情
- (NSMutableArray *)hideFaceWithInfo:(NSArray *)hideList WithFaceInfos:(NSMutableArray *)infos {
    for (NSString *faceId in hideList) {
        int newFaceId = [faceId intValue];
        for (YPFaceConfigInfo *items in infos) {
            if (items.id == newFaceId) {
                [infos removeObject:items];
                break;
            }
        }
    }
    return infos;
}

- (NSMutableArray *)getFaceInfos {
    NSMutableArray *arr = [NSMutableArray array];
    NSMutableArray *resultArr = [NSMutableArray array];
    
    self.faceInfos = [self.tempFaceInfos mutableCopy];
    self.faceInfos = [self hideFaceWithInfo:self.hideFaceList WithFaceInfos:self.faceInfos];
    
    for (int i = 0; i < self.faceInfos.count; i++) {
        YPFaceConfigInfo *item = self.faceInfos[i];
        if (!item.isNobleFace) {
            [arr addObject:item];
            
            if (arr.count == 15) {
                [resultArr addObject:arr];
                arr = [NSMutableArray array];
            }
            
            if ( i%15 != 0 && i == self.faceInfos.count - 1) {
                [resultArr addObject:arr];
            }
            
        }
    }
    return resultArr;
}

//一起玩
- (void)sendAllFace:(YPFaceConfigInfo *)faceInfo {
    if (faceInfo) {
        if (GetCore(YPImRoomCoreV2).isInRoom) {
            NSMutableArray *chatroomMembers = GetCore(YPImRoomCoreV2).micMembers;
            NSMutableArray *faceRecieveInfos = [NSMutableArray array];
            //        YPFaceConfigInfo *child = [[YPFaceConfigInfo alloc]init];
            //麦序成员的配置
            if (chatroomMembers != nil && chatroomMembers.count > 0) {
                for (int i = 0; i < chatroomMembers.count; i++) {
                    YPFaceReceiveInfo *faceRecieveInfo = [[YPFaceReceiveInfo alloc]init];
                    if (faceInfo.resultStartPos > 0 && faceInfo.resultEndPos > 0) {
                        int value = [self getRandomNumber:(short)faceInfo.resultStartPos to:(short)faceInfo.resultEndPos];
                        //插入结果
                        faceRecieveInfo.resultIndexes = [@[@(value)]mutableCopy];
                    }
                    NIMChatroomMember *chatRoomMember = chatroomMembers[i];
                    
                    faceRecieveInfo.nick = chatRoomMember.roomNickname;
                    faceRecieveInfo.uid = chatRoomMember.userId.userIDValue;
                    faceRecieveInfo.faceId = faceInfo.id;
                    //                if (child != nil) {
                    //                    faceRecieveInfo.faceId = child.faceId;
                    //                }else {
                    //                    faceRecieveInfo.faceId = faceInfo.faceId;
                    //                }
                    
                    [faceRecieveInfos addObject:faceRecieveInfo];
                    
                }
            }
            
            //        //房主的配置
            //        UserInfo *info = [GetCore(UserCore) getUserInfoInDB:GetCore(ImRoomCoreV2).currentRoomInfo.uid];
            //
            //        if (info != nil) {
            //            YPFaceReceiveInfo *faceRecieveInfo = [[YPFaceReceiveInfo alloc]init];
            //            if (faceInfo.resultStartPos > 0 && faceInfo.resultEndPos > 0) {
            //                int value = [self getRandomNumber:(short)faceInfo.resultStartPos to:(short)faceInfo.resultEndPos];
            //                faceRecieveInfo.resultIndexes = [@[@(value)]mutableCopy];
            //            }
            //
            //
            //            faceRecieveInfo.nick = info.nick;
            //            faceRecieveInfo.uid = info.uid;
            //            faceRecieveInfo.faceId = faceInfo.id;
            //
            //            [faceRecieveInfos addObject:faceRecieveInfo];
            //        }
            //
            YPFaceSendInfo *sendInfo = [[YPFaceSendInfo alloc]init];
            sendInfo.data = faceRecieveInfos;
            //        sendInfo.uid = info.uid;
            sendInfo.uid = [GetCore(YPAuthCoreHelp)getUid].userIDValue;
            
            YPAttachment *attachment = [[YPAttachment alloc]init];
            attachment.first = Custom_Noti_Header_Face;
            attachment.second = Custom_Noti_Sub_Face_Send;
            attachment.data = sendInfo.encodeAttachemt;
            
            NSString *sessionID = [NSString stringWithFormat:@"%ld",GetCore(YPImRoomCoreV2).currentRoomInfo.roomId];
            [GetCore(YPImMessageCore)sendCustomMessageAttachement:attachment sessionId:sessionID type:NIMSessionTypeChatroom];
        }
    }
    
}

- (YPFaceConfigInfo *)getPlayTogetherFace {
    if (self.faceInfos != nil && self.faceInfos.count > 0) {
        for (int i = 0; i < self.faceInfos.count; i++) {
            YPFaceConfigInfo *faceInfo = self.faceInfos[i];
            if (faceInfo.id == 17) {
                return faceInfo;
            }
        }
    }else {
        [self requestFaceJson];
    }
    return nil;
}

- (void)sendFace:(YPFaceConfigInfo *)faceInfo {
    YPChatRoomInfo *info = GetCore(YPImRoomCoreV2).currentRoomInfo;
    if (info != nil) {
        YPFaceReceiveInfo *faceRecieveInfo = [[YPFaceReceiveInfo alloc]init];
        NSMutableArray *resultIndexs = [NSMutableArray array];
        if (faceInfo.resultStartPos > 0 && faceInfo.resultEndPos > 0) {
            int value;
            if (faceInfo.canResultRepeat) { //结果可以重复
                for (int i = 0; i < faceInfo.resultCount; i++) {
                    value = [self getRandomNumber:(short)faceInfo.resultStartPos to:(short)faceInfo.resultEndPos];
                    [resultIndexs addObject:@(value)];
                    faceRecieveInfo.resultIndexes = [resultIndexs copy];
                }
                
            }else {
                faceRecieveInfo.resultIndexes = [[self randomArray:(short)faceInfo.resultStartPos to:(short)faceInfo.resultEndPos count:(short)faceInfo.resultCount] mutableCopy];
            }
            //            faceInfo = faceInfo.children[value];
            
        }
        
        //        UserInfo *info = [GetCore(UserCore)getUserInfo:[GetCore(AuthCore)getUid].userIDValue refresh:NO];
        UserInfo *info = [GetCore(YPUserCoreHelp) getUserInfoInDB:[GetCore(YPAuthCoreHelp)getUid].userIDValue];
        NSMutableArray *faceRecieveInfos = [NSMutableArray array];
        
        faceRecieveInfo.nick = info.nick;
        faceRecieveInfo.faceId = faceInfo.id;
        faceRecieveInfo.uid = info.uid;
        [faceRecieveInfos addObject:faceRecieveInfo];
        
        YPFaceSendInfo *sendInfo = [[YPFaceSendInfo alloc]init];
        sendInfo.data = faceRecieveInfos;
        sendInfo.uid = info.uid;
        
        YPAttachment *attachment = [[YPAttachment alloc]init];
        attachment.first = Custom_Noti_Header_Face;
        attachment.second = Custom_Noti_Sub_Face_Send;
        
        NSMutableDictionary *dicEncodeAttachemt = [NSMutableDictionary dictionaryWithDictionary:sendInfo.encodeAttachemt];
        
        [dicEncodeAttachemt setValue:[NSNumber numberWithInteger:info.experLevel] forKey:@"experLevel"];
        
        attachment.data = dicEncodeAttachemt;

        NSString *sessionID = [NSString stringWithFormat:@"%ld",GetCore(YPImRoomCoreV2).currentRoomInfo.roomId];
        [GetCore(YPImMessageCore) sendCustomMessageAttachement:attachment sessionId:sessionID type:NIMSessionTypeChatroom];
        
    }
}

#pragma mark - ImMessageCoreClient
- (void)onSendMessageSuccess:(NIMMessage *)msg {
    if (msg.messageType == NIMMessageTypeCustom) {
        NIMCustomObject *obj = (NIMCustomObject *)msg.messageObject;
        if (obj.attachment != nil && [obj.attachment isKindOfClass:[YPAttachment class]]) {
            YPAttachment *attachment = (YPAttachment *)obj.attachment;
            if (attachment.first == Custom_Noti_Header_Face) {
                if (attachment.second == Custom_Noti_Sub_Face_Send) {
                    YPFaceSendInfo *faceattachement = [YPFaceSendInfo yy_modelWithJSON:attachment.data];
                    NSMutableArray *arr = [faceattachement.data mutableCopy];
                    NotifyCoreClient(HJFaceCoreClient, @selector(onReceiveFace:), onReceiveFace:arr);
                    //                    YPFaceReceiveInfo *faceRecieveInfo = arr.firstObject;
                    //                    YPFaceInfo *faceInfo = [self findFaceInfoById:faceRecieveInfo.faceId];
                    //                    if (faceInfo != nil && faceInfo.faceParentId > 1) {
                    //
                    //                    }
                    
                    if (arr.count > 0) {
                        YPFaceReceiveInfo *faceRecieveInfo = arr.firstObject;
                        YPFaceConfigInfo *faceInfo = [self findFaceInfoById:faceRecieveInfo.faceId];
                        if (faceInfo.id > 1 && faceInfo != nil && faceRecieveInfo.resultIndexes.count > 0) {
                            YPFacePlayInfo *playInfo = [[YPFacePlayInfo alloc]init];
                            playInfo.delay = 3;
                            playInfo.YPFaceReceiveInfo = arr.firstObject;
                            playInfo.message = msg;
                            for (YPFaceReceiveInfo *item in arr) {
                                if (item.uid == [GetCore(YPAuthCoreHelp) getUid].userIDValue) {
                                    self.isShowingFace = YES;
                                }
                            }
                            
                            [self handleFaceMessage:playInfo];
                        }
                    }
                }
            }
        }
    }
}

- (void)onRecvChatRoomCustomMsg:(NIMMessage *)msg {
    if (msg.messageType == NIMMessageTypeCustom) {
        NIMCustomObject *obj = (NIMCustomObject *)msg.messageObject;
        if (obj.attachment != nil && [obj.attachment isKindOfClass:[YPAttachment class]]) {
            YPAttachment *attachment = (YPAttachment *)obj.attachment;
            if (attachment.first == Custom_Noti_Header_Face) {
                if (attachment.second == Custom_Noti_Sub_Face_Send) {
                    YPFaceSendInfo *faceattachement = [YPFaceSendInfo yy_modelWithJSON:attachment.data];
                    
                    NSMutableArray *arr = [faceattachement.data mutableCopy];
                    NotifyCoreClient(HJFaceCoreClient, @selector(onReceiveFace:), onReceiveFace:arr);
                    
                    //                    if (faceInfo != nil && faceInfo.faceParentId > 1) {
                    //                        //进入播放队列
                    //                        YPFacePlayInfo *playInfo = [[YPFacePlayInfo alloc]init];
                    //                        playInfo.delay = 3;
                    //                        playInfo.YPFaceReceiveInfo = faceRecieveInfo;
                    //                        [self handleFaceMessage:playInfo];
                    //                    }
                    if (arr.count > 0) {
                        YPFaceReceiveInfo *faceRecieveInfo = arr.firstObject;
                        YPFaceConfigInfo *faceInfo = [self findFaceInfoById:faceRecieveInfo.faceId];
                        if (faceInfo.id > 1 && faceInfo != nil && faceRecieveInfo.resultIndexes.count > 0) {
                            YPFacePlayInfo *playInfo = [[YPFacePlayInfo alloc]init];
                            playInfo.delay = 3;
                            playInfo.YPFaceReceiveInfo = arr.firstObject;
                            playInfo.message = msg;
                            for (YPFaceReceiveInfo *item in arr) {
                                if (item.uid == [GetCore(YPAuthCoreHelp) getUid].userIDValue) {
                                    self.isShowingFace = YES;
                                }
                            }
                            
                            [self handleFaceMessage:playInfo];
                        }
                        //                        [GetCore(FaceCore)findFaceInfoById:info.YPFaceReceiveInfo.faceId].children
                        
                        //                        YPFaceInfo *faceInfo = [self findFaceInfoById:]
                        
                        //                        for (YPFaceReceiveInfo *item in arr) {
                        //                            //进入播放队列
                        //
                        //
                        //                        }
                    }
                }
            }
        }
    }
}

#pragma mark - AuthCoreClient

- (void)onLoginSuccess {
//    [self requestFaceJson];
}

#pragma mark - Face Play Method

- (void)handleFaceMessage:(YPFacePlayInfo *)YPFacePlayInfo {
    [self.receiveFace addObject:YPFacePlayInfo];
    if (self.timer== nil) {
        [self startTheFaceQueueScanner];
    }
}

- (void)startTheFaceQueueScanner {
    NSTimeInterval period = 1; //设置时间间隔
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);    dispatch_source_t _timer = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue);
    dispatch_source_set_timer(_timer, dispatch_walltime(NULL, 0), period * NSEC_PER_SEC, 0); //每1秒执行
    @weakify(self);
    dispatch_source_set_event_handler(_timer, ^{
        @strongify(self);
        if (self.receiveFace.count > 0) {
            NSMutableArray *tempArr = [NSMutableArray array];
            NSMutableArray *temp = [self.receiveFace mutableCopy];
            for (YPFacePlayInfo *item in temp) {
                item.delay = item.delay - 1;
                if (item.delay == 0) {
                    self.isShowingFace = NO;
                    if ([NSThread isMainThread])
                    {
                        NotifyCoreClient(HJFaceCoreClient, @selector(onFaceIsResult:), onFaceIsResult:item);
                    }
                    else
                    {
                        dispatch_sync(dispatch_get_main_queue(), ^{
                            //Update UI in UI thread here
                            NotifyCoreClient(HJFaceCoreClient, @selector(onFaceIsResult:), onFaceIsResult:item);
                            
                            
                        });
                    }
                    
                }else {
                    [tempArr addObject:item];
                }
            }
            self.receiveFace = tempArr;
        } else {
            dispatch_source_cancel(_timer);
            self.timer = nil;
        }
        
    });
    
    dispatch_resume(_timer);
    self.timer = _timer;
}

#pragma mark - Private


/**
 生成固定区间的不重复随机数
 
 @param from 最小值
 @param to 最大值
 @param count 数量
 @return 返回值
 */
- (NSArray *)randomArray:(int)from to:(int)to count:(int)count {
    
    //随机数从这里边产生
    NSMutableArray *startArray= [NSMutableArray array];
    for (int i = from; i <= to; i++) {
        [startArray addObject:@(i)];
    }
    
    //随机数产生结果
    NSMutableArray *resultArray=[[NSMutableArray alloc] initWithCapacity:0];
    //随机数个数
    NSInteger m = count;
    for (int i=0; i<m; i++) {
        int t = arc4random()%startArray.count;
        resultArray[i]=startArray[t];
        startArray[t]=[startArray lastObject]; //为更好的乱序，故交换下位置
        [startArray removeLastObject];
    }
    return resultArray;
}

/**
 生成随机数
 
 @param from 最小值
 @param to 最大值
 @return 随机数
 */
- (int)getRandomNumber:(int)from to:(int)to {
    return (int)(from + (arc4random() % (to - from + 1)));
}

- (BOOL)isEnableFace:(YPFaceConfigInfo *)info {
    if (info.displayType != XCFaceDisplayTypeOnlyOne && info.displayType != XCFaceDisplayTypeFlow && info.displayType != XCFaceDisplayTypeOverLay) {
        return NO;
    }else {
        return YES;
    }
    
}

#pragma mark - Getter
- (NSMutableArray *)receiveFace {
    if (_receiveFace == nil) {
        _receiveFace = [NSMutableArray array];
    }
    return _receiveFace;
}

- (BOOL)getShowingFace {
    return self.isShowingFace;
}

- (NSCache *)faceCache {
    if (_faceCache == nil) {
        _faceCache = [[NSCache alloc]init];
    }
    return _faceCache;
}

#pragma mark - Test //测试使用

- (void)cancelFaceTimer {
    self.timer = nil;
}

- (void)startFaceTimer {
    // 获得队列
    dispatch_queue_t queue = dispatch_get_main_queue();
    // 创建一个定时器(dispatch_source_t本质还是个OC对象)
    self.testTimer = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue);
    // 设置定时器的各种属性（几时开始任务，每隔多长时间执行一次）
    // GCD的时间参数，一般是纳秒（1秒 == 10的9次方纳秒）
    // 何时开始执行第一个任务
    // dispatch_time(DISPATCH_TIME_NOW, 1.0 * NSEC_PER_SEC) 比当前时间晚3秒
    dispatch_time_t start = dispatch_time(DISPATCH_TIME_NOW, (int64_t)(8 * NSEC_PER_SEC));
    uint64_t interval = (uint64_t)(60.0 * NSEC_PER_SEC);
    dispatch_source_set_timer(self.testTimer, start, interval, 0);
    
    // 设置回调
    @weakify(self);
    dispatch_source_set_event_handler(self.testTimer, ^{
        @strongify(self);
        [self sendAllFace:[self getPlayTogetherFace]];
    });
    // 启动定时器
    dispatch_resume(self.testTimer);
}

#pragma mark - SSZipArchiveDelegate


@end
