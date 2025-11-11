//
//  XCFamilyManageItemModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseObject.h"
#import "FamilyDefines.h"

NS_ASSUME_NONNULL_BEGIN

@interface HJFamilyManageItemModel : BaseObject

@property (nonatomic, assign) XCFamilyManageItemType type;
@property (nonatomic, copy) NSString *title;
@property (nonatomic, assign) BOOL enable;

+ (instancetype)itemWithType:(XCFamilyManageItemType)type enable:(BOOL)enable;

@end

NS_ASSUME_NONNULL_END
