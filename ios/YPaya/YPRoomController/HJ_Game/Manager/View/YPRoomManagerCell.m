//
//  YPRoomManagerCell.m
//  HJLive
//
//  Created by apple on 2019/7/8.
//

#import "YPRoomManagerCell.h"

@implementation YPRoomManagerCell

- (IBAction)deleteAction:(id)sender {
    
    self.removeBlock(self.indexPath);
    
}

@end
