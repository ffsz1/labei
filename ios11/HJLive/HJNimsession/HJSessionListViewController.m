//
//  HJSessionListViewController.m
//  HJLive
//
//  Created by feiyin on 2020/6/26.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJSessionListViewController.h"
#import "HJSessionViewController.h"
#import "Attachment.h"
#import "HJOpenLiveAttachment.h"
#import "HJRedPacketInfoAttachment.h"
#import "HJNewsInfoAttachment.h"
#import "UIView+XCToast.h"
#import "HJGiftAttachment.h"
#import "TurntableAttachment.h"
#import "UIColor+UIColor_Hex.h"
#import "HJImLoginCoreClient.h"
#import <NIMSDK/NIMSDK.h>
#import "HJMySpaceVC.h"
#import "HJUserViewControllerFactory.h"

#import "HJEmptyView.h"

@interface HJSessionListViewController () <HJImLoginCoreClient>

@property (nonatomic,strong) HJEmptyView *tipView;


@end

@implementation HJSessionListViewController

- (void)dealloc {
    NSLog(@"dealloc");
}

- (void)zj_viewWillAppearForIndex:(NSInteger)index{
    [self refresh];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    [self refresh];
}

//
//- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
//{
//    // 输出点击的view的类名
//    NSLog(@"%@", NSStringFromClass([touch.view class]));
//
//    // 若为UITableViewCellContentView（即点击了tableViewCell），则不截获Touch事件
//    if ([NSStringFromClass([touch.view class]) isEqualToString:@"UITableViewCellContentView"]) {
//        return NO;
//    }
//    return  YES;
//}

- (void)viewDidLoad {
    [super viewDidLoad];
    AddCoreClient(HJImLoginCoreClient, self);
    // Do any additional setup after loading the view.
    self.view.backgroundColor = [UIColor whiteColor];
    self.tableView.backgroundColor = [UIColor whiteColor];
//    self.tableView.contentInset = UIEdgeInsetsMake(0, 0, 0, 0);
//    self.tableView.separatorColor = [UIColor colorWithHexString:@"#f5f5f5"];
    self.tableView.backgroundColor = [UIColor whiteColor];
    


    
    
    
    
//    UIView *headerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, 40)];
//    headerView.backgroundColor = [UIColor redColor];
//    self.tableView.tableHeaderView = headerView;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
//- (void)onDeleteRecentAtIndexPath:(NIMRecentSession *)recent atIndexPath:(NSIndexPath *)indexPath {
//
//}

- (void)onSelectedRecent:(NIMRecentSession *)recent atIndexPath:(NSIndexPath *)indexPath {
    HJSessionViewController *vc = [[HJSessionViewController alloc] initWithSession:recent.session];
    [vc.tableView reloadData];
    [self.navigationController pushViewController:vc animated:YES];
}

- (NSAttributedString *)contentForRecentSession:(NIMRecentSession *)recent{
    NSAttributedString *content;
    if (recent.lastMessage.messageType == NIMMessageTypeCustom)
    {
        NIMCustomObject *obj = (NIMCustomObject *)recent.lastMessage.messageObject;
        NSString *text = @"";
        if ([obj.attachment isKindOfClass:[HJOpenLiveAttachment class]]) {
            text = recent.lastMessage.apnsContent;
        }else if ([obj.attachment isKindOfClass:[HJRedPacketInfoAttachment class]]) {
            text = @"[新人福利]";
        }else if ([obj.attachment isKindOfClass:[HJNewsInfoAttachment class]]) {
            text = @"[推文]";
        }else if ([obj.attachment isKindOfClass:[HJGiftAttachment class]]) {
            text = @"[礼物]";
        }else if ([obj.attachment isKindOfClass:[TurntableAttachment class]]){
            text = @"[活动]";
        } else
        {
            if (recent.lastMessage.apnsContent.length > 0) {
                text = recent.lastMessage.apnsContent;
            }else {
                text = @"[未知消息]";
            }
            
        }
        Attachment *att = (Attachment *)obj.attachment;
        
        if (att.first == Custom_Noti_Header_Gift) {
            if (att.second == Custom_Noti_Sub_Gift_Send) {
                text = @"[礼物]";
            }
        }
        if (att.first == 51) {
           
                text = @"[红包]";
            
        }
        if (att.first == Custom_Noti_Header_NotiInviteRoom) {
            if (att.second == Custom_Noti_Header_NotiInviteRoom) {
                text = @"[房间邀请]";
            }
        }
        
        if (att.first == Custom_Noti_Header_RedPacket) {
            text = @"[新人福利]";
        }
        
        if (att.first == Custom_Noti_Header_News) {
            if (att.second == Custom_Noti_Sub_News) {
               text = @"[推文]";
            }
        }
        
        if (att.first == Custom_Noti_Header_CustomMsg) {
            if (att.second == Custom_Noti_Sub_Online_alert) {
                text = recent.lastMessage.apnsContent;
            }
        }
        
        content = [[NSAttributedString alloc] initWithString:text];
    }
    else
    {
        content = [super contentForRecentSession:recent];
    }
    NSMutableAttributedString *attContent = [[NSMutableAttributedString alloc] initWithAttributedString:content];
    return attContent;
}

- (void)refresh{
    [super refresh];
    if (self.recentSessions.count > 0) {
        self.tableView.tableFooterView = nil;
    }else {
        
        self.tableView.tableFooterView = self.tipView;
    }
    [self.tableView reloadData];
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (self.roomMessageListDidSelectCell) {
        self.roomMessageListDidSelectCell(indexPath.row);
    }
    else {
        [super tableView:tableView didSelectRowAtIndexPath:indexPath];
    }
}

#pragma mark - ImLoginCoreClient
- (void)onImLogoutSuccess {
    [self.recentSessions removeAllObjects];
}

- (void)onImSyncSuccess {
    if ([NIMSDK sharedSDK].conversationManager.allRecentSessions.count) {
        [self setValue:[[NIMSDK sharedSDK].conversationManager.allRecentSessions mutableCopy] forKey:NSStringFromSelector(@selector(recentSessions))];
        [self refresh];
    }
    
}

- (HJEmptyView *)tipView
{
    if (!_tipView) {
        _tipView = [[HJEmptyView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, 428)];
        [_tipView setTitle:@"暂无最新消息" image:@"blank"];
    }
    return _tipView;
}


@end
