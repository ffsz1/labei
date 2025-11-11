//
//  HJToolBarButton.h
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum : NSUInteger {
    HJToolBarButtonMicroSwitch,
    HJToolBarButtonMicroVolumeSwitch,
    HJToolBarButtonChat,
    HJToolBarButtonFace,
    HJToolBarButtonShare,
    HJToolBarButtonMicroQue,
    HJToolBarButtonMessage,
    HJToolBarButtonGift // 礼物
    
} HJToolBarButtonType;

@interface HJToolBarButton : UIButton

@property (nonatomic, assign) BOOL isShowRed;

@property (nonatomic, strong) UIImage *normalIcon;
@property (nonatomic, strong) UIImage *disableIcon;
@property (nonatomic, strong) UIImage *selectedIcon;

@property (nonatomic, strong) NSMethodSignature *selectorSign;

@property (nonatomic, assign) HJToolBarButtonType type;

@end
