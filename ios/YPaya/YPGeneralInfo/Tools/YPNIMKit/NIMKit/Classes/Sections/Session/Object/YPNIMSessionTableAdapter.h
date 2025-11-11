//
//  NIMSessionTableDelegate.h
//  YPNIMKit
//
//  Created by chris on 2016/11/7.
//  Copyright © 2016年 NetEase. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NIMSessionConfigurateProtocol.h"
#import "NIMMessageCellProtocol.h"

@interface YPNIMSessionTableAdapter : NSObject<UITableViewDelegate,UITableViewDataSource>

@property (nonatomic,weak) id<NIMSessionInteractor> interactor;

@property (nonatomic,weak) id<NIMMessageCellDelegate> delegate;

@end
