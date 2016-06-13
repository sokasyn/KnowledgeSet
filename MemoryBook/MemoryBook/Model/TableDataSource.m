//
//  TableDataSource.m
//  MemoryBook
//
//  Created by Samson on 16/6/13.
//  Copyright © 2016年 Samson Tseng. All rights reserved.
//

#import "TableDataSource.h"

@interface TableDataSource()

@property (strong ,nonatomic) NSArray *items;
@property (copy ,nonatomic) NSString *cellIdentifier;
@property (copy ,nonatomic) CellConfigBlock cellConfigBlock;

@end

@implementation TableDataSource

@synthesize items = _items;
@synthesize cellIdentifier = _cellIdentifier;
@synthesize cellConfigBlock = _cellConfigBlock;

- (id)initWithItems:(NSArray *)items cellIdentifier:(NSString *)identifier configCellBlock:(CellConfigBlock)block{
    
    self = [super init];
    if(self){
        _items = items;
        _cellIdentifier = identifier;
        _cellConfigBlock = [block copy];
    }
    return self;
}

#pragma mark -UITableView DataSource
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    NSInteger rows = [self.items count];
    return rows;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    NSString *cellId = self.cellIdentifier;
    //    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:self.cellIdentifier forIndexPath:indexPath];
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellId];
    if(cell == nil){
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellId];
    }
    // 具体的cell的配置是由业务决定的,所以需要用户在创建这个datasource的时候传入block
    NSUInteger row = indexPath.row;
    id item = [self.items objectAtIndex:row];
    
    NSLog(@"row:%lu,content:%@",(unsigned long)row,item);
    self.cellConfigBlock(cell,item);
    return cell;
}

@end
