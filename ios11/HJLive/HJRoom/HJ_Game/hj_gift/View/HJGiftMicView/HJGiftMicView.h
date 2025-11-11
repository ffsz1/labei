//
//  HJGiftMicView.h
//  HJLive
//
//  Created by apple on 2019/7/9.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface HJGiftMicView : UIView

//指定选中的麦位uid
@property (nonatomic,assign) UserID targetUID;


//获取选中的uids
- (NSString *)getSelUidsString;

//获取选中的第一个uid，用于点击 资料
- (UserID)getFirstSelUID;


//是否选中全麦
- (BOOL)isAllMic;

@end

NS_ASSUME_NONNULL_END
