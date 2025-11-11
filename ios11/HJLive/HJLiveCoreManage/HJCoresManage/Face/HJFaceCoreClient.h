//
//  HJFaceCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "FaceInfo.h"
#import "HJFaceReceiveInfo.h"
#import "HJFacePlayInfo.h"

@protocol HJFaceCoreClient <NSObject>
@optional
- (void)onFaceIsResult:(HJFacePlayInfo *)info;
- (void)onReceiveFace:(NSMutableArray<HJFaceReceiveInfo *> *)faceRecieveInfos;
- (void)onGetFaceJsonSuccess;
@end
