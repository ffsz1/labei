//
//  HJTopAlertViewTool.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HJUpdateView.h"
#import "HJNoticeView.h"
#import "HJBadNetworkAlertView.h"
NS_ASSUME_NONNULL_BEGIN

@interface HJTopAlertViewTool : UIView
typedef enum {
    XCAlertType_Update = 1,
    XCAlertType_badNetwork = 2
}AlertType;

@property (nonatomic, strong) HJUpdateView *updateView;
@property (nonatomic, strong) HJBadNetworkAlertView *badNetworkAlertView;
@property (nonatomic, strong) HJNoticeView *noticeView;

+ (instancetype)shareHJTopAlertViewTool;

- (void)showBadNetworkAlertView;
- (void)showUpdateViewWithDesc:(NSString *)desc version:(NSString *)version;
- (void)showNoticeViewWithDesc:(NSString *)desc version:(NSString *)version;

- (void)showNewUserAlertWithViewController:(UIViewController *)vc;


- (void)hidenAlertView;
- (void)showNewUserAlertWithViewController:(UIViewController *)vc
                                    roomId:(NSInteger)roomId
                                       uid:(NSInteger)uid
                                     title:(NSString *)title
                                    avatar:(NSString *)avatar;
@end

NS_ASSUME_NONNULL_END
