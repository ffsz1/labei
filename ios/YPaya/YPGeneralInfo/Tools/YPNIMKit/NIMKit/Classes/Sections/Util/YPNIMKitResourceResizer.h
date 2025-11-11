//
//  YPNIMKitResourceResizer.h
//  YPNIMKit
//
//  Created by chris.
//  Copyright (c) 2017 Netease. All rights reserved.
//

#import <UIKit/UIKit.h>


#define NIMKitNOSX(w,h) [[YPNIMKitResourceResizer sharedResizer] resize:@"x" width:(w) height:(h)]
#define NIMKitNOSY(w,h) [[YPNIMKitResourceResizer sharedResizer] resize:@"y" width:(w) height:(h)]
#define NIMKitNOSZ(w,h) [[YPNIMKitResourceResizer sharedResizer] resize:@"z" width:(w) height:(h)]

@interface YPNIMKitResourceResizer : NSObject
+ (instancetype)sharedResizer;

- (CGSize)resizeWidth:(CGFloat)width
               height:(CGFloat)height;

- (NSString *)resize:(NSString *)mode
               width:(CGFloat)width
              height:(CGFloat)height;

- (NSString *)imageThumbnailURL:(NSString *)urlString;
- (NSString *)videoThumbnailURL:(NSString *)urlString;

@end
