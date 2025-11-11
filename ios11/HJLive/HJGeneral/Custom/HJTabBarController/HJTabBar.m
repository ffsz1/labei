//
//  HJTabBar.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJTabBar.h"
#import "UIView+getTopVC.h"
@implementation HJTabBar

- (instancetype)init{
    if (self = [super init]){
        [self initView];
    }
    return self;
}
- (void)initView{
    self.backgroundColor = [UIColor whiteColor];
    _centerBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    //  设定button大小为适应图片
    UIImage *normalImage = [UIImage imageNamed:@"hj_tabBar_center"];
    _centerBtn.frame = CGRectMake(0, 0, normalImage.size.width, normalImage.size.height);
    [_centerBtn setImage:normalImage forState:UIControlStateNormal];
    //去除选择时高亮
    _centerBtn.adjustsImageWhenHighlighted = NO;
    //根据图片调整button的位置(图片中心在tabbar的中间最上部，这个时候由于按钮是有一部分超出tabbar的，所以点击无效，要进行处理)
    _centerBtn.frame = CGRectMake(([UIScreen mainScreen].bounds.size.width - normalImage.size.width)/2.0, -15, normalImage.size.width, normalImage.size.height);
//    [self addSubview:_centerBtn];
    
//    self.item
    
}

//处理超出区域点击无效的问题
- (UIView *)hitTest:(CGPoint)point withEvent:(UIEvent *)event{
    UIView *view = [super hitTest:point withEvent:event];
    if (view == nil){
        //转换坐标
        CGPoint tempPoint = [self.centerBtn convertPoint:point fromView:self];
        //判断点击的点是否在按钮区域内
        if (CGRectContainsPoint(self.centerBtn.bounds, tempPoint)){
            //返回按钮
            NSLog(@"%@",[self topViewController].className);
//            HJFirstHomeViewController HJMessageVC HJSpeedMatchVC HJMyViewController
            
            NSString *vcName = [self topViewController].className;
            
            if (![vcName isEqualToString:@"PagingViewController"]
                &&![vcName isEqualToString:@"HJMessageVC"]
                &&![vcName isEqualToString:@"HJSpeedMatchVC"]
                &&![vcName isEqualToString:@"HJMyViewController"]) {
                return view;
            }
            
            return _centerBtn;
        }
    }
    return view;
}


// 显示Badge
- (void)showBadgeOnItemIndex:(int)index num:(NSInteger)num{
    
    
    if (num>99) {
        num = 99;
    }
    
    if (index >= self.items.count) {
        return;
    }
    
    if (num == 0) {
        [self hideBadgeOnItemIndex:index];
        return;
    }
    
    // 如果之前添加过，直接设置hidden为NO
    UIView *icon = [self __iconViewWithIndex:index];
    for (UIView *subView in icon.subviews) {
        if (subView.tag == 10000) {
            
            if ([subView isKindOfClass:[UILabel class]]) {
                UILabel *label = (UILabel *)subView;
                label.text = [NSString stringWithFormat:@"%ld",(long)num];
                
                
            }
            
            subView.hidden = NO;
            return;
        }
    }
    
    UILabel *bageLabel = [[UILabel alloc] initWithFrame:CGRectMake(icon.frame.size.width - 10, 0, num>9?18:12, 12)];
    bageLabel.textColor = [UIColor whiteColor];
    bageLabel.backgroundColor = [UIColor colorWithRed:255/255.0 green:43/255.0 blue:129/255.0 alpha:1.0];
    bageLabel.textAlignment = NSTextAlignmentCenter;
    bageLabel.layer.cornerRadius = 6;
    bageLabel.layer.masksToBounds = YES;
    bageLabel.font = [UIFont boldSystemFontOfSize:9];
    bageLabel.text = [NSString stringWithFormat:@"%ld",(long)num];
    bageLabel.tag = 10000;
    
    [icon addSubview:bageLabel];
}

// 隐藏Badge
- (void)hideBadgeOnItemIndex:(int)index {
    UIView *icon = [self __iconViewWithIndex:index];
    for (UIView *subView in icon.subviews) {
        if (subView.tag == 10000) {
            subView.hidden = YES;
        }
    }
}

// 获取图标所在View
- (UIView *)__iconViewWithIndex:(int)index {
    UITabBarItem *item = self.items[index];
    UIView *tabBarButton = [item valueForKey:@"_view"];
//    UIView *icon = [tabBarButton valueForKey:@"_info"];
//    return icon;
    //life
    if (@available(iOS 13.0, *)) {
         UIView *icon = [tabBarButton subviews].firstObject;
         return icon;
    }else{
        UIView *icon = [tabBarButton valueForKey:@"_info"];
        return icon;
    }
}

@end
