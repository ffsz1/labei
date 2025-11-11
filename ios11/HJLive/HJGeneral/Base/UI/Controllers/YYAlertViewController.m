//
//  YYAlertViewController.m
//  CustomAlertView
//
//  Created by yangmengjun on 15/5/11.
//  Copyright (c) 2015年 YY.inc. All rights reserved.
//

#import "YYAlertViewController.h"
#import "YYTheme.h"
#import "YYUtility.h"

static float kButtonHeight = 44.0f;
static NSString *kCellIdentifier = @"YYAlertViewCollectionCell";

@interface YYAlertViewCollectionCell : UICollectionViewCell

- (void)setButtonTitle:(NSString *)title textColor:(UIColor *)color backgroundColor:(UIColor *)backgroundColor;

- (void)setButtonView:(UIView *)button backgroundColor:(UIColor *)backgroundColor;

@end

@implementation YYAlertViewCollectionCell

- (void)setButtonTitle:(NSString *)title
             textColor:(UIColor *)color
       backgroundColor:(UIColor *)backgroundColor;
{
    UILabel *label = [[UILabel alloc] init];
    [label setText:title];
    [label setTextAlignment:NSTextAlignmentCenter];
    [label setLineBreakMode:NSLineBreakByTruncatingMiddle];
    [label setFont:[UIFont systemFontOfSize:17]];
    [label setTextColor:color];
    
    [self setButtonView:label backgroundColor:backgroundColor];
}

- (void)setButtonView:(UIView *)button backgroundColor:(UIColor *)backgroundColor
{
    [self.contentView setBackgroundColor:backgroundColor];
    
    [[self.contentView subviews] makeObjectsPerformSelector:@selector(removeFromSuperview)];
    
    button.translatesAutoresizingMaskIntoConstraints = NO;
    
    [self.contentView addSubview:button];
    
    [self.contentView addConstraints:[NSLayoutConstraint
                                        constraintsWithVisualFormat:@"H:|-0-[button]-0-|"
                                        options:NSLayoutFormatDirectionLeadingToTrailing
                                        metrics:nil
                                        views:NSDictionaryOfVariableBindings(button)]];
    [self.contentView addConstraints:[NSLayoutConstraint
                                        constraintsWithVisualFormat:@"V:|-0-[button]-0-|"
                                        options:NSLayoutFormatDirectionLeadingToTrailing
                                        metrics:nil
                                        views:NSDictionaryOfVariableBindings(button)]];
}

@end

@interface YYAlertViewController ()<UICollectionViewDelegate, UICollectionViewDataSource, UIViewControllerTransitioningDelegate, UIViewControllerAnimatedTransitioning>

@property (strong, nonatomic) NSMutableArray *titles;
@property (strong, nonatomic) NSMutableArray *blocks;
@property (weak, nonatomic) IBOutlet UIView *containerView;
@property (strong, nonatomic) UIView *contentView;
@property (weak, nonatomic) IBOutlet UICollectionView *collectionView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *containerHeightConstraint;
@property (assign, nonatomic) CGFloat defaultContentViewBottomConstant;
@property (assign, nonatomic) BOOL isObserveKeyBoard;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *layoutContentViewBottom;
@end

@implementation YYAlertViewController

- (instancetype)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nil];
    
    if (self) {
        [self initData];
        
        self.modalPresentationStyle = UIModalPresentationCustom;
        self.transitioningDelegate = self;
    }
    
    return self;
}

- (void)initData{
    _blocks = [[NSMutableArray alloc] init];
    _titles = [[NSMutableArray alloc] init];
}

- (void)viewDidLoad {
    [super viewDidLoad];

    if (self.dialogBackgroundColor == nil) {
        self.dialogBackgroundColor = [UIColor whiteColor];
    }
    
    [self.containerView setBackgroundColor:self.dialogBackgroundColor];
    
    if (self.separatorLineColor == nil) {
        self.separatorLineColor = RGBCOLOR(217, 217, 217);
    }
    
    [self.collectionView setBackgroundColor:self.separatorLineColor];
    
    self.containerHeightConstraint.constant = MAX(90, kButtonHeight + CGRectGetHeight(self.contentView.frame));
    
    self.containerView.layer.cornerRadius = 4;
    self.containerView.layer.masksToBounds = YES;
    
    if (_contentView) {

        _contentView.translatesAutoresizingMaskIntoConstraints = NO;
        [self.containerView addSubview:_contentView];
        
        UIView *contentView = _contentView;
        [self.containerView addConstraints:[NSLayoutConstraint
                                            constraintsWithVisualFormat:@"H:|-0-[contentView]-0-|"
                                            options:NSLayoutFormatDirectionLeadingToTrailing
                                            metrics:nil
                                            views:NSDictionaryOfVariableBindings(contentView)]];
        [self.containerView addConstraints:[NSLayoutConstraint
                                            constraintsWithVisualFormat:@"V:|-0-[contentView]-44-|"
                                            options:NSLayoutFormatDirectionLeadingToTrailing
                                            metrics:nil
                                            views:NSDictionaryOfVariableBindings(contentView)]];
    }
    
    self.collectionView.delegate = self;
    self.collectionView.dataSource = self;
    
    [self.collectionView registerClass:[YYAlertViewCollectionCell class] forCellWithReuseIdentifier:kCellIdentifier];
}

-(void)viewWillAppear:(BOOL)animated
{
    // 使ContentView居中显示
    [super viewWillAppear:animated];
    CGFloat screenHeight = [UIScreen mainScreen].applicationFrame.size.height;
    
    self.defaultContentViewBottomConstant = (screenHeight - self.containerView.frame.size.height) / 2;
    self.layoutContentViewBottom.constant = self.defaultContentViewBottomConstant;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)regKeyboardNotifications
{
    //使用NSNotificationCenter 键盘出现
    [[NSNotificationCenter defaultCenter] addObserver:self
     
                                             selector:@selector(keyboardWillShow:)
     
                                                 name:UIKeyboardWillShowNotification object:nil];
    
    //使用NSNotificationCenter 键盘隐藏
    [[NSNotificationCenter defaultCenter] addObserver:self
     
                                             selector:@selector(keyboardWillHide:)
     
                                                 name:UIKeyboardWillHideNotification object:nil];
}

//取消监听键盘活动
- (void)unRegKeyboardNotifications
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardDidShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillHideNotification object:nil];
}

//实现当键盘出现的时候计算键盘的高度大小。用于输入框显示位置
- (void)keyboardWillShow:(NSNotification*)aNotification
{
    CGFloat topMargin = 8.0;  // 键盘离ContentView高度
    NSDictionary* info = [aNotification userInfo];
    CGSize kbSize = [[info objectForKey:UIKeyboardFrameEndUserInfoKey] CGRectValue].size;
    NSLog(@"hight_hitht:%f",kbSize.height);
    CGFloat distance = kbSize.height + topMargin - self.defaultContentViewBottomConstant;
    if(distance > 0){
        [self beginContentViewAnimation:kbSize.height + topMargin];
    }else if(self.layoutContentViewBottom.constant != self.defaultContentViewBottomConstant){
        [self beginContentViewAnimation:self.defaultContentViewBottomConstant];
    }
}

//当键盘隐藏的时候
- (void)keyboardWillHide:(NSNotification*)aNotification
{
    if(self.layoutContentViewBottom.constant != self.defaultContentViewBottomConstant){
        [self beginContentViewAnimation:self.defaultContentViewBottomConstant];
    }
}

//调整ContentView底部距离
-(void)beginContentViewAnimation:(CGFloat)constant
{
    [UIView animateWithDuration:0.2 delay:0.0 options:UIViewAnimationCurveEaseInOut|UIViewAnimationOptionBeginFromCurrentState animations:^{
        self.layoutContentViewBottom.constant = constant;
        [self.view layoutIfNeeded];
    } completion:nil];
}

//设置是否监听键盘变化
- (void)setIsObserveKeyBoard:(BOOL)isObserveKeyBoard
{
    if(!_isObserveKeyBoard && isObserveKeyBoard){
        [self regKeyboardNotifications];
    }else if(_isObserveKeyBoard && !isObserveKeyBoard){
        [self unRegKeyboardNotifications];
    }
    
    _isObserveKeyBoard = isObserveKeyBoard;
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (id <UIViewControllerAnimatedTransitioning>)animationControllerForPresentedController:(UIViewController *)presented presentingController:(UIViewController *)presenting sourceController:(UIViewController *)source
{
    return self;
}

-(id<UIViewControllerAnimatedTransitioning>)animationControllerForDismissedController:(UIViewController *)dismissed {
    return self;
}

-(NSTimeInterval)transitionDuration:(id<UIViewControllerContextTransitioning>)transitionContext {
    return 0.25;
}

-(void)animateTransition:(id<UIViewControllerContextTransitioning>)transitionContext {
    UIViewController* vc1 = [transitionContext viewControllerForKey:UITransitionContextFromViewControllerKey];
    UIViewController* vc2 = [transitionContext viewControllerForKey:UITransitionContextToViewControllerKey];
    UIView* con = [transitionContext containerView];
    UIView* v1 = vc1.view;
    UIView* v2 = vc2.view;
    
    __weak typeof(self) wself = self;
    
    if (vc2 == self) { // presenting
        [con addSubview:v2];
        v2.frame = v1.frame;
        wself.containerView.transform = CGAffineTransformMakeScale(1.6,1.6);
        v2.alpha = 0;
        v1.tintAdjustmentMode = UIViewTintAdjustmentModeDimmed;
        [UIView animateWithDuration:0.25 animations:^{
            v2.alpha = 1;
            wself.containerView.transform = CGAffineTransformIdentity;
        } completion:^(BOOL finished) {
            [transitionContext completeTransition:YES];
        }];
    }
    else { // dismissing
        [UIView animateWithDuration:0.25 animations:^{
            wself.containerView.transform = CGAffineTransformMakeScale(0.5,0.5);
            v1.alpha = 0;
        } completion:^(BOOL finished) {
            v2.tintAdjustmentMode = UIViewTintAdjustmentModeAutomatic;
            [transitionContext completeTransition:YES];
        }];
    }
}

#pragma mark - UICollectionDelegate
- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.row < [self.blocks count]) {
        YYAlertViewControllerButtonBlock block = [self.blocks objectAtIndex:indexPath.row];
        
        __weak typeof(self) wself = self;
        [self dismissViewControllerAnimated:YES completion:^{
            block(wself);
        }];
    }
}

#pragma mark - UICollectionViewDataSource
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    
    YYAlertViewCollectionCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:kCellIdentifier
                                              forIndexPath:indexPath];
    
    if (indexPath.row < [self.titles count]) {
        id title =  [self.titles objectAtIndex:indexPath.row];
        if ([title isKindOfClass:[NSString class]]) {
            
            if ([[YYUtility systemVersion] floatValue] >= 8.0) {
                [cell setButtonTitle:title
                           textColor:[UIApplication sharedApplication].keyWindow.tintColor
                     backgroundColor:self.dialogBackgroundColor];
            }
            else {
                [cell setButtonTitle:title
                           textColor:[[UIButton buttonWithType:UIButtonTypeSystem] tintColor]
                     backgroundColor:self.dialogBackgroundColor];
            }
        }
        else if ([title isKindOfClass:[UIView class]]) {
            [cell setButtonView:title backgroundColor:self.dialogBackgroundColor];
        }
    }

    return cell;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return [self.blocks count];
}

- (BOOL)collectionView:(UICollectionView *)collectionView shouldHighlightItemAtIndexPath:(NSIndexPath *)indexPath
{
    return YES;
}

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView
{
    return 1;
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section {
    return 0.5;
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section {
    return 0;
}

- (UIEdgeInsets)collectionView:(UICollectionView*)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section {
    return UIEdgeInsetsMake(0.5, 0, 0, 0); // top, left, bottom, right
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath{
    
    if ([self.blocks count] > 0) {
        CGFloat width = (CGRectGetWidth(self.collectionView.frame) / [self.blocks count]);
        
        if (indexPath.row != [self.blocks count] - 1) {
            width -= 0.5;
        }
        
        return CGSizeMake(width, kButtonHeight);
    }
    
    return CGSizeMake(CGRectGetWidth(self.collectionView.frame), kButtonHeight);
}

- (void)addButtonWithView:(UIView *)view block:(YYAlertViewControllerButtonBlock)block
{
    [self.titles addObject:view];
    [self.blocks addObject:[block copy]];
}

- (void)addButtonWithTitle:(NSString *)title block:(YYAlertViewControllerButtonBlock) block
{
    [self.titles addObject:[title copy]];
    [self.blocks addObject:[block copy]];
}

- (void)show
{
    UIViewController *current = [self currentVisiableRootViewController];
    
    [current presentViewController:self animated:YES completion:nil];
}

- (void)dismiss
{
    [self dismissViewControllerAnimated:YES completion:NULL];
}

- (void)dismissCompletion:(dispatch_block_t)completion
{
    [self dismissViewControllerAnimated:YES completion:completion];
}

- (void)addContentView:(UIView *)contentView
{
    if ([_contentView superview]) {
        [_contentView removeFromSuperview];
    }
    
    _contentView = contentView;
}

- (UIViewController *)currentVisiableRootViewController
{
    if (self.delegate != nil && [self.delegate respondsToSelector:@selector(currentVisiableRootViewController)]) {
        return [self.delegate currentVisiableRootViewController];
    }
    
    return [self delaultRootViewController];
}

- (UIViewController *)delaultRootViewController
{
    __block UIViewController *result = nil;
    // Try to find the root view controller programmically
    // Find the top window (that is not an alert view or other window)
    UIWindow *topWindow = [[UIApplication sharedApplication] keyWindow];
    if (topWindow.windowLevel != UIWindowLevelNormal)
    {
        NSArray *windows = [[UIApplication sharedApplication] windows];
        for(topWindow in windows)
        {
            if (topWindow.windowLevel == UIWindowLevelNormal)
                break;
        }
    }
    
    NSArray *windowSubviews = [topWindow subviews];
    
    [windowSubviews enumerateObjectsWithOptions:NSEnumerationReverse usingBlock:
     ^(id obj, NSUInteger idx, BOOL *stop) {
         UIView *rootView = obj;
         
         if ([NSStringFromClass([rootView class]) isEqualToString:@"UITransitionView"]) {
             
             NSArray *aSubViews = rootView.subviews;
             
             [aSubViews enumerateObjectsUsingBlock:^(id obj, NSUInteger idx, BOOL *stop) {
                 UIView *tempView = obj;
                 
                 id nextResponder = [tempView nextResponder];
                 
                 if ([nextResponder isKindOfClass:[UIViewController class]]) {
                     result = nextResponder;
                     *stop = YES;
                 }
             }];
             *stop = YES;
         } else {
             
             id nextResponder = [rootView nextResponder];
             
             if ([nextResponder isKindOfClass:[UIViewController class]]) {
                 result = nextResponder;
                 *stop = YES;
             }
         }
     }];
    
    if (result == nil && [topWindow respondsToSelector:@selector(rootViewController)] && topWindow.rootViewController != nil) {
        result = topWindow.rootViewController;
    }
    
    return result;

}

@end
