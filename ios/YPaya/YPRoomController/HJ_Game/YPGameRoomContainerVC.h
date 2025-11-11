//
//  YPGameRoomContainerVC.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
//  轰趴房容器

#import "YPYYViewController.h"
#import "HJImRoomCoreClient.h"

@interface YPGameRoomContainerVC : UIViewController
@property (strong, nonatomic) IBOutlet UIPageControl *pageControl;
@property (weak, nonatomic) IBOutlet UIVisualEffectView *effectView;
- (void)beKicked;//被踢
- (void)beInBlackList;
@end
