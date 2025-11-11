//
//  YPStoryboard.h
//  HJLive
//
//  Created by feiyin on 2020/6/30.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#ifndef YPStoryboard_h
#define YPStoryboard_h

#define YPBindingStoryBoard(VCName) [[UIStoryboard storyboardWithName:@"YPBinding" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]
#define YPWalletStoryBoard(VCName) [[UIStoryboard storyboardWithName:@"YPWallet" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]
#define YPRoomStoryBoard(VCName) [[UIStoryboard storyboardWithName:@"YPRoom" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]

#define YPHomeStoryBoard(VCName) [[UIStoryboard storyboardWithName:@"YPHome" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]

#define YPSetPasswordtoryBoard(VCName) [[UIStoryboard storyboardWithName:@"YPSetPassword" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]

#define YPMeStoryBoard(VCName) [[UIStoryboard storyboardWithName:@"YPMe" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]
#define YPSignStoryBoard(VCName) [[UIStoryboard storyboardWithName:@"YPSign" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]

#define YPLoginStoryBoard(VCName) [[UIStoryboard storyboardWithName:@"YPLogin" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]

#define YPSettingBoard(VCName) [[UIStoryboard storyboardWithName:@"YPSetting" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]

#define YPMessageBoard(VCName) [[UIStoryboard storyboardWithName:@"YPMessage" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]

#endif /* YPStoryboard_h */
