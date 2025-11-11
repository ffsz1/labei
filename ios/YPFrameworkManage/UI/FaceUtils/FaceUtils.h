//
//  FaceUtils.h
//  HJLive
//
//  Created by FF on 2020/7/13.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface FaceUtils : NSObject

+ (void)createGroupFace:(NSMutableArray *)group finished:(void (^)(UIImage *))resultImage;

@end
