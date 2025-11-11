//
//  YPYYActionSheetViewController.m
//  YYMobile
//
//  Created by yangmengjun on 15/5/14.
//  Copyright (c) 2015年 YY.inc. All rights reserved.
//

#import "YPYYActionSheetViewController.h"
#import "YYUtility.h"
#import "YPYYMainTabBarController.h"
#import "YPYYDefaultTheme.h"

const static float kCornerRadius = 0;

@interface YYActionSheetViewCell : UITableViewCell

@property(nonatomic,assign) BOOL top, bottom;

- (void)setButtonTitle:(NSString *)title
            buttonType:(YYActionSheetButtonType)buttonType
       backgroundColor:(UIColor *)backgroundColor
                cancel:(BOOL)cancel;

- (void)setButtonView:(UIView *)button backgroundColor:(UIColor *)backgroundColor;

- (void)roundCornersAtTop;

- (void)roundCornersAtBottom;

@end

@implementation YYActionSheetViewCell

- (void)setButtonTitle:(NSString *)title
             buttonType:(YYActionSheetButtonType)buttonType
       backgroundColor:(UIColor *)backgroundColor
                cancel:(BOOL)cancel;
{
    UILabel *label = [[UILabel alloc] init];
    [label setText:title];
    [label setTextAlignment:NSTextAlignmentCenter];
    [label setLineBreakMode:NSLineBreakByTruncatingMiddle];
    
    if (cancel) {
        [label setFont:[UIFont systemFontOfSize:17]];
    }
    else {
        [label setFont:[UIFont systemFontOfSize:17]];
    }
    
    UIColor *textColor = nil;
        
    if(buttonType == YYActionSheetButtonTypeDefault){
        if ([[YYUtility systemVersion] floatValue] >= 8.0) {
//            textColor = [UIApplication sharedApplication].keyWindow.tintColor;
            textColor = [UIColor blackColor];
        }
        else {
            textColor = [[UIButton buttonWithType:UIButtonTypeSystem] tintColor];
        }
    }else if(buttonType == YYActionSheetButtonTypeRed){
        textColor = [UIColor redColor];
    }
    
    [label setTextColor:textColor];
    
    [self setButtonView:label backgroundColor:backgroundColor];
}

- (void)setButtonView:(UIView *)button backgroundColor:(UIColor *)backgroundColor
{
    [self.contentView setBackgroundColor:backgroundColor];
    
    [[self.contentView subviews] makeObjectsPerformSelector:@selector(removeFromSuperview)];
    
    button.translatesAutoresizingMaskIntoConstraints = NO;
    
    [self.contentView addSubview:button];
    
    [self.contentView addConstraints:[NSLayoutConstraint
                                      constraintsWithVisualFormat:@"H:|-5-[button]-5-|"
                                      options:NSLayoutFormatDirectionLeadingToTrailing
                                      metrics:nil
                                      views:NSDictionaryOfVariableBindings(button)]];
    [self.contentView addConstraints:[NSLayoutConstraint
                                      constraintsWithVisualFormat:@"V:|-5-[button]-5-|"
                                      options:NSLayoutFormatDirectionLeadingToTrailing
                                      metrics:nil
                                      views:NSDictionaryOfVariableBindings(button)]];
}

- (void)roundCornersAtTop
{
    self.top = YES;
}

- (void)roundCornersAtBottom
{
    self.bottom = YES;
}

- (void)noCorner
{
    self.top = NO;
    self.bottom = NO;
}

- (void)layoutSubviews
{
    [super layoutSubviews];
    
    if(self.top && self.bottom)
    {
        self.layer.cornerRadius = kCornerRadius;
        self.layer.masksToBounds = YES;
    }
    else if (self.top)
    {
        CAShapeLayer *shape = [[CAShapeLayer alloc] init];
        shape.path = [UIBezierPath bezierPathWithRoundedRect:CGRectMake(0, 0, self.bounds.size.width, self.bounds.size.height) byRoundingCorners:UIRectCornerTopLeft|UIRectCornerTopRight cornerRadii:CGSizeMake(kCornerRadius, kCornerRadius)].CGPath;
        self.layer.mask = shape;
        self.layer.masksToBounds = YES;
    }
    else if (self.bottom)
    {
        CAShapeLayer *shape = [[CAShapeLayer alloc] init];
        shape.path = [UIBezierPath bezierPathWithRoundedRect:CGRectMake(0, 0, self.bounds.size.width, self.bounds.size.height) byRoundingCorners:UIRectCornerBottomLeft|UIRectCornerBottomRight cornerRadii:CGSizeMake(kCornerRadius, kCornerRadius)].CGPath;
        self.layer.mask = shape;
        self.layer.masksToBounds = YES;
    }
}

@end

static float kButtonHeight = 50.0f;
static NSString *kCellIdentifier = @"YYActionSheetViewCell";

@interface YPYYActionSheetViewController ()<UITableViewDelegate, UITableViewDataSource, UIViewControllerTransitioningDelegate, UIViewControllerAnimatedTransitioning, UIGestureRecognizerDelegate>

@property (strong, nonatomic) NSMutableArray *titles;
@property (strong, nonatomic) NSMutableArray *blocks;
@property (strong, nonatomic) NSMutableArray *titleColors;

@property (strong, nonatomic) NSMutableArray *cancelTitles;
@property (strong, nonatomic) NSMutableArray *cancelBlocks;
@property (weak, nonatomic) IBOutlet UIView *containerView;
@property (strong, nonatomic) UIView *contentView;
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *containerHeightConstraint;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *tableViewTopConstraint;

@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (strong, nonatomic) NSString *titleText;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *containerViewBottomConstraint;

@end

@implementation YPYYActionSheetViewController

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
    _titleColors = [[NSMutableArray alloc] init];

    _cancelBlocks = [[NSMutableArray alloc] init];
    _cancelTitles = [[NSMutableArray alloc] init];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.tableView.contentInset = UIEdgeInsetsMake(-1.0f, 0.0f, 0.0f, 0.0);
    
    if (self.dialogBackgroundColor == nil) {
        self.dialogBackgroundColor = [UIColor clearColor];
    }
    
    [self.containerView setBackgroundColor:self.dialogBackgroundColor];
    
    if (self.separatorLineColor != nil) {
        [self.tableView setSeparatorColor:self.separatorLineColor];
    }else{
        [self.tableView setSeparatorColor:[[YPYYDefaultTheme alloc] colorWithHexString:@"#d9d9d9" alpha:1.0]];
    }
    
    [self.tableView setBackgroundColor:self.dialogBackgroundColor];
    
    float tableViewHeight = kButtonHeight * ([self.titles count]  + [self.cancelTitles count]) + 8;
    float contentViewHeight = MAX(CGRectGetHeight(self.titleLabel.frame), CGRectGetHeight(self.contentView.frame));
    self.containerHeightConstraint.constant = MAX(90, tableViewHeight + contentViewHeight);
    self.tableViewTopConstraint.constant = contentViewHeight;
    
    self.containerView.layer.cornerRadius = kCornerRadius;
    self.containerView.layer.masksToBounds = YES;
    self.titleLabel.hidden = YES;
    
    if (self.contentView) {
        self.contentView.translatesAutoresizingMaskIntoConstraints = NO;
        [self.containerView addSubview:self.contentView];
        
        UIView *contentView = self.contentView;
        [self.containerView addConstraints:[NSLayoutConstraint
                                            constraintsWithVisualFormat:@"H:|-0-[contentView]-0-|"
                                            options:NSLayoutFormatDirectionLeadingToTrailing
                                            metrics:nil
                                            views:NSDictionaryOfVariableBindings(contentView)]];
        [self.containerView addConstraints:[NSLayoutConstraint
                                            constraintsWithVisualFormat:@"V:|-0-[contentView]-tableViewHeight-|"
                                            options:NSLayoutFormatDirectionLeadingToTrailing
                                            metrics: @{@"tableViewHeight":@(tableViewHeight)}
                                            views:NSDictionaryOfVariableBindings(contentView)]];
    }
    else if (self.titleText != nil){
        self.titleLabel.hidden = NO;
        self.titleLabel.text = self.titleText;
    }
    
    
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    
    [self.tableView registerClass:[YYActionSheetViewCell class] forCellReuseIdentifier:kCellIdentifier];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillLayoutSubviews
{
    [super viewWillLayoutSubviews];
    if ([self.tableView respondsToSelector:@selector(setSeparatorInset:)]) {
        [self.tableView setSeparatorInset:UIEdgeInsetsZero];
    }
    
    if ([self.tableView respondsToSelector:@selector(setLayoutMargins:)]) {
        [self.tableView setLayoutMargins:UIEdgeInsetsZero];
    }
    
}

- (UIStatusBarStyle)preferredStatusBarStyle
{
    return UIStatusBarStyleLightContent;
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

#pragma mark - UITableViewDelegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSMutableArray *blocks = nil;
    if (indexPath.section == 0) {
        blocks = self.blocks;
    }
    else {
        blocks = self.cancelBlocks;
    }
    
    if (indexPath.row < [blocks count]) {
        YYActionSheetViewControllerButtonBlock block = [blocks objectAtIndex:indexPath.row];
        
        __weak typeof(self) wself = self;
        [self dismissViewControllerAnimated:YES completion:^{
            block(wself);
        }];
    }
}

#pragma mark - UITableViewDataSource
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    YYActionSheetViewCell *cell = [tableView dequeueReusableCellWithIdentifier:kCellIdentifier forIndexPath:indexPath];
    
    NSMutableArray *titles = nil;
    BOOL cancel = NO;
    if (indexPath.section == 0) {
        titles = self.titles;
    }
    else {
        titles = self.cancelTitles;
        cancel = YES;
    }
    
    if (indexPath.row < [titles count]) {
        id title =  [titles objectAtIndex:indexPath.row];
        if ([title isKindOfClass:[NSString class]]) {
            
            if(indexPath.section == 0){
                [cell setButtonTitle:title
                          buttonType:[self.titleColors[indexPath.row] integerValue]
                     backgroundColor:self.dialogBackgroundColor
                              cancel:cancel];
            }else{
                [cell setButtonTitle:title
                          buttonType:YYActionSheetButtonTypeDefault
                     backgroundColor:self.dialogBackgroundColor
                              cancel:cancel];
            }
        }
        else if ([title isKindOfClass:[UIView class]]) {
            [cell setButtonView:title backgroundColor:self.dialogBackgroundColor];
        }
    }
    
    if (indexPath.row == [titles count] -1) {
        [cell roundCornersAtBottom];
    }
    
    if (indexPath.row == 0 && indexPath.section != 0) {
        [cell roundCornersAtTop];
    }
    
    if (indexPath.row != 0 && indexPath.row != [titles count] -1) {
        [cell noCorner];
    }
    
    
    return cell;
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([cell respondsToSelector:@selector(setSeparatorInset:)]) {
        [cell setSeparatorInset:UIEdgeInsetsZero];
    }
    
    if ([cell respondsToSelector:@selector(setLayoutMargins:)]) {
        [cell setLayoutMargins:UIEdgeInsetsZero];
    }
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 8;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 0) {
        return [self.titles count];
    }
    else {
        return [self.cancelTitles count];
    }
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 50;
}

#pragma mark - UIViewControllerAnimated
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
    float height = self.containerHeightConstraint.constant;
    __weak typeof(self) wself = self;
    
    if (vc2 == self) { // presenting
        [con addSubview:v2];
        v2.frame = v1.frame;
        wself.containerViewBottomConstraint.constant = - height;
        [v2 layoutIfNeeded];
        v2.alpha = 0;
        v1.tintAdjustmentMode = UIViewTintAdjustmentModeDimmed;
        [UIView animateWithDuration:.25 animations:^{
            wself.containerViewBottomConstraint.constant = 0;
            v2.alpha = 1;
            [v2 layoutIfNeeded];
        } completion:^(BOOL finished) {
            [transitionContext completeTransition:YES];
        }];
    }
    else { // dismissing
        [UIView animateWithDuration:.25 animations:^{
            wself.containerViewBottomConstraint.constant = - height;
            v1.alpha = 0;
            [v1 layoutIfNeeded];
        } completion:^(BOOL finished) {
            v2.tintAdjustmentMode = UIViewTintAdjustmentModeAutomatic;
            [transitionContext completeTransition:YES];
        }];
    }
}

#pragma mark - public method
- (void)addTitleText:(NSString *)title;
{
    self.titleLabel.hidden = NO;
    self.titleText = title;
}

- (void)addTitleView:(UIView *)view
{
    if ([self.contentView superview]) {
        [self.contentView removeFromSuperview];
    }
    
    self.contentView = view;
}

- (void)addButtonWithView:(UIView *)view block:(YYActionSheetViewControllerButtonBlock)block
{
    [self.titles addObject:view];
    [self.titleColors addObject:@(YYActionSheetButtonTypeDefault)];
    [self.blocks addObject:[block copy]];
}

- (void)addButtonWithTitle:(NSString *)title block:(YYActionSheetViewControllerButtonBlock) block
{
    [self addButtonWithTitle:title buttonType:YYActionSheetButtonTypeDefault block:block];
}

- (void)addButtonWithTitle:(NSString *)title buttonType:(YYActionSheetButtonType)buttonType block:(YYActionSheetViewControllerButtonBlock) block
{
    [self.titles addObject:[title copy]];
    [self.titleColors addObject:@(buttonType)];
    [self.blocks addObject:[block copy]];
}

- (void)addCancelButtonWithTitle:(NSString *)title block:(YYActionSheetViewControllerButtonBlock) block
{
    if (block) {
        [self.cancelBlocks addObject:[block copy]];
    }
    
    [self.cancelTitles addObject:[title copy]];
}

- (void)addCancelButtonWithView:(UIView *)view block:(YYActionSheetViewControllerButtonBlock) block
{
    [self.cancelBlocks addObject:[block copy]];
    [self.cancelTitles addObject:view];
}

- (void)show
{
    [self show:YES];
}

- (void)show:(BOOL)needBg
{
    UIViewController *current = [self delaultRootViewController];
    if (!needBg) {
        self.view.backgroundColor = [UIColor clearColor];
    }
    [current presentViewController:self animated:YES completion:nil];
}

- (void)dismiss
{
    [self dismissViewControllerAnimated:YES completion:NULL];
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
                     if (![nextResponder isKindOfClass:[YPYYMainTabBarController class]]) {
                         result = nextResponder;
                         *stop = YES;
                     }
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

#pragma mark - UIGestureRecognizerDelegate
- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
{
    if ([[touch.view superview] isKindOfClass:[UITableViewCell class]] || [[[touch.view superview] superview] isKindOfClass:[UITableViewCell class]])
    {
        return NO;
    }
    return YES;
}

- (IBAction)tapBlankSpace:(id)sender
{
     [self dismiss];
}

@end
