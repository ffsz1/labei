//
//  NSObject+JXMultipleDelegates.m
//  JXCategories
//
//  Created by Colin on 2018/6/2.
//

#import "NSObject+JXMultipleDelegates.h"
#import "NSObject+JXBase.h"
#import "NSPointerArray+JXBase.h"
#import <objc/runtime.h>

@interface NSObject ()

@property (nonatomic, assign) BOOL _jx_delegatesSelf; ///< 是否代理为自身(self.delegate = self)

@end


/**
 NSObject类属性信息
 */
@interface JXNSObjectClassPropertyInfo : NSObject

@property (nonatomic, readonly) objc_property_t property;
@property (nonatomic, readonly) NSString *name;
@property (nonatomic, readonly) BOOL isWeak;
@property (nonatomic, readonly) BOOL isStrong;

- (instancetype)initWithProperty:(objc_property_t)property;

@end

@implementation JXNSObjectClassPropertyInfo

- (instancetype)initWithProperty:(objc_property_t)property {
    if (!property) return nil;
    
    self = [super init];
    _property = property;
    const char *name = property_getName(property);
    if (name) {
        _name = [NSString stringWithUTF8String:name];
    }
    
    char *isStrongAttributeValue = property_copyAttributeValue(property, "&");
    char *isWeakAttributeValue = property_copyAttributeValue(property, "W");
    _isStrong = isStrongAttributeValue != NULL;
    _isWeak = isWeakAttributeValue != NULL;
    if (isStrongAttributeValue != NULL) {
        free(isStrongAttributeValue);
    }
    if (isWeakAttributeValue != NULL) {
        free(isWeakAttributeValue);
    }
    return self;
}

@end


/**
 代理容器类
 */
@interface JXNSObjectMultipleDelegates : NSObject

@property (nonatomic, strong) NSPointerArray *delegates;;
@property (nonatomic, weak) NSObject *parentObject;

+ (instancetype)weakDelegates;
+ (instancetype)strongDelegates;

- (void)addDelegate:(id)delegate;
- (BOOL)removeDelegate:(id)delegate;
- (void)removeAllDelegates;

@end

@implementation JXNSObjectMultipleDelegates

+ (instancetype)weakDelegates {
    JXNSObjectMultipleDelegates *delegates = [[JXNSObjectMultipleDelegates alloc] init];
    delegates.delegates = [NSPointerArray weakObjectsPointerArray];
    return delegates;
}

+ (instancetype)strongDelegates {
    JXNSObjectMultipleDelegates *delegates = [[JXNSObjectMultipleDelegates alloc] init];
    delegates.delegates = [NSPointerArray strongObjectsPointerArray];
    return delegates;
}

- (void)addDelegate:(id)delegate {
    if (![self containsDelegate:delegate] && delegate != self) {
        [self.delegates addPointer:(__bridge void *)delegate];
    }
}

- (BOOL)removeDelegate:(id)delegate {
    NSUInteger index = [self.delegates jx_indexOfPointer:(__bridge void *)delegate];
    if (index != NSNotFound) {
        [self.delegates removePointerAtIndex:index];
        return YES;
    }
    return NO;
}

- (void)removeAllDelegates {
    for (NSInteger i = self.delegates.count - 1; i >= 0; i--) {
        [self.delegates removePointerAtIndex:i];
    }
}

- (BOOL)containsDelegate:(id)delegate {
    return [self.delegates jx_containsPointer:(__bridge void *)delegate];
}

- (NSMethodSignature *)methodSignatureForSelector:(SEL)aSelector {
    NSMethodSignature *result = [super methodSignatureForSelector:aSelector];
    if (result) return result;
    
    NSPointerArray *delegates = [self.delegates copy];
    for (id delegate in delegates) {
        result = [delegate methodSignatureForSelector:aSelector];
        if (result && [delegate respondsToSelector:aSelector]) return result;
    }
    // https://github.com/facebookarchive/AsyncDisplayKit/pull/1562
    // Unfortunately, in order to get this object to work properly, the use of a method which creates an NSMethodSignature
    // from a C string. -methodSignatureForSelector is called when a compiled definition for the selector cannot be found.
    // This is the place where we have to create our own dud NSMethodSignature. This is necessary because if this method
    // returns nil, a selector not found exception is raised. The string argument to -signatureWithObjCTypes: outlines
    // the return type and arguments to the message. To return a dud NSMethodSignature, pretty much any signature will
    // suffice. Since the -forwardInvocation call will do nothing if the delegate does not respond to the selector,
    // the dud NSMethodSignature simply gets us around the exception.
    return [NSMethodSignature signatureWithObjCTypes:"@^v^c"];
}

- (void)forwardInvocation:(NSInvocation *)anInvocation {
    SEL selector = anInvocation.selector;
    NSPointerArray *delegates = [self.delegates copy];
    for (id delegate in delegates) {
        if ([delegate respondsToSelector:selector]) {
            [anInvocation invokeWithTarget:delegate];
        }
    }
}

- (BOOL)respondsToSelector:(SEL)aSelector {
    if ([super respondsToSelector:aSelector]) return YES;
    
    NSPointerArray *delegates = [self.delegates copy];
    for (id delegate in delegates) {
        if (class_respondsToSelector(self.class, aSelector)) return YES;
        
        // 处理delegate为JXNSObjectMultipleDelegates情况
        BOOL delegateCanRespondToSelector = [delegate isKindOfClass:self.class] ? [delegate respondsToSelector:aSelector] : class_respondsToSelector(((NSObject *)delegate).class, aSelector);
        
        // 处理obj.delegate = self情况
        BOOL isDelegateSelf = ((NSObject *)delegate)._jx_delegatesSelf;
        
        if (delegateCanRespondToSelector && !isDelegateSelf) return YES;
    }
    return NO;
}

- (NSString *)description {
    return [NSString stringWithFormat:@"%@, parentObject is %@, %@", [super description], self.parentObject, self.delegates];
}

- (NSPointerArray *)delegates {
    if (!_delegates) {
        _delegates = [NSPointerArray weakObjectsPointerArray];
    }
    return _delegates;
}

@end


static const int JX_NS_OBJECT_DELEGATES_KEY;
static const int JX_NS_OBJECT_MULTIPLE_DELEGATES_ENABLED_KEY;
static const int JX_NS_OBJECT_DELEGATES_SELF_KEY;
static NSMutableSet<NSString *> *_jx_methodsReplacedClasses;

@interface NSObject ()

@property (nonatomic, strong) NSMutableDictionary<NSString *, JXNSObjectMultipleDelegates *> *_jx_delegates;

@end


@implementation NSObject (JXMultipleDelegates)

- (void)jx_registerDelegateSelector:(SEL)getter {
    if (!self.jx_multipleDelegatesEnabled) return;

    Class targetClass = [self class];
    SEL originDelegateSetter = [self _jx_originSetterWithGetter:getter];
    SEL newDelegateSetter = [self _jx_newSetterWithGetter:getter];
    Method originMethod = class_getInstanceMethod(targetClass, originDelegateSetter);
    if (!originMethod) return;

    // 为这个 selector 创建一个 JXNSObjectMultipleDelegates 容器
    NSString *delegateGetterKey = NSStringFromSelector(getter);
    if (!self._jx_delegates[delegateGetterKey]) {
        objc_property_t property = class_getProperty(self.class, delegateGetterKey.UTF8String);
        JXNSObjectClassPropertyInfo *propertyInfo = [[JXNSObjectClassPropertyInfo alloc] initWithProperty:property];
        // 处理strong修饰delegate
        if (propertyInfo.isStrong) {
            JXNSObjectMultipleDelegates *strongDelegates = [JXNSObjectMultipleDelegates strongDelegates];
            strongDelegates.parentObject = self;
            self._jx_delegates[delegateGetterKey] = strongDelegates;
        } else {
            JXNSObjectMultipleDelegates *weakDelegates = [JXNSObjectMultipleDelegates weakDelegates];
            weakDelegates.parentObject = self;
            self._jx_delegates[delegateGetterKey] = weakDelegates;
        }
    }

    // 避免为某个 class 重复替换同一个方法的实现
    if (!_jx_methodsReplacedClasses) {
        _jx_methodsReplacedClasses = [NSMutableSet set];
    }
    NSString *classAndMethodIdentifier = [NSString stringWithFormat:@"%@-%@", NSStringFromClass(targetClass), delegateGetterKey];
    if (![_jx_methodsReplacedClasses containsObject:classAndMethodIdentifier]) {
        [_jx_methodsReplacedClasses addObject:classAndMethodIdentifier];
        
        IMP originIMP = method_getImplementation(originMethod);
        void (*originSelectorIMP)(id, SEL, id);
        originSelectorIMP = (void (*)(id, SEL, id))originIMP;
        
        BOOL isAddedMethod = class_addMethod(targetClass, newDelegateSetter, imp_implementationWithBlock(^(NSObject *selfObject, id aDelegate) {
            // 处理替换方法导致父类及父类的所有子类都被替换
            if (!selfObject.jx_multipleDelegatesEnabled || selfObject.class != targetClass) {
                originSelectorIMP(selfObject, originDelegateSetter, aDelegate);
                return;
            }
            
            JXNSObjectMultipleDelegates *delegates = selfObject._jx_delegates[delegateGetterKey];
            if (!aDelegate) {
                // 对应 setDelegate:nil，表示清理所有的 delegate
                [delegates removeAllDelegates];
                selfObject._jx_delegatesSelf = NO;
                // 只要 multipleDelegatesEnabled 开启，就会保证 delegate 一直是 delegates，所以不去调用系统默认的 set nil
                return;
            }
            
            if (aDelegate != delegates) {
                // 过滤掉容器自身，避免把 delegates 传进去 delegates 里，导致死循环
                [delegates addDelegate:aDelegate];
            }
            
            // 将类似 textView.delegate = textView 的情况标志起来，避免产生循环调用
            selfObject._jx_delegatesSelf = [delegates.delegates jx_containsPointer:(__bridge void * _Nullable)(selfObject)];
            
            originSelectorIMP(selfObject, originDelegateSetter, nil);// 先置为 nil 再设置 delegates，从而避免这个问题 https://github.com/QMUI/QMUI_iOS/issues/305
            originSelectorIMP(selfObject, originDelegateSetter, delegates);// 不管外面将什么 object 传给 setDelegate:，最终实际上传进去的都是 QMUIMultipleDelegates 容器
        }), method_getTypeEncoding(originMethod));
        
        if (isAddedMethod) {
            Method newMethod = class_getInstanceMethod(targetClass, newDelegateSetter);
            method_exchangeImplementations(originMethod, newMethod);
        }
    }
    
    // 如果原来已经有 delegate，则将其加到新建的容器里, 处理已有值的 object 打开 qmui_multipleDelegatesEnabled 会导致原来的值丢失
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Warc-performSelector-leaks"
    id originDelegate = [self performSelector:getter];
    if (originDelegate && originDelegate != self._jx_delegates[delegateGetterKey]) {
        [self performSelector:originDelegateSetter withObject:originDelegate];
    }
#pragma clang diagnostic pop
}

- (void)jx_removeDelegate:(id)delegate {
    if (!self.jx_multipleDelegatesEnabled) return;
    
    NSMutableArray<NSString *> *delegateGetters = [[NSMutableArray alloc] init];
    [self._jx_delegates enumerateKeysAndObjectsUsingBlock:^(NSString * _Nonnull key, JXNSObjectMultipleDelegates * _Nonnull obj, BOOL * _Nonnull stop) {
        BOOL removeSucceed = [obj removeDelegate:delegate];
        if (removeSucceed) {
            [delegateGetters addObject:key];
        }
    }];
    if (delegateGetters.count > 0) {
        for (NSString *getterString in delegateGetters) {
            [self _jx_refreshDelegateWithGetter:NSSelectorFromString(getterString)];
        }
    }
}

- (void)_jx_refreshDelegateWithGetter:(SEL)getter {
    SEL originSetterSEL = [self _jx_newSetterWithGetter:getter];
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Warc-performSelector-leaks"
    id originDelegate = [self performSelector:getter];
    [self performSelector:originSetterSEL withObject:nil];// 先置为 nil 再设置 delegates，从而避免这个问题 https://github.com/QMUI/QMUI_iOS/issues/305
    [self performSelector:originSetterSEL withObject:originDelegate];
#pragma clang diagnostic pop
}

// 根据 delegate property 的 getter，得到它对应的 setter
- (SEL)_jx_originSetterWithGetter:(SEL)getter {
    NSString *getterString = NSStringFromSelector(getter);
    NSMutableString *setterString = [[NSMutableString alloc] initWithString:@"set"];
    [setterString appendString:[getterString substringToIndex:1].uppercaseString];
    [setterString appendString:[getterString substringFromIndex:1]];
    [setterString appendString:@":"];
    SEL setter = NSSelectorFromString(setterString);
    return setter;
}

// 根据 delegate property 的 getter，得到 JXNSObjectMultipleDelegates 为它的 setter 创建的新 setter 方法，最终交换原方法，因此利用这个方法返回的 SEL，可以调用到原来的 delegate property setter 的实现
- (SEL)_jx_newSetterWithGetter:(SEL)getter {
    return NSSelectorFromString([NSString stringWithFormat:@"_jx_%@", NSStringFromSelector([self _jx_originSetterWithGetter:getter])]);
}

- (void)_jx_setDelegates:(NSMutableDictionary<NSString *, JXNSObjectMultipleDelegates *> *)jx_delegates {
    [self jx_setAssociatedValue:jx_delegates withKey:&JX_NS_OBJECT_DELEGATES_KEY];
}

- (NSMutableDictionary<NSString *, JXNSObjectMultipleDelegates *> *)_jx_delegates {
    NSMutableDictionary *delegates = [self jx_getAssociatedValueForKey:&JX_NS_OBJECT_DELEGATES_KEY];
    if (!delegates) {
        delegates = @{}.mutableCopy;
        [self _jx_setDelegates:delegates];
    }
    return delegates;
}

- (void)setJx_multipleDelegatesEnabled:(BOOL)jx_multipleDelegatesEnabled {
    [self jx_setAssociatedValue:@(jx_multipleDelegatesEnabled) withKey:&JX_NS_OBJECT_MULTIPLE_DELEGATES_ENABLED_KEY];
    if (jx_multipleDelegatesEnabled) {
        [self jx_registerDelegateSelector:@selector(delegate)];
        if ([self isKindOfClass:NSClassFromString(@"UITableView")] || [self isKindOfClass:NSClassFromString(@"UICollectionView")]) {
            [self jx_registerDelegateSelector:@selector(dataSource)];
        }
    }
}

- (BOOL)jx_multipleDelegatesEnabled {
    return [[self jx_getAssociatedValueForKey:&JX_NS_OBJECT_MULTIPLE_DELEGATES_ENABLED_KEY] boolValue];
}

- (void)set_jx_delegatesSelf:(BOOL)_jx_delegatesSelf {
    [self jx_setAssociatedValue:@(_jx_delegatesSelf) withKey:&JX_NS_OBJECT_DELEGATES_SELF_KEY];
}

- (BOOL)_jx_delegatesSelf {
    return [[self jx_getAssociatedValueForKey:&JX_NS_OBJECT_DELEGATES_SELF_KEY] boolValue];
}

@end
