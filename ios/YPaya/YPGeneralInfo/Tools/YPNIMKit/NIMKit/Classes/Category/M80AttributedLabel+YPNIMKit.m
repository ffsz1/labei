//
//  M80AttributedLabel+YPNIMKit
//  NIM
//
//  Created by chris.
//  Copyright (c) 2015 Netease. All rights reserved.
//

#import "M80AttributedLabel+YPNIMKit.h"
#import "YPNIMInputEmoticonParser.h"
#import "YPNIMInputEmoticonManager.h"
#import "UIImage+YPNIMKit.h"

@implementation M80AttributedLabel (YPNIMKit)
- (void)nim_setText:(NSString *)text
{
    [self setText:@""];
    NSArray *tokens = [[YPNIMInputEmoticonParser currentParser] tokens:text];
    for (NIMInputTextToken *token in tokens)
    {
        if (token.type == NIMInputTokenTypeEmoticon)
        {
            NIMInputEmoticon *emoticon = [[YPNIMInputEmoticonManager sharedManager] emoticonByTag:token.text];
            UIImage *image = [UIImage nim_emoticonInKit:emoticon.filename];
            if (image)
            {
                [self appendImage:image
                          maxSize:CGSizeMake(18, 18)];
            }
        }
        else
        {
            NSString *text = token.text;
            [self appendText:text];
        }
    }
}
@end
