//
//  XCDatePicker.h
//  HJLive
//
//  Created by feiyin on 2020/6/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol XCDatePickerDelegate <NSObject>

- (void)dismissDatePicker;
- (void)hadSelectedADate:(NSDate *)date;
@end
@interface XCDatePicker : UIView
@property (nonatomic, copy) NSString *dateStr;
@property (assign, nonatomic) id<XCDatePickerDelegate> delegate;

+ (instancetype)loadFromNib;
@end
