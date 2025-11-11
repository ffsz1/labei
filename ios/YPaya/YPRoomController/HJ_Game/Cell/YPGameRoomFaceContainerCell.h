//
//  YPGameRoomFaceContainerCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/8.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YPFaceConfigInfo.h"

@interface YPGameRoomFaceContainerCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UICollectionView *collectionView;
@property (strong, nonatomic) NSMutableArray<YPFaceConfigInfo *> *faceInfos;
@end
