//
//  YPGiftMicCell.h
//  HJLive
//
//  Created by apple on 2019/7/9.
//

#import <UIKit/UIKit.h>

#import "YPIMQueueItem.h"



@interface YPGiftMicCell : UICollectionViewCell

@property (nonatomic,assign) NSInteger item;

@property (nonatomic,strong) YPIMQueueItem *info;

@property (weak, nonatomic) IBOutlet UIImageView *imgView1;

@property (weak, nonatomic) IBOutlet UIImageView *imgView2;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *numLabel_bottom_layout;

- (void)setSelStytle:(BOOL)isSel;

@end


