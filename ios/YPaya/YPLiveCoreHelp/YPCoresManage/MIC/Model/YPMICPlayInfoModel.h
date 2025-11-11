//
//  YPMICPlayInfoModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef NS_ENUM(NSInteger, MICPlayState) {
    MICPlayStateUndefine,
    MICPlayStateBegin,
    MICPlayStateProgress,
    MICPlayStatePause,
    MICPlayStateCancel,
    MICPlayStateFinished,
};


@interface YPMICPlayInfoModel : NSObject

@property (nonatomic, assign) MICPlayState playState;
@property (nonatomic, assign) NSInteger duration;
@property (nonatomic, copy) NSString *voicePath;
@property (nonatomic, copy) NSString *localVoicePath;

@end
