//
//  YPGiftBoxView.h
//  HJLive
//
//  Created by apple on 2019/7/9.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPGiftBoxView : UIView
//+ (instancetype)shareGiftBoxView;

//展示用户送礼框（单人）
+ (void)show:(UserID)tagUID;

//展示麦上用户送礼框（多人）
+ (void)showAllMic:(UserID)tagUID;

//展示私聊送礼框（单人）
+ (void)showChat:(UserID)tagUID;


- (void)close;

@property (weak, nonatomic) IBOutlet UIView *normalItem;
@property (weak, nonatomic) IBOutlet UIView *bagItem;

@property (weak, nonatomic) IBOutlet UIView *pointItem;

@end

NS_ASSUME_NONNULL_END
