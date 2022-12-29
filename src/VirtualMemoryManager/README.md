

# 虚拟存储器管理模块设计

当 CPU 访问的页面不在物理内存时，便会产生一个缺页中断，请求操作系统将所缺页调入到物理内存。那它与一般中断的主要区别在于：

- 缺页中断在指令执行「期间」产生和处理中断信号，而一般中断在一条指令执行「完成」后检查和处理中断信号。
- 缺页中断返回到该指令的开始重新执行「该指令」，而一般中断返回回到该指令的「下一个指令」执行。

## 缺页中断的处理流程：

1. 在 CPU 里访问一条 Load M 指令，然后 CPU 会去找 M 所对应的页表项。
2. 如果找到的页表项在内存中，那 CPU 就可以直接去访问物理内存了，如果没有找到，CPU 就会发送缺页的中断请求
3. 操作系统收到缺页的中断请求，则会执行缺页中断处理函数，先会查找该页面在磁盘中的页面的位置。
4. 找到磁盘中对应的页面后，需要把该页面换入到物理内存中，但是在换入前，需要在物理内存中找空闲页，如果找到空闲页，就把页面换入到物理内存中。
5. 如果没有找到空闲页，就要使用页面置换算法，选择一个页面与磁盘进行置换


## 虚拟存储器管理流程：


![](https://raw.githubusercontent.com/Clear-Love/image/main/image/20221229233738.png?token=ATVNUUBV3HS3RKPSPYF6KKLDVW2IE)

## 建立模型

- 设计一个 Page 类表示页面

```java
private final int pageNum;  
// 状态位，用于表示该页是否有效  
//public boolean valid;  
// 访问字段，用于记录该页在一段时间被访问的次数  
public int accessCount;  
// 修改位，表示该页在调入内存后是否有被修改过，简单实现暂时忽略  
public boolean modified;  
// 页面内容  
private String content;
```

- 设计一个内存管理抽象类：`abstract class VirtualMemoryManager` 包含
    - `public final LinkedHashMap<Integer, Page> memoryPages;` 内存页表
    - `public final LinkedHashMap<Integer, Page> diskPages;` 磁盘页表
    - 页面访问队列从文件中读取
    - 抽象方法 run 子类实现该方法实现页面调度

- 数据结构
    - 需要根据页号快速查找页面，可以采用 HashMap，但是我需要保存内存页表的插入顺序，所以页表采用 LinkedHashMap

- 类的继承关系
  ![](https://raw.githubusercontent.com/Clear-Love/image/main/image/VirtualMemoryManager.png?token=ATVNUUH74BYTSUE6BC34DT3DVWK3C)

## 算法实现

### 先进先出置换算法 FIFO

**选择在内存驻留时间很长的页面进行中置换**


![](https://raw.githubusercontent.com/Clear-Love/image/main/image/20221229234813.png?token=ATVNUUCXEOHH42VHZGYCINLDVW3PY)
**实现**：每次置换选择内存表头（即驻留最长的页面）的元素置换


### 最近最久未使用的置换算法 LRU


最近最久未使用（_LRU_）的置换算法的基本思路是，发生缺页时，**选择最长时间没有被访问的页面进行置换**

![](https://raw.githubusercontent.com/Clear-Love/image/main/image/20221229235147.png?token=ATVNUUHWEC7SY763B6LVPHLDVW35E)


- 需要维护一个链表： `private final LinkedList<Integer> prevPages;` 最近最多使用的页面在表头，最近最少使用的页面在表尾。
- 链表长度为内存页面大小，若访问的页面在内存中可以找到，在链表中删除访问页面，移动的队列末尾，若在内存页面中找不到，则分为两种情况，若内存页表满了，置换链表头部的元素，访问页面插入链表尾部，若内存页表未满，访问页面直接插入链表尾


### 时钟页面置换算法

所有的页面都保存在一个类似钟面的「环形链表」中，一个表针指向最老的页面。

当发生缺页中断时，算法首先检查表针指向的页面：

- 如果它的访问位位是 0 就淘汰该页面，并把新的页面插入这个位置，然后把表针前移一个位置；
- 如果访问位是 1 就清除访问位，并把表针前移一个位置，重复这个过程直到找到了一个访问位为 0 的页面为止；

使用一个链表和一个指针实现循环量表指针加一时，指针大小除以链表长度取模，以达到循环的效果。

用一个布尔数组表示标志位


### 最不常用算法

最不常用（_LFU_）算法，它的意思不是指这个算法不常用，而是**当发生缺页中断时，选择「访问次数」最少的那个页面，并将其淘汰**。

- 记录每个页面被访问次数，当需要页面置换时，遍历内存页表，找到访问次数最少的页面置换