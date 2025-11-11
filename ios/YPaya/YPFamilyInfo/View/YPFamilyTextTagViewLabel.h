//
//  YPFamilyTextTagViewLabel.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "FamilyDefines.h"
NS_ASSUME_NONNULL_BEGIN

@interface YPFamilyTextTagViewLabel : UILabel
- (void)configureWithRoleStatus:(XCFamilyRoleStatus)status level:(NSInteger)level;
@end

NS_ASSUME_NONNULL_END
