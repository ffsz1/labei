//
//  HJRoomManagerCell.m
//  HJLive
//
//  Created by apple on 2019/7/8.
//

#import "HJRoomManagerCell.h"

@implementation HJRoomManagerCell

- (IBAction)deleteAction:(id)sender {
    
    self.removeBlock(self.indexPath);
    
}

@end
