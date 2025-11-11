//
//  XCFamilyManageJoinWayModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseObject.h"
#import "FamilyDefines.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPFamilyManageJoinWayModel : YPBaseObject

@property (nonatomic, assign) XCFamilyJoinWayType type;
@property (nonatomic, copy) NSString *title;
@property (nonatomic, assign) BOOL selected;

+ (instancetype)itemWithType:(XCFamilyJoinWayType)type selected:(BOOL)selected;

@end

NS_ASSUME_NONNULL_END
