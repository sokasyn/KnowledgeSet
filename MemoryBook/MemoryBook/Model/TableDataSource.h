//
//  TableDataSource.h
//  MemoryBook
//
//  Created by Samson on 16/6/13.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//
/*
 *  实现对UITableViewDataSource的封装,cell的样式配置部分通过block的形式回传,实现业务逻辑与共通部分的解耦
 */

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

// 业务逻辑block
typedef void(^CellConfigBlock)(UITableViewCell *cell , id item);

@interface TableDataSource : NSObject<UITableViewDataSource>

- (id)initWithItems:(NSArray *)items cellIdentifier:(NSString *)identifier configCellBlock:(CellConfigBlock)block;

@end
