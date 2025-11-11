//
//  YPFaceImageTool.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPFaceConfigInfo.h"
#import "YPFaceReceiveInfo.h"
#import "YPFaceCore.h"

@interface YPFaceImageTool : NSObject

+ (instancetype)shareFaceImageTool;


/**
 查询并生成结果图

 @param receiveInfo 收到表情的对象
 @param imageView 展示表情的ImageView
 @param success 成功
 @param failure 失败
 */
- (void)queryImage:(YPFaceReceiveInfo *)receiveInfo
         imageView:(UIImageView *)imageView
           success:(void (^)(YPFaceReceiveInfo *info))success
           failure:(void (^)(NSError *))failure;


//- (void)saveImageWithArr:(NSMutableArray<YPFaceReceiveInfo *> *)faceRecieveInfos
//               imageView:(UIImageView *)imageView
//                 success:(void (^)(NSMutableArray<YPFaceReceiveInfo *> *))success
//                 failure:(void (^)(NSError *))failure;

@end
