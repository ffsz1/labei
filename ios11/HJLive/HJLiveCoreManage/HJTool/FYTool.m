//
//  FYTool.m
//  XBD
//
//  Created by feiyin on 2019/11/1.
//

#import "FYTool.h"

@implementation FYTool

+ (FYTool *) shareInstance
{
    static FYTool *instance = nil;
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        instance = [[super allocWithZone:NULL] init];
    });
    return instance;
}




+ (UIImageView *)findNavBarBottomLine:(UIView *)view{
    if ([view isKindOfClass:[UIImageView class]]&&view.bounds.size.height<1) {
        return (UIImageView *)view;
    }
    for (UIView *subView in view.subviews) {
        UIImageView *imageView=[self findNavBarBottomLine:subView];
        if (imageView) {
            return imageView;
        }
    }
    return nil;
}

+(CAGradientLayer *)gradientLayerFromColorStr:(NSString*)fromColorStr toColorStr:(NSString*)toColorStr height:(CGFloat)height {
    
       CAGradientLayer* gradientLayer = [CAGradientLayer layer];
//                gradientLayer.colors = @[(__bridge id)[UIColor colorWithHexString:@"#FF35B8"].CGColor, (__bridge id)[UIColor colorWithHexString:@"#FFA2C1"].CGColor];
     gradientLayer.colors = @[(__bridge id)[UIColor colorWithHexString:fromColorStr].CGColor, (__bridge id)[UIColor colorWithHexString:toColorStr].CGColor];
        //        gradientLayer.locations = @[@0.3, @0.5, @1.0];
        gradientLayer.locations = @[@0,@1];
//                gradientLayer.startPoint = CGPointMake(1, 0);
//                gradientLayer.endPoint = CGPointMake(1.0, 1.0);
    gradientLayer.startPoint = CGPointMake(0, 0);
    gradientLayer.endPoint = CGPointMake(0, 1.0);
                gradientLayer.frame = CGRectMake(0, 0, kScreenWidth, height);
               
       
    
    return gradientLayer;
}
//trake 滑动轨迹
+(CAGradientLayer *)gradientLayerFromColorStrForLine:(NSString*)fromColorStr toColorStr:(NSString*)toColorStr {
    
       CAGradientLayer* gradientLayer = [CAGradientLayer layer];
     gradientLayer.colors = @[(__bridge id)[UIColor colorWithHexString:fromColorStr].CGColor, (__bridge id)[UIColor colorWithHexString:toColorStr].CGColor];
        gradientLayer.locations = @[@0.01,@1];

    gradientLayer.startPoint = CGPointMake(0, 0);
    gradientLayer.endPoint = CGPointMake(1, 0);
                gradientLayer.frame = CGRectMake(0, 0, 100, 4);
               
       
    
    return gradientLayer;
}

+(CAGradientLayer *)gradientLayerFromColorStrForPoint:(NSString*)fromColorStr toColorStr:(NSString*)toColorStr height:(CGFloat)height width:(CGFloat)width endpoint:(CGPoint)endpoint {
    
       CAGradientLayer* gradientLayer = [CAGradientLayer layer];
//                gradientLayer.colors = @[(__bridge id)[UIColor colorWithHexString:@"#FF35B8"].CGColor, (__bridge id)[UIColor colorWithHexString:@"#FFA2C1"].CGColor];
     gradientLayer.colors = @[(__bridge id)[UIColor colorWithHexString:fromColorStr].CGColor, (__bridge id)[UIColor colorWithHexString:toColorStr].CGColor];
        //        gradientLayer.locations = @[@0.3, @0.5, @1.0];
        gradientLayer.locations = @[@0,@1];
//                gradientLayer.startPoint = CGPointMake(1, 0);
//                gradientLayer.endPoint = CGPointMake(1.0, 1.0);
    gradientLayer.startPoint = CGPointMake(0, 0);
    gradientLayer.endPoint = endpoint;
                gradientLayer.frame = CGRectMake(0, 0, width, height);
               
       
    
    return gradientLayer;
}

+(void)addShadowToTheView:(UIView *)theView withColor:(UIColor *)theColor {
    // 阴影颜色
    theView.layer.shadowColor = theColor.CGColor;
    // 阴影偏移，默认(0, -3)
    theView.layer.shadowOffset = CGSizeMake(0,4);
    // 阴影透明度，默认0
    theView.layer.shadowOpacity = 0.6;
    // 阴影半径，默认3
    theView.layer.shadowRadius = 10;
    
//    _whiteCardBgView.layer.shadowColor = [UIColor colorWithHexString:@"#FF40B9"].CGColor;
//
//      _whiteCardBgView.layer.shadowOffset = CGSizeMake(0,10);//0,10
//      _whiteCardBgView.layer.shadowRadius = 10;//15
//      _whiteCardBgView.layer.shadowOpacity = 0.1;//1
}

+(void)addShadowToTheView:(UIView *)theView withColor:(UIColor *)theColor shadowOffset:(CGSize)shadowOffset shadowRadius:(CGFloat)shadowRadius{
    // 阴影颜色
    theView.layer.shadowColor = theColor.CGColor;
    // 阴影偏移，默认(0, -3)
    theView.layer.shadowOffset = shadowOffset;
    // 阴影透明度，默认0
    theView.layer.shadowOpacity = 0.7;
    // 阴影半径，默认3
//    theView.layer.shadowRadius = shadowRadius;
    
}



//设置部分字符串颜色
+(NSMutableAttributedString*)setAttributedString:(NSString*)attributeStr origStr:(NSString*)origstr color:(NSString*)color{
    
    NSMutableAttributedString *attrDescribeStr = [[NSMutableAttributedString alloc] initWithString:origstr];

    [attrDescribeStr addAttribute:NSForegroundColorAttributeName

    value:[UIColor colorWithHexString:color]

    range:[origstr rangeOfString:attributeStr]];
    
    return attrDescribeStr;
}

//设置部分字符串颜色2
+(NSMutableAttributedString*)setAttributedStringAll:(NSString*)attributeStr origStr:(NSString*)origstr color:(NSString*)color allColor:(NSString*)allColor{
    
    NSMutableAttributedString *attrDescribeStr = [[NSMutableAttributedString alloc] initWithString:origstr];

    [attrDescribeStr addAttribute:NSForegroundColorAttributeName

       value:[UIColor colorWithHexString:allColor]

       range:[origstr rangeOfString:origstr]];
    
    [attrDescribeStr addAttribute:NSForegroundColorAttributeName

    value:[UIColor colorWithHexString:color]

    range:[origstr rangeOfString:attributeStr]];
    
    return attrDescribeStr;
}
+ (UIImage*_Nonnull)imageWithBorderW:(CGFloat)borderW color:(nonnull UIColor*)color image:(nonnull UIImage*)image{

CGSize size =CGSizeMake(image.size.width+2* borderW, image.size.height+2* borderW);

UIGraphicsBeginImageContext(size);

UIBezierPath*path = [UIBezierPath bezierPathWithOvalInRect:CGRectMake(0,0, size.width, size.height)];

[color set];

[path fill];

UIBezierPath*clipPath = [UIBezierPath bezierPathWithOvalInRect:CGRectMake(borderW, borderW, image.size.width, image.size.height)];

[clipPath addClip];

[image drawAtPoint:CGPointMake(borderW, borderW)];

UIImage*newImage =UIGraphicsGetImageFromCurrentImageContext();

UIGraphicsEndPDFContext();

return newImage;

}

@end
