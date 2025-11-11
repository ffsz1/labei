//
//  YPGGMyFamilySectionHeader.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPGGMyFamilySectionHeader : UIView
@property (weak, nonatomic) IBOutlet UIButton *enterBtn;
@property (weak, nonatomic) IBOutlet UILabel *numLabel;
@property (copy,nonatomic) NSString *roomID;
@property (nonatomic,copy) NSString *familyID;


@end

NS_ASSUME_NONNULL_END
