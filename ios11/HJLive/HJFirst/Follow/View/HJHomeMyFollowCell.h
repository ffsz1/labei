//
//  HJHomeMyFollowCell.h
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface HJHomeMyFollowCell : UITableViewCell<UICollectionViewDelegate,UICollectionViewDataSource>
@property (weak, nonatomic) IBOutlet UICollectionView *collectionView;

@property (strong,nonatomic) NSArray *myFollowArr;

@end

NS_ASSUME_NONNULL_END
