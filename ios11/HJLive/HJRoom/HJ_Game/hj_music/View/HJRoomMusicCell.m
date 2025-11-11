//
//  HJRoomMusicCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRoomMusicCell.h"

#import "HJMusicCore.h"

@implementation HJRoomMusicCell


- (IBAction)deleteBtnAction:(id)sender {
    [GetCore(HJMusicCore) removeBackgroundMusic:self.url];

}

- (void)setModel:(HJMusicInfoModel *)model
{
    _model = model;
    
    if (model) {
        self.nameLabel.text = model.musicName;
        self.singerLabel.text = model.musicSinger;
    }
    
}

@end
