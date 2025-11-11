//
//  HJAnchorSharingMessageView.h
//  HJLive
//
//  Created by feiyin on 2020/6/26.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HJAnchorSharingMessageView : UIView


@property (weak, nonatomic) IBOutlet UILabel *enterRoomBtn;
@property (weak, nonatomic) IBOutlet UIView *bgView;
@property (weak, nonatomic) IBOutlet UIImageView *roomImg;
@property (weak, nonatomic) IBOutlet UILabel *title;
@property (weak, nonatomic) IBOutlet UIVisualEffectView *viEffect;
@property (assign, nonatomic) UserID uid;
@end
