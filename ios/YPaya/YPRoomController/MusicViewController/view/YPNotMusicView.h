//
//  YPNotMusicView.h
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YPNotMusicView : UIView
@property (weak, nonatomic) IBOutlet UIButton *startBtn;
@property (weak, nonatomic) IBOutlet UILabel *currentSong;
@property (weak, nonatomic) IBOutlet UILabel *tipSongLabel;
@property (weak, nonatomic) IBOutlet UIView *sliderContentView;
@property (weak, nonatomic) IBOutlet UISlider *slider;
@property (weak, nonatomic) IBOutlet UILabel *songName;
@property (nonatomic, copy) void(^configMusicBlock)();
@property (nonatomic, copy) void(^sliderBlock)(CGFloat progress);
@end
