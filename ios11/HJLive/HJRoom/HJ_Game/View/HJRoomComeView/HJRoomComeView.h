//
//  HJRoomComeView.h
//  HJLive
//
//  Created by feiyin on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "ChatRoomMember.h"

typedef void(^XBDRoomComeCarSvgaBlock)(NSString *carUrl);


@interface HJRoomComeView : UIView

@property (weak, nonatomic) IBOutlet UILabel *nameLabel;

@property (copy,nonatomic) XBDRoomComeCarSvgaBlock carBlock;

- (void)show:(ChatRoomMember *)member;

@end

