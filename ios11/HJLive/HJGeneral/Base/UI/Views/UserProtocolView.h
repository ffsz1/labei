//
//  UserProtocolView.h
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol UserProtocolViewDelegate<NSObject>
- (void) onSelectBtnClicked:(BOOL) select;
@end

@interface UserProtocolView : UIView
@property(nonatomic, weak) UINavigationController *nav;
@property(nonatomic, weak) id<UserProtocolViewDelegate> delegate;
- (BOOL) isSelect;
+ (instancetype)loadFromNIB;
@end
