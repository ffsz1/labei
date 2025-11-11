//
//  NSObject+JXDataBind.m
//  JXCategories
//
//  Created by Colin on 2019/2/12.
//

#import "NSObject+JXDataBind.h"
#import "NSNumber+JXBase.h"
#import "NSObject+JXBase.h"

/**
 若引用对象容器, 通过OBJC_ASSOCIATION_ASSIGN关联的对象, 如果对象稍后被释放, 通过objc_getAssociatedObject获取时, 会出现野指针问题, 此时将对象包装进容器类内, 并改为通过强引用方式绑定, 可安全获取原始对象
 */
@interface JXNSObjectWeakObjectContainer : NSObject

@property (nonatomic, weak) id object;

- (instancetype)initWithObject:(id)object;

@end

@implementation JXNSObjectWeakObjectContainer

- (instancetype)initWithObject:(id)object {
    if (self = [super init]) {
        _object = object;
    }
    return self;
}

@end


static const int JX_NS_OBJECT_ALL_BINDING_OBJECTS_KEY;

@implementation NSObject (JXDataBind)

- (NSMutableDictionary<id, id> *)_jx_allBindingObjects {
    NSMutableDictionary *buffer = [self jx_getAssociatedValueForKey:&JX_NS_OBJECT_ALL_BINDING_OBJECTS_KEY];
    if (!buffer) {
        buffer = @{}.mutableCopy;
        [self jx_setAssociatedValue:buffer withKey:&JX_NS_OBJECT_ALL_BINDING_OBJECTS_KEY];
    }
    return buffer;
}

- (NSArray<NSString *> *)jx_allBindingKeys {
    return [[self _jx_allBindingObjects] allKeys];
}

- (BOOL)jx_containsBindingKey:(NSString *)key {
    return [[self jx_allBindingKeys] containsObject:key];
}

- (void)jx_removeAllBindingObjects {
    [[self _jx_allBindingObjects] removeAllObjects];
}

- (void)jx_revomeBindingObjectForKey:(NSString *)key {
    [self jx_setBindingObject:nil forKey:key];
}

- (void)jx_setBindingObject:(id)object forKey:(NSString *)key {
    if (!key.length) return;
    
    if (object) {
        [[self _jx_allBindingObjects] setObject:object forKey:key];
    } else {
        [[self _jx_allBindingObjects] removeObjectForKey:key];
    }
}

- (void)jx_setBindingWeakObject:(id)object forKey:(NSString *)key {
    if (!key.length) return;
    
    if (object) {
        JXNSObjectWeakObjectContainer *container = [[JXNSObjectWeakObjectContainer alloc] initWithObject:object];
        [self jx_setBindingObject:container forKey:key];
    } else {
        [[self _jx_allBindingObjects] removeObjectForKey:key];
    }
}

- (id)jx_bindingObjectForKey:(NSString *)key {
    if (!key.length) return nil;
    
    id storedObj = [[self _jx_allBindingObjects] objectForKey:key];
    if ([storedObj isKindOfClass:[JXNSObjectWeakObjectContainer class]]) {
        storedObj = [(JXNSObjectWeakObjectContainer *)storedObj object];
    }
    return storedObj;
}

- (void)jx_setBindingBoolValue:(BOOL)value forKey:(NSString *)key {
    [self jx_setBindingObject:@(value) forKey:key];
}

- (BOOL)jx_bindingBoolValueForKey:(NSString *)key {
    return [[self jx_bindingObjectForKey:key] boolValue];
}

- (void)jx_setBindingCharValue:(char)value forKey:(NSString *)key {
    [self jx_setBindingObject:@(value) forKey:key];
}

- (char)jx_bindingCharValueForKey:(NSString *)key {
    return [[self jx_bindingObjectForKey:key] charValue];
}

- (void)jx_setBindingUnsignedCharValue:(unsigned char)value forKey:(NSString *)key {
    [self jx_setBindingObject:@(value) forKey:key];
}

- (unsigned char)jx_bindingUnsignedCharValueForKey:(NSString *)key {
    return [[self jx_bindingObjectForKey:key] unsignedCharValue];
}

- (void)jx_setBindingShortValue:(short)value forKey:(NSString *)key {
    [self jx_setBindingObject:@(value) forKey:key];
}

- (short)jx_bindingShortValueForKey:(NSString *)key {
    return [[self jx_bindingObjectForKey:key] shortValue];
}

- (void)jx_setBindingUnsignedShortValue:(unsigned short)value forKey:(NSString *)key {
    [self jx_setBindingObject:@(value) forKey:key];
}

- (unsigned short)jx_bindingUnsignedShortValueForKey:(NSString *)key {
    return [[self jx_bindingObjectForKey:key] unsignedShortValue];
}

- (void)jx_setBindingIntValue:(int)value forKey:(NSString *)key {
    [self jx_setBindingObject:@(value) forKey:key];
}

- (int)jx_bindingIntValueForKey:(NSString *)key {
    return [[self jx_bindingObjectForKey:key] intValue];
}

- (void)jx_setBindingUnsignedIntValue:(unsigned int)value forKey:(NSString *)key {
    [self jx_setBindingObject:@(value) forKey:key];
}

- (unsigned int)jx_bindingUnsignedIntValueForKey:(NSString *)key {
    return [[self jx_bindingObjectForKey:key] unsignedIntValue];
}

- (void)jx_setBindingLongValue:(long)value forKey:(NSString *)key {
    [self jx_setBindingObject:@(value) forKey:key];
}

- (long)jx_bindingLongValueForKey:(NSString *)key {
    return [[self jx_bindingObjectForKey:key] longValue];
}

- (void)jx_setBindingUnsignedLongValue:(unsigned long)value forKey:(NSString *)key {
    [self jx_setBindingObject:@(value) forKey:key];
}

- (unsigned long)jx_bindingUnsignedLongValueForKey:(NSString *)key {
    return [[self jx_bindingObjectForKey:key] unsignedLongValue];
}

- (void)jx_setBindingLongLongValue:(long long)value forKey:(NSString *)key {
    [self jx_setBindingObject:@(value) forKey:key];
}

- (long long)jx_bindingLongLongValueForKey:(NSString *)key {
    return [[self jx_bindingObjectForKey:key] longLongValue];
}

- (void)jx_setBindingUnsignedLongLongValue:(unsigned long long)value forKey:(NSString *)key {
    [self jx_setBindingObject:@(value) forKey:key];
}

- (unsigned long long)jx_bindingUnsignedLongLongValueForKey:(NSString *)key {
    return [[self jx_bindingObjectForKey:key] unsignedLongLongValue];
}

- (void)jx_setBindingFloatValue:(float)value forKey:(NSString *)key {
    [self jx_setBindingObject:@(value) forKey:key];
}

- (float)jx_bindingFloatValueForKey:(NSString *)key {
    return [[self jx_bindingObjectForKey:key] floatValue];
}

- (void)jx_setBindingDoubleValue:(double)value forKey:(NSString *)key {
    [self jx_setBindingObject:@(value) forKey:key];
}

- (double)jx_bindingDoubleValueForKey:(NSString *)key {
    return [[self jx_bindingObjectForKey:key] doubleValue];
}

- (void)jx_setBindingIntegerValue:(NSInteger)value forKey:(NSString *)key {
    [self jx_setBindingObject:@(value) forKey:key];
}

- (NSInteger)jx_bindingIntegerValueForKey:(NSString *)key {
    return [[self jx_bindingObjectForKey:key] integerValue];
}

- (void)jx_setBindingUnsignedIntegerValue:(NSUInteger)value forKey:(NSString *)key {
    [self jx_setBindingObject:@(value) forKey:key];
}

- (NSUInteger)jx_bindingUnsignedIntegerValueForKey:(NSString *)key {
    return [[self jx_bindingObjectForKey:key] unsignedIntegerValue];
}

- (void)jx_setBindingCGFloatValue:(CGFloat)value forKey:(NSString *)key {
    [self jx_setBindingObject:[NSNumber jx_numberWithCGFloat:value] forKey:key];
}

- (CGFloat)jx_bindingCGFloatValueForKey:(NSString *)key {
    return [[self jx_bindingObjectForKey:key] jx_CGFloatValue];
}

@end
