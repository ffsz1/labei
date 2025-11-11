//
//  CUIWaterfallViewCell.m
//  MyWaterflow
//
//  Created by daixiang on 13-12-30.
//  Copyright (c) 2013年 YY.inc. All rights reserved.
//

#import "CUIWaterfallViewCell.h"
#import "CUIWaterfallView.h"

@interface CUIWaterfallViewCell () <UIGestureRecognizerDelegate>

//@property (nonatomic) UITapGestureRecognizer *tapGesture;
@property (nonatomic, weak) CUIWaterfallView *waterfallView;

@end

@implementation CUIWaterfallViewCell

- (id)initWithReuseIdentifier:(NSString *)reuseIdentifier
{
    if (self = [super initWithFrame:CGRectMake(0, 0, 160, 160)])
    {
        //NSLog(@"initWithReuseIdentifier %@ %p", reuseIdentifier, self);
        _reuseIdentifier = reuseIdentifier;
        self.clipsToBounds = YES;
        
        //_tapGesture = [[UITapGestureRecognizer alloc] init];
        //tap.cancelsTouchesInView = NO;
        //_tapGesture.delegate = self;
    }
    
    return self;
}

- (void)dealloc
{
    //_tapGesture.delegate = nil;
    //[self removeGestureRecognizer:_tapGesture];
    //NSLog(@"dealloc %p", self);
}

//- (void)tapped
//{
//    NSLog(@"tapped");
//}

//- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
//{
//    if (touch.view != self) {
//        return NO;
//    }
//    else
//    {
//        return YES;
//    }
//}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    //NSLog(@"touches began");
    if (waterfallShouldUseCollectionView())
    {
        [super touchesBegan:touches withEvent:event];
    }
}

- (void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event
{
    //NSLog(@"touches cancel");
    if (waterfallShouldUseCollectionView())
    {
        [super touchesCancelled:touches withEvent:event];
    }
}

- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{
    //NSLog(@"touches end");
    if (waterfallShouldUseCollectionView())
    {
        [super touchesEnded:touches withEvent:event];
    }
    else
    {
        if ([self.waterfallView.delegate respondsToSelector:@selector(waterfallView:didSelectItemAtIndex:)])
        {
            [self.waterfallView.delegate waterfallView:self.waterfallView didSelectItemAtIndex:self.index];
        }
    }
}

@end
