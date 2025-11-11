//
//  YPRoomBgModel.h
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseObject.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPRoomBgModel : YPBaseObject

@property (nonatomic, copy) NSString *beginDate;
@property (nonatomic, copy) NSString *createDate;
@property (nonatomic, copy) NSString *endDate;
@property (nonatomic, assign) NSInteger id;
@property (nonatomic, copy) NSString *name;
@property (nonatomic, copy) NSString *picUrl;
@property (nonatomic, copy) NSString *sortNo;
@property (nonatomic, copy) NSString *status;
@property (nonatomic, copy) NSString *tagIds;
@property (nonatomic, copy) NSString *type;
@property (nonatomic, copy) NSString *uids;

@end

NS_ASSUME_NONNULL_END
