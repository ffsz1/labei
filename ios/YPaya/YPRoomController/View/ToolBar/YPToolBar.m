//
//  YPToolBar.m
//  HJLive
//
//  Created by feiyin on 2020/7/15.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPToolBar.h"

@implementation YPToolBar

- (void)layoutTheViews {
    NSInteger index = 0;
    CGFloat barWidth  = kScreenWidth - 20;
    CGFloat distance = 10;
    for (int i = 0; i < self.items.count; i++) {
        YPToolBarButton *item = self.items[i];
        item.tag = i;
        [item addTarget:self action:@selector(itemClick:) forControlEvents:UIControlEventTouchDown];
        [self addSubview:item];
        

        if (item.type == HJToolBarButtonMicroSwitch) {
            item.frame = CGRectMake(self.itemWidth * 0, 0, self.itemWidth, self.itemHeight);
        }else if (item.type == HJToolBarButtonMicroVolumeSwitch){
            item.frame = CGRectMake(self.itemWidth * 1+distance, 0, self.itemWidth, self.itemHeight);
        }else if (item.type == HJToolBarButtonGift) {
            item.frame = CGRectMake(barWidth/2-55/2, 0, 55, 55);
        }else if (item.type == HJToolBarButtonFace){
//            item.frame = CGRectMake(barWidth - self.itemWidth * 3, 0, self.itemWidth, self.itemHeight);
            item.frame = CGRectMake(self.itemWidth * 2+distance*2, 0, self.itemWidth, self.itemHeight);
        }else if (item.type == HJToolBarButtonMessage){
            item.frame = CGRectMake(barWidth - self.itemWidth * 3-distance*2, 0, self.itemWidth, self.itemHeight);
        }else if (item.type == HJToolBarButtonChat){
            item.frame = CGRectMake(barWidth - self.itemWidth * 2 , 0, self.itemWidth*2, self.itemHeight);
        }
        
        if (item.type == HJToolBarButtonChat) {
                    item.frame = CGRectMake(self.itemWidth * 0, 0, self.itemWidth*2, self.itemHeight);
                }else if (item.type == HJToolBarButtonMessage){
                    item.frame = CGRectMake(self.itemWidth * 2+distance, 0, self.itemWidth, self.itemHeight);
                }else if (item.type == HJToolBarButtonMicroVolumeSwitch) {
                    item.frame = CGRectMake(self.itemWidth * 3+distance*2, 0, self.itemWidth, self.itemHeight);
                }else if (item.type == HJToolBarButtonMicroSwitch){
        //            item.frame = CGRectMake(barWidth - self.itemWidth * 3, 0, self.itemWidth, self.itemHeight);
                    item.frame = CGRectMake(self.itemWidth * 4+distance*3, 0, self.itemWidth, self.itemHeight);
                }else if (item.type == HJToolBarButtonFace){
                    item.frame = CGRectMake(self.itemWidth * 5+distance*4, 0, self.itemWidth, self.itemHeight);
                }else if (item.type == HJToolBarButtonGift){
                    item.frame = CGRectMake(barWidth - distance - 55 , 0, 55, 55);
                }
    }
}

- (void)itemClick:(UIButton *)button {
    if (_delegate) {
        YPToolBarButton *item = self.items[button.tag];
        [_delegate toolBar:(YPToolBarButton *)button didSelectItem:button.tag];
    }
}

@end
