//
//  HJGameRoomFaceContainerCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/8.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HJFaceConfigInfo.h"

@interface HJGameRoomFaceContainerCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UICollectionView *collectionView;
@property (strong, nonatomic) NSMutableArray<HJFaceConfigInfo *> *faceInfos;
@end
