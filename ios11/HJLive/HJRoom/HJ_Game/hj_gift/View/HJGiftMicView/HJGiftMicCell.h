//
//  HJGiftMicCell.h
//  HJLive
//
//  Created by apple on 2019/7/9.
//

#import <UIKit/UIKit.h>

#import "HJIMQueueItem.h"



@interface HJGiftMicCell : UICollectionViewCell

@property (nonatomic,assign) NSInteger item;

@property (nonatomic,strong) HJIMQueueItem *info;

@property (weak, nonatomic) IBOutlet UIImageView *imgView1;

@property (weak, nonatomic) IBOutlet UIImageView *imgView2;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *numLabel_bottom_layout;

- (void)setSelStytle:(BOOL)isSel;

@end


