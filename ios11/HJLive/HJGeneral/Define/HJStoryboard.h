//
//  HJStoryboard.h
//  HJLive
//
//  Created by feiyin on 2020/6/30.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#ifndef HJStoryboard_h
#define HJStoryboard_h

#define HJBindingStoryBoard(VCName) [[UIStoryboard storyboardWithName:@"HJBinding" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]
#define HJWalletStoryBoard(VCName) [[UIStoryboard storyboardWithName:@"HJWallet" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]
#define HJRoomStoryBoard(VCName) [[UIStoryboard storyboardWithName:@"HJRoom" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]

#define HJHomeStoryBoard(VCName) [[UIStoryboard storyboardWithName:@"HJHome" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]

#define HJSetPasswordtoryBoard(VCName) [[UIStoryboard storyboardWithName:@"HJSetPassword" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]

#define HJMeStoryBoard(VCName) [[UIStoryboard storyboardWithName:@"HJMe" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]
#define HJSignStoryBoard(VCName) [[UIStoryboard storyboardWithName:@"HJSign" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]

#define HJLoginStoryBoard(VCName) [[UIStoryboard storyboardWithName:@"HJLogin" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]

#define HJSettingBoard(VCName) [[UIStoryboard storyboardWithName:@"HJSetting" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]

#define HJMessageBoard(VCName) [[UIStoryboard storyboardWithName:@"HJMessage" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]

#endif /* HJStoryboard_h */
