//
//  HJUserListCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UIImageView+YYWebImage.h"


@interface HJUserListCell : UICollectionViewCell

@property (copy, nonatomic) NSString *urlStr;
@property (weak, nonatomic) IBOutlet UIImageView *speakingAnimImage;
@property (weak, nonatomic) IBOutlet UIImageView *onMircoLogoImageView;
@property (weak, nonatomic) IBOutlet UIImageView *imageView;
@end
