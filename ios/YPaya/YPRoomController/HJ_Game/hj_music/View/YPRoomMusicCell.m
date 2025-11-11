//
//  YPRoomMusicCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRoomMusicCell.h"

#import "YPMusicCore.h"

@implementation YPRoomMusicCell


- (IBAction)deleteBtnAction:(id)sender {
    [GetCore(YPMusicCore) removeBackgroundMusic:self.url];

}

- (void)setModel:(YPMusicInfoModel *)model
{
    _model = model;
    
    if (model) {
        self.nameLabel.text = model.musicName;
        self.singerLabel.text = model.musicSinger;
    }
    
}

@end
