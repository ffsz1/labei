//
//  YPHomePeipeiModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/8.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseObject.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPHomePeipeiModel : YPBaseObject
@property (nonatomic, copy) NSString *avatar;
@property (nonatomic, copy) NSString *nick;
@property(nonatomic, assign) UserID uid;
@property (nonatomic, assign) long gender;
@property (nonatomic, assign) long signTime;
@property (nonatomic, assign) long glamour;
@property (nonatomic, assign) long signature;
@property (nonatomic, copy) NSString *userVoice;
@property (nonatomic, assign) long voiceDuration;
@property (nonatomic, copy) NSString *userDescription;
@property (nonatomic, assign) BOOL isLeft;
@property (nonatomic, assign) NSInteger roomState;//房间在线 roomState（1.在线 0.不在线）

//"uid": 100100693,
//"nick": "测试",
//"signature": null,
//"userVoice": null,
//"voiceDuration": null,
//"gender": 1,
//"avatar": "http://qc422frwu.bkt.clouddn.com/img_nv1_nor.png",
//"signTime": 1594260320000,
//"glamour": 16436
@end

NS_ASSUME_NONNULL_END
