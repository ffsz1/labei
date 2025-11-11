//
//  YPMediaCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseCore.h"

@interface YPMediaCore : YPBaseCore
- (void) record;
- (void) cancelRecord;
- (void) stopRecord;
- (BOOL) isRecording;

- (void) play:(NSString *)filePath;
- (void) stopPlay;
- (BOOL) isPlaying;
@end
