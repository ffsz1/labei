//
//  FYTool.h
//  XBD
//
//  Created by feiyin on 2019/11/1.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface FYTool : NSObject

@property (nonatomic,strong) NSArray *banaArray;
+ (FYTool *) shareInstance;
+ (UIImageView *)findNavBarBottomLine:(UIView *)view;

//颜色渐变
+(CAGradientLayer *)gradientLayerFromColorStr:(NSString*)fromColorStr toColorStr:(NSString*)toColorStr height:(CGFloat)height;
+(CAGradientLayer *)gradientLayerFromColorStrForLine:(NSString*)fromColorStr toColorStr:(NSString*)toColorStr;
+(CAGradientLayer *)gradientLayerFromColorStrForPoint:(NSString*)fromColorStr toColorStr:(NSString*)toColorStr height:(CGFloat)height width:(CGFloat)width endpoint:(CGPoint)endpoint;
//view边缘阴影效果
+(void)addShadowToTheView:(UIView *)theView withColor:(UIColor *)theColor;
+(void)addShadowToTheView:(UIView *)theView withColor:(UIColor *)theColor shadowOffset:(CGSize)shadowOffset shadowRadius:(CGFloat)shadowRadius;
+(NSMutableAttributedString*)setAttributedString:(NSString*)attributeStr origStr:(NSString*)origstr color:(NSString*)color;
//设置部分字符串颜色2
+(NSMutableAttributedString*)setAttributedStringAll:(NSString*)attributeStr origStr:(NSString*)origstr color:(NSString*)color allColor:(NSString*)allColor;
+ (UIImage*_Nonnull)imageWithBorderW:(CGFloat)borderW color:(nonnull UIColor*)color image:(nonnull UIImage*)image;
@end

NS_ASSUME_NONNULL_END
