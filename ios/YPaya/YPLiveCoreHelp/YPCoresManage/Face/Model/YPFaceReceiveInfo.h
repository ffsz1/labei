//
//  YPFaceReceiveInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPAttachment.h"
#import "YPBaseObject.h"

@interface YPFaceReceiveInfo : YPBaseObject

@property (assign, nonatomic)UserID uid;
@property (copy, nonatomic) NSString *nick;
@property (assign, nonatomic) NSInteger faceId;
@property (strong, nonatomic) UIImage *resultImage;
@property (strong, nonatomic) NSMutableArray *resultIndexes;
@property(nonatomic, assign) NSInteger experLevel;
@end
