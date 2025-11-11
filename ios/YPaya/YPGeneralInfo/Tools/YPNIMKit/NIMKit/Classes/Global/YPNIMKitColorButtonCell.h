//
//  YPNIMKitColorButtonCell.h
//  NIM
//
//  Created by chris on 15/3/11.
//  Copyright (c) 2015年 Netease. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger, NIMKitColorButtonCellStyle){
    NIMKitColorButtonCellStyleRed,
    NIMKitColorButtonCellStyleBlue,
};

@class NIMKitColorButton;

@interface YPNIMKitColorButtonCell : UITableViewCell

@property (nonatomic,strong) NIMKitColorButton *button;

@end



@interface NIMKitColorButton : UIButton

@property (nonatomic,assign) NIMKitColorButtonCellStyle style;

@end