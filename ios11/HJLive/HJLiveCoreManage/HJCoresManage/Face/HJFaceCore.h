//
//  HJFaceCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseCore.h"
#import "FaceInfo.h"
#import "HJFaceConfigInfo.h"

@interface HJFaceCore : BaseCore

@property (copy, nonatomic) NSString *version;
@property (copy, nonatomic) NSString *zipMd5;
@property (strong, nonatomic) NSURL *zipUrl;

@property (assign, nonatomic) BOOL isShowingFace;
@property (copy, nonatomic) NSString *destinationUrl;
@property (assign, nonatomic) BOOL isLoadFace;
@property (strong, nonatomic) NSArray *hideFaceList;

//- (void)requestFaceList;


/**
 获取表情列表

 @return 表情列表
 */
- (NSMutableArray *)getFaceInfos;



/**
 是否正在播放表情

 @return <#return value description#>
 */
- (BOOL)getShowingFace;


/**
 群发表情

 @param faceInfo 表情信息
 */
- (void)sendAllFace:(HJFaceConfigInfo *)faceInfo;


/**
 单独发表情

 @param faceInfo 表情信息
 */
- (void)sendFace:(HJFaceConfigInfo *)faceInfo;

/**
 获取一起玩表情（骰子）

 @return 表情信息
 */
- (HJFaceConfigInfo *)getPlayTogetherFace;


/**
 通过表情ID找表情

 @param faceId 表情ID
 @return 表情信息
 */
- (HJFaceConfigInfo *)findFaceInfoById:(NSInteger)faceId;


/**
 通过表情ID查到图片对象

 @param faceId 表情ID
 @return 表情图片对象
 */
- (UIImage *)findFaceIconImageById:(NSInteger)faceId;


/**
 通过表情ID与Index找图片对象

 @param faceId 表情ID
 @param index 表情Index
 @return 表情对象
 */
- (UIImage *)findFaceImageById:(NSInteger)faceId index:(NSInteger)index;


/**
 通过表情ConfigInfo查找图片

 @param configInfo 表情配置
 @param index 位置pos
 @return 表情图片对象
 */
- (UIImage *)findFaceImageByConfig:(HJFaceConfigInfo *)configInfo index:(NSInteger)index;



/**
 通过Config查找图片数组

 @param configInfo 表情配置
 @return 表情图片对象数组
 */
- (NSMutableArray<UIImage *> *)findFaceFrameArrByConfig:(HJFaceConfigInfo *)configInfo;


/**
 通过FaceId查找图片对象数组

 @param faceId 表情ID
 @return 图片对象数组
 */
- (NSMutableArray<UIImage *> *)findFaceFrameArrByFaceId:(NSInteger)faceId;


/**
 请求Face数据
 */
- (void)requestFaceJson;


/**
 测试使用接口，发送一起玩表情
 */
- (void)startFaceTimer;
- (void)cancelFaceTimer;
@end
