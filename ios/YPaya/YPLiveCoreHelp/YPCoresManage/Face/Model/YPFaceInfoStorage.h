//
//  FaceInfoStorage.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YPFaceInfoStorage : NSObject

+ (NSMutableArray *)getFaceInfos;
+ (void)saveFaceInfos:(NSString *)json;

@end
