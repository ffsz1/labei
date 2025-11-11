//
//  YPFamilyChatSetVC.h
//  HJLive
//
//  Created by feiyin on 2020/7/19.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <NIMSDK/NIMSDK.h>


NS_ASSUME_NONNULL_BEGIN

@interface YPFamilyChatSetVC : UIViewController

@property (nonatomic,strong) NIMTeam *team;
@property (nonatomic,copy) NSString *familyID;


@end

NS_ASSUME_NONNULL_END
