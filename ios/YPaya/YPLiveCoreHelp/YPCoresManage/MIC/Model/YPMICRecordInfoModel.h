//
//  YPMICRecordInfoModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef NS_ENUM(NSInteger, MICRecordState) {
    MICRecordStateUndefine,
    MICRecordStateBegin,
    MICRecordStateProgress,
    MICRecordStateCancel,
    MICRecordStateFinished,
    MICRecordStatePlaying,
};

@interface YPMICRecordInfoModel : NSObject

@property (nonatomic, assign) NSTimeInterval time;  ///< 录音时长
@property (nonatomic, assign) MICRecordState state; ///< 录音状态
@property (nonatomic, copy) NSString *filePath;     ///< 远程路径
@property (nonatomic, copy) NSString *localPath;    ///< 本地路径

@end
