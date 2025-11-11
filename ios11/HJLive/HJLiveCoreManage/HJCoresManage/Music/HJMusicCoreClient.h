//
//  HJMusicCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol HJMusicCoreClient<NSObject>
- (void)updateList;
- (void)changeIndexTitle;
- (void)stopMusicNoti;
@end
