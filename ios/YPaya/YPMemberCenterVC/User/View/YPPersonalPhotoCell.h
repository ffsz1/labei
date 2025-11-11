//
//  YPPersonalPhotoCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol HJPersonalPhotoCellDelegate <NSObject>

- (void)deletePhoto:(NSIndexPath *)indexPath;

@end

@interface YPPersonalPhotoCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIImageView *personalPhotoImagwView;
@property (weak, nonatomic) IBOutlet UIButton *deleteBtn;
@property (strong, nonatomic) NSIndexPath *indexPath;
@property (weak, nonatomic) id<HJPersonalPhotoCellDelegate>delegate;

@end
