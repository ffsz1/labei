//
//  YPMessageTableView.h
//  HJLive
//
//  Created by feiyin on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YPRoomMsgHeaderView.h"

typedef NS_ENUM(NSInteger, HJMessageType) { ///< 公屏聊天消息类型
    HJMessageTypeAll   = 0 ,                ///<全部消息
    HJMessageTypeTalk   = 1,                ///< 聊天消息
    HJMessageTypeGift   = 2,                ///< 礼物消息
    HJMessageTypeDaCall  = 3,               ///< 打Call消息
    
};

@protocol MessageTableViewDelegate <NSObject>

- (void)showUserInfoCardWithUid:(UserID)uid;

- (void)showGiftWithUid:(UserID)uid withName:(NSString *)name;

@end

@interface YPMessageTableView : UIView
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property(nonatomic, strong) NSMutableArray* messages; //消息
@property(nonatomic, weak) UINavigationController *navigationController;
@property(nonatomic, weak) id<MessageTableViewDelegate> delegate;
@property(nonatomic, strong) YPRoomMsgHeaderView* headerView;
@property(nonatomic, assign) HJMessageType talkMessageType;


+ (instancetype)loadFromNib;
- (void)reloadData;
- (void)scrollTableViewMessageListToBottoWithAnimated;
- (void)showPersonalViewForFrom:(UserID)uid withName:(NSString *)name;
//- (IBAction)gotoBottom:(UIButton *)messageTipButton;
@end
