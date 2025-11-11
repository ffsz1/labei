//
//  AdvertiseView.h
//  JinSeShiJi
//
//  Created by zn on 16/8/10.
//
//

#import <UIKit/UIKit.h>
#import "HJAdCache.h"

#define kscreenWidth [UIScreen mainScreen].bounds.size.width
#define kscreenHeight [UIScreen mainScreen].bounds.size.height
#define kUserDefaults [NSUserDefaults standardUserDefaults]
static NSString *const adImageName = @"yumengNewAdImageName";
static NSString *const adUrl = @"adUrl";
@interface WMAdvertiseView : UIView

/** 显示广告页面方法*/
- (void)show;

/** 图片路径*/
@property (nonatomic, copy) NSString *filePath;

@property (nonatomic, strong) UIImage *adImage;

@property (nonatomic, strong) AdInfo *oldAdInfo; //需要把旧的广告信息先存起来 因为每次显示的都是上一次下载的信息

@end
