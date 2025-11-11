//
//  HJBaseGestureSubColloctionVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJBaseGestureSubColloctionVC.h"

@interface HJBaseGestureSubColloctionVC ()
@end

@implementation HJBaseGestureSubColloctionVC

- (instancetype)init {
    
    if (self = [super init]) {
        
        _page = 1;
        _count = 0;
    }
    
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.canScroll = NO;
    [self.view addSubview:self.collectionView];
}

- (void)viewDidLayoutSubviews {
    
    [super viewDidLayoutSubviews];
    
    [self.collectionView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.leading.trailing.bottom.mas_equalTo(0);
    }];
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    
    if (self.canScroll == NO) {
        scrollView.contentOffset = CGPointZero;
    }
    
    if (scrollView.contentOffset.y <= 0 ) {
        self.canScroll = NO;
        scrollView.contentOffset = CGPointZero;
        self.block();
    }
}

- (void)handlerBlock:(notiBlock)block {
    self.block = block;
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (HJBaseGestureCollectionView *)collectionView {
    if (!_collectionView) {
        UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
        _collectionView = [[HJBaseGestureCollectionView alloc] initWithFrame:CGRectZero collectionViewLayout:layout];
        _collectionView.backgroundColor = [UIColor whiteColor];
        _collectionView.dataSource = self;
        _collectionView.delegate = self;
        _collectionView.scrollEnabled = YES;
        _collectionView.bounces = YES;
        _collectionView.alwaysBounceVertical = YES;
    }
    return _collectionView;
}

@end
