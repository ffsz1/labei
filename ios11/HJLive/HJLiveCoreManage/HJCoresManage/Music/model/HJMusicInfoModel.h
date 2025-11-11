//
//  HJMusicInfoModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface HJMusicInfoModel : NSObject

@property (nonatomic, copy) NSString *musicName;
@property (nonatomic, copy) NSString *musicSinger;
@property (nonatomic, assign) float duration;

@end

NS_ASSUME_NONNULL_END
