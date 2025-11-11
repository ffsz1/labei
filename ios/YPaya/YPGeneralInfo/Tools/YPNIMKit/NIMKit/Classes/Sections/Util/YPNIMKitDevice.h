//
//  YPNIMKitDevice.h
//  NIM
//
//  Created by chris on 15/9/18.
//  Copyright © 2015年 Netease. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YPNIMKitDevice : NSObject

+ (YPNIMKitDevice *)currentDevice;

//图片/音频推荐参数
- (CGFloat)suggestImagePixels;

- (CGFloat)compressQuality;

- (CGFloat)statusBarHeight;

@end
