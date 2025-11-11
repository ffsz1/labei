//
//  HJFaceConfigInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum : NSUInteger {
    XCFaceDisplayTypeOnlyOne = 0,
    XCFaceDisplayTypeFlow = 1,
    XCFaceDisplayTypeOverLay = 2,
} XCFaceDisplayType;

@interface HJFaceConfigInfo : NSObject

@property (nonatomic, assign) int id;
@property (nonatomic, copy) NSString *name;
@property (nonatomic, copy) NSString *pinyin;
@property (nonatomic, assign) NSInteger animDuration;
@property (nonatomic, assign) NSInteger animEndPos;
@property (nonatomic, assign) NSInteger animStartPos;
@property (nonatomic, assign) NSInteger iconPos;
@property (nonatomic, assign) NSInteger animRepeatCount;
@property (nonatomic, assign) NSInteger resultCount;
@property (nonatomic, assign) BOOL canResultRepeat;
@property (nonatomic, assign) NSInteger resultDuration;
@property (nonatomic, assign) NSInteger resultEndPos;
@property (nonatomic, assign) NSInteger resultStartPos;
@property (nonatomic, assign) NSInteger imageCount;
@property (nonatomic, assign) XCFaceDisplayType displayType;
@property (nonatomic, assign) BOOL isNobleFace; //是否贵族图片

@end
