//
//  HJMediaCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol HJMediaCoreClient <NSObject>
@optional
- (void) onRecordAudioBegan:(NSString*)filePath;
- (void) onRecordAudioComplete:(NSString*)filePath;
- (void) onRecordAudioProgress:(NSTimeInterval)currentTime;
- (void) onRecordAudioCancel;

- (void) onPlayAudioBegan:(NSString *)filePath;
- (void) onPlayAudioComplete:(NSString *)filePath;
@end
