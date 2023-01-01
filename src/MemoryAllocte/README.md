# 存贮器管理模块设计

## 建立模型

- 设计一个类 `MemoryBlock` 表示内存空间块，包括以下属性

```java
public int startAddress;   // 起始地址  
public int size;           // 大小  
public boolean allocated;  // 是否已分配  
private String workName; //已分配的工程名
```

- 一个内存分配表 `MemoryTable` 包含以下属性

```java
public final LinkedList<MemoryBlock> blocks;  // 空间块列表  
  
private final String name;
```

- 一个内存调度的接口 `MemoryAllocatot` 抽象类，包含以下方法
    - 分配内存空间  `void allocate(int size, String workName)` 分配一段固定大小 `size` 的内存空间给 `workName`
    - 释放名为 `name` 的进程占有的内存 `void free (String name)`
    - 获取已分配的内存空间信息  `void showAllocatedMemory ()`
    - 获取未分配的内存空间信息  `void showFreeMemory()`

- 不同算法间的继承关系
  ![](https://pan.lmio.xyz/pic/2f8aa38a805188271206af490e516fc0.png)


## 固定分区

预先把可分配的内存空间分成若干个连续区域，每个区域的大小可以相同，也可以不同。  
分配时查找一个符合要求的分区即可，无须对分区分割、合并处理。分区的数量不会变化。优点是实现简单，缺点是浪费多、碎片多。

```java
@Override  
public void allocate(int size, String workName) {  
    MemoryBlock freeBlock = findFreeBlock(size);  
    if(freeBlock == null){  
        System.out.println("内存不足，分配失败，请释放内存后再试");  
    }else {  
        System.out.println("分配成功");  
        freeBlock.allocated =true;  
        freeBlock.setWorkName(workName);  
        allocateList.insert(freeBlock);  
        freeList.remove(freeBlock);  
    }  
}  
  
@Override  
public void free(String workName) {  
    MemoryBlock freedBlock = allocateList.remove(workName);  
  
    if(freedBlock == null){  
        System.out.println("释放内存块失败");  
        return;  
    }  
    freedBlock.allocated = false;  
    freeList.insert(freedBlock);  
}
```

- 顺序查找可用的空闲区，找到插入已分配表即可

## 可变分区

根据每个作业要求的实际大小分割一块空间，回收时如果与其他空闲空间连接时，须合并。优点是内存利用率高，缺点是实现复杂。

- 分配一个作业时，根据算法找到内存块，一部分分配给用户，另一部分保留为空闲块

```java
@Override  
public void allocate(int size, String workName) {  
    // 找到一个空闲块，并将其分割成两个部分  
    MemoryBlock block = findFreeBlock(size);  
    if (block == null) {  
        System.out.println("内存不足！");  
        return;  
    }  
  
    // 将空闲块分成两个部分，一部分分配给用户，另一部分保留为空闲块  
    MemoryBlock allocatedBlock = new MemoryBlock(block.startAddress, size);  
    allocatedBlock.setWorkName(workName);  
    block.startAddress += size;  
    block.size -= size;  
    System.out.println("进程" + workName + "分配成功:" + allocatedBlock);  
  
    // 将分配的内存块插入链表中  
    allocatedBlock.allocated = true;  
    allocateList.insert(allocatedBlock);  
}  
  
/**  
 * @author lmio 
 * @description TODO 插入排序  
 * @time 23:00 2023/1/1  
 * @name free * @returntype void **/@Override  
public void free(String name) {  
    // 找到要释放的内存块  
    MemoryBlock freedBlock = allocateList.remove(name);  
    if (freedBlock == null) {  
        System.out.println("Invalid address!");  
        return;  
    }  
    freedBlock.setWorkName("");  
    freedBlock.allocated = false;  
    //合并空闲区  
    if(!marge(freedBlock)){  
        //排序  
        insert_sort(freedBlock);  
    }  
  
}
```

### 最优适应算法 (BF)

**算法思想**：由于动态分区分配是一种连续分配方式，为各进程分配的空间必须是连续的一整片区域。因此为了保证当“大进程”到来时能有连续的大片空间，可以尽可能多地留下大片的空闲区，即**优先使用更小的空闲区**。  
**如何实现**：空闲分区**按容量递增次序链接**。每次分配内存时顺序查找**空闲分区链**（或**空闲分区表**），找到大小能满足要求的第一个空闲分区。

```java
public BF_MemoryAllocator(int totalSize) {  
    super(totalSize);  
    //按内存大小从小到大排序  
    this.comparator = Comparator.comparingInt(p -> p.size);  
}
```

### 首次适应算法（First Fit）

**算法思想**：每次都从低地址开始查找，找到第一个能满足大小的空闲分区。  
**如何实现**：**空闲分区以地址递增的次序排列**。每次分配内存时[顺序查找](https://so.csdn.net/so/search?q=%E9%A1%BA%E5%BA%8F%E6%9F%A5%E6%89%BE&spm=1001.2101.3001.7020)**空闲分区链**（或**空闲分区表**），找到大小能满足要求的第一个空闲分区。

```java
public FF_MemoryAllocator(int totalSize) {  
    super(totalSize);  
    this.comparator = Comparator.comparingInt(p -> p.startAddress);  
}
```

### 最坏适应算法 (Worst Fit)

又称**最大适应算法（Largest Fit）**  
**算法思想**：为了解决最佳适应算法的问题——即留下太多难以利用的小碎片，可以在**每次分配时优先使用最大的连续空闲区**，这样分配后剩余的空闲区就不会太小，更方便使用。  
**如何实现**：**空闲分区按容量递减次序链接**。每次分配内存时顺序查找**空闲分区链**（或**空闲分区表**），找到大小能满足要求的第一个空闲分区。

```java
public WF_MemoryAllocator(int totalSize) {  
    super(totalSize);  
    //按内存大小递减排序  
    this.comparator = (p1, p2) ->(p2.size - p1.size);  
}
```

### 邻近适应算法（Next Fit）

**算法思想**：首次适应算法每次都从链头开始查找的。这可能会导致低地址部分出现很多小的空闲分区，而每次分配查找时，都要经过这些分区，因此也增加了查找的开销。如果**每次都从上次查找结束的位置开始检索**，就能解决上述问题。  
**如何实现**：**空闲分区以地址递增的顺序排列**（可排成一个循环链表）。每次分配内存时**从上次查找结束的位置开始查找空闲分区链**（或空闲[分区表](https://so.csdn.net/so/search?q=%E5%88%86%E5%8C%BA%E8%A1%A8&spm=1001.2101.3001.7020)），找到大小能满足要求的第一个空闲分区

```java
// 当前遍历位置  
MemoryBlock now;  
public NF_MemoryAllocator(int totalSize) {  
    super(totalSize);  
    //按地址递增的方式排列  
    this.comparator = Comparator.comparingInt(p -> p.startAddress);  
    now = freeList.blocks.get(0);  
}  
  
@Override  
public MemoryBlock findFreeBlock(int size) {  
    if(size > getTotalSize() || size <= 0){  
        System.out.println("out of index!");  
        return null;    }  
    ListIterator<MemoryBlock> it = freeList.blocks.listIterator();  
    while(it.hasNext()){  
        if(it.next() == now)  
            break;  
    }  
    //从空闲区找到对应内存区，用迭代器模拟循环链表  
    MemoryBlock mb;  
    do {  
        if(!it.hasNext())  
            it = freeList.blocks.listIterator();  
        mb = it.next();  
        if (mb.size >= size) {  
            now = mb;  
            return mb;  
        }  
    }while (mb != now);  
    System.out.println("未找到该作业");  
    return null;}
```