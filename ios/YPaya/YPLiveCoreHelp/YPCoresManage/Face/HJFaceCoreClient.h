//
//  HJFaceCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPFaceInfo.h"
#import "YPFaceReceiveInfo.h"
#import "YPFacePlayInfo.h"

@protocol HJFaceCoreClient <NSObject>
@optional
- (void)onFaceIsResult:(YPFacePlayInfo *)info;
- (void)onReceiveFace:(NSMutableArray<YPFaceReceiveInfo *> *)faceRecieveInfos;
- (void)onGetFaceJsonSuccess;
@end
