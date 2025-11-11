//
//  HJPlacardView.h
//  HJLive
//
//  Created by feiyin on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HJPlacardView : UIView
@property (weak, nonatomic) IBOutlet UILabel *tomiclabel;
@property (weak, nonatomic) IBOutlet UITextView *contentLabel;
@property (nonatomic, copy) void(^closeClick)();
@end
