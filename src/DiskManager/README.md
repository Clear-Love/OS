# 文件管理模块设计

实现三种磁盘调度算法：先来先服务、最短寻道优先、电梯算法。  
**输入**：磁道服务顺序从指定的文本文件（TXT 文件）中取出。  
**输出**：第一行：磁道的服务顺序；第二行：显示移动总道数。

## 建立模型

- 一个磁盘调度器的抽象类作为不同算法的父类，不同的算法实现 schedule 方法
- 一个磁盘调度器包含以下属性：
    - 采用链表，因为调度过程中经常删除和插入
```java
//磁盘终点  
private final int endPosition;  
// 磁头位置  
public int currentPosition;  
//移动总道数  
public int totalMovement;  
//请求队列  
public LinkedList<Integer> requestSequence;
```


- 继承关系
    - serve 方法：访问该磁道然后从请求队列中移除该磁道
      ![](https://raw.githubusercontent.com/Clear-Love/image/main/image/DiskScheduler.png?token=ATVNUUFAUIIQ6B6SCNIVRATDVWLSA)
## 先来先服务（FCFS）磁盘调度算法
先来先服务（_First-Come，First-Served，FCFS_），顾名思义，先到来的请求，先被服务。


```java
@Override  
void schedule(){  
    // 依次服务每个磁道请求  
    while (!requestSequence.isEmpty()){  
        serve(requestSequence.get(0));  
    }  
  
    // 输出移动总道数  
    System.out.println("移动总道数：" + totalMovement);  
}
```

那按照这个序列的话：

98，183，37，122，14，124，65，67

那么，磁盘的写入顺序是从左到右，如下图：

![](https://pan.lmio.xyz/pic/51c7f2cc573d05408c9edfb1e7b557fe.png)

先来先服务算法总共移动了 `640` 个磁道的距离


## 最短寻道时间优先

最短寻道时间优先（_Shortest Seek First，SSF_）算法的工作方式是，优先选择从当前磁头位置所需寻道时间最短的请求

```java
@Override  
void schedule() {  
    while(!requestSequence.isEmpty()){  
        int request = removeReq();  
        serve(request);  
    }  
}  
  
/**  
 * @author lmio  
 * @description TODO 找到磁头最近的磁道，移除并返回  
 * @time 15:43 2022/12/29  
 * @name removeReq  
 * @returntype int  
 **/private int removeReq(){  
    // 使用二分查找找到大于等于目标值的最小值  
    int left = 0;  
    int right = requestSequence.size() - 1;  
    while(left < right){  
        int mid = (left + right) >> 1;  
        if(requestSequence.get(mid) < currentPosition){  
            left = mid + 1;  
        }else {  
            right = mid;  
        }  
    }  
  
    left = Math.max(0, right - 1);  
    //找到距离最近的  
    int req = (requestSequence.get(right) + requestSequence.get(left))  
            > 2 * currentPosition ? left : right;  
    return requestSequence.remove(req);  
}
```

- 先把请求队列排序，然后使用的二分查找的思想，找到离磁头最近的磁道，直到请求队列为空


![](https://pan.lmio.xyz/pic/434952e2850d7dfb28989945452abcca.png)

## 电梯算法

最短寻道时间优先算法会产生饥饿的原因在于：磁头有可能再一个小区域内来回得移动。

为了防止这个问题，可以规定：**磁头在一个方向上移动，访问所有未完成的请求，直到磁头到达该方向上的最后的磁道，才调换方向，这就是扫描（_Scan_）算法**。

这种算法也叫做电梯算法，比如电梯保持按一个方向移动，直到在那个方向上没有请求为止，然后改变方向。

```java
@Override  
void schedule() {  
    // 依次服务每个磁道请求  
    int index = requestVal();  
    while(!requestSequence.isEmpty()){  
        serve(requestSequence.get(index));  
        index = Math.max(0, index - 1);  
    }  
    // 输出移动总道数  
    System.out.println("移动总道数：" + totalMovement);  
}  
  
/**  
 * @author lmio  
 * @description TODO 查找磁头右边第一个要服务的磁道  
 * @time 16:54 2022/12/29  
 * @name requestVal  
 * @returntype int  
 **/int requestVal(){  
    // 使用二分查找找到小与等于目标值的最大值  
    int left = 0;  
    int right = requestSequence.size() - 1;  
    while(left < right){  
        int mid = (left + right + 1) >> 1;  
        if(requestSequence.get(mid) > currentPosition){  
            right = mid - 1;  
        }else {  
            left = mid;  
        }  
    }  
    return left;  
}

```

- 首先找到磁头左侧最近的磁道
- 磁头向磁道号减少的方向移动，直到磁盘尽头
- 往反方向继续扫描，直到请求队列为空


![](https://pan.lmio.xyz/pic/1c81d5a1f74d221f3bb8a5de278a032b.png)


## 循环扫描

扫描算法使得每个磁道响应的频率存在差异，那么要优化这个问题的话，可以总是按相同的方向进行扫描，使得每个磁道的响应频率基本一致。

循环扫描（_Circular Scan, CSCAN_ ）规定：只有磁头朝某个特定方向移动时，才处理磁道访问请求，而返回时直接快速移动至最靠边缘的磁道，也就是复位磁头，这个过程是很快的，并且**返回中途不处理任何请求**，该算法的特点，就是**磁道只响应一个方向上的请求**。

```java
@Override  
void schedule() {  
    // 依次服务每个磁道请求  
    int index = requestVal();  
    while(!requestSequence.isEmpty()){  
        if(index < requestSequence.size()){  
            serve(requestSequence.get(index));  
        }else {  
            serve(requestSequence.get(0));  
        }  
    }  
    // 输出移动总道数  
    System.out.println("移动总道数：" + totalMovement);  
}
```

该算法继承的电梯算法，实现过程也更加简单

- 首先找到磁头左侧最近的磁道的下标
- 访问并从请求队列移除下标对应磁道，直到下标达到队列尽头
- 把下标置为 0，继续访问并从请求队列移除下标对应磁道，直到队列为空

![](https://pan.lmio.xyz/pic/747e58b3c43e469d9d617fde973ff878.png)
