//
//  FaceInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BaseObject.h"

@interface FaceInfo : BaseObject

@property (assign, nonatomic) NSInteger faceId;
@property (copy, nonatomic) NSString *faceName;
@property (assign, nonatomic) NSInteger faceParentId;
@property (copy, nonatomic) NSString *facePicUrl;
@property (assign, nonatomic) BOOL hasGifUrl;
@property (copy, nonatomic) NSString *faceGifUrl;
@property (assign, nonatomic) BOOL show;
@property (copy, nonatomic) NSArray *children;
@property (assign, nonatomic) NSInteger faceValue;
@end
