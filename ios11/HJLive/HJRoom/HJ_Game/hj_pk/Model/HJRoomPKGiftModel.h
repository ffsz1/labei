//
//  HJRoomPKGiftModel.h
//  HJLive
//
//  Created by apple on 2019/6/19.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface HJRoomPKGiftModel : NSObject

@property (nonatomic, assign)NSInteger giftId;
@property (nonatomic, assign)NSInteger giftGold;
@property (nonatomic, copy)NSString *giftName;
@property (nonatomic, assign)NSInteger giftNum;
@property (nonatomic, copy)NSString *giftUrl;

//giftGold = 5200;
//giftId = 140;
//giftName = "\U751c\U871c\U966a\U4f34";
//giftNum = 21;
//giftPic = "https://pic.hnyueqiang.com/Fg999bDCAWTgZ9d5T8ObCjFO601i?imageslim";

@end

NS_ASSUME_NONNULL_END
