//
//  HJImagePreViewVC.h
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//  浏览图片-[聊天]

#import "YYViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface HJImagePreViewVC : YYViewController <UIScrollViewDelegate>

@property (nonatomic, strong) UIScrollView *scrollView;

@property (nonatomic, strong) UIImageView *imageView;

@property (nonatomic, strong) NSString *ImageUrl;
@end

NS_ASSUME_NONNULL_END
