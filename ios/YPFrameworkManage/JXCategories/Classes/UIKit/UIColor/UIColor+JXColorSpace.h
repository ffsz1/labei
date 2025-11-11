//
//  UIColor+JXColorSpace.h
//  JXCategories
//
//  Created by colin on 2019/2/3.
//  色彩空间

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIColor (JXColorSpace)

#pragma mark - Info
@property (nonatomic, readonly) CGColorSpaceModel jx_colorSpaceModel; ///< 获取颜色的颜色空间
@property (nonatomic, readonly) NSString *jx_colorSpaceString;        ///< 获取颜色的颜色空间(字符串)

#pragma mark - Convert
/**
 RGB颜色转换为HSL颜色
 */
UIKIT_EXTERN void JXRGB2HSL(CGFloat red, CGFloat green, CGFloat blue,
                            CGFloat *hue, CGFloat *saturation, CGFloat *lightness);

/**
 HSL颜色转换为RGB颜色
 */
UIKIT_EXTERN void JXHSL2RGB(CGFloat hue, CGFloat saturation, CGFloat lightness,
                            CGFloat *red, CGFloat *green, CGFloat *blue);

/**
 RGB颜色转换为HSB颜色
 */
UIKIT_EXTERN void JXRGB2HSB(CGFloat red, CGFloat green, CGFloat blue,
                            CGFloat *hue, CGFloat *saturation, CGFloat *value);

/**
 HSB颜色转换为RGB颜色
 */
UIKIT_EXTERN void JXHSB2RGB(CGFloat hue, CGFloat saturation, CGFloat value,
                            CGFloat *red, CGFloat *green, CGFloat *blue);

/**
 RGB颜色转换为CMYK颜色
 */
UIKIT_EXTERN void JXRGB2CMYK(CGFloat red, CGFloat green, CGFloat blue,
                             CGFloat *cyan, CGFloat *magenta, CGFloat *yellow, CGFloat *key);

/**
 CMYK颜色转换为RGB颜色
 */
UIKIT_EXTERN void JXCMYK2RGB(CGFloat cyan, CGFloat magenta, CGFloat yellow, CGFloat key,
                             CGFloat *red, CGFloat *green, CGFloat *blue);

/**
 RGB颜色转换为CMY颜色
 */
UIKIT_EXTERN void JXRGB2CMY(CGFloat red, CGFloat green, CGFloat blue,
                            CGFloat *cyan, CGFloat *magenta, CGFloat *yellow);

/**
 CMY颜色转换为RGB颜色
 */
UIKIT_EXTERN void JXCMY2RGB(CGFloat cyan, CGFloat magenta, CGFloat yellow,
                            CGFloat *red, CGFloat *green, CGFloat *blue);

/**
 CMY颜色转换为CMYK颜色
 */
UIKIT_EXTERN void JXCMY2CMYK(CGFloat cyan, CGFloat magenta, CGFloat yellow,
                             CGFloat *cCyan, CGFloat *mMagenta, CGFloat *yYellow, CGFloat *kKey);

/**
 CMYK颜色转换为CMY颜色
 */
UIKIT_EXTERN void JXCMYK2CMY(CGFloat cyan, CGFloat magenta, CGFloat yellow, CGFloat key,
                             CGFloat *cCyan, CGFloat *mMagenta, CGFloat *yYellow);

/**
 HSB颜色转换为HSL颜色
 */
UIKIT_EXTERN void JXHSB2HSL(CGFloat hue, CGFloat saturation, CGFloat brightness,
                            CGFloat *hHue, CGFloat *sSaturation, CGFloat *lLightness);

/**
 HSL颜色转换为HSB颜色
 */
UIKIT_EXTERN void JXHSL2HSB(CGFloat hue, CGFloat saturation, CGFloat lightness,
                            CGFloat *hHue, CGFloat *sSaturation, CGFloat *bBrightness);

#pragma mark - CMYK
@property (nonatomic, readonly) CGFloat jx_cyanOfCMYK;    ///< 获取CMYK/CMY的Cyan值[0,1]
@property (nonatomic, readonly) CGFloat jx_magentaOfCMYK; ///< 获取CMYK/CMY的Magenta值[0,1]
@property (nonatomic, readonly) CGFloat jx_yellowOfCMYK;  ///< 获取CMYK/CMY的Yellow值[0,1]
@property (nonatomic, readonly) CGFloat jx_keyOfCMYK;     ///< 获取CMYK的Key(Black)值[0,1]

/**
 获取根据CMY颜色空间调配的各颜色部分
 
 @param cyan    CMYK的Cyan部分[0,1]
 @param magenta CMYK的Magenta部分[0,1]
 @param yellow  CMYK的Yellow部分[0,1]
 @param alpha   Alpha部分[0,1]
 @return 颜色成功转化返回YES, 否则返回NO
 */
- (BOOL)jx_getCyan:(CGFloat *)cyan
           magenta:(CGFloat *)magenta
            yellow:(CGFloat *)yellow
             alpha:(CGFloat *)alpha;

/**
 获取根据CMYK颜色空间调配的各颜色部分
 
 @param cyan    CMYK的Cyan部分[0,1]
 @param magenta CMYK的Magenta部分[0,1]
 @param yellow  CMYK的Yellow部分[0,1]
 @param key     CMYK的Key(Black)部分[0,1]
 @param alpha   Alpha部分[0,1]
 @return 颜色成功转化返回YES, 否则返回NO
 */
- (BOOL)jx_getCyan:(CGFloat *)cyan
           magenta:(CGFloat *)magenta
            yellow:(CGFloat *)yellow
               key:(CGFloat *)key
             alpha:(CGFloat *)alpha;

/**
 根据Cyan, Magenta, Yellow值, 获取颜色(基于设备RGB颜色空间调配)
 
 @param cyan    CMY的Cyan值[0,1]
 @param magenta CMY的Magenta值[0,1]
 @param yellow  CMY的Yellow值[0,1]
 @return 对应的颜色
 */
+ (UIColor *)jx_colorWithCyan:(CGFloat)cyan
                      magenta:(CGFloat)magenta
                       yellow:(CGFloat)yellow;

/**
 根据Cyan, Magenta, Yellow和Alpha值, 获取颜色(基于设备RGB颜色空间调配)
 
 @param cyan    CMY的Cyan值[0,1]
 @param magenta CMY的Magenta值[0,1]
 @param yellow  CMY的Yellow值[0,1]
 @param alpha   Alpha值[0,1]
 @return 对应的颜色
 */
+ (UIColor *)jx_colorWithCyan:(CGFloat)cyan
                      magenta:(CGFloat)magenta
                       yellow:(CGFloat)yellow
                        alpha:(CGFloat)alpha;

/**
 根据Cyan, Magenta, Yellow, Key(Black)值, 获取颜色(基于设备RGB颜色空间调配)
 
 @param cyan    CMYK的Cyan值[0,1]
 @param magenta CMYK的Magenta值[0,1]
 @param yellow  CMYK的Yellow值[0,1]
 @param key     CMYK的Key(Balck)值[0,1]
 @return 对应的颜色
 */
+ (UIColor *)jx_colorWithCyan:(CGFloat)cyan
                      magenta:(CGFloat)magenta
                       yellow:(CGFloat)yellow
                          key:(CGFloat)key;

/**
 根据Cyan, Magenta, Yellow, Key(Black)和Alpha值, 获取颜色(基于设备RGB颜色空间调配)
 
 @param cyan CMYK的Cyan值[0,1]
 @param magenta CMYK的Magenta值[0,1]
 @param yellow CMYK的Yellow值[0,1]
 @param key CMYK的Key(Balck)值[0,1]
 @param alpha Alpha值[0,1]
 @return 对应的颜色
 */
+ (UIColor *)jx_colorWithCyan:(CGFloat)cyan
                      magenta:(CGFloat)magenta
                       yellow:(CGFloat)yellow
                          key:(CGFloat)key
                        alpha:(CGFloat)alpha;

/**
 根据CMYK/CMY颜色Cyan值的变化, 获取修改后的颜色
 
 @param CyanDelta Cyan(CMYK/CMY)的变化值[-1,1](0, 不变)
 @return 修改后的颜色
 */
- (nullable UIColor *)jx_colorByChangeCyanOfCMYK:(CGFloat)CyanDelta;

/**
 根据CMYK/CMY颜色Magenta值的变化, 获取修改后的颜色
 
 @param magentaDelta Magenta(CMYK/CMY)的变化值[-1,1](0, 不变)
 @return 修改后的颜色
 */
- (nullable UIColor *)jx_colorByChangeMagentaOfCMYK:(CGFloat)magentaDelta;

/**
 根据CMYK/CMY颜色Yellow值的变化, 获取修改后的颜色
 
 @param yellowDelta Yellow(CMYK/CMY)的变化值[-1,1](0, 不变)
 @return 修改后的颜色
 */
- (nullable UIColor *)jx_colorByChangeYellowOfCMYK:(CGFloat)yellowDelta;

/**
 根据CMYK颜色Key(Black)值的变化, 获取修改后的颜色
 
 @param keyDelta keyDelta Key<Black>(CMYK)的变化值[-1,1](0, 不变)
 @return 修改后的颜色
 */
- (nullable UIColor *)jx_colorByChangeKeyOfCMYK:(CGFloat)keyDelta;

/**
 根据CMYK/CMY颜色Cyan, Magenta, Yellow和Alpha值的变化, 获取修改后的颜色
 
 @param cyanDelta    Cyan(CMYK/CMY)的变化值[-1,1](0, 不变)
 @param magentaDelta Magenta(CMYK/CMY)的变化值[-1,1](0, 不变)
 @param yellowDelta  Yellow(CMYK/CMY)的变化值[-1,1](0, 不变)
 @param alphaDelta   Alpha的变化值[-1,1](0, 不变)
 @return 修改后的颜色
 */
- (nullable UIColor *)jx_colorByChangeCyan:(CGFloat)cyanDelta
                                   magenta:(CGFloat)magentaDelta
                                    yellow:(CGFloat)yellowDelta
                                     alpha:(CGFloat)alphaDelta;

/**
 根据CMYK颜色Cyan, Magenta, Yellow, Key(Black)和Alpha值的变化, 获取修改后的颜色
 
 @param cyanDelta    Cyan(CMYK)的变化值[-1,1](0, 不变)
 @param magentaDelta Magenta(CMYK)的变化值[-1,1](0, 不变)
 @param yellowDelta  Yellow(CMYK)的变化值[-1,1](0, 不变)
 @param keyDelta     Key<Black>(CMYK)的变化值[-1,1](0, 不变)
 @param alphaDelta   Alpha的变化值[-1,1](0, 不变)
 @return 修改后的颜色
 */
- (nullable UIColor *)jx_colorByChangeCyan:(CGFloat)cyanDelta
                                   magenta:(CGFloat)magentaDelta
                                    yellow:(CGFloat)yellowDelta
                                       key:(CGFloat)keyDelta
                                     alpha:(CGFloat)alphaDelta;

#pragma mark - HSB
@property (nonatomic, readonly) CGFloat jx_hueOfHSB;        ///< 获取HSB(HSV)的色相(H)值[0,1]
@property (nonatomic, readonly) CGFloat jx_saturationOfHSB; ///< 获取HSB(HSV)的饱和度(S)值[0,1]
@property (nonatomic, readonly) CGFloat jx_brightnessOfHSB; ///< 获取HSB(HSV)的明度(B)值[0,1]

/**
 根据色相, 饱和度, 明度, 获取颜色(基于设备RGB颜色空间调配)
 
 @param hue         HSB的色相(H)值[0,1]
 @param saturation  HSB的饱和度(S)值[0,1]
 @param brightness  HSB的明度(B)值[0,1]
 @return 对应的颜色
 */
+ (UIColor *)jx_colorWithHue:(CGFloat)hue
                  saturation:(CGFloat)saturation
                  brightness:(CGFloat)brightness;

/**
 根据颜色HSB中色相的变化, 获取修改后的颜色
 
 @param hueDelta 色相(HSB<HSV>)的变化值[-1,1](0, 不变)
 @return 修改后的颜色
 */
- (UIColor *)jx_colorByChangeHueOfHSB:(CGFloat)hueDelta;

/**
 根据颜色HSB中饱和度的变化, 获取修改后的颜色
 
 @param saturationDelta 饱和度(HSB<HSV>)的变化值[-1,1](0, 不变)
 @return 修改后的颜色
 */
- (UIColor *)jx_colorByChangeSaturationOfHSB:(CGFloat)saturationDelta;

/**
 根据颜色HSB中明度的变化, 获取修改后的颜色
 
 @param brightnessDelta 明度(HSB<HSV>)的变化值[-1,1](0, 不变)
 @return 修改后的颜色
 */
- (UIColor *)jx_colorByChangeBrightnessOfHSB:(CGFloat)brightnessDelta;

/**
 根据颜色HSB中色相, 饱和度, 明度和Alpha值的变化, 获取修改后的颜色
 
 @param hueDelta        色相(HSB<HSV>)的变化值[-1,1](0, 不变)
 @param saturationDelta 饱和度(HSB<HSV>)的变化值[-1,1](0, 不变)
 @param brightnessDelta 明度(HSB<HSV>)的变化值[-1,1](0, 不变)
 @param alphaDelta      Alpha的变化值[-1,1](0, 不变)
 @return 修改后的颜色
 */
- (UIColor *)jx_colorByChangeHue:(CGFloat)hueDelta
                      saturation:(CGFloat)saturationDelta
                      brightness:(CGFloat)brightnessDelta
                           alpha:(CGFloat)alphaDelta;

#pragma mark - HSL
@property (nonatomic, readonly) CGFloat jx_hueOfHSL;        ///< 获取HSL的色相(H)值[0,1]
@property (nonatomic, readonly) CGFloat jx_saturationOfHSL; ///< 获取HSL的饱和度(S)值[0,1]
@property (nonatomic, readonly) CGFloat jx_lightnessOfHSL;  ///< 获取HSL的亮度(L)值[0,1]

/**
 获取根据HSL颜色空间调配的各颜色部分
 
 @param hue        HSL的色相(H)部分[0,1]
 @param saturation HSL的饱和度(S)部分[0,1]
 @param lightness  HSL的亮度(L)部分[0,1]
 @param alpha      Alpha部分[0,1]
 @return 颜色成功转化返回YES, 否则返回NO
 */
- (BOOL)jx_getHue:(CGFloat *)hue
       saturation:(CGFloat *)saturation
        lightness:(CGFloat *)lightness
            alpha:(CGFloat *)alpha;

/**
 根据色相, 饱和度, 亮度值, 获取颜色(基于设备RGB颜色空间调配)
 
 @param hue        HSL的色相(H)值[0,1]
 @param saturation HSL的饱和度(S)值[0,1]
 @param lightness  HSL的亮度(L)值[0,1]
 @return 对应的颜色
 */
+ (UIColor *)jx_colorWithHue:(CGFloat)hue
                  saturation:(CGFloat)saturation
                   lightness:(CGFloat)lightness;

/**
 根据色相, 饱和度, 亮度和Alpha值, 获取颜色(基于设备RGB颜色空间调配)
 
 @param hue        HSL的色相(H)值[0,1]
 @param saturation HSL的饱和度(S)值[0,1]
 @param lightness  HSL的亮度(L)值[0,1]
 @param alpha      Alpha值[0,1]
 @return 对应的颜色
 */
+ (UIColor *)jx_colorWithHue:(CGFloat)hue
                  saturation:(CGFloat)saturation
                   lightness:(CGFloat)lightness
                       alpha:(CGFloat)alpha;

/**
 根据颜色HSL中色相的变化, 获取修改后的颜色
 
 @param hueDelta 色相(HSL)的变化值[-1,1](0, 不变)
 @return 修改后的颜色
 */
- (nullable UIColor *)jx_colorByChangeHueOfHSL:(CGFloat)hueDelta;

/**
 根据颜色HSL中饱和度的变化, 获取修改后的颜色
 
 @param saturationDelta 饱和度(HSL)的变化值[-1,1](0, 不变)
 @return 修改后的颜色
 */
- (nullable UIColor *)jx_colorByChangeSaturationOfHSL:(CGFloat)saturationDelta;

/**
 根据颜色HSL中亮度的变化, 获取修改后的颜色
 
 @param lightnessDelta 亮度(HSL)的变化值[-1,1](0, 不变)
 @return 修改后的颜色
 */
- (nullable UIColor *)jx_colorByChangeLightnessOfHSL:(CGFloat)lightnessDelta;

/**
 根据颜色HSL中色相, 饱和度, 亮度和Alpha值的变化, 获取修改后的颜色
 
 @param hueDelta        色相(HSL)的变化值[-1,1](0, 不变)
 @param saturationDelta 饱和度(HSL)的变化值[-1,1](0, 不变)
 @param lightnessDelta  亮度(HSL)的变化值[-1,1](0, 不变)
 @param alphaDelta      Alpha的变化值[-1,1](0, 不变)
 @return 修改后的颜色
 */
- (nullable UIColor *)jx_colorByChangeHue:(CGFloat)hueDelta
                               saturation:(CGFloat)saturationDelta
                                lightness:(CGFloat)lightnessDelta
                                    alpha:(CGFloat)alphaDelta;

@end

NS_ASSUME_NONNULL_END
