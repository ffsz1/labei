//
//  UITableView+MJRefresh.h
//  HJLive
//
//  Created by FF on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

/*
 
 tip：
 用于MJRefresh header/footer快速设置
 
 */

#import <UIKit/UIKit.h>
#import <MJRefresh.h>

typedef void(^FreshBlock)(void);


@interface UITableView (MJRefresh)

//用于获取当前页码
@property (nonatomic,assign) NSInteger page;

//需要header/footer就设置下面两属性，不设置则为不显示
@property (nonatomic,copy) FreshBlock headerBlock;
@property (nonatomic,copy) FreshBlock footerBlock;

@end
