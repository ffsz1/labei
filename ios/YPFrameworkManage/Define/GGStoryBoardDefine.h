//
//  GGStoryBoardDefine.h
//  HJLive
//
//  Created by apple on 2018/11/15.
//  Copyright © 2018 XC. All rights reserved.
//

#ifndef GGStoryBoardDefine_h
#define GGStoryBoardDefine_h

#define GGLevelStoryBoard(VCName) [[UIStoryboard storyboardWithName:@"GGLevel" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]

#define GGAccompanyStoryBoard(VCName) [[UIStoryboard storyboardWithName:@"GGAccompany" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]


#define GGAuthBoard(VCName) [[UIStoryboard storyboardWithName:@"Auth" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]

#define GGFamilyBoard(VCName) [[UIStoryboard storyboardWithName:@"HJFamily" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:VCName]





#endif /* GGStoryBoardDefine_h */
